module kr.ac.sch.oopsla {
    requires javafx.controls;
    requires javafx.fxml;
    requires webcam.capture;
    requires java.desktop;
    requires javafx.swing;

    opens kr.ac.sch.oopsla to javafx.fxml;
    exports kr.ac.sch.oopsla;
}
