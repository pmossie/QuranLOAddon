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
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Utility for registering and retrieving LibreOffice UNO components. */
public final class RegistrationHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationHandler.class);

  private static final String CLASSES_RESOURCE = "RegistrationHandler.classes";
  private static final List<Class<?>> SERVICE_CLASSES = loadServiceClasses();

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
    return SERVICE_CLASSES.stream()
        .filter(cls -> implName.equals(cls.getCanonicalName()))
        .findFirst()
        .map(clazz -> invokeFactory(clazz, implName))
        .orElse(null);
  }

  private static XSingleComponentFactory invokeFactory(Class<?> clazz, String implName) {
    try {
      Object factory =
          clazz.getMethod("__getComponentFactory", String.class).invoke(null, implName);
      return (XSingleComponentFactory) factory;
    } catch (ReflectiveOperationException e) {
      LOGGER.error("Failed to invoke __getComponentFactory on {}", clazz.getName(), e);
      return null;
    }
  }

  /**
   * Writes registry service info for all known components.
   *
   * @param registryKey UNO registry key
   * @return {@code true} if all registrations succeed; {@code false} otherwise
   */
  public static boolean __writeRegistryServiceInfo(final XRegistryKey registryKey) {
    Objects.requireNonNull(registryKey, "RegistryKey must not be null");
    boolean[] result = {true};
    SERVICE_CLASSES.forEach(
        clazz -> {
          try {
            boolean ok =
                (boolean)
                    clazz
                        .getMethod("__writeRegistryServiceInfo", XRegistryKey.class)
                        .invoke(null, registryKey);
            if (!ok) {
              result[0] = false;
              LOGGER.warn("Service info registration returned false for {}", clazz.getName());
            }
          } catch (ReflectiveOperationException e) {
            result[0] = false;
            LOGGER.error("Failed to invoke __writeRegistryServiceInfo on {}", clazz.getName(), e);
          }
        });
    return result[0];
  }

  private static List<Class<?>> loadServiceClasses() {
    InputStream is = RegistrationHandler.class.getResourceAsStream(CLASSES_RESOURCE);
    if (is == null) {
      LOGGER.error("Resource '{}' not found on classpath", CLASSES_RESOURCE);
      return Collections.emptyList();
    }
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
      return reader
          .lines()
          .map(String::trim)
          .filter(line -> !line.isEmpty())
          .map(RegistrationHandler::loadClass)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
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
    }
  }
}
