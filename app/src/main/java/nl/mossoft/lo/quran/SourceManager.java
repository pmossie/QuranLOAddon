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

package nl.mossoft.lo.quran;

import static nl.mossoft.lo.quran.SourceLanguage.*;
import static nl.mossoft.lo.util.FileHelper.getFilePath;

import com.sun.star.uno.XComponentContext;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton utility class that manages different Quran sources. Provides access to available Quran
 * versions by type, language, and version.
 *
 * @see SourceInfo
 * @see SourceType
 * @see SourceLanguage
 */
public final class SourceManager {

  /** A map storing sources by language, ensuring efficient lookup. */
  private static final Map<SourceType, List<SourceInfo>> SOURCES_BY_TYPE = new HashMap<>();

  // Static block initializes the data once
  static {
    addSource(SourceType.ORIGINAL, ARABIC, "Uthmani", "data/quran/QuranText.Arabic.Uthmani.xml");
    addSource(SourceType.TRANSLATION, DUTCH, "Leemhuis", "data/quran/QuranText.Dutch.Leemhuis.xml");
    addSource(SourceType.TRANSLATION, DUTCH, "Siregar", "data/quran/QuranText.Dutch.Siregar.xml");
    addSource(
        SourceType.TRANSLATION, ENGLISH, "Pickthall", "data/quran/QuranText.English.Pickthall.xml");
    addSource(
        SourceType.TRANSLATION,
        ENGLISH,
        "Sahih International",
        "data/quran/QuranText.English.Sahih_International.xml");
    addSource(
        SourceType.TRANSLATION,
        INDONESIAN,
        "Ministry of Religious Affairs",
        "data/quran/QuranText.Indonesian.Ministry_of_Religious_Affairs.xml");
    addSource(
        SourceType.TRANSLITERATION,
        ENGLISH,
        "International",
        "data/quran/QuranText.English.Transliteration.xml");

    // Convert to immutable maps for safety
    makeImmutable();
  }

  /** Private constructor to prevent instantiation. */
  private SourceManager() {}

  /**
   * Adds a source to the map.
   *
   * @param language The language of the source.
   * @param version The version of the Quran translation.
   * @param filename The file path to the Quran source.
   */
  private static void addSource(
      SourceType type, SourceLanguage language, String version, String filename) {
    SOURCES_BY_TYPE
        .computeIfAbsent(type, k -> new ArrayList<>())
        .add(new SourceInfo(type, language, version, filename));
  }

  /** Makes the source map immutable after initialization. */
  private static void makeImmutable() {
    SOURCES_BY_TYPE.replaceAll((key, value) -> Collections.unmodifiableList(value));
  }

  /**
   * Retrieves the filename for a specific Quran source.
   *
   * @param type the source type (original, translation, transliteration)
   * @param language the language of the Quran version
   * @param version the specific version or translator name
   * @return the file path to the Quran source, or {@code null} if not found
   */
  public static String getSourceFilename(SourceType type, SourceLanguage language, String version) {
    return SOURCES_BY_TYPE.getOrDefault(type, Collections.emptyList()).stream()
        .filter(s -> s.language().equals(language))
        .filter(s -> s.version().equalsIgnoreCase(version))
        .map(SourceInfo::fileName)
        .findFirst()
        .orElse(null);
  }

  /**
   * Retrieves all versions of a specific source type as a formatted string.
   *
   * @param type the source type to filter by
   * @return comma-separated string of available versions in format "Language (Version)"
   */
  public static String getVersionsOfTypeAsString(SourceType type) {
    return SOURCES_BY_TYPE.entrySet().stream()
        .filter(entry -> type.equals(entry.getKey()))
        .flatMap(
            entry ->
                entry.getValue().stream()
                    .map(source -> source.language().id() + " (" + source.version() + ")"))
        .sorted()
        .collect(Collectors.joining(", "));
  }

  /**
   * Retrieves all SourceInfo objects for a particular SourceType as an array, sorted by language.
   *
   * @param type The SourceType to filter by.
   * @return An array of SourceInfo objects for the given SourceType, sorted by language.
   */
  public static SourceInfo[] getSourceInfoOfTypeAsArray(SourceType type) {
    List<SourceInfo> sources =
        new ArrayList<>(SOURCES_BY_TYPE.getOrDefault(type, Collections.emptyList()));
    sources.sort(
        Comparator.comparing(SourceInfo::language)
            .thenComparing(SourceInfo::version)); // Sort by language
    return sources.toArray(new SourceInfo[0]);
  }

  /**
   * Retrieves the file URI for a specific source type and version index.
   *
   * @param type the source type
   * @param version the index of the version in the available sources array
   * @param ctx the component context for file path resolution
   * @return the File object pointing to the Quran source
   * @throws ArrayIndexOutOfBoundsException if version index is invalid
   */
  public static File getSourceUri(SourceType type, int version, XComponentContext ctx) {
    return getFilePath(getSourceInfoOfTypeAsArray(type)[version].fileName(), ctx);
  }
}
