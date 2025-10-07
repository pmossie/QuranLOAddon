# QuranLOAddon

**QuranLOAddon** is a LibreOffice extension that integrates Quranic text directly into Writer documents. It provides seamless insertion of Arabic verses along with translations and transliterations. The goal is to make working with Quranic content efficient, accurate, and fully customizable within LibreOffice.

---

<img width="827" height="448" alt="image" src="https://github.com/user-attachments/assets/ebdcec43-76cb-435f-8291-9cdf005f4972" />

---

## Features

* Insert Arabic Quranic text directly into LibreOffice Writer
* Include translations and transliterations (from [Tanzil.net](https://tanzil.net))
* Automatic detection of Arabic-supporting fonts
* Customizable font and font size settings
* Built-in fallbacks for missing Arabic glyphs or numerals
* Compatible with LibreOffice 7.x and above

---

## Font Configuration

By default, QuranLOAddon uses the font settings defined in LibreOffice.
You can override these through the add-on’s configuration dialog.

### Arabic Font Selection

Only fonts that support Arabic script are displayed in the Arabic font dropdown.

### Recommended Arabic Fonts

* **Scheherazade** – [Download](https://software.sil.org/scheherazade/)
  Supports OpenType “smart features” for advanced typography. You can generate your own variant with custom settings.

* **KFGQPC Uthmanic Script HAFS** – [Download](http://fonts.qurancomplex.gov.sa/)
  Optimized for Uthmani script and widely used in digital Quran publications.

> ⚠️ Other Arabic fonts may produce inconsistent results.
> Some fonts lack parentheses or standard Arabic numerals.
> QuranLOAddon includes limited substitution logic to mitigate these issues.

---

## Data Sources

Quran text and translations are sourced from **[Tanzil.net](https://tanzil.net)**:

```
Tanzil Quran Text (Uthmani, version 1.0.2)
Copyright (C) 2008–2010 Tanzil.net
License: Creative Commons Attribution 3.0
```

---

## License

This project follows the same **Creative Commons Attribution 3.0** license for the included Quran text data.
See the LICENSE file for details.

---

## Contributing

Pull requests and issue reports are welcome.
If you’d like to contribute translations, font support, or UI improvements, please open an issue first to discuss your idea.

---
