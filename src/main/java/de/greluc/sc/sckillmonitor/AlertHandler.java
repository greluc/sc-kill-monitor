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

package de.greluc.sc.sckillmonitor;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lombok.Generated;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class AlertHandler {

  /**
   * Shows a general error that doesn't specify a specific error in its message.
   */
  @Generated
  public static void showGeneralError() {
    showAlert(Alert.AlertType.ERROR, "SC Kill Monitor", "ERROR", "An error occurred while performing the desired action.");
  }

  /**
   * Shows an alert. Uses the {@link Alert} class.
   *
   * @param alertType {@link Alert.AlertType} that should be used for the alert.
   * @param titleKey I18N key for the title of the alert window.
   * @param headerKey I18N key for the short text with main information.
   * @param contentKey I18N key for the description of the alert.
   */
  @Generated
  public static void showAlert(@NotNull @NonNull Alert.AlertType alertType,
                        @NotNull @NonNull String titleKey, @NotNull @NonNull String headerKey,
                        @NotNull @NonNull String contentKey) {
    var alert = new Alert(alertType);
    alert.titleProperty().set(titleKey);
    alert.headerTextProperty().set(headerKey);
    alert.contentTextProperty().set(contentKey);
    alert.setResizable(true);
    alert.setHeight(500);
    alert.setWidth(500);
    alert.showAndWait();
  }
}
