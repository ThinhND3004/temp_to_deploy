package personal.kayden.temp_to_deploy.config;

import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FlywayConfiguration {

    /**
     * Customize Flyway configuration
     * Cách này KHÔNG override bean, chỉ customize config
     */
    @Bean
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer(Environment env) {
        return configuration -> {
            // ============================================
            // CUSTOM NAMING CONVENTION
            // ============================================
            // Format: YYYYMMDDHHmm__description.sql

            configuration
                    // Prefix (empty for timestamp)
                    .sqlMigrationPrefix("")

                    // Separator
                    .sqlMigrationSeparator("__")

                    // Suffixes
                    .sqlMigrationSuffixes(".sql")

                    // Disable naming validation
                    .validateMigrationNaming(false)

                    // ============================================
                    // OTHER SETTINGS
                    // ============================================

                    // Repeatable migrations
                    .repeatableSqlMigrationPrefix("R__")

                    // Baseline
                    .baselineOnMigrate(true)
                    .baselineVersion("0")

                    // Validation
                    .validateOnMigrate(true)
                    .outOfOrder(false)

                    // Schema history table
                    .table("flyway_schema_history")

                    // Encoding
                    .encoding("UTF-8")

                    // Clean disabled (for safety)
                    .cleanDisabled(getCleanDisabled(env))

                    // Placeholder
                    .placeholderReplacement(false);
        };
    }

    /**
     * Clean chỉ enable trên local, disable trên staging/prod
     */
    private boolean getCleanDisabled(Environment env) {
        String profile = env.getProperty("spring.profiles.active", "local");
        return !profile.equals("local");
    }

    /**
     * Custom migration strategy (optional)
     */
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // Logic custom trước khi migrate
            System.out.println("🔄 Running Flyway migrations...");

            // Info before migration
            var info = flyway.info();
            var pending = info.pending();

            if (pending.length > 0) {
                System.out.println("⏳ Found " + pending.length + " pending migration(s):");
                for (var migration : pending) {
                    System.out.println("   - " + migration.getVersion() +
                            ": " + migration.getDescription());
                }
            }

            // Execute migration
            flyway.migrate();

            // Info after migration
            System.out.println("✅ Migrations completed successfully!");
        };
    }
}