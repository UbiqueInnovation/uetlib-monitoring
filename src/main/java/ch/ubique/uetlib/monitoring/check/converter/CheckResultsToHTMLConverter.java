package ch.ubique.uetlib.monitoring.check.converter;

import static j2html.TagCreator.*;

import ch.ubique.uetlib.monitoring.check.models.*;
import j2html.tags.DomContent;
import j2html.tags.specialized.TableTag;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Component
public class CheckResultsToHTMLConverter {

    // language=JQuery-CSS
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

            @media (max-width: 800px) {
                thead {
                    display: none;
                }

                tbody, tr, tfoot, td {
                    display: block;
                    width: 100%;
                }

                /* omit empty cells */
                td:empty {
                  display: none;
                }

                /* omit "OK" cell */
                tr.check-passed td:nth-child(2) {
                  display: none;
                }

                tr {
                    margin-bottom: 3px;
                    border-radius: 3px;
                }
            }
            """;

    private final JsonMapper jsonMapper;

    public CheckResultsToHTMLConverter(final JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public String convert(final CheckResults results) {
        return document(
                html(
                        head(
                                title("Checks"),
                                meta().withName("viewport")
                                        .attr(
                                                "content",
                                                "width=device-width, initial-scale=1, shrink=1"),
                                meta().withCharset("utf-8"),
                                style(STYLE)),
                        body(buildTable(results), resultsAsJsonScript(results))));
    }

    // Include results as json. Ensures that simple string matches that expect the json document
    // will work even if they get this HTML page instead.
    private @Nullable DomContent resultsAsJsonScript(final CheckResults results) {
        try {
            return script(jsonMapper.writeValueAsString(results))
                    .withId("data")
                    .withType("application/json");
        } catch (final JacksonException e) {
            return null;
        }
    }

    private TableTag buildTable(final CheckResults results) {
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

    private String checkFailureReason(final CheckStatus status) {
        return switch (status) {
            case final CheckPassed ignored -> "";
            case final CheckFailed failed -> failed.reason();
        };
    }

    private @Nullable DomContent checkFailureContext(final CheckStatus status) {
        return switch (status) {
            case final CheckPassed ignored -> null;
            case final CheckFailed failed -> {
                try {
                    yield pre(
                            jsonMapper
                                    .writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(failed.context()));
                } catch (final JacksonException e) {
                    yield pre(String.valueOf(failed.context()));
                }
            }
        };
    }

    private String checkTrClass(final CheckResult check) {
        return checkClass(check.result().passed(), check.level());
    }

    private String allChecksTrClass(final CheckResults results) {
        final var mostSevereFailedCheckLevel =
                results.checks().stream()
                        .filter(check -> !check.result().passed())
                        .map(CheckResult::level)
                        .min(CheckLevel::compareTo) // defined in order most to least severe
                        .orElse(CheckLevel.ERROR); // default level is ERROR

        return checkClass(results.allPassed(), mostSevereFailedCheckLevel);
    }

    private String checkClass(final boolean passed, @NotNull final CheckLevel level) {
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
