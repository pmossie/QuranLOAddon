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

package nl.mossoft.lo.dialog;

import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XDialog;
import com.sun.star.beans.*;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UnoOperations {
  private static final Logger LOGGER = LoggerFactory.getLogger(UnoOperations.class);

  private UnoOperations() {} // Prevent instantiation

  /**
   * Creates a UNO service instance and queries for the specified interface
   *
   * @param <T> The UNO interface type
   * @param context The component context
   * @param serviceName The service name (e.g., "com.sun.star.ucb.FileContentProvider")
   * @param interfaceType The interface class (e.g., XFileIdentifierConverter.class)
   * @return The queried interface instance
   * @throws IllegalStateException if service creation or query fails
   */
  public static <T> T createService(
      XComponentContext context, String serviceName, Class<T> interfaceType) {
    try {
      Object service = context.getServiceManager().createInstanceWithContext(serviceName, context);
      T result = UnoRuntime.queryInterface(interfaceType, service);
      if (result == null) {
        throw new IllegalStateException("Service query returned null for: " + serviceName);
      }
      return result;
    } catch (Exception e) {
      LOGGER.error("Failed to create service: '{}'", serviceName, e);
      throw new IllegalStateException("Service creation failed: " + serviceName, e);
    }
  }

  /**
   * Validates that objects are not null
   *
   * @param objects Varargs of objects to check
   * @throws IllegalArgumentException if any object is null
   */
  public static void requireNonNull(Object... objects) {
    for (int i = 0; i < objects.length; i++) {
      if (objects[i] == null) {
        throw new IllegalArgumentException("Argument at position " + i + " must not be null");
      }
    }
  }

  public static <T> T getUnoControl(XDialog dialog, final Class<T> type, final String id) {
    LOGGER.debug("Get control '{}' as type '{}'", id, type.getName());
    return UnoRuntime.queryInterface(type, getControlContainer(dialog).getControl(id));
  }

  private static XControlContainer getControlContainer(XDialog dialog) {
    return UnoRuntime.queryInterface(XControlContainer.class, dialog);
  }

  @SuppressWarnings("unchecked")
  public static <T> T getPropertyValue(
      XDialog dialog, String id, String propertyName, Class<T> type) {
    T result = null;
    Object value = getPropertyValue(dialog, id, propertyName);
    if (type.isInstance(value)) {
      result = (T) value;
    } else {
      LOGGER.warn(
          "Property '{}' on control '{}' is not of expected type {}",
          propertyName,
          id,
          type.getSimpleName());
    }
    return result;
  }

  public static Object getPropertyValue(XDialog dialog, String id, String propertyName) {
    LOGGER.debug("Get '{}' of control '{}'", propertyName, id);
    final XControlContainer controlContainer = getControlContainer(dialog);
    final XControl control = getXControl(id, controlContainer);
    final XPropertySet ps = getPropertySet(control.getModel());

    try {
      return ps.getPropertyValue(propertyName);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      LOGGER.error("Failed to get '{}' of control '{}'", propertyName, control, e);
      return null;
    }
  }

  public static void setPropertyValue(
      XDialog dialog, String id, String propertyName, Object value) {
    LOGGER.debug("Set property '{}' of control '{}' to '{}'", propertyName, id, value);
    final XControlContainer controlContainer = getControlContainer(dialog);
    final XControl control = getXControl(id, controlContainer);
    final XPropertySet ps = getPropertySet(control.getModel());
    try {
      ps.setPropertyValue(propertyName, value);
    } catch (final IllegalArgumentException
        | UnknownPropertyException
        | PropertyVetoException
        | WrappedTargetException e) {
      LOGGER.error("Failed to set '{}' of control '{}'", propertyName, control, e);
    }
  }

  /**
   * Get the XPropertySet interface of the given object. "
   *
   * @param object The object to get the property set from.
   * @return The XPropertySet interface of the object.
   */
  public static XPropertySet getPropertySet(final Object object) {
    return UnoRuntime.queryInterface(XPropertySet.class, object);
  }

  private static XControl getXControl(String controlId, XControlContainer controlContainer) {
    return UnoRuntime.queryInterface(XControl.class, controlContainer.getControl(controlId));
  }

  public static void showProperties(XDialog dialog, String controlId) {
    final XPropertySet ps =
        UnoRuntime.queryInterface(
            XPropertySet.class, (getControlContainer(dialog).getControl(controlId).getModel()));
    final Property[] properties = ps.getPropertySetInfo().getProperties();
    for (final Property property : properties) {
      try {
        LOGGER.debug(
            "--> {}.{} = {}", controlId, property.Name, ps.getPropertyValue(property.Name));
      } catch (final UnknownPropertyException | WrappedTargetException e) {
        LOGGER.error(e.toString(), e);
      }
    }
  }

  /** Returns the LibreOffice desktop if available, otherwise empty. */
  public static Optional<XDesktop> getDesktop(XComponentContext ctx) {
    if (ctx == null) {
      LOGGER.warn("XComponentContext is null");
      return Optional.empty();
    }
    try {
      XMultiComponentFactory smgr =
          UnoRuntime.queryInterface(XMultiComponentFactory.class, ctx.getServiceManager());
      if (smgr == null) {
        LOGGER.warn("ServiceManager is unavailable");
        return Optional.empty();
      }
      Object desktop = smgr.createInstanceWithContext("com.sun.star.frame.Desktop", ctx);
      XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, desktop);
      if (xDesktop == null) {
        LOGGER.warn("Failed to query XDesktop from Desktop service");
      }
      return Optional.ofNullable(xDesktop);
    } catch (com.sun.star.uno.Exception e) {
      LOGGER.warn("Could not create Desktop service", e);
      return Optional.empty();
    }
  }

  /** Returns the current XComponent if any, otherwise empty. */
  public static Optional<XComponent> getCurrentComponent(XComponentContext ctx) {
    return getDesktop(ctx).map(XDesktop::getCurrentComponent);
  }

  /** Returns the current text document if one is active, otherwise empty. */
  public static Optional<XTextDocument> getCurrentDocument(XComponentContext ctx) {
    return getCurrentComponent(ctx).map(c -> UnoRuntime.queryInterface(XTextDocument.class, c));
  }

  public static XTextViewCursorSupplier getCursorSupplier(final XController controller) {
    return UnoRuntime.queryInterface(XTextViewCursorSupplier.class, controller);
  }
}
