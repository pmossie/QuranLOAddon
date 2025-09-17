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
import java.util.Objects;
import nl.mossoft.lo.dialog.AboutDialog;
import nl.mossoft.lo.dialog.DialogAction;
import nl.mossoft.lo.dialog.MainDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QuranLOAddonImpl implements XServiceInfo, XJobExecutor {
  private static final Logger LOGGER = LoggerFactory.getLogger(QuranLOAddonImpl.class);

  public static final String ADDON_NAME = "nl.mossoft.lo.QuranLOAddon";
  private static final String IMPLEMENTATION_NAME = QuranLOAddonImpl.class.getName();
  private static final String[] SERVICE_NAMES = {ADDON_NAME};

  private final XComponentContext ctx;

  public QuranLOAddonImpl(XComponentContext ctx) {
    this.ctx = Objects.requireNonNull(ctx, "componentContext must not be null");
  }

  /**
   * Get a Component Factory for the service to be implemented.
   *
   * @param implementationName name of the service to be implemented
   * @return Component Factory for the service
   */
  public static XSingleComponentFactory __getComponentFactory(final String implementationName) {
    XSingleComponentFactory singleComponentFactory = null;

    if (implementationName.equals(IMPLEMENTATION_NAME)) {
      singleComponentFactory =
          Factory.createComponentFactory(QuranLOAddonImpl.class, SERVICE_NAMES);
    }
    return singleComponentFactory;
  }

  /**
   * Registers the service to be implemented.
   *
   * @param registryKey the registration key
   * @return true if successful
   */
  public static boolean __writeRegistryServiceInfo(final XRegistryKey registryKey) {
    return Factory.writeRegistryServiceInfo(IMPLEMENTATION_NAME, SERVICE_NAMES, registryKey);
  }

  @Override
  public String getImplementationName() {
    return IMPLEMENTATION_NAME;
  }

  @Override
  public boolean supportsService(String name) {
    for (String serviceName : SERVICE_NAMES) {
      if (name.equals(serviceName)) return true;
    }
    return false;
  }

  @Override
  public String[] getSupportedServiceNames() {
    return SERVICE_NAMES.clone();
  }

  @Override
  public void trigger(String action) {
    LOGGER.debug("Executing action: '{}'", action);

    DialogAction.fromId(action)
        .ifPresentOrElse(
            a -> {
              switch (a) {
                case SHOW_ABOUT_DIALOG:
                  try {
                    new AboutDialog(ctx).show();
                  } catch (Exception e) {
                    LOGGER.error("Failed to show 'AboutDialog'", e);
                  }
                  break;
                case SHOW_MAIN_DIALOG:
                  try {
                    new MainDialog(ctx).show();
                  } catch (Exception e) {
                    LOGGER.error("Failed to show 'MainDialog'", e);
                  }
                  break;
              }
            },
            () -> LOGGER.debug("trigger({}) called on {}", action, IMPLEMENTATION_NAME));
  }
}
