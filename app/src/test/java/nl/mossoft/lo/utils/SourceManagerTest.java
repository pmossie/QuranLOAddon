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

package nl.mossoft.lo.utils;

import static nl.mossoft.lo.quran.SourceLanguage.*;
import static nl.mossoft.lo.quran.SourceManager.getSourcesOfTypeAsArray;
import static nl.mossoft.lo.quran.SourceType.TRANSLATION;
import static nl.mossoft.lo.quran.SurahManager.getSurahSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.mockStatic;

import com.sun.star.uno.XComponentContext;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;
import nl.mossoft.lo.quran.*;
import nl.mossoft.lo.util.FileHelper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

public class SourceManagerTest {

  @Test
  void getSourcesOfTypeAsArray_translation_sortedAndContentOk() {
    SourceInfo[] arr = getSourcesOfTypeAsArray(TRANSLATION);

    // Ensure it is sorted by language, then version
    // Expected order (lexicographic):
    // 0: Dutch (Leemhuis)
    // 1: Dutch (Siregar)
    // 2: English (Pickthall)
    // 3: English (Sahih International)
    // 4: Indonesian (Ministry of Religious Affairs)
    assertThat(arr).hasSize(5);

    assertThat(arr[0].language()).isEqualTo(DUTCH);
    assertThat(arr[0].version()).isEqualTo("Leemhuis");

    assertThat(arr[1].language()).isEqualTo(DUTCH);
    assertThat(arr[1].version()).isEqualTo("Siregar");

    assertThat(arr[2].language()).isEqualTo(ENGLISH);
    assertThat(arr[2].version()).isEqualTo("Pickthall");

    assertThat(arr[3].language()).isEqualTo(ENGLISH);
    assertThat(arr[3].version()).isEqualTo("Sahih International");

    assertThat(arr[4].language()).isEqualTo(INDONESIAN);
    assertThat(arr[4].version()).isEqualTo("Ministry of Religious Affairs");
  }

  @Test
  void getSourcesOfTypeAsArray_transliteration_singleEntry() {
    SourceInfo[] arr = getSourcesOfTypeAsArray(SourceType.TRANSLITERATION);
    assertThat(arr).hasSize(1);
    assertThat(arr[0].language()).isEqualTo(ENGLISH);
    assertThat(arr[0].version()).isEqualTo("Tanzil.net");
    assertThat(arr[0].fileName()).isEqualTo("data/quran/QuranText.English.Transliteration.xml");
  }

  @Test
  void getVersionsOfTypeAsString_translation_formattedAndSorted() {
    String s = SourceManager.getVersionsOfTypeAsString(TRANSLATION);
    // Sorted lexicographically as implemented
    assertThat(s)
        .isEqualTo(
            String.join(
                ", ",
                Arrays.asList(
                    "Dutch (Leemhuis)",
                    "Dutch (Siregar)",
                    "English (Pickthall)",
                    "English (Sahih International)",
                    "Indonesian (Ministry of Religious Affairs)")));
  }

  @Test
  void getSourceUri_delegatesToFileHelperWithSortedIndex() {
    try (MockedStatic<FileHelper> mocked = mockStatic(FileHelper.class)) {
      XComponentContext ctx = org.mockito.Mockito.mock(XComponentContext.class);

      File expected = new File("QuranText.Dutch.Leemhuis.xml"); // relative, not absolute
      mocked
          .when(() -> FileHelper.getFilePath("data/quran/QuranText.Dutch.Leemhuis.xml", ctx))
          .thenReturn(expected);

      File result = SourceManager.getSourceUri(TRANSLATION, 0, ctx);

      assertThat(result).isSameAs(expected);
      mocked.verify(() -> FileHelper.getFilePath("data/quran/QuranText.Dutch.Leemhuis.xml", ctx));
    }
  }

  @Test
  void getSourceFilename_translation_pickthall() {
    String path = SourceManager.getSourceFilename(TRANSLATION, ENGLISH, "Pickthall");
    assertThat(path).isEqualTo("data/quran/QuranText.English.Pickthall.xml");
  }

