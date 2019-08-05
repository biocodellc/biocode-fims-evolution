package biocoe.fims.application.config;

import biocode.fims.repositories.EntityIdentifierRepository;
import biocode.fims.repositories.RecordRepository;
import biocoe.fims.evolution.processing.EvolutionTaskExecutor;
import biocoe.fims.evolution.service.EvolutionService;
import biocoe.fims.run.EvolutionDatasetAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.Executors;

/**
 * Configuration class for biocode-fims-evolution application.
 */
@Configuration
@PropertySource(value = "classpath:biocode-fims-evolution.props", ignoreResourceNotFound = true)
@Import({EvolutionProperties.class})
public class EvolutionAppConfig {
    @Autowired
    EvolutionProperties evolutionProperties;

    @Autowired
    RecordRepository recordRepository;
    @Autowired
    EntityIdentifierRepository entityIdentifierRepository;

    @Bean
    public EvolutionService evolutionService() {
        return new EvolutionService(ClientBuilder.newClient(), evolutionProperties);
    }

    @Bean
    public EvolutionTaskExecutor evolutionTaskExecutor() {
        return new EvolutionTaskExecutor(Executors.newFixedThreadPool(5));
    }

    public EvolutionDatasetAction evolutionDatasetAction() {
        return new EvolutionDatasetAction(recordRepository, evolutionService(), evolutionTaskExecutor(), entityIdentifierRepository, evolutionProperties);
    }
}
