package nl.mossoft.lo.dialog;

import static nl.mossoft.lo.dialog.MainDialog.numToNumberString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nl.mossoft.lo.quran.SourceLanguage;
import org.junit.jupiter.api.Test;

class MainDialogTest {
  @Test
  void numToNumberString_Latin_Carlito_font_isCorrect() {

    String nms = numToNumberString(286, SourceLanguage.ENGLISH, "Carlito");
    assertThat(nms).isEqualTo("286");
  }

    @Test
    void numToNumberString_ARABIC_KFGQPC_HAFS_Uthmanic_Script_font_isCorrect() {

      String nms = numToNumberString(286, SourceLanguage.ARABIC, "KFGQPC HAFS Uthmanic Script");
      assertThat(nms).isEqualTo("ﵖﵘﵒ");
    }
  @Test
  void numToNumberString_ARABIC_Amiri_Quran_font_isCorrect() {

    String nms = numToNumberString(286, SourceLanguage.ARABIC, "Amiri Quran");
    assertThat(nms).isEqualTo("٢٨٦");
  }

  @Test
  void numToNumberString_ARABIC_Scheherazade_New_font_isCorrect() {

    String nms = numToNumberString(286, SourceLanguage.ARABIC, "Scheherazade New");
    assertThat(nms).isEqualTo("٢٨٦");
  }
}
