import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class IncidentMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final IntWritable one = new IntWritable(1);
    private Text yearCountry = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        // Skip header
        if (line.contains("Date,StateOfOccurrence")) return;

        // Remove extra quotes and split
        line = line.replaceAll("\"", "");
        String[] fields = line.split(",", -1);

        if (fields.length > 0) {
            try {
                String date = fields[0];
                String country = fields[1];

                if (!date.isEmpty() && !country.isEmpty()) {
                    String year = date.substring(0, 4);
                    yearCountry.set(year + "_" + country);
                    context.write(yearCountry, one);
                }
            } catch (Exception e) {
                // Handle or log malformed lines
            }
        }
    }
}
