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

/**
 * Functional interface for handling dialog events in the Quran LibreOffice Addon.
 *
 * <p>This interface defines a single method for processing dialog events such as button clicks,
 * value changes, or property modifications. Implementations of this interface are registered with
 * {@link BaseDialog} to respond to specific user interactions or programmatic events.
 *
 * <p>Being a functional interface, it can be implemented using lambda expressions:
 *
 * <pre>{@code
 * registerHandler(DialogEvents.ON_BUTTON_CLICKED,
 *     (dialog, args, event) -> handleButtonClick(dialog));
 * }</pre>
 *
 * @see BaseDialog#registerHandler(DialogEvents, DialogEventHandler)
 * @see BaseDialog#triggerEvent(DialogEvents, XDialog, Object, String)
 */
@FunctionalInterface
public interface DialogEventHandler {
  /**
   * Handles a dialog event.
   *
   * <p>This method is invoked when a registered dialog event occurs, either through user
   * interaction (e.g., clicking a button) or programmatically via {@link
   * BaseDialog#triggerEvent(DialogEvents, XDialog, Object, String)}.
   *
   * @param dialog the dialog instance where the event occurred
   * @param args additional event arguments provided by the UNO framework or caller; may be {@code
   *     null} depending on the event type
   * @param event the string identifier of the event that occurred
   */
  void handle(XDialog dialog, Object args, String event);
}