  @Test
  void getSourceFilename_translation_dutch_variants() {
    assertThat(SourceManager.getSourceFilename(TRANSLATION, DUTCH, "Leemhuis"))
        .isEqualTo("data/quran/QuranText.Dutch.Leemhuis.xml");

    assertThat(SourceManager.getSourceFilename(TRANSLATION, DUTCH, "Siregar"))
        .isEqualTo("data/quran/QuranText.Dutch.Siregar.xml");
  }

  @Test
  void getSourceFilename_arabic_uthmani() {
    String path = SourceManager.getSourceFilename(SourceType.ARABIC, ARABIC, "Uthmani");
    assertThat(path).isEqualTo("data/quran/QuranText.Arabic.Uthmani.xml");
  }

  @Test
  void getSourceFilename_transliteration_tanzil() {
    String path =
        SourceManager.getSourceFilename(SourceType.TRANSLITERATION, ENGLISH, "Tanzil.net");
    assertThat(path).isEqualTo("data/quran/QuranText.English.Transliteration.xml");
  }

  @Test
  void getSourceFilename_noMatch_returnsNull() {
    String path = SourceManager.getSourceFilename(TRANSLATION, ENGLISH, "Nonexistent");
    assertThat(path).isNull();
  }

  //
  //  @Test
  //  void checkIntegrityQuranSourceFiles(){  {
  //
  //     SourceType.stream()
  //         .forEach(this::checkIntegrityQuranSourceFile);
  //    }
  //  }
  //
  //  void checkIntegrityQuranSourceFile(SourceType type) {
  //    for (SourceInfo si : getSourcesOfTypeAsArray(type)) {
  //      QuranReader rdr = new QuranReader(new File("src/main/oxt/" + si.fileName()));
  //
  //      LOGGER.debug("Checking file: '{}'", si.fileName());
  //      assertThat(rdr.getTotalSurahCount()).isEqualTo(114);
  //      assertThat(rdr.getTotalAyahCount()).isEqualTo(6236);
  //
  //      for (int i = 1; i <= 114; i++) {
  //        assertThat(rdr.getTotalAyahCountInSurah(i)).isEqualTo(getSurahSize(i));
  //      }
  //    }
  //  }

  private static final Path BASE = Paths.get("src", "main", "oxt");

  @ParameterizedTest(name = "{index} => {0} {1} ({2})")
  @MethodSource("allSources")
  @DisplayName("Quran source integrity per file")
  void checkIntegrityQuranSourceFile(
      SourceType type, SourceLanguage language, String version, Path path) {
    // Optional: guard against bad paths early
    assertThat(path).as("source path").exists().isRegularFile();

    // Optional: put a global timeout around the expensive part
    assertTimeoutPreemptively(
        Duration.ofSeconds(10),
        () -> {
          try (QuranReader rdr = new QuranReader(path.toFile())) {
            // Global sanity checks
            assertThat(rdr.getTotalSurahCount()).isEqualTo(114);
            assertThat(rdr.getTotalAyahCount()).isEqualTo(6236);

            // Check all surah sizes, but collect all failures first (better report)
            SoftAssertions.assertSoftly(
                soft -> {
                  for (int surah = 1; surah <= 114; surah++) {
                    soft.assertThat(rdr.getTotalAyahCountInSurah(surah))
                        .as("Ayah count of surah %s in %s %s (%s)", surah, language, version, type)
                        .isEqualTo(getSurahSize(surah));
                  }
                });
          }
        });
  }

  /** Supplies (type, language, version, path) for every known source file. */
  static Stream<Arguments> allSources() {
    return Arrays.stream(SourceType.values())
        .flatMap(
            type ->
                Arrays.stream(SourceManager.getSourcesOfTypeAsArray(type))
                    .map(
                        si ->
                            org.junit.jupiter.params.provider.Arguments.of(
                                type, si.language(), si.version(), BASE.resolve(si.fileName()))));
  }
}
