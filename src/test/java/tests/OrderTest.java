package tests;

import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

public class OrderTest extends BaseTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void testCreateOrderWithAuthorization() {
        // Создаём заказ с правильными ингредиентами
        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f");
        Order order = new Order(ingredients);

        // Создаём заказ с токеном авторизации
        Response response = orderClient.createOrderWithAuthorization(registeredUser.getAccessToken(), order);

        System.out.println("Create order with authorization response: " + response.asString());

        // Проверяем успешный статус и правильность данных в ответе
        response.then().statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void testCreateOrderWithoutAuthorization() {
        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f");
        Order order = new Order(ingredients);

        Response response = orderClient.createOrderWithoutAuthorization(order);

        System.out.println("Create order without authorization response: " + response.asString());

        // Если API теперь разрешает заказы без авторизации, проверяем 200 OK
        response.then().statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
    }


    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    public void testCreateOrderWithoutIngredientsWithAuthorization() {
        // Создаём заказ с пустым списком ингредиентов
        List<String> ingredients = new ArrayList<>();
        Order order = new Order(ingredients);

        // Попытка создать заказ с пустым списком ингредиентов, но с авторизацией
        Response response = orderClient.createOrderWithAuthorization(registeredUser.getAccessToken(), order);

        System.out.println("Create order without ingredients with authorization response: " + response.asString());

        // Проверяем, что возвращается ошибка 400 (плохой запрос)
        response.then().statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов неавторизованным пользователем")
    public void testCreateOrderWithoutIngredientsWithoutAuthorization() {
        // Создаём заказ без ингредиентов
        List<String> ingredients = new ArrayList<>();
        Order order = new Order(ingredients);

        // Попытка создать заказ без авторизации
        Response response = orderClient.createOrderWithoutAuthorization(order);

        System.out.println("Create order without ingredients without authorization response: " + response.asString());

        // Проверяем, что возвращается ошибка 400
        response.then().statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неправильным хешем ингредиентов авторизованным пользователем")
    public void testCreateOrderWithInvalidIngredientHashes() {
        // Создаём заказ с невалидными хешами ингредиентов
        List<String> ingredients = Arrays.asList("invalid_hash_1", "invalid_hash_2");
        Order order = new Order(ingredients);

        // Попытка создать заказ с неверными хешами ингредиентов
        Response response = orderClient.createOrderWithAuthorization(registeredUser.getAccessToken(), order);

        System.out.println("Create order with invalid ingredients hashes with authorization response: " + response.asString());

        // Проверяем, что возвращается ошибка 500 (внутренняя ошибка сервера)
        response.then().statusCode(500);
    }
}
