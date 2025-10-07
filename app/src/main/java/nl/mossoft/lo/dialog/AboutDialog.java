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

/**
 * Creates and manages the About dialog for the Quran LibreOffice Addon. This dialog displays
 * version information, build timestamp, and copyright notice.
 */
public class AboutDialog extends BaseDialog {
  /** Control ID for the About dialog */
  public static final String ABOUT_DIALOG = "AboutDialog";

  /** Control ID for the Build label displaying build timestamp */
  public static final String BUILD_LABEL = "BuildLabel";

  /** Control ID for the Copyright label */
  public static final String COPYRIGHT_LABEL = "CopyrightLabel";

  /** Control ID for the Version label */
  public static final String VERSION_LABEL = "VersionLabel";

  /** Handler method name for the Close button click event */
  public static final String HANDLE_CLOSE_BUTTON_CLICKED = "handleCloseButtonClicked";

  /* Handlers*/
  private static final Logger LOGGER = LoggerFactory.getLogger(AboutDialog.class);

  /**
   * Constructs a new AboutDialog instance.
   *
   * @param ctx the UNO component context
   */
  public AboutDialog(XComponentContext ctx) {
    super(ctx, ABOUT_DIALOG + ".xdl");
  }

  /**
   * Initializes all dialog controls with their content. Sets up version, build timestamp, and
   * copyright information.
   */
  @Override
  protected void initDialogControls() {
    initVersionLabel();
    initBuildLabel();
    initCopyrightLabel();

    initializationCompleted = true;
  }

  /** Initializes the Copyright label with copyright notice text. */
  private void initCopyrightLabel() {
    XFixedText copyrightLabel = getUnoControl(this.dialog, XFixedText.class, COPYRIGHT_LABEL);
    copyrightLabel.setText(getCopyright());
  }

  /**
   * Returns the copyright notice for the QuranLOAddon.
   *
   * @return the copyright string
   */
  private static String getCopyright() {
    return "Copyright © 2020-" + getBuildTimestamp().substring(0, 4) + " <mossie@mossoft.nl>";
  }

  /** Initializes the Build label with build timestamp information. */
  private void initBuildLabel() {
    XFixedText buildLabel = getUnoControl(this.dialog, XFixedText.class, BUILD_LABEL);
    buildLabel.setText("(Build-Timestamp: " + getBuildTimestamp() + ")");
  }

  /**
   * Returns the build of the QuranLOAddon.
   *
   * @return the build string
   */
  private static String getBuildTimestamp() {
    return AboutDialog.class.getPackage().getImplementationVersion().split("-")[1];
  }

  /** Initializes the Version label with version information. */
  private void initVersionLabel() {
    XFixedText versionLabel = getUnoControl(this.dialog, XFixedText.class, VERSION_LABEL);
    versionLabel.setText(getVersion());
  }

  /**
   * Returns the version of the QuranLOAddon.
   *
   * @return the version string
   */
  private static String getVersion() {
    return AboutDialog.class.getPackage().getImplementationVersion().split("-")[0];
  }

  /**
   * Registers all event handlers for the About dialog. Currently registers the Close button click
   * handler.
   */
  @Override
  protected void initHandlers() {
    registerHandler(
        DialogEvents.ON_ABOUT_DIALOG_CLOSE_BUTTON_CLICKED, this::handleCloseButtonClicked);
  }

  /**
   * Handles the Close button click event. Closes the About dialog by ending its execution.
   *
   * @param dialog the dialog instance
   * @param args additional event arguments (unused)
   * @param event the event name (unused)
   */
  private void handleCloseButtonClicked(
      XDialog dialog,
      @SuppressWarnings("unused") Object args,
      @SuppressWarnings("unused") String event) {
    LOGGER.debug(HANDLE_CLOSE_BUTTON_CLICKED + "()");
    dialog.endExecute();
  }
}
