package etna.myweather;


public class DailyModal {
    private String time;
    private String temperature;
    private String icon;
    private String windSpeed;
    private String cond;



    public DailyModal(String time, String temperature, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.cond = cond;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}