/*
 * Copyright (c) 2020-2025. <mossie@mossoft.nl>
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package nl.mossoft.lo.util;

public enum ConfigurationKeys {
  ALL_AYAT_CHECK_BOX_STATE("AllAyatCheckBoxState"),
  ARABIC_FONT_BOLD_BUTTON_STATE("ArabicFontBoldButtonState"),
  ARABIC_FONT_ITALIC_BUTTON_STATE("ArabicFontItalicButtonState"),
  ARABIC_FONT_LIST_BOX_ITEM_LIST("ArabicFontListBoxItemList"),
  ARABIC_FONT_LIST_BOX_ITEM_SELECTED("ArabicFontListBoxItemSelected"),
  ARABIC_FONT_WEIGHT_LIST_BOX_ITEM_LIST("ArabicFontWeightListBoxItemList"),
  ARABIC_FONT_WEIGHT_LIST_BOX_ITEM_SELECTED("ArabicFontWeightListBoxItemSelected"),
  ARABIC_FONT_SELECTED("ArabicFontSelected"),
  ARABIC_FONT_SIZE_COMBO_BOX_VALUE("ArabicFontSizeComboBoxValue"),
  ARABIC_LANGUAGE_SELECTED("ArabicLanguageSelected"),
  ARABIC_SOURCE_SELECTED("ArabicSourceSelected"),
  ARABIC_VERSION_CHECK_BOX_STATE("ArabicVersionCheckBoxState"),
  ARABIC_VERSION_LIST_BOX_ITEM_LIST("ArabicVersionListBoxItemList"),
  ARABIC_VERSION_LIST_BOX_ITEM_SELECTED("ArabicVersionListBoxItemSelected"),
  AYAT_FROM_NUMERIC_FIELD_MAX("AyatFromNumericFieldMax"),
  AYAT_FROM_NUMERIC_FIELD_MIN("AyatFromNumericFieldMin"),
  AYAT_FROM_NUMERIC_FIELD_VALUE("AyatFromNumericFieldValue"),
  AYAT_PER_LINE_CHECK_BOX_STATE("AyatPerLineCheckBoxState"),
  AYAT_TO_NUMERIC_FIELD_MAX("AyatToNumericFieldMax"),
  AYAT_TO_NUMERIC_FIELD_MIN("AyatToNumericFieldMin"),
  AYAT_TO_NUMERIC_FIELD_VALUE("AyatToNumericFieldValue"),
  DEFAULT_FONT_SIZES("DefaultFontSizes"),
  LATIN_FONT_BOLD_BUTTON_STATE("LatinFontBoldButtonState"),
  LATIN_FONT_ITALIC_BUTTON_STATE("LatinFontItalicButtonState"),
  LATIN_FONT_LIST_BOX_ITEM_LIST("LatinFontListBoxItemList"),
  LATIN_FONT_LIST_BOX_ITEM_SELECTED("LatinFontListBoxItemSelected"),
  LATIN_FONT_SELECTED("LatinFontSelected"),
  LATIN_FONT_SIZE_COMBO_BOX_VALUE("LatinFontSizeComboBoxValue"),
  LOCALE("Locale"),
  SURAH_LIST_BOX_ITEM_LIST("SurahListBoxItemList"),
  SURAH_LIST_BOX_ITEM_SELECTED("SurahListBoxItemSelected"),
  SURAH_SELECTED("SurahSelected"),
  TRANSLATION_LANGUAGE_SELECTED("TranslationLanguageSelected"),
  TRANSLATION_SOURCE_SELECTED("TranslationSourceSelected"),
  TRANSLATION_VERSION_CHECK_BOX_STATE("TranslationVersionCheckBoxState"),
  TRANSLATION_VERSION_LIST_BOX_ITEM_LIST("TranslationVersionListBoxItemList"),
  TRANSLATION_VERSION_LIST_BOX_ITEM_SELECTED("TranslationVersionListBoxItemSelected"),
  TRANSLITERATION_LANGUAGE_SELECTED("TransliterationLanguageSelected"),
  TRANSLITERATION_SOURCE_SELECTED("TransliterationSourceSelected"),
  TRANSLITERATION_VERSION_CHECK_BOX_STATE("TransliterationVersionCheckBoxState"),
  TRANSLITERATION_VERSION_LIST_BOX_ITEM_LIST("TransliterationVersionListBoxItemList"),
  TRANSLITERATION_VERSION_LIST_BOX_ITEM_SELECTED("TransliterationVersionListBoxItemSelected");

  /* the name of the Configuration Key */
  private final String key;

  ConfigurationKeys(String key) {
    this.key = key;
  }

  public static ConfigurationKeys fromKey(String key) {
    for (ConfigurationKeys configKey : values()) {
      if (configKey.getKey().equals(key)) {
        return configKey;
      }
    }
    return null; // Return null if no match is found
  }

  public String getKey() {
    return key;
  }

  @Override
  public String toString() {
    return key;
  }
}
