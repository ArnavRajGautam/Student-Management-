package com.arnav.sms.config;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * Logging Configuration
 *
 * WHY: Application startup logging aur initialization
 * WHEN: Spring container create hone ke baad run hota hai
 */
@Configuration
@Slf4j
public class LoggingConfig {

    // Application properties se values inject karo
    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    /**
     * @PostConstruct: Bean create hone ke turant baad run hota hai
     *
     * WHY: Application startup info log karne ke liye
     */
    @PostConstruct
    public void init() {
        log.info("===================================");
        log.info("Application: {}", applicationName);
        log.info("Active Profile: {}", activeProfile);
        log.info("Server Port: {}", serverPort);
        log.info("Status: Starting...");
        log.info("===================================");

        // Log important configurations
        logEnvironmentInfo();
    }

    /**
     * Environment information logging
     */
    private void logEnvironmentInfo() {
        log.debug("Java Version: {}", System.getProperty("java.version"));
        log.debug("OS: {}", System.getProperty("os.name"));
        log.debug("User Dir: {}", System.getProperty("user.dir"));

        // Profile-specific logging
        if ("prod".equals(activeProfile)) {
            log.warn("Running in PRODUCTION mode!");
        } else if ("dev".equals(activeProfile)) {
            log.info("Running in DEVELOPMENT mode");
        }
    }
}