package controller;
/*
 *   Created by Corentin on 12/05/2020 at 14:38
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.menu.CounterThread;
import model.game.Game;
import model.game.Individual;

import java.io.IOException;

public class MenuController {

    //==================================================================================================================
    //=                                                                                                                =
    //=                                                 POPULATION                                                     =
    //=                                                                                                                =
    //==================================================================================================================

    //============================================================================================Number of people (nop)
    @FXML public Text nop;
    private static final int NOP_MAX = 500;
    private static final int NOP_MIN = 10;

    public void nopplus(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Button){
            Button button = (Button) mouseEvent.getSource();

            CounterThread ct = new CounterThread(this::nopAdd);
            ct.start();

            button.setOnMouseReleased(event -> {
                ct.cancel();
            });
        }
    }

    public void nopmoin(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Button){
            Button button = (Button) mouseEvent.getSource();

            CounterThread ct = new CounterThread(this::nopRemove);
            ct.start();

            button.setOnMouseReleased(event -> {
                ct.cancel();
            });
        }
    }

    private void nopAdd(){
        int value = Integer.parseInt(nop.getText());
        value = Math.min(NOP_MAX, value+1);
        nop.setText(String.valueOf(value));
    }

    private void nopRemove(){
        int value = Integer.parseInt(nop.getText());
        value = Math.max(NOP_MIN, value-1);
        nop.setText(String.valueOf(value));
    }

    //=============================================================================================Population speed (ps)

    @FXML public Text ps;
    private static final int PS_MAX = 50;
    private static final int PS_MIN = 1;

    public void psplus(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Button){
            Button button = (Button) mouseEvent.getSource();

            CounterThread ct = new CounterThread(this::psAdd);
            ct.start();

            button.setOnMouseReleased(event -> {
                ct.cancel();
            });
        }
    }

    public void psmoin(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Button){
            Button button = (Button) mouseEvent.getSource();

            CounterThread ct = new CounterThread(this::psRemove);
            ct.start();

            button.setOnMouseReleased(event -> {
                ct.cancel();
            });
        }
    }

    private void psAdd(){
        int value = Integer.parseInt(ps.getText());
        value = Math.min(PS_MAX, value+1);
        ps.setText(String.valueOf(value));
    }

    private void psRemove(){
        int value = Integer.parseInt(ps.getText());
        value = Math.max(PS_MIN, value-1);
        ps.setText(String.valueOf(value));
    }

    //=============================================================================================Population size (psi)

    @FXML public Text psi;
    private static final int PSI_MAX = 20;
    private static final int PSI_MIN = 3;

    public void psiplus(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Button){
            Button button = (Button) mouseEvent.getSource();

            CounterThread ct = new CounterThread(this::psiAdd);
            ct.start();

            button.setOnMouseReleased(event -> {
                ct.cancel();
            });
        }
    }

    public void psimoin(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Button){
            Button button = (Button) mouseEvent.getSource();

            CounterThread ct = new CounterThread(this::psiRemove);
            ct.start();

            button.setOnMouseReleased(event -> {
                ct.cancel();
            });
        }
    }

    private void psiAdd(){
        int value = Integer.parseInt(psi.getText());
        value = Math.min(PSI_MAX, value+1);
        psi.setText(String.valueOf(value));
    }

    private void psiRemove(){
        int value = Integer.parseInt(psi.getText());
        value = Math.max(PSI_MIN, value-1);
        psi.setText(String.valueOf(value));
    }

    //==================================================================================================================
    //=                                                                                                                =
    //=                                                   VIRUS                                                        =
    //=                                                                                                                =
    //==================================================================================================================

    //========================================================Percentage of population infected before lockdown (popibl)

    @FXML public Text popibl;
    @FXML public Slider popiblslider;

    public void popiblmove(){
        int value = (int) popiblslider.getValue();
        popibl.setText(value+"%");
    }

    //=========================================================================================================Contagion

    @FXML public Text contagion;
    @FXML public Slider contagionslider;

    public void contagionmove(){
        int value = (int) contagionslider.getValue();
        contagion.setText(value+"%");
    }

    //=========================================================================================================Mortality

    @FXML public Text mortality;
    @FXML public Slider mortalityslider;

    public void mortalitymove(){
        int value = (int) mortalityslider.getValue();
        mortality.setText(value+"%");
    }

    //==========================================================================================Time to diagnostic (ttd)

    @FXML public Text ttd;
    private static final int TTD_MAX = 2000;
    private static final int TTD_MIN = 10;

    public void ttdplus(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Button){
            Button button = (Button) mouseEvent.getSource();

            CounterThread ct = new CounterThread(this::ttdAdd);
            ct.start();

            button.setOnMouseReleased(event -> {
                ct.cancel();
            });
        }
    }

    public void ttdmoin(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() instanceof Button){
            Button button = (Button) mouseEvent.getSource();

            CounterThread ct = new CounterThread(this::ttdRemove);
            ct.start();

            button.setOnMouseReleased(event -> {
                ct.cancel();
            });
        }
    }

    private void ttdAdd(){
        int value = Integer.parseInt(ttd.getText());
        value = Math.min(TTD_MAX, value+1);
        ttd.setText(String.valueOf(value));
    }

    private void ttdRemove(){
        int value = Integer.parseInt(ttd.getText());
        value = Math.max(TTD_MIN, value-1);
        ttd.setText(String.valueOf(value));
    }


    public void start() throws IOException {
        Stage primaryStage = (Stage) nop.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/game.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));

        MainController gController = loader.getController();

        //People
        Game.maxPeople = Integer.parseInt(this.nop.getText());
        Individual.defaultSpeed = Integer.parseInt(this.ps.getText());
        Individual.defaultRadius = Integer.parseInt(this.psi.getText());

        //Virus
        Game.lockdownMinPeoplePercentage = ((int) popiblslider.getValue()) / 100f;
        Game.contagion = ((int) contagionslider.getValue()) / 100f;
        Individual.chanceToDie = ((int) mortalityslider.getValue()) / 100f;
        Individual.timeToDiagnostic = Integer.parseInt(this.ttd.getText());

        gController.runGame();
    }
}
