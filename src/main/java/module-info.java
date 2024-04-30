module com.sfu.diploma.diplomadesktop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires org.joda.time;
    requires kotlin.stdlib;

    opens com.sfu.diploma.diplomadesktop to javafx.fxml;
    exports com.sfu.diploma.diplomadesktop;
}