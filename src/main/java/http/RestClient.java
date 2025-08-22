package http;


import config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.given;


public class RestClient {
    private final RequestSpecification spec;


    public RestClient() {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.baseUrl())
                .setContentType(ContentType.JSON)
                .build();
    }


    public Response get(String path) { return given().spec(spec).when().get(path); }
    public Response get(String path, String id) { return given().spec(spec).when().get(path + "/" + id); }
    public Response getWithQuery(String path, String query) { return given().spec(spec).when().get(path + query); }
    public Response post(String path, Object body) { return given().spec(spec).body(body).when().post(path); }
    public Response delete(String path, String id) { return given().spec(spec).when().delete(path + "/" + id); }
}