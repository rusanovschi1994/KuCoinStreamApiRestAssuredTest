package apiTest;

import org.junit.Assert;
import org.junit.Test;
import pojo.TickerCustomData;
import pojo.TickerData;
import pojo.util.Specification;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pojo.util.*;

import static io.restassured.RestAssured.given;

public class KuCoinApiTickerTest {

    private static final String URL = "https://api.kucoin.com/";

    public List<TickerData> getTickers(){

        Specification.installSpecification(Specification.requestSpec(URL), Specification.responseSpec(200));

        return given()
                .when()
                .get("api/v1/market/allTickers")
                .then().log().all()
                .extract().body().jsonPath().getList("data.ticker", TickerData.class);

    }

    @Test
    public void checkCrypto(){

        List<TickerData> tickers = getTickers()
                .stream().filter(x -> x.getSymbol().endsWith("USDT")).collect(Collectors.toList());

        Assert.assertTrue(tickers.stream().allMatch(x->x.getSymbol().endsWith("USDT")));
    }

    @Test
    public void getRateHighToLow(){

        List<TickerData> tickers = getTickers()
                .stream().filter(x->x.getSymbol().endsWith("USDT")).sorted(new Comparator<TickerData>() {
                    @Override
                    public int compare(TickerData o1, TickerData o2) {
                        return o2.getChangeRate().compareTo(o1.getChangeRate());
                    }
                }).collect(Collectors.toList());

        List<TickerData> top10HighToLow = tickers.stream().limit(10).collect(Collectors.toList());

        Assert.assertEquals("HIDOODLES-USDT", top10HighToLow.get(0).getSymbol());
    }

    @Test
    public void getRateLowToHigh(){

        List<TickerData> tickers = getTickers()
                .stream().filter(x->x.getSymbol().endsWith("USDT"))
                .sorted(new TickerComparatorLow()).collect(Collectors.toList());

        List<TickerData> top10LowToHigh = tickers.stream().limit(10).collect(Collectors.toList());

        Assert.assertEquals("LTC3S-USDT", top10LowToHigh.get(1).getSymbol());
    }

    @Test
    public void getTickerDataToCustomPojo(){

        List<TickerCustomData> tickers = new ArrayList<>();

        getTickers().forEach(x->tickers.add(new TickerCustomData(x.getSymbol(), Float.parseFloat(x.getChangeRate()))));

        List<TickerCustomData> customDataTickers = tickers
                .stream().filter(x->x.getName().endsWith("BTC")).sorted(new Comparator<TickerCustomData>() {
                    @Override
                    public int compare(TickerCustomData o1, TickerCustomData o2) {
                        return o2.getChangeRate().compareTo(o1.getChangeRate());
                    }
                }).collect(Collectors.toList());

        int i = 5;
        Assert.assertEquals("AKRO-BTC", customDataTickers.get(0).getName());
    }
}
