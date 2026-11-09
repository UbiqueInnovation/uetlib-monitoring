package ch.uetlib.monitoring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.uetlib.monitoring.check.StatusCheckService;
import ch.uetlib.monitoring.check.models.CheckLevel;
import ch.uetlib.monitoring.check.models.CheckResults;
import ch.uetlib.monitoring.controller.config.CheckControllerTestConfig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

@WebMvcTest(CheckController.class)
@ContextConfiguration(classes = CheckControllerTestConfig.class)
class CheckControllerTest extends BaseControllerTest {

    @MockitoBean private StatusCheckService statusCheckService;

    @Test
    void check() throws Exception {
        when(statusCheckService.runChecks(any(), any())).thenReturn(new CheckResults(List.of()));
        testCheckEndpoint(CheckLevel.INFO, status().is2xxSuccessful());
    }

    protected void testCheckEndpoint(
            final CheckLevel checkLevel, final ResultMatcher... resultMatchers) throws Exception {
        testRequest(get("/check").param("level", checkLevel.name()), resultMatchers);
    }
}
