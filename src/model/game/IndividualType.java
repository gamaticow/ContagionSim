package model.game;
/*
 *   Created by Corentin on 10/06/2020 at 22:14
 */

/**
 * Datas from :
 * https://www.statcan.gc.ca/fra/concepts/definitions/age2
 * https://www.insee.fr/fr/statistiques/2381474
 */
public enum IndividualType {

    YOUNG       (0.18,    1.8f, 0.5f    ),  //0-14  years old
    ADOLESCENT  (0.11,    1.1f, 0.8f    ),  //15-24 years old
    ADULT       (0.44,    1f,   1f      ),  //25-64 years old
    SENIOR      (0.27,    0.5f, 1.5f    );  //65+   years old

    private double populationPercentage;
    private float speed;
    private float mortality;

    IndividualType(double populationPercentage, float speed, float mortality){
        this.populationPercentage = populationPercentage;
        this.speed = speed;
        this.mortality = mortality;
    }

    public float getSpeed(){
        return speed;
    }

    public float getMortality(){
        return mortality;
    }

    public static IndividualType getRandomType(){
        double random = Math.random();
        if(random < YOUNG.populationPercentage)
            return YOUNG;
        else if (random < YOUNG.populationPercentage+ADOLESCENT.populationPercentage)
            return ADOLESCENT;
        else if (random < YOUNG.populationPercentage+ADOLESCENT.populationPercentage+ADULT.populationPercentage)
            return ADULT;
        else
            return SENIOR;
    }
}
