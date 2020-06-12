package model.game;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class manages the game (initialising, drawing, balls collision, state update, game update).
 * @author Quentin Cld
 */
public class Game {

    /**
     * Le pourncetage minimal de personnes diagnostiquées positives pour déclencher le confinement
     */
    public static float lockdownMinPeoplePercentage = 0.25f;

    /**
     * Nombre d'individu dans la simulation
     */
    public static int maxPeople = 100;

    /**
     * Percentage of contagion of the virus
     */
    public static float contagion = 0.5f;

    /**
     * Liste des individus
     */
    public ArrayList<Individual> individuals;

    /**
     * Liste des individus morts
     */
    public ArrayList<Individual> deadIndividuals;

    /**
     * Largeur du canvas
     */
    private int width;

    /**
     * Hauteur du canvas
     */
    private int height;

    /**
     * Nombre total d'itération
     */
    private int iter = 0;

    /**
     * Confinement en cours ou non
     */
    private boolean lockdown = false;

    public Game(int width, int height) {
        individuals = new ArrayList<>();
        deadIndividuals = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    /**
     * Checks if there is no collision between an existing individual and the one being placed.
     * @param individual The existing individual.
     * @return True if there's no collision, false otherwise.
     */
    private boolean canPlace(Individual individual) {
        for (int j = 0; j < individuals.size(); j++) {
            if (individual.collideWith(individuals.get(j))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Initialise the game (place individuals).
     */
    public void initialise() {
        Individual individual;
        boolean canPlace = true;
        for (int i = 0; i < maxPeople-1; i++) {
            individual = new Individual(IndividualType.getRandomType(), width, height, State.HEALTHY);
            while (!canPlace(individual))
                individual = new Individual(IndividualType.getRandomType(), width, height, State.HEALTHY);

            individuals.add(individual);
        }

        individual = new Individual(IndividualType.getRandomType(), width, height, State.INFECTED);
        while (!canPlace(individual)) {
            individual = new Individual(IndividualType.getRandomType(), width, height, State.INFECTED);
        }
        individuals.add(individual);
    }

    /**
     * Draws the individuals on the canvas, according to their colour and coordinates.
     * @see Individual
     * @param gc The graphics context used to draw the individuals.
     */
    private void drawBallsOnCanvas(GraphicsContext gc) {
        for (Individual individual: deadIndividuals) {
            gc.setFill(individual.getColour());
            gc.fillOval(individual.getX(), individual.getY(), individual.getRadius(), individual.getRadius());
        }

        for (Individual individual: individuals) {
            gc.setFill(individual.getColour());
            gc.fillOval(individual.getX(), individual.getY(), individual.getRadius(), individual.getRadius());
        }
    }

    /**
     * Makes a collision happen between two individuals. Each individual will bounce
     * towards the initial direction of the other one.
     * @param i1 The first individual.
     * @param i2 The second individual.
     */
    private void makeCollision(Individual i1, Individual i2) {
        int tmp_dx, tmp_dy;
        double tmp_direction;

        tmp_dx = i1.getDx();
        tmp_dy = i1.getDy();
        tmp_direction = i1.getDirection();

        i1.setDx(i2.getDx());
        i1.setDy(i2.getDy());
        i1.setDirection(i2.getDirection());

        i2.setDx(tmp_dx);
        i2.setDy(tmp_dy);
        i2.setDirection(tmp_direction);
    }

    /**
     * Updates the state of the individuals when a collision happens.
     * @param i1 The first individual.
     * @param i2 The second individual.
     */
    private void checkHealth(Individual i1, Individual i2) {
        if (i1.getState() == State.INFECTED && i2.getState() == State.INFECTED)
            return;
        if (i1.getState() == State.IMMUNE || i2.getState() == State.IMMUNE)
            return;

        if ((i1.getState() == State.INFECTED || i1.getState() == State.DIAGNOSED) && i2.getState() == State.HEALTHY) {
            if(Math.random() < contagion)
                i2.setState(State.INFECTED);
        }

        if ((i2.getState() == State.INFECTED || i2.getState() == State.DIAGNOSED) && i1.getState() == State.HEALTHY) {
            if(Math.random() < contagion)
                i1.setState(State.INFECTED);
        }

    }

    /**
     * Checks if a collision occurred between some individuals. An ArrayList is used in order
     * to reduce the number of collision checking.
     */
    private void checkForCollision() {
        ArrayList<Integer> AlreadyDoneIndex = new ArrayList<>();

        for (int i = 0; i < individuals.size(); i++) {
            if (!AlreadyDoneIndex.contains(i)) {
                for (int j = 0; j < individuals.size(); j++) {
                    if (i != j) {
                        if (individuals.get(i).collideWith(individuals.get(j))) {
                            makeCollision(individuals.get(i), individuals.get(j));
                            checkHealth(individuals.get(i), individuals.get(j));
                            AlreadyDoneIndex.add(j);
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates the game at each step.
     * @see Individual#update(int, int, boolean)
     * @param gc The graphics context used to draw the individuals.
     */
    public void update(GraphicsContext gc) {
        ArrayList<Integer> toRemove = new ArrayList<>();
        int diagnosedCases = 0;

        for (int i = 0; i < individuals.size(); i++) {
            individuals.get(i).update(width, height, lockdown);

            if (individuals.get(i).getState() == State.DEAD) {
                toRemove.add(i);
            }

            if (individuals.get(i).getState() == State.DIAGNOSED ||
                individuals.get(i).getState() == State.IMMUNE ||
                individuals.get(i).getState() == State.DEAD) {

                diagnosedCases++;
            }
        }

        toRemove.sort(Collections.reverseOrder());

        for (int idx: toRemove) {
            deadIndividuals.add(individuals.get(idx));
            individuals.remove(idx);
        }

        if (!lockdown && (float) diagnosedCases / (float) individuals.size() > lockdownMinPeoplePercentage)
            lockdown = true;

        checkForCollision();

        gc.clearRect(0, 0, width, height);
        drawBallsOnCanvas(gc);
        iter++;
    }

    public int getIteration(){
        return iter;
    }
}
