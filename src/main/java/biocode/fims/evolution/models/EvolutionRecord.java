package biocode.fims.evolution.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author rjewing
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvolutionRecord {
    public String guid;
    public String url;
    public Map<String, Object> data;
    public String eventId;
    public String userId;

    public EvolutionRecord(String guid, String url, Map<String, Object> data, String eventId, String userId) {
        this.guid = guid;
        this.url = url;
        this.data = data;
        this.eventId = eventId;
        this.userId = userId;
    }
}
