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

import com.sun.star.lang.XServiceInfo;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.task.XJobExecutor;
import com.sun.star.uno.XComponentContext;
import java.util.*;
import java.util.function.Consumer;
import nl.mossoft.lo.dialog.AboutDialog;
import nl.mossoft.lo.dialog.DialogAction;
import nl.mossoft.lo.dialog.MainDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LibreOffice Add-on entry point for the QuranLOAddon.
 *
 * <p>Provides UNO service registration, and maps addon toolbar or menu actions to dialogs.
 */
public final class QuranLOAddonImpl implements XServiceInfo, XJobExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuranLOAddonImpl.class);

  public static final String ADDON_NAME = "nl.mossoft.lo.QuranLOAddon";
  private static final String IMPLEMENTATION_NAME = QuranLOAddonImpl.class.getName();
  private static final List<String> SERVICE_NAMES = List.of(ADDON_NAME);

  private final XComponentContext ctx;
  private final Map<DialogAction, Consumer<XComponentContext>> actionHandlers;

  // ---------------- Constructor ----------------

  public QuranLOAddonImpl(final XComponentContext ctx) {
    this.ctx = Objects.requireNonNull(ctx, "componentContext must not be null");
    this.actionHandlers = createActionHandlers();
  }

  // ---------------- Factory & Registry ----------------

  public static XSingleComponentFactory __getComponentFactory(final String implementationName) {
    return IMPLEMENTATION_NAME.equals(implementationName)
        ? Factory.createComponentFactory(
            QuranLOAddonImpl.class, SERVICE_NAMES.toArray(String[]::new))
        : null;
  }

  public static boolean __writeRegistryServiceInfo(final XRegistryKey registryKey) {
    return Factory.writeRegistryServiceInfo(
        IMPLEMENTATION_NAME, SERVICE_NAMES.toArray(String[]::new), registryKey);
  }

  // ---------------- XServiceInfo ----------------

  @Override
  public String getImplementationName() {
    return IMPLEMENTATION_NAME;
  }

  @Override
  public boolean supportsService(final String name) {
    return SERVICE_NAMES.contains(name);
  }

  @Override
  public String[] getSupportedServiceNames() {
    return SERVICE_NAMES.toArray(String[]::new);
  }

  // ---------------- XJobExecutor ----------------

  @Override
  public void trigger(final String action) {
    LOGGER.debug("Executing action: '{}'", action);

    DialogAction.fromId(action)
        .ifPresentOrElse(
            this::executeAction,
            () -> LOGGER.warn("Unknown trigger action '{}' in {}", action, IMPLEMENTATION_NAME));
  }

  // ---------------- Implementation details ----------------

  private void executeAction(DialogAction action) {
    Consumer<XComponentContext> handler = actionHandlers.get(action);
    if (handler == null) {
      LOGGER.warn("No handler registered for DialogAction '{}'", action);
      return;
    }
    try {
      handler.accept(ctx);
    } catch (Exception e) {
      LOGGER.error("Failed to execute handler for action '{}'", action, e);
    }
  }

  private Map<DialogAction, Consumer<XComponentContext>> createActionHandlers() {
    Map<DialogAction, Consumer<XComponentContext>> handlers = new EnumMap<>(DialogAction.class);

    handlers.put(DialogAction.SHOW_ABOUT_DIALOG, c -> new AboutDialog(c).show());
    handlers.put(DialogAction.SHOW_MAIN_DIALOG, c -> new MainDialog(c).show());

    return Collections.unmodifiableMap(handlers);
  }
}
