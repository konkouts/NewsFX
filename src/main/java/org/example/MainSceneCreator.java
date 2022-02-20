package org.example;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class MainSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {
    FlowPane rootFlowPane;
    Button headlinesBtn, everythingBtn, exitBtn;


    public MainSceneCreator(double width, double height){
        super(width, height);
        rootFlowPane = new FlowPane();
        headlinesBtn = new Button("Τρέχοντες κορυφαίοι τίτλοι ειδήσεων");
        everythingBtn = new Button("Αναζήτηση τίτλων ειδήσεων");
        exitBtn = new Button("Έξοδος");

        //attach handle event to headlinesBtn
        headlinesBtn.setOnMouseClicked(this);
        everythingBtn.setOnMouseClicked(this);
        exitBtn.setOnMouseClicked(this);

        //setup flowpane
        rootFlowPane.setHgap(10);
        rootFlowPane.setAlignment(Pos.CENTER);

        //set buttons sizes
        headlinesBtn.setMinSize(120,30);
        everythingBtn.setMinSize(120,30);
        exitBtn.setMinSize(120,30);

        //add buttons to rootflowpane
        rootFlowPane.getChildren().add(headlinesBtn);
        rootFlowPane.getChildren().add(everythingBtn);
        rootFlowPane.getChildren().add(exitBtn);
    }

    @Override
    Scene createScene() {
        return new Scene(rootFlowPane, width, height);
    }

    //Define actions of each button
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == headlinesBtn) {
            App.primaryStage.setTitle("ΤΡΕΧΟΝΤΕΣ ΚΟΡΥΦΑΙΟΙ ΤΙΤΛΟΙ ΕΙΔΗΣΕΩΝ");
            App.primaryStage.setScene(App.newsScene);
        }
        else if (event.getSource() == everythingBtn) {
            App.primaryStage.setTitle("ΑΝΑΖΗΤΗΣΗ ΤΙΤΛΩΝ ΕΙΔΗΣΕΩΝ");
            App.primaryStage.setScene(App.allNewsScene);
        }
        else if (event.getSource() == exitBtn) {
            System.exit(0);
        }
    }
}
