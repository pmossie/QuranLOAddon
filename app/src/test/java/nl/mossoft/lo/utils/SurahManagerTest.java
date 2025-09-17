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

package nl.mossoft.lo.utils;

import static nl.mossoft.lo.quran.SurahManager.getSurahSize;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class SurahManagerTest {

  @Test
  void getSurahSize_isCorrect() {
    assertThat(getSurahSize(1)).isEqualTo(7);
    assertThat(getSurahSize(2)).isEqualTo(286);
    assertThat(getSurahSize(3)).isEqualTo(200);
    assertThat(getSurahSize(4)).isEqualTo(176);
    assertThat(getSurahSize(5)).isEqualTo(120);
    assertThat(getSurahSize(6)).isEqualTo(165);
    assertThat(getSurahSize(7)).isEqualTo(206);
    assertThat(getSurahSize(8)).isEqualTo(75);
    assertThat(getSurahSize(9)).isEqualTo(129);
    assertThat(getSurahSize(10)).isEqualTo(109);
    assertThat(getSurahSize(11)).isEqualTo(123);
    assertThat(getSurahSize(12)).isEqualTo(111);
    assertThat(getSurahSize(13)).isEqualTo(43);
    assertThat(getSurahSize(14)).isEqualTo(52);
    assertThat(getSurahSize(15)).isEqualTo(99);
    assertThat(getSurahSize(16)).isEqualTo(128);
    assertThat(getSurahSize(17)).isEqualTo(111);
    assertThat(getSurahSize(18)).isEqualTo(110);
    assertThat(getSurahSize(19)).isEqualTo(98);
    assertThat(getSurahSize(20)).isEqualTo(135);
    assertThat(getSurahSize(21)).isEqualTo(112);
    assertThat(getSurahSize(22)).isEqualTo(78);
    assertThat(getSurahSize(23)).isEqualTo(118);
    assertThat(getSurahSize(24)).isEqualTo(64);
    assertThat(getSurahSize(25)).isEqualTo(77);
    assertThat(getSurahSize(26)).isEqualTo(227);
    assertThat(getSurahSize(27)).isEqualTo(93);
    assertThat(getSurahSize(28)).isEqualTo(88);
    assertThat(getSurahSize(29)).isEqualTo(69);
    assertThat(getSurahSize(30)).isEqualTo(60);
    assertThat(getSurahSize(31)).isEqualTo(34);
    assertThat(getSurahSize(32)).isEqualTo(30);
    assertThat(getSurahSize(33)).isEqualTo(73);
    assertThat(getSurahSize(34)).isEqualTo(54);
    assertThat(getSurahSize(35)).isEqualTo(45);
    assertThat(getSurahSize(36)).isEqualTo(83);
    assertThat(getSurahSize(37)).isEqualTo(182);
    assertThat(getSurahSize(38)).isEqualTo(88);
    assertThat(getSurahSize(39)).isEqualTo(75);
    assertThat(getSurahSize(40)).isEqualTo(85);
    assertThat(getSurahSize(41)).isEqualTo(54);
    assertThat(getSurahSize(42)).isEqualTo(53);
    assertThat(getSurahSize(43)).isEqualTo(89);
    assertThat(getSurahSize(44)).isEqualTo(59);
    assertThat(getSurahSize(45)).isEqualTo(37);
    assertThat(getSurahSize(46)).isEqualTo(35);
    assertThat(getSurahSize(47)).isEqualTo(38);
    assertThat(getSurahSize(48)).isEqualTo(29);
    assertThat(getSurahSize(49)).isEqualTo(18);
    assertThat(getSurahSize(50)).isEqualTo(45);
    assertThat(getSurahSize(51)).isEqualTo(60);
    assertThat(getSurahSize(52)).isEqualTo(49);
    assertThat(getSurahSize(53)).isEqualTo(62);
    assertThat(getSurahSize(54)).isEqualTo(55);
    assertThat(getSurahSize(55)).isEqualTo(78);
    assertThat(getSurahSize(56)).isEqualTo(96);
    assertThat(getSurahSize(57)).isEqualTo(29);
    assertThat(getSurahSize(58)).isEqualTo(22);
    assertThat(getSurahSize(59)).isEqualTo(24);
    assertThat(getSurahSize(60)).isEqualTo(13);
    assertThat(getSurahSize(61)).isEqualTo(14);
    assertThat(getSurahSize(62)).isEqualTo(11);
    assertThat(getSurahSize(63)).isEqualTo(11);
    assertThat(getSurahSize(64)).isEqualTo(18);
    assertThat(getSurahSize(65)).isEqualTo(12);
    assertThat(getSurahSize(66)).isEqualTo(12);
    assertThat(getSurahSize(67)).isEqualTo(30);
    assertThat(getSurahSize(68)).isEqualTo(52);
    assertThat(getSurahSize(69)).isEqualTo(52);
    assertThat(getSurahSize(70)).isEqualTo(44);
    assertThat(getSurahSize(71)).isEqualTo(28);
    assertThat(getSurahSize(72)).isEqualTo(28);
    assertThat(getSurahSize(73)).isEqualTo(20);
    assertThat(getSurahSize(74)).isEqualTo(56);
    assertThat(getSurahSize(75)).isEqualTo(40);
    assertThat(getSurahSize(76)).isEqualTo(31);
    assertThat(getSurahSize(77)).isEqualTo(50);
    assertThat(getSurahSize(78)).isEqualTo(40);
    assertThat(getSurahSize(79)).isEqualTo(46);
    assertThat(getSurahSize(80)).isEqualTo(42);
    assertThat(getSurahSize(81)).isEqualTo(29);
    assertThat(getSurahSize(82)).isEqualTo(19);
    assertThat(getSurahSize(83)).isEqualTo(36);
    assertThat(getSurahSize(84)).isEqualTo(25);
    assertThat(getSurahSize(85)).isEqualTo(22);
    assertThat(getSurahSize(86)).isEqualTo(17);
    assertThat(getSurahSize(87)).isEqualTo(19);
    assertThat(getSurahSize(88)).isEqualTo(26);
    assertThat(getSurahSize(89)).isEqualTo(30);
    assertThat(getSurahSize(90)).isEqualTo(20);
    assertThat(getSurahSize(91)).isEqualTo(15);
    assertThat(getSurahSize(92)).isEqualTo(21);
    assertThat(getSurahSize(93)).isEqualTo(11);
    assertThat(getSurahSize(94)).isEqualTo(8);
    assertThat(getSurahSize(95)).isEqualTo(8);
    assertThat(getSurahSize(96)).isEqualTo(19);
    assertThat(getSurahSize(97)).isEqualTo(5);
    assertThat(getSurahSize(98)).isEqualTo(8);
    assertThat(getSurahSize(99)).isEqualTo(8);
    assertThat(getSurahSize(100)).isEqualTo(11);
    assertThat(getSurahSize(101)).isEqualTo(11);
    assertThat(getSurahSize(102)).isEqualTo(8);
    assertThat(getSurahSize(103)).isEqualTo(3);
    assertThat(getSurahSize(104)).isEqualTo(9);
    assertThat(getSurahSize(105)).isEqualTo(5);
    assertThat(getSurahSize(106)).isEqualTo(4);
    assertThat(getSurahSize(107)).isEqualTo(7);
    assertThat(getSurahSize(108)).isEqualTo(3);
    assertThat(getSurahSize(109)).isEqualTo(6);
    assertThat(getSurahSize(110)).isEqualTo(3);
    assertThat(getSurahSize(111)).isEqualTo(5);
    assertThat(getSurahSize(112)).isEqualTo(4);
    assertThat(getSurahSize(113)).isEqualTo(5);
    assertThat(getSurahSize(114)).isEqualTo(6);
  }
}
