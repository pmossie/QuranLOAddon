/*
 * Copyright (c) 2020-2025. <mossie@mossoft.nl>
 *
 * This is free software:  you can redistribute it and/or modify it under the terms of the  GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/
 */

package nl.mossoft.lo.quran;

import java.util.stream.Stream;

/**
 * Enumeration of Quran source types. Defines categories of Quran texts: original Arabic,
 * translations, and transliterations.
 */
public enum SourceType {
  /** The original Arabic Quran text. */
  ORIGINAL("Original"),
  /** Translated versions of the Quran. */
  TRANSLATION("Translation"),
  /** Phonetic transliterations of the Arabic text. */
  TRANSLITERATION("Transliteration");

  private final String id;

  SourceType(String id) {
    this.id = id;
  }

  public String id() {
    return id;
  }

  /**
   * Returns a stream of all source types.
   *
   * @return stream of all SourceType values
   */
  public static Stream<SourceType> stream() {
    return Stream.of(SourceType.values());
  }
}
