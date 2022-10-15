/*
 * This file is part of QuranLO
 *
 * Copyright (C) 2020-2022 <mossie@mossoft.nl>
 *
 * QuranLO is free software: you can redistribute it and/or modify it under the terms of the GNU
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * The type Registration handler.
 */
public class RegistrationHandler {

  /**
   * Get a component factory for a single component.
   *
   * @param implementationName the implementation name of the component
   * @return the single component factory
   */
  public static XSingleComponentFactory __getComponentFactory(String implementationName) {
    XSingleComponentFactory factory = null;

    Class[] classes = findServicesImplementationClasses();

    int i = 0;
    while (i < classes.length && factory == null) {
      Class<?> clazz = classes[i];
      if (implementationName.equals(clazz.getCanonicalName())) {
        try {
          Class<?>[] getTypes = new Class[]{String.class};
          Method getFactoryMethod = clazz.getMethod("__getComponentFactory", getTypes);
          Object o = getFactoryMethod.invoke(null, implementationName);
          factory = (XSingleComponentFactory) o;
        } catch (Exception e) {
          System.err.println("Error happened");
          e.printStackTrace();
        }
      }
      i++;
    }
    return factory;
  }

  /**
   * It reads the file `RegistrationHandler.classes` and returns the classes to be implemented.
   *
   * @return An array of classes that implement the XRegistryKey interface.
   */
  private static Class<?>[] findServicesImplementationClasses() {

    ArrayList<Class<?>> classes = new ArrayList<>();

    InputStream in = RegistrationHandler.class.getResourceAsStream("RegistrationHandler.classes");

    try (in; LineNumberReader reader = new LineNumberReader(new InputStreamReader(in))) {
      String line = reader.readLine();
      while (line != null) {
        if (!line.equals("")) {
          line = line.trim();
          try {
            Class<?> clazz = Class.forName(line);

            Class<?>[] writeTypes = new Class[]{XRegistryKey.class};
            Class<?>[] getTypes = new Class[]{String.class};

            Method writeRegMethod = clazz.getMethod("__writeRegistryServiceInfo", writeTypes);
            Method getFactoryMethod = clazz.getMethod("__getComponentFactory", getTypes);

            if (writeRegMethod != null && getFactoryMethod != null) {
              classes.add(clazz);
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        line = reader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return classes.toArray(new Class[classes.size()]);
  }

  /**
   * Registers the service and returns if its successful.
   *
   * @param registryKey the registration key
   * @return true if successful
   */
  public static boolean __writeRegistryServiceInfo(XRegistryKey registryKey) {

    Class<?>[] classes = findServicesImplementationClasses();

    boolean success = true;
    int i = 0;
    while (i < classes.length && success) {
      Class<?> clazz = classes[i];
      try {
        Class<?>[] writeTypes = new Class[]{XRegistryKey.class};
        Method getFactoryMethod = clazz.getMethod("__writeRegistryServiceInfo", writeTypes);
        Object o = getFactoryMethod.invoke(null, registryKey);
        success = success && ((Boolean) o).booleanValue();
      } catch (Exception e) {
        success = false;
        e.printStackTrace();
      }
      i++;
    }
    return success;
  }
}
