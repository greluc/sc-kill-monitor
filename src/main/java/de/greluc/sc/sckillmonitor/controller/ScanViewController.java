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

import de.greluc.sc.sckillmonitor.data.KillEvent;
import de.greluc.sc.sckillmonitor.settings.SettingsData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static de.greluc.sc.sckillmonitor.Constants.*;
import static de.greluc.sc.sckillmonitor.Constants.TECH_PREVIEW;

/**
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @version 1.0.0
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

  @FXML
  protected void initialize() {
    textPane.setFillWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    executorService.submit(this::startScan);
  }

  @FXML
  private void onStopPressed() {
    executorService.shutdownNow();
    mainViewController.onStopPressed();
  }

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

    AtomicReference<ZonedDateTime> lastTime = new AtomicReference<>(ZonedDateTime.now().minusYears(1));
    while (true) {
      try {
        extractKillEvents(selectedPathValue, lastTime);
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
   * Extracts kill events from the specified game log file.
   * Each line of the log file is analyzed to identify events marked with "<Actor Dead>".
   * If a kill event is successfully parsed from a log line,
   * it is added to the resulting list.
   *
   * @param logFilePath The file path to the log file from which kill events are to be extracted. Must not be null.
   * @throws IOException If an I/O error occurs while reading the log file.
   */
  private void extractKillEvents(@NotNull String logFilePath, @NotNull AtomicReference<ZonedDateTime> lastTime) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains("<Actor Death>")) {
          Optional<KillEvent> event = parseKillEvent(line);
          event.ifPresent(killEvent -> {
            if (killEvent.getKilledPlayer().equals(SettingsData.getHandle())
                && killEvent.getTimestamp().isAfter(lastTime.get())) {
              Platform.runLater(() -> {
                TextArea textArea = new TextArea(killEvent.toString());
                textArea.setEditable(false);
                textArea.setMinHeight(150);
                textArea.setMaxHeight(150);
                textArea.prefWidthProperty().bind(textPane.widthProperty());

                textPane.getChildren().add(textArea);
              });

              log.info("New kill event detected:\n{}", killEvent);

              lastTime.set(killEvent.getTimestamp());
            }
          });
        }
      }
    }
  }

  /**
   * Parses a log line to extract details about a kill event and constructs a {@link KillEvent} object.
   * If the log line cannot be parsed or does not contain the required information,
   * an empty {@code Optional} is returned.
   *
   * @param logLine The log line containing information about the kill event. Must not be null.
   * @return An {@code Optional} containing the constructed {@link KillEvent} object if parsing is successful,
   * otherwise an empty {@code Optional}.
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

      return Optional.of(new KillEvent(ZonedDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME), killedPlayer, killer, weapon, damageType, zone));
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
  private @NotNull String extractValue(@NotNull String text, @NotNull String startToken, @NotNull String endToken) {
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

  void setMainViewController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }
}
