package nl.mossoft.lo.dialog;

import static nl.mossoft.lo.utils.Localization.getLanguageFontType;

import com.sun.star.awt.ActionEvent;
import com.sun.star.awt.FontWeight;
import com.sun.star.awt.ItemEvent;
import com.sun.star.awt.TextEvent;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XButton;
import com.sun.star.awt.XCheckBox;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XDialog;
import com.sun.star.awt.XItemListener;
import com.sun.star.awt.XListBox;
import com.sun.star.awt.XNumericField;
import com.sun.star.awt.XProgressBar;
import com.sun.star.awt.XTextComponent;
import com.sun.star.awt.XTextListener;
import com.sun.star.awt.XToolkit;
import com.sun.star.awt.XWindow;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameContainer;
import com.sun.star.frame.XController;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.VerticalAlignment;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import nl.mossoft.lo.utils.DocumentHandler;
import nl.mossoft.lo.utils.Localization;
import nl.mossoft.lo.utils.QuranReader;

/**
 * The type Insert quran text dialog.
 */
public class InsertQuranTextDialog {

  private static final String ARABIC = "Arabic";

  private static final String D000_INSERT_QURAN_TEXT_DIAOG = "D000_INSERT_QURAN_TEXT_DIAOG";
  private static final String D001_SURAH_GROUPBOX = "D001_SURAH_GROUPBOX";
  private static final String D002_SURAH_LABEL = "D002_SURAH_LABEL";
  private static final String D003_SURAH_LISTBOX = "D003_SURAH_LISTBOX";
  private static final String D004_AYAT_GROUPBOX = "D004_AYAT_GROUPBOX";
  private static final String D005_AYAT_LABEL = "D005_AYAT_LABEL";
  private static final String D006_AYAT_ALL_CHECKBOX = "D006_AYAT_ALL_CHECKBOX";
  private static final String D007_AYAT_FROM_LABEL = "D007_AYAT_FROM_LABEL";
  private static final String D008_AYAT_FROM_NUMFLD = "D008_AYAT_FROM_NUMFLD";
  private static final String D009_AYAT_TO_LBL = "D009_AYAT_TO_LBL";
  private static final String D010_AYAT_TO_NUMFLD = "D010_AYAT_TO_NUMFLD";
  private static final String D011_LANGUAGE_GROUPBOX = "D011_LANGUAGE_GROUPBOX";
  private static final String D012_ARABIC_LANGUAGE_LABEL = "D012_ARABIC_LANGUAGE_LABEL";
  private static final String D013_ARABIC_LANGUAGE_CHECKBOX = "D013_ARABIC_CHECKBOX";
  private static final String D014_ARABIC_LANGUAGE_VERSION_LISTBOX =
      "D014_ARABIC_LANGUAGE_VERSION_LISTBOX";
  private static final String D015_TRANSLATION_LANGUAGE_LABEL = "D015_TRANSLATION_LANGUAGE_LABEL";
  private static final String D016_TRANSLATION_LANGUAGE_CHECKBOX =
      "D016_TRANSLATION_LANGUAGE_CHECKBOX";
  private static final String D017_TRANSLATION_LANGUAGE_VERSION_LISTBOX =
      "D017_TRANSLATION_LANGUAGE_VERSION_LISTBOX";
  private static final String D018_TRANSLITERATION_LANGUAGE_LABEL =
      "D018_TRANSLITERATION_LANGUAGE_LABEL";
  private static final String D019_TRANSLITERATION_LANGUAGE_CHECKBOX =
      "D019_TRANSLITERATION_LANGUAGE_CHECKBOX";
  private static final String D020_TRANSLITERATION_LANGUAGE_VERSION_LISTBOX =
      "D020_TRANSLITERATION_LANGUAGE_VERSION_LISTBOX";
  private static final String D021_ARABIC_FONT_GROUPBOX = "D021_ARABIC_FONT_GROUPBOX";
  private static final String D022_ARABIC_FONT_LABEL = "D022_ARABIC_FONT_LABEL";
  private static final String D023_ARABIC_FONT_LISTBOX = "D023_ARABIC_FONT_LISTBOX";
  private static final String D024_ARABIC_FONTSIZE_LABEL = "D024_ARABIC_FONTSIZE_LABEL";
  private static final String D025_ARABIC_FONTSIZE_NUMFLD = "D025_ARABIC_FONTSIZE_NUMFLD";
  private static final String D026_LATIN_FONT_GROUPBOX = "D026_LATIN_FONT_GROUPBOX";
  private static final String D027_LATIN_FONT_LABEL = "D027_LATIN_FONT_LABEL";
  private static final String D028_LATIN_FONT_LISTBOX = "D028_LATIN_FONT_LISTBOX";
  private static final String D029_LATIN_FONTSIZE_LABEL = "D029_LATIN_FONTSIZE_LABEL";
  private static final String D030_LATIN_FONTSIZE_NUMFLD = "D030_LATIN_FONTSIZE_NUMFLD";
  private static final String D031_MISCELLANEOUS_GROUPBOX = "D031_MISCELLANEOUS_GROUPBOX";
  private static final String D032_LINE_BY_LINE_LABEL = "D032_LINE_BY_LINE_LABEL";
  private static final String D033_LINE_BY_LINE_CHECKBOX = "D033_LINE_BY_LINE_CHECKBOX";
  private static final String D034_OK_BUTTON = "D034_OK_BUTTON";
  private static final String D035_PROGRESS_BAR = "D035_PROGRESS_BAR";

  private static final String PROP_ALIGN = "Align";
  private static final String PROP_CHAR_HEIGHT_COMPLEX = "CharHeightComplex";
  private static final String PROP_CHAR_FONT_NAME = "CharFontName";
  private static final String PROP_CHAR_FONT_NAME_COMPLEX = "CharFontNameComplex";
  private static final String PROP_CHAR_HEIGHT = "CharHeight";
  private static final String PROP_DECIMAL_ACCURACY = "DecimalAccuracy";
  private static final String PROP_DROPDOWN = "Dropdown";
  private static final String PROP_ENABLED = "Enabled";
  private static final String PROP_FONT_WEIGHT = "FontWeight";
  private static final String PROP_HEIGHT = "Height";
  private static final String PROP_LABEL = "Label";
  private static final String PROP_NAME = "Name";
  private static final String PROP_POSITION_X = "PositionX";
  private static final String PROP_POSITION_Y = "PositionY";
  private static final String PROP_SPIN = "Spin";
  private static final String PROP_STATE = "State";
  private static final String PROP_TITLE = "Title";
  private static final String PROP_TRI_STATE = "TriState";
  private static final String PROP_VERTICAL_ALIGN = "VerticalAlign";
  private static final String PROP_WIDTH = "Width";

  private static final String RESOURCE_BUNDLE = "nl.mossoft.lo.messages.DialogLabels";

  private static final String LEFT_PARENTHESIS = new String(Character.toChars(0xFD3E));
  private static final String RIGHT_PARENTHESIS = new String(Character.toChars(0xFD3F));

  private final XComponentContext componentContext;
  private final XControlContainer controlContainer;
  private final XDialog dialog;
  private final XMultiServiceFactory multiServiceFctry;
  private final XNameContainer nameContainer;
  private final ResourceBundle resourceBundle;

