package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.game.Game;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import model.game.Individual;
import model.statistics.GameStats;
import model.statistics.StatPart;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * The main controller for the game.
 * @author Quentin Cld
 */
public class MainController {

    /**
     * The canvas used to draw the game
     */
    @FXML
    private Canvas mainCanvas;

    /**
     * The timeline of the game
     */
    private Timeline updateGame;

    /**
     * The current game
     */
    private static Game game;
    
    /**
     * The stats of the current game.
     */
    public static GameStats stats;
    
    /**
     * Function used to run the game. The game update every 10ms. Changing this value
     * may cause unexpected behaviours.
     */
    public void runGame() {
        game = new Game((int)mainCanvas.getWidth(), (int)mainCanvas.getHeight());
        game.initialise();

        stats = new GameStats();

        updateValues(0);

        startTimeline();

        // ---------------------------------------------------------------------------------------------------
        // This way of updating seems to cause Java to think that he can dispose of some data,
        // see https://stackoverflow.com/questions/52918195/java-game-with-javafx-canvas-canvas-stops-updating
        // and https://stackoverflow.com/questions/9966136/javafx-periodic-background-task/9966213#9966213
        // ---------------------------------------------------------------------------------------------------

        /*Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                game.update(gc);
            }
        }, 0, 10);*/
    }

    public void startTimeline() {
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();

        updateGame = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                game.update(gc);
                updateValues(game.getIteration());
            }
        }));

        updateGame.setCycleCount(Timeline.INDEFINITE);
        updateGame.play();
    }

    public void play(){
        updateGame.play();
    }

    public void pause(){
        updateGame.pause();
    }

    @FXML private Text healthy;
    @FXML private Text infected;
    @FXML private Text diagnosed;
    @FXML private Text dead;
    @FXML private Text immune;
    @FXML private Text speed;

    public void updateValues(int time){
        int h = 0;
        int i = 0;
        int d = 0;
        int im = 0;
        double speed = 0;

        for(Individual individual : game.individuals){
            speed += individual.getSpeed();
            switch (individual.getState()){
                case HEALTHY:
                    h++;
                    break;
                case INFECTED:
                    i++;
                    break;
                case DIAGNOSED:
                    d++;
                    break;
                case IMMUNE:
                    im++;
            }
        }
        speed /= game.individuals.size();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        healthy.setText(String.valueOf(h));
        infected.setText(String.valueOf(i));
        diagnosed.setText(String.valueOf(d));
        dead.setText(String.valueOf(game.deadIndividuals.size()));
        immune.setText(String.valueOf(im));
        this.speed.setText(df.format(speed));

        if(time % 100 == 0)
            new StatPart(time, h, i, d, game.deadIndividuals.size(), im);
    }

    public void restart() {
        updateGame.stop();
        runGame();
    }

    /**
     * Function call by the restart button on the game view
     */
    public void menu() throws IOException {
        updateGame.stop();

        Stage primaryStage = (Stage) mainCanvas.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));

        MenuController menu = loader.getController();

        //People
        menu.nop.setText(String.valueOf(Game.maxPeople));
        menu.ps.setText(String.valueOf(Individual.defaultSpeed));
        menu.psi.setText(String.valueOf(Individual.defaultRadius));

        //Virus
        menu.popiblslider.setValue(Game.lockdownMinPeoplePercentage * 100);
        menu.popibl.setText(Math.round(Game.lockdownMinPeoplePercentage*100) + "%");
        menu.contagionslider.setValue(Game.contagion * 100);
        menu.contagion.setText(Math.round(Game.contagion*100) + "%");
        menu.mortalityslider.setValue(Individual.chanceToDie * 100);
        menu.mortality.setText(Math.round(Individual.chanceToDie*100) + "%");
        menu.ttd.setText(String.valueOf(Individual.timeToDiagnostic));
    }

    public void graphs() throws IOException {
        updateGame.stop();

        Stage primaryStage = (Stage) mainCanvas.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/statistics.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
    }
}
