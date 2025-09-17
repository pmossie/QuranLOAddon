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
import java.util.stream.Stream;

public enum DialogAction {
  SHOW_MAIN_DIALOG("ShowMainDialog"),
  SHOW_ABOUT_DIALOG("ShowAboutDialog");

  private final String id;

  DialogAction(String id) {
    this.id = id;
  }

  public static Optional<DialogAction> fromId(String id) {
    return Stream.of(values()).filter(a -> a.id.equals(id)).findFirst();
  }

  public String id() {
    return id;
  }
}
