package brain; //packages are used to organize code in Java.  

//Since these classes are in a different package, they needs to be imported.

import actor.*;
import grid.Location;

/**
 * @author Spock
 * MinerBot chooses a random move each turn, but chooses to MINE when beside a Rock.
 */
public class SmarterMinerBot extends BotBrain
{        
    public int chooseAction()
    {
        Location myLoc = new Location(getRow(), getCol());
//        PathFinder(myLoc, 1);
//        for(int r=0; r<values.length; r++) {
//            for (int c = 0; c < values[0].length; c++) {
//                System.out.print(values[r][c] + ", ");
//            }
//            System.out.println();
//        }
        //Mine a Rock if you are beside it.
        if(directionTowardsAdjacentRock() != -1)
            return MINE+directionTowardsAdjacentRock();
        int randomDirection = (int)(Math.random()*8)*45;
        int dirTowardsNearestRock = myLoc.getDirectionToward(locationOfNearestRock());
        Location next = myLoc.getAdjacentLocation(dirTowardsNearestRock);
        if(next.isValidLocation() && getArena()[next.getRow()][next.getCol()]==null)
            return dirTowardsNearestRock;
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
   // public int values[][] = new int[getArena().length][getArena()[0].length];
    public void initValues(int[][] val) {
        for(int r=0; r<val.length; r++)
            for(int c=0; c<val[0].length; c++)
                val[r][c]=0;
    }
/*
    public void PathFinder(Location current, int pathnum) {
        GameObject theArena[][] = getArena();
        if(theArena[current.getRow()][current.getCol()] != null ||
                theArena[current.getRow()][current.getCol()] instanceof Scissors ||
                theArena[current.getRow()][current.getCol()] instanceof Paper) {
            values[current.getRow()][current.getCol()] = 1000;

        }
        else
            values[current.getRow()][current.getCol()] = pathnum;
        int counter = 0;
        for(int r=0; r<values.length; r++) {
            for(int c=0; c<values[0].length; c++) {
                if(values[r][c] != 0)
                    counter++;
            }
        }
        if (!(counter == (theArena.length * theArena[0].length))) {
            for (int dir = 0; dir < 360; dir += 45) {
                Location next = current.getAdjacentLocation(dir);
                PathFinder(next, pathnum + 1);
            }
        }

    }
 */
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
    

