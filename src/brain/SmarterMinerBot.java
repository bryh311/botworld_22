package brain; //packages are used to organize code in Java.  

//Since these classes are in a different package, they needs to be imported.

import actor.*;
import grid.Location;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * @author Spock
 * MinerBot chooses a random move each turn, but chooses to MINE when beside a Rock.
 */
public class SmarterMinerBot extends BotBrain
{        
    public int chooseAction()
    {
        Location myLoc = new Location(getRow(), getCol());
        //TODO FIX OUT OF BOUNDS ERROR
        ArrayList<Location> locationHistory = new ArrayList<>();
        if(getMoveNumber() == 1)
            locationHistory.add(myLoc);
        else
            addCurrentLoc(locationHistory);


        System.out.println(locationHistory.get(getMoveNumber()-1).getCol());
        //System.out.println(getMoveNumber());
        int randomDirection = (int)(Math.random()*8)*45;

        //Mine a Rock if you are beside it.
        if(directionTowardsAdjacentRock() != -1)
            return MINE+directionTowardsAdjacentRock();

        int dirTowardsNearestRock = myLoc.getDirectionToward(locationOfNearestRock());
        int dirTowardsNextStepInPath = myLoc.getDirectionToward(pathStep(locationOfNearestRock(), locationHistory));

        Location next = myLoc.getAdjacentLocation(dirTowardsNextStepInPath);
        if(next.isValidLocation() && getArena()[next.getRow()][next.getCol()]==null)
            return dirTowardsNextStepInPath;
        else if(getArena()[next.getRow()][next.getCol()] instanceof Wall) {
            return randomDirection;
        }

        return randomDirection;
    }
    
    /**
     * Gets the direction toward an adjacent Rock (if any)
     * @return a direction where there is an adjacent Rock, -1 if no Rocks are adjacent.
     */
    public int directionTowardsAdjacentRock()
    {
        int result = -1;
        Location myLoc = new Location(getRow(), getCol());
        
        for(int dir=0; dir<360; dir+=45) //loop through the 8 directions
        {
            Location next = myLoc.getAdjacentLocation(dir);
            if(next.isValidLocation() && getArena()[next.getRow()][next.getCol()] instanceof Rock)
                result = dir;  
        }
        
        return result;
    }

    public int directionTowardsNextStepInPath() {
        return 1;
    }
    public void addCurrentLoc(ArrayList<Location> currentLoc) {
        currentLoc.add(new Location(getRow(), getCol()));
    }

    public Location pathStep(Location goal, ArrayList<Location> history) {
        //TODO MAKE HISTORY WORK CORRECTLY
        HashMap<Integer, Location> valuesAndDirs = new HashMap<>();
        GameObject theArena[][] = getArena();
        Location myLoc = new Location(getRow(), getCol());
        for(int dir=0; dir<360; dir+=45) {
            Location next = myLoc.getAdjacentLocation(dir);
            int counter = 0;
            try {
                if (theArena[next.getRow()][next.getCol()] instanceof Wall || theArena[next.getRow()][next.getCol()] instanceof Rock)
                    counter += 1000;
            } catch(Exception e) {
                counter += 1000;
            }
            if(next.distanceTo(goal) > myLoc.distanceTo(goal))
                counter += 20;
            else if(next.distanceTo(goal) == myLoc.distanceTo(goal))
                counter += 10;
            try {
                if (history.get(getMoveNumber()) == next)
                    counter += 300;
                else if(history.get(getMoveNumber()-2) == next)
                    counter += 400;
            } catch(Exception f) {}

            valuesAndDirs.put(counter, next);
        }
        Location returnLoc;
        int k = 4000;
        for(Map.Entry mapElement: valuesAndDirs.entrySet()) {
            if((int) mapElement.getKey() < k)
                k = (int) mapElement.getKey();
        }
        return valuesAndDirs.get(k);
    }

   // public int values[][] = new int[getArena().length][getArena()[0].length];
    public void initValues(int[][] val) {
        for(int r=0; r<val.length; r++)
            for(int c=0; c<val[0].length; c++)
                val[r][c]=0;
    }

    public Location locationOfRock() {
        GameObject theArena[][] = getArena();
        for(int r=0; r<theArena.length; r++)
            for(int c=0; c<theArena[0].length; c++)
            {
                if(theArena[r][c] instanceof Rock)
                    return new Location(r,c);
            }
        return null;
    }
    public Location locationOfBot() {
        GameObject theArena[][] = getArena();
        for(int r=0; r<theArena.length; r++) {
            for(int c=0; c<theArena[0].length; c++) {
                if(theArena[r][c] instanceof Bot && (r != getRow()) && (r != getCol()))
                    return new Location(r,c);
            }
        }
        return null;
    }
    public Location locationOfNearestRock() {
        Location loc = new Location(0,0);
        Location myLoc = new Location(getRow(), getCol());
        if(myLoc.getCol()==0 && myLoc.getRow()==0)
            loc = new Location(24,24);
        GameObject theArena[][] = getArena();
        for(int r=0; r<theArena.length; r++) {
            for(int c=0; c<theArena[0].length; c++) {
                if(theArena[r][c] instanceof Rock) {
                    double d1 = myLoc.distanceTo(loc);
                    double d2 = myLoc.distanceTo(new Location(r,c));
                    if(d1 >= d2)
                        loc = new Location(r,c);
                }
            }
        }
        if(loc.getRow() ==0 && loc.getCol()==0 && !(theArena[loc.getRow()][loc.getCol()] instanceof Rock)) {
            loc = new Location(12,12);
        }
        return loc;
    }
}
    

