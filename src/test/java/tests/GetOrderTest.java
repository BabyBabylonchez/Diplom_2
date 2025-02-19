package tests;

import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class GetOrderTest extends BaseTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void testGetOrdersWithAuthorization() {
        // Создаём заказ от имени зарегистрированного пользователя
        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f");
        Order order = new Order(ingredients);
        orderClient.createOrderWithAuthorization(registeredUser.getAccessToken(), order);

        // Получаем заказы пользователя
        Response response = orderClient.getOrdersWithAuthorization(registeredUser.getAccessToken());

        System.out.println("Get orders with authorization response: " + response.asString());

        // Проверяем, что запрос успешный и содержит заказы
        response.then().statusCode(200)
                .body("success", is(true))
                .body("orders", not(empty()))
                .body("total", greaterThanOrEqualTo(1))
                .body("totalToday", greaterThanOrEqualTo(1));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void testGetOrdersWithoutAuthorization() {
        // Отправляем запрос без токена
        Response response = orderClient.getOrdersWithoutAuthorization();

        System.out.println("Get orders without authorization response: " + response.asString());

        // Проверяем, что сервер отвечает 401 Unauthorized
        response.then().statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }
}

