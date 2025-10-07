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

public enum DialogEvents {
  ON_ABOUT_DIALOG_CLOSE_BUTTON_CLICKED("onAboutDialogCloseButtonClicked"),
  ON_ALL_AYAT_CHECK_BUTTON_PRESSED("onAllAyatCheckButtonPressed"),
  ON_ARABIC_FONT_LIST_BOX_ITEM_SELECTED("onArabicFontListBoxItemSelected"),
  ON_ARABIC_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onArabicFontListBoxPropertyEnabledToBeChanged"),
  ON_ARABIC_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onArabicFontSizeComboBoxPropertyEnabledToBeChanged"),
  ON_ARABIC_FONT_SIZE_COMBO_BOX_VALUE_CHANGED("onArabicFontSizeComboBoxValueChanged"),
  ON_ARABIC_VERSION_CHECK_BUTTON_PRESSED("onArabicVersionCheckButtonPressed"),
  ON_ARABIC_VERSION_LIST_BOX_ITEM_SELECTED("onArabicVersionListBoxItemSelected"),
  ON_ARABIC_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onArabicVersionListBoxPropertyEnabledToBeChanged"),
  ON_AYAT_FROM_NUMERIC_FIELD_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onAyatFromNumericFieldPropertyEnabledToBeChanged"),
  ON_AYAT_FROM_NUMERIC_FIELD_VALUE_CHANGED("onAyatFromNumericFieldValueChanged"),
  ON_AYAT_FROM_NUMERIC_FIELD_VALUE_UPDATED("onAyatFromNumericFieldValueUpdated"),
  ON_AYAT_RANGE_LABEL_PROPERTY_ENABLED_TO_BE_CHANGED("onAyatRangeLabelPropertyEnabledToBeChanged"),
  ON_AYAT_PER_LINE_CHECK_BUTTON_PRESSED("onAyatPerLineCheckButtonPressed"),
  ON_AYAT_RANGE_SEPARATOR_LABEL_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onAyatRangeSeparatorLabelPropertyEnabledToBeChanged"),
  ON_AYAT_TO_NUMERIC_FIELD_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onAyatToNumericFieldPropertyEnabledToBeChanged"),
  ON_AYAT_TO_NUMERIC_FIELD_VALUE_CHANGED("onAyatToNumericFieldValueChanged"),
  ON_AYAT_TO_NUMERIC_FIELD_VALUE_UPDATED("onAyatToNumericFieldValueUpdated"),
  ON_INSERT_BUTTON_CLICKED("onInsertButtonClicked"),
  ON_INSERT_BUTTON_PROPERTY_ENABLED_TO_BE_CHANGED("onInsertButtonPropertyEnabledToBeChanged"),
  ON_LATIN_FONT_LIST_BOX_ITEM_SELECTED("onLatinFontListBoxItemSelected"),
  ON_LATIN_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onLatinFontListBoxPropertyEnabledToBeChanged"),
  ON_LATIN_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onLatinFontSizeComboBoxPropertyEnabledToBeChanged"),
  ON_LATIN_FONT_SIZE_COMBO_BOX_VALUE_CHANGED("onLatinFontSizeComboBoxValueChanged"),
  ON_SURAH_LIST_BOX_ITEM_SELECTED("onSurahListBoxItemSelected"),
  ON_TRANSLATION_VERSION_CHECK_BUTTON_PRESSED("onTranslationVersionCheckButtonPressed"),
  ON_TRANSLATION_VERSION_LIST_BOX_ITEM_SELECTED("onTranslationVersionListBoxItemSelected"),
  ON_TRANSLATION_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onTranslationVersionListBoxPropertyEnabledToBeChanged"),
  ON_TRANSLITERATION_VERSION_CHECK_BUTTON_PRESSED("onTransliterationVersionCheckButtonPressed"),
  ON_TRANSLITERATION_VERSION_LIST_BOX_ITEM_SELECTED("onTransliterationVersionListBoxItemSelected"),
  ON_TRANSLITERATION_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED(
      "onTransliterationVersionListBoxPropertyEnabledToBeChanged");

  private final String id;

  DialogEvents(String id) {
    this.id = id;
  }

  public static Optional<DialogEvents> fromId(String id) {
    return Stream.of(values()).filter(a -> a.id.equals(id)).findFirst();
  }

  public String id() {
    return id;
  }
}
