package ch.ubique.backend.monitoring.service;

import ch.ubique.backend.monitoring.check.StatusCheckService;
import ch.ubique.backend.monitoring.check.models.CheckLevel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@SuppressWarnings("unused")
public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    private final RestClient restClient;
    private final String heartbeatEndpoint;
    private final String heartbeatFailEndpoint;
    private final StatusCheckService statusCheckService;

    public MonitoringService(
            final RestClient restClient,
            final String heartbeatEndpoint,
            final StatusCheckService statusCheckService) {
        this.restClient = restClient;
        this.heartbeatEndpoint = heartbeatEndpoint;
        this.heartbeatFailEndpoint =
                UriComponentsBuilder.fromUriString(heartbeatEndpoint)
                        .pathSegment("fail")
                        .build()
                        .toUri()
                        .toString();
        this.statusCheckService = statusCheckService;
        logger.debug(
                "Configured MonitoringService with heartbeat urls:\nbaseurl {}\nfail-url {}",
                this.heartbeatEndpoint,
                heartbeatFailEndpoint);
    }

    public void heartbeat() {
        final var results = statusCheckService.runChecks(CheckLevel.ERROR);
        if (results.allPassed()) {
            restClient.get().uri(heartbeatEndpoint).retrieve().toBodilessEntity();
        } else {
            restClient
                    .post()
                    .uri(heartbeatFailEndpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(results)
                    .retrieve()
                    .toBodilessEntity();
        }
    }
}
