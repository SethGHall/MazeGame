package maze;

/******************************************************************************************
PROGRAM NAME:   The Keyboard Mouse of Precision
FILE:           KeyboardMouse.java
DESCRIPTION:    This program represents a mouse that can be controlled by the keyboard
*******************************************************************************************/
import java.awt.Graphics; 
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent; 
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class KeyboardMouse extends Mouse implements KeyListener
{  
    private Direction direction;
    private Image northImage,southImage,eastImage,westImage;
    
    //constructor that initialises the keyboard mouse    
    public KeyboardMouse(Maze maze,int delay,int startRow,int startCol)
    {
        super(maze,delay,startRow,startCol);
        direction = Direction.WEST;
    }
    //moves the mouse in the desired direction and overides abstract
    //mouse move() method  
    public void move()
    { 
        //sets direction if the maze door is open
        
        switch(direction)
        {
            case NORTH:
            if(maze.isOpen(row,col,Direction.NORTH))
                row--;
            break;
            
            case SOUTH:
            if(maze.isOpen(row,col,Direction.SOUTH))
                row++;
            break;
            
            case WEST:
            if(maze.isOpen(row,col,Direction.WEST))
                col--;
            break;

            case EAST:
            if(maze.isOpen(row,col,Direction.EAST))
                col++;
            break;
        }
    }
    //listens for keypresses and sets there direction of that press
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_UP)
            direction = Direction.NORTH;         
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            direction = Direction.SOUTH;
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            direction = Direction.WEST;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            direction = Direction.EAST;
    } 
    public void keyReleased(KeyEvent e) 
    {//empty definition
    }
    public void keyTyped(KeyEvent e)  
    {//empty definition
    }
    public void loadPngs(int worldWidth, int worldHeight)
    {   int mouseSizeX = worldWidth/maze.getNumCols() - 4*Maze.W_INSET;
        int mouseSizeY = worldHeight/maze.getNumRows() -4*Maze.H_INSET;
        try {
            northImage = ImageIO.read(new File("mouseNorth.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);
            southImage = ImageIO.read(new File("mouseSouth.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);;
            eastImage = ImageIO.read(new File("mouseEast.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);;
            westImage = ImageIO.read(new File("mouseWest.png")).getScaledInstance(mouseSizeX, mouseSizeY, Image.SCALE_SMOOTH);;
        } catch (IOException ex) {
                System.out.println(ex);        
        }
        
        
    }
    //draws a oval to represent the mouse that is one third room height and width
    public void draw(Graphics g,int worldWidth, int worldHeight)
    {
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
        if(image != null)
        {
            g.drawImage(image,mouseSizeX*col + 2*Maze.W_INSET,mouseSizeY*row + 2*Maze.H_INSET, null);
        }
        else{
            g.setColor(Color.BLUE);
            g.fillOval(mouseSizeX*col + Maze.W_INSET,mouseSizeY*row + Maze.H_INSET,mouseSizeX,mouseSizeY); 
        }
    }
} 
