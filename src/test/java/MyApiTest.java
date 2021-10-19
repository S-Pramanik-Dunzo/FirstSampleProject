
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
public class MyApiTest {
    private final static String EMAIL = "eve.holt@reqres.in";
    private final static String PASSWORD = "pistol";

    @BeforeTest
    public void set_baseUrl() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    public void registration_successful() {
        Map<String,String> credential = new HashMap<>();
        credential.put("password",PASSWORD);
        credential.put("email",EMAIL);
        JSONObject json = new JSONObject(credential);
        int statusCode = given()
                .header("Content-Type", "application/json")
                .body(json.toJSONString())
                .when()
                .post("/api/register")
                .getStatusCode();
        Assert.assertEquals(statusCode,HttpStatus.SC_OK,"Wrong status code received");
    }
    @Test
    public void registration_unsuccessful() {
        Map<String,String> credential = new HashMap<>();
        credential.put("email",EMAIL);
        JSONObject json = new JSONObject(credential);
        Response response = given()
                .header("Content-Type", "application/json")
                .body(json.toJSONString())
                .when()
                .post("/api/register")
                .andReturn();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(response.getStatusCode(),HttpStatus.SC_BAD_REQUEST,"Wrong status code received");
        Assert.assertEquals(jsonPath.get("error"),"Missing password","Wrong error message provided");
    }
    @Test
    public void login_successful() {
        Map<String,String> credential = new HashMap<>();
        credential.put("password",PASSWORD);
        credential.put("email",EMAIL);
        JSONObject json = new JSONObject(credential);
        int statusCode = given()
                .header("Content-Type","application/json")
                .body(json.toJSONString())
                .when()
                .post("/api/login")
                .getStatusCode();
        Assert.assertEquals(statusCode,HttpStatus.SC_OK,"Wrong status code received");
    }
    @Test
    public void login_unsuccessful() {
        Map<String,String> credential = new HashMap<>();
        credential.put("email",EMAIL);
        JSONObject json = new JSONObject(credential);
        Response response = given()
                .header("Content-Type", "application/json")
                .body(json.toJSONString())
                .when()
                .post("/api/login")
                .andReturn();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(response.getStatusCode(),HttpStatus.SC_BAD_REQUEST,"Wrong status code received");
        Assert.assertEquals(jsonPath.get("error"),"Missing password","Wrong error message provided");
    }
    @Test
    public void list_user() {
        ArrayList<String> expectedUsers = new ArrayList<>();
        expectedUsers.add("michael.lawson@reqres.in");
        expectedUsers.add("lindsay.ferguson@reqres.in");
        expectedUsers.add("tobias.funke@reqres.in");
        expectedUsers.add("byron.fields@reqres.in");
        expectedUsers.add("george.edwards@reqres.in");
        expectedUsers.add("rachel.howell@reqres.in");
        Response response = given().when().get("/api/users?page=2").andReturn();
        List<Map<String,String>> data = response.jsonPath().getList("data");

        ArrayList<String> actualUsers = new ArrayList<>();
        for (Map<String,String> map:data) {
            actualUsers.add(map.get("email"));
        }
        Assert.assertEquals(actualUsers,expectedUsers,"One or more username not valid");
    }
}

