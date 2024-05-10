package utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.*;
import java.util.Properties;

public class Utils {

    public RequestSpecification requestSpecification() throws IOException {
        PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
        return new RequestSpecBuilder()
                .setBaseUri(getGlobalValue("baseURL"))
                .addHeader("Content-Type", "application/json")
                .addQueryParam("key", "qaclick")
                .log(LogDetail.ALL)
                .addFilter(RequestLoggingFilter.logRequestTo(log))
                .addFilter(ResponseLoggingFilter.logResponseTo(log))
                .build();
    }

    public ResponseSpecification responseSpecification200(){
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.ALL)
                .log(LogDetail.ALL)
                .build();
    }

    public static String getGlobalValue(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/java/utilities/global.properties");
        prop.load(fis);
        String value = prop.getProperty(key);
        return value;
    }

}
