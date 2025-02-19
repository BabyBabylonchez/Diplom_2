package tests;

import io.qameta.allure.junit4.DisplayName;
import model.User;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserLoginTest extends BaseTest {

    @Test
    @DisplayName("Авторизация с валидными данными")
    public void testLoginWithValidCredentials() {
        Response response = userClient.loginUser(registeredUser);

        System.out.println("Login response: " + response.asString());

        response.then().statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(registeredUser.getEmail()))
                .body("user.name", equalTo(registeredUser.getName()));
    }

    @Test
    @DisplayName("Авторизация с невалидными данными")
    public void testLoginWithInvalidCredentials() {
        User invalidUser = new User("invalid@yandex.ru", "wrongpassword", "Username");
        Response response = userClient.loginUser(invalidUser);

        System.out.println("Login with invalid credentials response: " + response.asString());

        response.then().statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
