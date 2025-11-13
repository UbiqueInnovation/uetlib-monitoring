package ch.ubique.backend.monitoring.check.converter;

import static j2html.TagCreator.*;

import ch.ubique.backend.monitoring.check.models.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import j2html.tags.DomContent;
import j2html.tags.specialized.TableTag;

import jakarta.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

@Component
public class CheckResultsToHTMLConverter {

    private static final String STYLE =
            """
            table {
                border-collapse: collapse;
                width: 100%;
            }
            tbody {
                border-top: 2px solid black;
                border-bottom: 2px solid black;
            }
            th, td {
                text-align: left;
                vertical-align: top;
                padding: 8px;
            }
            td pre {
                white-space: pre-wrap;
            }
            tr.check-passed {
                background-color: #1fb26d80;
            }
            tr.check-info {
                background-color: #0dcaf080;
            }
            tr.check-warning {
                background-color: #ffc10780;
            }
            tr.check-error {
                background-color: #e6175c80;
            }
            """;

    private final ObjectMapper objectMapper;

    public CheckResultsToHTMLConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convert(CheckResults results) {
        return html(
                        head(
                                title("Checks"),
                                meta().withName("viewport")
                                        .attr(
                                                "content",
                                                "width=device-width, initial-scale=1, shrink=1"),
                                meta().withCharset("utf-8"),
                                style(STYLE)),
                        body(buildTable(results), resultsAsJsonScript(results)))
                .render();
    }

    // Include results as json. Ensures that simple string matches that expect the json document
    // will work even if they get this HTML page instead.
    private DomContent resultsAsJsonScript(CheckResults results) {
        try {
            return script(objectMapper.writeValueAsString(results))
                    .withId("data")
                    .withType("application/json");
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private TableTag buildTable(CheckResults results) {
        return table(
                thead(
                        tr(
                                th("Check (Class Name)"),
                                th("Result"),
                                th("Failure Reason"),
                                th("Failure Context"))),
                tbody(
                        each(
                                results.checks(),
                                check ->
                                        tr(
                                                        td(check.check().getSimpleName()),
                                                        td(
                                                                check.result().passed()
                                                                        ? "OK"
                                                                        : check.level().toString()),
                                                        td(checkFailureReason(check.result())),
                                                        td(checkFailureContext(check.result())))
                                                .withClass(checkTrClass(check)))),
                tfoot(
                        tr(td("All passed"), td(String.valueOf(results.allPassed())), td(), td())
                                .withClass(allChecksTrClass(results))));
    }

    private String checkFailureReason(CheckStatus status) {
        return switch (status) {
            case CheckPassed _s -> "";
            case CheckFailed failed -> failed.reason();
        };
    }

    private DomContent checkFailureContext(CheckStatus status) {
        return switch (status) {
            case CheckPassed _s -> null;
            case CheckFailed failed -> {
                try {
                    yield pre(
                            objectMapper
                                    .writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(failed.context()));
                } catch (JsonProcessingException e) {
                    yield pre(String.valueOf(failed.context()));
                }
            }
        };
    }

    private String checkTrClass(CheckResult check) {
        return checkClass(check.result().passed(), check.level());
    }

    private String allChecksTrClass(CheckResults results) {
        var mostSevereFailedCheckLevel =
                results.checks().stream()
                        .filter(check -> check.result().passed())
                        .map(CheckResult::level)
                        .min(CheckLevel::compareTo) // defined in order most to least severe
                        .orElse(CheckLevel.ERROR); // default level is ERROR

        return checkClass(results.allPassed(), mostSevereFailedCheckLevel);
    }

    private String checkClass(boolean passed, @NotNull CheckLevel level) {
        if (passed) {
            return "check-passed";
        }

        return switch (level) {
            case ERROR -> "check-error";
            case WARNING -> "check-warning";
            case INFO -> "check-info";
        };
    }
}
