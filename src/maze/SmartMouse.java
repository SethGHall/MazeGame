package maze;

/******************************************************************************************
PROGRAM NAME:   The Smart  Mouse 
FILE:           SmartMouse.java
DESCRIPTION:    This program represents a mouse that navigates through a maze using a
                depth-first-search algorithm.
*******************************************************************************************/

import java.awt.Graphics;
import java.util.Stack; 
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class SmartMouse extends Mouse
{
    private Stack<VisitedRooms> stack;
    private Random generator;
    private VisitedRooms[][] visitedRooms;
    private boolean foundExit;
    private Direction direction;
    private Image northImage,southImage,eastImage,westImage;
    private boolean backtracking;
    
    //initialises smart mouse and creates a stack of the mouses
    //previous path and a class that has each room it visits 
    public SmartMouse(Maze maze,int delay,int startRow,int startCol)
    {
        super(maze,delay,startRow,startCol);
        generator = new Random();
        visitedRooms = new VisitedRooms[maze.getNumCols()][maze.getNumRows()];
        //loops to initialise VisitedRooms
        for(int i=0;i<maze.getNumCols();i++)
        {
            for(int j=0;j<maze.getNumRows();j++)
            {
                visitedRooms[i][j] = new VisitedRooms(j,i);
            }
        }
        backtracking = false;
        //creates a stack of VisitedRooms
        stack = new Stack<VisitedRooms>();
        visitedRooms[col][row].setVisited(true,Direction.NORTH);
        stack.push(visitedRooms[col][row]);
        foundExit = false;
        direction = Direction.NORTH;
    }
    public void loadPngs(int worldWidth, int worldHeight)
    {   int mouseSizeX = worldWidth/maze.getNumCols() - 4*Maze.W_INSET;
        int mouseSizeY = worldHeight/maze.getNumRows() -4*Maze.H_INSET;
        try {
            northImage = ImageIO.read(new File("mouseNorth2.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);
            southImage = ImageIO.read(new File("mouseSouth2.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);
            eastImage = ImageIO.read(new File("mouseEast2.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);
            westImage = ImageIO.read(new File("mouseWest2.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
                System.out.println(ex);        
        }
        
        
    }
    
    //move the mouse depth first search algorithm   
    public void move()
    {   
          //if the exit is right there smart mouse will exit
        if(!maze.isInsideMaze(row,col+1)&&(maze.isOpen(row,col,Direction.EAST)))
        {
            col++;
            foundExit = true;
        }
        else if(!foundExit && maze.isInsideMaze(row,col))
        {
            ArrayList<Direction> possibleMoves = new ArrayList<>(4);
            if(maze.isOpen(row,col,Direction.NORTH) && !visitedRooms[col][row-1].hasBeenVisited())
                possibleMoves.add(Direction.NORTH);
            if(maze.isOpen(row,col,Direction.SOUTH) && !visitedRooms[col][row+1].hasBeenVisited())
                possibleMoves.add(Direction.SOUTH);
            if(maze.isOpen(row,col,Direction.WEST) && !visitedRooms[col-1][row].hasBeenVisited())
                possibleMoves.add(Direction.WEST);
            if(maze.isOpen(row,col,Direction.EAST) && !visitedRooms[col+1][row].hasBeenVisited())
                possibleMoves.add(Direction.EAST);
            
            if(possibleMoves.size() ==0){
                
                VisitedRooms visited = stack.pop();
                switch(visited.d)
                {
                    case EAST: direction = Direction.WEST;    
                    break;
                    case WEST: direction = Direction.EAST;    
                    break;
                    case NORTH: direction = Direction.SOUTH;    
                    break;
                    case SOUTH: direction = Direction.NORTH;    
                    break;
                }
            }  
            else
            {
                Collections.shuffle(possibleMoves);
                backtracking = false;
                Direction move = possibleMoves.get(0);
                switch(move)
                {
                    case EAST: col++;    
                    break;
                    case WEST: col--;    
                    break;
                    case NORTH: row--;    
                    break;
                    case SOUTH: row++;    
                    break;
                }
                direction = move;
                stack.push(visitedRooms[col][row]);
            }
            row = stack.peek().getRoomRow();
            col = stack.peek().getRoomCol();
            visitedRooms[col][row].setVisited(true,direction);
        }        
    }
    //inner class that holds the col and row of room and a boolean whether its visited
    private class VisitedRooms
    {
        private boolean isVisited;
        private int roomCol;
        private int roomRow;
        public Direction d;
        
        public VisitedRooms(int roomRow,int roomCol)
        {
            this.roomCol = roomCol;
            this.roomRow = roomRow; 
            isVisited = false; 
        } 
        //set this room to be visited
        public void setVisited(boolean isVisited, Direction d)
        {   this.d = d;
            this.isVisited = isVisited;
        }
        //get the col for this room
        public int getRoomCol()
        {
            return roomCol;
        }
        //get the row for this room
        public int getRoomRow()
        {
            return roomRow;
        }
        //returns true if this room has been visited
        public boolean hasBeenVisited()
        {
            return isVisited;
        }
    }
    

    //draws the smart mouse
    public void draw(Graphics n, int worldWidth, int worldHeight)
    {
        Graphics2D g = (Graphics2D)n;
        int mouseSizeX = worldWidth/maze.getNumCols();
        int mouseSizeY = worldHeight/maze.getNumRows();
        Image image = null;
        
        switch(direction)
        {
            case NORTH:
            image = northImage;
            break;
            case SOUTH:
            image = southImage;
            break;
            case EAST:
            image = eastImage;
            break;
            case WEST:
            image = westImage;
            break;
        }
        
        if(image == null)
        {    g.setColor(Color.RED);
            g.fillOval(mouseSizeX*col + 2*Maze.W_INSET,mouseSizeY*row + 2*Maze.H_INSET,mouseSizeX,mouseSizeY); 
        }
        else
        {
            g.drawImage(image,mouseSizeX*col + 2*Maze.W_INSET,mouseSizeY*row + 2*Maze.H_INSET, null);
            
        }
    }
} 
