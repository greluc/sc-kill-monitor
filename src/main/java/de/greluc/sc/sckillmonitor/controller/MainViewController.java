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

import de.greluc.sc.sckillmonitor.ScKillMonitorApp;
import de.greluc.sc.sckillmonitor.settings.SettingsHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

/**
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @since 1.0.0
 * @version 1.0.0
 */
@Log4j2
public class MainViewController {
  @FXML
  private GridPane basePane;
  private GridPane startPane;
  private GridPane scanPane;
  private final SettingsHandler settingsHandler = new SettingsHandler();

  @FXML
  protected void initialize() {
    settingsHandler.loadSettings();
    FXMLLoader fxmlLoader = new FXMLLoader(ScKillMonitorApp.class.getResource("StartView.fxml"));
    try {
      startPane = fxmlLoader.load();
    } catch (IOException ioException) {
      log.error("Could not load StartView.fxml", ioException);
      System.exit(-1);
    }
    StartViewController startViewController = fxmlLoader.getController();
    startViewController.setMainViewController(this);
    GridPane.setConstraints(startPane, 0, 1);
    basePane.getChildren().add(startPane);
  }

  @FXML
  protected void onSettingsPressed() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(ScKillMonitorApp.class.getResource("SettingsView.fxml"));
      Stage stage = new Stage();
      Scene scene = new Scene(fxmlLoader.load());
      SettingsViewController settingsViewController = fxmlLoader.getController();
      settingsViewController.setSettingsHandler(settingsHandler);
      stage.setScene(scene);
      stage.setMaximized(false);
      stage.setResizable(true);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.show();
    } catch (IOException ioException) {
      log.error("Couldn't load SettingsView.fxml", ioException);
    }
  }

  @FXML
  protected void onClosePressed() {
    System.exit(0);
  }

  @FXML
  protected void onAboutPressed() {
    // TODO implement
  }

  protected void onStartPressed() {
    basePane.getChildren().remove(startPane);
    FXMLLoader fxmlLoader = new FXMLLoader(ScKillMonitorApp.class.getResource("ScanView.fxml"));
    scanPane = null;
    try {
      scanPane = fxmlLoader.load();
    } catch (IOException ioException) {
      log.error("Could not load ScanView.fxml", ioException);
      System.exit(-1);
    }
    ScanViewController scanViewController = fxmlLoader.getController();
    scanViewController.setMainViewController(this);
    GridPane.setConstraints(scanPane, 0, 1);
    basePane.getChildren().add(scanPane);
  }

  protected void onStopPressed() {
    basePane.getChildren().remove(scanPane);
    FXMLLoader fxmlLoader = new FXMLLoader(ScKillMonitorApp.class.getResource("StartView.fxml"));
    try {
      startPane = fxmlLoader.load();
    } catch (IOException ioException) {
      log.error("Could not load StartView.fxml", ioException);
      System.exit(-1);
    }
    StartViewController startViewController = fxmlLoader.getController();
    startViewController.setMainViewController(this);
    GridPane.setConstraints(startPane, 0, 1);
    basePane.getChildren().add(startPane);
  }
}