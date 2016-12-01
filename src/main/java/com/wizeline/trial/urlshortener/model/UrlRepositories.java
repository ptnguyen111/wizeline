package com.wizeline.trial.urlshortener.model;

import com.wizeline.trial.urlshortener.rest.CustomUrlException;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Created by macintosh-vn on 11/30/16.
 */
@Component
public class UrlRepositories {

    private static final String SHORTERNED_URL_PREFIX = "wize.ln";
    private static final String AVAILABLE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int FIXED_LENGTH = 5;

    private Map<String, String> urlMappings = new HashMap<>();

    /**
     * Get Full URL from the short one
     *
     * @param shortUrl
     * @return
     */
    public String getFullUrl(String shortUrl) {
        return urlMappings.get(shortUrl);
    }

    /**
     * create short url
     *
     * @param fullUrl
     * @return
     */
    public String addNewUrl(String fullUrl) {
        URL url;
        try {
            url = new URL(fullUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Url is invalid.");
        }


        Optional<Map.Entry<String, String>> entry1 = urlMappings.entrySet().stream().filter(entry -> entry.getValue().equals(fullUrl)).findFirst();
        if (entry1.isPresent()) {
            return entry1.get().getKey();
        }

        String shorternUrl = generateString(new Random(), AVAILABLE_CHARS, FIXED_LENGTH);
        shorternUrl = url.getProtocol() + "://" + SHORTERNED_URL_PREFIX + "/" + shorternUrl;
        int count = 0;
        while (urlMappings.containsKey(shorternUrl)) {
            shorternUrl = generateString(new Random(), AVAILABLE_CHARS, FIXED_LENGTH);
            shorternUrl = url.getProtocol() + "://" + SHORTERNED_URL_PREFIX + "/" + shorternUrl;
            if (count ++ == 100) {
                // give up
                throw new RuntimeException("Give up");
            }
        }

        urlMappings.put(shorternUrl, fullUrl);
        return shorternUrl;
    }

    /**
     * create short custom url
     *
     * @param fullUrl
     * @param customSuffix
     * @return
     */
    public String addCutomUrl(String fullUrl, String customSuffix) {
        URL url;
        try {
            url = new URL(fullUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Url is invalid.");
        }

        String shorternUrl = url.getProtocol() + "://" + SHORTERNED_URL_PREFIX + "/" + customSuffix;
        if (urlMappings.containsKey(shorternUrl)) {
            throw new CustomUrlException();
        }

        urlMappings.put(shorternUrl, fullUrl);
        return shorternUrl;
    }

    public static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}
