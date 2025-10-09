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

import com.sun.star.lang.Locale;
import com.sun.star.text.WritingMode2;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of supported Quran source languages. Provides localization information for each
 * language including writing direction and locale.
 */
public enum SourceLanguage {
  ARABIC("Arabic", WritingMode2.RL_TB, makeUnoLocale("ar", "SA")),
  DUTCH("Dutch", WritingMode2.LR_TB, makeUnoLocale("nl", "NL")),
  ENGLISH("English", WritingMode2.LR_TB, makeUnoLocale("en", "US")),
  INDONESIAN("Indonesian", WritingMode2.LR_TB, makeUnoLocale("id", "ID"));

  private static final Map<String, SourceLanguage> BY_ID = new HashMap<>();

  static {
    for (SourceLanguage lang : values()) {
      BY_ID.put(lang.id, lang);
    }
  }

  private final String id;
  private final short wm;
  private final Locale locale;

  SourceLanguage(String id, short wm, Locale locale) {
    this.id = id;
    this.wm = wm;
    this.locale = locale;
  }

  /**
   * Looks up a SourceLanguage by its identifier string.
   *
   * @param id the language identifier (e.g., "Arabic", "English")
   * @return the matching SourceLanguage, or {@code null} if no match found
   */
  public static SourceLanguage fromId(String id) {
    return BY_ID.get(id);
  }

  /**
   * Creates a UNO Locale for the specified language and country.
   *
   * @param lang the language code (e.g., "ar", "en")
   * @param country the country code (e.g., "SA", "US")
   * @return a configured Locale object
   */
  public static Locale makeUnoLocale(String lang, String country) {
    Locale loc = new Locale();
    loc.Language = lang;
    loc.Country = country;
    loc.Variant = "";
    return loc;
  }

  /**
   * Returns the language identifier.
   *
   * @return the language name as identifier
   */
  public String id() {
    return id;
  }

  /**
   * Returns the writing direction.
   *
   * @return writing direction of the language
   */
  public short wm() {
    return wm;
  }

  /**
   * Returns the locale setting for the language.
   *
   * @return the locale setting for the language.
   */
  public Locale locale() {
    return locale;
  }
}
