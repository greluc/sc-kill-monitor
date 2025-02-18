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

package de.greluc.sc.sckm.controller;

import de.greluc.sc.sckm.AlertHandler;
import de.greluc.sc.sckm.settings.SettingsHandler;
import de.greluc.sc.sckm.settings.SettingsListener;
import de.greluc.sc.sckm.settings.SettingsData;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import static de.greluc.sc.sckm.Constants.*;

/**
 * The StartViewController class manages the user interface interactions and related logic
 * for the starting view of the application. It handles user inputs, updates UI elements
 * based on application data, and communicates with other controllers.
 * <p>
 * Implements the SettingsListener interface to respond to changes in settings data.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @since 1.0.0
 * @version 1.0.0
 */
@Log4j2
public class StartViewController implements SettingsListener {
  @FXML
  private Label selectedPathValue;
  @FXML
  private TextField inputHandle;
  @FXML
  private TextField inputInterval;
  @FXML
  private ComboBox<String> channelSelection;
  private MainViewController mainViewController;

  /**
   * Initializes the controller and its associated UI components with predefined settings and data.
   * <p>
   * This method is executed automatically when the FXML file is loaded. It sets initial values
   * for input fields and dropdown menus, and binds the UI components to the application settings.<br>
   * It configures:<br>
   *  - The handle input field with the application's saved handle.<br>
   *  - The interval input field using the saved interval value.<br>
   *  - The channel selection dropdown with a list of predefined channels.<br>
   * It also sets the selected path for the application and registers the current instance
   * as a listener to settings changes.
   */
  @FXML
  protected void initialize() {
    inputHandle.setText(SettingsData.getHandle());
    inputInterval.setText(String.valueOf(SettingsData.getInterval()));
    channelSelection.setItems(
        FXCollections
            .observableArrayList(LIVE, PTU, EPTU, HOTFIX, TECH_PREVIEW, CUSTOM));
    channelSelection.getSelectionModel().select(SettingsData.getSelectedChannel());
    setSelectedPath();
    SettingsData.addListener(this);
  }

  /**
   * Handles the event triggered when the "Start" button is clicked in the user interface.
   * <p>
   * This method performs input validation on three fields: handle, interval, and path.
   * If any of the fields are empty, an error alert is displayed and the method returns early.
   * Additionally, the method ensures the interval input is a valid integer, showing an error
   * if the value is invalid.
   * <p>
   * Upon successful validation:<br>
   * - The handle and interval values are stored using the {@code SettingsData}.<br>
   * - The main controller's start logic is triggered using {@code mainViewController.onStartPressed()}.
   * <p>
   * Validation logic includes:<br>
   * - Checking if the handle input field is empty and logging a warning if so.<br>
   * - Checking if the interval input field is empty and logging a warning if so.<br>
   * - Checking if the selected path field is empty and logging a warning if so.<br>
   * - Parsing the interval input to an integer, handling potential {@code NumberFormatException}
   *   to ensure a valid integer is provided.
   * <p>
   * Alerts are displayed to the user via {@code AlertHandler} with specific messages indicating
   * the cause of the issue.
   */
  @FXML
  protected void onStartButtonClicked() {
    if (inputHandle.getText().isEmpty()) {
      log.warn("Handle is empty");
      AlertHandler.showAlert(Alert.AlertType.ERROR, "Handle is empty", "Please enter a handle");
      return;
    }
    if (inputInterval.getText().isEmpty()) {
      log.warn("Interval is empty");
      AlertHandler.showAlert(Alert.AlertType.ERROR, "Interval is empty", "Please enter an interval");
      return;
    }
    if (selectedPathValue.getText().isEmpty()) {
      log.warn("Path is empty");
      AlertHandler.showAlert(Alert.AlertType.ERROR, "Path is empty", "Please select a path");
      return;
    }

    try {
      SettingsData.setHandle(inputHandle.getText());
      SettingsData.setInterval(Integer.parseInt(inputInterval.getText()));
      SettingsHandler settingsHandler = new SettingsHandler();
      settingsHandler.saveSettings();
      mainViewController.onStartPressed();
    } catch (NumberFormatException numberFormatException) {
      log.warn("Interval is invalid");
      AlertHandler.showAlert(Alert.AlertType.ERROR, "Interval is invalid", "Please enter a valid interval");
    }
  }

  /**
   * Handles the selection of a channel in the channel selection dropdown.
   * <p>
   * This method retrieves the currently selected channel from the channel selection dropdown
   * and updates the application's settings to reflect the selected channel. Additionally,
   * it performs necessary updates to ensure that any dependent paths or configurations
   * are aligned with the new channel selection.
   * <p>
   * The method is triggered by a user action on the channel selection UI component.
   */
  @FXML
  protected void onChannelSelection() {
    SettingsData.setSelectedChannel(channelSelection.getSelectionModel().getSelectedItem());
    setSelectedPath();
  }

  /**
   * Sets the appropriate text value for the `selectedPathValue` UI element based on the currently
   * selected channel in the application settings.
   * <p>
   * This method evaluates the selected channel retrieved from `SettingsData.getSelectedChannel()`
   * and assigns the respective path string obtained from `SettingsData` to the text property
   * of the `selectedPathValue` label. If the selected channel does not match any predefined
   * channels, it defaults to the "Live" channel path.
   * <p>
   * The supported channels and their corresponding paths include:<br>
   * - PTU: Path retrieved via `SettingsData.getPathPtu()`<br>
   * - EPTU: Path retrieved via `SettingsData.getPathEptu()`<br>
   * - HOTFIX: Path retrieved via `SettingsData.getPathHotfix()`<br>
   * - TECH_PREVIEW: Path retrieved via `SettingsData.getPathTechPreview()`<br>
   * - CUSTOM: Path retrieved via `SettingsData.getPathCustom()`<br>
   * - Default: Path retrieved via `SettingsData.getPathLive()`
   */
  private void setSelectedPath() {
    switch (SettingsData.getSelectedChannel()) {
      case PTU:
        selectedPathValue.setText(SettingsData.getPathPtu());
        break;
      case EPTU:
        selectedPathValue.setText(SettingsData.getPathEptu());
        break;
      case HOTFIX:
        selectedPathValue.setText(SettingsData.getPathHotfix());
        break;
      case TECH_PREVIEW:
        selectedPathValue.setText(SettingsData.getPathTechPreview());
        break;
      case CUSTOM:
        selectedPathValue.setText(SettingsData.getPathCustom());
        break;
      default:
        selectedPathValue.setText(SettingsData.getPathLive());
        break;
    }
  }

  /**
   * Sets the main controller for managing the application's primary view.
   * <p>
   * This method is typically used by other controllers to establish a connection
   * with the MainViewController. It allows the passed MainViewController instance
   * to maintain communication and manage state across different application views.
   *
   * @param mainViewController the main controller instance to be set.
   */
  void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }

  /**
   * Handles actions required when the application's settings have been modified.
   * <p>
   * This method is intended to ensure that the current state of the application reflects
   * any changes made to the settings. Specifically, it recalculates or updates the
   * selected path based on the new configuration. It acts as a listener or trigger
   * following settings updates.
   * <p>
   * The method is overridden to provide a concrete implementation for managing
   * settings changes within the respective context of this implementation.
   */
  @Override
  public void settingsChanged() {
    setSelectedPath();
  }
}