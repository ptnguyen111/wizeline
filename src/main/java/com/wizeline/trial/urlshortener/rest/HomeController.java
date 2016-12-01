package com.wizeline.trial.urlshortener.rest;

import com.wizeline.trial.urlshortener.model.UrlRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by macintosh-vn on 11/30/16.
 */
@RestController
@RequestMapping(value = "/rest/url")
public class HomeController {


    @Autowired
    private UrlRepositories urlRepositories;

    @RequestMapping(value = "", method= RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestParam("customUrl") String customUrl, @RequestParam("fullUrl") String fullUrl) {

        if (customUrl == null || customUrl.isEmpty()) {
            String shortUrl = urlRepositories.addNewUrl(fullUrl);
            return ResponseEntity.ok(buildJsonResponse(shortUrl));
        } else {
            try {
                String result = urlRepositories.addCutomUrl(fullUrl, customUrl);
                return ResponseEntity.ok(buildJsonResponse(result));
            } catch (CustomUrlException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("customUrl is already in Used");
            }

        }

    }

    private String buildJsonResponse(String url) {
        return new StringBuilder("{ \"url\": ").append("\"" + url + "\"").append("}").toString();
    }

    @RequestMapping(value = "", method=RequestMethod.GET)
    public ResponseEntity<Object> getFullUrl(@RequestParam("shortUrl") String shortUrl) {
        if (shortUrl == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid parameter shortUrl");
        }

        String result = urlRepositories.getFullUrl(shortUrl);
        result = result == null ? "" : result;
        return ResponseEntity.ok(buildJsonResponse(result));
    }
}
