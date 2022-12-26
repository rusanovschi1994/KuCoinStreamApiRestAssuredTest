package pojo.util;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specification {

    public static RequestSpecification requestSpec(String url){

        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(url)
                .build();
    }

    public static ResponseSpecification responseSpec(int status){

        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .build();
    }

    public static void installSpecification(RequestSpecification request,
                                     ResponseSpecification response){

        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }
}
