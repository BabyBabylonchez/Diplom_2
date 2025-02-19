package client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Order;

import java.util.List;

import static utils.ApiConstants.ORDERS_ENDPOINT;

public class OrderClient {

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuthorization(String accessToken, Order order) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken) // Указываем, что передаем JSON
                .body(order) // RestAssured сам преобразует объект Order в JSON
                .post(ORDERS_ENDPOINT);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuthorization(Order order) {
        return RestAssured.given()
                //.log().all()  // Логируем весь запрос, чтобы проверить, что заголовок Authorization отсутствует
                .header("Content-Type", "application/json") // Указываем, что передаем JSON
                .body(order) // RestAssured сам преобразует объект Order в JSON
                .post(ORDERS_ENDPOINT);
    }

    @Step("Получение заказа с авторизацией")
    public Response getOrdersWithAuthorization(String accessToken) {
        return RestAssured.given()
                .header("Authorization", accessToken)
                .get(ORDERS_ENDPOINT);
    }

    @Step("Получение заказа без авторизации")
    public Response getOrdersWithoutAuthorization() {
        return RestAssured.given()
                .get(ORDERS_ENDPOINT);
    }

}
