package nl.mossoft.lo.util;

import static nl.mossoft.lo.quran.SourceLanguage.ARABIC;
import static nl.mossoft.lo.util.FontManager.fontNumberBase;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FontManagerTest {

  @Test
  void fontNumberBase_KFGQPC_fonts_IsCorrect() {
    int nb = fontNumberBase(ARABIC, "KFGQPC HAFS Uthmanic Script");

    assertThat(nb).isEqualTo(0xFD50);
  }
}
