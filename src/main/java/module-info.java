module com.dija.lecturehomework {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.dija.lecturehomework.model to javafx.base;
    requires javafx.base;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires jakarta.jws;
    requires jakarta.xml.ws;
    requires com.sun.xml.bind;
    requires com.sun.xml.fastinfoset;
    requires java.sql;
    requires jakarta.persistence;
    requires v20;
    requires org.slf4j;
    opens com.dija.lecturehomework to javafx.fxml;


    exports com.dija.lecturehomework.service to com.sun.xml.ws;
    opens com.dija.lecturehomework.service to com.sun.xml.bind;

    exports com.dija.lecturehomework;
}
