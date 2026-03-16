package com.portfolio.rebalancing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@SpringBootApplication
public class PortfolioRebalancingApplication {

    public static void main(String[] args) {
        /*
         * Interview-friendly rationale:
         * - SQLite needs a filesystem path (a classpath resource inside a JAR is not writable).
         * - The assignment ships an existing `model_portfolio.db` in resources.
         * - So we resolve a real file path at startup:
         *   - Prefer the dev-time path `backend/src/main/resources/model_portfolio.db` if present.
         *   - Otherwise extract the classpath DB into a user-home app folder.
         * This keeps the schema unchanged while allowing READ/WRITE tables to work.
         */
        System.setProperty("portfolio.db.path", resolveSqliteDbPath().toString());
        SpringApplication.run(PortfolioRebalancingApplication.class, args);
    }

    private static Path resolveSqliteDbPath() {
        Path devPath = Path.of("src/main/resources/model_portfolio.db").toAbsolutePath().normalize();
        if (Files.exists(devPath)) {
            return devPath;
        }

        Path appDir = Path.of(System.getProperty("user.home"), ".portfolio-rebalancing");
        Path extractedDb = appDir.resolve("model_portfolio.db");
        try {
            Files.createDirectories(appDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create app data directory: " + appDir, e);
        }

        if (Files.exists(extractedDb)) {
            return extractedDb;
        }

        try (InputStream in = PortfolioRebalancingApplication.class.getClassLoader()
                .getResourceAsStream("model_portfolio.db")) {
            if (in == null) {
                throw new IllegalStateException(
                        "Missing required SQLite DB resource: src/main/resources/model_portfolio.db"
                );
            }
            Files.copy(in, extractedDb, StandardCopyOption.REPLACE_EXISTING);
            return extractedDb;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to extract SQLite DB to: " + extractedDb, e);
        }
    }
}

