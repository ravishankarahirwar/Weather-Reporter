package weatherreporter.dataclasses;

/**
 * Created by ravi on 23/1/15.
 */
public class ForecastData extends Main{
    public ForecastData() {
    }
    public String windSpeed;
    public String deg;

    public int imageId;

    public String dt;
    public String day;
    public String night;
    public String eve;
    public String morn;

    public String main;
    public String description;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
