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

package nl.mossoft.lo.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class FontManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(FontManager.class);

  /** Cached lists to avoid redundant calculations. */
  private static final List<String> ARABIC_FONTS = new ArrayList<>();

  private static final List<String> LATIN_FONTS = new ArrayList<>();

  static {
    initializeFonts();
  }

  /** Initializes Arabic and Latin fonts. */
  private static void initializeFonts() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] allFonts = ge.getAvailableFontFamilyNames();

    for (String font : allFonts) {
      Font testFont = new Font(font, Font.PLAIN, 10);
      if (testFont.canDisplay(0x0627)) { // Arabic character check
        ARABIC_FONTS.add(font);
      }
      if (testFont.canDisplay(0x0061)) { // Latin character check
        LATIN_FONTS.add(font);
      }
    }
  }

  /**
   * Retrieves a list of fonts that support Arabic script.
   *
   * @return List of Arabic-supporting font names.
   */
  public static List<String> getArabicSupportedFonts() {
    return Collections.unmodifiableList(ARABIC_FONTS);
  }

  /**
   * Retrieves a list of fonts that support Latin script.
   *
   * @return List of Latin-supporting font names.
   */
  public static List<String> getLatinSupportedFonts() {
    return Collections.unmodifiableList(LATIN_FONTS);
  }

  /**
   * Retrieves the index of a font in the Arabic fonts list.
   *
   * @param fontName The name of the font.
   * @return The index of the font in the Arabic font list, or -1 if not found.
   */
  public static int getFontIndexInArabicList(String fontName) {
    return ARABIC_FONTS.indexOf(fontName);
  }

  /**
   * Retrieves the index of a font in the Latin fonts list.
   *
   * @param fontName The name of the font.
   * @return The index of the font in the Latin font list, or -1 if not found.
   */
  public static int getFontIndexInLatinList(String fontName) {
    return LATIN_FONTS.indexOf(fontName);
  }

  /**
   * Retrieves Arabic fonts as a comma-separated string.
   *
   * @return A string containing Arabic fonts, or "No Arabic fonts found".
   */
  public static String getArabicSupportedFontsAsString() {
    return ARABIC_FONTS.isEmpty() ? "No Arabic fonts found" : String.join(", ", ARABIC_FONTS);
  }

  /**
   * Retrieves Latin fonts as a comma-separated string.
   *
   * @return A string containing Latin fonts, or "No Latin fonts found".
   */
  public static String getLatinSupportedFontsAsString() {
    return LATIN_FONTS.isEmpty() ? "No Latin fonts found" : String.join(", ", LATIN_FONTS);
  }

  /**
   * Retrieves all Latin fonts as an array.
   *
   * @return An array of Latin font names.
   */
  public static String[] getLatinSupportedFontsAsArray() {
    return LATIN_FONTS.toArray(String[]::new);
  }

  /**
   * Retrieves all Arabic fonts as an array.
   *
   * @return An array of Arabic font names.
   */
  public static String[] getArabicSupportedFontsAsArray() {
    return ARABIC_FONTS.toArray(String[]::new);
  }
}
