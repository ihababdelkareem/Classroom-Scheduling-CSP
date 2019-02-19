package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller {


    private Stage primaryStage;

    private Desktop desktop = Desktop.getDesktop();
    final FileChooser fileChooser = new FileChooser();


    public void openFile() throws IOException {
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            openFile(file);
        }
    }

    public void openFile(File file) throws IOException {


        /*try {
            desktop.open(file);

        } catch (IOException ex) {

        }*/


        //
        Stage stage = new Stage();
        Parent tables = FXMLLoader.load(getClass().getResource("tables2.fxml"));
        stage.setTitle("tables");
        stage.setScene(new Scene(tables, 1893, 1147));
        stage.show();


        //


    }

    public void setStageAndSetupListeners(Stage stage){
        this.primaryStage = stage;
    }

}
