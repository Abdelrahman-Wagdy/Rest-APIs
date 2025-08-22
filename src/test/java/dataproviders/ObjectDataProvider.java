package dataproviders;


import com.fasterxml.jackson.core.type.TypeReference;
import models.ObjectPayload;
import org.testng.annotations.DataProvider;
import utils.JsonUtils;


import java.util.List;


public class ObjectDataProvider {
    @DataProvider(name = "createObjects")
    public Object[][] createObjects() {
        List<ObjectPayload> items = JsonUtils.readListFromResource(
                "testdata/create_objects.json",
                new TypeReference<List<ObjectPayload>>() {}
        );
        return items.stream().map(i -> new Object[]{ i }).toArray(Object[][]::new);
    }
}