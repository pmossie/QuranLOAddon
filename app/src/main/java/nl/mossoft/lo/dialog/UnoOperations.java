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
import com.sun.star.beans.Property;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
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

/**
 * Utility class providing operations for working with UNO (Universal Network Objects) in
 * LibreOffice.
 *
 * <p>This class offers static utility methods for:
 *
 * <ul>
 *   <li>Creating and querying UNO service instances
 *   <li>Accessing and manipulating dialog controls
 *   <li>Getting and setting control properties
 *   <li>Working with LibreOffice documents and desktop
 *   <li>Debugging and introspection of UNO objects
 * </ul>
 *
 * <p>This is a utility class and cannot be instantiated.
 *
 * @see UnoControlProperties
 */
public final class UnoOperations {
  private static final Logger LOGGER = LoggerFactory.getLogger(UnoOperations.class);

  /**
   * Private constructor to prevent instantiation. This is a utility class that should only be used
   * for its static methods.
   */
  private UnoOperations() {}

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
   * Validates that all provided objects are not null.
   *
   * <p>This method is useful for validating multiple parameters at once, providing clear error
   * messages about which argument position failed.
   *
   * @param objects varargs of objects to check for null
   * @throws IllegalArgumentException if any object is null, with a message indicating the position
   */
  public static void requireNonNull(Object... objects) {
    for (int i = 0; i < objects.length; i++) {
      if (objects[i] == null) {
        throw new IllegalArgumentException("Argument at position " + i + " must not be null");
      }
    }
  }

  /**
   * Retrieves a UNO control from a dialog and queries it for a specific interface.
   *
   * <p>This is a convenience method that combines control retrieval and interface querying in a
   * single call with type safety.
   *
   * @param <T> the UNO interface type to query for
   * @param dialog the dialog containing the control
   * @param type the class object of the interface type (e.g., {@code XListBox.class})
   * @param id the control identifier
   * @return the control cast to the specified interface type
   */
  public static <T> T getUnoControl(XDialog dialog, final Class<T> type, final String id) {
    LOGGER.debug("Get control '{}' as type '{}'", id, type.getName());
    return UnoRuntime.queryInterface(type, getControlContainer(dialog).getControl(id));
  }

  /**
   * Retrieves the control container from a dialog.
   *
   * @param dialog the dialog to get the control container from
   * @return the XControlContainer interface of the dialog
   */
  private static XControlContainer getControlContainer(XDialog dialog) {
    return UnoRuntime.queryInterface(XControlContainer.class, dialog);
  }

  /**
   * Gets a property value from a dialog control and casts it to the specified type.
   *
   * <p>This type-safe version validates that the property value is of the expected type before
   * casting. If the type doesn't match, a warning is logged and null is returned.
   *
   * @param <T> the expected type of the property value
   * @param dialog the dialog containing the control
   * @param id the control identifier
   * @param propertyName the name of the property to retrieve
   * @param type the class object of the expected type
   * @return the property value cast to type T, or null if the property is not of the expected type
   * @see #getPropertyValue(XDialog, String, String)
   */
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

  /**
   * Gets a property value from a dialog control.
   *
   * <p>This method retrieves the raw property value without type checking. For type-safe retrieval,
   * use {@link #getPropertyValue(XDialog, String, String, Class)}.
   *
   * @param dialog the dialog containing the control
   * @param id the control identifier
   * @param propertyName the name of the property to retrieve
   * @return the property value as an Object, or null if the property cannot be retrieved
   * @see UnoControlProperties
   */
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

  /**
   * Get the XPropertySet interface of the given object. "
   *
   * @param object The object to get the property set from.
   * @return The XPropertySet interface of the object.
   */
  public static XPropertySet getPropertySet(final Object object) {
    return UnoRuntime.queryInterface(XPropertySet.class, object);
  }

  /**
   * Gets the XControl interface for a specific control in a container.
   *
   * @param controlId the control identifier
   * @param controlContainer the container holding the control
   * @return the XControl interface of the specified control
   */
  private static XControl getXControl(String controlId, XControlContainer controlContainer) {
    return UnoRuntime.queryInterface(XControl.class, controlContainer.getControl(controlId));
  }

  /**
   * Sets a property value on a dialog control.
   *
   * <p>This method updates a single property of a control. If the property cannot be set (e.g., due
   * to an unknown property name or veto), an error is logged but no exception is thrown.
   *
   * @param dialog the dialog containing the control
   * @param id the control identifier
   * @param propertyName the name of the property to set
   * @param value the new value for the property
   * @see UnoControlProperties
   */
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
   * Logs all properties and their values for a dialog control.
   *
   * <p>This debugging utility method is useful for discovering what properties are available on a
   * control and what their current values are.
   *
   * @param dialog the dialog containing the control
   * @param controlId the control identifier to inspect
   */
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

  /**
   * Returns the currently active text document in LibreOffice.
   *
   * <p>This method retrieves the current component and queries it for the XTextDocument interface.
   * If the current component is not a text document, an empty Optional is returned.
   *
   * @param ctx the UNO component context
   * @return an Optional containing the current XTextDocument, or empty if no text document is
   *     active
   */
  public static Optional<XTextDocument> getCurrentDocument(XComponentContext ctx) {
    return getCurrentComponent(ctx).map(c -> UnoRuntime.queryInterface(XTextDocument.class, c));
  }

  /**
   * Returns the currently active component (document) in LibreOffice.
   *
   * <p>This method retrieves the component that currently has focus in the LibreOffice application.
   *
   * @param ctx the UNO component context
   * @return an Optional containing the current XComponent, or empty if none is active
   */
  public static Optional<XComponent> getCurrentComponent(XComponentContext ctx) {
    return getDesktop(ctx).map(XDesktop::getCurrentComponent);
  }

  /**
   * Returns the LibreOffice desktop service if available.
   *
   * <p>The desktop service is the main entry point for accessing the LibreOffice application and
   * its documents. This method safely handles cases where the desktop service is unavailable.
   *
   * @param ctx the UNO component context
   * @return an Optional containing the XDesktop instance, or empty if unavailable
   */
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

  /**
   * Retrieves the text view cursor supplier from a controller.
   *
   * <p>The cursor supplier provides access to the view cursor, which represents the current
   * position and selection in the document view.
   *
   * @param controller the document controller
   * @return the XTextViewCursorSupplier interface of the controller
   */
  public static XTextViewCursorSupplier getCursorSupplier(final XController controller) {
    return UnoRuntime.queryInterface(XTextViewCursorSupplier.class, controller);
  }
}
