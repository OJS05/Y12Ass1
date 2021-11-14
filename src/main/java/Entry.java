import com.opencsv.bean.CsvBindByName;

public class Entry {

    @CsvBindByName(column = "Country")
    public String country;

    @CsvBindByName(column = "Continent")
    public Continent continent;

    @CsvBindByName(column = "Date")
    public int date;

    @CsvBindByName(column = "GDP")
    public int gdp;

    @CsvBindByName(column = "GNI")
    public int gni;

    @CsvBindByName(column = "Live births number")
    public int liveBirthsNumber;

    @CsvBindByName(column = "Live births rate")
    public float liveBirthsRate;

    @CsvBindByName(column = "Deaths number")
    public int deathsNumber;

    @CsvBindByName(column = "Death rate")
    public float deathRate;

    @CsvBindByName(column = "Rate of increase")
    public float rateOfIncrease;

    @CsvBindByName(column = "Infant deaths number")
    public int infantDeathsNumber;

    @CsvBindByName(column = "Infant deaths rate (per 1000 births)")
    public float infantDeathsRate;

    @CsvBindByName(column = "Life expectancy at birth male")
    public float lifeExpectancyMale;

    @CsvBindByName(column = "Life expectancy at birth female")
    public float lifeExpectancyFemale;

    @CsvBindByName(column = "Total fertility rate")
    public float totalFertilityRate;
}
