package com.amadeus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

public class ConfigurationTest {
  /**
   * Configuration Test.
   */
  @Test public void testInitialize() {
    Configuration configuration = new Configuration("id", "secret");
    assertTrue(configuration instanceof Configuration, "should return a Configuration object");
  }

  @Test public void testBuild() {
    Configuration configuration = new Configuration("123", "234");

    assertNotNull(configuration.build(), "should return a Amadeus object");
    assertEquals(configuration.getClientId(),
                 "123",
               "should set the com.amadeus.client ID");
    assertEquals(configuration.getClientSecret(),
                "234",
              "should set the com.amadeus.client secret");
  }

  @Test public void testBuildDefaults() {
    Configuration configuration = new Configuration("id", "secret");
    assertNotNull(configuration.getLogger());
    assertEquals(configuration.getLogLevel(), "silent");
    assertEquals(configuration.getHostname(), "test");
    assertEquals(configuration.getHost(), "test.api.amadeus.com");
    assertTrue(configuration.isSsl());
    assertEquals(configuration.getPort(), 443);
    assertNull(configuration.getCustomAppId());
    assertNull(configuration.getCustomAppVersion());
  }

  @Test public void testBuildCustomLogger() {
    Logger logger = Logger.getLogger("Test");
    Configuration configuration = new Configuration("id", "secret")
            .setLogger(logger)
            .setLogLevel("debug");

    assertEquals(configuration.getLogger(), logger);
    assertEquals(configuration.getLogLevel(), "debug");
  }

  @Test public void testBuildCustomHostname() {
    Configuration configuration = new Configuration("id", "secret").setHostname("production");
    assertEquals(configuration.getHostname(), "production");
    assertEquals(configuration.getHost(), "api.amadeus.com");
  }

  @Test public void testBuildInvalidHostname() {
    assertThrows(IllegalArgumentException.class, () ->
        new Configuration("id", "secret").setHostname("foo"));
  }

  @Test public void testBuildCustomHost() {
    Configuration configuration = new Configuration("id", "secret").setHost("foo.bar.com");
    assertEquals(configuration.getHost(), "foo.bar.com");
  }

  @Test public void testBuildCustomSsl() {
    Configuration configuration = new Configuration("id", "secret").setSsl(true);
    assertTrue(configuration.isSsl());
    assertEquals(configuration.getPort(),443);
  }


  @Test public void testBuildCustomSslWithCustomPort() {
    Configuration configuration = new Configuration("id", "secret").setPort(8080).setSsl(true);
    assertTrue(configuration.isSsl());
    assertEquals(configuration.getPort(),8080);
  }

  @Test public void testBuildCustomNonSsl() {
    Configuration configuration = new Configuration("id", "secret").setSsl(false);
    assertFalse(configuration.isSsl());
    assertEquals(configuration.getPort(),80);
  }

  @Test public void testBuildCustomNonSslWithCustomPort() {
    Configuration configuration = new Configuration("id", "secret").setPort(8080).setSsl(false);
    assertFalse(configuration.isSsl());
    assertEquals(configuration.getPort(),8080);
  }

  @Test public void testToString() {
    Configuration configuration = new Configuration("id", "secret").setPort(8080).setSsl(false);
    assertTrue(configuration.toString()
            .startsWith("Configuration(clientId=id, clientSecret=secret,"));
  }
}
