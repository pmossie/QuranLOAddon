/*
 * Copyright (C) 2020-2025 <mossie@mossoft.nl>
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

package nl.mossoft.lo.dialog;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enumeration of all dialog events in the Quran LibreOffice Addon.
 *
 * <p>This enum defines all possible events that can occur within the addon's dialogs, including
 * user interactions (button clicks, value changes) and programmatic events (property changes,
 * validation updates).
 *
 * <p>Events are used in conjunction with {@link DialogEventHandler} to implement the event handling
 * logic for dialog controls.
 *
 * @see DialogEventHandler
 * @see BaseDialog#registerHandler(DialogEvents, DialogEventHandler)
 */
public enum DialogEvents {
  /** Event fired when the Close button in the About dialog is clicked */
  ON_ABOUT_DIALOG_CLOSE_BUTTON_CLICKED("onAboutDialogCloseButtonClicked"),

  /** Event fired when the "All Ayat" checkbox is pressed */
  ON_ALL_AYAT_CHECK_BUTTON_PRESSED("onAllAyatCheckButtonPressed"),

  /** Event fired when an item is selected in the Arabic font list box */
  ON_ARABIC_FONT_LIST_BOX_ITEM_SELECTED("onArabicFontListBoxItemSelected"),

  /** Event fired when the enabled property of the Arabic font list box is about to change */
  ON_ARABIC_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onArabicFontListBoxPropertyEnabledToBeChanged"),

  /** Event fired when the enabled property of the Arabic font size combo box is about to change */
  ON_ARABIC_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onArabicFontSizeComboBoxPropertyEnabledToBeChanged"),

  /** Event fired when the value of the Arabic font size combo box changes */
  ON_ARABIC_FONT_SIZE_COMBO_BOX_VALUE_CHANGED("onArabicFontSizeComboBoxValueChanged"),

  /** Event fired when the Arabic version checkbox is pressed */
  ON_ARABIC_VERSION_CHECK_BUTTON_PRESSED("onArabicVersionCheckButtonPressed"),

  /** Event fired when an item is selected in the Arabic version list box */
  ON_ARABIC_VERSION_LIST_BOX_ITEM_SELECTED("onArabicVersionListBoxItemSelected"),

  /** Event fired when the enabled property of the Arabic version list box is about to change */
  ON_ARABIC_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onArabicVersionListBoxPropertyEnabledToBeChanged"),

  /** Event fired when the enabled property of the "From Ayat" numeric field is about to change */
  ON_AYAT_FROM_NUMERIC_FIELD_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onAyatFromNumericFieldPropertyEnabledToBeChanged"),

  /** Event fired when the value of the "From Ayat" numeric field changes */
  ON_AYAT_FROM_NUMERIC_FIELD_VALUE_CHANGED("onAyatFromNumericFieldValueChanged"),

  /**
   * Event fired when the "From Ayat" numeric field needs to be updated (e.g., after Surah
   * selection)
   */
  ON_AYAT_FROM_NUMERIC_FIELD_VALUE_UPDATED("onAyatFromNumericFieldValueUpdated"),

  /** Event fired when the "Ayat Per Line" checkbox is pressed */
  ON_AYAT_PER_LINE_CHECK_BUTTON_PRESSED("onAyatPerLineCheckButtonPressed"),

  /** Event fired when the enabled property of the Ayat range label is about to change */
  ON_AYAT_RANGE_LABEL_PROPERTY_ENABLED_TO_BE_CHANGED("onAyatRangeLabelPropertyEnabledToBeChanged"),

  /** Event fired when the enabled property of the Ayat range separator label is about to change */
  ON_AYAT_RANGE_SEPARATOR_LABEL_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onAyatRangeSeparatorLabelPropertyEnabledToBeChanged"),

  /** Event fired when the enabled property of the "To Ayat" numeric field is about to change */
  ON_AYAT_TO_NUMERIC_FIELD_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onAyatToNumericFieldPropertyEnabledToBeChanged"),

  /** Event fired when the value of the "To Ayat" numeric field changes */
  ON_AYAT_TO_NUMERIC_FIELD_VALUE_CHANGED("onAyatToNumericFieldValueChanged"),

  /**
   * Event fired when the "To Ayat" numeric field needs to be updated (e.g., after Surah selection)
   */
  ON_AYAT_TO_NUMERIC_FIELD_VALUE_UPDATED("onAyatToNumericFieldValueUpdated"),

  /** Event fired when the Insert button is clicked */
  ON_INSERT_BUTTON_CLICKED("onInsertButtonClicked"),

  /** Event fired when the enabled property of the Insert button is about to change */
  ON_INSERT_BUTTON_PROPERTY_ENABLED_TO_BE_CHANGED("onInsertButtonPropertyEnabledToBeChanged"),

  /** Event fired when an item is selected in the Latin font list box */
  ON_LATIN_FONT_LIST_BOX_ITEM_SELECTED("onLatinFontListBoxItemSelected"),

  /** Event fired when the enabled property of the Latin font list box is about to change */
  ON_LATIN_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onLatinFontListBoxPropertyEnabledToBeChanged"),

  /** Event fired when the enabled property of the Latin font size combo box is about to change */
  ON_LATIN_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onLatinFontSizeComboBoxPropertyEnabledToBeChanged"),

  /** Event fired when the value of the Latin font size combo box changes */
  ON_LATIN_FONT_SIZE_COMBO_BOX_VALUE_CHANGED("onLatinFontSizeComboBoxValueChanged"),

  /** Event fired when an item is selected in the Surah list box */
  ON_SURAH_LIST_BOX_ITEM_SELECTED("onSurahListBoxItemSelected"),

  /** Event fired when the Translation version checkbox is pressed */
  ON_TRANSLATION_VERSION_CHECK_BUTTON_PRESSED("onTranslationVersionCheckButtonPressed"),

  /** Event fired when an item is selected in the Translation version list box */
  ON_TRANSLATION_VERSION_LIST_BOX_ITEM_SELECTED("onTranslationVersionListBoxItemSelected"),

  /**
   * Event fired when the enabled property of the Translation version list box is about to change
   */
  ON_TRANSLATION_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onTranslationVersionListBoxPropertyEnabledToBeChanged"),

  /** Event fired when the Transliteration version checkbox is pressed */
  ON_TRANSLITERATION_VERSION_CHECK_BUTTON_PRESSED("onTransliterationVersionCheckButtonPressed"),

  /** Event fired when an item is selected in the Transliteration version list box */
  ON_TRANSLITERATION_VERSION_LIST_BOX_ITEM_SELECTED("onTransliterationVersionListBoxItemSelected"),

  /**
   * Event fired when the enabled property of the Transliteration version list box is about to
   * change
   */
  ON_TRANSLITERATION_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onTransliterationVersionListBoxPropertyEnabledToBeChanged");

  /** The string identifier for this dialog event */
  private final String id;

  /**
   * Constructs a DialogEvents constant with the specified identifier.
   *
   * @param id the string identifier for this event
   */
  DialogEvents(String id) {
    this.id = id;
  }

  /**
   * Finds a DialogEvents constant by its string identifier.
   *
   * @param id the string identifier to search for
   * @return an Optional containing the matching DialogEvents constant, or empty if no match is
   *     found
   */
  public static Optional<DialogEvents> fromId(String id) {
    return Stream.of(values()).filter(a -> a.id.equals(id)).findFirst();
  }

  /**
   * Returns the string identifier for this dialog event.
   *
   * @return the event identifier
   */
  public String id() {
    return id;
  }
}
