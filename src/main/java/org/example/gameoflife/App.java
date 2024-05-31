package org.example.gameoflife;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;


import java.io.IOException;

public class App extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),Settings.getSettings().getSceneWidth(),Settings.getSettings().getSceneHeight());
        scene.getWindow().setOnCloseRequest((e)->{
            System.exit(0);
        });

        Pane pane = (Pane) scene.lookup("#gridfx");
        if(pane == null){
            System.err.println("Skill issue w App::start(); scene.lookup(\"#gridfx\") jest null");
            System.exit(-1);
        }
        pane.prefHeight(Settings.getSettings().getSceneWidth());
        pane.prefWidth(Settings.getSettings().getSceneHeight());

        createCellStructure(pane);
        AnimationTimer render = getRender(pane);

        Grid.getInstance().start();
        render.start();

        stage.setTitle("Game of life!");
        stage.setScene(scene);
        stage.show();

    }

    private static AnimationTimer getRender(Pane pane) {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update your GUI elements here
                for(int i = 0; i < Settings.getSettings().getRows(); i++){
                    for(int j = 0; j < Settings.getSettings().getColumns(); j++){
                        Rectangle rect = (Rectangle) pane.getChildren().get(i*Settings.getSettings().getColumns()+j);
                        rect.setFill(Grid.getInstance().get(i,j).readWriteColor(null));
                    }
                }
            }
        };
    }

    private static void createCellStructure(Pane pane) {
        for(int i = 0; i < Settings.getSettings().getRows(); i++){
            for(int j = 0; j < Settings.getSettings().getColumns(); j++){
                Rectangle r = new Rectangle();
                r.setY(i*Settings.getSettings().getCellHeight());
                r.setX(j*Settings.getSettings().getCellWidth());
                r.setWidth(Settings.getSettings().getCellWidth());
                r.setHeight(Settings.getSettings().getCellHeight());
                r.setFill(Color.BLUE);
                r.setId(String.valueOf(i*Settings.getSettings().getColumns()+j));
                r.setOnMouseClicked((Event e) -> {
                    (new Thread(()->{
                        int id = Integer.parseInt(((Rectangle) e.getSource()).getId());
                        Grid.getInstance().get(id/Settings.getSettings().getColumns(),id%Settings.getSettings().getColumns()).switchState();
                    })).start();
                });
                pane.getChildren().add(r);
            }
        }
    }


    public static void main(String[] args) {

        getSettings(args);
        
        launch();
    }

    private static void getSettings(String[] args) {
        if(args.length != 4){
            System.err.println("Expected 4 arguments, but got " + args.length);
            System.exit(-1);
        }

        try {
            Settings.getSettings().setColumns(Integer.parseInt(args[0]));
        }catch (NumberFormatException e){
            System.err.println("Expected integer at position 0");
            System.exit(-1);
        }

        try {
            Settings.getSettings().setRows(Integer.parseInt(args[1]));
        }catch (NumberFormatException e){
            System.err.println("Expected integer at position 1");
            System.exit(-1);
        }

        try {
            Settings.getSettings().setProbability(Double.parseDouble(args[2]));
        }catch (NumberFormatException e){
            System.err.println("Expected double at position 2");
            System.exit(-1);
        }

        try {
            Settings.getSettings().setSpeed(Integer.parseInt(args[3]));
        }catch (NumberFormatException e){
            System.err.println("Expected int at position 3");
            System.exit(-1);
        }

        Settings.getSettings().calculateDimensions();
    }
}