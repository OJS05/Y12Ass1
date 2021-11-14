import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String CSV_FILE_PATH = "src/main/resources/data.csv";
    public static final String CSV_FILE_PATH_OUTPUT = "src/main/resources/data_output.csv";

    //public static final String CSV_FILE_PATH = "./data.csv";
    //public static final String CSV_FILE_PATH_OUTPUT = "./data_output.csv";

    public static int NUMBER_OF_ENTRIES;

    public static void main(String[] args) throws IOException, CsvException {
        /*
        var file = readFile(CSV_FILE_PATH);

        ArrayList<Float> entries = new ArrayList<>();
        file.forEach(Entry -> entries.add(Entry.infantDeathsRate));
        float[] array = new float[entries.size()];
        int i = 0;
        for (Float f : entries) {
            array[i++] = f;
        }
        System.out.println(calculateMean(array));

         */
        System.out.println("Enter the number of entries you want to sample between 2 - 105: ");
        Scanner input = new Scanner(System.in);
        NUMBER_OF_ENTRIES = input.nextInt();

        polishData(CSV_FILE_PATH, CSV_FILE_PATH_OUTPUT);

    }

    public static List<Entry> readFile(String fileName) throws FileNotFoundException {
        List<Entry> entries = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(Entry.class)
                .build()
                .parse();
        return entries;
    }

    public static String calculateMean(float[] entries) {
        try {
            double x = 0;
            int n = 0;
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != 0.0f) {
                    x += entries[i];
                    n += 1;
                }
            }
            return String.valueOf((float) x / n);
        } catch (ArithmeticException e) {
            return "division by zero";
        }
    }

    public static String calculateMean(int[] entries) {
        try {
            int x = 0;
            int n = 0;
            for (int i = 0; i < entries.length; i++) {
                if (entries[i] != 0) {
                    x += entries[i];
                    n += 1;
                }
            }
            return String.valueOf(x/n);
        } catch (ArithmeticException e) {
            return "division by zero";
        }
    }

    public static int calculateMedian(int[] entries) {
        int n = entries.length;
        Arrays.sort(entries);
        if (n % 2 == 0) {
            return (entries[n / 2] + entries[n / 2 + 1]) / 2;
        }else{
            return entries[n / 2];
        }
    }

    public static float calculateMedian(float[] entries) {
        int n = entries.length;
        Arrays.sort(entries);
        if (n % 2 == 0) {
            return (entries[n / 2] + entries[n / 2 + 1]) / 2;
        }else{
            return entries[n / 2];
        }
    }

    public static void polishData(String fileToRead, String fileToWrite) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(fileToRead));
        reader.skip(1);
        List<String[]> entries = reader.readAll();
        List<String[]> polishedEntries = sampleReservoir(entries, NUMBER_OF_ENTRIES);
        CSVWriter writer = new CSVWriter(new FileWriter(fileToWrite));
        String[] header = {"Country", "Continent", "Date", "GDP", "GNI", "Live births number", "Live births rate", "Deaths number", "Death rate", "Rate of increase", "Infant deaths number", "Infant deaths rate (per 1000 births)", "Life expectancy at birth male", "Life expectancy at birth female", "Total fertility rate" };
        writer.writeNext(header);
        writer.writeAll(polishedEntries);
        writer.close();

        //tryRead(CSV_FILE_PATH_OUTPUT);
        analyse(readFile(CSV_FILE_PATH_OUTPUT));
    }

    public static void tryRead(String filename) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
        }
    }

    public static List<String[]> sampleReservoir(List<String[]> entries, int k) {
        int n = entries.size();
        List<String[]> reservoir = new ArrayList<>();
        int i = 0;

        while (i < k) {
            // choose random index between 0 and n
            int random = (int) (Math.random() * n) % n;
            boolean found = false;
            // check if the random index is already in the reservoir
            for (int j = 0; j < i; j++) {
                if (reservoir.get(j) == entries.get(random)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                reservoir.add(entries.get(random));
                i++;
            }

        }

        return reservoir;

    }

    public static void analyse(List<Entry> entries) {
        var lists = splitByContinent(entries);

        int totalIndex = 0;
        int[] totalGDP = new int[lists.size()];
        int[] totalGNI = new int[lists.size()];
        int[] totalLiveBirthsNumber = new int[lists.size()];
        float[] totalLiveBirthsRate = new float[lists.size()];
        int[] totalDeathsNumber = new int[lists.size()];
        float[] totalDeathRate = new float[lists.size()];
        float[] totalRateOfIncrease = new float[lists.size()];
        int[] totalInfantDeathsNumber = new int[lists.size()];
        float[] totalInfantDeathsRate = new float[lists.size()];
        float[] totalLifeExpectancyAtBirthMale = new float[lists.size()];
        float[] totalLifeExpectancyAtBirthFemale = new float[lists.size()];
        float[] totalTotalFertilityRate = new float[lists.size()];

        for (List<Entry> list : lists) {
            Continent continent = null;
            int i = 0;

            int[] gdp = new int[list.size()];
            int[] gni = new int[list.size()];
            int[] liveBirthsNumber = new int[list.size()];
            float[] liveBirthsRate = new float[list.size()];
            int[] deathsNumber = new int[list.size()];
            float[] deathsRate = new float[list.size()];
            float[] rateOfIncrease = new float[list.size()];
            int[] infantDeathsNumber = new int[list.size()];
            float[] infantDeathsRate = new float[list.size()];
            float[] lifeExpectancyAtBirthMale = new float[list.size()];
            float[] lifeExpectancyAtBirthFemale = new float[list.size()];
            float[] totalFertilityRate = new float[list.size()];

            for (Entry entry : list) {
                continent = entry.continent;
                gdp[i] = entry.gdp;
                gni[i] = entry.gni;
                liveBirthsNumber[i] = entry.liveBirthsNumber;
                liveBirthsRate[i] = entry.liveBirthsRate;
                deathsNumber[i] = entry.deathsNumber;
                deathsRate[i] = entry.deathRate;
                rateOfIncrease[i] = entry.rateOfIncrease;
                infantDeathsNumber[i] = entry.infantDeathsNumber;
                infantDeathsRate[i] = entry.infantDeathsRate;
                lifeExpectancyAtBirthMale[i] = entry.lifeExpectancyMale;
                lifeExpectancyAtBirthFemale[i] = entry.lifeExpectancyFemale;
                totalFertilityRate[i] = entry.totalFertilityRate;
                i++;
            }

            totalGDP[totalIndex] = Integer.parseInt(calculateMean(gdp));
            totalGNI[totalIndex] = Integer.parseInt(calculateMean(gni));
            totalLiveBirthsNumber[totalIndex] = Integer.parseInt(calculateMean(liveBirthsNumber));
            totalLiveBirthsRate[totalIndex] = Float.parseFloat(calculateMean(liveBirthsRate));
            totalDeathsNumber[totalIndex] = Integer.parseInt(calculateMean(deathsNumber));
            totalDeathRate[totalIndex] = Float.parseFloat(calculateMean(deathsRate));
            totalRateOfIncrease[totalIndex] = Float.parseFloat(calculateMean(rateOfIncrease));
            totalInfantDeathsNumber[totalIndex] = Integer.parseInt(calculateMean(infantDeathsNumber));
            totalInfantDeathsRate[totalIndex] = Float.parseFloat(calculateMean(infantDeathsRate));
            totalLifeExpectancyAtBirthMale[totalIndex] = Float.parseFloat(calculateMean(lifeExpectancyAtBirthMale));
            totalLifeExpectancyAtBirthFemale[totalIndex] = Float.parseFloat(calculateMean(lifeExpectancyAtBirthFemale));
            totalTotalFertilityRate[totalIndex] = Float.parseFloat(calculateMean(totalFertilityRate));
            totalIndex++;

            System.out.println("Continent: " + continent);
            System.out.println("x̄ GDP: " + calculateMean(gdp));
            System.out.println("x̃ GDP: " + calculateMedian(gdp));
            System.out.println("x̄ GNI: " + calculateMean(gni));
            System.out.println("x̃ GNI: " + calculateMedian(gni));
            System.out.println("x̄ Live births number: " + calculateMean(liveBirthsNumber));
            System.out.println("x̃ Live births number: " + calculateMedian(liveBirthsNumber));
            System.out.println("x̄ Live births rate: " + calculateMean(liveBirthsRate));
            System.out.println("x̃ Live births rate: " + calculateMedian(liveBirthsRate));
            System.out.println("x̄ Deaths number: " + calculateMean(deathsNumber));
            System.out.println("x̃ Deaths number: " + calculateMedian(deathsNumber));
            System.out.println("x̄ Deaths rate: " + calculateMean(deathsRate));
            System.out.println("x̃ Deaths rate: " + calculateMedian(deathsRate));
            System.out.println("x̄ Rate of increase: " + calculateMean(rateOfIncrease));
            System.out.println("x̃ Rate of increase: " + calculateMedian(rateOfIncrease));
            System.out.println("x̄ Infant deaths number: " + calculateMean(infantDeathsNumber));
            System.out.println("x̃ Infant deaths number: " + calculateMedian(infantDeathsNumber));
            System.out.println("x̄ Infant deaths rate: " + calculateMean(infantDeathsRate));
            System.out.println("x̃ Infant deaths rate: " + calculateMedian(infantDeathsRate));
            System.out.println("x̄ Life expectancy at birth male: " + calculateMean(lifeExpectancyAtBirthMale));
            System.out.println("x̃ Life expectancy at birth male: " + calculateMedian(lifeExpectancyAtBirthMale));
            System.out.println("x̄ Life expectancy at birth female: " + calculateMean(lifeExpectancyAtBirthFemale));
            System.out.println("x̃ Life expectancy at birth female: " + calculateMedian(lifeExpectancyAtBirthFemale));
            System.out.println("x̄ Total fertility rate: " + calculateMean(totalFertilityRate));
            System.out.println("x̃ Total fertility rate: " + calculateMedian(totalFertilityRate));
            System.out.println("Number of countries analysed: " + i);
            System.out.println("\n");
        }

        System.out.println("Total");
        System.out.println("x̄ GDP: " + calculateMean(totalGDP));
        System.out.println("x̃ GDP: " + calculateMedian(totalGDP));
        System.out.println("x̄ GNI: " + calculateMean(totalGNI));
        System.out.println("x̃ GNI: " + calculateMedian(totalGNI));
        System.out.println("x̄ Live births number: " + calculateMean(totalLiveBirthsNumber));
        System.out.println("x̃ Live births number: " + calculateMedian(totalLiveBirthsNumber));
        System.out.println("x̄ Live births rate: " + calculateMean(totalLiveBirthsRate));
        System.out.println("x̃ Live births rate: " + calculateMedian(totalLiveBirthsRate));
        System.out.println("x̄ Deaths number: " + calculateMean(totalDeathsNumber));
        System.out.println("x̃ Deaths number: " + calculateMedian(totalDeathsNumber));
        System.out.println("x̄ Deaths rate: " + calculateMean(totalDeathRate));
        System.out.println("x̃ Deaths rate: " + calculateMedian(totalDeathRate));
        System.out.println("x̄ Rate of increase: " + calculateMean(totalRateOfIncrease));
        System.out.println("x̃ Rate of increase: " + calculateMedian(totalRateOfIncrease));
        System.out.println("x̄ Infant deaths number: " + calculateMean(totalInfantDeathsNumber));
        System.out.println("x̃ Infant deaths number: " + calculateMedian(totalInfantDeathsNumber));
        System.out.println("x̄ Infant deaths rate: " + calculateMean(totalInfantDeathsRate));
        System.out.println("x̃ Infant deaths rate: " + calculateMedian(totalInfantDeathsRate));
        System.out.println("x̄ Life expectancy at birth male: " + calculateMean(totalLifeExpectancyAtBirthMale));
        System.out.println("x̃ Life expectancy at birth male: " + calculateMedian(totalLifeExpectancyAtBirthMale));
        System.out.println("x̄ Life expectancy at birth female: " + calculateMean(totalLifeExpectancyAtBirthFemale));
        System.out.println("x̃ Life expectancy at birth female: " + calculateMedian(totalLifeExpectancyAtBirthFemale));
        System.out.println("x̄ Total fertility rate: " + calculateMean(totalTotalFertilityRate));
        System.out.println("x̃ Total fertility rate: " + calculateMedian(totalTotalFertilityRate));
        System.out.println("Total number of countries analysed: " + NUMBER_OF_ENTRIES);

    }

    public static List<List<Entry>> splitByContinent(List<Entry> entries) {
        List<List<Entry>> result = new ArrayList<>();
        List<Entry> asia = new ArrayList<>();
        List<Entry> europe = new ArrayList<>();
        List<Entry> africa = new ArrayList<>();
        List<Entry> northAmerica = new ArrayList<>();
        List<Entry> southAmerica = new ArrayList<>();
        List<Entry> oceania = new ArrayList<>();

        for (Entry entry : entries) {
            if (entry.continent == Continent.ASIA) {
                asia.add(entry);
            } else if (entry.continent == Continent.EUROPE) {
                europe.add(entry);
            } else if (entry.continent == Continent.AFRICA) {
                africa.add(entry);
            } else if (entry.continent == Continent.NORTH_AMERICA) {
                northAmerica.add(entry);
            } else if (entry.continent == Continent.SOUTH_AMERICA) {
                southAmerica.add(entry);
            } else if (entry.continent == Continent.OCEANIA) {
                oceania.add(entry);
            }
        }

        result.add(asia);
        result.add(europe);
        result.add(africa);
        result.add(northAmerica);
        result.add(southAmerica);
        result.add(oceania);

        return result;
    }
}