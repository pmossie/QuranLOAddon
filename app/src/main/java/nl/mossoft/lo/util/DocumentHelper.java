package nl.mossoft.lo.util;

import nl.mossoft.lo.quran.SourceLanguage;

public class DocumentHelper {
  /**
   * Returns the string representation of a number based on language and font.
   *
   * @param n number between 0-9
   * @return number string
   */
  public static String numToAyatNumber(int n, SourceLanguage language, String fontName) {
    final int base = FontManager.fontNumberBase(language, fontName);

    final StringBuilder as = new StringBuilder();
    while (n > 0) {
      as.append(Character.toChars(base + (n % 10)));
      n = n / 10;
    }
    return as.reverse().toString();
  }
}
