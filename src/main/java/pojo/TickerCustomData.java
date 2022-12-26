package pojo;

public class TickerCustomData {

    private String name;
    private float changeRate;

    public TickerCustomData() {
    }

    public TickerCustomData(String name, float changeRate) {
        this.name = name;
        this.changeRate = changeRate;
    }

    public String getName() {
        return name;
    }

    public Float getChangeRate() {
        return changeRate;
    }
}
