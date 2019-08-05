package biocoe.fims.evolution.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author rjewing
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvolutionRecordReference {
    public String recordGuid;

    public EvolutionRecordReference(String recordGuid) {
        this.recordGuid = recordGuid;
    }
}
