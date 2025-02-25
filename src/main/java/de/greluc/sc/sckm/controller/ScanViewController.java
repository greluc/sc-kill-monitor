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

package de.greluc.sc.sckm.controller;

import static de.greluc.sc.sckm.Constants.CUSTOM;
import static de.greluc.sc.sckm.Constants.EPTU;
import static de.greluc.sc.sckm.Constants.HOTFIX;
import static de.greluc.sc.sckm.Constants.PTU;
import static de.greluc.sc.sckm.Constants.TECH_PREVIEW;
import static de.greluc.sc.sckm.data.KillEventExtractor.extractKillEvents;

import de.greluc.sc.sckm.data.KillEvent;
import de.greluc.sc.sckm.data.KillEventFormatter;
import de.greluc.sc.sckm.settings.SettingsData;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

/**
 * The ScanViewController class handles the scanning of game logs for specific events and updates
 * the user interface with the detected events. It is responsible for managing background tasks for
 * scanning operations and coordinating with the MainViewController to handle UI transitions and
 * user interactions.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @version 1.2.1
 * @since 1.0.0
 */
@Log4j2
public class ScanViewController {
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();
  private final List<KillEvent> killEvents = new ArrayList<>();
  private final List<KillEvent> evaluatedKillEvents = new ArrayList<>();
  @FXML private VBox textPane;
  @FXML private ScrollPane scrollPane;
  @FXML private CheckBox cbShowAll;
  private MainViewController mainViewController;

  /**
   * Initializes the controller after its root element has been completely processed.
   *
   * <p>This method is automatically called when the associated FXML file is loaded. It configures
   * specific properties of the user interface components and starts the initial task submission to
   * the executor service.
   *
   * <p>Actions performed by this method include:
   *
   * <ul>
   *   <li>Configuring the text pane to enable wrapping of its contents.
   *   <li>Adjusting the scroll pane configuration to fit its height and width dynamically.
   *   <li>Submitting the `startScan` task to the {@code executorService}.
   * </ul>
   */
  @FXML
  protected void initialize() {
    textPane.setFillWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    executorService.submit(this::startScan);
    cbShowAll.setSelected(SettingsData.isShowAll());
  }

  /**
   * Handles the "Stop" button press event action.
   *
   * <p>This method is responsible for halting all ongoing tasks by immediately terminating the
   * execution of the associated ExecutorService. It also delegates the stop action to the main view
   * controller, ensuring that any associated view state or logic is properly reverted or handled.
   *
   * <p>This method should be invoked when the user decides to interrupt the active process and
   * return the application to a "stopped" state.
   */
  @FXML
  private void onStopPressed() {
    executorService.shutdownNow();
    mainViewController.onStopPressed();
  }

  /**
   * Handles the "Show All" button click event in the UI.
   *
   * <p>This method updates the application's settings to reflect the state of the "Show All"
   * checkbox. It clears the collection of evaluated kill events and the content displayed in the
   * text pane. After clearing the text pane, it repopulates it by calling the displayKillEvents
   * method.
   */
  @FXML
  protected void onShowAllClicked() {
    SettingsData.setShowAll(cbShowAll.isSelected());
    evaluatedKillEvents.clear();
    textPane.getChildren().clear();
    displayKillEvents();
  }

