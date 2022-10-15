/*
 * This file is part of QuranLO
 *
 * Copyright (C) 2020-2022 <mossie@mossoft.nl>
 *
 * QuranLO is free software: you can redistribute it and/or modify it under the terms of the GNU
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

package nl.mossoft.lo.utils;

import com.sun.star.text.WritingMode2;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Localization.
 */
public class Localization {

  private Localization() { /* Empty */ }

  /**
   * get the codepoint for zero in the font.
   *
   * @param fontname the fontname
   * @return base font number base
   */
  public static int getFontNumberBase(final String fontname) {
    final Map<String, Integer> fontNumberBaseMap = new LinkedHashMap<>();

    fontNumberBaseMap.put("Al Qalam Quran Majeed", 0x06F0);
    fontNumberBaseMap.put("Al Qalam Quran Majeed 1", 0x06F0);
    fontNumberBaseMap.put("Al Qalam Quran Majeed 2", 0x06F0);
    fontNumberBaseMap.put("Noto Nastaliq Urdu", 0x0660);
    fontNumberBaseMap.put("KFGQPC Uthmanic Script HAFS", 0x0030);
    fontNumberBaseMap.put("me_quran", 0x0660);
    fontNumberBaseMap.put("Scheherazade", 0x0660);
    fontNumberBaseMap.put("Scheherazade quran", 0x0660);
    fontNumberBaseMap.put("Scheherazade New quran", 0x0660);

    return fontNumberBaseMap.getOrDefault(fontname, 0x0030);
  }

  /**
   * Gets language font type.
   *
   * @param language the language
   * @return the font type Arabic of Latin.
   */
  public static String getLanguageFontType(String language) {
    final Map<String, String> languageFontTypeMap = new LinkedHashMap<>();

    // Only specif languages that need a Arabic font (Latin is default).
    languageFontTypeMap.put("Arabic", "Arabic");
    languageFontTypeMap.put("Urdu", "Arabic");

    return languageFontTypeMap.getOrDefault(language, "Latin");
  }

  /**
   * Gets language writing mode.
   *
   * @param language the language
   * @return the language writing mode (Left to Right is default)
   */
  public static short getLanguageWritingMode(final String language) {
    final Map<String, Short> direction = new LinkedHashMap<>();

    // Only specif languages that need a Right to Left (Left to Right is default).
    direction.put("Arabic", com.sun.star.text.WritingMode2.RL_TB);
    direction.put("Urdu", com.sun.star.text.WritingMode2.RL_TB);

    return direction.getOrDefault(language, WritingMode2.LR_TB);
  }
}
