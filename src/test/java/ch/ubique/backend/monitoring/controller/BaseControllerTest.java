package ch.ubique.backend.monitoring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.ubique.backend.monitoring.autoconfigure.UetlibMonitoringAutoConfiguration;
import ch.ubique.backend.monitoring.check.StatusCheckService;
import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResults;

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

import java.util.List;

@AutoConfigureMockMvc
@WebMvcTest({CheckController.class, InfoController.class})
@Import(UetlibMonitoringAutoConfiguration.class)
@ActiveProfiles({"test", "local-output"})
@AutoConfigureJson
@AutoConfigureJsonTesters
class CheckControllerTest {

    @Autowired private MockMvc mvc;

    @MockitoBean private StatusCheckService statusCheckService;

    @Test
    void check() throws Exception {
        when(statusCheckService.runChecks(any())).thenReturn(new CheckResults(List.of()));
        testCheckEndpoint(CheckLevel.INFO, status().is2xxSuccessful());
    }

    protected void testCheckEndpoint(
            final CheckLevel checkLevel, final ResultMatcher... resultMatchers) throws Exception {
        testRequest(get("/check").param("level", checkLevel.name()), resultMatchers);
    }

    protected void testRequest(
            final MockHttpServletRequestBuilder requestBuilder,
            final ResultMatcher... resultMatchers)
            throws Exception {
        mvc.perform(requestBuilder).andExpectAll(resultMatchers);
    }
}
