package biocoe.fims.evolution.processing;

import biocode.fims.bcid.BcidBuilder;
import biocode.fims.records.Record;
import biocode.fims.utils.RecordHasher;
import biocoe.fims.evolution.models.EvolutionRecord;
import biocoe.fims.evolution.service.EvolutionService;
import org.apache.commons.collections4.ListUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author rjewing
 */
public class EvolutionUpdateCreateTask implements Runnable {

    private final EvolutionService evolutionService;
    private final BcidBuilder bcidBuilder;
    private final List<Record> newRecords;
    private final List<Record> updatedRecords;
    private final String resolverEndpoint;

    public EvolutionUpdateCreateTask(EvolutionService evolutionService, BcidBuilder bcidBuilder, List<Record> newRecords,
                                     List<Record> updatedRecords, String resolverEndpoint) {
        this.evolutionService = evolutionService;
        this.bcidBuilder = bcidBuilder;
        this.newRecords = newRecords;
        this.updatedRecords = updatedRecords;
        this.resolverEndpoint = resolverEndpoint;
    }

    @Override
    public void run() {
        String eventId = UUID.randomUUID().toString();

        List<EvolutionRecord> evolutionRecords = newRecords.stream()
                .map(record -> {
                    String bcid = bcidBuilder.build(record);
                    return new EvolutionRecord(bcid, resolverEndpoint + bcid, record.properties(), eventId);
                }).collect(Collectors.toList());

        ListUtils.partition(evolutionRecords, 1000).forEach(evolutionService::create);

        evolutionRecords = updatedRecords.stream()
                .map(record -> {
                    String bcid = bcidBuilder.build(record);
                    return new EvolutionRecord(bcid, resolverEndpoint + bcid, record.properties(), eventId);
                }).collect(Collectors.toList());

        ListUtils.partition(evolutionRecords, 1000).forEach(evolutionService::update);
    }

}

