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

import de.greluc.sc.sckm.data.KillEvent;
import de.greluc.sc.sckm.settings.SettingsData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

/**
 * The ScanViewController class handles the scanning of game logs for specific events
 * and updates the user interface with the detected events. It is responsible for managing
 * background tasks for scanning operations and coordinating with the MainViewController
 * to handle UI transitions and user interactions.
 *
 * <p>Responsibilities:
 * - Initialize UI components and configure their properties.
 * - Start and manage a background scanning process in a separate thread.
 * - Parse log files to extract relevant events and update the UI.
 * - Handle user interactions like stopping the scan.
 * - Coordinate with the MainViewController for managing state changes.
 *
 * <p>Workflow:
 * - Upon initialization, the scan is started in a background thread.
 * - Log files are monitored to detect kill events, using the settings provided by the application.
 * - Detected events are processed and displayed in the UI via JavaFX updates.
 * - The scan operation can be terminated by user action, leading to cleanup and state management.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @version 1.0.1
 * @since 1.0.0
 */
@Log4j2
public class ScanViewController {
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();
  @FXML
  private VBox textPane;
  @FXML
  private ScrollPane scrollPane;
  private MainViewController mainViewController;

  /**
   * Initializes the controller after its root element has been completely processed.
   *
   * <p>This method is automatically called when the associated FXML file is loaded. It configures
   * specific properties of the user interface components and starts the initial task
   * submission to the executor service.
   *
   * <p>Actions performed by this method include:<br>
   * - Configuring the text pane to enable wrapping of its contents.<br>
   * - Adjusting the scroll pane configuration to fit its height and width dynamically.<br>
   * - Submitting the `startScan` task to the {@code executorService}.
   */
  @FXML
  protected void initialize() {
    textPane.setFillWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    executorService.submit(this::startScan);
  }

  /**
   * Handles the "Stop" button press event action.
   *
   * <p>This method is responsible for halting all ongoing tasks by immediately terminating the
   * execution of the associated ExecutorService. It also delegates the stop action to the main
   * view controller, ensuring that any associated view state or logic is properly reverted or
   * handled.
   *
   * <p>This method should be invoked when the user decides to interrupt the active process
   * and return the application to a "stopped" state.
   */
  @FXML
  private void onStopPressed() {
    executorService.shutdownNow();
    mainViewController.onStopPressed();
  }

  /**
   * Starts the log scanning process to monitor kill events.
   *
   * <p>This method continuously scans a specified log file for kill events, extracts
   * relevant data, and updates the user interface with the extracted information.
   * It operates in an endless loop unless interrupted or terminated due to critical
   * errors, such as a missing log file path or user handle.
   *
   * <p>The selected log file path is determined based on the user's channel configuration,
   * with fallback logic to ensure a valid path is retrieved. If no valid path or user
   * handle is provided, the application logs an error and terminates.
   *
   * <p>Key functionalities include:<br>
   * - Determining the log file path based on the selected channel configuration.<br>
   * - Validating the log file path and user handle for non-null and non-empty values.<br>
   * - Extracting kill events from the log file and updating the user interface.<br>
   * - Periodically rescanning the log file at an interval defined in the settings.
   *
   * <p>Errors during log file access or scanning are logged for troubleshooting purposes.
   * The scanning process relies on `Platform.runLater` to update the JavaFX user interface
   * thread with processed kill event data.
   *
   * <p>If the scanning thread is interrupted, it logs the interruption and exits the loop.
   */
  public void startScan() {
    String selectedPathValue = switch (SettingsData.getSelectedChannel()) {
      case PTU -> SettingsData.getPathPtu();
      case EPTU -> SettingsData.getPathEptu();
      case HOTFIX -> SettingsData.getPathHotfix();
      case TECH_PREVIEW -> SettingsData.getPathTechPreview();
      case CUSTOM -> SettingsData.getPathCustom();
      default -> SettingsData.getPathLive();
    };

    if (selectedPathValue == null || selectedPathValue.isEmpty()) {
      log.error("No log file path specified!");
      log.error("Check your input!");
      System.exit(-1);
    }
    if (SettingsData.getHandle() == null || SettingsData.getHandle().isEmpty()) {
      log.error("No handle specified!");
      log.error("Check your input!");
      System.exit(-1);
    }
    log.debug("Using the selected handle: {}", SettingsData.getHandle());
    log.debug("Using the selected channel: {}", SettingsData.getSelectedChannel());
    log.debug("Using the selected log file path: {}", selectedPathValue);

    while (true) {
      try {
        List<KillEvent> killEvents = extractKillEvents(selectedPathValue);
        Platform.runLater(() -> {
          textPane.getChildren().clear();
          killEvents.forEach(killEvent -> textPane.getChildren().add(getKillEventPane(killEvent)));
        });
        log.debug("Finished extracting kill events from log file: {}", selectedPathValue);
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
   * Extracts a list of kill events from a log file. This method reads a log file line by line,
   * detects entries related to actor deaths, and parses the relevant information into
   * {@link KillEvent} objects. The extracted kill events are filtered and sorted
   * based on their timestamp in descending order.
   *
   * @param logFilePath the path to the log file to be processed, must not be null.
   * @return a list of {@link KillEvent} objects extracted from the log file, sorted by timestamp
   *         in descending order, never returns null.
   * @throws IOException if an I/O error occurs during reading the log file.
   */
  private @NotNull List<KillEvent> extractKillEvents(@NotNull String logFilePath)
      throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
      String line;
      List<KillEvent> killEvents = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        if (line.contains("<Actor Death>")) {
          Optional<KillEvent> event = parseKillEvent(line);
          event.ifPresent(killEvent -> {
            if (killEvent.killedPlayer().equals(SettingsData.getHandle())) {
              killEvents.addFirst(killEvent);
              log.info("New kill event detected:\n{}", killEvent);
            }
          });
        }
      }
      killEvents.sort(Comparator.comparing(KillEvent::timestamp, Comparator.reverseOrder()));
      return killEvents;
    }
  }

