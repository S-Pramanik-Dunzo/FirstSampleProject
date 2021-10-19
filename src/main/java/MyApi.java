import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class MyApi {
    private final static String BASE_URL = "https://reqres.in/";
    MyApi() {
        RestAssured.baseURI = BASE_URL;
    }
    public Response register(String username,String password) {
       return given().param("email",username)
               .param("password",password)
               .when().get("api/register")
               .andReturn();
    }
    public Response login(String username,String password) {
        return given().param("email",username)
                .param("password",password)
                .when().get("api/login")
                .andReturn();
    }
}
