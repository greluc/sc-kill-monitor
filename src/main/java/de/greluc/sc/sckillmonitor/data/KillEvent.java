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

package de.greluc.sc.sckillmonitor.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The KillEvent record represents an event in which a player is killed in a game,
 * storing all relevant details surrounding the event.
 * <p>
 * This record is used as part of log parsing and event processing
 * to capture and represent the killing actions within the gaming environment.
 * It encapsulates critical information such as the timestamp of the event,
 * the names of the killed player and the killer, the weapon used, type of damage,
 * the in-game zone, and the specific location of the event.
 * <p>
 * Primary usage includes structured representation of kill events for further
 * processing, storage, or analysis.
 * <p>
 * Fields:
 * - timestamp: The time at which the kill event occurred.
 * - killedPlayer: The name of the player who was killed.
 * - killer: The name of the player who performed the kill.
 * - weapon: The weapon used by the killer to perform the kill.
 * - damageType: The type of damage dealt that resulted in the kill.
 * - zone: The in-game zone where the kill event took place.
 * - location: The specific location or direction related to the kill event.
 * <p>
 * Overrides:
 * - toString: Provides a string representation of the KillEvent
 * record, including all its fields.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@pm.me)
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public final class KillEvent {
  private final ZonedDateTime timestamp;
  private final String killedPlayer;
  private final String killer;
  private final String weapon;
  private final String damageType;
  private final String zone;

  @Contract(pure = true)
  @Override
  public @NotNull String toString() {
    return "KillEvent at Time " + timestamp.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss:SSS")) + " UTC" + "\n" +
        "Killed Player = " + killedPlayer + "\n" +
        "Zone = " + zone + "\n" +
        "Killer = " + killer + "\n" +
        "Used Method/Weapon = " + weapon + "\n" +
        "Damage Type = " + damageType;
  }
}