  /**
   * Sets the main view controller. This method establishes the main controller responsible for
   * interacting with and managing the primary application views and their transitions.
   *
   * @param mainViewController the instance of {@code MainViewController} to be set
   */
  void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }

  /**
   * Initiates a continuous scanning process to extract and display kill events from a specific log
   * file path based on the current channel selection, interval, and handle settings.
   *
   * <p>The method performs the following steps:
   *
   * <ul>
   *   <li>Determines the log file path to monitor based on the currently selected channel.
   *   <li>Logs information about the scanning configuration, including the handle, interval,
   *       selected channel, and log file path.
   *   <li>Enters an infinite loop to repeatedly perform the scanning operation at specified
   *       intervals.
   *   <li>Attempts to extract kill events from the specified log file and updates the GUI with
   *       extracted data.
   *   <li>Handles exceptions during file I/O operations and logs errors if the process fails.
   *   <li>Supports interruption of the scanning process, ensuring proper thread termination.
   * </ul>
   *
   * <p>Note that this method executes indefinitely unless explicitly interrupted, making it crucial
   * to manage thread lifecycle when invoking this method.
   */
  public void startScan() {
    String selectedPathValue =
        switch (SettingsData.getSelectedChannel()) {
          case PTU -> SettingsData.getPathPtu();
          case EPTU -> SettingsData.getPathEptu();
          case HOTFIX -> SettingsData.getPathHotfix();
          case TECH_PREVIEW -> SettingsData.getPathTechPreview();
          case CUSTOM -> SettingsData.getPathCustom();
          default -> SettingsData.getPathLive();
        };

    log.info("Starting scan for kill events...");
    log.info("Using the selected handle: {}", SettingsData.getHandle());
    log.info("Using the selected interval: {}", SettingsData.getInterval());
    log.info("Using the selected channel: {}", SettingsData.getSelectedChannel());
    log.info("Using the selected log file path: {}", selectedPathValue);

    while (true) {
      try {
        extractKillEvents(killEvents, selectedPathValue, ZonedDateTime.now());
        log.debug("Finished extracting kill events");
        displayKillEvents();
        log.debug("Finished updating the GUI with kill events");
      } catch (IOException ioException) {
        log.error("Failed to read the log file: {}", selectedPathValue, ioException);
      }

      try {
        TimeUnit.SECONDS.sleep(SettingsData.getInterval());
      } catch (InterruptedException e) {
        log.debug("Scan thread was interrupted. Terminating...");
        Thread.currentThread().interrupt();
        return;
      }
    }
  }

  /**
   * Displays a list of kill events dynamically within the application's user interface. This method
   * executes on the JavaFX Application Thread using the Platform.runLater mechanism to ensure
   * thread safety when updating the user interface.
   *
   * <p>The method clears the existing content of the textPane, then iterates through the list of
   * kill events and determines which events should be displayed based on specific conditions. If a
   * kill event meets the criteria, it is processed and added to the textPane for display.
   *
   * <p>Conditions for displaying kill events:
   *
   * <ul>
   *   <li>If the killer in the kill event matches the handle retrieved from SettingsData.
   *   <li>If the killer's name contains certain predefined keywords such as "unknown", "aimodule",
   *       or "pu_human".
   *   <li>If SettingsData.isShowAll() is true, the event is displayed regardless of other
   *       conditions.
   * </ul>
   *
   * <p>The method integrates with the getKillEventPane helper function to generate the appropriate
   * UI components for each kill event.
   */
  private void displayKillEvents() {
    Platform.runLater(
        () ->
            killEvents.forEach(
                killEvent -> {
                  if (!evaluatedKillEvents.contains(killEvent)) {
                    if (killEvent.killer().equals(SettingsData.getHandle())
                        || killEvent.killer().toLowerCase().contains("unknown")
                        || killEvent.killer().toLowerCase().contains("aimodule")
                        || killEvent.killer().toLowerCase().contains("pu_")
                        || killEvent.killer().toLowerCase().contains("npc_")
                        || killEvent.killer().toLowerCase().contains("kopion_")) {
                      if (SettingsData.isShowAll()) {
                        textPane.getChildren().add(getKillEventPane(killEvent));
                        evaluatedKillEvents.add(killEvent);
                      }
                    } else {
                      textPane.getChildren().add(getKillEventPane(killEvent));
                      evaluatedKillEvents.add(killEvent);
                    }
                  }
                }));
  }

  /**
   * Creates a VBox containing a non-editable TextArea that displays information about the specified
   * KillEvent. The VBox adjusts its width dynamically according to the width of its container.
   *
   * @param killEvent the KillEvent object whose details are to be displayed in the TextArea
   * @return a VBox containing the TextArea displaying the details of the KillEvent
   */
  private @NotNull VBox getKillEventPane(@NotNull KillEvent killEvent) {
    TextArea textArea = new TextArea(KillEventFormatter.format(killEvent));
    textArea.setEditable(false);
    textArea.setMinHeight(160);
    textArea.setMaxHeight(160);

    VBox wrapper = new VBox(textArea);
    wrapper.prefWidthProperty().bind(textPane.widthProperty());
    textArea.prefWidthProperty().bind(wrapper.widthProperty());

    VBox.setMargin(textArea, new Insets(5, 10, 0, 0)); // Top, Right, Bottom, Left
    return wrapper;
  }
}
