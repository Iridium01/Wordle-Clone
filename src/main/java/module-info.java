module com.wordle {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.wordle to javafx.fxml;
    exports com.wordle;
}
