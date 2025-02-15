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

package de.greluc.sc.sckillmonitor;

import lombok.Generated;

/**
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @since 1.0.0
 * @version 1.0.0
 */
public class Constants {
  public static final String LIVE = "LIVE";
  public static final String PTU = "PTU";
  public static final String EPTU = "EPTU";
  public static final String HOTFIX = "HOTFIX";
  public static final String TECH_PREVIEW = "TECH-PREVIEW";
  public static final String CUSTOM = "Custom";

  public static final String UTILITY_CLASS = "Utility class";

  /**
   * Used to exclude the unused constructor from code coverage evaluation.
   */
  @Generated
  private Constants() {
    throw new IllegalStateException(Constants.UTILITY_CLASS);
  }
}
