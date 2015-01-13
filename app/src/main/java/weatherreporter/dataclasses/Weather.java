package weatherreporter.dataclasses;

/**
 * Created by acer on 1/12/2015.
 */
public class Weather {
    public String main;
    public String description;

    public Weather() {
    }

    public Weather(String main, String description) {
        this.main = main;
        this.description = description;
    }

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
