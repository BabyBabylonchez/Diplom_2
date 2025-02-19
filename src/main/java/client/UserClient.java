package client;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static utils.ApiConstants.*;


public class UserClient {

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(REGISTER_ENDPOINT);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken) // Передаем accessToken в заголовке
                .when()
                .delete(USER_ENDPOINT); // Добавить USER_ENDPOINT
    }

    @Step("Обновление данных пользователя")
    public Response updateUser(String accessToken, String newEmail, String newName) {
        // Создаём объект с новыми данными пользователя
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", newEmail);
        requestBody.put("name", newName);

        // Преобразуем объект в JSON
        String jsonBody = new Gson().toJson(requestBody);

        return given()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .patch(USER_ENDPOINT);
    }
}