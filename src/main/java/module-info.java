module de.greluc.sc.sckillmonitor {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.jetbrains.annotations;
    requires static lombok;
    requires java.prefs;
    requires org.apache.logging.log4j;

    opens de.greluc.sc.sckillmonitor to javafx.fxml;
    exports de.greluc.sc.sckillmonitor;
    exports de.greluc.sc.sckillmonitor.controller;
    opens de.greluc.sc.sckillmonitor.controller to javafx.fxml;
    exports de.greluc.sc.sckillmonitor.data;
    opens de.greluc.sc.sckillmonitor.data to javafx.fxml;
    exports de.greluc.sc.sckillmonitor.settings;
    opens de.greluc.sc.sckillmonitor.settings to javafx.fxml;
}