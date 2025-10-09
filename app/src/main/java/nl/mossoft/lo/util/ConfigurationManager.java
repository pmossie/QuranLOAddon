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

import static nl.mossoft.lo.dialog.DialogHelper.getLocalizedSize;
import static nl.mossoft.lo.dialog.UnoControlProperties.*;
import static nl.mossoft.lo.quran.SourceManager.getSourceFilename;
import static nl.mossoft.lo.quran.SourceType.*;
import static nl.mossoft.lo.util.ConfigurationKeys.*;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.style.XStyle;
import com.sun.star.style.XStyleFamiliesSupplier;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import nl.mossoft.lo.quran.SourceLanguage;
import nl.mossoft.lo.quran.SourceManager;
import nl.mossoft.lo.quran.SurahManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton manager for application configuration settings.
 *
 * <p>Manages a centralized configuration store for all Quran add-on settings including:
 *
 * <ul>
 *   <li>Font selections and sizes
 *   <li>Language and version preferences
 *   <li>Surah and verse range selections
 *   <li>UI control states
 * </ul>
 *
 * <p>Automatically initializes with sensible defaults and provides thread-safe access to
 * configuration values.
 *
 * @see ConfigurationKeys
 */
public enum ConfigurationManager {
  INSTANCE; // ✅ the single instance

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);

  private static final String FALSE = "false";
  private static final String TRUE = "true";
  private static final String THREE = "3";
  private static final String ZERO = "0";
  private static final String HIGHEST_AYAT_NUMBER_AL_FATIHAH = "7";
  private static final String LOWEST_AYAT_NUMBER = "1";
  private static final String DEFAULT_ARABIC_VERSION = "Uthmani";
  private static final String DEFAULT_SURAH = "Al-Fâtihah";
  private static final String DEFAULT_TRANSLATION_VERSION = "Sahih International";
  private static final String DEFAULT_TRANSLITERATION_VERSION = "International";

  private static String DEFAULT_ARABIC_FONT;
  private static String DEFAULT_ARABIC_FONT_SIZE;
  private static String DEFAULT_LATIN_FONT;
  private static String DEFAULT_LATIN_FONT_SIZE;

  private final ConcurrentHashMap<ConfigurationKeys, String> configMap = new ConcurrentHashMap<>();

  // === API remains the same ===

  /**
   * Initializes the configuration manager with default values. This method must be called before
   * using any configuration operations.
   *
   * <p>Performs the following initialization steps:
   *
   * <ol>
   *   <li>Detects system default fonts via a hidden document
   *   <li>Loads all default configuration values
   *   <li>Populates the configuration map
   * </ol>
   *
   * @param ctx the component context used for font detection and service creation
   * @throws RuntimeException if font detection fails
   */
  public void initializeDefaults(XComponentContext ctx) {
    getAppDefaultFontsViaHiddenDoc(ctx);
    configMap.putAll(getDefaultConfigurations());
    LOGGER.debug("{} items loaded", configMap.size());
  }

  private Map<ConfigurationKeys, String> getDefaultConfigurations() {
    Map<ConfigurationKeys, String> defaults = new ConcurrentHashMap<>();

    defaults.put(ALL_AYAT_CHECK_BOX_STATE, TRUE);
    defaults.put(ARABIC_FONT_LIST_BOX_ITEM_LIST, FontManager.getArabicSupportedFontsAsString());
    defaults.put(
        ARABIC_FONT_LIST_BOX_ITEM_SELECTED,
        Integer.toString(FontManager.getFontIndexInArabicList(DEFAULT_ARABIC_FONT)));
    defaults.put(ARABIC_FONT_SELECTED, DEFAULT_ARABIC_FONT);
    defaults.put(ARABIC_FONT_SIZE_COMBO_BOX_VALUE, DEFAULT_ARABIC_FONT_SIZE);
    defaults.put(ARABIC_LANGUAGE_SELECTED, SourceLanguage.ARABIC.id());
    defaults.put(ARABIC_VERSION_CHECK_BOX_STATE, TRUE);
    defaults.put(
        ARABIC_VERSION_LIST_BOX_ITEM_LIST, SourceManager.getVersionsOfTypeAsString(ORIGINAL));
    defaults.put(ARABIC_VERSION_LIST_BOX_ITEM_SELECTED, ZERO);
    defaults.put(
        ARABIC_SOURCE_SELECTED,
        getSourceFilename(ORIGINAL, SourceLanguage.ARABIC, DEFAULT_ARABIC_VERSION));
    defaults.put(AYAT_FROM_NUMERIC_FIELD_MAX, HIGHEST_AYAT_NUMBER_AL_FATIHAH);
    defaults.put(AYAT_FROM_NUMERIC_FIELD_MIN, LOWEST_AYAT_NUMBER);
    defaults.put(AYAT_FROM_NUMERIC_FIELD_VALUE, LOWEST_AYAT_NUMBER);
    defaults.put(AYAT_PER_LINE_CHECK_BOX_STATE, FALSE);
    defaults.put(AYAT_TO_NUMERIC_FIELD_MAX, HIGHEST_AYAT_NUMBER_AL_FATIHAH);
    defaults.put(AYAT_TO_NUMERIC_FIELD_MIN, LOWEST_AYAT_NUMBER);
    defaults.put(AYAT_TO_NUMERIC_FIELD_VALUE, HIGHEST_AYAT_NUMBER_AL_FATIHAH);
    defaults.put(LATIN_FONT_LIST_BOX_ITEM_LIST, FontManager.getLatinSupportedFontsAsString());
    defaults.put(
        LATIN_FONT_LIST_BOX_ITEM_SELECTED,
        Integer.toString(FontManager.getFontIndexInLatinList(DEFAULT_LATIN_FONT)));
    defaults.put(LATIN_FONT_SELECTED, DEFAULT_LATIN_FONT);
    defaults.put(LATIN_FONT_SIZE_COMBO_BOX_VALUE, DEFAULT_LATIN_FONT_SIZE);
    defaults.put(SURAH_LIST_BOX_ITEM_LIST, SurahManager.getAllSurahsAsString());
    defaults.put(SURAH_LIST_BOX_ITEM_SELECTED, ZERO);
    defaults.put(SURAH_SELECTED, DEFAULT_SURAH);
    defaults.put(TRANSLATION_LANGUAGE_SELECTED, SourceLanguage.ENGLISH.id());
    defaults.put(TRANSLATION_VERSION_CHECK_BOX_STATE, FALSE);
    defaults.put(
        TRANSLATION_VERSION_LIST_BOX_ITEM_LIST,
        SourceManager.getVersionsOfTypeAsString(TRANSLATION));
    defaults.put(TRANSLATION_VERSION_LIST_BOX_ITEM_SELECTED, THREE);
    defaults.put(
        TRANSLATION_SOURCE_SELECTED,
        getSourceFilename(TRANSLATION, SourceLanguage.ENGLISH, DEFAULT_TRANSLATION_VERSION));
    defaults.put(TRANSLITERATION_LANGUAGE_SELECTED, SourceLanguage.ENGLISH.id());
    defaults.put(TRANSLITERATION_VERSION_CHECK_BOX_STATE, FALSE);
    defaults.put(
        TRANSLITERATION_VERSION_LIST_BOX_ITEM_LIST,
        SourceManager.getVersionsOfTypeAsString(TRANSLITERATION));
    defaults.put(TRANSLITERATION_VERSION_LIST_BOX_ITEM_SELECTED, ZERO);
    defaults.put(
        TRANSLITERATION_SOURCE_SELECTED,
        getSourceFilename(
            TRANSLITERATION, SourceLanguage.ENGLISH, DEFAULT_TRANSLITERATION_VERSION));
    return defaults;
  }

  public static void getAppDefaultFontsViaHiddenDoc(XComponentContext ctx) {
    try {
      Object desktop =
          ctx.getServiceManager().createInstanceWithContext("com.sun.star.frame.Desktop", ctx);
      XComponentLoader loader = UnoRuntime.queryInterface(XComponentLoader.class, desktop);

      PropertyValue hidden = new PropertyValue();
      hidden.Name = "Hidden";
      hidden.Value = Boolean.TRUE;

      XComponent comp =
          loader.loadComponentFromURL(
              "private:factory/swriter", "_blank", 0, new PropertyValue[] {hidden});
      try {
        getDocumentDefaultFonts(UnoRuntime.queryInterface(XTextDocument.class, comp), ctx);
      } finally {
        comp.dispose();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Extracts default font information from an existing text document. Reads the default paragraph
   * style to determine Latin and Arabic font settings.
   *
   * @param doc the text document to analyze
   * @param ctx the component context for localization
   * @throws RuntimeException if font properties cannot be accessed
   */
  public static void getDocumentDefaultFonts(XTextDocument doc, XComponentContext ctx) {
    try {
      XStyleFamiliesSupplier famSup = UnoRuntime.queryInterface(XStyleFamiliesSupplier.class, doc);
      XNameAccess families =
          UnoRuntime.queryInterface(XNameAccess.class, famSup.getStyleFamilies());
      XNameAccess paraStyles =
          UnoRuntime.queryInterface(XNameAccess.class, families.getByName("ParagraphStyles"));

      XStyle defaultPara =
          tryGetStyle(paraStyles, "Default Paragraph Style")
              .or(() -> tryGetStyle(paraStyles, "Standard"))
              .orElseGet(() -> findRootParagraphStyle(paraStyles));

      XPropertySet ps = UnoRuntime.queryInterface(XPropertySet.class, defaultPara);

      DEFAULT_LATIN_FONT = (String) ps.getPropertyValue(CHAR_FONT_NAME);
      DEFAULT_LATIN_FONT_SIZE = getLocalizedSize(ctx, (float) ps.getPropertyValue(CHAR_HEIGHT));

      DEFAULT_ARABIC_FONT = (String) ps.getPropertyValue(CHAR_FONT_NAME_COMPLEX);
      DEFAULT_ARABIC_FONT_SIZE =
          getLocalizedSize(ctx, (float) ps.getPropertyValue(CHAR_HEIGHT_COMPLEX));

      LOGGER.debug("Default fonts: latin='{}' {} pt", DEFAULT_LATIN_FONT, DEFAULT_LATIN_FONT_SIZE);
      LOGGER.debug(
          "Default fonts: arabic='{}' {} pt", DEFAULT_ARABIC_FONT, DEFAULT_ARABIC_FONT_SIZE);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Optional<XStyle> tryGetStyle(XNameAccess paraStyles, String name) {
    try {
      Object s = paraStyles.getByName(name);
      return Optional.ofNullable(UnoRuntime.queryInterface(XStyle.class, s));
    } catch (Exception ignore) {
      return Optional.empty();
    }
  }

  private static XStyle findRootParagraphStyle(XNameAccess paraStyles) {
    try {
      for (String n : paraStyles.getElementNames()) {
        XStyle s = UnoRuntime.queryInterface(XStyle.class, paraStyles.getByName(n));
        XPropertySet ps = UnoRuntime.queryInterface(XPropertySet.class, s);
        String parent = (String) ps.getPropertyValue("ParentStyle");
        if (parent == null || parent.isEmpty()) return s;
      }
    } catch (Exception ignored) {
    }
    throw new IllegalStateException("Default paragraph style not found.");
  }

  /**
   * Retrieves the configuration value for the specified key.
   *
   * @param key the configuration key to retrieve
   * @return the configuration value as a string, or {@code null} if key not found
   * @throws NullPointerException if key is {@code null}
   */
  public String getConfig(ConfigurationKeys key) {
    String value = configMap.get(key);
    LOGGER.debug("Get key: '{}' -> '{}'", key, value);
    if (value == null) {
      LOGGER.error("Configuration key '{}' not found in configMap.", key);
    }
    return value;
  }

  /**
   * Updates the configuration value for the specified key.
   *
   * @param key the configuration key to update
   * @param value the new value to store
   * @throws NullPointerException if key is {@code null}
   */
  public void setConfig(ConfigurationKeys key, String value) {
    LOGGER.debug("Update key: '{}' -> '{}'", key, value);
    configMap.put(key, value);
  }

  /**
   * Logs all current configuration keys and values in alphabetical order. Useful for debugging
   * configuration state.
   */
  public void listAllConfigurationKeys() {
    configMap.entrySet().stream()
        .sorted(Comparator.comparing(e -> e.getKey().toString()))
        .forEach(entry -> LOGGER.debug("'{}' -> '{}'", entry.getKey(), entry.getValue()));
  }
}
