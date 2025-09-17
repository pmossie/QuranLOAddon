package nl.mossoft.lo.util;

import static nl.mossoft.lo.quran.SourceLanguage.*;

import java.util.LinkedHashMap;
import java.util.Map;
import nl.mossoft.lo.quran.SourceLanguage;

public class DocumentHelper {

  public static short getLanguageWritingMode(final SourceLanguage language) {
    final Map<SourceLanguage, Short> directionmap = new LinkedHashMap<>();

    directionmap.put(ARABIC, com.sun.star.text.WritingMode2.RL_TB);
    directionmap.put(DUTCH, com.sun.star.text.WritingMode2.LR_TB);
    directionmap.put(ENGLISH, com.sun.star.text.WritingMode2.LR_TB);
    directionmap.put(INDONESIAN, com.sun.star.text.WritingMode2.LR_TB);

    return directionmap.getOrDefault(language, com.sun.star.text.WritingMode2.LR_TB);
  }
}
