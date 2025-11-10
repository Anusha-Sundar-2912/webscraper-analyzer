package com.example.backend_services;

import java.util.Map;

public class AnalysisResponse {

    private String url;
    private String title;
    private String description;
    private Map<String, Integer> keywordCounts;
    private Map<String, Double> keywordDensity;
    private String nestedUrls;
    private String gcpFileUrl;

    public AnalysisResponse() {
    }

    public AnalysisResponse(
            String url,
            String title,
            String description,
            Map<String, Integer> keywordCounts,
            Map<String, Double> keywordDensity,
            String nestedUrls,
            String gcpFileUrl) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.keywordCounts = keywordCounts;
        this.keywordDensity = keywordDensity;
        this.nestedUrls = nestedUrls;
        this.gcpFileUrl = gcpFileUrl;
    }

    // Getters
    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Integer> getKeywordCounts() {
        return keywordCounts;
    }

    public Map<String, Double> getKeywordDensity() {
        return keywordDensity;
    }

    public String getNestedUrls() {
        return nestedUrls;
    }

    public String getGcpFileUrl() {
        return gcpFileUrl;
    }

    // Setters
    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKeywordCounts(Map<String, Integer> keywordCounts) {
        this.keywordCounts = keywordCounts;
    }

    public void setKeywordDensity(Map<String, Double> keywordDensity) {
        this.keywordDensity = keywordDensity;
    }

    public void setNestedUrls(String nestedUrls) {
        this.nestedUrls = nestedUrls;
    }

    public void setGcpFileUrl(String gcpFileUrl) {
        this.gcpFileUrl = gcpFileUrl;
    }
}
