package biocode.fims.evolution.processing;

import biocode.fims.evolution.models.EvolutionRecordReference;
import biocode.fims.evolution.service.EvolutionService;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

/**
 * @author rjewing
 */
public class EvolutionRetrievalTask implements Runnable {

    private final EvolutionService evolutionService;
    private final List<EvolutionRecordReference> references;

    public EvolutionRetrievalTask(EvolutionService evolutionService, List<EvolutionRecordReference> references) {
        this.evolutionService = evolutionService;
        this.references = references;
    }

    @Override
    public void run() {
        ListUtils.partition(references, 10000).forEach(evolutionService::retrieval);
    }

}

