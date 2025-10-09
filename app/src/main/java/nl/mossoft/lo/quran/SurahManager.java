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

package nl.mossoft.lo.quran;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class providing information about Quran chapters (Surahs). Contains Surah names, sizes
 * (number of verses), and lookup methods. All data is immutable and loaded statically.
 */
public final class SurahManager {

  /** Immutable map storing Surah names with formatted numbering (e.g., "001 Al-Fâtihah"). */
  private static final Map<Integer, String> SURAH_NAMES;

  /** Immutable map storing the number of verses in each Surah. */
  private static final Map<Integer, Integer> SURAH_SIZES;

  // Static block initializes the Surah data
  static {
    Object[][] surahData = {
      {1, "Al-Fâtihah", 7},
      {2, "Al-Baqarah", 286},
      {3, "Âli-Imrân", 200},
      {4, "An-Nisâ", 176},
      {5, "Al-Mâidah", 120},
      {6, "Al-Anâm", 165},
      {7, "Al-Arâf", 206},
      {8, "Al-Anfâl", 75},
      {9, "At-Tawbah", 129},
      {10, "Yûnus", 109},
      {11, "Hûd", 123},
      {12, "Yûsuf", 111},
      {13, "Ar-Rad", 43},
      {14, "Ibrâhîm", 52},
      {15, "Al-Ḥijr", 99},
      {16, "An-Naḥl", 128},
      {17, "Al-Isrâ", 111},
      {18, "Al-Kahf", 110},
      {19, "Maryam", 98},
      {20, "Tâ-Hâ", 135},
      {21, "Al-Anbiyâ", 112},
      {22, "Al-Ḥajj", 78},
      {23, "Al-Muminûm", 118},
      {24, "An-Nûr", 64},
      {25, "Al-Furqân", 77},
      {26, "Ash-Shuarâ", 227},
      {27, "Al-Naml", 93},
      {28, "Al-Qaṣaṣ", 88},
      {29, "Al-Ankabût", 69},
      {30, "Ar-Rûm", 60},
      {31, "Luqmân", 34},
      {32, "As-Sajdah", 30},
      {33, "Al-Aḥzâb", 73},
      {34, "Saba", 54},
      {35, "Fâṭir", 45},
      {36, "Yâ-Sîn", 83},
      {37, "Aṣ-Ṣâffât", 182},
      {38, "Ṣâd", 88},
      {39, "Az-Zumar", 75},
      {40, "Ghâfir", 85},
      {41, "Fuṣṣilat", 54},
      {42, "Ash-Shûra", 53},
      {43, "Az-Zukhruf", 89},
      {44, "Ad-Dukhân", 59},
      {45, "Al-Jâthiyah", 37},
      {46, "Al-Aḥqâf", 35},
      {47, "Muḥammad", 38},
      {48, "Al-Fatḥ", 29},
      {49, "Al-Ḥujurât", 18},
      {50, "Qâf", 45},
      {51, "Adh-Dhâriyât", 60},
      {52, "Aṭ-Ṭûr", 49},
      {53, "An-Najm", 62},
      {54, "Al-Qamar", 55},
      {55, "Ar-Raḥmân", 78},
      {56, "Al-Wâqiah", 96},
      {57, "Al-Ḥadîd", 29},
      {58, "Al-Mudjâdilah", 22},
      {59, "Al-Ḥashr", 24},
      {60, "Al-Mumtaḥanah", 13},
      {61, "Aṣ-Ṣaf", 14},
      {62, "Al-Jumuah", 11},
      {63, "Al-Munȃfiqȗn", 11},
      {64, "At-Taghȃbun", 18},
      {65, "At-Talȃq", 12},
      {66, "At-Taḥrîm", 12},
      {67, "Al-Mulk", 30},
      {68, "Al-Qalam", 52},
      {69, "Al-Ḥaqqah", 52},
      {70, "Al-Maȃrij", 44},
      {71, "Al-Nȗḥ", 28},
      {72, "Al-Jinn", 28},
      {73, "Al-Muzzammil", 20},
      {74, "Al-Muddaththir", 56},
      {75, "Al-Qiyȃmah", 40},
      {76, "Al-Insȃn", 31},
      {77, "Al-Mursalȃt", 50},
      {78, "Al-Naba", 40},
      {79, "Al-Nȃziat", 46},
      {80, "Abasa", 42},
      {81, "At-Takwîr", 29},
      {82, "Al-Infiṭȃr", 19},
      {83, "Al-Muṭaffifîn", 36},
      {84, "Al-Inshiqȃq", 25},
      {85, "Al-Burȗj", 22},
      {86, "At-Ṭȃriq", 17},
      {87, "Al-Alȃ", 19},
      {88, "Al-Ghȃshiyah", 26},
      {89, "Al-Fajr", 30},
      {90, "Al-Balad", 20},
      {91, "Ash-Shams", 15},
      {92, "Al-Layl", 21},
      {93, "Aḍ-Ḍuḥȃ", 11},
      {94, "Ash-Sharḥ", 8},
      {95, "At-Tîn", 8},
      {96, "Al-Alaq", 19},
      {97, "Al-Qadr", 5},
      {98, "Al-Bayyinah", 8},
      {99, "Az-Zalzalah", 8},
      {100, "Al-Âdiyȃt", 11},
      {101, "Al-Qȃriah", 11},
      {102, "At-Takȃthur", 8},
      {103, "Al-Aṣr", 3},
      {104, "Al-Humazah", 9},
      {105, "Al-Fîl", 5},
      {106, "Quraysh", 4},
      {107, "Al-Mȃȗn", 7},
      {108, "Al-Kawthar", 3},
      {109, "Al-Kȃfirȗn", 6},
      {110, "An-Naṣr", 3},
      {111, "Al-Masad", 5},
      {112, "Al-Ikhlȃṣ", 4},
      {113, "Al-Falaq", 5},
      {114, "An-Nȃs", 6}
    };

    SURAH_NAMES =
        Collections.unmodifiableMap(
            Arrays.stream(surahData)
                .collect(
                    Collectors.toMap(
                        row -> (Integer) row[0],
                        row -> String.format("%03d %s", row[0], row[1]),
                        (a, b) -> a,
                        LinkedHashMap::new)));

    SURAH_SIZES =
        Collections.unmodifiableMap(
            Arrays.stream(surahData)
                .collect(
                    Collectors.toMap(
                        row -> (Integer) row[0],
                        row -> (Integer) row[2],
                        (a, b) -> a,
                        LinkedHashMap::new)));
  }

