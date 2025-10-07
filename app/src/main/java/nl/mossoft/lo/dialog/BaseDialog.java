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

import static nl.mossoft.lo.dialog.DialogHelper.createDialog;

import com.sun.star.awt.XDialog;
import com.sun.star.awt.XDialogEventHandler;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.uno.XComponentContext;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import nl.mossoft.lo.util.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for extension dialogs in the Quran LibreOffice Addon. Provides common
 * functionality for dialog management, event handling, and lifecycle management.
 *
 * <p>This class implements the {@link XDialogEventHandler} interface to handle UNO dialog events
 * and extends {@link WeakBase} for proper UNO object lifecycle management.
 *
 * <p>Subclasses must implement:
 *
 * <ul>
 *   <li>{@link #initDialogControls()} - Initialize all dialog controls with their values
 *   <li>{@link #initHandlers()} - Register event handlers for dialog controls
 * </ul>
 */
public abstract class BaseDialog extends WeakBase implements XDialogEventHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(BaseDialog.class);

  /** The UNO dialog instance managed by this base dialog */
  protected final XDialog dialog;

  /** The UNO component context for accessing LibreOffice services */
  protected final XComponentContext ctx;

  /** Configuration manager for storing and retrieving dialog settings */
  protected final ConfigurationManager configManager;

  /** The name of the dialog, derived from the XDL resource filename */
  private final String name;

  /** Map of registered event handlers keyed by dialog event type */
  private final Map<DialogEvents, DialogEventHandler> handlers = new EnumMap<>(DialogEvents.class);

  /** Flag indicating whether dialog initialization has been completed */
  protected boolean initializationCompleted = false;

  /**
   * Constructs a new BaseDialog instance and initializes the dialog from an XDL resource.
   *
   * @param ctx the UNO component context for accessing LibreOffice services
   * @param xdlResource the name of the XDL resource file (e.g., "MainDialog.xdl")
   */
  protected BaseDialog(XComponentContext ctx, String xdlResource) {
    this.name = xdlResource.split("\\.")[0];
    this.ctx = ctx;
    this.configManager = ConfigurationManager.INSTANCE;

    this.dialog = createDialog(xdlResource, ctx, this);

    initHandlers();
  }

  /**
   * Registers event handlers for dialog controls. This method is called during dialog construction
   * to set up event handling.
   *
   * <p>Subclasses must implement this method to register handlers using {@link
   * #registerHandler(DialogEvents, DialogEventHandler)}.
   */
  protected abstract void initHandlers();

  /**
   * Shows the dialog and waits for it to be closed. This method initializes dialog controls,
   * executes the dialog, and ensures proper cleanup.
   *
   * <p>The dialog is shown modally and execution blocks until the dialog is closed. The finally
   * block ensures that {@link XDialog#endExecute()} is called for proper cleanup.
   */
  public void show() {
    try {
      initDialogControls();
      dialog.execute();
    } finally {
      dialog.endExecute();
    }
  }

  /**
   * Initializes all dialog controls with their default values and states. This method is called
   * after the dialog is created but before it is displayed.
   *
   * <p>Subclasses must implement this method to set up their specific controls, such as list boxes,
   * text fields, checkboxes, etc.
   */
  protected abstract void initDialogControls();

  /**
   * Registers an event handler for a specific dialog event.
   *
   * @param event the dialog event to handle (e.g., button click, value change)
   * @param handler the handler to invoke when the event occurs
   */
  protected void registerHandler(DialogEvents event, DialogEventHandler handler) {
    LOGGER.debug("'{}' - Registering handler for event '{}'", this.name, event);
    handlers.put(event, handler);
  }

  /**
   * Manually triggers a dialog event and invokes its registered handler. This method is useful for
   * programmatically triggering events that would normally be triggered by user interaction.
   *
   * @param event the dialog event to trigger
   * @param dialog the dialog instance
   * @param args additional event arguments to pass to the handler
   * @param triggerer the name of the control or component triggering the event (for logging)
   */
  public void triggerEvent(DialogEvents event, XDialog dialog, Object args, String triggerer) {
    LOGGER.debug("Event '{}' triggered by '{}'", event.id(), triggerer);
    Optional.ofNullable(handlers.get(event))
        .ifPresent(handler -> handler.handle(dialog, args, event.id()));
  }

  /**
   * Called by the UNO framework when a dialog event occurs. This method is part of the {@link
   * XDialogEventHandler} interface implementation.
   *
   * <p>Events occurring before initialization is completed are ignored (returns true immediately).
   * This prevents handlers from being invoked during dialog setup when controls are being populated
   * with initial values.
   *
   * @param dialog the dialog instance where the event occurred
   * @param args additional event arguments provided by the UNO framework
   * @param event the event identifier as a string
   * @return {@code true} if the event was handled, {@code false} otherwise
   */
  @Override
  public boolean callHandlerMethod(XDialog dialog, Object args, String event) {
    if (!initializationCompleted) return true;
    LOGGER.debug("Event '{}' triggered", event);
    return DialogEvents.fromId(event)
        .flatMap(evt -> Optional.ofNullable(handlers.get(evt)))
        .map(
            handler -> {
              handler.handle(dialog, args, event); // Pass context!
              return true;
            })
        .orElse(false);
  }

  /**
   * Returns the names of all supported event handler methods. This method is part of the {@link
   * XDialogEventHandler} interface implementation.
   *
   * <p>The UNO framework uses this information to determine which events this handler can process.
   * Only events returned by this method will be dispatched to {@link #callHandlerMethod(XDialog,
   * Object, String)}.
   *
   * @return an array of event method names that this handler supports
   */
  @Override
  public String[] getSupportedMethodNames() {
    return handlers.keySet().stream().map(DialogEvents::id).toArray(String[]::new);
  }
}
