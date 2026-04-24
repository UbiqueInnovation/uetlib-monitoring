package ch.ubique.backend.monitoring.service;

import ch.ubique.backend.monitoring.check.StatusCheckService;
import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

public class MonitoringService {

    private final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

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
        this.logger.info(
                "Configured MonitoringService with heartbeat urls:\nbaseurl {}\nfail-url {}",
                this.heartbeatEndpoint,
                this.heartbeatFailEndpoint);
    }

    public void heartbeat() {
        final CheckResults results = statusCheckService.checkAll(CheckLevel.ERROR);
        if (results.allPassed()) {
            restClient.get().uri(this.heartbeatEndpoint).retrieve().toBodilessEntity();
        } else {
            restClient
                    .post()
                    .uri(this.heartbeatFailEndpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(results)
                    .retrieve()
                    .toBodilessEntity();
        }
    }
}
