package model.statistics;
/*
 *   Created by Corentin on 01/06/2020 at 15:23
 */

import java.util.ArrayList;
import java.util.List;

public class GameStats {

    private List<StatPart> statParts;
    public GameStats(){
        statParts = new ArrayList<>();
    }

    protected void addStatPart(StatPart sp){
        statParts.add(sp);
    }

    public List<StatPart> getStatParts(){
        return statParts;
    }

}
