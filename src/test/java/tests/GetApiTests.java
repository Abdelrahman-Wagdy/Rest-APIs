package tests;


import base.TestBase;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


@Epic("Objects API")
@Feature("GET")
public class GetApiTests extends TestBase {


    @Test(description = "TC01 – GET all objects returns 200 and non-empty array")
    @Severity(SeverityLevel.CRITICAL)
    public void getAllObjects() {
        Response r = client.get(objectsPath);
        assertStatus(r, 200);
        Assert.assertTrue(r.jsonPath().getList("$").size() > 0, "Expected non-empty list");
    }


    @Test(description = "TC02 – GET single known object by id=1 contains name")
    public void getSingleObject() {
        Response r = client.get(objectsPath, "1");
        assertStatus(r, 200);
        Assert.assertNotNull(r.jsonPath().getString("name"));
    }


    @Test(description = "TC03 – GET multiple objects by ids query returns exact count")
    public void getObjectsByIds() {
// API supports: /objects?id=3&id=5&id=10
        Response r = client.getWithQuery(objectsPath, "?id=3&id=5&id=10");
        assertStatus(r, 200);
        Assert.assertEquals(r.jsonPath().getList("$").size(), 3);
    }


    @Test(description = "TC04 – GET non-existing id returns 404")
    public void getMissingObject() {
        Response r = client.get(objectsPath, "999999999");
        Assert.assertTrue(r.statusCode() == 404 || r.statusCode() == 400,
                "Expected 404/400 for missing object, got: " + r.statusCode());
    }
}