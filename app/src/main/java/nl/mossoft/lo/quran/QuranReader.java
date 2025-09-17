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

package nl.mossoft.lo.quran;

import static nl.mossoft.lo.quran.SurahManager.getSurahNumber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public final class QuranReader implements AutoCloseable {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuranReader.class);

  private Document doc;
  private XPath xpath = null;

  /**
   * Creates a document reader for the Qur'an xml files.
   *
   * @param uri to the xml file
   */
  public QuranReader(File uri) {
    try {
      final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
      df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      df.setNamespaceAware(true);

      final DocumentBuilder builder = df.newDocumentBuilder();
      doc = builder.parse(uri);

      // Create XPathFactory object
      final XPathFactory xpathFactory = XPathFactory.newInstance();

      // Create XPath object
      xpath = xpathFactory.newXPath();

    } catch (ParserConfigurationException | org.xml.sax.SAXException | java.io.IOException e) {
      LOGGER.error(String.valueOf(e));
    }
  }

  /**
   * Returns the ayah text for the given surah and ayah numbers.
   *
   * @throws QuranReaderAyahNotFoundException if the (surah, ayah) pair does not exist
   * @throws IllegalStateException if the XPath cannot be compiled or evaluated
   */
  public String getAyahNoOfSurahNo(int suraNo, int ayaNo) throws QuranReaderAyahNotFoundException {
    final String exprString =
        String.format("/quran/surah[@no='%d']/ayah[@no='%d']/@text", suraNo, ayaNo);
    try {
      XPathExpression expr = xpath.compile(exprString);
      String text = (String) expr.evaluate(doc, XPathConstants.STRING);
      if (text == null || text.isEmpty()) {
        throw new QuranReaderAyahNotFoundException(
            "Ayah not found: surah=" + suraNo + ", ayah=" + ayaNo);
      }
      return text;
    } catch (XPathExpressionException e) {
      // Don’t just log-and-continue; surface the error.
      throw new IllegalStateException("Failed to evaluate XPath: " + exprString, e);
    }
  }

  /**
   * Returns all ayat from {@code fromAyah} to {@code toAyah} (inclusive) of the given surah.
   *
   * @param suraNo surah number
   * @param fromAyah first ayah number (inclusive)
   * @param toAyah last ayah number (inclusive)
   * @return list of ayat texts
   * @throws QuranReaderAyahNotFoundException if any ayah in the range is not found
   * @throws IllegalArgumentException if {@code fromAyah > toAyah}
   */
  public List<String> getAyatFromToOfSuraNo(int suraNo, int fromAyah, int toAyah)
      throws QuranReaderAyahNotFoundException, IllegalArgumentException {

    if (fromAyah > toAyah) {
      throw new IllegalArgumentException("fromAyah must be <= toAyah");
    }

    List<String> result = new ArrayList<>(toAyah - fromAyah + 1);
    for (int ayah = fromAyah; ayah <= toAyah; ayah++) {
      result.add(getAyahNoOfSurahNo(suraNo, ayah)); // will throw if not found
    }
    return result;
  }

  /**
   * Get Bismillah.
   *
   * @return Bismillah
   */
  public String getBismillah() throws QuranReaderAyahNotFoundException {
    return getAyahNoOfSurahNo(getSurahNumber("Al-Fâtihah"), 1);
  }

  /**
   * @return number of <surah> elements in the file
   */
  public int getTotalSurahCount() {
    try {
      XPathExpression expr = xpath.compile("count(/quran/surah)");
      Double n = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
      return n.intValue();
    } catch (XPathExpressionException e) {
      LOGGER.error("getSurahCount failed", e);
      return 0;
    }
  }

  /**
   * @return total number of <ayah> elements across all surahs
   */
  public int getTotalAyahCount() {
    try {
      // Either of these are fine: "count(/quran/surah/ayah)" or "count(//ayah)"
      XPathExpression expr = xpath.compile("count(/quran/surah/ayah)");
      Double n = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
      return n.intValue();
    } catch (XPathExpressionException e) {
      LOGGER.error("getTotalAyahCount failed", e);
      return 0;
    }
  }

  /**
   * @return number of <ayah> in a specific surah (by its @no attribute)
   */
  public int getTotalAyahCountInSurah(int surahNo) {
    try {
      XPathExpression expr = xpath.compile("count(/quran/surah[@no='" + surahNo + "']/ayah)");
      Double n = (Double) expr.evaluate(doc, XPathConstants.NUMBER);
      return n.intValue();
    } catch (XPathExpressionException e) {
      LOGGER.error("getAyahCountInSurah failed for surah {}", surahNo, e);
      return 0;
    }
  }

  @Override
  public void close() throws Exception {}
}
