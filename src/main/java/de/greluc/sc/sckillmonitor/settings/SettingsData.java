/**************************************************************************************************
 * SC Kill Monitor                                                                                *
 * Copyright (C) 2025-2025 SC Kill Monitor Team                                                   *
 *                                                                                                *
 * This file is part of SC Kill Monitor.                                                          *
 *                                                                                                *
 * SC Kill Monitor is free software: you can redistribute it and/or modify                        *
 * it under the terms of the GNU General Public License as published by                           *
 * the Free Software Foundation, either version 3 of the License, or                              *
 * (at your option) any later version.                                                            *
 *                                                                                                *
 * SC Kill Monitor is distributed in the hope that it will be useful,                             *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                                 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                                  *
 * GNU General Public License for more details.                                                   *
 *                                                                                                *
 * You should have received a copy of the GNU General Public License                              *
 * along with SC Kill Monitor. If not, see <http://www.gnu.org/licenses/>.                        *
 **************************************************************************************************/

package de.greluc.sc.sckillmonitor.settings;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static de.greluc.sc.sckillmonitor.Constants.LIVE;

/**
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @since 1.0.0
 * @version 1.0.0
 */
public class SettingsData {
  @Getter
  private static String pathLive = "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\LIVE\\game.log";
  @Getter
  private static String pathPtu = "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\PTU\\game.log";
  @Getter
  private static String pathEptu = "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\EPTU\\game.log";
  @Getter
  private static String pathHotfix = "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\HOTFIX\\game.log";
  @Getter
  private static String pathTechPreview = "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\TECH-PREVIEW\\game.log";
  @Getter
  private static String pathCustom = "";
  @Getter
  private static String handle = "";
  @Getter
  private static int interval = 1;
  @Getter
  private static String selectedChannel = LIVE;

  private static final List<SettingsListener> listeners = new ArrayList<>();

  public static void setPathLive(String pathLive) {
    SettingsData.pathLive = pathLive;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void setPathPtu(String pathPtu) {
    SettingsData.pathPtu = pathPtu;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void setPathEptu(String pathEptu) {
    SettingsData.pathEptu = pathEptu;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void setPathHotfix(String pathHotfix) {
    SettingsData.pathHotfix = pathHotfix;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void setPathTechPreview(String pathTechPreview) {
    SettingsData.pathTechPreview = pathTechPreview;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void setPathCustom(String pathCustom) {
    SettingsData.pathCustom = pathCustom;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void setHandle(String handle) {
    SettingsData.handle = handle;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void setInterval(int interval) {
    SettingsData.interval = interval;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void setSelectedChannel(String selectedChannel) {
    SettingsData.selectedChannel = selectedChannel;
    listeners.forEach(SettingsListener::settingsChanged);
  }

  public static void addListener(SettingsListener listener) {
    listeners.add(listener);
  }

  public static void removeListener(SettingsListener listener) {
    listeners.remove(listener);
  }
}
