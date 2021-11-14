public enum Continent {
    ASIA ("Asia"),
    EUROPE ("Europe"),
    AFRICA ("Africa"),
    NORTH_AMERICA ("North America"),
    SOUTH_AMERICA ("South America"),
    OCEANIA ("Oceania");

    private String displayName;

    Continent(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
