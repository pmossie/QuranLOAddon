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

/**
 * Utility class providing helper methods for dialog creation and manipulation in the Quran
 * LibreOffice Addon.
 *
 * <p>This class offers static utility methods for:
 *
 * <ul>
 *   <li>Creating dialogs from XDL definition files
 *   <li>Converting file paths to UNO URLs
 *   <li>Converting between boolean and short values (for UNO checkbox states)
 *   <li>Localizing numeric values for display
 * </ul>
 *
 * <p>This is a utility class and should not be instantiated.
 */
public class DialogHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(DialogHelper.class);

  /**
   * Creates a dialog from an XDL definition file.
   *
   * <p>This method locates the specified XDL resource file within the addon's dialogs directory and
   * creates a UNO dialog instance with the provided event handler.
   *
   * @param filename the filename of the XDL definition file (e.g., "MainDialog.xdl")
   * @param ctx the UNO component context for accessing LibreOffice services
   * @param handler the event handler for processing dialog events
   * @return the created XDialog instance
   * @throws RuntimeException if the dialog cannot be created
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

  /**
   * Converts a file path to a URL format required by XDialogProvider.
   *
   * <p>This method transforms a local file path into a UNO-compatible URL that can be used with the
   * XDialogProvider service to create dialogs.
   *
   * @param xContext the UNO component context
   * @param dialogFile the dialog file to convert to URL format
   * @return the URL string representation of the dialog file
   */
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
   * Converts a boolean value to a short value.
   *
   * <p>This conversion is necessary for UNO checkbox states, where:
   *
   * <ul>
   *   <li>{@code true} is represented as {@code 1}
   *   <li>{@code false} is represented as {@code 0}
   * </ul>
   *
   * @param b the boolean value to convert
   * @return {@code 1} if {@code true}, {@code 0} if {@code false}
   * @see #short2Boolean(short)
   */
  public static short boolean2Short(final boolean b) {
    return (short) (b ? 1 : 0);
  }

  /**
   * Converts a short value to a boolean value.
   *
   * <p>This conversion is necessary for interpreting UNO checkbox states, where:
   *
   * <ul>
   *   <li>Non-zero values are interpreted as {@code true}
   *   <li>Zero is interpreted as {@code false}
   * </ul>
   *
   * @param s the short value to convert
   * @return {@code true} if {@code s} is non-zero, {@code false} otherwise
   * @see #boolean2Short(boolean)
   */
  public static boolean short2Boolean(final short s) {
    return s != 0;
  }

  /**
   * Formats a numeric size value according to the system's locale settings.
   *
   * <p>This method ensures that numeric values (such as font sizes) are displayed using the
   * appropriate decimal separator for the user's locale (e.g., comma vs. period).
   *
   * @param context the UNO component context for accessing locale information
   * @param size the numeric size value to format
   * @return the localized string representation of the size
   */
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
