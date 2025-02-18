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
 * along with SC Kill Monitor. If not, see <https://www.gnu.org/licenses/>.                       *
 **************************************************************************************************/

package de.greluc.sc.sckillmonitor.settings;

import lombok.extern.log4j.Log4j2;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Handles the persistence and retrieval of user-specific settings for the application.
 * Provides methods to save the current settings to a persistent storage and load them
 * back when needed.
 * <p>
 * This class utilizes the `java.util.prefs.Preferences` API to save and retrieve application
 * settings. The preferences are associated with the current user and are stored in a
 * persistent storage backend provided by the JVM.
 * <p>
 * The settings managed by this class include various file paths, user handle, and scan interval.
 * These settings are initially retrieved from the `SettingsData` class and can also be
 * updated in `SettingsData` when loaded from persistent storage.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @since 1.0.0
 * @version 1.0.0
 */
@Log4j2
public class SettingsHandler {
  private final Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

  /**
   * Saves the current settings to persistent storage using the Preferences API.
   * The settings are retrieved from the {@code SettingsData} class and written
   * to the preferences node associated with the current user.
   * <p>
   * The following settings are saved:<br>
   * - Paths for different application configurations.<br>
   * - The user handle.<br>
   * - Scan interval in seconds.
   * <p>
   * After saving the settings to the Preferences API, an attempt is made to
   * persist the preferences to the underlying storage.
   * <p>
   * If an error occurs during the persistence operation, a log entry will
   * be created with the corresponding error message and stack trace.
   *
   * @throws BackingStoreException if an error occurs while flushing the preferences
   *         to the persistent store. This exception is logged and not propagated further.
   */
  public void saveSettings() {
    preferences.put("PATH_LIVE", SettingsData.getPathLive());
    preferences.put("PATH_PTU", SettingsData.getPathPtu());
    preferences.put("PATH_EPTU", SettingsData.getPathEptu());
    preferences.put("PATH_HOTFIX", SettingsData.getPathHotfix());
    preferences.put("PATH_TECH_PREVIEW", SettingsData.getPathTechPreview());
    preferences.put("PATH_CUSTOM", SettingsData.getPathCustom());
    preferences.put("PLAYER_HANDLE", SettingsData.getHandle());
    preferences.putInt("SCAN_INTERVAL_SECONDS", SettingsData.getInterval());
    try {
      preferences.flush();
    } catch (BackingStoreException exception) {
      log.error("Couldn't persist the preferences to the persistent store!", exception);
    }
  }

  /**
   * Loads and applies user preferences stored in a persistent storage.
   * If the preferences cannot be synchronized due to a BackingStoreException,
   * default values will be used for all settings.
   * <p>
   * This method retrieves specific configuration values for the application's
   * file paths, user handle, and scanning interval from the preferences store.
   * The loaded settings are applied to the static fields of the SettingsData
   * class using its setter methods.
   * <p>
   * Preferences retrieved:<br>
   * - PATH_LIVE: File path to the game log for the LIVE version.<br>
   * - PATH_PTU: File path to the game log for the PTU version.<br>
   * - PATH_EPTU: File path to the game log for the EPTU version.<br>
   * - PATH_HOTFIX: File path to the game log for the HOTFIX version.<br>
   * - PATH_TECH_PREVIEW: File path to the game log for the TECH PREVIEW version.<br>
   * - PATH_CUSTOM: File path to a user-specified custom location.<br>
   * - PLAYER_HANDLE: Player's handle or username.<br>
   * - SCAN_INTERVAL_SECONDS: Time interval, in seconds, used for scanning.
   * <p>
   * If any of the retrieved preference values are not found, default values
   * are used:<br>
   * - File paths default to typical installation directories.<br>
   * - PLAYER_HANDLE defaults to an empty string.<br>
   * - SCAN_INTERVAL_SECONDS defaults to 1 second.
   * <p>
   * Logs an error message if the preferences cannot be loaded and defaults
   * are applied.
   */
  public void loadSettings() {
    try {
      preferences.sync();
    } catch (BackingStoreException e) {
      log.error("Couldn't load the preferences from the persistent store! Using defaults.", e);
    }
    SettingsData.setPathLive(preferences.get("PATH_LIVE", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\LIVE\\game.log"));
    SettingsData.setPathPtu(preferences.get("PATH_EPTU", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\EPTU\\game.log"));
    SettingsData.setPathEptu(preferences.get("PATH_EPTU", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\EPTU\\game.log"));
    SettingsData.setPathHotfix(preferences.get("PATH_HOTFIX", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\HOTFIX\\game.log"));
    SettingsData.setPathTechPreview(preferences.get("PATH_TECH_PREVIEW", "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\TECH-PREVIEW\\game.log"));
    SettingsData.setPathCustom(preferences.get("PATH_CUSTOM", ""));
    SettingsData.setHandle(preferences.get("PLAYER_HANDLE", ""));
    SettingsData.setInterval(Integer.parseInt(preferences.get("SCAN_INTERVAL_SECONDS", "1")));
  }
}
