package ch.uetlib.monitoring.controller;

import ch.uetlib.monitoring.autoconfigure.UetlibMonitoringAutoConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@AutoConfigureMockMvc
@Import(UetlibMonitoringAutoConfiguration.class)
@ActiveProfiles({"test", "local-output"})
@AutoConfigureJson
@AutoConfigureJsonTesters
abstract class BaseControllerTest {

    @MockitoBean private BuildProperties buildProperties;
    @Autowired private MockMvc mvc;

    protected void testRequest(
            final MockHttpServletRequestBuilder requestBuilder,
            final ResultMatcher... resultMatchers)
            throws Exception {
        mvc.perform(requestBuilder).andExpectAll(resultMatchers);
    }
}
