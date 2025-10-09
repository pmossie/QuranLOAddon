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

/**
 * Immutable record representing font attributes for text rendering.
 *
 * <p>Contains Unicode code points for special characters and formatting used in Quran text
 * rendering, particularly for Arabic text with proper digit sets and decorative elements.
 *
 * @param fontName the name of the font family
 * @param numberBase the Unicode code point for the digit set's zero character
 * @param leftParenthesis the code point for left parenthesis/ornament
 * @param rightParenthesis the code point for right parenthesis/ornament
 * @param sukun the code point for Arabic sukun mark (ﹰ), or 0 if not supported
 * @param highRoundedZero the code point for Arabic small high rounded zero (۟), or 0 if not
 *     supported
 */
public record FontAttr(
    String fontName,
    int numberBase, // code point for 'zero' of the digit set (e.g., 0x0030, 0x0660, 0x06F0)
    int leftParenthesis, // code point (e.g., 0xFD3E or '(')
    int rightParenthesis, // code point (e.g., 0xFD3F or ')')
    int sukun, // code point U+0652 or 0
    int highRoundedZero // code point U+06E0 or 0
    ) {

  /**
   * Returns the left parenthesis as a string if supported.
   *
   * @return the left parenthesis character as string, or empty string if not supported
   */
  public String leftParenthesisStr() {
    return leftParenthesis == 0 ? "" : new String(Character.toChars(leftParenthesis));
  }

  /**
   * Returns the right parenthesis as a string if supported.
   *
   * @return the right parenthesis character as string, or empty string if not supported
   */
  public String rightParenthesisStr() {
    return rightParenthesis == 0 ? "" : new String(Character.toChars(rightParenthesis));
  }

  /**
   * Returns the Arabic sukun mark as a string if supported.
   *
   * @return the sukun character as string, or empty string if not supported
   */
  public String sukunStr() {
    return sukun == 0 ? "" : new String(Character.toChars(sukun));
  }

  /**
   * Returns the Arabic small high rounded zero as a string if supported.
   *
   * @return the high rounded zero character as string, or empty string if not supported
   */
  public String highRoundedZeroStr() {
    return highRoundedZero == 0 ? "" : new String(Character.toChars(highRoundedZero));
  }
}
