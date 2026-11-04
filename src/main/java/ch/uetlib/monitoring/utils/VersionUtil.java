package ch.uetlib.monitoring.utils;

import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;

public final class VersionUtil {

    private VersionUtil() {}

    public static Version parseVersion(final String version) {

        try {
            return Version.parse(version);
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Failed to parse version " + version, e);
        }
    }
}