  /**
   * Creates a VBox containing a non-editable TextArea that displays information
   * about the specified KillEvent. The VBox adjusts its width dynamically
   * according to the width of its container.
   *
   * @param killEvent the KillEvent object whose details are to be displayed in the TextArea
   * @return a VBox containing the TextArea displaying the details of the KillEvent
   */
  private @NotNull VBox getKillEventPane(@NotNull KillEvent killEvent) {
    TextArea textArea = new TextArea(killEvent.toString());
    textArea.setEditable(false);
    textArea.setMinHeight(150);
    textArea.setMaxHeight(150);

    VBox wrapper = new VBox(textArea);
    wrapper.prefWidthProperty().bind(textPane.widthProperty());
    textArea.prefWidthProperty().bind(wrapper.widthProperty());

    VBox.setMargin(textArea, new Insets(5, 10, 0, 0)); // Top, Right, Bottom, Left
    return wrapper;
  }

  /**
   * Parses a log line to create a KillEvent object.
   *
   * <p>The method attempts to extract various components from the provided log line, such as the
   * timestamp, killed player, killer, weapon, damage type, and zone. If successful, it returns
   * an Optional containing the constructed KillEvent object. If the parsing fails, it logs an
   * error and returns an empty Optional.
   *
   * @param logLine the log line to be parsed, which should contain structured information about
   *                a kill event in a specific format.
   * @return an Optional containing a KillEvent object when the log line is successfully parsed,
   *         or an empty Optional if the parsing fails.
   */
  private @NotNull Optional<KillEvent> parseKillEvent(@NotNull String logLine) {
    try {
      // Example log line parsing
      String timestamp = logLine.substring(logLine.indexOf('<') + 1, logLine.indexOf('>'));
      String killedPlayer = extractValue(logLine, "CActor::Kill: '", "'");
      String zone = extractValue(logLine, "in zone '", "'");
      String killer = extractValue(logLine, "killed by '", "'");
      String weapon = extractValue(logLine, "using '", "'");
      String damageType = extractValue(logLine, "with damage type '", "'");

      return Optional.of(new KillEvent(
          ZonedDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME),
          killedPlayer,
          killer,
          weapon,
          damageType,
          zone));
    } catch (Exception exception) {
      log.error("Failed to parse log line: {}", logLine, exception);
      return Optional.empty();
    }
  }

  /**
   * Extracts a substring between the specified start and end tokens within a given text.
   *
   * @param text       The input string from which the value should be extracted. Must not be null.
   * @param startToken The starting delimiter of the substring to extract. Must not be null.
   * @param endToken   The ending delimiter of the substring to extract. Must not be null.
   * @return The extracted substring if both tokens are found; otherwise, an empty string.
   */
  @SuppressWarnings("SameParameterValue")
  private @NotNull String extractValue(@NotNull String text,
                                       @NotNull String startToken,
                                       @NotNull String endToken) {
    int startIndex = text.indexOf(startToken);
    if (startIndex == -1) {
      return "";
    }
    startIndex += startToken.length();
    int endIndex = text.indexOf(endToken, startIndex);
    if (endIndex == -1) {
      return "";
    }
    return text.substring(startIndex, endIndex);
  }

  /**
   * Sets the main view controller.
   * This method establishes the main controller responsible for interacting with
   * and managing the primary application views and their transitions.
   *
   * @param mainViewController the instance of {@code MainViewController} to be set
   */
  void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }
}
