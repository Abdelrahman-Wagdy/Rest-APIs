package tests.t1;


import base.TestBase;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


@Feature("DELETE")
public class DeleteApiTests extends TestBase {


    // For demo, create then delete inline (TC09, TC10)
    @Test(description = "TC09 – Create a temp object, then DELETE it returns 200/204")
    public void deleteCreatedObject() {
        var temp = java.util.Map.of(
                "name", "Temp To Delete",
                "data", java.util.Map.of("purpose", "delete-demo")
        );
        var create = client.post(objectsPath, temp);
        assertStatus(create, 200);
        String id = create.jsonPath().getString("id");


        Response del = client.delete(objectsPath, id);
        Assert.assertTrue(del.statusCode() == 200 || del.statusCode() == 204,
                "DELETE expected 200/204, got: " + del.statusCode());


// TC10 – Subsequent GET should be 404/400
        Response getAfter = client.get(objectsPath, id);
        Assert.assertTrue(getAfter.statusCode() == 404 || getAfter.statusCode() == 400,
                "Expected missing after delete, got: " + getAfter.statusCode());
    }
}