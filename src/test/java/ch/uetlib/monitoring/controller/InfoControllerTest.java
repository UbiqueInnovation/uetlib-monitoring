package ch.uetlib.monitoring.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.uetlib.monitoring.controller.config.InfoControllerTestConfig;
import ch.uetlib.monitoring.model.Artifact;
import ch.uetlib.monitoring.model.InfoResponse;
import ch.uetlib.monitoring.model.SystemInfo;
import ch.uetlib.monitoring.service.InfoService;

import com.github.zafarkhaja.semver.Version;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@WebMvcTest(InfoController.class)
@ContextConfiguration(classes = InfoControllerTestConfig.class)
class InfoControllerTest extends BaseControllerTest {

    @MockitoBean private InfoService infoService;

    @Test
    void info() throws Exception {
        when(infoService.getInfo())
                .thenReturn(
                        new InfoResponse(
                                Version.parse("1.2.3"),
                                Runtime.version().toString(),
                                System.getProperty("java.vendor"),
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
}
