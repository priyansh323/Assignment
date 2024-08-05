package constants;

public enum ProjectConstants    {
    CHROME_DRIVER_PATH("C:\\Users\\Acer1\\OneDrive\\Documents\\chromedriver-win64\\chromedriver.exe"),
    TRIP_TYPE_ROUND("Round Trip"),
    DESTINATION_DUBAI("Dubai"),
    DESTINATION_FROM("Bangalore"),
    MMT_URL("https://makemytrip.com/");
    private final String projectConstant;

    public String get(){
        return this.projectConstant;
    }

    private ProjectConstants(String projectConstant){
        this.projectConstant = projectConstant;
    }

}