  /** Private constructor to prevent instantiation. */
  private SurahManager() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  /**
   * Retrieves the number of verses in a specific Surah.
   *
   * @param order the Surah number (1-114)
   * @return the number of verses, or -1 if Surah not found
   */
  public static int getSurahSize(int order) {
    return SURAH_SIZES.getOrDefault(order, -1);
  }

  /**
   * Retrieves a formatted Surah name including its number.
   *
   * @param order the Surah number (1-114)
   * @return formatted name (e.g., "001 Al-Fâtihah"), or {@code null} if not found
   */
  public static String getSurahName(int order) {
    return SURAH_NAMES.get(order);
  }

  /**
   * Retrieves all Surah names as a comma-separated string.
   *
   * @return A single string containing all Surah names.
   */
  public static String getAllSurahsAsString() {
    return String.join(", ", SURAH_NAMES.values());
  }

  /**
   * Finds the Surah number for a given Surah name. Supports both formatted names ("001 Al-Fâtihah")
   * and plain names ("Al-Fâtihah").
   *
   * @param surahName the Surah name to look up
   * @return Surah number (1-114), or -1 if not found
   */
  public static int getSurahNumber(String surahName) {
    if (surahName == null || surahName.isBlank()) {
      return -1;
    }

    // Normalize input for flexible matching
    String normalizedInput = surahName.trim();

    // Try to match full formatted name (e.g. "001 Al-Fâtihah")
    Optional<Integer> match =
        SURAH_NAMES.entrySet().stream()
            .filter(e -> e.getValue().equalsIgnoreCase(normalizedInput))
            .map(Map.Entry::getKey)
            .findFirst();

    if (match.isPresent()) {
      return match.get();
    }

    // Try to match by plain name (e.g. "Al-Fâtihah")
    match =
        SURAH_NAMES.entrySet().stream()
            .filter(e -> e.getValue().substring(4).equalsIgnoreCase(normalizedInput))
            .map(Map.Entry::getKey)
            .findFirst();

    return match.orElse(-1);
  }
}
