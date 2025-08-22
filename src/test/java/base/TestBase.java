package base;


import config.ConfigManager;
import http.RestClient;
import io.qameta.allure.Attachment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;


public abstract class TestBase {
    protected RestClient client;
    protected String objectsPath;


    @BeforeClass
    public void setupSuite() {
        this.client = new RestClient();
        this.objectsPath = ConfigManager.objectsPath();
    }


    @AfterMethod(alwaysRun = true)
    protected void attachIfFailed(Object[] params) { /* placeholder for extra hooks */ }


    @Attachment(value = "response.json", type = "application/json")
    protected byte[] attachResponse(Response r) { return r.asByteArray(); }


    protected void assertStatus(Response r, int code) {
        attachResponse(r);
        Assert.assertEquals(r.statusCode(), code, "Unexpected status: " + r.asString());
    }
}