import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class Example {
    @Test(description = "Получение всех телефонов")
    public void testGetAllPhones() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("cat", "phone");
        given()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
        .when()
                .post("https://api.demoblaze.com/bycat")
        .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/get-all-phones-schema.json"))
                .body("Items.id", hasItems(1, 2, 3, 4, 5, 6, 7))
                .statusCode(200).log().all();
    }

    @Test(description = "Получение всех ноутбуков")
    public void testGetAllLaptops() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("cat", "notebook");
        given()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
                .when()
                .post("https://api.demoblaze.com/bycat")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/get-all-laptops-schema.json"))
                .body("Items.id", hasItems(8, 9, 11, 12, 13, 15))
                .statusCode(200).log().all();
    }

    @Test(description = "Получение всех мониторов")
    public void testGetAllMonitors() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("cat", "monitor");
        given()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
                .when()
                .post("https://api.demoblaze.com/bycat")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/get-all-monitors-schema.json"))
                .body("Items.id", hasItems(10, 14))
                .statusCode(200).log().all();
    }

    @Test(description = "Авторизация незарегестрированного пользователя")
    public void testUnregisteredUserAuthorization() {
        JSONObject requestParams = new JSONObject();

        requestParams.put("username", "Hitler2287");
        requestParams.put("password", "pass");

        given()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
        .when()
                .post("https://api.demoblaze.com/login")
        .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/unregistered-user-authorization-schema.json"))
                .body("errorMessage", equalTo("User does not exist."))
                .statusCode(200).log().all();
    }

    @Test(description = "Авторизация пользователя с неверным паролем")
    public void testWrongPasswordAuthorization() {
        JSONObject requestParams = new JSONObject();

        requestParams.put("username", "user");
        requestParams.put("password", "user");

        given()
                .contentType(ContentType.JSON)
                .body(requestParams.toJSONString())
                .when()
                .post("https://api.demoblaze.com/login")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/wrong-password-authorization-schema.json"))
                .body("errorMessage", equalTo("Wrong password."))
                .statusCode(200).log().all();
    }
}
