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

import com.sun.star.awt.XDialog;
import com.sun.star.awt.XDialogEventHandler;
import com.sun.star.awt.XDialogProvider2;
import com.sun.star.lang.Locale;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XNumberFormatsSupplier;
import com.sun.star.util.XNumberFormatter;
import java.io.File;
import nl.mossoft.lo.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class DialogHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(DialogHelper.class);

  /**
   * Create a dialog from a xdl definition file.
   *
   * @param filename The filename of the xdl definition file
   * @param ctx component context
   * @param handler event handler
   * @return XDialog
   */
  public static XDialog createDialog(
      String filename, XComponentContext ctx, XDialogEventHandler handler) {
    LOGGER.debug("Create dialog: '{}'", filename.split("\\.")[0]);
    Object oDialogProvider;
    try {
      oDialogProvider =
          ctx.getServiceManager()
              .createInstanceWithContext("com.sun.star.awt.DialogProvider2", ctx);
      XDialogProvider2 xDialogProv =
          UnoRuntime.queryInterface(XDialogProvider2.class, oDialogProvider);
      File dialogFile = FileHelper.getDialogFilePath(filename, ctx);
      return xDialogProv.createDialogWithHandler(convertToURL(ctx, dialogFile), handler);
    } catch (Exception e) {
      return null;
    }
  }

  /** Returns a URL to be used with XDialogProvider to create a dialog */
  public static String convertToURL(XComponentContext xContext, File dialogFile) {
    String sURL;
    try {
      com.sun.star.ucb.XFileIdentifierConverter xFileConverter =
          UnoRuntime.queryInterface(
              com.sun.star.ucb.XFileIdentifierConverter.class,
              xContext
                  .getServiceManager()
                  .createInstanceWithContext("com.sun.star.ucb.FileContentProvider", xContext));
      sURL = xFileConverter.getFileURLFromSystemPath("", dialogFile.getAbsolutePath());
    } catch (com.sun.star.uno.Exception ex) {
      return null;
    }
    return sURL;
  }

  /**
   * Convert boolean to short.
   *
   * @param b the boolean
   * @return the short
   */
  public static short boolean2Short(final boolean b) {
    return (short) (b ? 1 : 0);
  }

  /**
   * Convert short to boolean.
   *
   * @param s the short
   * @return the boolean
   */
  public static boolean short2Boolean(final short s) {
    return s != 0;
  }

  public static String getLocalizedSize(XComponentContext context, float size) {
    try {
      // Create NumberFormatsSupplier
      Object supplierObj =
          context
              .getServiceManager()
              .createInstanceWithContext("com.sun.star.util.NumberFormatsSupplier", context);
      XNumberFormatsSupplier supplier =
          UnoRuntime.queryInterface(XNumberFormatsSupplier.class, supplierObj);

      // Create NumberFormatter
      Object formatterObj =
          context
              .getServiceManager()
              .createInstanceWithContext("com.sun.star.util.NumberFormatter", context);
      XNumberFormatter formatter = UnoRuntime.queryInterface(XNumberFormatter.class, formatterObj);
      formatter.attachNumberFormatsSupplier(supplier);

      // Get format key for "0.0" with auto-locale
      XNumberFormats formats = supplier.getNumberFormats();
      int formatKey =
          formats.queryKey("0.#", new Locale(), true); // Empty locale = use current UI locale

      return formatter.convertNumberToString(formatKey, size);

    } catch (Exception e) {
      LOGGER.error(String.valueOf(e));
      return "";
    }
  }
}
