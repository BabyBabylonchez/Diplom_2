package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserUpdateTest extends BaseTest {

    private String accessToken = registeredUser.getAccessToken();
    String newName = "UpdatedName";
    String newEmail = "updated_" + registeredUser.getEmail();

    @Test
    @DisplayName("Обновление данных пользователя будучи авторизованным")
    public void testUpdateUserDataWithAuthorization() {
        System.out.println("Sending request with new data: email=" + newEmail + ", name=" + newName);

        Response response = userClient.updateUser(accessToken, newEmail, newName);

        System.out.println("Update user response: " + response.asString());

        response.then().statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(newEmail))
                .body("user.name", equalTo(newName));

        /*registeredUser.setEmail(newEmail);
        registeredUser.setName(newName);*/
    }

    @Test
    @DisplayName("Обновление данных пользователя не будучи авторизованным")
    public void testUpdateUserDataWithoutAuthorization() {
        Response response = userClient.updateUser("", "123"+newEmail, newName+"123");

        System.out.println("Update user without auth response: " + response.asString());

        response.then().statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Смена email пользователя на уже зарегистрированный email")
    public void testUpdateUserDataWithExistingEmail() {
        // Создаём второго пользователя
        User anotherUser = User.generateUniqueUser();
        Response createAnotherUserResponse = userClient.createUser(anotherUser);
        createAnotherUserResponse.then().statusCode(200);
        String existingEmail = anotherUser.getEmail();

        Response response = userClient.updateUser(accessToken, existingEmail, "NewName");

        System.out.println("Update user with existing email response:" + response.asString());

        response.then().statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User with such email already exists"));

        // Удаляем созданного пользователя после теста
        userClient.deleteUser(createAnotherUserResponse.jsonPath().getString("accessToken"));
    }
}
