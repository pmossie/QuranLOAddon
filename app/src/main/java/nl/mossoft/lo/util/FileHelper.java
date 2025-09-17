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

public class FileHelper {
  private static final String DIALOG_RESOURCES = "dialogs/";
  private static final String URL_TRANSFORMER_SERVICE = "com.sun.star.util.URLTransformer";

  private FileHelper() {}

  /** Returns a path to a dialog file */
  public static File getDialogFilePath(String xdlFile, XComponentContext ctx) {
    return getFilePath(DIALOG_RESOURCES + xdlFile, ctx);
  }

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
