package com.example.backend_services.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.example.backend_services.AnalysisResponse;
import com.example.backend_services.util.CSVLogger;
import com.example.backend_services.util.GCPStorageService;

@Service
public class WebScraperService {

    private final CSVLogger csvLogger = new CSVLogger();
    private final GCPStorageService gcpStorageService = new GCPStorageService();

    public AnalysisResponse scrapeWebsite(String url, String keyword) {
        try {
            System.out.println("🔍 Scraping started for: " + url);
            Document doc = Jsoup.connect(url).get();

            String title = doc.title();
            String description = doc.select("meta[name=description]").attr("content");
            String text = doc.text();

            Map<String, Integer> keywordCounts = new HashMap<>();
            Map<String, Double> keywordDensity = new HashMap<>();

            if (keyword != null && !keyword.isEmpty()) {
                String[] keywords = keyword.split(",");
                int totalWords = text.split("\\s+").length;

                for (String k : keywords) {
                    k = k.trim().toLowerCase();
                    int count = countOccurrences(text.toLowerCase(), k);
                    keywordCounts.put(k, count);

                    double density = (count / (double) totalWords) * 100;
                    keywordDensity.put(k, density);
                }
            }

            Elements links = doc.select("a[href]");
            StringBuilder nestedUrls = new StringBuilder();
            for (Element link : links) {
                nestedUrls.append(link.attr("abs:href")).append("\n");
            }

            // ✅ FIXED: Pass keywordCounts (Map), not an int
            String logFile = csvLogger.logResult(url, keyword, keywordCounts);

            // ✅ Upload the generated CSV to GCP
            String uploadedUrl = gcpStorageService.uploadFile(logFile);

            System.out.println("✅ Scraping completed successfully!");
            return new AnalysisResponse(
                    url,
                    title,
                    description,
                    keywordCounts,
                    keywordDensity,
                    nestedUrls.toString(),
                    uploadedUrl
            );

        } catch (IOException e) {
            System.err.println("❌ Network/HTTP Error: " + e.getMessage());
            return new AnalysisResponse(
                    url,
                    "Error",
                    "Network/HTTP failure",
                    null,
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            System.err.println("❌ Scraping failed: " + e.getMessage());
            return new AnalysisResponse(
                    url,
                    "Error",
                    "Unexpected error",
                    null,
                    null,
                    null,
                    null
            );
        }
    }

    private int countOccurrences(String text, String keyword) {
        if (keyword.isEmpty()) {
            return 0;
        }
        int count = 0, index = 0;
        while ((index = text.indexOf(keyword, index)) != -1) {
            count++;
            index += keyword.length();
        }
        return count;
    }
}
