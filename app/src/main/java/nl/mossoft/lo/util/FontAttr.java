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

public record FontAttr(
    String fontName,
    int numberBase, // code point for 'zero' of the digit set (e.g., 0x0030, 0x0660, 0x06F0)
    int leftParenthesis, // code point (e.g., 0xFD3E or '(')
    int rightParenthesis, // code point (e.g., 0xFD3F or ')')
    int sukun, // code point U+0652 or 0
    int highRoundedZero // code point U+06E0 or 0
    ) {
  public String leftParenthesisStr() {
    return leftParenthesis == 0 ? "" : new String(Character.toChars(leftParenthesis));
  }

  public String rightParenthesisStr() {
    return rightParenthesis == 0 ? "" : new String(Character.toChars(rightParenthesis));
  }

  public String sukunStr() {
    return sukun == 0 ? "" : new String(Character.toChars(sukun));
  }

  public String highRoundedZeroStr() {
    return highRoundedZero == 0 ? "" : new String(Character.toChars(highRoundedZero));
  }
}
