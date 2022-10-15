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
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.uno.XComponentContext;
import nl.mossoft.lo.dialog.InsertQuranTextDialog;

/**
 * The implementatie of the type QuranAddon.
 */
public class QuranAddonImpl
    extends WeakBase
    implements com.sun.star.lang.XServiceInfo,
    com.sun.star.task.XJobExecutor {

  public static final String ADDON_NAME = "nl.mossoft.lo.QuranLOAddon";
  private static final String[] SERVICE_NAMES = {ADDON_NAME};
  private static final String IMPLEMENTATION_NAME = QuranAddonImpl.class.getName();

  private static final String COMMAND_INSERT_QURAN_TEXT = "InsertQuranText";

  private final XComponentContext componentContext;

  /**
   * Instantiates a new Quran addon.
   *
   * @param context the context
   */
  public QuranAddonImpl(XComponentContext context) {
    this.componentContext = context;
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
      singleComponentFactory = Factory.createComponentFactory(QuranAddonImpl.class, SERVICE_NAMES);
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
  public boolean supportsService(final String serviceName) {
    for (String s : SERVICE_NAMES) {
      if (serviceName.equals(s)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String[] getSupportedServiceNames() {
    return SERVICE_NAMES;
  }

  @Override
  public void trigger(final String command) {
    if (command.equals(COMMAND_INSERT_QURAN_TEXT)) {
      InsertQuranTextDialog.loadAddonDialog(componentContext).show();
    }
  }
}
