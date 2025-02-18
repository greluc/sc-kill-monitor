module de.greluc.sc.sckm {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.jetbrains.annotations;
    requires static lombok;
    requires java.prefs;
    requires org.apache.logging.log4j;

    opens de.greluc.sc.sckm to javafx.fxml;
    exports de.greluc.sc.sckm;
    exports de.greluc.sc.sckm.controller;
    opens de.greluc.sc.sckm.controller to javafx.fxml;
    exports de.greluc.sc.sckm.data;
    opens de.greluc.sc.sckm.data to javafx.fxml;
    exports de.greluc.sc.sckm.settings;
    opens de.greluc.sc.sckm.settings to javafx.fxml;
}