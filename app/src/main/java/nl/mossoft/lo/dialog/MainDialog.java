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

import static com.sun.star.text.ControlCharacter.LINE_BREAK;
import static com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseUnsignedInt;
import static nl.mossoft.lo.dialog.DialogEvents.*;
import static nl.mossoft.lo.dialog.DialogHelper.*;
import static nl.mossoft.lo.dialog.UnoControlProperties.*;
import static nl.mossoft.lo.dialog.UnoOperations.*;
import static nl.mossoft.lo.quran.SourceLanguage.makeUnoLocale;
import static nl.mossoft.lo.quran.SourceManager.getSourceInfoOfTypeAsArray;
import static nl.mossoft.lo.quran.SourceType.*;
import static nl.mossoft.lo.quran.SurahManager.getSurahName;
import static nl.mossoft.lo.quran.SurahManager.getSurahSize;
import static nl.mossoft.lo.util.ConfigurationKeys.*;
import static nl.mossoft.lo.util.FileHelper.getFilePath;
import static nl.mossoft.lo.util.FontManager.*;

import com.sun.star.awt.*;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.lang.Locale;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.text.*;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nl.mossoft.lo.quran.QuranReader;
import nl.mossoft.lo.quran.SourceLanguage;
import nl.mossoft.lo.quran.SourceManager;
import nl.mossoft.lo.quran.SourceType;
import nl.mossoft.lo.util.ConfigurationKeys;
import nl.mossoft.lo.util.FontAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates and manages the main dialog for the Quran LibreOffice Addon. This dialog allows users to
 * select Quranic text, configure display options, and insert formatted text into LibreOffice
 * documents.
 */
public class MainDialog extends BaseDialog {
  /* Controls */
  /** Control ID for the "All Ayat" checkbox */
  public static final String ALL_AYAT_CHECK_BOX = "AllAyatCheckBox";

  /** Control ID for the Arabic font list box */
  public static final String ARABIC_FONT_LIST_BOX = "ArabicFontListBox";

  /** Control ID for the Arabic font size combo box */
  public static final String ARABIC_FONT_SIZE_COMBO_BOX = "ArabicFontSizeComboBox";

  /** Control ID for the Arabic version checkbox */
  public static final String ARABIC_VERSION_CHECK_BOX = "ArabicVersionCheckBox";

  /** Control ID for the Arabic version list box */
  public static final String ARABIC_VERSION_LIST_BOX = "ArabicVersionListBox";

  /** Control ID for the "From Ayat" numeric field */
  public static final String AYAT_FROM_NUMERIC_FIELD = "AyatFromNumericField";

  /** Control ID for the "Ayat Per Line" checkbox */
  public static final String AYAT_PER_LINE_CHECK_BOX = "AyatPerLineCheckBox";

  /** Control ID for the "To Ayat" numeric field */
  public static final String AYAT_TO_NUMERIC_FIELD = "AyatToNumericField";

  /** Control ID for the Insert button */
  public static final String INSERT_BUTTON = "InsertButton";

  /** Control ID for the Latin font list box */
  public static final String LATIN_FONT_LIST_BOX = "LatinFontListBox";

  /** Control ID for the Latin font size combo box */
  public static final String LATIN_FONT_SIZE_COMBO_BOX = "LatinFontSizeComboBox";

  /** Control ID for the main dialog */
  public static final String MAIN_DIALOG = "MainDialog";

  /** Control ID for the Surah list box */
  public static final String SURAH_LIST_BOX = "SurahListBox";

  /** Control ID for the Translation version checkbox */
  public static final String TRANSLATION_VERSION_CHECK_BOX = "TranslationVersionCheckBox";

  /** Control ID for the Translation version list box */
  public static final String TRANSLATION_VERSION_LIST_BOX = "TranslationVersionListBox";

  /** Control ID for the Transliteration version checkbox */
  public static final String TRANSLITERATION_VERSION_CHECK_BOX = "TransliterationVersionCheckBox";

  /** Control ID for the Transliteration version list box */
  public static final String TRANSLITERATION_VERSION_LIST_BOX = "TransliterationVersionListBox";

  private static final Logger LOGGER = LoggerFactory.getLogger(MainDialog.class);

  /**
   * Constructs a new MainDialog instance.
   *
   * @param ctx the UNO component context
   */
  public MainDialog(XComponentContext ctx) {
    super(ctx, MAIN_DIALOG + ".xdl");
  }

  /**
   * Initializes all dialog controls with their default values and configurations. This method sets
   * up list boxes, checkboxes, numeric fields, and combo boxes.
   */
  @Override
  protected void initDialogControls() {
    LOGGER.debug("Initializing Dialog Controls");

    initDefaults();

    initSurahListBox();

    initAllAyatCheckBox();
    initAyatFromNumericField();
    initAyatToNumericField();

    initArabicVersionCheckBox();
    initArabicVersionListBox();

    initTranslationVersionCheckBox();
    initTranslationVersionListBox();
    initTransliterationVersionCheckBox();
    initTransliterationVersionListBox();

    initArabicFontListBox();
    initArabicFontSizeComboBox();
    initLatinFontSizeComboBox();
    initLatinFontListBox();

    initAyatPerLineCheckBox();

    initFinalized();

    LOGGER.debug("Initialization Completed");
  }

  /** Marks dialog initialization as complete. */
  private void initFinalized() {
    initializationCompleted = true;
  }

  /** Initializes default configuration values for the dialog. */
  private void initDefaults() {
    LOGGER.debug("initializeDefaults()");

    this.configManager.initializeDefaults(ctx);
    this.configManager.listAllConfigurationKeys();
  }

  /** Initializes the Arabic font size combo box with available font sizes. */
  private void initArabicFontSizeComboBox() {
    LOGGER.debug("initArabicFontSizeComboBox()");

    XComboBox comboBox = getUnoControl(dialog, XComboBox.class, ARABIC_FONT_SIZE_COMBO_BOX);

    initializeFontSizes(comboBox);

    XTextComponent textComponent =
        getUnoControl(dialog, XTextComponent.class, ARABIC_FONT_SIZE_COMBO_BOX);

    textComponent.setText(configManager.getConfig(ARABIC_FONT_SIZE_COMBO_BOX_VALUE) + " pt");
  }

  /**
   * Populates a combo box with the standard set of available font sizes.
   *
   * @param comboBox the combo box to populate
   */
  private void initializeFontSizes(XComboBox comboBox) {
    final Float[] AVAILABLE_FONT_SIZES =
        new Float[] {
          6.0F, 7F, 8F, 9F, 10F, 10.5F, 11F, 12F, 13F, 14F, 15F, 16F, 18F, 20F, 21F, 22F, 24F, 26F,
          28F, 32F, 36F, 40F, 42F, 44F, 48F, 54F, 60F, 66F, 72F, 80F, 88F, 96F
        };

    for (int i = 0; i < AVAILABLE_FONT_SIZES.length; i++) {
      comboBox.addItem(getLocalizedSize(ctx, AVAILABLE_FONT_SIZES[i]) + " pt", (short) i);
    }
  }

