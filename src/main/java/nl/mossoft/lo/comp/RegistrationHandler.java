package nl.mossoft.lo.comp;

import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class RegistrationHandler {

  public static XSingleComponentFactory __getComponentFactory(String sImplementationName) {
    XSingleComponentFactory xFactory = null;

    Class[] classes = findServicesImplementationClasses();

    int i = 0;
    while (i < classes.length && xFactory == null) {
      Class<?> clazz = classes[i];
      if (sImplementationName.equals(clazz.getCanonicalName())) {
        try {
          Class<?>[] getTypes = new Class[]{String.class};
          Method getFactoryMethod = clazz.getMethod("__getComponentFactory", getTypes);
          Object o = getFactoryMethod.invoke(null, sImplementationName);
          xFactory = (XSingleComponentFactory) o;
        } catch (Exception e) {
          System.err.println("Error happened");
          e.printStackTrace();
        }
      }
      i++;
    }
    return xFactory;
  }

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

  public static boolean __writeRegistryServiceInfo(XRegistryKey xRegistryKey) {

    Class<?>[] classes = findServicesImplementationClasses();

    boolean success = true;
    int i = 0;
    while (i < classes.length && success) {
      Class<?> clazz = classes[i];
      try {
        Class<?>[] writeTypes = new Class[]{XRegistryKey.class};
        Method getFactoryMethod = clazz.getMethod("__writeRegistryServiceInfo", writeTypes);
        Object o = getFactoryMethod.invoke(null, xRegistryKey);
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
