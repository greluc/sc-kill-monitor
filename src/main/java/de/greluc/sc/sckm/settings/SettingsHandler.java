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
 * along with SC Kill Monitor. If not, see https://www.gnu.org/licenses/                          *
 **************************************************************************************************/

package de.greluc.sc.sckm.settings;

import static de.greluc.sc.sckm.Constants.SETTINGS_PATH_CUSTOM;
import static de.greluc.sc.sckm.Constants.SETTINGS_PATH_EPTU;
import static de.greluc.sc.sckm.Constants.SETTINGS_PATH_HOTFIX;
import static de.greluc.sc.sckm.Constants.SETTINGS_PATH_LIVE;
import static de.greluc.sc.sckm.Constants.SETTINGS_PATH_PTU;
import static de.greluc.sc.sckm.Constants.SETTINGS_PATH_TECH_PREVIEW;
import static de.greluc.sc.sckm.Constants.SETTINGS_PLAYER_HANDLE;
import static de.greluc.sc.sckm.Constants.SETTINGS_SCAN_INTERVAL_SECONDS;
import static de.greluc.sc.sckm.Constants.SETTINGS_SHOW_ALL;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import lombok.extern.log4j.Log4j2;

/**
 * Handles the persistence and retrieval of user-specific settings for the application. Provides
 * methods to save the current settings to a persistent storage and load them back when needed.
 *
 * <p>This class utilizes the {@link java.util.prefs.Preferences} API to save and retrieve
 * application settings. The preferences are associated with the current user and are stored in a
 * persistent storage backend provided by the JVM.
 *
 * <p>The settings managed by this class include various file paths, user handle, and scan interval.
 * These settings are initially retrieved from the {@link SettingsData} class and can also be
 * updated in {@link SettingsData} when loaded from persistent storage.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @version 1.2.1
 * @since 1.0.0
 */
@Log4j2
public class SettingsHandler {
  private final Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

  /**
   * Saves the current settings to persistent storage using the Preferences API. The settings are
   * retrieved from the {@link SettingsData} class and written to the preferences node associated
   * with the current user.
   *
   * <p>The following settings are saved:
   *
   * <ul>
   *   <li>Paths for different application configurations.
   *   <li>The user handle.
   *   <li>Scan interval in seconds.
   * </ul>
   *
   * <p>After saving the settings to the Preferences API, an attempt is made to persist the
   * preferences to the underlying storage.
   *
   * <p>If an error occurs during the persistence operation, a log entry will be created with the
   * corresponding error message and stack trace.
   */
  public void saveSettings() {
    preferences.put(SETTINGS_PATH_LIVE, SettingsData.getPathLive());
    preferences.put(SETTINGS_PATH_PTU, SettingsData.getPathPtu());
    preferences.put(SETTINGS_PATH_EPTU, SettingsData.getPathEptu());
    preferences.put(SETTINGS_PATH_HOTFIX, SettingsData.getPathHotfix());
    preferences.put(SETTINGS_PATH_TECH_PREVIEW, SettingsData.getPathTechPreview());
    preferences.put(SETTINGS_PATH_CUSTOM, SettingsData.getPathCustom());
    preferences.put(SETTINGS_PLAYER_HANDLE, SettingsData.getHandle());
    preferences.putInt(SETTINGS_SCAN_INTERVAL_SECONDS, SettingsData.getInterval());
    preferences.putBoolean(SETTINGS_SHOW_ALL, SettingsData.isShowAll());
    try {
      preferences.flush();
    } catch (BackingStoreException exception) {
      log.error("Couldn't persist the preferences to the persistent store!", exception);
    }
  }

  /**
   * Loads and applies user preferences stored in a persistent storage. If the preferences cannot be
   * synchronized due to a BackingStoreException, default values will be used for all settings.
   *
   * <p>This method retrieves specific configuration values for the application's file paths, user
   * handle, and scanning interval from the preferences store. The loaded settings are applied to
   * the static fields of the SettingsData class using its setter methods.
   *
   * <p>Preferences retrieved:
   *
   * <ul>
   *   <li>PATH_LIVE: File path to the game log for the LIVE version.
   *   <li>PATH_PTU: File path to the game log for the PTU version.
   *   <li>PATH_EPTU: File path to the game log for the EPTU version.
   *   <li>PATH_HOTFIX: File path to the game log for the HOTFIX version.
   *   <li>PATH_TECH_PREVIEW: File path to the game log for the TECH PREVIEW version.
   *   <li>PATH_CUSTOM: File path to a user-specified custom location.
   *   <li>PLAYER_HANDLE: Player's handle or username.
   *   <li>SCAN_INTERVAL_SECONDS: Time interval, in seconds, used for scanning.
   * </ul>
   *
   * <p>If any of the retrieved preference values are not found, default values are used:
   *
   * <ul>
   *   <li>File paths default to typical installation directories.
   *   <li>PLAYER_HANDLE defaults to an empty string.
   *   <li>SCAN_INTERVAL_SECONDS defaults to 1 second.
   * </ul>
   *
   * <p>Logs an error message if the preferences cannot be loaded and defaults are applied.
   */
  public void loadSettings() {
    try {
      preferences.sync();
    } catch (BackingStoreException e) {
      log.error("Couldn't load the preferences from the persistent store! Using defaults.", e);
    }
    SettingsData.setPathLive(
        preferences.get(
            SETTINGS_PATH_LIVE,
            "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\LIVE\\game.log"));
    SettingsData.setPathPtu(
        preferences.get(
            SETTINGS_PATH_PTU,
            "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\EPTU\\game.log"));
    SettingsData.setPathEptu(
        preferences.get(
            SETTINGS_PATH_EPTU,
            "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\EPTU\\game.log"));
    SettingsData.setPathHotfix(
        preferences.get(
            SETTINGS_PATH_HOTFIX,
            "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\HOTFIX\\game.log"));
    SettingsData.setPathTechPreview(
        preferences.get(
            SETTINGS_PATH_TECH_PREVIEW,
            "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\TECH-PREVIEW\\game.log"));
    SettingsData.setPathCustom(preferences.get(SETTINGS_PATH_CUSTOM, ""));
    SettingsData.setHandle(preferences.get(SETTINGS_PLAYER_HANDLE, ""));
    SettingsData.setInterval(
        Integer.parseInt(preferences.get(SETTINGS_SCAN_INTERVAL_SECONDS, "60")));
    SettingsData.setShowAll(preferences.getBoolean(SETTINGS_SHOW_ALL, false));
  }
}
