package biocode.fims.evolution.processing;

import biocode.fims.bcid.BcidBuilder;
import biocode.fims.models.Expedition;
import biocode.fims.records.Record;
import biocode.fims.evolution.models.EvolutionRecord;
import biocode.fims.evolution.service.EvolutionService;
import biocode.fims.service.ExpeditionService;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author rjewing
 */
public class EvolutionUpdateCreateTask implements Runnable {

    private final EvolutionService evolutionService;
    private final ExpeditionService expeditionService;
    private final BcidBuilder bcidBuilder;
    private final List<Record> newRecords;
    private final List<Record> updatedRecords;
    private final String resolverEndpoint;
    private final Map<MultiKey<String>, String> userCache = new HashMap<>();
    private String userURIPrefix;

    public EvolutionUpdateCreateTask(EvolutionService evolutionService, ExpeditionService expeditionService, BcidBuilder bcidBuilder, List<Record> newRecords,
                                     List<Record> updatedRecords, String resolverEndpoint, String userURIPrefix) {
        this.evolutionService = evolutionService;
        this.expeditionService = expeditionService;
        this.bcidBuilder = bcidBuilder;
        this.newRecords = newRecords;
        this.updatedRecords = updatedRecords;
        this.resolverEndpoint = resolverEndpoint;
        this.userURIPrefix = userURIPrefix;
    }

    @Override
    public void run() {
        String eventId = UUID.randomUUID().toString();

        List<EvolutionRecord> evolutionRecords = newRecords.stream()
                .map(record -> {
                    String bcid = bcidBuilder.build(record);
                    String userId = getUserId(record.projectId(), record.expeditionCode());
                    return new EvolutionRecord(bcid, resolverEndpoint + bcid, record.properties(), eventId, userId);
                }).collect(Collectors.toList());

        ListUtils.partition(evolutionRecords, 1000).forEach(evolutionService::create);

        evolutionRecords = updatedRecords.stream()
                .map(record -> {
                    String bcid = bcidBuilder.build(record);
                    String userId = getUserId(record.projectId(), record.expeditionCode());
                    return new EvolutionRecord(bcid, resolverEndpoint + bcid, record.properties(), eventId, userId);
                }).collect(Collectors.toList());

        ListUtils.partition(evolutionRecords, 1000).forEach(evolutionService::update);
    }

    private String getUserId(int projectId, String expeditionCode) {
        MultiKey<String> key = new MultiKey<>(String.valueOf(projectId), expeditionCode);

        if (userCache.containsKey(key)) return userCache.get(key);

        Expedition expedition = expeditionService.getExpedition(expeditionCode, projectId);
        userCache.put(key, userURIPrefix + String.valueOf(expedition.getUser().getUserId()));
        return userCache.get(key);
    }

}

