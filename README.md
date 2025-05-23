# ✈️ Analyse des Incidents Aériens avec Hadoop MapReduce

Ce projet Hadoop MapReduce permet d'analyser des données d'incidents aériens issues de fichiers CSV, afin d'identifier :
1. 📊 Le **nombre d’incidents par an et par pays**
2. 📈 Le **taux de croissance annuel des incidents**

---

## 🗂 Structure des Données

Les fichiers CSV contiennent des informations détaillées sur chaque incident. Voici la description des colonnes principales :

| Colonne              | Description                                                                 |
|----------------------|-----------------------------------------------------------------------------|
| `Date`               | Date de l’incident (format ISO : `YYYY-MM-DD`)                              |
| `StateOfOccurrence`  | Pays dans lequel l’incident s’est produit                                   |
| `Location`           | Ville ou aéroport                                                            |
| `Model`              | Modèle de l’aéronef (ex: `BOEING 737`)                                       |
| `Registration`       | Immatriculation de l’aéronef                                                 |
| `Operator`           | Compagnie aérienne opérant l’appareil                                        |
| `StateOfOperator`    | Pays de l’opérateur                                                          |
| `StateOfRegistry`    | Pays de l’immatriculation                                                    |
| `FlightPhase`        | Phase de vol (ex: `Take-off`, `Landing`)                                     |
| `Class`              | Type d’événement (`Incident`, `Serious incident`)                            |
| `Fatalities`         | Nombre de décès enregistrés                                                  |
| `Over2250`           | Avion > 2 250 kg (booléen)                                                   |
| `Over5700`           | Avion > 5 700 kg (booléen)                                                   |
| `ScheduledCommercial`| Vol commercial programmé (booléen)                                          |
| `InjuryLevel`        | Gravité des blessures (`None`, `Minor`, `Serious`, etc.)                    |
| `TypeDesignator`     | Désignation de type (abréviation de modèle, ex: `A320`, `B37M`)              |
| `Helicopter`         | Est-ce un hélicoptère ? (booléen)                                           |
| `Airplane`           | Est-ce un avion ? (booléen)                                                 |
| `Engines`            | Nombre de moteurs                                                            |
| `EngineType`         | Type de moteur (`Jet`, `Turboprop`, `Piston`, etc.)                          |
| `Official`           | Données officielles (souvent vide ou booléen)                               |
| `OccCats`            | Catégories d’occurrence (ex: `[F-NI, SCF-PP]`)                               |
| `Risk`               | Niveau de risque (abréviation : `SCF`, `OTH`, etc.)                         |
| `Year`               | Année extraite de la date de l’incident (ex: `2010`)                         |

---

## ⚙️ Exécution du Projet

### 1. 📁 Préparer les données
Copiez vos fichiers locaux vers HDFS :

```bash
hdfs dfs -mkdir -p /user/yourname/incidents
hdfs dfs -put /chemin/vers/fichiers/*.csv /user/yourname/incidents/

### 1. 📁 Lancer les job
hadoop jar /chemin/vers/IncidentAnalysis.jar chemin/vers/incidents.csv output/
