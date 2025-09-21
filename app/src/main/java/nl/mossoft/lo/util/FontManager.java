/*
 * Copyright (C) 2020-2024 <mossie@mossoft.nl>
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

package nl.mossoft.lo.util;

import com.sun.star.text.WritingMode2;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.util.*;
import nl.mossoft.lo.quran.SourceLanguage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FontManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(FontManager.class);

  /** Frozen caches (never mutate after static init). */
  private static final List<FontAttr> ARABIC_FONTS;

  private static final List<FontAttr> LATIN_FONTS;
  private static final Map<String, FontAttr> BY_NAME;

  static {
    List<FontAttr> arabic = new ArrayList<>();
    List<FontAttr> latin = new ArrayList<>();
    Map<String, FontAttr> byName = new HashMap<>();

    try {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] allFonts = ge.getAvailableFontFamilyNames();

      // Deterministic order
      Arrays.sort(allFonts, String.CASE_INSENSITIVE_ORDER);

      for (String font : allFonts) {
        Font test = new Font(font, Font.PLAIN, 10);

        // Arabic-supporting fonts
        if (test.canDisplay(0x0627)) { // Arabic ALEF
          FontAttr attr =
              new FontAttr(
                  font,
                  fontNumberBase(SourceLanguage.ARABIC, font),
                  // Parentheses & marks as code points
                  pickCodePoint(
                      test,
                      font.contains("KFGQPC") ? ' ' : 0xFD3E,
                      ')'), // ARABIC ORNATE LEFT PARENTHESIS
                  pickCodePoint(
                      test,
                      font.contains("KFGQPC") ? ' ' : 0xFD3F,
                      '('), // ARABIC ORNATE RIGHT PARENTHESIS
                  pickCodePoint(test, 0x0652, 0), // SUKUN (optional)
                  pickCodePoint(test, 0x06E0, 0) // ARABIC SMALL HIGH ROUNDED ZERO (optional)
                  );
          arabic.add(attr);
          byName.putIfAbsent(font, attr);
        }

        // Latin-supporting fonts
        if (test.canDisplay(0x0061)) { // 'a'
          FontAttr attr =
              new FontAttr(
                  font,
                  0x0030, // ASCII digits
                  '(',
                  ')',
                  0,
                  0 // not typically present / needed
                  );
          latin.add(attr);
          byName.putIfAbsent(font, attr);
        }
      }

    } catch (HeadlessException | SecurityException e) {
      LOGGER.warn("Font discovery unavailable in this environment: {}", e.toString());
    }

    // Freeze lists and map
    ARABIC_FONTS = List.copyOf(arabic);
    LATIN_FONTS = List.copyOf(latin);
    BY_NAME = Map.copyOf(byName);
  }

  private FontManager() {}

  /** Choose cp if displayable, else fallback (or 0 meaning 'absent'). */
  private static int pickCodePoint(Font font, int cp, int fallbackIfMissing) {
    return font.canDisplay(cp) ? cp : fallbackIfMissing;
  }

  /**
   * Zero codepoint for the digit set to use with this font & language. Returns 0x0030 (ASCII) for
   * LTR, or Arabic-Indic (0x0660) / Extended (0x06F0) if supported.
   */
  static int fontNumberBase(SourceLanguage language, String fontName) {
    if (language.wm() == WritingMode2.LR_TB) {
      return 0x0030;
    }
    Font font = new Font(fontName, Font.PLAIN, 10);
    if (font.canDisplay(0x0660)) return 0x0660; // Arabic-Indic digits
    if (font.canDisplay(0x06F0)) return 0x06F0; // Extended Arabic-Indic
    return 0x0030;
  }

  // ---------------------- Public API ----------------------

  /** Fonts that support Arabic (frozen list, deterministically ordered). */
  public static List<String> getArabicSupportedFonts() {
    return ARABIC_FONTS.stream().map(FontAttr::fontName).toList();
  }

  /** Fonts that support Latin (frozen list, deterministically ordered). */
  public static List<String> getLatinSupportedFonts() {
    return LATIN_FONTS.stream().map(FontAttr::fontName).toList();
  }

  public static String[] getLatinSupportedFontsAsArray() {
    return getLatinSupportedFonts().toArray(String[]::new);
  }

  public static String[] getArabicSupportedFontsAsArray() {
    return getArabicSupportedFonts().toArray(String[]::new);
  }

  public static String getArabicSupportedFontsAsString() {
    if (ARABIC_FONTS.isEmpty()) return "No Arabic fonts found";
    return String.join(", ", getArabicSupportedFonts());
  }

  public static String getLatinSupportedFontsAsString() {
    if (LATIN_FONTS.isEmpty()) return "No Latin fonts found";
    return String.join(", ", getLatinSupportedFonts());
  }

  public static int getFontIndexInArabicList(String fontName) {
    Objects.requireNonNull(fontName, "fontName");
    return getArabicSupportedFonts().indexOf(fontName);
  }

  public static int getFontIndexInLatinList(String fontName) {
    Objects.requireNonNull(fontName, "fontName");
    return getLatinSupportedFonts().indexOf(fontName);
  }

  /** Fast lookup: full attributes for a font if known. */
  public static Optional<FontAttr> getFontAttrByName(String fontName) {
    Objects.requireNonNull(fontName, "fontName");
    return Optional.ofNullable(BY_NAME.get(fontName));
  }
}
