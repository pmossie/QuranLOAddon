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

import static nl.mossoft.lo.quran.SurahManager.getSurahNumber;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import nl.mossoft.lo.quran.QuranReader;
import nl.mossoft.lo.quran.QuranReaderAyahNotFoundException;
import org.junit.jupiter.api.Test;

public class QuranReaderTest {

  private static final String BISMILLAH_ARABIC = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ";
  private static final String BISMILLAH_ENGLISH =
      "In the name of Allah, the Entirely Merciful, the Especially Merciful.";
  private static final String KURSI_ARABIC =
      "ٱللَّهُ لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ ۚ لَا تَأْخُذُهُۥ سِنَةٌۭ وَلَا نَوْمٌۭ ۚ لَّهُۥ مَا فِى ٱلسَّمَٰوَٰتِ وَمَا فِى ٱلْأَرْضِ ۗ مَن ذَا ٱلَّذِى يَشْفَعُ عِندَهُۥٓ إِلَّا بِإِذْنِهِۦ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَىْءٍۢ مِّنْ عِلْمِهِۦٓ إِلَّا بِمَا شَآءَ ۚ وَسِعَ كُرْسِيُّهُ ٱلسَّمَٰوَٰتِ وَٱلْأَرْضَ ۖ وَلَا يَـُٔودُهُۥ حِفْظُهُمَا ۚ وَهُوَ ٱلْعَلِىُّ ٱلْعَظِيمُ";
  private static final String KURSI_ENGLISH =
      "Allah - there is no deity except Him, the Ever-Living, the Sustainer of [all] existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is [presently] before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.";
  private static final String QURAN_SOURCE_FILE_ARABIC = "src/test/resources/quran_arabic.test.xml";
  private static final String QURAN_SOURCE_FILE_ARABIC_WITH_ERRORS =
      "src/test/resources/quran_arabic_with_errors.test.xml";
  private static final String QURAN_SOURCE_FILE_ENGLISH =
      "src/test/resources/quran_english.test.xml";
  private static final String QURAN_SOURCE_FILE_ENGLISH_WITH_ERRORS =
      "src/test/resources/quran_english_with_errors.test.xml";

