package nl.mossoft.lo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nl.mossoft.lo.utils.QuranReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The type Quran data file test.
 */
public class QuranDataFileTest {

  /**
   * Checks the Number of Quran Data Files.
   */
  @Test(groups = {"QuranDataFileTests"})
  public void AllFileCheckTest() {
    final List<String> fns = getQuranTxtFiles();
    final int size = fns.size();
    Assert.assertEquals(size, 8);
  }

  /**
   * Check the Number of surahs per Quran Data File.
   */
  @Test(groups = {"QuranDataFileTests"})
  public void CheckNumberOfSurahs() {
    final List<String> fns = getQuranTxtFiles();
    for (final String fn : fns) {
      System.out.println("Data File: "+ fn);
      Assert.assertEquals(countNumberOfSurahs(fn), 114);
    }
  }

  private int countNumberOfSurahs(String fn) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try (InputStream is = new FileInputStream("data/quran/" + fn)) {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(is);

      NodeList listOfSurahs = doc.getElementsByTagName("surah");
      System.out.println("Number of surahs: " + listOfSurahs.getLength());
      for (int i = 0; i < listOfSurahs.getLength(); i++) {
        Node s = listOfSurahs.item(i);
        System.out.println("Surah: " + s.getAttributes().getNamedItem("no").getNodeValue() + " has "
            + (s.getChildNodes().getLength() - 1) / 2 + " ayah");
        Assert.assertEquals((s.getChildNodes().getLength() - 1) / 2, QuranReader.getSurahSize(
            Integer.parseInt(s.getAttributes().getNamedItem("no").getNodeValue())));
      }

      return listOfSurahs.getLength();
    } catch (
        ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * Get the available Quran text files.
   *
   * @return list of files
   */
  private List<String> getQuranTxtFiles() {
    final File path = new File("data/quran");
    final String[] files = path.list((dir, name) -> name.toLowerCase()
        .endsWith(".xml"));
    assert files != null;
    final List<String> fns = Arrays.asList(files);
    Collections.sort(fns);
    return fns;
  }
}
