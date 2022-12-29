/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;

/**
 *
 * @author Seth
 */
public class MazeRaceGame extends JPanel implements ActionListener
{
    private Maze maze;
    private DrawPanel drawPanel;
    private JButton startGame;
    private Timer timer;
    private Mouse playerMouse;
    private LinkedList<Mouse> smartMouse;
    private ArrayList<Mouse> randomMouse;
    private JSpinner rowSpinner,colSpinner,numSmart,numRandom;
    private JSlider randomSlider,smartSlider;
    
    public MazeRaceGame()
    {
        super(new BorderLayout());
        
        
        this.maze = new Maze(20,30);
        MazeMaker.createMazePaths(maze);
        timer = new Timer(25, this);
        //create the mouses
        smartMouse = new LinkedList<>();
        randomMouse = new ArrayList<>();
        
        
        JPanel northPanel = new JPanel();
        rowSpinner = new JSpinner(new SpinnerNumberModel(20, 3, 50, 1));
        rowSpinner.setBorder(BorderFactory.createTitledBorder("Maze Rows"));
        colSpinner = new JSpinner(new SpinnerNumberModel(30, 3, 50, 1));
        colSpinner.setBorder(BorderFactory.createTitledBorder("Maze Cols"));
        
        numSmart = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        numSmart.setBorder(BorderFactory.createTitledBorder("Num Mice"));
        numRandom = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        numRandom.setBorder(BorderFactory.createTitledBorder("Num Cats"));
        randomSlider = new JSlider(0, 500, 500);
        randomSlider.setPaintTicks(true);
        randomSlider.setPaintLabels(true);
        randomSlider.setMajorTickSpacing(100);
        randomSlider.setBorder(BorderFactory.createTitledBorder("Cat Speed"));
        smartSlider = new JSlider(0, 500, 300);
        smartSlider.setPaintTicks(true);
        smartSlider.setPaintLabels(true);
        smartSlider.setMajorTickSpacing(100);
        smartSlider.setBorder(BorderFactory.createTitledBorder("Mouse Speed"));
        
        northPanel.setLayout(new GridLayout(1, 0));
        northPanel.add(new JPanel().add(rowSpinner));
        northPanel.add(colSpinner);
        northPanel.add(randomSlider);
        northPanel.add(numRandom);
        northPanel.add(smartSlider);
        northPanel.add(numSmart);
        
        startGame = new JButton("Start Game");
        startGame.addActionListener((ActionListener)this);
        super.add(startGame,BorderLayout.SOUTH);
        super.add(northPanel,BorderLayout.NORTH);
        
        drawPanel = new DrawPanel();
        super.add(drawPanel,BorderLayout.CENTER);
    }
    
    
    @Override
     public boolean isFocusable()
    {
        return true;
    }
    
    private class DrawPanel extends JPanel
    {
        public DrawPanel(){
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(500,500));      
        }
        //draws the maze and draws the different mouses in the maze
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            

            if(playerMouse != null)
            {   
                for(Mouse m:smartMouse)
                    m.draw(g,getWidth()-2*Maze.W_INSET,getHeight()-2*Maze.H_INSET);
                  
               
                playerMouse.draw(g,getWidth()-2*Maze.W_INSET,getHeight()-2*Maze.H_INSET);
                
                for(Mouse m:randomMouse)
                    m.draw(g,getWidth()-2*Maze.W_INSET,getHeight()-2*Maze.H_INSET);
                 
            }
            if(maze != null) maze.draw(g,getWidth()-2*Maze.W_INSET,getHeight()-2*Maze.H_INSET); 
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == startGame)
        {   timer.stop();
            smartMouse.clear();
            randomMouse.clear();
            maze = new Maze((Integer)rowSpinner.getValue(),(Integer)colSpinner.getValue()); 
            MazeMaker.createMazePaths(maze);
            if(playerMouse != null)
                playerMouse.requestStop();
            playerMouse = new KeyboardMouse(maze,200,0,0);
            ((KeyboardMouse)playerMouse).loadPngs(drawPanel.getWidth(),drawPanel.getHeight());
            
            Thread thread = new Thread(playerMouse);
            super.addKeyListener((KeyboardMouse)playerMouse);
            
            Random rand = new Random();
            int numberOfSmart = (Integer) numSmart.getValue();
            for(int i=0;i<numberOfSmart;i++)
            {   int startRow = rand.nextInt(maze.getNumRows()-2)+1;
                Mouse mouse = new SmartMouse(maze, smartSlider.getValue(), startRow, 0);
                ((SmartMouse)mouse).loadPngs(drawPanel.getWidth(),drawPanel.getHeight());
                Thread thread2 = new Thread(mouse);
                smartMouse.add(mouse);
                thread2.start();
            }
            
            int numberOfCats = (Integer) numRandom.getValue();
            for(int i=0;i<numberOfCats;i++)
            {   int startRow = rand.nextInt(maze.getNumRows()-2)+1;
                int startCol = rand.nextInt(maze.getNumCols()-2)+1;
                Mouse mouse = new RandomMouse(maze, randomSlider.getValue(), startRow, startCol);
                ((RandomMouse)mouse).loadPngs(drawPanel.getWidth(),drawPanel.getHeight());
                Thread thread2 = new Thread(mouse);
                randomMouse.add(mouse);
                thread2.start();
            }
            
            timer.start();
            
            super.requestFocusInWindow();
            
            thread.start();
        }
        else if(e.getSource() == timer)
        {
            if(!maze.isInsideMaze(playerMouse.getRow(),playerMouse.getCol()))
            {   killGame();
                //PLAYER WINS 
                JOptionPane.showMessageDialog(this, "PLAYER HAS ESCAPED THE MAZE! YOU WIN");
            }
            for(Mouse m:randomMouse)
            {    
                if(playerMouse.getRow() == m.getRow() && playerMouse.getCol() == m.getCol())
                {
                    killGame();
                    JOptionPane.showMessageDialog(this, "YOU LOSE, YOU WERE EATEN BY A CAT");
                }
                
            }
            for(Mouse smart :smartMouse)
            {   
                if(!maze.isInsideMaze(smart.getRow(),smart.getCol()))
                {
                    killGame();
                    JOptionPane.showMessageDialog(this, "A SMART MOUSE EXITED THE MAZE, YOU LOSE");
                    break;
                }
                boolean collide = false;
                for(Mouse m:randomMouse)
                {
                    if(smart.getRow() == m.getRow() && smart.getCol() == m.getCol())
                    {
                        collide = true;
                        break;
                    }
                }
                if(collide){
                    smartMouse.remove(smart);
                    break;
                }
            }
        }
        
        drawPanel.repaint();
    }
    public void killGame()
    {   timer.stop();
        for(Mouse m:smartMouse)
            m.requestStop();
        for(Mouse m:randomMouse)
            m.requestStop();
        playerMouse.requestStop();
        drawPanel.repaint();
    }
    //request keyboard focus

    //main method to test this game
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Maze Maker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MazeRaceGame());
        frame.pack();
        // position the frame in the middle of the screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize();
        frame.setLocation((screenDimension.width-frameDimension.width)/2,
            (screenDimension.height-frameDimension.height)/2);
        frame.setVisible(true);
    }
}
