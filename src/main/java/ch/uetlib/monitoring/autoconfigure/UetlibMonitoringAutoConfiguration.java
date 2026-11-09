package ch.uetlib.monitoring.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = {"ch.uetlib.monitoring", "ch.uetlib.versions"})
public class UetlibMonitoringAutoConfiguration {}
