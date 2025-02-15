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

import de.greluc.sc.sckillmonitor.AlertHandler;
import de.greluc.sc.sckillmonitor.settings.SettingsListener;
import de.greluc.sc.sckillmonitor.settings.SettingsData;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import static de.greluc.sc.sckillmonitor.Constants.*;

/**
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

    @FXML
    protected void onStartButtonClicked() {
        if (inputHandle.getText().isEmpty()) {
            log.warn("Handle is empty");
            AlertHandler.showAlert(Alert.AlertType.ERROR, "SC Kill Monitor", "Handle is empty", "Please enter a handle");
            return;
        }
        if (inputInterval.getText().isEmpty()) {
            log.warn("Interval is empty");
            AlertHandler.showAlert(Alert.AlertType.ERROR, "SC Kill Monitor", "Interval is empty", "Please enter an interval");
            return;
        }
        SettingsData.setHandle(inputHandle.getText());
        SettingsData.setInterval(Integer.parseInt(inputInterval.getText())); // TODO error handling
        mainViewController.onStartPressed();
    }

    @FXML
    protected void onChannelSelection() {
        SettingsData.setSelectedChannel(channelSelection.getSelectionModel().getSelectedItem());
        setSelectedPath();
    }

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

    void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @Override
    public void settingsChanged() {
        setSelectedPath();
    }
}