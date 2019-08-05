package biocoe.fims.evolution.processing;

import biocoe.fims.evolution.models.EvolutionRecordReference;
import biocoe.fims.evolution.service.EvolutionService;

import java.util.List;

/**
 * @author rjewing
 */
public class EvolutionDiscoveryTask implements Runnable {

    private final EvolutionService evolutionService;
    private final List<EvolutionRecordReference> references;

    public EvolutionDiscoveryTask(EvolutionService evolutionService, List<EvolutionRecordReference> references) {
        this.evolutionService = evolutionService;
        this.references = references;
    }

    @Override
    public void run() {
        evolutionService.discovery(references);
    }

}

