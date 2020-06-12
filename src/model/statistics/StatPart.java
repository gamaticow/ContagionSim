package model.statistics;

import controller.MainController;

/*
 *   Created by Corentin on 01/06/2020 at 15:23
 */

public class StatPart {

    private int time;

    private int healthy;
    private int infected;
    private int diagnosed;
    private int dead;
    private int immune;

    public StatPart(int time, int healthy, int infected, int diagnosed, int dead, int immune){
        MainController.stats.addStatPart(this);

        this.time = time;
        this.healthy = healthy;
        this.infected = infected;
        this.diagnosed = diagnosed;
        this.dead = dead;
        this.immune = immune;
    }

    public int getTime() {
        return time;
    }

    public int getHealthy() {
        return healthy;
    }

    public int getInfected() {
        return infected;
    }

    public int getDiagnosed() {
        return diagnosed;
    }

    public int getDead() {
        return dead;
    }

    public int getImmune() {
        return immune;
    }
}