  private final boolean selectedTransliterationInd = false;
  private final String selectedTransliterationLanguage = "";
  private final String selectedTransliterationLanguageVersion = "";
  private boolean selectedLineNumberInd = true;
  private boolean selectedLineByLineInd = true;
  private String selectedLatinFontName = "";
  private String selectedArabicFontName = "";
  private double selectedArabicFontSize;
  private String selectedTranslationLanguage = "";
  private String selectedTranslationLanguageVersion = "";
  private String selectedArabicLanguage = "";
  private String selectedArabicLanguageVersion = "";
  private boolean selectedArabicInd = false;
  private boolean selectedTranslationInd = false;
  private double selectedLatinFontSize;
  private boolean selectedAyatAllInd = true;
  private long selectedAyatFrom = 1;
  private long selectedAyatTo = 7;
  private int selectedSurahNo = 1;

  private String defaultArabicFontName;
  private double defaultArabicFontSize;
  private String defaultLatinFontName;
  private double defaultLatinFontSize;

  /**
   * Instantiates a new Insert quran text dialog.
   *
   * @param context the context
   */
  public InsertQuranTextDialog(final XComponentContext context) {
    try {
      this.componentContext = context;
      this.resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, new Locale("en", "US"));

      Object dialogModel = createInstanceWithContext("UnoControlDialogModel",
          this.componentContext);
      XPropertySet ps = getPropertSet(dialogModel);
      ps.setPropertyValue(PROP_POSITION_X, 100);
      ps.setPropertyValue(PROP_POSITION_Y, 100);
      ps.setPropertyValue(PROP_WIDTH, 296);
      ps.setPropertyValue(PROP_HEIGHT, 170);
      ps.setPropertyValue(PROP_TITLE, resourceBundle.getString(D000_INSERT_QURAN_TEXT_DIAOG));

      // get the service manager from the dialog model
      this.multiServiceFctry = UnoRuntime.queryInterface(XMultiServiceFactory.class, dialogModel);

      this.nameContainer = UnoRuntime.queryInterface(XNameContainer.class, dialogModel);

      Object unoDialogControl = createInstanceWithContext("UnoControlDialog",
          this.componentContext);

      XControl control = UnoRuntime.queryInterface(XControl.class, unoDialogControl);
      this.controlContainer = UnoRuntime.queryInterface(XControlContainer.class, unoDialogControl);
      XControlModel controlModel = UnoRuntime.queryInterface(XControlModel.class, dialogModel);
      control.setModel(controlModel);

      createSurahGroupBox(4, 5);
      createAyatGroupBox(4, 32);
      createLanguageGroupBox(4, 91);
      createArabicFontGroupBox(150, 5);
      createLatinFontGroupBox(150, 64);
      createMiscellaneousGroupBox(150, 123);
      createStatusGroupBox(0, 150);

      // create a peer
      Object extToolkit = createInstanceWithContext("ExtToolkit", this.componentContext);

      XToolkit toolkit = UnoRuntime.queryInterface(XToolkit.class, extToolkit);
      XWindow window = UnoRuntime.queryInterface(XWindow.class, control);
      window.setVisible(false);
      control.createPeer(toolkit, null);

      this.dialog = UnoRuntime.queryInterface(XDialog.class, unoDialogControl);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Load addon dialog insert quran text dialog.
   *
   * @param context the context
   * @return the insert quran text dialog
   */
  public static InsertQuranTextDialog loadAddonDialog(final XComponentContext context) {
    return new InsertQuranTextDialog(context);
  }

  /**
   * Returns the string representation of a number based on language and font.
   *
   * @param n        number between 0-9
   * @param language language
   * @param fontname fontname
   * @return number string
   */
  public static String numToAyatNumber(long n, final String language, final String fontname) {
    final int base = Localization.getFontNumberBase(fontname);

    final StringBuilder as = new StringBuilder();
    while (n > 0) {
      as.append(Character.toChars(base + (int) (n % 10)));
      n = n / 10;
    }
    return as.reverse()
        .toString();
  }

  /**
   * Convert boolean to short.
   *
   * @param b the boolean
   * @return the short
   */
  private static short boolean2Short(final boolean b) {
    return (short) (b ? 1 : 0);
  }

  /**
   * Transforms the listbox item description of a languguage listbox into a language.
   *
   * @param item the listbox item
   * @return the language
   */
  private static String getItemLanguague(final String item) {
    final String[] itemsSelected = item.split("[(]");
    return itemsSelected[0].trim();
  }

  /**
   * Transforms the listbox item description of a languguage listbox into a text version.
   *
   * @param item the listbox item
   * @return the version
   */
  private static String getItemVersion(final String item) {
    final String[] itemsSelected = item.split("[(]");
    return itemsSelected[1].replace(")", " ")
        .trim()
        .replace(" ", "_");
  }

  private static XPropertySet getPropertSet(Object object) {
    return UnoRuntime.queryInterface(XPropertySet.class, object);
  }

  /**
   * Convert short to boolean.
   *
   * @param s the short
   * @return the boolean
   */
  private static boolean short2Boolean(final short s) {
    return s != 0;
  }

  /**
   * Add Handler for  the  Arabic Font ListBox.
   */
  private void addArabicFontListBoxHandler() {
    final XListBox listBox = getControl(XListBox.class, D023_ARABIC_FONT_LISTBOX);
    listBox.addItemListener(new XItemListener() {
      @Override
      public void disposing(EventObject eventObject) { /* Empty */
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        selectedArabicFontName = listBox.getSelectedItem();
      }
    });
  }

  /**
   * Handler for the Arabic Fontsize NumericField.
   */
  private void addArabicFontSizeHandler() {
    final XNumericField sizeField = getControl(XNumericField.class, D025_ARABIC_FONTSIZE_NUMFLD);
    final XTextComponent sizeTextComponent = getControl(XTextComponent.class,
        D025_ARABIC_FONTSIZE_NUMFLD);

    sizeTextComponent.addTextListener(new XTextListener() {
      @Override
      public void disposing(EventObject eventObject) { /* Empty */
      }

      @Override
      public void textChanged(TextEvent textEvent) {
        selectedArabicFontSize = sizeField.getValue();
      }
    });
  }

  /**
   * Handler for the Arabic Language CheckButton.
   */
  private void addArabicLanguageCheckBoxHandler() {
    final XCheckBox checkBox = getControl(XCheckBox.class, D013_ARABIC_LANGUAGE_CHECKBOX);
    checkBox.addItemListener(new XItemListener() {

      @Override
      public void disposing(EventObject eventObject) { /* Empty */
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        checkBox.setState(boolean2Short(!selectedArabicInd));
        selectedArabicInd = short2Boolean(checkBox.getState());

        enableControl(D012_ARABIC_LANGUAGE_LABEL, selectedArabicInd);
        enableControl(D014_ARABIC_LANGUAGE_VERSION_LISTBOX, selectedArabicInd);
        enableControl(D021_ARABIC_FONT_GROUPBOX, selectedArabicInd);
        enableControl(D022_ARABIC_FONT_LABEL, selectedArabicInd);
        enableControl(D023_ARABIC_FONT_LISTBOX, selectedArabicInd);
        enableControl(D024_ARABIC_FONTSIZE_LABEL, selectedArabicInd);
        enableControl(D025_ARABIC_FONTSIZE_NUMFLD, selectedArabicInd);
        enableControl(D034_OK_BUTTON,
            selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
        enableControl(D031_MISCELLANEOUS_GROUPBOX,
            selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
        enableControl(D032_LINE_BY_LINE_LABEL,
            selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
        enableControl(D033_LINE_BY_LINE_CHECKBOX,
            selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
      }
    });
  }

  /**
   * Add Handler for Arabic Version ListBox.
   */
  private void addArabicLanguageVersionListBoxHandler() {
    final XListBox listBox = getControl(XListBox.class, D014_ARABIC_LANGUAGE_VERSION_LISTBOX);
    listBox.addItemListener(new XItemListener() {
      @Override
      public void disposing(EventObject eventObject) { /* empty */
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        selectedArabicLanguage = getItemLanguague(listBox.getSelectedItem());
        selectedArabicLanguageVersion = getItemVersion(listBox.getSelectedItem());
      }
    });
  }

  /**
   * Add Handler for the All Ayat CheckBox.
   */
  private void addAyatAllCheckBoxHandler() {
    final XCheckBox checkBox = getControl(XCheckBox.class, D006_AYAT_ALL_CHECKBOX);
    checkBox.addItemListener(new XItemListener() {
      @Override
      public void disposing(EventObject eventObject) { /*empty*/
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        checkBox.setState(boolean2Short(!selectedAyatAllInd));
        selectedAyatAllInd = short2Boolean(checkBox.getState());

        enableControl(D007_AYAT_FROM_LABEL, !selectedAyatAllInd);
        enableControl(D008_AYAT_FROM_NUMFLD, !selectedAyatAllInd);
        enableControl(D009_AYAT_TO_LBL, !selectedAyatAllInd);
        enableControl(D010_AYAT_TO_NUMFLD, !selectedAyatAllInd);

        if (selectedAyatAllInd) {
          initializeAyatFrom();
          initializeAyatTo();
        }
      }
    });
  }

  /**
   * Add Handler for the Ayat From NumericField.
   */
  private void addAyatFromFieldHandler() {
    final XNumericField fromField = getControl(XNumericField.class, D008_AYAT_FROM_NUMFLD);
    final XTextComponent fromTextComponent = getControl(XTextComponent.class,
        D008_AYAT_FROM_NUMFLD);
    fromTextComponent.addTextListener(new XTextListener() {
      @Override
      public void disposing(EventObject eventObject) { /*empty*/
      }

      @Override
      public void textChanged(TextEvent textEvent) {
        if (Math.round(fromField.getValue()) >= selectedAyatTo) {
          fromField.setValue(selectedAyatTo);
        } else if (Math.round(fromField.getValue()) <= 1) {
          fromField.setValue(1);
        }
        selectedAyatFrom = Math.round(fromField.getValue());
      }
    });
  }

  /**
   * Add Handler for the Ayat To NumericField.
   */
  private void addAyatToFieldHandler() {
    final XNumericField toField = getControl(XNumericField.class, D010_AYAT_TO_NUMFLD);
    final XTextComponent toTextComponent = getControl(XTextComponent.class, D010_AYAT_TO_NUMFLD);
    toTextComponent.addTextListener(new XTextListener() {
      @Override
      public void disposing(EventObject eventObject) { /*empty*/
      }

      @Override
      public void textChanged(TextEvent textEvent) {
        if (Math.round(toField.getValue()) <= selectedAyatFrom) {
          toField.setValue(selectedAyatFrom);
        } else if (Math.round(toField.getValue()) >= QuranReader.getSurahSize(selectedSurahNo)) {
          toField.setValue(QuranReader.getSurahSize(selectedSurahNo));
        }
        selectedAyatTo = Math.round(toField.getValue());
      }
    });
  }

  private void addLatinFontListBoxHandler() {
    final XListBox listBox = getControl(XListBox.class, D028_LATIN_FONT_LISTBOX);
    listBox.addItemListener(new XItemListener() {
      @Override
      public void disposing(EventObject eventObject) { /* Empty */
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        selectedLatinFontName = listBox.getSelectedItem();
      }
    });
  }

  private void addLatinFontSizeHandler() {
    final XNumericField sizeField = getControl(XNumericField.class, D030_LATIN_FONTSIZE_NUMFLD);
    final XTextComponent sizeTextComponent = getControl(XTextComponent.class,
        D030_LATIN_FONTSIZE_NUMFLD);

    sizeTextComponent.addTextListener(new XTextListener() {
      @Override
      public void disposing(EventObject eventObject) { /* Empty */
      }

      @Override
      public void textChanged(TextEvent textEvent) {
        selectedLatinFontSize = sizeField.getValue();
      }
    });
  }

  /**
   * Add Handler for the Line By Line CheckButton.
   */
  private void addLineByLineCheckBoxHandler() {
    final XCheckBox checkBox = getControl(XCheckBox.class, D033_LINE_BY_LINE_CHECKBOX);
    checkBox.addItemListener(new XItemListener() {
      @Override
      public void disposing(EventObject eventObject) { /* Empty */
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        checkBox.setState(boolean2Short(!selectedLineByLineInd));
        selectedLineByLineInd = short2Boolean(checkBox.getState());
      }
    });
  }

  private void addOkButtonHandler() {
    final XButton button = getControl(XButton.class, D034_OK_BUTTON);
    button.addActionListener(new XActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        writeSurah(selectedSurahNo);
        endShow();
      }

      @Override
      public void disposing(EventObject eventObject) { /* Empty */
      }
    });
  }

  /**
   * Handler for the Surah listbox.
   */
  private void addSurahListBoxHandler() {
    XListBox listBox = getControl(XListBox.class, D003_SURAH_LISTBOX);
    listBox.addItemListener(new XItemListener() {
      @Override
      public void disposing(EventObject eventObject) { /* Empty */
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        selectedSurahNo = listBox.getSelectedItemPos() + 1;

        final XNumericField numericFieldFrom = getControl(XNumericField.class,
            D008_AYAT_FROM_NUMFLD);
        numericFieldFrom.setValue(1);
        selectedAyatFrom = Math.round(numericFieldFrom.getValue());

        final XNumericField numericFieldTo = getControl(XNumericField.class, D010_AYAT_TO_NUMFLD);
        numericFieldTo.setValue(QuranReader.getSurahSize(selectedSurahNo));
        selectedAyatTo = Math.round(numericFieldTo.getValue());
      }

    });
  }

  /**
   * Handler for the Translation CheckButton.
   */
  private void addTranslationLanguageCheckBoxHandler() {
    XCheckBox checkBox = getControl(XCheckBox.class, D016_TRANSLATION_LANGUAGE_CHECKBOX);
    checkBox.addItemListener(new XItemListener() {

      @Override
      public void disposing(EventObject eventObject) { /* EMPTY */
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        checkBox.setState(boolean2Short(!selectedTranslationInd));
        selectedTranslationInd = short2Boolean(checkBox.getState());

        enableControl(D017_TRANSLATION_LANGUAGE_VERSION_LISTBOX, selectedTranslationInd);
        enableControl(D015_TRANSLATION_LANGUAGE_LABEL, selectedTranslationInd);
        enableControl(D026_LATIN_FONT_GROUPBOX,
            selectedTransliterationInd || selectedTranslationInd);
        enableControl(D027_LATIN_FONT_LABEL, selectedTransliterationInd || selectedTranslationInd);
        enableControl(D028_LATIN_FONT_LISTBOX,
            selectedTransliterationInd || selectedTranslationInd);
        enableControl(D029_LATIN_FONTSIZE_LABEL,
            selectedTransliterationInd || selectedTranslationInd);
        enableControl(D030_LATIN_FONTSIZE_NUMFLD,
            selectedTransliterationInd || selectedTranslationInd);
        enableControl(D034_OK_BUTTON,
            selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
        enableControl(D031_MISCELLANEOUS_GROUPBOX,
            selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
        enableControl(D032_LINE_BY_LINE_LABEL,
            selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
        enableControl(D033_LINE_BY_LINE_CHECKBOX,
            selectedArabicInd || selectedTranslationInd || selectedTransliterationInd);
      }
    });
  }

  private void addTranslationLanguageVersionListBoxHandler() {
    XListBox listBox = getControl(XListBox.class, D017_TRANSLATION_LANGUAGE_VERSION_LISTBOX);
    listBox.addItemListener(new XItemListener() {
      @Override
      public void disposing(EventObject eventObject) { /* EMPTY */
      }

      @Override
      public void itemStateChanged(ItemEvent itemEvent) {
        selectedTranslationLanguage = getItemLanguague(listBox.getSelectedItem());
        selectedTranslationLanguageVersion = getItemVersion(listBox.getSelectedItem());
      }
    });
  }

  private void addTransliterationCheckBoxHandler() { /* NOT YET IMPLEMENTED */
  }

  private void addTransliterationLanguageVersionListBoxHandler() { /* NOT YET IMPLEMENTED */
  }

  private void createArabicFontGroupBox(final int x, final int y)
      throws
      Exception {
    insertGroupBox(D021_ARABIC_FONT_GROUPBOX, x, y, 142, 59, true);
    insertLabel(D022_ARABIC_FONT_LABEL, x, y + 10, 50, 10, true);
    insertListBox(D023_ARABIC_FONT_LISTBOX, x + 67, y + 10, 73, 10, true);
    addArabicFontListBoxHandler();

    insertLabel(D024_ARABIC_FONTSIZE_LABEL, x, y + 26, 50, 10, true);
    insertNumericField(D025_ARABIC_FONTSIZE_NUMFLD, x + 67, y + 26, 50, 10, true);
    addArabicFontSizeHandler();
  }

  private void createAyatGroupBox(int x, int y)
      throws
      Exception {
    insertGroupBox(D004_AYAT_GROUPBOX, x, y, 142, 59, true);

    insertLabel(D005_AYAT_LABEL, x, y + 10, 50, 10, true);

    insertCheckBox(D006_AYAT_ALL_CHECKBOX, x + 55, y + 10, 10, 10, (short) 1, true);
    addAyatAllCheckBoxHandler();

    insertLabel(D007_AYAT_FROM_LABEL, x, y + 26, 50, 10, false);

    insertNumericField(D008_AYAT_FROM_NUMFLD, x + 67, y + 26, 50, 10, false);
    addAyatFromFieldHandler();

    insertLabel(D009_AYAT_TO_LBL, x, y + 42, 50, 10, false);

    insertNumericField(D010_AYAT_TO_NUMFLD, x + 67, y + 42, 50, 10, false);
    addAyatToFieldHandler();
  }

  private Object createInstance(String s)
      throws
      Exception {
    return this.multiServiceFctry.createInstance("com.sun.star.awt." + s);
  }

  private Object createInstanceWithContext(final String s, final XComponentContext c)
      throws
      Exception {
    return this.componentContext.getServiceManager()
        .createInstanceWithContext("com.sun.star.awt" + "." + s, c);
  }

  private void createLanguageGroupBox(final int x, final int y)
      throws
      Exception {
    insertGroupBox(D011_LANGUAGE_GROUPBOX, x, y, 142, 59, true);

    insertLabel(D012_ARABIC_LANGUAGE_LABEL, x, y + 10, 50, 10, true);
    insertCheckBox(D013_ARABIC_LANGUAGE_CHECKBOX, x + 55, y + 10, 10, 10, (short) 1, true);
    addArabicLanguageCheckBoxHandler();
    insertListBox(D014_ARABIC_LANGUAGE_VERSION_LISTBOX, x + 67, y + 10, 73, 10, true);
    addArabicLanguageVersionListBoxHandler();

    insertLabel(D015_TRANSLATION_LANGUAGE_LABEL, x, y + 26, 50, 10, false);
    insertCheckBox(D016_TRANSLATION_LANGUAGE_CHECKBOX, x + 55, y + 26, 10, 10, (short) 0, true);
    addTranslationLanguageCheckBoxHandler();
    insertListBox(D017_TRANSLATION_LANGUAGE_VERSION_LISTBOX, x + 67, y + 26, 73, 10, false);
    addTranslationLanguageVersionListBoxHandler();

    insertLabel(D018_TRANSLITERATION_LANGUAGE_LABEL, x, y + 42, 50, 10, false);
    insertCheckBox(D019_TRANSLITERATION_LANGUAGE_CHECKBOX,
        x + 55,
        y + 42,
        10,
        10,
        (short) 0,
        false);
    addTransliterationCheckBoxHandler();
    insertListBox(D020_TRANSLITERATION_LANGUAGE_VERSION_LISTBOX, x + 67, y + 42, 73, 10, false);
    addTransliterationLanguageVersionListBoxHandler();
  }

  private void createLatinFontGroupBox(int x, int y)
      throws
      Exception {
    insertGroupBox(D026_LATIN_FONT_GROUPBOX, x, y, 142, 59, false);
    insertLabel(D027_LATIN_FONT_LABEL, x, y + 10, 50, 10, false);
    insertListBox(D028_LATIN_FONT_LISTBOX, x + 67, y + 10, 73, 10, false);
    addLatinFontListBoxHandler();

    insertLabel(D029_LATIN_FONTSIZE_LABEL, x, y + 26, 50, 10, false);
    insertNumericField(D030_LATIN_FONTSIZE_NUMFLD, x + 67, y + 26, 50, 10, false);
    addLatinFontSizeHandler();
  }

  private void createMiscellaneousGroupBox(int x, int y)
      throws
      Exception {
    insertGroupBox(D031_MISCELLANEOUS_GROUPBOX, x, y, 142, 27, true);
    insertLabel(D032_LINE_BY_LINE_LABEL, x, y + 10, 50, 10, true);
    insertCheckBox(D033_LINE_BY_LINE_CHECKBOX, x + 55, y + 10, 10, 10, (short) 1, true);
    addLineByLineCheckBoxHandler();
  }

  private void createStatusGroupBox(int x, int y)
      throws
      Exception {
    insertButton(D034_OK_BUTTON, x + 250, y + 2, 40, 15, true);
    addOkButtonHandler();
    insertProgressBar(D035_PROGRESS_BAR, x, y + 17, 295, 4, false);
  }

  private void createSurahGroupBox(final int x, final int y)
      throws
      Exception {
    insertGroupBox(D001_SURAH_GROUPBOX, x, y, 142, 27, true);
    insertLabel(D002_SURAH_LABEL, x, y + 10, 50, 10, true);
    insertListBox(D003_SURAH_LISTBOX, x + 67, y + 10, 73, 10, true);
    addSurahListBoxHandler();
  }

  private void enableControl(final String id, final boolean toggle) {
    XControl control = getControl(XControl.class, id);
    XPropertySet ps = getPropertSet(control.getModel());
    try {
      ps.setPropertyValue(PROP_ENABLED, toggle);
    } catch (IllegalArgumentException
             | UnknownPropertyException
             | PropertyVetoException
             | WrappedTargetException e) {
      e.printStackTrace();
    }
  }

  /**
   * End show.
   */
  public void endShow() {
    this.dialog.endExecute();
  }

  /**
   * Gets a ayat for the selected Surah.
   *
   * @param surahno  the surah number
   * @param ayahno   the ayat number
   * @param language the language to be used
   * @param version  the text version to be used
   * @param fontName the font used to write the text
   * @return the ayat
   */
  private String getAyahLine(
      final int surahno,
      final long ayahno,
      final String language,
      final String version,
      String fontName) {

    final QuranReader qr = new QuranReader(language, version, this.componentContext);

    String line = qr.getAyahNoOfSuraNo(surahno, ayahno);
    if (selectedLineNumberInd) {
      if (Localization.getLanguageWritingMode(language) == com.sun.star.text.WritingMode2.RL_TB) {
        line = line + " " + RIGHT_PARENTHESIS + numToAyatNumber(ayahno,
            language,
            fontName) + LEFT_PARENTHESIS + " ";
      } else {
        line = "(" + numToAyatNumber(ayahno, language, fontName) + ") " + line;
      }
    }
    return line;
  }

  /**
   * Gets the text Bismillah in the language from a text version.
   *
   * @param language the language to be used
   * @param version  the text version to be used
   * @return the text
   */
  private String getBismillah(final String language, final String version) {
    final QuranReader qr = new QuranReader(language, version, this.componentContext);
    return qr.getBismillah();
  }

  private <T> T getControl(final Class<T> type, final String id) {
    return UnoRuntime.queryInterface(type, this.controlContainer.getControl(id));
  }

  /**
   * Get the default font for Arabic.
   *
   * @return the fontsize
   */
  private String getDefaultArabicFontName() {
    return defaultArabicFontName;
  }

  /**
   * Get the default fontsize for Arabic.
   *
   * @return the fontsize
   */
  private double getDefaultArabicFontsize() {
    return defaultArabicFontSize;
  }

  /**
   * Get the default font for non Arabic.
   *
   * @return the fontsize
   */
  private String getDefaultLatinFontName() {
    return defaultLatinFontName;
  }

  /**
   * Get the default latin fontsize.
   *
   * @return the fontsize
   */
  private double getDefaultLatinFontSize() {
    return defaultLatinFontSize;
  }

  /**
   * Get default settings from LibreOffice.
   */
  private void getLoDocumentDefaults() {
    final XTextDocument textDoc = DocumentHandler.getCurrentDocument(this.componentContext);
    final XController controller = textDoc.getCurrentController();
    final XTextViewCursorSupplier textViewCursorSupplier = DocumentHandler.getCursorSupplier(
        controller);
    final XTextViewCursor textViewCursor = textViewCursorSupplier.getViewCursor();
    final XText text = textViewCursor.getText();
    final XTextCursor textCursor = text.createTextCursorByRange(textViewCursor.getStart());
    final XParagraphCursor paragraphCursor = UnoRuntime.queryInterface(XParagraphCursor.class,
        textCursor);
    final XPropertySet paragraphCursorPropertySet = DocumentHandler.getPropertySet(paragraphCursor);

    try {
      defaultArabicFontSize = (float) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_HEIGHT_COMPLEX);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      defaultArabicFontSize = 10;
    }
    try {
      defaultArabicFontName = (String) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_FONT_NAME_COMPLEX);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      defaultArabicFontName = "No Default set";
    }
    try {
      defaultLatinFontName = (String) paragraphCursorPropertySet.getPropertyValue(
          PROP_CHAR_FONT_NAME);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      defaultLatinFontName = "No Default set";
    }
    try {
      defaultLatinFontSize = (float) paragraphCursorPropertySet.getPropertyValue(PROP_CHAR_HEIGHT);
    } catch (UnknownPropertyException | WrappedTargetException e) {
      defaultLatinFontSize = 10;
    }
  }

  /**
   * Get the available Quran text files.
   *
   * @return list of files
   */
  private List<String> getQuranTxtFiles() {
    final File path = QuranReader.getFilePath("data/quran", this.componentContext);
    assert path != null;
    final String[] files = path.list((dir, name) -> name.toLowerCase()
        .endsWith(".xml"));
    assert files != null;
    final List<String> fns = Arrays.asList(files);
    Collections.sort(fns);
    return fns;
  }

  /**
   * Iniializes the listbox with all the fonts that support Arabic characters.
   */
  private void initializeArabicFontListBox() {
    final XListBox listBox = getControl(XListBox.class, D023_ARABIC_FONT_LISTBOX);
    final Locale locale = new Locale.Builder().setScript("ARAB")
        .build();
    final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames(locale);

    for (int i = 0; i < fonts.length; i++) {
      if (new Font(fonts[i], Font.PLAIN, 10).canDisplay(0x0627)) { // If Alif -> Arabic support
        listBox.addItem(fonts[i], (short) i);
        if (fonts[i].equals(getDefaultArabicFontName())) {
          listBox.selectItemPos((short) i, true);
        }
      }
    }
    listBox.selectItem(getDefaultArabicFontName(), true);
    selectedArabicFontName = listBox.getSelectedItem();
  }

  /**
   * Initialize the Arabic Fontsize NumericField.
   */
  private void initializeArabicFontSize() {
    final XNumericField sizeField = getControl(XNumericField.class, D025_ARABIC_FONTSIZE_NUMFLD);

    sizeField.setValue(getDefaultArabicFontsize());
    selectedArabicFontSize = getDefaultArabicFontsize();
  }

  /**
   * Iniializes the Arabic Language checkbox.
   */
  private void initializeArabicLanguageCheckBox() {
    final XCheckBox checkBox = getControl(XCheckBox.class, D013_ARABIC_LANGUAGE_CHECKBOX);

    checkBox.setState(boolean2Short(true));
    selectedArabicInd = short2Boolean(checkBox.getState());

    new Thread(() -> {
      initializeArabicLanguageVersionListBox();
      initializeArabicFontListBox();
      initializeArabicFontSize();
    }).start();
  }

  /**
   * Iniializes the listbox with all the Arabic versions of the Qur'an.
   */
  private void initializeArabicLanguageVersionListBox() {
    final XListBox listBox = getControl(XListBox.class, D014_ARABIC_LANGUAGE_VERSION_LISTBOX);

    final List<String> fns = getQuranTxtFiles();
    int k = 0;
    for (final String fn : fns) {
      final String[] parts = fn.split("[.]");
      if (parts[1].equals(ARABIC)) {
        listBox.addItem(parts[1] + " (" + parts[2].replace("_", " ") + ")", (short) k++);
      }
    }
    if (listBox.getItemCount() > 0) {
      listBox.selectItemPos((short) 0, true);

      selectedArabicLanguage = getItemLanguague(listBox.getSelectedItem());
      selectedArabicLanguageVersion = getItemVersion(listBox.getSelectedItem());
    }
  }

  /**
   * Initialize the Arabic LineNumber CheckBox.
   */
  private void initializeArabicLineNumberCheckBox() {
    selectedLineNumberInd = true;
  }

  /**
   * Iniializes the all ayat checkbox.
   */
  private void initializeAyatAllChkBx() {
    final XCheckBox checkBox = getControl(XCheckBox.class, D006_AYAT_ALL_CHECKBOX);

    checkBox.setState(boolean2Short(true));
    selectedAyatAllInd = short2Boolean(checkBox.getState());
  }

  /**
   * Initialize the Ayat From NumericField.
   */
  private void initializeAyatFrom() {
    final XNumericField numericFieldFrom = getControl(XNumericField.class, D008_AYAT_FROM_NUMFLD);

    numericFieldFrom.setValue(1);
    selectedAyatFrom = Math.round(numericFieldFrom.getValue());
  }

  /**
   * Initialize the Ayat To NumericField.
   */
  private void initializeAyatTo() {
    final XNumericField numericFieldTo = getControl(XNumericField.class, D010_AYAT_TO_NUMFLD);

    numericFieldTo.setValue(QuranReader.getSurahSize(selectedSurahNo));
    selectedAyatTo = Math.round(numericFieldTo.getValue());
  }

  private void initializeInsertQuranDialog() {
    getLoDocumentDefaults();

    initializeSurahLstBx();
    initializeAyatAllChkBx();
    initializeAyatTo();
    initializeAyatFrom();
    initializeArabicLanguageCheckBox();
    initializeTranslationLanguageCheckBox();
    initializeTransliterationLanguageCheckBox();
    initializeArabicLineNumberCheckBox();
    initializeLineByLineCheckBox();
    initializeWriteSurahProgressBar();
  }

  /**
   * Initializes the listbox with all the fonts that support Latin characters.
   */
  private void initializeLatinFontListBox() {
    final XListBox listBox = getControl(XListBox.class, D028_LATIN_FONT_LISTBOX);

    final Locale locale = new Locale.Builder().setScript("LATN")
        .build();
    final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames(locale);
    for (int i = 0; i < fonts.length; i++) {
      listBox.addItem(fonts[i], (short) i);
      if (fonts[i].equals(getDefaultLatinFontName())) {
        listBox.selectItemPos((short) i, true);
      }
    }
    listBox.selectItem(getDefaultLatinFontName(), true);
    selectedLatinFontName = listBox.getSelectedItem();
  }

  /**
   * Initialize the Latin Fontsize NumericField.
   */
  private void initializeLatinFontSize() {
    final XNumericField sizeField = getControl(XNumericField.class, D030_LATIN_FONTSIZE_NUMFLD);

    sizeField.setValue(getDefaultLatinFontSize());
    selectedLatinFontSize = getDefaultLatinFontSize();
  }

  /**
   * Iniializes the Line By Line checkbox.
   */
  private void initializeLineByLineCheckBox() {
    XCheckBox checkBox = getControl(XCheckBox.class, D033_LINE_BY_LINE_CHECKBOX);

    checkBox.setState(boolean2Short(true));
    selectedLineByLineInd = short2Boolean(checkBox.getState());
  }

  /**
   * Initializes the listbox with all the surah names of the Qur'an.
   */
  private void initializeSurahLstBx() {
    final XListBox listBox = getControl(XListBox.class, D003_SURAH_LISTBOX);

    for (int i = 0; i < 114; i++) {
      listBox.addItem(QuranReader.getSurahName(i + 1) + " (" + (i + 1) + ")", (short) i);
    }
    listBox.selectItemPos((short) 0, true);
    selectedSurahNo = listBox.getSelectedItemPos() + 1;
  }

  private void initializeTranslationLanguageCheckBox() {
    final XCheckBox checkBox = getControl(XCheckBox.class, D016_TRANSLATION_LANGUAGE_CHECKBOX);

    checkBox.setState(boolean2Short(false));
    selectedTranslationInd = short2Boolean(checkBox.getState());

    new Thread(() -> {
      initializeTranslationLanguageVersionListBox();
      initializeLatinFontListBox();
      initializeLatinFontSize();
    }).start();
  }

  /**
   * Iniializes the listbox with all the Translation versions of the Qur'an.
   */
  private void initializeTranslationLanguageVersionListBox() {
    XListBox listBox = getControl(XListBox.class, D017_TRANSLATION_LANGUAGE_VERSION_LISTBOX);

    final List<String> fns = getQuranTxtFiles();
    int k = 0;
    for (final String fn : fns) {
      final String[] parts = fn.split("[.]");
      if (!parts[1].equals(ARABIC)) {
        listBox.addItem(parts[1] + " (" + parts[2].replace("_", " ") + ")", (short) k++);
      }
    }
    if (listBox.getItemCount() > 0) {
      listBox.selectItemPos((short) 0, true);

      selectedTranslationLanguage = getItemLanguague(listBox.getSelectedItem());
      selectedTranslationLanguageVersion = getItemVersion(listBox.getSelectedItem());
    }
  }

  private void initializeTransliterationLanguageCheckBox() { /* Not Yet Implemented */
  }

  private void initializeTransliterationLanguageVersionListBox() { /* Not Yet Implemented */
  }

  /**
   * Initialize the ProgressBar.
   */
  private void initializeWriteSurahProgressBar() {
    final XProgressBar progressBar = getControl(XProgressBar.class, D035_PROGRESS_BAR);

    progressBar.setRange(0, 100);
    progressBar.setValue(0);
  }

  private void insertButton(String id, int x, int y, int width, int height, boolean enable)
      throws
      Exception {
    Object buttomModel = createInstance("UnoControlButtonModel");
    XPropertySet ps = getPropertSet(buttomModel);
    ps.setPropertyValue(PROP_NAME, id);
    ps.setPropertyValue(PROP_POSITION_X, x);
    ps.setPropertyValue(PROP_POSITION_Y, y);
    ps.setPropertyValue(PROP_WIDTH, width);
    ps.setPropertyValue(PROP_HEIGHT, height);
    ps.setPropertyValue(PROP_LABEL, resourceBundle.getString(id));
    ps.setPropertyValue(PROP_ALIGN, (short) 1);
    ps.setPropertyValue(PROP_VERTICAL_ALIGN, VerticalAlignment.MIDDLE);
    ps.setPropertyValue(PROP_ENABLED, enable);
    nameContainer.insertByName(id, buttomModel);
  }

  private void insertCheckBox(
      String id, int x, int y, int width, int height, short state, boolean enable)
      throws
      Exception {
    Object checkBoxModel = createInstance("UnoControlCheckBoxModel");
    XPropertySet ps = getPropertSet(checkBoxModel);
    ps.setPropertyValue(PROP_NAME, id);
    ps.setPropertyValue(PROP_POSITION_X, x);
    ps.setPropertyValue(PROP_POSITION_Y, y);
    ps.setPropertyValue(PROP_WIDTH, width);
    ps.setPropertyValue(PROP_HEIGHT, height);
    ps.setPropertyValue(PROP_TRI_STATE, false);
    ps.setPropertyValue(PROP_STATE, state);
    ps.setPropertyValue(PROP_VERTICAL_ALIGN, VerticalAlignment.MIDDLE);
    ps.setPropertyValue(PROP_ENABLED, enable);
    nameContainer.insertByName(id, checkBoxModel);
  }

  private void insertGroupBox(String id, int x, int y, int width, int height, boolean enable)
      throws
      Exception {
    Object groupBoxModel = createInstance("UnoControlGroupBoxModel");
    XPropertySet ps = getPropertSet(groupBoxModel);
    ps.setPropertyValue(PROP_NAME, id);
    ps.setPropertyValue(PROP_POSITION_X, x);
    ps.setPropertyValue(PROP_POSITION_Y, y);
    ps.setPropertyValue(PROP_WIDTH, width);
    ps.setPropertyValue(PROP_HEIGHT, height);
    ps.setPropertyValue(PROP_LABEL, resourceBundle.getString(id));
    ps.setPropertyValue(PROP_ENABLED, enable);
    ps.setPropertyValue(PROP_FONT_WEIGHT, FontWeight.BOLD);
    nameContainer.insertByName(id, groupBoxModel);
  }

  private void insertLabel(String id, int x, int y, int width, int height, boolean enable)
      throws
      Exception {
    Object fixedTextModel = createInstance("UnoControlFixedTextModel");
    XPropertySet ps = getPropertSet(fixedTextModel);
    ps.setPropertyValue(PROP_NAME, id);
    ps.setPropertyValue(PROP_POSITION_X, x);
    ps.setPropertyValue(PROP_POSITION_Y, y);
    ps.setPropertyValue(PROP_WIDTH, width);
    ps.setPropertyValue(PROP_HEIGHT, height);
    ps.setPropertyValue(PROP_LABEL, resourceBundle.getString(id));
    ps.setPropertyValue(PROP_ALIGN, (short) 2);
    ps.setPropertyValue(PROP_VERTICAL_ALIGN, VerticalAlignment.MIDDLE);
    ps.setPropertyValue(PROP_ENABLED, enable);
    nameContainer.insertByName(id, fixedTextModel);
  }

  private void insertListBox(String id, int x, int y, int width, int height, boolean enable)
      throws
      Exception {
    Object listBoxModel = createInstance("UnoControlListBoxModel");
    XPropertySet ps = getPropertSet(listBoxModel);
    ps.setPropertyValue(PROP_NAME, id);
    ps.setPropertyValue(PROP_POSITION_X, x);
    ps.setPropertyValue(PROP_POSITION_Y, y);
    ps.setPropertyValue(PROP_WIDTH, width);
    ps.setPropertyValue(PROP_HEIGHT, height);
    ps.setPropertyValue(PROP_DROPDOWN, true);
    ps.setPropertyValue(PROP_ENABLED, enable);
    nameContainer.insertByName(id, listBoxModel);
  }

  private void insertNumericField(String id, int x, int y, int width, int height, boolean enable)
      throws
      Exception {
    Object numericFieldModel = createInstance("UnoControlNumericFieldModel");
    XPropertySet ps = getPropertSet(numericFieldModel);
    ps.setPropertyValue(PROP_NAME, id);
    ps.setPropertyValue(PROP_POSITION_X, x);
    ps.setPropertyValue(PROP_POSITION_Y, y);
    ps.setPropertyValue(PROP_WIDTH, width);
    ps.setPropertyValue(PROP_HEIGHT, height);
    ps.setPropertyValue(PROP_ALIGN, (short) 1);
    ps.setPropertyValue(PROP_SPIN, true);
    ps.setPropertyValue(PROP_DECIMAL_ACCURACY, (short) 0);
    ps.setPropertyValue(PROP_VERTICAL_ALIGN, VerticalAlignment.MIDDLE);
    ps.setPropertyValue(PROP_ENABLED, enable);
    nameContainer.insertByName(id, numericFieldModel);
  }

  private void insertProgressBar(String id, int x, int y, int width, int height, boolean enable)
      throws
      Exception {
    Object progressBarModel = createInstance("UnoControlProgressBarModel");
    XPropertySet ps = getPropertSet(progressBarModel);
    ps.setPropertyValue(PROP_NAME, id);
    ps.setPropertyValue(PROP_POSITION_X, x);
    ps.setPropertyValue(PROP_POSITION_Y, y);
    ps.setPropertyValue(PROP_WIDTH, width);
    ps.setPropertyValue(PROP_HEIGHT, height);
    ps.setPropertyValue("ProgressValueMin", 0);
    ps.setPropertyValue("ProgressValueMax", 100);
    ps.setPropertyValue("ProgressValue", 50);
    ps.setPropertyValue(PROP_ENABLED, enable);
    nameContainer.insertByName(id, progressBarModel);
  }

  /**
   * Show.
   */
  public void show() {
    initializeInsertQuranDialog();
    this.dialog.execute();
  }

  private void writeParagraph(
      final XText text,
      final XParagraphCursor paragraphCursor,
      final String paragraph,
      final String language,
      final String fontName,
      final double fontSize)
      throws
      UnknownPropertyException,
      PropertyVetoException,
      WrappedTargetException {

    paragraphCursor.gotoEndOfParagraph(false);
    text.insertControlCharacter(paragraphCursor, ControlCharacter.PARAGRAPH_BREAK, false);

    final XPropertySet paragraphCursorPropertySet = DocumentHandler.getPropertySet(paragraphCursor);

    if (Localization.getLanguageWritingMode(language) == com.sun.star.text.WritingMode2.LR_TB) {
      paragraphCursorPropertySet.setPropertyValue("ParaAdjust",
          com.sun.star.style.ParagraphAdjust.LEFT);
      paragraphCursorPropertySet.setPropertyValue("WritingMode",
          com.sun.star.text.WritingMode2.LR_TB);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_FONT_NAME, fontName);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_HEIGHT, fontSize);
    } else {
      paragraphCursorPropertySet.setPropertyValue("ParaAdjust",
          com.sun.star.style.ParagraphAdjust.RIGHT);
      paragraphCursorPropertySet.setPropertyValue("WritingMode",
          com.sun.star.text.WritingMode2.RL_TB);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_FONT_NAME_COMPLEX, fontName);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_HEIGHT_COMPLEX, fontSize);
    }
    text.insertString(paragraphCursor, paragraph, false);
  }

