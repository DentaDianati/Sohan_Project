module com.denta.sohanadmin {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens com.denta.sohanadmin to javafx.fxml;
    exports com.denta.sohanadmin;
}