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

package de.greluc.sc.sckm.data;

import static de.greluc.sc.sckm.FileHandler.writeKillEventToFile;

import de.greluc.sc.sckm.settings.SettingsData;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

/**
 * The KillEventExtractor class is responsible for extracting and processing kill events
 * from game log files. These events are represented by {@link KillEvent} objects
 * containing detailed information about each kill event such as timestamp, killer, killed player,
 * weapon used, and location.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @version 1.2.1
 * @since 1.2.1
 */
@Log4j2
public class KillEventExtractor {

  /**
   * Extracts kill events from a specified log file and adds them to the provided list of kill events.
   * The method processes the log file line by line, identifies kill events marked with {@code <Actor Death>},
   * validates the events, and adds unique kill events to the list. Newly detected kill events are also
   * logged and written to a file.
   *
   * @param killEvents a list of KillEvent objects where the extracted and validated kill events will be added
   * @param inputFilePath the file path of the log file to be read for extracting kill events
   * @param scanStartTime the timestamp when the scan started, used for creating filenames for writing events to a file
   * @throws IOException if an I/O error occurs while accessing or reading the log file
   */
  public static void extractKillEvents(@NotNull List<KillEvent> killEvents, @NotNull String inputFilePath, @NotNull ZonedDateTime scanStartTime) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains("<Actor Death>")) {
          Optional<KillEvent> event = parseKillEvent(line);
          event.ifPresent(
              killEvent -> {
                if (killEvent.killedPlayer().equals(SettingsData.getHandle())
                    && !killEvents.contains(killEvent)) {
                  killEvents.addFirst(killEvent);
                  log.info("New kill event detected");
                  log.debug("Kill Event:\n{}", killEvent);
                  writeKillEventToFile(
                      killEvent,
                      scanStartTime.format(DateTimeFormatter.ofPattern("yyMMdd-HHmmss")));
                }
              });
        }
      }
      killEvents.sort(Comparator.comparing(KillEvent::timestamp, Comparator.reverseOrder()));
    } catch (FileNotFoundException fileNotFoundException) {
      log.error("Failed to find the specified log file: {}", inputFilePath, fileNotFoundException);
    }
  }

  /**
   * Parses a log line to create a KillEvent object.
   *
   * <p>The method attempts to extract various components from the provided log line, such as the
   * timestamp, killed player, killer, weapon, damage type, and zone. If successful, it returns an
   * Optional containing the constructed KillEvent object. If the parsing fails, it logs an error
   * and returns an empty Optional.
   *
   * @param logLine the log line to be parsed, which should contain structured information about a
   *     kill event in a specific format.
   * @return an Optional containing a KillEvent object when the log line is successfully parsed, or
   *     an empty Optional if the parsing fails.
   */
  private static @NotNull Optional<KillEvent> parseKillEvent(@NotNull String logLine) {
    try {
      String timestamp = logLine.substring(logLine.indexOf('<') + 1, logLine.indexOf('>'));
      String killedPlayer = extractValue(logLine, "CActor::Kill: '", "'");
      String zone = extractValue(logLine, "in zone '", "'");
      String killer = extractValue(logLine, "killed by '", "'");
      String weapon = extractValue(logLine, "using '", "'");
      String weaponClass = extractValue(logLine, "[Class ", "]");
      String damageType = extractValue(logLine, "with damage type '", "'");

      return Optional.of(
          new KillEvent(
              ZonedDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME),
              killedPlayer,
              killer,
              weapon,
              weaponClass,
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
   * @param text The input string from which the value should be extracted. Must not be null.
   * @param startToken The starting delimiter of the substring to extract. Must not be null.
   * @param endToken The ending delimiter of the substring to extract. Must not be null.
   * @return The extracted substring if both tokens are found; otherwise, an empty string.
   */
  @SuppressWarnings("SameParameterValue")
  private static @NotNull String extractValue(
      @NotNull String text, @NotNull String startToken, @NotNull String endToken) {
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
  }}