  @Test
  void getBismillah_Arabic_isFoundAndContentIsCorrect() throws Exception {
    String text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC))) {

      text = assertDoesNotThrow(reader::getBismillah);
    }
    assertThat(text).isEqualTo(BISMILLAH_ARABIC);
  }

  @Test
  void getBismillah_Arabic_isNotFound() throws Exception {
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC_WITH_ERRORS))) {

      assertThatExceptionOfType(QuranReaderAyahNotFoundException.class)
          .isThrownBy(reader::getBismillah)
          .withMessageContaining("Ayah not found: surah=1, ayah=1");
    }
  }

  @Test
  void getBismillah_English_isFoundButContentIsWrong() throws Exception {
    String text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ENGLISH_WITH_ERRORS))) {

      text = assertDoesNotThrow(reader::getBismillah);
    }
    assertThat(text).isNotEqualTo(BISMILLAH_ENGLISH);
  }

  @Test
  void getAyahNoOfSurahNo_Arabic_isFoundAndContentIsCorrect() throws Exception {
    String text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC))) {

      text = assertDoesNotThrow(() -> reader.getAyahNoOfSurahNo(getSurahNumber("Al-Baqarah"), 255));
    }
    assertThat(text).isEqualTo(KURSI_ARABIC);
  }

  @Test
  void getAyahNoOfSurahNo_Arabic_isFoundButContentIsWrong() throws Exception {
    String text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC_WITH_ERRORS))) {

      text = assertDoesNotThrow(() -> reader.getAyahNoOfSurahNo(getSurahNumber("Al-Baqarah"), 255));
    }
    assertThat(text).isNotEqualTo(KURSI_ARABIC);
  }

  @Test
  void getAyahNoOfSurahNo_English_isFoundAndContentIsCorrect() throws Exception {
    String text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ENGLISH))) {

      text = assertDoesNotThrow(() -> reader.getAyahNoOfSurahNo(getSurahNumber("Al-Baqarah"), 255));
    }
    assertThat(text).isEqualTo(KURSI_ENGLISH);
  }

  @Test
  void getAyahNoOfSurahNo_English_isFoundButContentIsWrong() throws Exception {
    String text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ENGLISH_WITH_ERRORS))) {

      text = assertDoesNotThrow(() -> reader.getAyahNoOfSurahNo(getSurahNumber("Al-Baqarah"), 255));
    }
    assertThat(text).isNotEqualTo(KURSI_ENGLISH);
  }

  @Test
  void checkNumberOfSurahInSourceFile_isCorrect() throws Exception {
    for (String s : Arrays.asList(QURAN_SOURCE_FILE_ARABIC, QURAN_SOURCE_FILE_ENGLISH)) {
      try (QuranReader reader = new QuranReader(new File(s))) {
        assertThat(reader.getTotalSurahCount()).isEqualTo(6);
      }
    }
  }

  @Test
  void checkNumberOfSurahInSourceFile_IsWrong() throws Exception {
    for (String s :
        Arrays.asList(
            QURAN_SOURCE_FILE_ARABIC_WITH_ERRORS, QURAN_SOURCE_FILE_ENGLISH_WITH_ERRORS)) {
      try (QuranReader reader = new QuranReader(new File(s))) {
        assertThat(reader.getTotalSurahCount()).isEqualTo(5);
      }
    }
  }

  @Test
  void checkNumberOfAyatInSourceFile_isCorrect() throws Exception {
    for (String s : Arrays.asList(QURAN_SOURCE_FILE_ARABIC, QURAN_SOURCE_FILE_ENGLISH)) {
      try (QuranReader reader = new QuranReader(new File(s))) {
        assertThat(reader.getTotalAyahCount()).isEqualTo(313);
      }
    }
  }

  @Test
  void checkNumberOfAyatInSourceFile_isWrong() throws Exception {
    for (String s :
        Arrays.asList(
            QURAN_SOURCE_FILE_ARABIC_WITH_ERRORS, QURAN_SOURCE_FILE_ENGLISH_WITH_ERRORS)) {
      try (QuranReader reader = new QuranReader(new File(s))) {
        assertThat(reader.getTotalAyahCount()).isNotEqualTo(313);
      }
    }
  }

  @Test
  void checkNumberOfAyatForSurahInFile_isCorrect() throws Exception {
    for (String s : Arrays.asList(QURAN_SOURCE_FILE_ARABIC, QURAN_SOURCE_FILE_ENGLISH)) {
      try (QuranReader reader = new QuranReader(new File(s))) {
        assertThat(reader.getTotalAyahCountInSurah(getSurahNumber("Al-Baqarah"))).isEqualTo(286);
      }
    }
  }

  @Test
  void checkNumberOfAyatForSurahInFile_isWrong() throws Exception {
    for (String s :
        Arrays.asList(
            QURAN_SOURCE_FILE_ARABIC_WITH_ERRORS, QURAN_SOURCE_FILE_ENGLISH_WITH_ERRORS)) {
      try (QuranReader reader = new QuranReader(new File(s))) {
        assertThat(reader.getTotalAyahCountInSurah(getSurahNumber("Al-Baqarah"))).isNotEqualTo(286);
      }
    }
  }

  @Test
  void getARangeOfAyat_Arabic_isFoundAndContentIsCorrect() throws Exception {
    List<String> text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC))) {

      text = assertDoesNotThrow(() -> reader.getAyatFromToOfSuraNo(getSurahNumber("An-Nȃs"), 2, 4));
    }

    assertThat(text)
        .isEqualTo(
            List.of("مَلِكِ ٱلنَّاسِ", "إِلَٰهِ ٱلنَّاسِ", "مِن شَرِّ ٱلْوَسْوَاسِ ٱلْخَنَّاسِ"));
  }

  @Test
  void getARangeOfAyat_English_isFoundAndContentIsCorrect() throws Exception {
    List<String> text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ENGLISH))) {

      text = assertDoesNotThrow(() -> reader.getAyatFromToOfSuraNo(getSurahNumber("An-Nȃs"), 2, 4));
    }
    assertThat(text)
        .isEqualTo(
            List.of(
                "The Sovereign of mankind.",
                "The God of mankind,",
                "From the evil of the retreating whisperer -"));
  }

  @Test
  void getARangeOfAyat_Arabic_isFoundButContentIsWrong() throws Exception {
    List<String> text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC_WITH_ERRORS))) {

      text =
          assertDoesNotThrow(() -> reader.getAyatFromToOfSuraNo(getSurahNumber("Al-Ikhlȃṣ"), 2, 3));
    }
    assertThat(text).isEqualTo(List.of("ٱللَّهُ ٱلصَّمَدُ", "لَمْ يَلِدْ وَلَمْ يُولَدْ"));
  }

  @Test
  void getARangeOfAyat_English_isFoundButContentIsWrong() throws Exception {
    List<String> text;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ENGLISH_WITH_ERRORS))) {

      text = assertDoesNotThrow(() -> reader.getAyatFromToOfSuraNo(getSurahNumber("An-Nȃs"), 2, 4));
    }

    assertThat(text)
        .isNotEqualTo(
            List.of(
                "The Sovereign of mankind.",
                "The God of mankind,",
                "From the evil of the retreating whisperer -"));
  }

  @Test
  void getAyatFromToOfSuraNo_invalidRange_throwsIllegalArgumentException() throws Exception {
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC))) {

      assertThatIllegalArgumentException()
          .isThrownBy(() -> reader.getAyatFromToOfSuraNo(getSurahNumber("Al-Baqarah"), 5, 3))
          .withMessageContaining("fromAyah")
          .withMessageContaining("<=");
    }
  }

  @Test
  void getAyatFromToOfSuraNo_succeeds_returnsFullRange() throws Exception {
    List<String> ayat;
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC))) {

      ayat = reader.getAyatFromToOfSuraNo(getSurahNumber("Al-Baqarah"), 1, 3);
    }

    assertThat(ayat).hasSize(3).allSatisfy(aya -> assertThat(aya).isNotBlank());
  }

  @Test
  void getAyatFromToOfSuraNo_missingAyah_throwsQuranReaderAyahNotFound() throws Exception {
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ARABIC_WITH_ERRORS))) {

      assertThatExceptionOfType(QuranReaderAyahNotFoundException.class)
          .isThrownBy(() -> reader.getAyatFromToOfSuraNo(getSurahNumber("An-Nȃs"), 2, 4))
          .withMessageContaining("surah")
          .withMessageContaining("ayah");
    }
  }

  @Test
  void getAyatFromToOfSuraNo_singleAyahRange_returnsExactlyOne() throws Exception {
    try (QuranReader reader = new QuranReader(new File(QURAN_SOURCE_FILE_ENGLISH))) {

      List<String> ayat = reader.getAyatFromToOfSuraNo(getSurahNumber("Al-Baqarah"), 1, 1);

      assertThat(ayat)
          .singleElement()
          .isEqualTo(reader.getAyahNoOfSurahNo(getSurahNumber("Al-Baqarah"), 1));
    }
  }
}
