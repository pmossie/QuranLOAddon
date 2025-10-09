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

package nl.mossoft.lo.quran;

/**
 * Exception thrown when a requested Quran verse (ayah) cannot be found. This typically occurs when
 * invalid surah or ayah numbers are provided.
 *
 * @see QuranReader
 */
public class QuranReaderAyahNotFoundException extends Exception {
  /**
   * Constructs a new exception with the specified detail message.
   *
   * @param message the detail message explaining which ayah was not found
   */
  public QuranReaderAyahNotFoundException(String message) {
    super(message);
  }
}
