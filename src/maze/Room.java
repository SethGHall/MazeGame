/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

/**
 *
 * @author sehall
 */
public class Room
{
        public boolean north;
        public boolean south;
        public boolean east;
        public boolean west;
    
        //initialise all doors to be false
        public Room()
        {
            north = false;
            south = false;
            east = false;
            west = false;
        }
        //opens the necessary door in the desired direction
        public void openDoor(Direction door)
        {
            switch(door)
            {
                case NORTH:
                north = true;
                break;
                
                case SOUTH:
                south = true;
                break;
                
                case EAST:
                east = true;
                break;
                
                case WEST:
                west = true;
                break;
            } 
        }
        //returns true if the room has at least 1 open door
        public boolean hasOpenDoor()
        {
            if(north || south || east || west) 
                return true;
            else return false;   
        }
        //checks to see if the door in the desired direction is open
        public boolean isOpen(Direction door)
        {
            boolean doorStatus = false;
            switch(door)
            {
                case NORTH:
                doorStatus = north;
                break;
                
                case SOUTH:
                doorStatus = south;
                break;
                
                case EAST:
                doorStatus = east;
                break;
                
                case WEST:
                doorStatus = west;
                break;
            }    
            return doorStatus;
        }
    }   