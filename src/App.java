import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Configuration conf = new Configuration();
        String[] pathArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        
        if (pathArgs.length < 2){
            System.exit(2);
        }
        
        Job job = Job.getInstance(conf, "MapReduce");

        job.setJarByClass(App.class);
        job.setMapperClass(IncidentMapper.class);
        job.setCombinerClass(IncidentReducer.class);
        job.setReducerClass(IncidentReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        Job jobratio = Job.getInstance(conf, "MapReduce");

        jobratio.setJarByClass(App.class);
        jobratio.setMapperClass(RatioIndicentMapper.class);
        jobratio.setCombinerClass(RatioIncidentReducer.class);
        jobratio.setReducerClass(RatioIncidentReducer.class);

        jobratio.setOutputKeyClass(Text.class);
        jobratio.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(jobratio, new Path(args[0]));
        FileOutputFormat.setOutputPath(jobratio, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class RatioIndicentMapper extends Mapper<LongWritable, Text, Text, Text> {
        private Text model = new Text();
        private Text year = new Text();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            if (line.contains("Date,StateOfOccurrence")) return;

            line = line.replaceAll("\"", "");
            String[] fields = line.split(",", -1);

            if (fields.length > 0) {
                try {
                    String date = fields[0];
                    String modelStr = fields[3];

                    if (!date.isEmpty() && !modelStr.isEmpty()) {
                        String yearStr = date.substring(0, 4);
                        model.set(modelStr);
                        year.set(yearStr);
                        context.write(model, year);
                    }
                } catch (Exception e) {
                    // Skip bad lines
                }
            }
        }
    }


    public static class RatioIncidentReducer extends Reducer<Text, Text, Text, DoubleWritable> {
        public void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {

            Set<String> years = new HashSet<>();
            int totalIncidents = 0;

            for (Text val : values) {
                years.add(val.toString());
                totalIncidents++;
            }

            int yearsActive = years.size();
            double ratio = yearsActive == 0 ? 0.0 : (double) totalIncidents / yearsActive;
            context.write(key, new DoubleWritable(ratio));
        }
    }

}
