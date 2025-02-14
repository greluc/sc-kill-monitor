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

val checkstyleVersion="10.21.2" // https://github.com/checkstyle/checkstyle
val annotationsVersion="26.0.2" // https://mvnrepository.com/artifact/org.jetbrains/annotations https://github.com/JetBrains/java-annotations
val junitVersion = "5.11.4"
val controlsfxVersion = "11.2.1"
val atlantafxVersion = "2.0.1"

plugins {
  id("java")
  id("application")
  id("idea")
  id("jacoco")
  id("checkstyle")
  id("io.freefair.lombok") version "8.12.1"
  id("org.cyclonedx.bom") version "2.1.0" // https://github.com/CycloneDX/cyclonedx-gradle-plugin
  id("dev.hydraulic.conveyor") version "1.12"
  id("org.javamodularity.moduleplugin") version "1.8.15"
  id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.controlsfx:controlsfx:${controlsfxVersion}")
  implementation("org.jetbrains:annotations:$annotationsVersion")
  implementation("io.github.mkpaz:atlantafx-base:${atlantafxVersion}")
  testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
  // https://mvnrepository.com/artifact/org.mockito/mockito-core
  testImplementation("org.mockito:mockito-core:5.15.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

base {
  group = "de.greluc.sc"
  version = "1.0.0"
  description = "SC Kill Monitor - See who griefed you!"
}

java {
  sourceCompatibility = JavaVersion.VERSION_23
  targetCompatibility = JavaVersion.VERSION_23
  toolchain.languageVersion.set(JavaLanguageVersion.of(23))
  modularity.inferModulePath = true
  withSourcesJar()
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

tasks.withType(JavaCompile::class.java) {
  options.encoding = "UTF-8"
}

idea {
  module {
    inheritOutputDirs = true
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

checkstyle {
  toolVersion = checkstyleVersion
}

tasks.cyclonedxBom {
  setProjectType("library")
  setSchemaVersion("1.6")
  setDestination(project.file("docs/bom"))
  setOutputName("bom")
  setOutputFormat("all")
  setIncludeBomSerialNumber(true)
  setIncludeLicenseText(true)
}

tasks.javadoc {
  options {
    (this as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
  }
  setDestinationDir(project.file("docs/javadoc"))
}

application {
  mainModule = "de.greluc.sc.sckillmonitor"
  mainClass = "de.greluc.sc.sckillmonitor.ScKillMonitorApp"
}


javafx {
  version = "23"
  modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.test {
  useJUnitPlatform()
  finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.required.set(true)
    csv.required.set(true)
    html.required.set(true)
  }
}
