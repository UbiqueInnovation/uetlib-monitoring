package ch.ubique.uetlib.monitoring.service;

import static ch.ubique.uetlib.monitoring.utils.VersionUtil.parseVersion;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.ubique.uetlib.monitoring.service.artifact.ArtifactVersionService;
import com.github.zafarkhaja.semver.Version;
import de.westemeyer.version.service.spring.ArtifactVersionCollector;
import java.util.List;
import java.util.Properties;
import kotlin.KotlinVersion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;

class InfoServiceTest {

    @Test
    void testInfoResponse() {
        final var infoService = mockInfoService();

        assertEquals(
                String.valueOf(Runtime.version().feature()), infoService.getInfo().javaVersion());
        assertEquals(
                parseVersion(String.valueOf(KotlinVersion.CURRENT)),
                infoService.getInfo().kotlinVersion());
        assertEquals(Version.of(2, 3, 1), infoService.getInfo().projectVersion());
    }

    private static InfoService mockInfoService() {
        final var properties = new Properties();
        properties.setProperty("group", "ch.ubique.uetlib");
        properties.setProperty("artifact", "uetlib-monitoring");
        properties.setProperty("version", "2.3.1");
        return new InfoService(
                new ArtifactVersionService(new ArtifactVersionCollector(List.of())),
                new BuildProperties(properties));
    }
}
