package pojo.executables;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.pojoClasses.e2eSpecbuilder.*;
import pojo.pojoClasses.e2eSpecbuilder.requestCreateOrder.OrderDetails;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class E2ESpecbuilder {

    ResponseLogin responseLogin = new ResponseLogin();
    ResponseCreateOrder responseCreateOrder = new ResponseCreateOrder();
    String productId = "";

    @Test(priority = 0)
    public void login() {

        RequestLogin requestLogin = new RequestLogin();
        requestLogin.setUserEmail("restassuredtest@gmail.com");
        requestLogin.setUserPassword("Password1.");

        RequestSpecification reqsSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .setBody(requestLogin)
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .expectStatusCode(200)
                .build();

        responseLogin = RestAssured.given()
                .spec(reqsSpec)
                .when()
                .post("/api/ecom/auth/login")
                .then()
                .spec(resSpec)
                .extract()
                .response().as(ResponseLogin.class);

        System.out.println("token - " + responseLogin.getToken());
        System.out.println("userId - " + responseLogin.getUserId());
        System.out.println("message - " + responseLogin.getMessage());

    }

    @Test(priority = 1)
    public void createProduct(){

        ObjectMapper objectMapper = new ObjectMapper();
        RequestCreateProduct requestCreateProduct = new RequestCreateProduct();
        requestCreateProduct.setProductName("qwerty");
        requestCreateProduct.setProductAddedBy(responseLogin.getUserId());
        requestCreateProduct.setProductCategory("fashion");
        requestCreateProduct.setProductSubCategory("shirts");
        requestCreateProduct.setProductPrice("10000");
        requestCreateProduct.setProductDescription("Adidas");
        requestCreateProduct.setProductFor("women");

        Map<String, Object> map = objectMapper.convertValue(requestCreateProduct, Map.class);

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Authorization", responseLogin.getToken())
                .addMultiPart("productImage",new File("src/main/resources/superstar.jpg"))
                .addFormParams(map)
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .log(LogDetail.PARAMS)
                .build();

        String response = RestAssured.given()
                .spec(reqSpec)
                .when()
                .post("/api/ecom/product/add-product")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        productId = js.get("productId");
        System.out.println("productId: " + productId);

    }

    @Test(priority = 3)
    public void createOrder(){

        RequestCreateOrder requestCreateOrder = new RequestCreateOrder();
        List<OrderDetails> listOrderDetails = new ArrayList<>();

        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1.setProductOrderedId(productId);
        orderDetails1.setCountry("Philippines");
        listOrderDetails.add(orderDetails1);

//        OrderDetails orderDetails2 = new OrderDetails();
//        orderDetails2.setProductOrderedId("6581cade9fd99c85e8ee7ff5");
//        orderDetails2.setCountry("Philippines");
//        listOrderDetails.add(orderDetails2);

        requestCreateOrder.setOrders(listOrderDetails);

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", responseLogin.getToken())
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .setBody(requestCreateOrder)
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .build();

        responseCreateOrder = RestAssured.given()
                .spec(reqSpec)
                .when()
                .post("/api/ecom/order/create-order")
                .then()
                .spec(resSpec)
                .log().all()
                .extract().response().as(ResponseCreateOrder.class);

        System.out.println(responseCreateOrder.getOrders());
        System.out.println(responseCreateOrder.getProductOrderId());
        System.out.println(responseCreateOrder.getMessage());
    }

    @Test(priority = 4)
    public void deleteProduct(){

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", responseLogin.getToken())
                .addPathParam("productId", productId)
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .build();

        String response = RestAssured.given()
                .spec(reqSpec)
                .when()
                .delete("/api/ecom/product/delete-product/{productId}")
                .then()
                .spec(resSpec)
                .log().all()
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String message = js.get("message");
        Assert.assertEquals("Product Deleted Successfully", message);

    }

    @Test(priority = 5)
    public void deleteOrder(){

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", responseLogin.getToken())
                .addPathParam("orderId", responseCreateOrder.getOrders().get(0))
                .log(LogDetail.ALL)
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.HEADERS)
                .log(LogDetail.BODY)
                .build();

        String response = RestAssured.given()
                .spec(reqSpec)
                .when()
                .delete("/api/ecom/order/delete-order/{orderId}")
                .then()
                .spec(resSpec)
                .log().all()
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String message = js.get("message");
        Assert.assertEquals("Orders Deleted Successfully", message);

    }


}
