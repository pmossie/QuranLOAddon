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

/**
 * Utility class for font discovery and management.
 *
 * <p>Automatically detects available system fonts and categorizes them by script support (Arabic
 * vs. Latin). Provides font attributes including proper digit sets, parentheses, and Arabic
 * diacritical marks.
 *
 * <p>All font lists are immutable and deterministically ordered after static initialization.
 *
 * @see FontAttr
 */
public final class FontManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(FontManager.class);

  /** Frozen caches (never mutate after static init). */
  private static final List<FontAttr> ARABIC_FONTS;

  private static final List<FontAttr> LATIN_FONTS;
  private static final Map<String, FontAttr> BY_NAME;

  private static final int ARABIC_INDIC_DIGIT_ZERO = 0x0660;
  private static final int ARABIC_SUKUN = 0x0652;
  private static final int ARABIC_SMALL_DOTLESS_HEAD_OF_KHAH = 0x06E1;
  private static final int ARABIC_SMALL_HIGH_ROUNDED_ZERO = 0x06DF;
  private static final int DIGIT_ZERO = 0x0030;
  private static final int EXTENDED_ARABIC_INDIC_DIGIT_ZERO = 0x06F0;
  private static final int ORNATE_LEFT_PARENTHESIS = 0xFD3E;
  private static final int ORNATE_RIGHT_PARENTHESIS = 0xFD3F;
  // Special KFGQPC number base used for that specific font's digit style.
  private static final int KFGQPC_SPECIAL_NUMBER_BASE = 0xFD50;
  private static final int KFGQPC_SPECIAL_ORNATE_LEFT_PARENTHESIS = 0xFD76;
  private static final int KFGQPC_SPECIAL_ORNATE_RIGHT_PARENTHESIS = 0xFD77;

  private static final String ARABIC_SMALL_HIGH_ROUNDED_ZERO_STR = "۟";
  private static final String ARABIC_SUKUN_STR = "ْ";

  static {
    List<FontAttr> arabic = new ArrayList<>();
    List<FontAttr> latin = new ArrayList<>();
    Map<String, FontAttr> byName = new HashMap<>();

    try {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] allFonts = ge.getAvailableFontFamilyNames();

      // Deterministic order
      Arrays.sort(allFonts, String.CASE_INSENSITIVE_ORDER);

      for (String fontName : allFonts) {
        Font test = new Font(fontName, Font.PLAIN, 10);

        // Arabic-supporting fonts
        if (test.canDisplay(0x0627)) { // Arabic ALIF
          FontAttr attr =
              new FontAttr(
                  fontName,
                  determineArabicNumberBase(fontName, test), // Pre-calculate number base
                  // Parentheses & marks as code points
                  pickCodePoint(
                      test,
                      fontName.contains("KFGQPC")
                          ? KFGQPC_SPECIAL_ORNATE_LEFT_PARENTHESIS
                          : ORNATE_LEFT_PARENTHESIS,
                      ')'), // ARABIC ORNATE LEFT PARENTHESIS
                  pickCodePoint(
                      test,
                      fontName.contains("KFGQPC")
                          ? KFGQPC_SPECIAL_ORNATE_RIGHT_PARENTHESIS
                          : ORNATE_RIGHT_PARENTHESIS,
                      '('), // ARABIC ORNATE RIGHT PARENTHESIS
                  pickCodePoint(
                      test, ARABIC_SMALL_DOTLESS_HEAD_OF_KHAH, ARABIC_SUKUN), // SUKUN (optional)
                  pickCodePoint(
                      test,
                      fontName.contains("KFGQPC") ? ARABIC_SUKUN : ARABIC_SMALL_HIGH_ROUNDED_ZERO,
                      0) // ARABIC SMALL HIGH ROUNDED ZERO (optional)
                  );
          arabic.add(attr);
          byName.putIfAbsent(fontName, attr);
        }

        // Latin-supporting fonts
        if (test.canDisplay(0x0061)) { // 'a'
          FontAttr attr =
              new FontAttr(
                  fontName,
                  DIGIT_ZERO, // ASCII digits (0x0030)
                  '(',
                  ')',
                  0,
                  0 // not typically present / needed for Latin
                  );
          latin.add(attr);
          byName.putIfAbsent(fontName, attr);
        }
      }

    } catch (HeadlessException | SecurityException e) {
      // Log the stack trace for better diagnostics
      LOGGER.warn("Font discovery unavailable in this environment: {}", e.toString(), e);
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
   * Determines the appropriate digit set (number base) for an Arabic font.
   *
   * <p>Attempts to use Arabic-Indic digits (0x0660) or Extended Arabic-Indic digits (0x06F0) if
   * supported by the font, falling back to ASCII (0x0030) or KFGQPC special base.
   *
   * @param fontName the font name
   * @param font the font object to check for digit support
   * @return the Unicode code point for the digit set's zero character
   */
  private static int determineArabicNumberBase(String fontName, Font font) {
    if (fontName.contains("KFGQPC")) {
      return KFGQPC_SPECIAL_NUMBER_BASE;
    } // Return KFGQPC Font specific number base
    if (font.canDisplay(ARABIC_INDIC_DIGIT_ZERO)) {
      return ARABIC_INDIC_DIGIT_ZERO;
    } // Arabic-Indic digits
    if (font.canDisplay(EXTENDED_ARABIC_INDIC_DIGIT_ZERO)) {
      return EXTENDED_ARABIC_INDIC_DIGIT_ZERO;
    } // Extended Arabic-Indic
    return DIGIT_ZERO;
  }

  // ---------------------- Public API ----------------------

  /**
   * Determines the appropriate digit set (number base) for a given language and font.
   *
   * <p>For left-to-right languages, returns ASCII digits (0x0030). For right-to-left languages
   * (Arabic), retrieves the pre-calculated number base from the font attributes.
   *
   * @param language the target language for digit rendering
   * @param fontName the font name to look up
   * @return the Unicode code point for the digit set's zero character
   * @throws IllegalArgumentException if the font is Arabic-supported but not found in the manager
   */
  public static int fontNumberBase(SourceLanguage language, String fontName) {
    if (language.wm() == WritingMode2.LR_TB) {
      return DIGIT_ZERO;
    }

    // For RTL languages, rely on the pre-calculated value in FontAttr
    return getFontAttrByName(fontName)
        .orElseThrow(
            () -> new IllegalArgumentException("Cannot find FontAttr for font: " + fontName))
        .numberBase();
  }

  /**
   * Returns an immutable list of fonts that support Arabic script. The list is deterministically
   * ordered case-insensitively.
   *
   * @return immutable list of Arabic-capable font names
   */
  public static List<String> getArabicSupportedFonts() {
    return ARABIC_FONTS.stream().map(FontAttr::fontName).toList();
  }

  /**
   * Returns an immutable list of fonts that support Latin script. The list is deterministically
   * ordered case-insensitively.
   *
   * @return immutable list of Latin-capable font names
   */
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
    List<String> arabicFonts = getArabicSupportedFonts();
    if (arabicFonts.isEmpty()) return "No Arabic fonts found";
    return String.join(", ", arabicFonts);
  }

  public static String getLatinSupportedFontsAsString() {
    List<String> latinFonts = getLatinSupportedFonts();
    if (latinFonts.isEmpty()) return "No Latin fonts found";
    return String.join(", ", latinFonts);
  }

  /**
   * Finds the index of a font in the Arabic fonts list.
   *
   * @param fontName the font name to search for
   * @return the index of the font, or -1 if not found
   * @throws NullPointerException if fontName is {@code null}
   */
  public static int getFontIndexInArabicList(String fontName) {
    Objects.requireNonNull(fontName, "fontName");
    return getArabicSupportedFonts().indexOf(fontName);
  }

  /**
   * Finds the index of a font in the Latin fonts list.
   *
   * @param fontName the font name to search for
   * @return the index of the font, or -1 if not found
   * @throws NullPointerException if fontName is {@code null}
   */
  public static int getFontIndexInLatinList(String fontName) {
    Objects.requireNonNull(fontName, "fontName");
    return getLatinSupportedFonts().indexOf(fontName);
  }

  /**
   * Transforms Arabic text by replacing generic diacritical marks with font-specific equivalents
   * when available.
   *
   * @param text the Arabic text to transform
   * @param fontName the target font for the transformation
   * @return the transformed text with font-specific characters
   * @throws NullPointerException if text or fontName is {@code null}
   * @throws IllegalArgumentException if font attributes are not found for {@code fontName}
   */
  public static String transFonter(String text, String fontName) {
    Objects.requireNonNull(text, "text");
    Objects.requireNonNull(fontName, "fontName");

    // Retrieve FontAttr once
    FontAttr attr =
        getFontAttrByName(fontName)
            .orElseThrow(
                () -> new IllegalArgumentException("No FontAttr found for font: " + fontName));

    // Perform replacements using the retrieved attributes
    return text.replace(ARABIC_SUKUN_STR, attr.sukunStr())
        .replace(ARABIC_SMALL_HIGH_ROUNDED_ZERO_STR, attr.highRoundedZeroStr());
  }

  /**
   * Retrieves font attributes for a given font name.
   *
   * @param fontName the font name to look up
   * @return an Optional containing FontAttr if found, empty otherwise
   * @throws NullPointerException if fontName is {@code null}
   */
  public static Optional<FontAttr> getFontAttrByName(String fontName) {
    Objects.requireNonNull(fontName, "fontName");
    return Optional.ofNullable(BY_NAME.get(fontName));
  }
}
