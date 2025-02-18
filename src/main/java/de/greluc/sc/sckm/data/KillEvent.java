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

package de.greluc.sc.sckm.data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event in which a player is killed during gameplay.
 *
 * <ul>
 *   <li><strong>timestamp</strong>: The date and time when the kill event occurred.</li>
 *   <li><strong>killedPlayer</strong>: The name of the player who was killed.</li>
 *   <li><strong>killer</strong>: The name of the player, NPC,
 *   or entity that performed the kill.</li>
 *   <li><strong>weapon</strong>: The weapon or method used to perform the kill.</li>
 *   <li><strong>damageType</strong>: The type of damage inflicted
 *   (e.g., explosive, ballistic).</li>
 *   <li><strong>zone</strong>: The location or area in the game where the kill occurred.</li>
 * </ul>
 *
 * <p>This record provides a detailed representation of a kill event, storing all relevant details
 * for tracking or monitoring purposes.
 * The {@code toString} method formats these details into a human-readable string.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @version 1.0.1
 * @since 1.0.0
 */
public record KillEvent(ZonedDateTime timestamp, String killedPlayer, String killer, String weapon,
                        String damageType,
                        String zone) {
  /**
   * Returns a string representation of the kill event.
   * The string includes details such as the kill date, killed player, zone, killer,
   * weapon or method used, and the type of damage inflicted.
   *
   * @return a formatted string containing the details of the kill event.
   */
  @Contract(pure = true)
  @Override
  public @NotNull String toString() {
    return "Kill Date = " + timestamp.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss:SSS"))
        + " UTC" + "\n"
        + "Killed Player = " + killedPlayer + "\n"
        + "Zone = " + zone + "\n"
        + "Killer = " + killer + "\n"
        + "Used Method/Weapon = " + weapon + "\n"
        + "Damage Type = " + damageType;
  }
}