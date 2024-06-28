package ch.ubique.backend.monitoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.zafarkhaja.semver.Version;

import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;

import java.util.Properties;

class InfoServiceTest {

    @Test
    void testInfoResponse() {
        final var properties = new Properties();
        properties.setProperty("version", "2.3.1");
        final var service = new InfoService(new BuildProperties(properties));
        assertEquals(
                String.valueOf(Runtime.version().feature()),
                service.getInfo().javaVersion().toString());
        assertEquals(Version.of(2, 3, 1), service.getInfo().projectVersion());
    }
}
