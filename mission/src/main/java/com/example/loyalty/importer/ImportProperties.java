package com.example.loyalty.importer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImportProperties {
    @Value("${importer.path:}")
    public String importerPath;

    @Value("${importer.poll-interval-ms:60000}")
    public long pollIntervalMs;

    @Value("${importer.stable-check-ms:2000}")
    public long stableCheckMs;

    @Value("${importer.processed-subdir:processed}")
    public String processedSubdir;
}
