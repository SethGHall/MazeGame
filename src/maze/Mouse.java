package maze;

 /******************************************************************************************
PROGRAM NAME:   The Abstract Mouse
FILE:           Mouse.java
DESCRIPTION:    This program represents a mouse in a maze
*******************************************************************************************/

import java.awt.Graphics; 
import java.awt.Color;

public abstract class Mouse implements Runnable 
{
    protected Maze maze;
    protected int row;
    protected int col; 
    private boolean stopRequest;
    private int delay;
    
    //initialises fields in the random mouse
    public Mouse(Maze maze,int delay,int startRow, int startCol)
    {
        this.maze = maze;
        this.delay = delay;
        row = startRow;
        col = startCol;
        stopRequest = false;
    }
    //returns the row of the mouse
    public int getRow()
    {
        return row;
    }
    //returns the col of the mouse
    public int getCol()
    {
        return col;
    }
    //runs this mouse in a thread until stopRequest is called
    public void run()
    {
        stopRequest = false;
        while(!stopRequest)
        {
            move();   //move the mouse
            try
            {
                //sleep this thread for the desired delay
                Thread.sleep(delay);
            }catch(InterruptedException e){}
        }
    }
    //request this thread to stop
    public void requestStop()
    {
        stopRequest = false;
    }
    //returns the status of this thread
    public boolean isStopped()
    {
        return stopRequest;
    }
    //abstract method move that is overiden by subclasses
    public abstract void move();
    //creates a mouse represented by and oval
    public abstract void draw(Graphics g, int worldWidth, int worldHeight);
} 