  /** Initializes the Latin font size combo box with available font sizes. */
  private void initLatinFontSizeComboBox() {
    LOGGER.debug("initLatinFontSizeComboBox()");

    XComboBox comboBox = getUnoControl(dialog, XComboBox.class, LATIN_FONT_SIZE_COMBO_BOX);

    initializeFontSizes(comboBox);

    XTextComponent textComponent =
        getUnoControl(dialog, XTextComponent.class, LATIN_FONT_SIZE_COMBO_BOX);

    textComponent.setText(configManager.getConfig(LATIN_FONT_SIZE_COMBO_BOX_VALUE) + " pt");
  }

  /** Initializes the Latin font list box with available Latin fonts. */
  private void initLatinFontListBox() {
    LOGGER.debug("initLatinFontListBox()");

    XListBox listBox = getUnoControl(this.dialog, XListBox.class, LATIN_FONT_LIST_BOX);

    listBox.addItems(configManager.getConfig(LATIN_FONT_LIST_BOX_ITEM_LIST).split(", "), (short) 0);
    listBox.selectItemPos(
        (short) parseUnsignedInt(configManager.getConfig(LATIN_FONT_LIST_BOX_ITEM_SELECTED)), true);
  }

  /** Initializes the Arabic font list box with available Arabic fonts. */
  private void initArabicFontListBox() {
    LOGGER.debug("initArabicFontListBox()");

    XListBox listBox = getUnoControl(this.dialog, XListBox.class, ARABIC_FONT_LIST_BOX);

    listBox.addItems(
        configManager.getConfig(ARABIC_FONT_LIST_BOX_ITEM_LIST).split(", "), (short) 0);
    listBox.selectItemPos(
        (short) parseUnsignedInt(configManager.getConfig(ARABIC_FONT_LIST_BOX_ITEM_SELECTED)),
        true);
  }

  /** Initializes the Translation version list box with available translations. */
  private void initTranslationVersionListBox() {
    LOGGER.debug("initTranslationVersionListBox()");

    XListBox listBox = getUnoControl(this.dialog, XListBox.class, TRANSLATION_VERSION_LIST_BOX);

    listBox.addItems(
        configManager.getConfig(TRANSLATION_VERSION_LIST_BOX_ITEM_LIST).split(", "), (short) 0);
    listBox.selectItemPos(
        (short)
            parseUnsignedInt(configManager.getConfig(TRANSLATION_VERSION_LIST_BOX_ITEM_SELECTED)),
        true);
  }

  /** Initializes the Translation version checkbox with its saved state. */
  private void initTranslationVersionCheckBox() {
    LOGGER.debug("initTranslationVersionCheckBox()");

    XCheckBox checkBox = getUnoControl(this.dialog, XCheckBox.class, TRANSLATION_VERSION_CHECK_BOX);

    checkBox.setState(
        boolean2Short(parseBoolean(configManager.getConfig(TRANSLATION_VERSION_CHECK_BOX_STATE))));
  }

  /** Initializes the Transliteration version list box with available transliterations. */
  private void initTransliterationVersionListBox() {
    LOGGER.debug("initTransliterationVersionListBox()");

    XListBox listBox = getUnoControl(this.dialog, XListBox.class, TRANSLITERATION_VERSION_LIST_BOX);

    listBox.addItems(
        configManager.getConfig(TRANSLITERATION_VERSION_LIST_BOX_ITEM_LIST).split(", "), (short) 0);
    listBox.selectItemPos(
        (short)
            parseUnsignedInt(
                configManager.getConfig(TRANSLITERATION_VERSION_LIST_BOX_ITEM_SELECTED)),
        true);
  }

  /** Initializes the Transliteration version checkbox with its saved state. */
  private void initTransliterationVersionCheckBox() {
    LOGGER.debug("initTransliterationVersionCheckBox()");

    XCheckBox checkBox =
        getUnoControl(this.dialog, XCheckBox.class, TRANSLITERATION_VERSION_CHECK_BOX);

    checkBox.setState(
        boolean2Short(
            parseBoolean(configManager.getConfig(TRANSLITERATION_VERSION_CHECK_BOX_STATE))));
  }

  /** Initializes the Arabic version list box with available Arabic versions. */
  private void initArabicVersionListBox() {
    LOGGER.debug("initArabicVersionListBox()");

    XListBox listBox = getUnoControl(this.dialog, XListBox.class, ARABIC_VERSION_LIST_BOX);

    listBox.addItems(
        configManager.getConfig(ARABIC_VERSION_LIST_BOX_ITEM_LIST).split(", "), (short) 0);
    listBox.selectItemPos(
        (short) parseUnsignedInt(configManager.getConfig(ARABIC_VERSION_LIST_BOX_ITEM_SELECTED)),
        true);
  }

  /** Initializes the Arabic version checkbox with its saved state. */
  private void initArabicVersionCheckBox() {
    LOGGER.debug("initArabicVersionCheckBox()");

    XCheckBox checkBox = getUnoControl(this.dialog, XCheckBox.class, ARABIC_VERSION_CHECK_BOX);

    checkBox.setState(
        boolean2Short(parseBoolean(configManager.getConfig(ARABIC_VERSION_CHECK_BOX_STATE))));
  }

  /** Initializes the "To Ayat" numeric field with minimum, maximum, and current values. */
  private void initAyatToNumericField() {
    LOGGER.debug("initAyatToNumericField()");

    XNumericField to = getUnoControl(this.dialog, XNumericField.class, AYAT_TO_NUMERIC_FIELD);

    to.setMin(parseDouble(configManager.getConfig(AYAT_TO_NUMERIC_FIELD_MIN)));
    to.setMax(parseDouble(configManager.getConfig(AYAT_TO_NUMERIC_FIELD_MAX)));
    to.setValue(parseDouble(configManager.getConfig(AYAT_TO_NUMERIC_FIELD_MAX)));
  }

  /** Initializes the "From Ayat" numeric field with minimum, maximum, and current values. */
  private void initAyatFromNumericField() {
    LOGGER.debug("initAyatFromNumericField()");

    XNumericField from = getUnoControl(this.dialog, XNumericField.class, AYAT_FROM_NUMERIC_FIELD);

    from.setMin(parseDouble(configManager.getConfig(AYAT_FROM_NUMERIC_FIELD_MIN)));
    from.setMax(parseDouble(configManager.getConfig(AYAT_FROM_NUMERIC_FIELD_MAX)));
    from.setValue(parseDouble(configManager.getConfig(AYAT_FROM_NUMERIC_FIELD_MIN)));
  }

  /** Initializes the "All Ayat" checkbox with its saved state. */
  private void initAllAyatCheckBox() {
    LOGGER.debug("initAllAyatCheckBox()");

    XCheckBox checkBox = getUnoControl(this.dialog, XCheckBox.class, ALL_AYAT_CHECK_BOX);

    checkBox.setState(
        boolean2Short(parseBoolean(configManager.getConfig(ALL_AYAT_CHECK_BOX_STATE))));
  }

  /** Initializes the Surah list box with all available Surahs. */
  private void initSurahListBox() {
    LOGGER.debug("initSurahListBox()");

    XListBox listBox = getUnoControl(this.dialog, XListBox.class, SURAH_LIST_BOX);

    listBox.addItems(configManager.getConfig(SURAH_LIST_BOX_ITEM_LIST).split(", "), (short) 0);
    listBox.selectItemPos(
        (short) parseUnsignedInt(configManager.getConfig(SURAH_LIST_BOX_ITEM_SELECTED)), true);
  }

