package controller;
/*
 *   Created by Corentin on 01/06/2020 at 15:47
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.statistics.GameStats;
import model.statistics.StatPart;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    @FXML private LineChart<Number, Number> lineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        XYChart.Series<Number, Number> healthy = new XYChart.Series<>();
        healthy.setName("Healthy");
        XYChart.Series<Number, Number> infected = new XYChart.Series<>();
        infected.setName("Infected");
        XYChart.Series<Number, Number> diagnosed = new XYChart.Series<>();
        diagnosed.setName("Diagnosed");
        XYChart.Series<Number, Number> dead = new XYChart.Series<>();
        dead.setName("Dead");
        XYChart.Series<Number, Number> immune = new XYChart.Series<>();
        immune.setName("Immune");

        for(StatPart sp : MainController.stats.getStatParts()){
            healthy.getData().add(new XYChart.Data<>(sp.getTime(), sp.getHealthy()));
            infected.getData().add(new XYChart.Data<>(sp.getTime(), sp.getInfected()));
            diagnosed.getData().add(new XYChart.Data<>(sp.getTime(), sp.getDiagnosed()));
            dead.getData().add(new XYChart.Data<>(sp.getTime(), sp.getDead()));
            immune.getData().add(new XYChart.Data<>(sp.getTime(), sp.getImmune()));
        }

        lineChart.getData().add(healthy);
        lineChart.getData().add(infected);
        lineChart.getData().add(diagnosed);
        lineChart.getData().add(dead);
        lineChart.getData().add(immune);
    }

    public void back() throws IOException {
        Stage primaryStage = (Stage) lineChart.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));

        MainController gController = loader.getController();
        gController.startTimeline();
    }
}
