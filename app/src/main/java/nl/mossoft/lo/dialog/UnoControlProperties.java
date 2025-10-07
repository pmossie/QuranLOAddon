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

package nl.mossoft.lo.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that contains constant property names for UNO controls.
 *
 * <p>This class provides a centralized collection of property name constants used to access and
 * modify properties of UNO dialog controls and text formatting in LibreOffice. Using these
 * constants helps prevent typos and provides compile-time checking for property names.
 *
 * <p>Properties are organized into categories:
 *
 * <ul>
 *   <li>Text character properties (font, size, locale)
 *   <li>Control layout properties (position, size, alignment)
 *   <li>Control state properties (enabled, visible, value)
 *   <li>Control behavior properties (dropdown, multiline, spin)
 * </ul>
 *
 * <p>This is a utility class and cannot be instantiated.
 *
 * @see UnoOperations
 */
public final class UnoControlProperties {
  /** Property for horizontal alignment of control content */
  public static final String ALIGN = "Align";

  // Properties
  // Properties
  /** Property for the font name used for Latin text */
  public static final String CHAR_FONT_NAME = "CharFontName";

  /** Property for the font name used for complex text layout (CTL) such as Arabic */
  public static final String CHAR_FONT_NAME_COMPLEX = "CharFontNameComplex";

  /** Property for the font height (size) for Latin text */
  public static final String CHAR_HEIGHT = "CharHeight";

  /** Property for the font height (size) for complex text layout (CTL) */
  public static final String CHAR_HEIGHT_COMPLEX = "CharHeightComplex";

  /** Property for the locale (language/country) for Latin text */
  public static final String CHAR_LOCALE = "CharLocale";

  /** Property for the locale (language/country) for complex text layout (CTL) */
  public static final String CHAR_LOCALE_COMPLEX = "CharLocaleComplex";

  /** Property to disable spell checking for text */
  public static final String CHAR_NO_SPELL_CHECK = "CharNoSpellCheck";

  /** Property for the font posture (normal, italic, oblique) */
  public static final String CHAR_POSTURE = "CharPosture";

  /** Property for the number of decimal places in numeric fields */
  public static final String DECIMAL_ACCURACY = "DecimalAccuracy";

  /** Property indicating whether a combo box has a dropdown list */
  public static final String DROPDOWN = "Dropdown";

  /** Property indicating whether a control is enabled for user interaction */
  public static final String ENABLED = "Enabled";

  /** Property for enabling visibility control */
  public static final String ENABLE_VISIBLE = "EnableVisible";

  /** Property for the font height (size) of control text */
  public static final String FONT_HEIGHT = "FontHeight";

  /** Property for the font slant (italic angle) */
  public static final String FONT_SLANT = "FontSlant";

  /** Property for the font weight (normal, bold) */
  public static final String FONT_WEIGHT = "FontWeight";

  /** Property for the height of a control */
  public static final String HEIGHT = "Height";

  /** Property for the URL of an image to display in a control */
  public static final String IMAGEURL = "ImageURL";

  /** Property for the label text of a control */
  public static final String LABEL = "Label";

  /** Property indicating whether a text control supports multiple lines */
  public static final String MULTILINE = "MultiLine";

  /** Property for the name identifier of a control */
  public static final String NAME = "Name";

  /** Property for paragraph alignment (left, right, center, justify) */
  public static final String PARA_ADJUST = "ParaAdjust";

  /** Property for the X coordinate position of a control */
  public static final String POSITION_X = "PositionX";

  /** Property for the Y coordinate position of a control */
  public static final String POSITION_Y = "PositionY";

  /** Property for the type of push button (OK, Cancel, Help) */
  public static final String PUSHBUTTON_TYPE = "PushButtonType";

  /** Property for the array of selected item indices in a list box */
  public static final String SELECTED_ITEMS = "SelectedItems";

  /** Property indicating whether a numeric field has spin buttons */
  public static final String SPIN = "Spin";

  /** Property for the state of a checkbox (0=unchecked, 1=checked, 2=indeterminate) */
  public static final String STATE = "State";

  /** Property for the array of string items in a list box or combo box */
  public static final String STRING_ITEM_LIST = "StringItemList";

  /** Property for the title of a dialog or control */
  public static final String TITLE = "Title";

  /** Property indicating whether a button acts as a toggle button */
  public static final String TOGGLE = "Toggle";

  /**
   * Property indicating whether a checkbox supports three states (unchecked, checked,
   * indeterminate)
   */
  public static final String TRI_STATE = "TriState";

  /** Property for the current value of a control (numeric or text) */
  public static final String VALUE = "Value";

  /** Property for the maximum value allowed in a numeric field */
  public static final String VALUE_MAX = "ValueMax";

  /** Property for the minimum value allowed in a numeric field */
  public static final String VALUE_MIN = "ValueMin";

  /** Property for vertical alignment of control content */
  public static final String VERTICAL_ALIGN = "VerticalAlign";

  /** Property for the width of a control */
  public static final String WIDTH = "Width";

  /** Property for the writing mode (left-to-right or right-to-left) */
  public static final String WRITING_MODE = "WritingMode";

  private static final Logger LOGGER = LoggerFactory.getLogger(UnoControlProperties.class);

  // Static initializer block for logging
  static {
    LOGGER.debug("UnoControlProperties class loaded successfully");
  }

  /**
   * Private constructor to prevent instantiation. This is a utility class that should only be used
   * for its static constants.
   *
   * @throws AssertionError if an attempt is made to instantiate this class
   */
  private UnoControlProperties() {
    throw new AssertionError("Cannot instantiate " + getClass());
  }
}