  /** Registers all event handlers for dialog controls. */
  @Override
  protected void initHandlers() {
    registerHandler(ON_ALL_AYAT_CHECK_BUTTON_PRESSED, this::handleAllAyatCheckButtonPressed);
    registerHandler(
        ON_ARABIC_FONT_LIST_BOX_ITEM_SELECTED, this::handleArabicFontListBoxItemSelected);
    registerHandler(
        ON_ARABIC_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_ARABIC_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_ARABIC_FONT_SIZE_COMBO_BOX_VALUE_CHANGED,
        this::handleArabicFontSizeComboBoxValueChanged);
    registerHandler(
        ON_ARABIC_VERSION_CHECK_BUTTON_PRESSED, this::handleArabicVersionCheckButtonPressed);
    registerHandler(
        ON_ARABIC_VERSION_LIST_BOX_ITEM_SELECTED, this::handleArabicVersionListBoxItemSelected);
    registerHandler(
        ON_ARABIC_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_AYAT_FROM_NUMERIC_FIELD_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_AYAT_FROM_NUMERIC_FIELD_VALUE_CHANGED, this::handleAyatFromNumericFieldValueChanged);
    registerHandler(
        ON_AYAT_FROM_NUMERIC_FIELD_VALUE_UPDATED, this::handleAyatFromNumericFieldValueUpdated);
    registerHandler(
        ON_AYAT_RANGE_LABEL_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_AYAT_PER_LINE_CHECK_BUTTON_PRESSED, this::handleAyatPerLineCheckButtonPressed);
    registerHandler(
        ON_AYAT_RANGE_SEPARATOR_LABEL_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_AYAT_TO_NUMERIC_FIELD_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_AYAT_TO_NUMERIC_FIELD_VALUE_CHANGED, this::handleAyatToNumericFieldValueChanged);
    registerHandler(
        ON_AYAT_TO_NUMERIC_FIELD_VALUE_UPDATED, this::handleAyatToNumericFieldValueUpdated);
    registerHandler(ON_INSERT_BUTTON_CLICKED, this::handleInsertButtonClicked);
    registerHandler(
        ON_INSERT_BUTTON_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleInsertButtonPropertyEnabledToBeChanged);
    registerHandler(ON_LATIN_FONT_LIST_BOX_ITEM_SELECTED, this::handleLatinFontListBoxItemSelected);
    registerHandler(
        ON_LATIN_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleLatinFontControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_LATIN_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleLatinFontControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_LATIN_FONT_SIZE_COMBO_BOX_VALUE_CHANGED, this::handleLatinFontSizeComboBoxValueChanged);
    registerHandler(ON_SURAH_LIST_BOX_ITEM_SELECTED, this::handleSurahListBoxItemSelected);
    registerHandler(
        ON_TRANSLATION_VERSION_CHECK_BUTTON_PRESSED,
        this::handleTranslationVersionCheckButtonPressed);
    registerHandler(
        ON_TRANSLATION_VERSION_LIST_BOX_ITEM_SELECTED,
        this::handleTranslationVersionListBoxItemSelected);
    registerHandler(
        ON_TRANSLATION_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
    registerHandler(
        ON_TRANSLITERATION_VERSION_CHECK_BUTTON_PRESSED,
        this::handleTransliterationVersionCheckButtonPressed);
    registerHandler(
        ON_TRANSLITERATION_VERSION_LIST_BOX_ITEM_SELECTED,
        this::handleTransliterationVersionListBoxItemSelected);
    registerHandler(
        ON_TRANSLITERATION_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        this::handleControlPropertyEnabledToBeChanged);
  }

  /**
   * Handles changes to the Insert button's enabled state based on version checkbox states.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleInsertButtonPropertyEnabledToBeChanged(
      XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleInsertButtonPropertyEnabledToBeChanged()");

    setPropertyValue(
        dialog,
        INSERT_BUTTON,
        ENABLED,
        parseBoolean(configManager.getConfig((ARABIC_VERSION_CHECK_BOX_STATE)))
            || parseBoolean((configManager.getConfig((TRANSLATION_VERSION_CHECK_BOX_STATE))))
            || parseBoolean(configManager.getConfig(TRANSLITERATION_VERSION_CHECK_BOX_STATE)));
  }

  /**
   * Handles changes to the Latin font size combo box value.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleLatinFontSizeComboBoxValueChanged(XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleLatinFontSizeComboBoxValueChanged()");

    XTextComponent textComponent =
        getUnoControl(dialog, XTextComponent.class, LATIN_FONT_SIZE_COMBO_BOX);

    Optional<Float> size = extractPositivePointSize(textComponent.getText());
    size.ifPresent(
        s -> configManager.setConfig(LATIN_FONT_SIZE_COMBO_BOX_VALUE, String.valueOf(s)));
  }

  /**
   * Handles changes to the Arabic font size combo box value.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleArabicFontSizeComboBoxValueChanged(XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleArabicFontSizeComboBoxValueChanged()");

    XTextComponent textComponent =
        getUnoControl(dialog, XTextComponent.class, ARABIC_FONT_SIZE_COMBO_BOX);

    Optional<Float> size = extractPositivePointSize(textComponent.getText());
    size.ifPresent(
        s -> configManager.setConfig(ARABIC_FONT_SIZE_COMBO_BOX_VALUE, String.valueOf(s)));
  }

  /**
   * Extracts a positive float point size value from a string. Handles both comma and dot as decimal
   * separators and removes "pt" suffix.
   *
   * @param input the input string containing a size value
   * @return an Optional containing the parsed positive float value, or empty if invalid
   */
  public static Optional<Float> extractPositivePointSize(String input) {
    if (input == null) return Optional.empty();

    // Normalize decimal separator: comma → dot
    String normalized = input.trim().toLowerCase().replace("pt", "").trim().replace(',', '.');

    try {
      float value = Float.parseFloat(normalized);
      if (value > 0) {
        return Optional.of(value);
      }
    } catch (NumberFormatException e) {
      // ignore
    }

    return Optional.empty();
  }

  /**
   * Handles selection of an item in the Latin font list box.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleLatinFontListBoxItemSelected(XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleLatinFontListBoxItemSelected()");

    XListBox listBox = getUnoControl(dialog, XListBox.class, LATIN_FONT_LIST_BOX);
    int fontNo = listBox.getSelectedItemPos();

    LOGGER.debug(
        "handleLatinFontListBoxItemSelected() Font '{}' selected",
        getLatinSupportedFontsAsArray()[fontNo]);

    configManager.setConfig(LATIN_FONT_LIST_BOX_ITEM_SELECTED, String.valueOf(fontNo));
    configManager.setConfig(LATIN_FONT_SELECTED, getLatinSupportedFontsAsArray()[fontNo]);
  }

  /**
   * Handles selection of an item in the Arabic font list box.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleArabicFontListBoxItemSelected(XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleArabicFontListBoxItemSelected()");

    XListBox listBox = getUnoControl(dialog, XListBox.class, ARABIC_FONT_LIST_BOX);
    int fontNo = listBox.getSelectedItemPos();

    LOGGER.debug(
        "handleArabicFontListBoxItemSelected() Font '{}' selected",
        getArabicSupportedFontsAsArray()[fontNo]);

    configManager.setConfig(ARABIC_FONT_LIST_BOX_ITEM_SELECTED, String.valueOf(fontNo));
    configManager.setConfig(ARABIC_FONT_SELECTED, getArabicSupportedFontsAsArray()[fontNo]);
  }

  /**
   * Handles selection of an item in the Transliteration version list box.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleTransliterationVersionListBoxItemSelected(
      XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleTransliterationVersionListBoxItemSelected()");

    XListBox listBox = getUnoControl(dialog, XListBox.class, TRANSLITERATION_VERSION_LIST_BOX);
    int versionNo = listBox.getSelectedItemPos();

    configManager.setConfig(
        TRANSLITERATION_VERSION_LIST_BOX_ITEM_SELECTED, String.valueOf(versionNo));
    configManager.setConfig(
        TRANSLITERATION_LANGUAGE_SELECTED,
        String.valueOf(
            SourceManager.getSourceInfoOfTypeAsArray(SourceType.TRANSLITERATION)[versionNo]
                .language()
                .id()));

    configManager.setConfig(
        TRANSLITERATION_SOURCE_SELECTED,
        getSourceInfoOfTypeAsArray(TRANSLITERATION)[versionNo].fileName());
  }

  /**
   * Handles the Transliteration version checkbox state change.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleTransliterationVersionCheckButtonPressed(
      XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleTransliterationVersionCheckButtonPressed()");

    XCheckBox checkBox = getUnoControl(dialog, XCheckBox.class, TRANSLITERATION_VERSION_CHECK_BOX);

    configManager.setConfig(
        TRANSLITERATION_VERSION_CHECK_BOX_STATE,
        String.valueOf(short2Boolean(checkBox.getState())));

    triggerEvent(
        ON_TRANSLITERATION_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        TRANSLITERATION_VERSION_CHECK_BOX);
    triggerEvent(
        ON_LATIN_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        TRANSLITERATION_VERSION_CHECK_BOX);
    triggerEvent(
        ON_LATIN_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        TRANSLITERATION_VERSION_CHECK_BOX);
    triggerEvent(
        ON_INSERT_BUTTON_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        TRANSLITERATION_VERSION_CHECK_BOX);
  }

  /**
   * Handles selection of an item in the Translation version list box.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleTranslationVersionListBoxItemSelected(
      XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleTranslationVersionListBoxItemSelected()");

    XListBox listBox = getUnoControl(dialog, XListBox.class, TRANSLATION_VERSION_LIST_BOX);
    int versionNo = listBox.getSelectedItemPos();

    configManager.setConfig(TRANSLATION_VERSION_LIST_BOX_ITEM_SELECTED, String.valueOf(versionNo));
    configManager.setConfig(
        TRANSLATION_LANGUAGE_SELECTED,
        String.valueOf(
            SourceManager.getSourceInfoOfTypeAsArray(SourceType.TRANSLATION)[versionNo]
                .language()
                .id()));
    configManager.setConfig(
        TRANSLATION_SOURCE_SELECTED, getSourceInfoOfTypeAsArray(TRANSLATION)[versionNo].fileName());
  }

  /**
   * Handles the Translation version checkbox state change.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleTranslationVersionCheckButtonPressed(XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleTranslationVersionCheckButtonPressed()");

    XCheckBox checkBox = getUnoControl(dialog, XCheckBox.class, TRANSLATION_VERSION_CHECK_BOX);

    configManager.setConfig(
        TRANSLATION_VERSION_CHECK_BOX_STATE, String.valueOf(short2Boolean(checkBox.getState())));

    triggerEvent(
        ON_TRANSLATION_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        TRANSLATION_VERSION_CHECK_BOX);
    triggerEvent(
        ON_LATIN_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        TRANSLATION_VERSION_CHECK_BOX);
    triggerEvent(
        ON_LATIN_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        TRANSLATION_VERSION_CHECK_BOX);
    triggerEvent(
        ON_INSERT_BUTTON_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        TRANSLATION_VERSION_CHECK_BOX);
  }

  /**
   * Handles selection of an item in the Arabic version list box.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleArabicVersionListBoxItemSelected(XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleArabicVersionListBoxItemSelected()");

    XListBox listBox = getUnoControl(dialog, XListBox.class, ARABIC_VERSION_LIST_BOX);
    int versionNo = listBox.getSelectedItemPos();

    configManager.setConfig(ARABIC_VERSION_LIST_BOX_ITEM_SELECTED, String.valueOf(versionNo));
    configManager.setConfig(
        ARABIC_SOURCE_SELECTED, getSourceInfoOfTypeAsArray(ORIGINAL)[versionNo].fileName());
  }

  /**
   * Handles the Arabic version checkbox state change.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleArabicVersionCheckButtonPressed(XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleArabicVersionCheckButtonPressed()");

    XCheckBox checkBox = getUnoControl(dialog, XCheckBox.class, ARABIC_VERSION_CHECK_BOX);

    configManager.setConfig(
        ARABIC_VERSION_CHECK_BOX_STATE, String.valueOf(short2Boolean(checkBox.getState())));

    triggerEvent(
        ON_ARABIC_VERSION_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        ARABIC_VERSION_CHECK_BOX);
    triggerEvent(
        ON_ARABIC_FONT_LIST_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        ARABIC_VERSION_CHECK_BOX);
    triggerEvent(
        ON_ARABIC_FONT_SIZE_COMBO_BOX_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        ARABIC_VERSION_CHECK_BOX);
    triggerEvent(
        ON_INSERT_BUTTON_PROPERTY_ENABLED_TO_BE_CHANGED, dialog, null, ARABIC_VERSION_CHECK_BOX);
  }

  /**
   * Handles generic control enabled property changes by toggling the enabled state.
   *
   * @param dialog the dialog instance
   * @param args additional event arguments
   * @param event the event name containing the control ID
   */
  private void handleControlPropertyEnabledToBeChanged(XDialog dialog, Object args, String event) {

    final String controlId =
        event.substring("on".length(), event.length() - "PropertyEnabledToBeChanged".length());

    LOGGER.debug("handle{}PropertyEnabledToBeChanged()", controlId);

    setPropertyValue(
        dialog,
        controlId,
        ENABLED,
        Boolean.FALSE.equals(getPropertyValue(dialog, controlId, "Enabled", Boolean.class)));
  }

  /**
   * Handles enabled property changes for Latin font controls based on translation/transliteration
   * states.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleLatinFontControlPropertyEnabledToBeChanged(
      XDialog xDialog, Object o, String event) {
    final String controlId =
        event.substring("on".length(), event.length() - "PropertyEnabledToBeChanged".length());

    LOGGER.debug("handleLatinFontControlPropertyEnabledToBeChanged()");

    setPropertyValue(
        dialog,
        controlId,
        ENABLED,
        parseBoolean(configManager.getConfig((TRANSLATION_VERSION_CHECK_BOX_STATE)))
            || parseBoolean(configManager.getConfig(TRANSLITERATION_VERSION_CHECK_BOX_STATE)));
  }

  /**
   * Updates the "To Ayat" numeric field based on the selected Surah.
   *
   * @param dialog the dialog instance
   * @param args additional event arguments
   * @param event the event name
   */
  private void handleAyatToNumericFieldValueUpdated(XDialog dialog, Object args, String event) {
    LOGGER.debug("handleAyatToNumericFieldValueUpdated()");

    XNumericField to = getUnoControl(this.dialog, XNumericField.class, AYAT_TO_NUMERIC_FIELD);
    XListBox listBox = getUnoControl(this.dialog, XListBox.class, SURAH_LIST_BOX);
    int selectedItemPos = listBox.getSelectedItemPos();
    int surahSize = getSurahSize(selectedItemPos + 1);

    configManager.setConfig(AYAT_TO_NUMERIC_FIELD_MIN, String.valueOf(1));
    to.setMin(1);

    configManager.setConfig(AYAT_TO_NUMERIC_FIELD_MAX, String.valueOf(surahSize));
    to.setMax(surahSize);

    configManager.setConfig(AYAT_TO_NUMERIC_FIELD_VALUE, String.valueOf(surahSize));
    to.setValue(surahSize);
  }

  /**
   * Updates the "From Ayat" numeric field based on the selected Surah.
   *
   * @param dialog the dialog instance
   * @param args additional event arguments
   * @param event the event name
   */
  private void handleAyatFromNumericFieldValueUpdated(XDialog dialog, Object args, String event) {
    LOGGER.debug("handleAyatFromNumericFieldValueUpdated()");

    XNumericField from = getUnoControl(this.dialog, XNumericField.class, AYAT_FROM_NUMERIC_FIELD);

    XListBox listBox = getUnoControl(this.dialog, XListBox.class, SURAH_LIST_BOX);
    int selectedItemPos = listBox.getSelectedItemPos();
    int surahSize = getSurahSize(selectedItemPos + 1);

    configManager.setConfig(AYAT_FROM_NUMERIC_FIELD_MIN, String.valueOf(1));
    from.setMin(1);

    configManager.setConfig(AYAT_FROM_NUMERIC_FIELD_MAX, String.valueOf(surahSize));
    from.setMax(surahSize);

    configManager.setConfig(AYAT_FROM_NUMERIC_FIELD_VALUE, String.valueOf(1));
    from.setValue(1);
  }

  /**
   * Validates and handles changes to the "From Ayat" numeric field value. Ensures "From" is not
   * greater than "To".
   *
   * @param dialog the dialog instance
   * @param args additional event arguments
   * @param event the event name
   */
  private void handleAyatFromNumericFieldValueChanged(XDialog dialog, Object args, String event) {
    LOGGER.debug("handleAyatFromNumericFieldValueChanged()");

    XNumericField from = getUnoControl(this.dialog, XNumericField.class, AYAT_FROM_NUMERIC_FIELD);
    XNumericField to = getUnoControl(this.dialog, XNumericField.class, AYAT_TO_NUMERIC_FIELD);

    if (from.getValue() > to.getValue()) {
      /* from must be smaller or equal to from */
      LOGGER.debug(
          "VALIDATION ERROR: From {} is larger than to {}", from.getValue(), to.getValue());

      from.setValue(parseDouble(configManager.getConfig(AYAT_FROM_NUMERIC_FIELD_VALUE)));

    } else {
      configManager.setConfig(
          AYAT_FROM_NUMERIC_FIELD_VALUE, String.valueOf((int) Math.round(from.getValue())));
    }
  }

  /**
   * Validates and handles changes to the "To Ayat" numeric field value. Ensures "To" is not less
   * than "From".
   *
   * @param dialog the dialog instance
   * @param args additional event arguments
   * @param event the event name
   */
  private void handleAyatToNumericFieldValueChanged(XDialog dialog, Object args, String event) {
    LOGGER.debug("handleAyatToNumericFieldValueChanged()");

    XNumericField from = getUnoControl(this.dialog, XNumericField.class, AYAT_FROM_NUMERIC_FIELD);
    XNumericField to = getUnoControl(this.dialog, XNumericField.class, AYAT_TO_NUMERIC_FIELD);

    if (to.getValue() < from.getValue()) {
      /* to must be larger than from */
      LOGGER.debug(
          "VALIDATION ERROR: TO {} is smaller than FROM {}", to.getValue(), from.getValue());

      to.setValue(parseDouble(configManager.getConfig(AYAT_TO_NUMERIC_FIELD_VALUE)));

    } else {
      configManager.setConfig(
          AYAT_TO_NUMERIC_FIELD_VALUE, String.valueOf((int) Math.round(to.getValue())));
    }
  }

  /**
   * Handles the "All Ayat" checkbox state change.
   *
   * @param dialog the dialog instance
   * @param args additional event arguments
   * @param event the event name
   */
  private void handleAllAyatCheckButtonPressed(XDialog dialog, Object args, String event) {
    LOGGER.debug("handleAllAyatCheckButtonPressed()");

    XCheckBox checkBox = getUnoControl(dialog, XCheckBox.class, ALL_AYAT_CHECK_BOX);

    configManager.setConfig(
        ALL_AYAT_CHECK_BOX_STATE, String.valueOf(short2Boolean(checkBox.getState())));

    triggerEvent(
        ON_AYAT_RANGE_LABEL_PROPERTY_ENABLED_TO_BE_CHANGED, dialog, null, ALL_AYAT_CHECK_BOX);
    triggerEvent(
        ON_AYAT_FROM_NUMERIC_FIELD_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        ALL_AYAT_CHECK_BOX);
    triggerEvent(
        ON_AYAT_RANGE_SEPARATOR_LABEL_PROPERTY_ENABLED_TO_BE_CHANGED,
        dialog,
        null,
        ALL_AYAT_CHECK_BOX);
    triggerEvent(
        ON_AYAT_TO_NUMERIC_FIELD_PROPERTY_ENABLED_TO_BE_CHANGED, dialog, null, ALL_AYAT_CHECK_BOX);
  }

  /**
   * Handles selection of a Surah in the Surah list box.
   *
   * @param dialog the dialog instance
   * @param args additional event arguments
   * @param event the event name
   */
  private void handleSurahListBoxItemSelected(XDialog dialog, Object args, String event) {
    LOGGER.debug("handleSurahListBoxItemSelected()");

    XListBox listBox = getUnoControl(dialog, XListBox.class, SURAH_LIST_BOX);
    int selectedItemPos = listBox.getSelectedItemPos(); // 0- based
    int surahSize = getSurahSize(selectedItemPos + 1);

    LOGGER.debug(
        "handleSurahListBoxItemSelected() Surah '{} ({} ayat)' selected",
        getSurahName(selectedItemPos + 1),
        surahSize);

    configManager.setConfig(SURAH_LIST_BOX_ITEM_SELECTED, String.valueOf(selectedItemPos));
    configManager.setConfig(SURAH_SELECTED, getSurahName(selectedItemPos + 1));

    triggerEvent(ON_AYAT_FROM_NUMERIC_FIELD_VALUE_UPDATED, dialog, null, SURAH_LIST_BOX);
    triggerEvent(ON_AYAT_TO_NUMERIC_FIELD_VALUE_UPDATED, dialog, null, SURAH_LIST_BOX);
  }

  /**
   * Handles the "Ayat Per Line" checkbox state change.
   *
   * @param xDialog the dialog instance
   * @param o additional event data
   * @param event the event name
   */
  private void handleAyatPerLineCheckButtonPressed(XDialog xDialog, Object o, String event) {
    LOGGER.debug("handleAyatPerLineCheckButtonPressed()");

    XCheckBox checkBox = getUnoControl(dialog, XCheckBox.class, AYAT_PER_LINE_CHECK_BOX);

    configManager.setConfig(
        AYAT_PER_LINE_CHECK_BOX_STATE, String.valueOf(short2Boolean(checkBox.getState())));
  }

  /**
   * Handles the Insert button click event to insert Quranic text into the document.
   *
   * @param dialog the dialog instance
   * @param args additional event arguments
   * @param event the event name
   */
  private void handleInsertButtonClicked(XDialog dialog, Object args, String event) {
    LOGGER.debug("handleInsertButtonClicked()");

    this.configManager.listAllConfigurationKeys();
    try {
      insertQuranText(parseInt(configManager.getConfig(SURAH_LIST_BOX_ITEM_SELECTED)) + 1);
    } catch (PropertyVetoException | WrappedTargetException | UnknownPropertyException e) {
      throw new RuntimeException(e);
    }
    dialog.endExecute();
  }

  /**
   * Inserts Quranic text for the specified Surah into the current document.
   *
   * @param surahNo the Surah number (1-based)
   * @throws PropertyVetoException if a property cannot be set
   * @throws WrappedTargetException if a UNO operation fails
   * @throws UnknownPropertyException if an unknown property is accessed
   */
  private void insertQuranText(int surahNo)
      throws PropertyVetoException, WrappedTargetException, UnknownPropertyException {
    LOGGER.debug("insertQuranText({})", getSurahName(surahNo));

    final XTextDocument textDoc =
        getCurrentDocument(this.ctx)
            .orElseThrow(() -> new IllegalStateException("No current document available"));
    final XController controller = textDoc.getCurrentController();
    final XTextViewCursorSupplier textViewCursorSupplier = getCursorSupplier(controller);
    final XTextViewCursor textViewCursor = textViewCursorSupplier.getViewCursor();
    final XText text = textViewCursor.getText();
    final XTextCursor textCursor = text.createTextCursorByRange(textViewCursor.getStart());
    final XParagraphCursor paragraphCursor =
        UnoRuntime.queryInterface(XParagraphCursor.class, textCursor);

    final XPropertySet paragraphCursorPropertySet = getPropertySet(paragraphCursor);
    paragraphCursorPropertySet.setPropertyValue(
        CHAR_FONT_NAME, configManager.getConfig(LATIN_FONT_SELECTED));
    paragraphCursorPropertySet.setPropertyValue(
        CHAR_HEIGHT, parseDouble(configManager.getConfig(LATIN_FONT_SIZE_COMBO_BOX_VALUE)));
    paragraphCursorPropertySet.setPropertyValue(
        CHAR_FONT_NAME_COMPLEX, configManager.getConfig(ARABIC_FONT_SELECTED));
    paragraphCursorPropertySet.setPropertyValue(
        CHAR_HEIGHT_COMPLEX,
        parseDouble(configManager.getConfig(ARABIC_FONT_SIZE_COMBO_BOX_VALUE)));

    LOGGER.debug("insertQuranText({}) Default Properties set", getSurahName(surahNo));

    try {
      if (parseBoolean(configManager.getConfig(AYAT_PER_LINE_CHECK_BOX_STATE))) {
        writeSurahTextAsAyatPerLine(surahNo, paragraphCursorPropertySet, textViewCursor);
      } else {
        writeSurahTextAsOneBlock(surahNo, paragraphCursorPropertySet, textViewCursor);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Writes Surah text with each Ayat on a separate line.
   *
   * @param surahNo the Surah number
   * @param paragraphCursorPropertySet property set for paragraph formatting
   * @param textViewCursor the text view cursor
   * @throws Exception if an error occurs during text insertion
   */
  private void writeSurahTextAsAyatPerLine(
      int surahNo, XPropertySet paragraphCursorPropertySet, XTextViewCursor textViewCursor)
      throws Exception {
    LOGGER.debug("writeSurahTextAsAyatPerLine({})", surahNo);

    int from = parseInt(configManager.getConfig(AYAT_FROM_NUMERIC_FIELD_VALUE));
    int to = parseInt(configManager.getConfig(AYAT_TO_NUMERIC_FIELD_VALUE));

    boolean arabic = parseBoolean(configManager.getConfig(ARABIC_VERSION_CHECK_BOX_STATE));
    boolean translation =
        parseBoolean(configManager.getConfig(TRANSLATION_VERSION_CHECK_BOX_STATE));
    boolean transliteration =
        parseBoolean(configManager.getConfig(TRANSLITERATION_VERSION_CHECK_BOX_STATE));

    for (int a = from; a <= to; a++) {
      if (arabic) {
        setParagraphDirectionSpellCheckLanguage(
            paragraphCursorPropertySet, ARABIC_LANGUAGE_SELECTED, true);
        writeParagraph(
            textViewCursor,
            getSurahAyatText(surahNo, a, ARABIC_LANGUAGE_SELECTED, ARABIC_SOURCE_SELECTED),
            ParagraphAdjust.RIGHT);
        writeEndOfParagraph(textViewCursor);
      }
      if (transliteration) {
        setParagraphDirectionSpellCheckLanguage(
            paragraphCursorPropertySet, TRANSLITERATION_LANGUAGE_SELECTED, false);
        writeParagraph(
            textViewCursor,
            getSurahAyatText(
                surahNo, a, TRANSLITERATION_LANGUAGE_SELECTED, TRANSLITERATION_SOURCE_SELECTED),
            ParagraphAdjust.BLOCK);
        writeEndOfParagraph(textViewCursor);
      }

      if (translation) {
        setParagraphDirectionSpellCheckLanguage(
            paragraphCursorPropertySet, TRANSLATION_LANGUAGE_SELECTED, true);
        writeParagraph(
            textViewCursor,
            getSurahAyatText(
                surahNo, a, TRANSLATION_LANGUAGE_SELECTED, TRANSLATION_SOURCE_SELECTED),
            ParagraphAdjust.BLOCK);
        writeEndOfParagraph(textViewCursor);
      }
    }
  }

  /**
   * Retrieves formatted text for a specific Ayat of a Surah.
   *
   * @param surahNo the Surah number
   * @param ayatNo the Ayat number
   * @param sourceLanguage the source language configuration key
   * @param source the source file configuration key
   * @return formatted Ayat text with number
   * @throws Exception if an error occurs reading the Quran text
   */
  private String getSurahAyatText(
      int surahNo, int ayatNo, ConfigurationKeys sourceLanguage, ConfigurationKeys source)
      throws Exception {

    try (QuranReader reader = new QuranReader(getFilePath(configManager.getConfig(source), ctx))) {
      String ayat = reader.getAyahNoOfSurahNo(surahNo, ayatNo);

      SourceLanguage language = SourceLanguage.fromId(configManager.getConfig(sourceLanguage));
      short writingMode = language.wm();
      String fontName =
          (writingMode == WritingMode2.LR_TB)
              ? configManager.getConfig(LATIN_FONT_SELECTED)
              : configManager.getConfig(ARABIC_FONT_SELECTED);
      FontAttr fa = getFontAttrByName(fontName).orElseThrow();

      final StringBuilder paragraph = new StringBuilder();
      if (writingMode == WritingMode2.LR_TB) {
        paragraph.append(fa.leftParenthesisStr());
        paragraph.append(numToNumberString(ayatNo, language, fontName));
        paragraph.append(fa.rightParenthesisStr());
        paragraph.append(' ');
        paragraph.append(transFonter(ayat, fontName));
        paragraph.append(' ');
      } else {

        paragraph.append(transFonter(ayat, fontName));
        paragraph.append(fa.rightParenthesisStr());
        paragraph.append(numToNumberString(ayatNo, language, fontName));
        paragraph.append(fa.leftParenthesisStr());
        paragraph.append(' ');
      }
      return paragraph.toString();
    }
  }

  /**
   * Converts a number to its string representation based on language and font.
   *
   * @param n the number to convert
   * @param language the source language
   * @param fontName the font name
   * @return string representation of the Ayat number
   */
  public static String numToNumberString(int n, SourceLanguage language, String fontName) {
    final int base = fontNumberBase(language, fontName);

    final StringBuilder as = new StringBuilder();
    while (n > 0) {
      as.append(Character.toChars(base + (n % 10)));
      n = n / 10;
    }
    if (fontName.contains("KFGQPC")) return as.toString(); // special case
    return as.reverse().toString();
  }

  /**
   * Writes a paragraph of text at the current cursor position.
   *
   * @param textViewCursor the text view cursor
   * @param paragraph the paragraph text to write
   * @param paragraphAdjust the paragraph alignment
   */
  private void writeParagraph(
      XTextViewCursor textViewCursor, String paragraph, ParagraphAdjust paragraphAdjust) {
    final XText text = textViewCursor.getText();
    final XTextCursor textCursor = text.createTextCursorByRange(textViewCursor.getStart());
    final XParagraphCursor paragraphCursor =
        UnoRuntime.queryInterface(XParagraphCursor.class, textCursor);
    final XPropertySet props = getPropertySet(paragraphCursor);
    try {
      props.setPropertyValue(PARA_ADJUST, paragraphAdjust);
    } catch (UnknownPropertyException | PropertyVetoException | WrappedTargetException e) {
      throw new RuntimeException(e);
    }

    text.insertString(paragraphCursor, paragraph, false);
  }

  /**
   * Inserts line break and paragraph break at the current cursor position.
   *
   * @param textViewCursor the text view cursor
   */
  private void writeEndOfParagraph(XTextViewCursor textViewCursor) {
    final XText text = textViewCursor.getText();
    final XTextCursor textCursor = text.createTextCursorByRange(textViewCursor.getStart());

    final XParagraphCursor paragraphCursor =
        UnoRuntime.queryInterface(XParagraphCursor.class, textCursor);
    text.insertControlCharacter(paragraphCursor, PARAGRAPH_BREAK, false);
    text.insertControlCharacter(paragraphCursor, LINE_BREAK, false);
  }

  /**
   * Sets paragraph direction, writing mode, and spell check language.
   *
   * @param props the property set to configure
   * @param languageKey the configuration key for the language
   * @param enableSpellCheck whether to enable spell checking
   */
  public void setParagraphDirectionSpellCheckLanguage(
      final XPropertySet props,
      final ConfigurationKeys languageKey,
      final boolean enableSpellCheck) {

    Objects.requireNonNull(props, "paragraphCursorPropertySet must not be null");

    final SourceLanguage srcLang = SourceLanguage.fromId(configManager.getConfig(languageKey));
    final boolean isLeftToRight = srcLang.wm() == WritingMode2.LR_TB;

    try {
      // Writing mode
      props.setPropertyValue(WRITING_MODE, isLeftToRight ? WritingMode2.LR_TB : WritingMode2.RL_TB);

      // Build appropriate locale
      Locale localeToUse =
          enableSpellCheck
              ? srcLang.locale()
              : makeUnoLocale("zxx", ""); // "zxx" → no linguistic content (no spellcheck)

      // Apply locale to correct property family
      if (isLeftToRight) {
        props.setPropertyValue(CHAR_LOCALE, localeToUse);
      } else {
        props.setPropertyValue(CHAR_LOCALE_COMPLEX, localeToUse);
      }

    } catch (UnknownPropertyException e) {
      LOGGER.error("Unknown UNO property while setting paragraph direction/language", e);
      throw new RuntimeException("Invalid UNO property", e);
    } catch (PropertyVetoException e) {
      LOGGER.error("Property veto while setting paragraph direction/language", e);
      throw new RuntimeException("UNO property veto", e);
    } catch (WrappedTargetException e) {
      LOGGER.error("UNO target exception while setting paragraph direction/language", e);
      throw new RuntimeException("UNO wrapped target exception", e);
    }
  }

  /**
   * Writes Surah text as a single continuous block.
   *
   * @param surahNo the Surah number
   * @param paragraphCursorPropertySet property set for paragraph formatting
   * @param textViewCursor the text view cursor
   * @throws Exception if an error occurs during text insertion
   */
  private void writeSurahTextAsOneBlock(
      int surahNo, XPropertySet paragraphCursorPropertySet, XTextViewCursor textViewCursor)
      throws Exception {
    LOGGER.debug("writeSurahTextAsOneBlock({})", surahNo);

    int from = parseInt(configManager.getConfig(AYAT_FROM_NUMERIC_FIELD_VALUE));
    int to = parseInt(configManager.getConfig(AYAT_TO_NUMERIC_FIELD_VALUE));

    final boolean addBismillah =
        parseBoolean(configManager.getConfig(ALL_AYAT_CHECK_BOX_STATE))
            && !((surahNo == 1) || (surahNo == 9));

    if (parseBoolean(configManager.getConfig(ARABIC_VERSION_CHECK_BOX_STATE))) {
      setParagraphDirectionSpellCheckLanguage(
          paragraphCursorPropertySet, ARABIC_LANGUAGE_SELECTED, true);

      if (addBismillah) {
        writeParagraph(
            textViewCursor,
            getBismillah(ARABIC_LANGUAGE_SELECTED, ARABIC_SOURCE_SELECTED),
            ParagraphAdjust.RIGHT);
        writeEndOfParagraph(textViewCursor);
      }

      writeParagraph(
          textViewCursor,
          getSurahTextBlock(surahNo, from, to, ARABIC_LANGUAGE_SELECTED, ARABIC_SOURCE_SELECTED),
          ParagraphAdjust.RIGHT);
      writeEndOfParagraph(textViewCursor);

      writeParagraph(
          textViewCursor,
          getSurahFooterText(
              paragraphCursorPropertySet,
              surahNo,
              from,
              to,
              ARABIC_LANGUAGE_SELECTED,
              ARABIC_SOURCE_SELECTED),
          ParagraphAdjust.RIGHT);
      writeEndOfParagraph(textViewCursor);
    }

    if (parseBoolean(configManager.getConfig(TRANSLITERATION_VERSION_CHECK_BOX_STATE))) {
      setParagraphDirectionSpellCheckLanguage(
          paragraphCursorPropertySet, TRANSLITERATION_LANGUAGE_SELECTED, false);

      if (addBismillah) {
        writeParagraph(
            textViewCursor,
            getBismillah(TRANSLITERATION_LANGUAGE_SELECTED, TRANSLITERATION_SOURCE_SELECTED),
            ParagraphAdjust.LEFT);
        writeEndOfParagraph(textViewCursor);
      }

      writeParagraph(
          textViewCursor,
          getSurahTextBlock(
              surahNo,
              from,
              to,
              TRANSLITERATION_LANGUAGE_SELECTED,
              TRANSLITERATION_SOURCE_SELECTED),
          ParagraphAdjust.BLOCK);
      writeEndOfParagraph(textViewCursor);
      writeParagraph(
          textViewCursor,
          getSurahFooterText(
              paragraphCursorPropertySet,
              surahNo,
              from,
              to,
              TRANSLITERATION_LANGUAGE_SELECTED,
              TRANSLITERATION_SOURCE_SELECTED),
          ParagraphAdjust.RIGHT);
      writeEndOfParagraph(textViewCursor);
    }

    if (parseBoolean(configManager.getConfig(TRANSLATION_VERSION_CHECK_BOX_STATE))) {
      setParagraphDirectionSpellCheckLanguage(
          paragraphCursorPropertySet, TRANSLATION_LANGUAGE_SELECTED, true);

      if (addBismillah) {
        writeParagraph(
            textViewCursor,
            getBismillah(TRANSLATION_LANGUAGE_SELECTED, TRANSLATION_SOURCE_SELECTED),
            ParagraphAdjust.LEFT);
        writeEndOfParagraph(textViewCursor);
      }

      writeParagraph(
          textViewCursor,
          getSurahTextBlock(
              surahNo, from, to, TRANSLATION_LANGUAGE_SELECTED, TRANSLATION_SOURCE_SELECTED),
          ParagraphAdjust.BLOCK);
      writeEndOfParagraph(textViewCursor);

      writeParagraph(
          textViewCursor,
          getSurahFooterText(
              paragraphCursorPropertySet,
              surahNo,
              from,
              to,
              TRANSLATION_LANGUAGE_SELECTED,
              TRANSLATION_SOURCE_SELECTED),
          ParagraphAdjust.RIGHT);
      writeEndOfParagraph(textViewCursor);
    }
  }

  private String getSurahFooterText(
      XPropertySet props,
      int surahNo,
      int from,
      int to,
      ConfigurationKeys sourceLanguage,
      ConfigurationKeys source) {
    try (QuranReader reader = new QuranReader(getFilePath(configManager.getConfig(source), ctx))) {
      SourceLanguage language = SourceLanguage.fromId(configManager.getConfig(sourceLanguage));
      short writingMode = language.wm();
      String fontName =
          (writingMode == WritingMode2.LR_TB)
              ? configManager.getConfig(LATIN_FONT_SELECTED)
              : configManager.getConfig(ARABIC_FONT_SELECTED);

      final StringBuilder paragraph = new StringBuilder();

      if (writingMode == WritingMode2.LR_TB) {
        paragraph.append("(");
        paragraph.append(reader.getAyahNameOfSurahNo(surahNo));
        paragraph.append(" [");
        paragraph.append(numToNumberString(surahNo, language, fontName));
        paragraph.append(":");
        if (to > from) {
          paragraph.append(numToNumberString(from, language, fontName));
          paragraph.append("-");
        }
        paragraph.append(numToNumberString(to, language, fontName));
        paragraph.append("])");
      } else {
        paragraph.append("(");
        paragraph.append(reader.getAyahNameOfSurahNo(surahNo));
        paragraph.append(" [");
        paragraph.append(numToNumberString(surahNo, language, fontName));
        paragraph.append(":");
        if (to > from) {
          paragraph.append(numToNumberString(from, language, fontName));
          paragraph.append("-");
        }
        paragraph.append(numToNumberString(to, language, fontName));
        paragraph.append("])");

        paragraph.append(")");
      }

      return paragraph.toString();
    }
  }

  /**
   * Retrieves formatted text for Bismillah.
   *
   * @param sourceLanguage the source language configuration key
   * @param source the source file configuration key
   * @return formatted block of text
   * @throws Exception if an error occurs reading the Quran text
   */
  private String getBismillah(ConfigurationKeys sourceLanguage, ConfigurationKeys source)
      throws Exception {
    try (QuranReader reader = new QuranReader(getFilePath(configManager.getConfig(source), ctx))) {

      SourceLanguage language = SourceLanguage.fromId(configManager.getConfig(sourceLanguage));
      short writingMode = language.wm();
      String fontName =
          (writingMode == WritingMode2.LR_TB)
              ? configManager.getConfig(LATIN_FONT_SELECTED)
              : configManager.getConfig(ARABIC_FONT_SELECTED);

      return transFonter(reader.getBismillah(), fontName);
    }
  }

  /**
   * Retrieves formatted text for a range of Ayat as a single block.
   *
   * @param surahNo the Surah number
   * @param from the starting Ayat number
   * @param to the ending Ayat number
   * @param sourceLanguage the source language configuration key
   * @param source the source file configuration key
   * @return formatted block of Ayat text
   * @throws Exception if an error occurs reading the Quran text
   */
  private String getSurahTextBlock(
      int surahNo, int from, int to, ConfigurationKeys sourceLanguage, ConfigurationKeys source)
      throws Exception {

    try (QuranReader reader = new QuranReader(getFilePath(configManager.getConfig(source), ctx))) {
      List<String> ayat = reader.getAyatFromToOfSuraNo(surahNo, from, to);

      SourceLanguage language = SourceLanguage.fromId(configManager.getConfig(sourceLanguage));
      short writingMode = language.wm();
      String fontName =
          (writingMode == WritingMode2.LR_TB)
              ? configManager.getConfig(LATIN_FONT_SELECTED)
              : configManager.getConfig(ARABIC_FONT_SELECTED);
      FontAttr fa = getFontAttrByName(fontName).orElseThrow();

      final StringBuilder paragraph = new StringBuilder();
      int ayatNo = from;
      for (Iterator<String> it = ayat.iterator(); it.hasNext(); ayatNo++) {
        if (writingMode == WritingMode2.LR_TB) {
          paragraph.append(fa.leftParenthesisStr());
          paragraph.append(numToNumberString(ayatNo, language, fontName));
          paragraph.append(fa.rightParenthesisStr());
          paragraph.append(' ');
          paragraph.append(transFonter(it.next(), fontName));
          paragraph.append(' ');
        } else {
          paragraph.append(transFonter(it.next(), fontName));
          paragraph.append(fa.rightParenthesisStr());
          paragraph.append(numToNumberString(ayatNo, language, fontName));
          paragraph.append(fa.leftParenthesisStr());
          paragraph.append(' ');
        }
      }
      return paragraph.toString();
    }
  }

  /** Initializes the "Ayat Per Line" checkbox with its saved state. */
  private void initAyatPerLineCheckBox() {
    LOGGER.debug("initAyatPerLineCheckBox()");

    XCheckBox checkBox = getUnoControl(this.dialog, XCheckBox.class, AYAT_PER_LINE_CHECK_BOX);

    checkBox.setState(
        boolean2Short(parseBoolean(configManager.getConfig(AYAT_PER_LINE_CHECK_BOX_STATE))));
  }
}
