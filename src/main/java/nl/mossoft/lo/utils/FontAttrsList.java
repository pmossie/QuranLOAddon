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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type FontAttrsList.
 * <p>
 * Is a singleton class that holds list of Arabic fonts with their attributes that are required for
 * the proper formatting of the Arabic text.
 */
public class FontAttrsList {

  private static final String LEFT_PARENTHESIS = new String(Character.toChars(0xFD3E));
  private static final String RIGHT_PARENTHESIS = new String(Character.toChars(0xFD3F));
  private static final String ARABIC_SMALL_DOTLESS_HEAD_OF_KHAH = new String(
      Character.toChars(0x06E1));
  private static final String ARABIC_SMALL_HIGH_ROUNDED_ZERO = new String(
      Character.toChars(0x06DF));
  private static final String ARABIC_SUKUN = new String(Character.toChars(0x0652));

  private static final Map<String, FontAttrs> fonts = new LinkedHashMap<>();
  private static FontAttrsList instance;
  private final FontAttrs defaultFont = new FontAttrs(LEFT_PARENTHESIS,
      RIGHT_PARENTHESIS, 0x0030,
      ARABIC_SUKUN, ARABIC_SMALL_HIGH_ROUNDED_ZERO);

  private FontAttrsList() {

    fonts.put("Al Qalam Quran Majeed",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS, 0x06F0,
            ARABIC_SUKUN, ARABIC_SMALL_HIGH_ROUNDED_ZERO));
    fonts.put("Al Qalam Quran Majeed 1",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS, 0x06F0,
            ARABIC_SUKUN, ARABIC_SMALL_HIGH_ROUNDED_ZERO));
    fonts.put("Al Qalam Quran Majeed 2",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS, 0x06F0,
            ARABIC_SUKUN, ARABIC_SMALL_HIGH_ROUNDED_ZERO));
    fonts.put("Noto Nastaliq Urdu",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS, 0x0660,
            ARABIC_SUKUN, ARABIC_SMALL_HIGH_ROUNDED_ZERO));
      fonts.put("KFGQPC HAFS Uthmanic Script",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
            0xFD50, ARABIC_SMALL_DOTLESS_HEAD_OF_KHAH, ARABIC_SUKUN));
    fonts.put("me_quran",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS, 0x0660, ARABIC_SUKUN,
            ARABIC_SMALL_HIGH_ROUNDED_ZERO));
    fonts.put("Scheherazade",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS, 0x0660,
            ARABIC_SUKUN, ARABIC_SMALL_HIGH_ROUNDED_ZERO));
    fonts.put("Scheherazade quran",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS, 0x0660,
            ARABIC_SUKUN, ARABIC_SMALL_HIGH_ROUNDED_ZERO));
    fonts.put("Scheherazade New quran",
        new FontAttrs(LEFT_PARENTHESIS, RIGHT_PARENTHESIS, 0x0660,
            ARABIC_SUKUN, ARABIC_SMALL_HIGH_ROUNDED_ZERO));

  }

  /**
   * If the instance is null, create a new instance and return it. Otherwise, return the existing
   * instance.
   *
   * @return The instance of the FontAttrsList class.
   */
  public static FontAttrsList getInstance() {
    if (instance == null) {
      instance = new FontAttrsList();
    }
    return instance;
  }

  /**
   * Gets font number base.
   *
   * @param fontName the font name
   * @return the font number base
   */
  public int getFontNumberBase(String fontName) {
    return (fonts.getOrDefault(fontName, defaultFont)).numberBase;
  }

  /**
   * Gets font left parenthesis.
   *
   * @param fontName the font name
   * @return the font left parenthesis
   */
  public String getFontLeftParenthesis(String fontName) {
    return (fonts.getOrDefault(fontName, defaultFont)).leftParenthesis;
  }

  /**
   * Gets font right parenthesis.
   *
   * @param fontName the font name
   * @return the font right parenthesis
   */
  public String getFontRightParenthesis(String fontName) {
    return (fonts.getOrDefault(fontName, defaultFont)).rightParenthesis;
  }

  /**
   * Gets font sukun.
   *
   * @param fontName the font name
   * @return the font sukun
   */
  public String getFontSukun(String fontName) {
    return (fonts.getOrDefault(fontName, defaultFont)).sukun;
  }

  /**
   * Gets font small high rounded zero.
   *
   * @param fontName the font name
   * @return the font small high rounded zero
   */
  public String getFontSmallHighRoundedZero(String fontName) {
    return (fonts.getOrDefault(fontName, defaultFont)).smallHighRoundedZero;
  }

  /**
   * Has sukun mod boolean.
   *
   * @param fontName the font name
   * @return the boolean
   */
  public boolean hasSukunMod(String fontName) {
    final String sukun = (fonts.getOrDefault(fontName, defaultFont)).sukun;
    return !sukun.equals(ARABIC_SUKUN);
  }

  /**
   * Has small high rounded zero mod boolean.
   *
   * @param fontName the font name
   * @return the boolean
   */
  public boolean hasSmallHighRoundedZeroMod(String fontName) {
    final String smallHighRoundedZero = (fonts.getOrDefault(fontName,
        defaultFont)).smallHighRoundedZero;
    return !smallHighRoundedZero.equals(ARABIC_SMALL_HIGH_ROUNDED_ZERO);
  }

  private class FontAttrs {

    private final String leftParenthesis;
    private final String rightParenthesis;
    private final Integer numberBase;
    private final String sukun;
    private final String smallHighRoundedZero;

    /**
     * Instantiates a new Font attrs.
     *
     * @param leftParenthesis      the left parenthesis
     * @param rightParenthesis     the right parenthesis
     * @param numberBase           the number base
     * @param sukun                the sukun
     * @param smallHighRoundedZero the small high rounded zero
     */
    public FontAttrs(String leftParenthesis,
        String rightParenthesis,
        Integer numberBase, String sukun, String smallHighRoundedZero) {

      this.leftParenthesis = leftParenthesis;
      this.rightParenthesis = rightParenthesis;
      this.numberBase = numberBase;
      this.sukun = sukun;
      this.smallHighRoundedZero = smallHighRoundedZero;
    }
  }
}
