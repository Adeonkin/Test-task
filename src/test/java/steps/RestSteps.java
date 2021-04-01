package steps;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import steps.pojo.NewUserRest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.Base64;
import java.util.List;
import java.util.Random;


public class RestSteps {

    public static String baseUrl = "http://test-api.d6.dev.devcaz.com";
    public static String baseUserName = "front_2d6b0a8391742f5d789d7d915755e09e";
    public String token;
    public String pass = encodedString();
    public String userName = generateString(8);

    @Given("^get user token$")
    public void getToken() {
        token = given()
                .baseUri(baseUrl)
                .auth().preemptive().basic(baseUserName, "")
                .body("{\n" +
                        "\"grant_type\":\"client_credentials\",\n" +
                        "\"scope\":\"guest:default\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .post("/v2/oauth2/token")
                .then()
                .log().all()
                .statusCode(200)
                .assertThat().body("token_type", equalTo("Bearer"))
                .assertThat().body("expires_in", equalTo(86400))
                .assertThat().body("access_token", notNullValue())
                .extract().jsonPath().get("access_token");
        System.out.println(token);

    }

    @Given("^registration new user$")
    public void registrationNewPlayer() {
        List<NewUserRest> respons = given()
                .baseUri(baseUrl)
                .auth().preemptive().oauth2(token)
                .body("{\n" +
                        "\"username\": \"" + userName + "\",\n" +
                        "\"password_change\": \"" + pass + "\",\n" +
                        "\"password_repeat\": \"" + pass + "\",\n" +
                        "\"email\": \"" + userName + "@example.com\",\n" +
                        "\"name\": \"Tester\",\n" +
                        "\"surname\": \"Tester\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .log().all()
                .post("/v2/players")
                .then()
                .log().all()
                .statusCode(201)
                .extract().jsonPath().getList("data", NewUserRest.class);
    }


    @Given("^login$")
    public void login() {
        RestAssured.given()
                .baseUri(baseUrl)
                .auth().basic(userName, "janedoe123")
                .body("{\n" +
                        "\"grant_type\":\"password\",\n" +
                        "\"username\": \"" + userName + "\",\n" +
                        "\"password\": \"" + pass + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
                .log().all()
                .post("/v2/oauth2/token")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Given("^generate String length (\\d+) and save as \"([^\"]+)\"$")
    public void getNumber(int length, String stashKey) {
        String result = generateString(length);
        System.out.println("generate String '" + result + "' and save as '" + stashKey + "'");
    }

    public String encodedString() {
        String encodedString = Base64.getEncoder().encodeToString(generateString(10).getBytes());
        return encodedString;
    }

    public String generateString(int number) {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = number;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

}