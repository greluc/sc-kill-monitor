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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @since 1.0.0
 * @version 1.0.0
 */
public class SettingsHandler {
  private final Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

  public void saveSettings() {
    preferences.put("PATH_LIVE", SettingsData.getPathLive());
    preferences.put("PATH_PTU", SettingsData.getPathPtu());
    preferences.put("PATH_EPTU", SettingsData.getPathEptu());
    preferences.put("PATH_HOTFIX", SettingsData.getPathHotfix());
    preferences.put("PATH_TECH_PREVIEW", SettingsData.getPathTechPreview());
    preferences.put("PLAYER_HANDLE", SettingsData.getHandle());
    preferences.putInt("SCAN_INTERVAL_SECONDS", SettingsData.getInterval());
    try {
      preferences.flush();
    } catch (BackingStoreException exception) {
      System.err.println("Couldn't persist the preferences to the persistent store!"); // TODO real logging and error message
    }
  }

  public void loadSettings() {
    try {
      preferences.sync();
    } catch (BackingStoreException e) {
      System.err.println("Couldn't load settings. Using defaults."); // TODO real logging and error message
    }
    SettingsData.setPathLive(preferences.get("PATH_LIVE", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\LIVE\\game.log"));
    SettingsData.setPathPtu(preferences.get("PATH_EPTU", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\EPTU\\game.log"));
    SettingsData.setPathEptu(preferences.get("PATH_EPTU", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\EPTU\\game.log"));
    SettingsData.setPathHotfix(preferences.get("PATH_HOTFIX", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\HOTFIX\\game.log"));
    SettingsData.setPathTechPreview(preferences.get("PATH_TECH_PREVIEW", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\TECH-PREVIEW\\game.log"));
    SettingsData.setHandle(preferences.get("PLAYER_HANDLE", ""));
    SettingsData.setInterval(Integer.parseInt(preferences.get("SCAN_INTERVAL_SECONDS", "1")));
  }
}
