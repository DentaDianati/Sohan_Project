module com.denta.sohan {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires java.prefs;
    requires com.google.gson;

    opens com.denta.sohan to javafx.fxml, com.google.gson;
    exports com.denta.sohan;
}