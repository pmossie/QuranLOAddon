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
 * A utility class that contains constant property names for the UnoControls. This class cannot be
 * instantiated.
 */
public final class UnoControlProperties {
  private static final Logger LOGGER = LoggerFactory.getLogger(UnoControlProperties.class);

  // Properties
  public static final String ALIGN = "Align";
  public static final String CHAR_FONT_NAME = "CharFontName";
  public static final String CHAR_FONT_NAME_COMPLEX = "CharFontNameComplex";
  public static final String CHAR_HEIGHT = "CharHeight";
  public static final String CHAR_HEIGHT_COMPLEX = "CharHeightComplex";
  public static final String CHAR_POSTURE = "CharPosture";
  public static final String DECIMAL_ACCURACY = "DecimalAccuracy";
  public static final String DROPDOWN = "Dropdown";
  public static final String ENABLED = "Enabled";
  public static final String ENABLE_VISIBLE = "EnableVisible";
  public static final String FONT_HEIGHT = "FontHeight";
  public static final String FONT_SLANT = "FontSlant";
  public static final String FONT_WEIGHT = "FontWeight";
  public static final String HEIGHT = "Height";
  public static final String IMAGEURL = "ImageURL";
  public static final String LABEL = "Label";
  public static final String MULTILINE = "MultiLine";
  public static final String NAME = "Name";
  public static final String PARA_ADJUST = "ParaAdjust";
  public static final String POSITION_X = "PositionX";
  public static final String POSITION_Y = "PositionY";
  public static final String PUSHBUTTON_TYPE = "PushButtonType";
  public static final String SELECTED_ITEMS = "SelectedItems";
  public static final String SPIN = "Spin";
  public static final String STATE = "State";
  public static final String STRING_ITEM_LIST = "StringItemList";
  public static final String TITLE = "Title";
  public static final String TOGGLE = "Toggle";
  public static final String TRI_STATE = "TriState";
  public static final String VALUE = "Value";
  public static final String VALUE_MAX = "ValueMax";
  public static final String VALUE_MIN = "ValueMin";
  public static final String VERTICAL_ALIGN = "VerticalAlign";
  public static final String WIDTH = "Width";
  public static final String WRITING_MODE = "WritingMode";

  // Static initializer block for logging
  static {
    LOGGER.debug("UnoControlProperties class loaded successfully");
  }

  // Private constructor to prevent instantiation
  private UnoControlProperties() {
    throw new AssertionError("Cannot instantiate " + getClass());
  }
}
