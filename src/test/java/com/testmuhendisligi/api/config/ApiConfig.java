package com.testmuhendisligi.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Test ortami yapilandirma degerlerini config.properties dosyasindan
 * veya Maven sistem parametrelerinden (-Dapi.base.url vb.) okur.
 */
public final class ApiConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ApiConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Yapilandirma dosyasi bulunamadi: " + CONFIG_FILE);
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Yapilandirma dosyasi okunurken hata olustu: " + CONFIG_FILE, e);
        }
    }

    private ApiConfig() {
        // Yardimci sinif, ornek olusturulmaz.
    }

    public static String getBaseUrl() {
        return System.getProperty("api.base.url", PROPERTIES.getProperty("api.base.url"));
    }

    public static long getMaxResponseTimeMs() {
        String value = System.getProperty(
                "api.max.response.time.ms",
                PROPERTIES.getProperty("api.max.response.time.ms", "3000")
        );
        return Long.parseLong(value.trim());
    }
}
