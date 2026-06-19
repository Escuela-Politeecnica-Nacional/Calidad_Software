package repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class JpaUtil {
    private static final Logger LOGGER = Logger.getLogger(JpaUtil.class.getName());
    private static volatile EntityManagerFactory emf;

    private JpaUtil() {
    }

    private static Map<String, Object> buildOverrides() {
        Map<String, Object> overrides = new LinkedHashMap<>();

        putIfPresent(overrides, "jakarta.persistence.jdbc.driver", env("DB_DRIVER"));
        putIfPresent(overrides, "jakarta.persistence.jdbc.url", env("DB_URL"));
        putIfPresent(overrides, "jakarta.persistence.jdbc.user", env("DB_USER"));
        putIfPresent(overrides, "jakarta.persistence.jdbc.password", env("DB_PASSWORD"));
        putIfPresent(overrides, "hibernate.dialect", env("HIBERNATE_DIALECT"));
        putIfPresent(overrides, "hibernate.hbm2ddl.auto", env("HIBERNATE_HBM2DDL_AUTO"));
        putIfPresent(overrides, "hibernate.show_sql", env("HIBERNATE_SHOW_SQL"));

        LOGGER.info(overrides.isEmpty()
                ? "JPA usando la configuración local de persistence.xml."
                : "JPA configurado mediante variables de entorno.");

        return overrides;
    }

    private static String env(String key) {
        return firstNonBlank(System.getProperty(key), System.getenv(key));
    }

    private static void putIfPresent(Map<String, Object> overrides, String property, String value) {
        if (value != null && !value.isBlank()) {
            overrides.put(property, value.trim());
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "";
    }

    public static String getConfigValue(String key) {
        return env(key);
    }

    public static String getConfigValue(String key, String defaultValue) {
        String value = env(key);
        return value.isBlank() ? defaultValue : value;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void shutdown() {
        EntityManagerFactory local = emf;
        if (local != null && local.isOpen()) {
            local.close();
        }
    }

    private static EntityManagerFactory getEntityManagerFactory() {
        EntityManagerFactory local = emf;
        if (local != null) {
            return local;
        }

        synchronized (JpaUtil.class) {
            local = emf;
            if (local == null) {
                local = Persistence.createEntityManagerFactory("pdciaePU", buildOverrides());
                emf = local;
            }
        }

        return local;
    }
}
