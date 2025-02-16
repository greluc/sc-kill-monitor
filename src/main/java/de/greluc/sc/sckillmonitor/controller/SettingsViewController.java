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

package de.greluc.sc.sckillmonitor.controller;

import de.greluc.sc.sckillmonitor.FileHandler;
import de.greluc.sc.sckillmonitor.settings.SettingsData;
import de.greluc.sc.sckillmonitor.settings.SettingsHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Generated;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @since 1.0.0
 * @version 1.0.0
 */
public class SettingsViewController {
  @FXML
  private TextField inputPathLive;
  @FXML
  private TextField inputPathPtu;
  @FXML
  private TextField inputPathEptu;
  @FXML
  private TextField inputPathHotfix;
  @FXML
  private TextField inputPathTechPreview;
  @FXML
  private TextField inputPathCustom;
  @Setter
  private SettingsHandler settingsHandler;

  @FXML
  protected void initialize() {
    inputPathLive.setText(SettingsData.getPathLive());
    inputPathPtu.setText(SettingsData.getPathPtu());
    inputPathEptu.setText(SettingsData.getPathEptu());
    inputPathHotfix.setText(SettingsData.getPathHotfix());
    inputPathTechPreview.setText(SettingsData.getPathTechPreview());
    inputPathCustom.setText(SettingsData.getPathCustom());
  }

  @FXML
  protected void onSave() {
    SettingsData.setPathLive(inputPathLive.getText());
    SettingsData.setPathPtu(inputPathPtu.getText());
    SettingsData.setPathEptu(inputPathEptu.getText());
    SettingsData.setPathHotfix(inputPathHotfix.getText());
    SettingsData.setPathTechPreview(inputPathTechPreview.getText());
    SettingsData.setPathCustom(inputPathCustom.getText());
    settingsHandler.saveSettings();
    closeWindow();
  }

  /**
   * Closes the dedicated settings window.
   */
  @FXML
  @Generated
  private void closeWindow() {
    var stage = (Stage) inputPathLive.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void onLiveClicked() {
    inputPathLive.setText(getPath());
  }

  @FXML
  private void onPtuClicked() {
    inputPathPtu.setText(getPath());
  }

  @FXML
  private void onEptuClicked() {
    inputPathEptu.setText(getPath());
  }

  @FXML
  private void onHotfixClicked() {
    inputPathHotfix.setText(getPath());
  }

  @FXML
  private void onTechPreviewClicked() {
    inputPathTechPreview.setText(getPath());
  }

  @FXML
  private void onCustomClicked() {
    inputPathCustom.setText(getPath());
  }

  private @NotNull String getPath() {
    File file = FileHandler.showFileChooser();
    if (file != null) {
       return file.getPath();
    } else {
      return "";
    }
  }
}
