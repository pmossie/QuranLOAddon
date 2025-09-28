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

import com.sun.star.text.WritingMode2;
import java.util.HashMap;
import java.util.Map;

public enum SourceLanguage {
  ARABIC("Arabic", WritingMode2.RL_TB),
  DUTCH("Dutch", WritingMode2.LR_TB),
  ENGLISH("English", WritingMode2.LR_TB),
  INDONESIAN("Indonesian", WritingMode2.LR_TB);

  private static final Map<String, SourceLanguage> BY_ID = new HashMap<>();

  static {
    for (SourceLanguage lang : values()) {
      BY_ID.put(lang.id, lang);
    }
  }

  private final String id;
  private final short wm;

  SourceLanguage(String id, short wm) {
    this.id = id;
    this.wm = wm;
  }

  /**
   * Lookup a SourceLanguage by its id string.
   *
   * @param id the id string (e.g. "Arabic")
   * @return the matching SourceLanguage, or null if none matches
   */
  public static SourceLanguage fromId(String id) {
    return BY_ID.get(id);
  }

  public String id() {
    return id;
  }

  public short wm() {
    return wm;
  }
}
