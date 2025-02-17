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

import javafx.stage.FileChooser;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Optional;

/**
 * This class provides various utilities for handling file operations.
 * <p>
 * The FileHandler class currently includes a method for opening a file chooser dialog
 * that allows the user to select a specific file, filtered by a predefined file type.
 * It utilizes JavaFX FileChooser for the user interface and logs relevant information
 * for debugging purposes.
 *
 * @author Lucas Greuloch (greluc, lucas.greuloch@protonmail.com)
 * @since 1.0.0
 * @version 1.0.0
 */
@Log4j2
public class FileHandler {

  /**
   * Displays a file chooser dialog that filters files based on a predefined extension.
   * Specifically, it allows the user to select files with the ".log" extension.
   * <p>
   * The chosen file is wrapped in an {@link Optional}. If no file is selected,
   * the returned {@link Optional} will be empty.
   *
   * @return an {@link Optional} containing the selected {@link File},
   *         or an empty {@link Optional} if no file is chosen.
   */
  public static @NotNull Optional<File> showFileChooser() {
    log.debug("Trying to choose a file!");
    final var fileType = "*.log";
    final var filter = new FileChooser.ExtensionFilter("log File", fileType);
    final var chooser = new FileChooser();
    chooser.getExtensionFilters().add(filter);
    return Optional.of(chooser.showOpenDialog(null));
  }
}
