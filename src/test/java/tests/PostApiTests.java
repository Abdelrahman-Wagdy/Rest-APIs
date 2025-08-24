package tests;


import base.TestBase;
import dataproviders.ObjectDataProvider;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.ObjectPayload;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.util.ArrayList;
import java.util.List;


@Epic("Objects API")
@Feature("POST")
public class PostApiTests extends TestBase {


    private final List<String> createdIds = new ArrayList<>();


    @Test(dataProvider = "createObjects", dataProviderClass = ObjectDataProvider.class,
            description = "TC05/06/07 – POST creates objects from JSON data", groups = {"smoke"})
    @Severity(SeverityLevel.BLOCKER)
    public void createObject(ObjectPayload payload) {
        Response r = client.post(objectsPath, payload);
        assertStatus(r, 200); // API returns 200 with createdAt
        String id = r.jsonPath().getString("id");
        Assert.assertNotNull(id);
        createdIds.add(id);


// TC06 – Validate echo of name
        Assert.assertEquals(r.jsonPath().getString("name"), payload.getName());


// TC07 – createdAt present
        Assert.assertNotNull(r.jsonPath().getString("createdAt"));
    }


    @Test(dependsOnMethods = "createObject", description = "TC08 – GET each created id returns the same name")
    public void verifyCreatedObjectsViaGet() {
        for (String id : createdIds) {
            Response r = client.get(objectsPath, id);
            assertStatus(r, 200);
            Assert.assertNotNull(r.jsonPath().getString("name"));
        }
    }


    public List<String> getCreatedIds() { return createdIds; }
}