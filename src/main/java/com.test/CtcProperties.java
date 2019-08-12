package com.test;

import net.thucydides.core.guice.Injectors;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.PropertiesFileLocalPreferences;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public final class CtcProperties {

    private static final String ENV = System.getProperty("env");
    private static final String PROJECT_PROPERTY_FILE = "src/test/resources/environment/" + ENV + "/project.properties";
    private static final String COMMON_PROPERTY_FILE = "src/test/resources/common_properties/common.properties";
    private static final String REST_CLIENT_PROPERTY_FILE = "src/test/resources/common_properties/restclient.properties";
    private static final String SERENITY_UNIQUE_PROPERTY_FILE = "src/test/resources/environment/" + ENV
            + "/serenity.properties";
    private static final String SERENITY_COMMON_PROPERTY_FILE = "src/test/resources/common_properties/serenity_common.properties";
    private static final String SERENITY_PROPERTY_FILE = "src/test/resources/serenity.properties";

    private static final Logger LOG = LoggerFactory.getLogger(CtcProperties.class);
    private static EnvironmentVariables environmentVariables = Injectors.getInjector()
            .getProvider(EnvironmentVariables.class).get();
    private static CtcProperties instance = new CtcProperties();
    private Properties properties;

    private CtcProperties() {
        this.properties = new Properties();
        mergeSerenityConfigsAndSave(SERENITY_UNIQUE_PROPERTY_FILE, SERENITY_COMMON_PROPERTY_FILE);
        loadSerenityPropertiesToEnvironment();
        loadPropertiesFromFiles(PROJECT_PROPERTY_FILE, COMMON_PROPERTY_FILE, REST_CLIENT_PROPERTY_FILE);
    }

    public static CtcProperties getInstance() {
        return instance;
    }


    private void mergeSerenityConfigsAndSave(final String... serenityPropertyFiles) {
        try (FileOutputStream outStream = new FileOutputStream(SERENITY_PROPERTY_FILE)) {
            int character;
            for (String configFile : serenityPropertyFiles) {
                FileInputStream configFileInputStream = new FileInputStream(configFile);
                while ((character = configFileInputStream.read()) != -1) {
                    outStream.write(character);
                }
                outStream.write(System.getProperty("line.separator").getBytes());
                configFileInputStream.close();
            }
            outStream.flush();
        } catch (IOException ex) {
            LOG.error("error merging configuration files", ex);
        } finally {
            File outStream = new File(SERENITY_PROPERTY_FILE);
            outStream.deleteOnExit();
        }
    }

    private void loadSerenityPropertiesToEnvironment() {
        Properties localPreferences = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(SERENITY_PROPERTY_FILE)) {
            localPreferences.load(fileInputStream);
            Enumeration<?> propertyNames = localPreferences.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String propertyName = (String) propertyNames.nextElement();
                String localPropertyValue = localPreferences.getProperty(propertyName);
                String currentPropertyValue = environmentVariables.getProperty(propertyName);
                if ((currentPropertyValue == null) && (localPropertyValue != null)) {
                    environmentVariables.setProperty(propertyName, localPropertyValue);
                    LOG.debug("System property {} was successfully added, --> {}", propertyName, localPropertyValue);
                }
            }
            new PropertiesFileLocalPreferences(environmentVariables);
        } catch (IOException e) {
            LOG.error("error loading serenity properties", e);
        }
    }

    private void loadPropertiesFromFiles(final String... propertyFileNames) {
        for (String propertyFile : propertyFileNames) {
            propertiesFromFile(propertyFile);
        }
        afterPropertiesSet();
    }

    private void propertiesFromFile(final String fileName) {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            properties.load(fileInputStream);
            LOG.debug("System properties were successfully loaded, file: {}", fileName);
        } catch (IOException e) {
            LOG.error("error loading properties from file {}", fileName, e);
            throw new RuntimeException(e);
        }
    }

    private void afterPropertiesSet() {
        for (Map.Entry<Object, Object> props : properties.entrySet()) {
            String key = String.valueOf(props.getKey());
            String value = String.valueOf(props.getValue());
            if (StringUtils.isBlank(System.getProperty(key))) {
                System.setProperty(key, value);
                LOG.debug("System property {} was successfully added: {}", key, value);
            }
        }
    }

    public enum Key {

        DISPATCHER_BASE_URL("webdriver.base.url"), //
        IMPLICIT_WAIT("webdriver.timeouts.implicitlywait"), //
        WEB_DRIVER("webdriver.driver"), //
        PARALLEL_AGENT_NUMBER("parallel.agent.number"), //
        PARALLEL_AGENT_TOTAL("parallel.agent.total"), //
        CTC_STORIES("ctc.stories");//

        private final String text;

        /**
         * Provides a string representation of given enumeration.
         *
         * @param text
         *            string of text to attach to Key enum.
         */

        private Key(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
