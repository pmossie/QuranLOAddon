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

import com.sun.star.awt.*;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.uno.XComponentContext;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import nl.mossoft.lo.util.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDialog extends WeakBase implements XDialogEventHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(BaseDialog.class);

  protected final XDialog dialog;
  protected final XComponentContext ctx;

  protected final ConfigurationManager configManager;

  private final String name;
  private final Map<DialogEvents, DialogEventHandler> handlers = new EnumMap<>(DialogEvents.class);
  protected boolean initializationCompleted = false;

  protected BaseDialog(XComponentContext ctx, String xdlResource) {
    this.name = xdlResource.split("\\.")[0];
    this.ctx = ctx;
    this.configManager = ConfigurationManager.INSTANCE;

    this.dialog = createDialog(xdlResource, ctx, this);

    initHandlers();
  }

  protected abstract void initDialogControls();

  protected abstract void initHandlers();

  /** Show (and later dispose) the dialog. */
  public void show() {
    try {
      initDialogControls();
      dialog.execute();
    } finally {
      dialog.endExecute();
    }
  }

  protected void registerHandler(DialogEvents event, DialogEventHandler handler) {
    LOGGER.debug("'{}' - Registering handler for event '{}'", this.name, event);
    handlers.put(event, handler);
  }

  public void triggerEvent(DialogEvents event, XDialog dialog, Object args, String triggerer) {
    LOGGER.debug("Event '{}' triggered by '{}'", event.id(), triggerer);
    Optional.ofNullable(handlers.get(event))
        .ifPresent(handler -> handler.handle(dialog, args, event.id()));
  }

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

  @Override
  public String[] getSupportedMethodNames() {
    return handlers.keySet().stream().map(DialogEvents::id).toArray(String[]::new);
  }
}
