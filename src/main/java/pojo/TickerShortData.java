package pojo;

public class TickerShortData {

    private String name;
    private float changeRate;

    public TickerShortData() {
    }

    public TickerShortData(String name, float changeRate) {
        this.name = name;
        this.changeRate = changeRate;
    }

    public String getName() {
        return name;
    }

    public float getChangeRate() {
        return changeRate;
    }
}
