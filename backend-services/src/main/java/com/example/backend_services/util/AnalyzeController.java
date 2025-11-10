package com.example.backend_services.util;

import com.example.backend_services.AnalysisResponse;
import com.example.backend_services.service.WebScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analyze")
@CrossOrigin(origins = "*")
public class AnalyzeController {

    @Autowired
    private WebScraperService webScraperService;

    @GetMapping
    public AnalysisResponse analyze(
            @RequestParam String url,
            @RequestParam String keyword) {

        System.out.println("⚙️ Received API request - URL: " + url + ", Keyword: " + keyword);
        return webScraperService.scrapeWebsite(url, keyword);
    }
}
