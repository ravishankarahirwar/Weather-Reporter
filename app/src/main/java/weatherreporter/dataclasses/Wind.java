package weatherreporter.dataclasses;

/**
 * Created by acer on 1/12/2015.
 */
public class Wind {
    private String speed;
    private String deg;
    private String gust;

    public Wind() {
    }

    public Wind(String speed, String deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public String getGust() {
        return gust;
    }

    public void setGust(String gust) {
        this.gust = gust;
    }
}
