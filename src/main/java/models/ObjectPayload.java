package models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectPayload {
    private String id; // returned from API
    private String name;


    @JsonProperty("data")
    private Map<String, Object> data;


    public ObjectPayload() {}
    public ObjectPayload(String name, Map<String, Object> data) {
        this.name = name; this.data = data;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}