  /**
   * Write surah.
   *
   * @param surahNumber the surah number
   */
  public void writeSurah(final int surahNumber) {
    final XTextDocument textDoc = DocumentHandler.getCurrentDocument(this.componentContext);
    final XController controller = textDoc.getCurrentController();
    final XTextViewCursorSupplier textViewCursorSupplier = DocumentHandler.getCursorSupplier(
        controller);
    final XTextViewCursor textViewCursor = textViewCursorSupplier.getViewCursor();
    final XText text = textViewCursor.getText();
    final XTextCursor textCursor = text.createTextCursorByRange(textViewCursor.getStart());
    final XParagraphCursor paragraphCursor = UnoRuntime.queryInterface(XParagraphCursor.class,
        textCursor);
    final XPropertySet paragraphCursorPropertySet = DocumentHandler.getPropertySet(paragraphCursor);

    try {
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_FONT_NAME, selectedLatinFontName);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_FONT_NAME_COMPLEX,
          selectedArabicFontName);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_HEIGHT, selectedLatinFontSize);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_HEIGHT_COMPLEX, selectedArabicFontSize);

      final long from = (selectedAyatAllInd) ? 1 : selectedAyatFrom;
      final long to =
          (selectedAyatAllInd) ? QuranReader.getSurahSize(surahNumber) + 1 : selectedAyatTo + 1;

      if (selectedLineByLineInd) {
        writeSurahLineByLine(surahNumber, text, paragraphCursor, from, to);
      } else {
        writeSurahAsOneBlock(surahNumber, text, paragraphCursor, from, to);
      }

      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_FONT_NAME, selectedLatinFontName);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_FONT_NAME_COMPLEX,
          selectedArabicFontName);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_HEIGHT, selectedLatinFontSize);
      paragraphCursorPropertySet.setPropertyValue(PROP_CHAR_HEIGHT_COMPLEX, selectedArabicFontSize);

    } catch (com.sun.star.lang.IllegalArgumentException | UnknownPropertyException |
             PropertyVetoException | WrappedTargetException e) {
      e.printStackTrace();
    }
  }


  /**
   * Write the Text block.
   *
   * @param surahNumber              the surah number
   * @param text                     the text
   * @param paragraphCursor          the paragraph
   * @param from                     start ayat
   * @param to                       last ayat
   * @param language                 the language of the text
   * @param fontName                 the font name
   * @param fontSize                 the font size
   * @param languageVersion          the text version for the language
   * @param dlgWriteSurahProgressBar the progressBar
   */
  private void writeSurahTextBlock(
      final int surahNumber,
      final XText text,
      final XParagraphCursor paragraphCursor,
      final long from,
      final long to,
      final String language,
      final String languageVersion,
      final String fontName,
      final double fontSize,
      final XProgressBar dlgWriteSurahProgressBar) {
    try {
      final StringBuilder lb = new StringBuilder();
      for (long l = from; l < to; l++) {
        if ((l == 1) && (surahNumber != 1 && surahNumber != 9)) {
          lb.append(getBismillah(language, languageVersion));
          lb.append("\n");
        }
        dlgWriteSurahProgressBar.setValue((int) (100 * l / (to - from + 1)));
        lb.append(getAyahLine(surahNumber, l, language, languageVersion, fontName));
        lb.append(" ");
      }
      writeParagraph(text, paragraphCursor, lb + "\n", language, fontName, fontSize);
    } catch (com.sun.star.lang.IllegalArgumentException
             | UnknownPropertyException
             | PropertyVetoException
             | WrappedTargetException e) {
      e.printStackTrace();
    }
  }

  /**
   * Write the selected Surah as one block.
   *
   * @param surahNumber     the surah number
   * @param text            the text
   * @param paragraphCursor the paragraph
   * @param from            start ayat
   * @param to              last ayat
   */
  private void writeSurahAsOneBlock(final int surahNumber,
      final XText text,
      final XParagraphCursor paragraphCursor,
      final long from,
      final long to) {
    final XProgressBar progressBar = getControl(XProgressBar.class, D035_PROGRESS_BAR);

    if (selectedArabicInd) {
      writeSurahTextBlock(surahNumber,
          text,
          paragraphCursor,
          from,
          to,
          selectedArabicLanguage,
          selectedArabicLanguageVersion,
          selectedArabicFontName,
          selectedArabicFontSize,
          progressBar);
    }
    if (selectedTranslationInd) {
      writeSurahTextBlock(surahNumber,
          text,
          paragraphCursor,
          from,
          to,
          selectedTranslationLanguage,
          selectedTranslationLanguageVersion,
          getTranslationFontName(selectedTranslationLanguage),
          getTranslationFontSize(selectedTranslationLanguage),
          progressBar);
    }
    if (selectedTransliterationInd) {
      writeSurahTextBlock(surahNumber,
          text,
          paragraphCursor,
          from,
          to,
          selectedTransliterationLanguage,
          selectedTransliterationLanguageVersion,
          selectedLatinFontName,
          selectedArabicFontSize,
          progressBar);
    }
  }

  /**
   * Write the selected Surah Line By Line.
   *
   * @param surahNumber     the surah number
   * @param text            the text
   * @param paragraphCursor the paragraph of the text
   * @param from            start ayat
   * @param to              last ayat
   */
  private void writeSurahLineByLine(final int surahNumber,
      final XText text,
      final XParagraphCursor paragraphCursor,
      final long from,
      final long to) {
    try {

      final XProgressBar progressBar = getControl(XProgressBar.class, D035_PROGRESS_BAR);
      if ((from == 1) && (surahNumber != 1 && surahNumber != 9)) {
        writeBismillahSurahLineByLIne(text, paragraphCursor);
      }

      for (long l = from; l < to; l++) {
        progressBar.setValue((int) (100 * l / (to - from + 1)));
        if (selectedArabicInd) {
          writeParagraph(text,
              paragraphCursor,
              getAyahLine(surahNumber,
                  l,
                  selectedArabicLanguage,
                  selectedArabicLanguageVersion,
                  selectedArabicFontName),
              selectedArabicLanguage,
              selectedArabicFontName,
              selectedArabicFontSize);
        }
        if (selectedTranslationInd) {
          writeParagraph(text,
              paragraphCursor,
              getAyahLine(surahNumber,
                  l,
                  selectedTranslationLanguage,
                  selectedTranslationLanguageVersion,
                  selectedLatinFontName),
              selectedTranslationLanguage,
              getTranslationFontName(selectedTranslationLanguage),
              getTranslationFontSize(selectedTranslationLanguage));
        }
        if (selectedTransliterationInd) {
          writeParagraph(text,
              paragraphCursor,
              getAyahLine(surahNumber,
                  l,
                  selectedTransliterationLanguage,
                  selectedTransliterationLanguageVersion,
                  selectedLatinFontName),
              selectedTransliterationLanguage,
              selectedLatinFontName,
              selectedLatinFontSize);
        }
      }
    } catch (com.sun.star.lang.IllegalArgumentException | UnknownPropertyException |
             PropertyVetoException | WrappedTargetException e) {
      e.printStackTrace();
    }
  }

  /**
   * Write Bismillah lines.
   *
   * @param text            the text
   * @param paragraphCursor the paragraph
   */
  private void writeBismillahSurahLineByLIne(final XText text,
      final XParagraphCursor paragraphCursor) {
    try {
      if (selectedArabicInd) {
        writeParagraph(text,
            paragraphCursor,
            getBismillah(selectedArabicLanguage, selectedArabicLanguageVersion),
            selectedArabicLanguage,
            selectedArabicFontName,
            selectedArabicFontSize);
      }
      if (selectedTranslationInd) {
        writeParagraph(text,
            paragraphCursor,
            getBismillah(selectedTranslationLanguage, selectedTranslationLanguageVersion),
            selectedTranslationLanguage,
            getTranslationFontName(selectedTranslationLanguage),
            getTranslationFontSize(selectedTranslationLanguage));
      }
      if (selectedTransliterationInd) {
        writeParagraph(text,
            paragraphCursor,
            getBismillah(selectedTransliterationLanguage,
                selectedTransliterationLanguageVersion),
            selectedTranslationLanguage,
            selectedLatinFontName,
            selectedLatinFontSize);
      }
    } catch (com.sun.star.lang.IllegalArgumentException
             | UnknownPropertyException
             | PropertyVetoException
             | WrappedTargetException e) {
      e.printStackTrace();
    }
  }

  private String getTranslationFontName(String language) {

    if (getLanguageFontType(language).equals("Latin")) {
      return selectedLatinFontName;
    } else {
      return selectedArabicFontName;
    }
  }

  private double getTranslationFontSize(String language) {
    if (getLanguageFontType(language).equals("Latin")) {
      return selectedLatinFontSize;
    } else {
      return selectedArabicFontSize;
    }
  }
}
