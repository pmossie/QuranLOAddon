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

import java.util.Optional;

/**
 * Enumeration of available dialog actions in the Quran LibreOffice Addon. Each action represents a
 * specific dialog that can be displayed to the user.
 *
 * <p>This enum provides a type-safe way to reference dialog actions and includes utilities for
 * converting between action identifiers and enum constants.
 */
public enum DialogAction {
  /** Action to display the main dialog for inserting Quranic text */
  SHOW_MAIN_DIALOG("ShowMainDialog"),

  /** Action to display the about dialog with version and copyright information */
  SHOW_ABOUT_DIALOG("ShowAboutDialog");

  /** The string identifier for this dialog action */
  private final String id;

  /**
   * Constructs a DialogAction with the specified identifier.
   *
   * @param id the string identifier for this action
   */
  DialogAction(String id) {
    this.id = id;
  }

  /**
   * Finds a DialogAction by its string identifier.
   *
   * @param id the string identifier to search for
   * @return an Optional containing the matching DialogAction, or empty if no match is found
   */
  public static Optional<DialogAction> fromId(String id) {
    return java.util.stream.Stream.of(values()).filter(a -> a.id.equals(id)).findFirst();
  }

  /**
   * Returns the string identifier for this dialog action.
   *
   * @return the action identifier
   */
  public String id() {
    return id;
  }
}
