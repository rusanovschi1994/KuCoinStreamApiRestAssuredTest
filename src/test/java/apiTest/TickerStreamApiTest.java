package apiTest;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;
import pojo.TickerData;
import pojo.TickerShortData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class TickerStreamApiTest {

    public List<TickerData> getTickers(){

       return given()
                .when()
                .contentType(ContentType.JSON)
                .get("https://api.kucoin.com/api/v1/market/allTickers")
                .then()
                .extract().body().jsonPath().getList("data.ticker", TickerData.class);
    }

    @Test
    public void checkCrypto(){

        List<TickerData> tickers = getTickers()
                .stream().filter(x->x.getSymbol().endsWith("USDT")).collect(Collectors.toList());

        Assert.assertTrue(tickers.stream().allMatch(x->x.getSymbol().endsWith("USDT")));
    }

    @Test
    public void sortHighToLow(){

        List<TickerData> tickersHighToLow = getTickers()
                .stream().filter(x->x.getSymbol().endsWith("USDT")).sorted(new Comparator<TickerData>() {
                    @Override
                    public int compare(TickerData o1, TickerData o2) {

                        float result = o2.getChangeRate().compareTo(o1.getChangeRate());

                        return (int) result;
                    }
                }).collect(Collectors.toList());

        List<TickerData> top10 = tickersHighToLow.stream().limit(10).collect(Collectors.toList());

        Assert.assertEquals(top10.get(1).getSymbol(), "HNT-USDT");
    }

    @Test
    public void sortLowToHigh(){

        List<TickerData> tickersLowToHigh = getTickers()
                .stream().filter(x->x.getSymbol().endsWith("USDT"))
                .sorted(new TickerComparatorLow())
                .collect(Collectors.toList());
        List<TickerData> top10 = tickersLowToHigh.stream().limit(10).collect(Collectors.toList());

        Assert.assertEquals(top10.get(0).getSymbol(),"HIMEEBITS-USDT");
    }

    @Test
    public void createCryptoMap(){

        List<TickerShortData> tickers = new ArrayList<>();

        getTickers().forEach(x->tickers.add(new TickerShortData(x.getSymbol(), Float.parseFloat(x.getChangeRate()))));

        int i = 9;
    }
}
