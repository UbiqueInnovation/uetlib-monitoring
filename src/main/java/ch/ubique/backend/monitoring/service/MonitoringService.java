package ch.ubique.backend.monitoring.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

public class MonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    private final RestClient restClient;
    private final String heartbeatEndpoint;

    public MonitoringService(final RestClient restClient, final String heartbeatEndpoint) {
        this.restClient = restClient;
        this.heartbeatEndpoint = heartbeatEndpoint;
    }

    public void heartbeat() {
        if (StringUtils.isNotBlank(heartbeatEndpoint)) {
            logger.debug("Ping heartbeat endpoint {}.", heartbeatEndpoint);
            final var response =
                    restClient.get().uri(heartbeatEndpoint).retrieve().toEntity(String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.warn(
                        "heartbeat endpoint {} returned status {}.",
                        heartbeatEndpoint,
                        response.getStatusCode());
            }
        }
    }
}
