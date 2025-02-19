package tests;

import io.qameta.allure.junit4.DisplayName;
import model.User;
import io.restassured.response.Response;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserRegistrationTest extends BaseTest{

    @Test
    @DisplayName("Регистрация уже существующего пользователя")
    public void testCreateExistingUser() {
        // Проверяем, что пользователь был успешно создан
        Response response = userClient.createUser(registeredUser);

        System.out.println("Create existing user response: " + response.asString());

        response.then().statusCode(403) // Ожидаем ошибку, так как пользователь уже существует
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    public void testCreateUserWithoutName() {
        User user = new User(registeredUser.getEmail(), registeredUser.getPassword(), null);
        Response response = userClient.createUser(user);

        // Логгирование ответа сервера
        System.out.println("Create user without name response: " + response.asString());

        response.then().statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}