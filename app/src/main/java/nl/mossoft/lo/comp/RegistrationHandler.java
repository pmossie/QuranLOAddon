/*
 * Copyright (C) 2020-2025 <mossie@mossoft.nl>
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <https://www.gnu.org/licenses/>.
 */

package nl.mossoft.lo.comp;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Utility for registering and retrieving LibreOffice UNO components. */
public final class RegistrationHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationHandler.class);

  // Make the resource name configurable for testing
  private static final String CLASSES_RESOURCE =
      System.getProperty("lo.comp.registration.resource", "RegistrationHandler.classes");

  // Make the service classes list immutable and thread-safe
  private static final List<Class<?>> SERVICE_CLASSES =
      Collections.unmodifiableList(loadServiceClasses());

  // Cache method lookups for better performance
  private static final Map<Class<?>, Method> FACTORY_METHODS = new ConcurrentHashMap<>();
  private static final Map<Class<?>, Method> REGISTRY_METHODS = new ConcurrentHashMap<>();

  private RegistrationHandler() {
    // Prevent instantiation
  }

  /**
   * Retrieves a UNO component factory for the given implementation name.
   *
   * @param implName fully qualified implementation class name
   * @return corresponding XSingleComponentFactory, or {@code null} if not found or on error
   */
  public static XSingleComponentFactory __getComponentFactory(final String implName) {
    Objects.requireNonNull(implName, "Implementation name must not be null");

    if (implName.trim().isEmpty()) {
      LOGGER.warn("Empty implementation name provided");
      return null;
    }

    return SERVICE_CLASSES.stream()
        .filter(cls -> implName.equals(cls.getCanonicalName()))
        .findFirst()
        .map(clazz -> invokeFactory(clazz, implName))
        .orElseGet(
            () -> {
              LOGGER.warn("No factory found for implementation: {}", implName);
              return null;
            });
  }

  private static XSingleComponentFactory invokeFactory(Class<?> clazz, String implName) {
    try {
      Method factoryMethod = getFactoryMethod(clazz);
      if (factoryMethod == null) {
        return null;
      }

      Object factory = factoryMethod.invoke(null, implName);
      return (XSingleComponentFactory) factory;
    } catch (IllegalAccessException e) {
      LOGGER.error("Cannot access __getComponentFactory method in {}", clazz.getName(), e);
    } catch (InvocationTargetException e) {
      LOGGER.error(
          "__getComponentFactory method threw exception in {}",
          clazz.getName(),
          e.getTargetException());
    } catch (ClassCastException e) {
      LOGGER.error("Factory method returned wrong type for {}", clazz.getName(), e);
    } catch (Exception e) {
      LOGGER.error("Unexpected error invoking factory method on {}", clazz.getName(), e);
    }
    return null;
  }

  private static Method getFactoryMethod(Class<?> clazz) {
    return FACTORY_METHODS.computeIfAbsent(
        clazz,
        k -> {
          try {
            return k.getMethod("__getComponentFactory", String.class);
          } catch (NoSuchMethodException e) {
            LOGGER.error("Missing __getComponentFactory method in {}", k.getName(), e);
            return null;
          }
        });
  }

  private static Method getRegistryMethod(Class<?> clazz) {
    return REGISTRY_METHODS.computeIfAbsent(
        clazz,
        k -> {
          try {
            return k.getMethod("__writeRegistryServiceInfo", XRegistryKey.class);
          } catch (NoSuchMethodException e) {
            LOGGER.error("Missing __writeRegistryServiceInfo method in {}", k.getName(), e);
            return null;
          }
        });
  }

  /**
   * Writes registry service info for all known components.
   *
   * @param registryKey UNO registry key
   * @return {@code true} if all registrations succeed; {@code false} otherwise
   */
  public static boolean __writeRegistryServiceInfo(final XRegistryKey registryKey) {
    Objects.requireNonNull(registryKey, "RegistryKey must not be null");

    if (SERVICE_CLASSES.isEmpty()) {
      LOGGER.warn("No service classes available for registration");
      return false;
    }

    boolean[] result = {true};
    SERVICE_CLASSES.forEach(
        clazz -> {
          try {
            Method registryMethod = getRegistryMethod(clazz);
            if (registryMethod == null) {
              result[0] = false;
              return;
            }

            boolean ok = (boolean) registryMethod.invoke(null, registryKey);
            if (!ok) {
              result[0] = false;
              LOGGER.warn("Service info registration returned false for {}", clazz.getName());
            }
          } catch (IllegalAccessException e) {
            result[0] = false;
            LOGGER.error(
                "Cannot access __writeRegistryServiceInfo method in {}", clazz.getName(), e);
          } catch (InvocationTargetException e) {
            result[0] = false;
            LOGGER.error(
                "__writeRegistryServiceInfo method threw exception in {}",
                clazz.getName(),
                e.getTargetException());
          } catch (Exception e) {
            result[0] = false;
            LOGGER.error("Unexpected error invoking registry method on {}", clazz.getName(), e);
          }
        });

    if (result[0]) {
      LOGGER.debug("Successfully registered {} service classes", SERVICE_CLASSES.size());
    } else {
      LOGGER.error("Failed to register some service classes");
    }

    return result[0];
  }

  private static List<Class<?>> loadServiceClasses() {
    ClassLoader classLoader = RegistrationHandler.class.getClassLoader();
    InputStream is = classLoader.getResourceAsStream(CLASSES_RESOURCE);

    if (is == null) {
      LOGGER.error(
          "Resource '{}' not found. Searched with classloader: {}", CLASSES_RESOURCE, classLoader);
      return Collections.emptyList();
    }

    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

      List<Class<?>> classes =
          reader
              .lines()
              .map(String::trim)
              .filter(line -> !line.isEmpty() && !line.startsWith("#")) // Allow comments
              .map(RegistrationHandler::loadClass)
              .filter(Objects::nonNull)
              .collect(Collectors.toList());

      LOGGER.info("Loaded {} UNO service classes from {}", classes.size(), CLASSES_RESOURCE);
      if (classes.isEmpty()) {
        LOGGER.warn("No UNO service classes found. Check resource: {}", CLASSES_RESOURCE);
      }

      return classes;
    } catch (IOException e) {
      LOGGER.error("Error reading {} resource", CLASSES_RESOURCE, e);
      return Collections.emptyList();
    }
  }

  private static Class<?> loadClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      LOGGER.error("Service class '{}' not found", className, e);
      return null;
    } catch (Exception e) {
      LOGGER.error("Unexpected error loading class '{}'", className, e);
      return null;
    }
  }
}
