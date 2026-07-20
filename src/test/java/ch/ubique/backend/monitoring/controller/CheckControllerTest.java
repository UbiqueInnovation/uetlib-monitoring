package ch.ubique.backend.monitoring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.ubique.backend.monitoring.autoconfigure.UetlibMonitoringAutoConfiguration;
import ch.ubique.backend.monitoring.check.StatusCheckService;
import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResults;
import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.InfoResponse;
import ch.ubique.backend.monitoring.model.SystemInfo;
import ch.ubique.backend.monitoring.service.InfoService;

import com.github.zafarkhaja.semver.Version;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@AutoConfigureMockMvc
@WebMvcTest({CheckController.class, InfoController.class})
@Import(UetlibMonitoringAutoConfiguration.class)
@ActiveProfiles({"test", "local-output"})
@AutoConfigureJson
@AutoConfigureJsonTesters
class WsPublicEndpointsTest {

    @Autowired private MockMvc mvc;

    @MockitoBean private StatusCheckService statusCheckService;
    @MockitoBean private InfoService infoService;

    @Test
    void check() throws Exception {
        when(statusCheckService.runChecks(any())).thenReturn(new CheckResults(List.of()));
        testCheckEndpoint(CheckLevel.INFO, status().is2xxSuccessful());
    }

    protected void testCheckEndpoint(
            final CheckLevel checkLevel, final ResultMatcher... resultMatchers) throws Exception {
        testRequest(get("/check").param("level", checkLevel.name()), resultMatchers);
    }

    @Test
    void info() throws Exception {
        when(infoService.getInfo())
                .thenReturn(
                        new InfoResponse(
                                Version.parse("1.2.3"),
                                String.valueOf(Runtime.version().feature()),
                                Version.parse("0.0.0"),
                                Version.parse("4.0.5"),
                                Version.parse("7.0.6"),
                                new Artifact("a", "b", Version.parse("1.2.3")),
                                new SystemInfo(
                                        Duration.ofMillis(35),
                                        ZonedDateTime.now().minus(Duration.ofMillis(36))),
                                List.of()));
        testInfoEndpoint(status().is2xxSuccessful());
    }

    protected void testInfoEndpoint(final ResultMatcher... resultMatchers) throws Exception {
        testRequest(get("/v1/info"), resultMatchers);
    }

    protected void testRequest(
            final MockHttpServletRequestBuilder requestBuilder,
            final ResultMatcher... resultMatchers)
            throws Exception {
        mvc.perform(requestBuilder).andExpectAll(resultMatchers);
    }
}
