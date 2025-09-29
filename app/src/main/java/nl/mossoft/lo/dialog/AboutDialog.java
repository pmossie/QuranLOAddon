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

import static nl.mossoft.lo.dialog.UnoOperations.getUnoControl;

import com.sun.star.awt.XDialog;
import com.sun.star.awt.XFixedText;
import com.sun.star.uno.XComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Creates the AboutDialog */
public class AboutDialog extends BaseDialog {
  private static final Logger LOGGER = LoggerFactory.getLogger(AboutDialog.class);

  /* Controls */
  public static final String ABOUT_DIALOG = "AboutDialog";
  public static final String BUILD_LABEL = "BuildLabel";
  public static final String COPYRIGHT_LABEL = "CopyrightLabel";
  public static final String VERSION_LABEL = "VersionLabel";

  /* Handlers*/
  public static final String HANDLE_CLOSE_BUTTON_CLICKED = "handleCloseButtonClicked";

  public AboutDialog(XComponentContext ctx) {
    super(ctx, ABOUT_DIALOG + ".xdl");
  }

  @Override
  protected void initDialogControls() {
    initVersionLabel();
    initBuildLabel();
    initCopyrightLabel();

    initializationCompleted = true;
  }

  private void initCopyrightLabel() {
    XFixedText copyrightLabel = getUnoControl(this.dialog, XFixedText.class, COPYRIGHT_LABEL);
    copyrightLabel.setText(getCopyright());
  }

  /**
   * Returns the copyright notice for the QuranLOAddon.
   *
   * @return the copyright string
   */
  private String getCopyright() {
    return "Copyright © 2020-" + getBuildTimestamp().substring(0, 4) + " <mossie@mossoft.nl>";
  }

  private void initBuildLabel() {
    XFixedText buildLabel = getUnoControl(this.dialog, XFixedText.class, BUILD_LABEL);
    buildLabel.setText("(Build-Timestamp: " + getBuildTimestamp() + ")");
  }

  /**
   * Returns the build of the QuranLOAddon.
   *
   * @return the build string
   */
  private String getBuildTimestamp() {
    return AboutDialog.class.getPackage().getImplementationVersion().split("-")[1];
  }

  private void initVersionLabel() {
    XFixedText versionLabel = getUnoControl(this.dialog, XFixedText.class, VERSION_LABEL);
    versionLabel.setText(getVersion());
  }

  /**
   * Returns the version of the QuranLOAddon.
   *
   * @return the version string
   */
  private String getVersion() {
    return AboutDialog.class.getPackage().getImplementationVersion().split("-")[0];
  }

  @Override
  protected void initHandlers() {
    registerHandler(
        DialogEvents.ON_ABOUT_DIALOG_CLOSE_BUTTON_CLICKED, this::handleCloseButtonClicked);
  }

  private void handleCloseButtonClicked(XDialog dialog, Object args, String event) {
    LOGGER.debug(HANDLE_CLOSE_BUTTON_CLICKED + "()");
    dialog.endExecute();
  }
}
