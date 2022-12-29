package maze;

/******* ***********************************************************************************
PROGRAM NAME:   The Amazing Maze
FILE:           Maze.java
DESCRIPTION:    This program represents a rectangular maze with numRows and numCols
*******************************************************************************************/

import java.awt.BasicStroke;
import java.awt.Graphics; 
import java.awt.Graphics2D;


public class Maze 
{
    private int numRows;
    private int numCols;
    private Room[][] room;
    public final static int W_INSET = 2;
    public final static int H_INSET = 2;
    
    //constructor that creates an maze with all doors initialy closed
    public Maze(int numRows,int numCols)
    {
        this.numRows = numRows;
        this.numCols = numCols;
        room = new Room[numCols][numRows];
        for(int i=0;i<numCols;i++)
        {
            for(int j=0;j<numRows;j++)
            {
                room[i][j] = new Room();
            }
        }
    }
    //returns true if the row and col is inside the maze
    public boolean isInsideMaze(int row,int col)
    {
        if((row < numRows && col < numCols)&&(row>=0&&col>=0))
            return true;
        else return false;
    }
    //gets the number of rows in this maze
    public int getNumRows()
    {
        return numRows;
    }
    //gets the number of cols in this maze
    public int getNumCols()
    {
        return numCols;
    } 
    //returns true ifrow and col is in the maze and door in Direction is open
    public boolean isOpen(int row,int col,Direction door)
    {
        if(!isInsideMaze(row,col))
            return false;
        return room[col][row].isOpen(door);
    }
    //returns true if the row and col is in the maze and room has an open door
    public boolean hasOpenDoor(int row,int col)
    {
        if(!isInsideMaze(row,col))
            return false;
        else
            return room[col][row].hasOpenDoor();
            
    }
    //checks to see if the row and col is in the maze then opens the door
    public void openDoor(int row,int col,Direction door)
    {
        if(isInsideMaze(row,col))
        {
            //open the door in that direction
            room[col][row].openDoor(door); 
            //open the adjacent door if inside the mazr
            if(door == Direction.NORTH && isInsideMaze(row-1,col))
                room[col][row-1].openDoor(Direction.SOUTH);
            if(door == Direction.SOUTH && isInsideMaze(row+1,col))
                room[col][row+1].openDoor(Direction.NORTH);
            if(door == Direction.EAST && isInsideMaze(row,col+1))
                room[col+1][row].openDoor(Direction.WEST);
            if(door == Direction.WEST && isInsideMaze(row,col-1))
                room[col-1][row].openDoor(Direction.EAST);
        }   
    }
    
    //draw the maze calculating the room height and width and drawing the closed
    //doors in the maze
    public void draw(Graphics g,int width,int height)
    {
        int roomWidth = width/numCols;
        int roomHeight = height/numRows;
         ((Graphics2D)g).setStroke(new BasicStroke(3));
        for(int i=0;i<numCols;i++)
        {
            for(int j=0;j<numRows;j++)
            {
                if(!isOpen(j,i,Direction.NORTH))
                    g.drawLine((roomWidth*i)+W_INSET,(roomHeight*j)+H_INSET,
                            (roomWidth*i)+roomWidth,(roomHeight*j)+H_INSET);
                if(!isOpen(j,i,Direction.SOUTH))
                    g.drawLine((roomWidth*i)+W_INSET,(roomHeight*j)+roomHeight+H_INSET,
                            (roomWidth*i)+roomWidth+W_INSET,(roomHeight*j)+roomHeight+H_INSET);
                    
                if(!isOpen(j,i,Direction.WEST))
                    g.drawLine((roomWidth*i)+W_INSET,(roomHeight*j)+H_INSET,
                            (roomWidth*i)+W_INSET,(roomHeight*j)+roomHeight+H_INSET);
                if(!isOpen(j,i,Direction.EAST))
                    g.drawLine((roomWidth*i)+roomWidth+W_INSET,(roomHeight*j)+H_INSET,
                            (roomWidth*i)+roomWidth+W_INSET,(roomHeight*j)+roomHeight+H_INSET);    
            }
        }     
    } 
    //inner class responsible for representing a Room in the maze
    //it holds 4 booleans to represent the status of each door 
   
} 
