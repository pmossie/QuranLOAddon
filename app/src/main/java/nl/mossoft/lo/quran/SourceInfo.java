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

/**
 * Contains metadata about the source type, language, version, and file location.
 *
 * @param type the type of source (original, translation, or transliteration)
 * @param language the language of the Quran version
 * @param version the specific version or translator name
 * @param fileName the file path to the Quran XML source
 */
public record SourceInfo(
    SourceType type, SourceLanguage language, String version, String fileName) {}
