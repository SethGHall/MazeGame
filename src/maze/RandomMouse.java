package maze;

/******************************************************************************************
PROGRAM NAME:   The Intoxicated and Drug Fuelled Random Mouse
FILE:           RandomMouse.java
DESCRIPTION:    This program represents a mouse that is moved in random directions
*******************************************************************************************/

import java.awt.Graphics; 
import java.util.Random;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RandomMouse extends Mouse
{
    private Random generator;
    private Direction direction;
    private Image image;

    //class that initialises a random mouse
    public RandomMouse(Maze maze,int delay,int startRow,int startCol)
    {
        super(maze,delay,startRow,startCol);
        generator = new Random();
        col = generator.nextInt(maze.getNumCols()-1)+1;
    }
    public void loadPngs(int worldWidth, int worldHeight)
    {   int mouseSizeX = worldWidth/maze.getNumCols() - 4*Maze.W_INSET;
        int mouseSizeY = worldHeight/maze.getNumRows() -4*Maze.H_INSET;
        try {
            image = ImageIO.read(new File("catface.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);
         } catch (IOException ex) {
                System.out.println(ex);        
        }
        
        
    }
    //moves the mouse in a random direction and loops until it can move
    //in a certain direction
    public void move()
    {
        boolean allowMovement = false;
        do 
        {
            int choice = generator.nextInt(4);
            if(choice == 0 && maze.isOpen(row,col,Direction.NORTH)&&maze.isInsideMaze(row-1, col))
            {
                row--;
                allowMovement = true;
            }
            if(choice == 1 && maze.isOpen(row,col,Direction.SOUTH)&&maze.isInsideMaze(row+1, col))
            {
                row++;
                allowMovement = true;
            }
            if(choice == 2 && maze.isOpen(row,col,Direction.EAST) && maze.isInsideMaze(row, col+1))
            {
                col++;
                allowMovement = true;
            }
            if(choice == 3 && maze.isOpen(row,col,Direction.WEST) && maze.isInsideMaze(row, col-1))
            {
                col--;
                allowMovement = true;
            }
       
            
        }while(!allowMovement);
    }
    //draws the random mouse
    public void draw(Graphics g, int worldWidth, int worldHeight)
    {   int mouseSizeX = worldWidth/maze.getNumCols();
        int mouseSizeY = worldHeight/maze.getNumRows();
        if(image != null)
        {
            g.drawImage(image,mouseSizeX*col + 2*Maze.W_INSET,mouseSizeY*row + 2*Maze.H_INSET, null);
        }
        else{
            g.setColor(Color.BLACK);
            g.fillOval(mouseSizeX*col + Maze.W_INSET,mouseSizeY*row + Maze.H_INSET,mouseSizeX,mouseSizeY); 
        }

    }
} 
