package tests;

import client.UserClient;
import model.User;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static org.hamcrest.CoreMatchers.is;

public class BaseTest {
    protected static UserClient userClient = new UserClient();
    protected static User registeredUser;

    @BeforeClass
    public static void setUpClass() {
        userClient = new UserClient();
        registeredUser = User.generateUniqueUser(); // Генерируем уникального пользователя
        Response response = userClient.createUser(registeredUser);

        System.out.println("Registered user: " + registeredUser.getEmail());
        //System.out.println("User registration response: " + response.asString());

        response.then().statusCode(200).body("success", is(true));

        // Сохраняем accessToken
        registeredUser.setAccessToken(response.jsonPath().getString("accessToken"));
    }

    @AfterClass
    public static void tearDown() {
        if (registeredUser != null && registeredUser.getAccessToken() != null) {
            System.out.println("Deleting user " + registeredUser.getName());// + " with accessToken: " + registeredUser.getAccessToken());
            Response response = userClient.deleteUser(registeredUser.getAccessToken());

            System.out.println("User deletion response: " + response.asString());

            response.then().statusCode(202).body("success", is(true));
        }
    }
}

