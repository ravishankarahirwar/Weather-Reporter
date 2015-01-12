package weatherreporter.dataclasses;

/**
 * Created by Ravi on 1/12/2015.
 */
public class Main {
    public String temp;
    public String temp_min;
    public String temp_max;
    public String pressure;
    public String humidity;
    public String city;
    public Main(String temp, String temp_min, String temp_max, String pressure, String humidity) {
        this.temp = temp;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
    }



    public String getTemperature() {
        return temp;
    }

    public void setTemperature(String temp) {
        this.temp = temp;
    }

    public String getMinimumTemperature() {
        return temp_min;
    }

    public void setMinimumTemperature(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getMaximumTemperature() {
        return temp_max;
    }

    public void setMaximumTemperature(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
