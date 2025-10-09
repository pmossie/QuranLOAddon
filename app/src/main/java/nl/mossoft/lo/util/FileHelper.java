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

package nl.mossoft.lo.util;

import static nl.mossoft.lo.comp.QuranLOAddonImpl.ADDON_NAME;

import com.sun.star.deployment.PackageInformationProvider;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XURLTransformer;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import nl.mossoft.lo.dialog.UnoOperations;

/**
 * Utility class for file path resolution and resource location.
 *
 * <p>Provides methods to locate application resources within the add-on package, with special
 * handling for dialog files and Quran data files. Uses UNO URL transformation to resolve paths
 * correctly in the LibreOffice environment.
 *
 * @see PackageInformationProvider
 */
public class FileHelper {
  private static final String DIALOG_RESOURCES = "dialogs/";
  private static final String URL_TRANSFORMER_SERVICE = "com.sun.star.util.URLTransformer";

  private FileHelper() {}

  /**
   * Returns the file path to a dialog resource file.
   *
   * @param xdlFile the dialog file name (without path)
   * @param ctx the component context for package location resolution
   * @return the canonical File object pointing to the dialog resource
   * @throws IllegalStateException if the package location cannot be determined
   * @throws IllegalArgumentException if xdlFile is blank
   */
  public static File getDialogFilePath(String xdlFile, XComponentContext ctx) {
    return getFilePath(DIALOG_RESOURCES + xdlFile, ctx);
  }

  /**
   * Resolves a file path within the add-on package.
   *
   * <p>This method:
   *
   * <ol>
   *   <li>Determines the add-on package location
   *   <li>Constructs the complete URL
   *   <li>Transforms the URL using UNO services
   *   <li>Creates a canonical File object
   * </ol>
   *
   * @param fileName the relative file path within the package
   * @param context the component context for package resolution
   * @return the canonical File object for the specified resource
   * @throws IllegalArgumentException if fileName is blank
   * @throws IllegalStateException if package location is unavailable or path resolution fails
   */
  public static File getFilePath(String fileName, XComponentContext context) {
    UnoOperations.requireNonNull(fileName, context);
    if (fileName.isBlank()) {
      throw new IllegalArgumentException("File name must not be blank");
    }

    try {
      String packageLocation = getPackageLocation(context);
      String completeUrl = packageLocation + "/" + fileName;
      return createCanonicalFile(transformUrl(completeUrl, context).Complete);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to resolve file path for: " + fileName, e);
    }
  }

  private static String getPackageLocation(XComponentContext context) {
    String location = PackageInformationProvider.get(context).getPackageLocation(ADDON_NAME);
    if (location == null || location.isBlank()) {
      throw new IllegalStateException("Package location not found for: " + ADDON_NAME);
    }
    return location;
  }

  private static com.sun.star.util.URL transformUrl(String url, XComponentContext context) {
    XURLTransformer transformer =
        UnoOperations.createService(context, URL_TRANSFORMER_SERVICE, XURLTransformer.class);

    com.sun.star.util.URL unoUrl = new com.sun.star.util.URL();
    unoUrl.Complete = url;
    transformer.parseStrict(new com.sun.star.util.URL[] {unoUrl});
    return unoUrl;
  }

  private static File createCanonicalFile(String path) throws IOException {
    try {
      return new File(new URI(path).getPath()).getCanonicalFile();
    } catch (URISyntaxException | IOException e) {
      return new File(path).getCanonicalFile();
    }
  }
}
