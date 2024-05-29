package org.example.gameoflife;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),32*Settings.getSettings().getWidth(),32*Settings.getSettings().getHeight());

        Pane pane = (Pane) scene.lookup("#gridfx");
        pane.prefHeight(32*Settings.getSettings().getHeight());
        pane.prefWidth(32*Settings.getSettings().getWidth());

        for(int i = 0; i < Settings.getSettings().getHeight(); i++){
            for(int j = 0; j < Settings.getSettings().getWidth(); j++){
                Rectangle r = new Rectangle();
                r.setY(i*32+1);
                r.setX(j*32+1);
                r.setWidth(30);
                r.setHeight(30);
                r.setFill(Color.BLUE);
                r.setOnMouseClicked((MouseEvent e) -> {
                    (new Thread(()->{
                        Grid.getInstance().get(0,0).wave();
                    })).start();
                });
                pane.getChildren().add(r);
            }
        }

        Grid.getInstance().start();
        Thread render = new Thread(()->{
            while(true){
                for(int i = 0; i < Settings.getSettings().getHeight(); i++){
                    for(int j = 0; j < Settings.getSettings().getWidth(); j++){
                        Rectangle rect = (Rectangle) pane.getChildren().get(i*Settings.getSettings().getWidth()+j);
                        rect.setFill(Grid.getInstance().get(i,j).readWriteColor(null));
                    }
                }
            }
        });
        render.start();




        stage.setTitle("Game of life!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if(args.length != 4){
            System.err.println("Expected 4 arguments, but got " + args.length);
            System.exit(-1);
        }

        try {
            Settings.getSettings().setWidth(Integer.parseInt(args[0]));
        }catch (NumberFormatException e){
            System.err.println("Expected integer at position 0");
            System.exit(-1);
        }

        try {
            Settings.getSettings().setHeight(Integer.parseInt(args[1]));
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

        launch();
    }
}