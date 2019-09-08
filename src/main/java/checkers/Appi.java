package checkers;

import checkers.board.Board;
import checkers.core.BoardLogic;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Appi extends Application {
    BoardLogic design = new BoardLogic();
    Board board = new Board();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(BoardLogic.getGridPane(), 751, 650, Color.STEELBLUE);
        scene.setOnMouseClicked(event -> board.readMouseEvent(event));
        primaryStage.setTitle("CheckersFX");
        primaryStage.getIcons().add(new Image("red_pawn.png"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

