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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * The SettingsDataTest class contains unit tests for verifying the behavior of the SettingsData
 * class. These tests are designed to confirm correct functionality and expected behavior when
 * modifying the selected channel and interacting with listeners.
 *
 * <p>Key functionalities tested include:
 *
 * <ul>
 *   <li>Updating the selected channel property.
 *   <li>Ensuring listeners are notified on updates.
 *   <li>Verifying that removed listeners are not notified.
 * </ul>
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @version 1.2.0
 * @since 1.0.0
 */
class SettingsDataTest {

  @Test
  void testSetSelectedChannelUpdatesValue() {
    // Arrange
    String newChannel = "PTU";

    // Act
    SettingsData.setSelectedChannel(newChannel);

    // Assert
    assertEquals(newChannel, SettingsData.getSelectedChannel());
  }

  @Test
  void testSetSelectedChannelNotifiesListeners() {
    // Arrange
    String newChannel = "TECH-PREVIEW";
    SettingsListener mockListener = mock(SettingsListener.class);
    SettingsData.addListener(mockListener);

    // Act
    SettingsData.setSelectedChannel(newChannel);

    // Assert
    verify(mockListener, times(1)).settingsChanged();

    // Cleanup
    SettingsData.removeListener(mockListener);
  }

  @Test
  void testSetSelectedChannelDoesNotNotifyRemovedListener() {
    // Arrange
    String newChannel = "HOTFIX";
    SettingsListener mockListener = mock(SettingsListener.class);
    SettingsData.addListener(mockListener);
    SettingsData.removeListener(mockListener);

    // Act
    SettingsData.setSelectedChannel(newChannel);

    // Assert
    verify(mockListener, never()).settingsChanged();
  }
}
