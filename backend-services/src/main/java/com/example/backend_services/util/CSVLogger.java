package com.example.backend_services.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CSVLogger {

    private static final String LOGS_DIR = "logs";
    private static final String LOGS_FILE = LOGS_DIR + "/analysis_log.csv";

    public String logResult(String url, String keywords, Map<String, Integer> keywordCounts) {
        try {
            // ✅ Ensure "logs" directory exists
            File dir = new File(LOGS_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // ✅ Create or append CSV file
            try (FileWriter writer = new FileWriter(LOGS_FILE, true)) {
                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                writer.append(timestamp).append(",");
                writer.append(url).append(",");
                writer.append(keywords).append(",");

                if (keywordCounts != null && !keywordCounts.isEmpty()) {
                    for (Map.Entry<String, Integer> entry : keywordCounts.entrySet()) {
                        writer.append(entry.getKey())
                                .append(":")
                                .append(String.valueOf(entry.getValue()))
                                .append(" ");
                    }
                } else {
                    writer.append("No keywords found");
                }

                writer.append("\n");
            }

            return LOGS_FILE;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
