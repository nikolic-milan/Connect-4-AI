import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Connect4 {
 
    /**
     *		Base for this project. A Connect-4 game for 2 player, no agents.
     *      Program:        Connect4.java
     *      Purpose:        Stacking disk game for 2 players
     *      Creator:        Chris Clarke
     *      Created:        19.08.2007
     *      Modified:       29.11.2012 (JFrame)
     */   

	/**
	 * 		Agents Random, MinMax, ABMinMax, human vs agent and agent vs agent.
	 * 		Program:		Connect4.java
	 * 		Purpose:		Play against and Agent, or have Agents play each other
	 * 		Creator:		Milan Nikolić
	 * 		Created:		31.08.2022
	 */
 
	public static void main(String[] args) {
            Connect4JFrame frame = new Connect4JFrame();
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
}
 
class Connect4JFrame extends JFrame implements ActionListener {
 
        private Button          btn1, btn2, btn3, btn4, btn5, btn6, btn7;
        private Label           lblSpacer;
        private JLabel 			yellowLabel, redLabel, bothPlayerLabel;
        private Panel 			movesPanel;
        MenuItem                newMI, exitMI, redMI, yellowMI, yellowHuman,
        						yellowRandom, yellowMinMax, redHuman, redRandom, redMinMax;
        int[][]                 theArray;
        boolean                 end=false;
        boolean                 gameStart;
        public static final int BLANK = 0;
        public static final int RED = 1;
        public static final int YELLOW = 2;
 
        public static final int MAXROW = 6;     // 6 rows
        public static final int MAXCOL = 7;     // 7 columns
 
        public static final String SPACE = "                  "; // 18 spaces
        	
        public static final int HUMAN = 0;
        public static final int RANDOM = 1;
        public static final int MINMAX = 2;
        
        int yellowPlayer = HUMAN;
        int redPlayer = HUMAN;
        
        int activeColour = RED;
        int activePlayer = HUMAN;
       
        public static String yellowPlaceHolder = "Yellow Moves Counter: ";
        public static String redPlaceHolder = "Red Moves Counter: ";
        public static String bothPlayerHolder = "Total Number of Moves: ";
        int yellowMoveCounter = 0;
        int redMoveCounter = 0;
        
        public Connect4JFrame() {
            setTitle("Connect4 by Chris Clarke");
            MenuBar mbar = new MenuBar();
            Menu fileMenu = new Menu("File");
            newMI = new MenuItem("New");
            newMI.addActionListener(this);
            fileMenu.add(newMI);
            exitMI = new MenuItem("Exit");
            exitMI.addActionListener(this);
            fileMenu.add(exitMI);
            mbar.add(fileMenu);
            Menu optMenu = new Menu("Options");
            redMI = new MenuItem("Red starts");
            redMI.addActionListener(this);
            optMenu.add(redMI);
            yellowMI = new MenuItem("Yellow starts");
            yellowMI.addActionListener(this);
            optMenu.add(yellowMI);
            mbar.add(optMenu);
            // Yellow Player Menu
            Menu yellowPlayerMenu = new Menu ("Yellow Player");
            yellowHuman = new MenuItem("Yellow as Human");
            yellowHuman.addActionListener(this);
            yellowPlayerMenu.add(yellowHuman);
            yellowRandom = new MenuItem("Yellow as Random Agent");
            yellowRandom.addActionListener(this);
            yellowPlayerMenu.add(yellowRandom);
            yellowMinMax = new MenuItem("Yellow as MinMax Agent");
            yellowMinMax.addActionListener(this);
            yellowPlayerMenu.add(yellowMinMax);
            mbar.add(yellowPlayerMenu);
            // Red Player Menu
            Menu redPlayerMenu = new Menu ("Red Player");
            redHuman = new MenuItem("Red as Human");
            redHuman.addActionListener(this);
            redPlayerMenu.add(redHuman);
            redRandom = new MenuItem("Red as Random Agent");
            redRandom.addActionListener(this);
            redPlayerMenu.add(redRandom);
            redMinMax = new MenuItem("Red as MinMax Agent");
            redMinMax.addActionListener(this);
            redPlayerMenu.add(redMinMax);
            mbar.add(redPlayerMenu);
            setMenuBar(mbar);
            
            // Build Moves counter
            movesPanel = new Panel();
            
            yellowLabel = new JLabel(yellowPlaceHolder + yellowMoveCounter);
            redLabel = new JLabel(redPlaceHolder + redMoveCounter);;
            bothPlayerLabel = new JLabel(bothPlayerHolder + (yellowMoveCounter + redMoveCounter));
           //movesPanel.add(bothPlayerLabel);                
                add(movesPanel, BorderLayout.SOUTH);
 
                // Build control panel.
                Panel panel = new Panel();
 
                btn1 = new Button("1");
                btn1.addActionListener(this);
                panel.add(btn1);
                lblSpacer = new Label(SPACE);
                panel.add(lblSpacer);
 
                btn2 = new Button("2");
                btn2.addActionListener(this);
                panel.add(btn2);
                lblSpacer = new Label(SPACE);
                panel.add(lblSpacer);
 
                btn3 = new Button("3");
                btn3.addActionListener(this);
                panel.add(btn3);
                lblSpacer = new Label(SPACE);
                panel.add(lblSpacer);
 
                btn4 = new Button("4");
                btn4.addActionListener(this);
                panel.add(btn4);
                lblSpacer = new Label(SPACE);
                panel.add(lblSpacer);
 
                btn5 = new Button("5");
                btn5.addActionListener(this);
                panel.add(btn5);
                lblSpacer = new Label(SPACE);
                panel.add(lblSpacer);
 
                btn6 = new Button("6");
                btn6.addActionListener(this);
                panel.add(btn6);
                lblSpacer = new Label(SPACE);
                panel.add(lblSpacer);
 
                btn7 = new Button("7");
                btn7.addActionListener(this);
                panel.add(btn7);
 
                add(panel, BorderLayout.NORTH);
                initialize();
                // Set to a reasonable size.
            setSize(1024, 768);
        } // Connect4
 
        public void initialize() {
            theArray=new int[MAXROW][MAXCOL];
            for (int row=0; row<MAXROW; row++)
                for (int col=0; col<MAXCOL; col++)
                    theArray[row][col]=BLANK;
            gameStart=false;
         // show counters
            redMoveCounter = 0;
            redLabel.setText(redPlaceHolder + redMoveCounter);
        	movesPanel.add(redLabel);
        	yellowMoveCounter = 0;
            yellowLabel.setText(yellowPlaceHolder + yellowMoveCounter);
        	movesPanel.add(yellowLabel);
        	bothPlayerLabel.setText(bothPlayerHolder + (yellowMoveCounter + redMoveCounter));
        	movesPanel.add(bothPlayerLabel);
        } // initialize
 
        public void paint(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(110, 50, 100+100*MAXCOL, 100+100*MAXROW);
            for (int row=0; row<MAXROW; row++)
                for (int col=0; col<MAXCOL; col++) {
                    if (theArray[row][col]==BLANK) g.setColor(Color.WHITE);
                    if (theArray[row][col]==RED) {
                    	g.setColor(Color.RED);
                    	
                    	
                    }
                    if (theArray[row][col]==YELLOW) {
                    	g.setColor(Color.YELLOW);
                    	
                    	
                    }
                    g.fillOval(160+100*col, 100+100*row, 100, 100);
                }
            // show counters
            redLabel.setText(redPlaceHolder + redMoveCounter);
        	movesPanel.add(redLabel);
            yellowLabel.setText(yellowPlaceHolder + yellowMoveCounter);
        	movesPanel.add(yellowLabel);
        	bothPlayerLabel.setText(bothPlayerHolder + (yellowMoveCounter + redMoveCounter));
        	movesPanel.add(bothPlayerLabel);
        	check4(g);
        } // paint
 
        public void putDisk(int n) {
        // put a disk on top of column n
            // if game is won, do nothing
            if (end) return;
            gameStart=true;
            int row;
            n--;
            for (row=0; row<MAXROW; row++)
                if (theArray[row][n]>0) break;
            if (row>0) {
                theArray[--row][n]=activeColour;
                if (activeColour==RED) {
                	redMoveCounter = redMoveCounter + 1;
                	activeColour=YELLOW;
                }
                	                      
                else {
                	yellowMoveCounter = yellowMoveCounter + 1;
                	activeColour=RED;
                }
                       
                repaint();
            }
        }
 
        public void displayWinner(Graphics g, int n) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Courier", Font.BOLD, 100));
            if (n==RED)
                g.drawString("Red wins!", 100, 400);
            else
                g.drawString("Yellow wins!", 100, 400);
            end=true;
        }
 
        public void check4(Graphics g) {
        // see if there are 4 disks in a row: horizontal, vertical or diagonal
            // horizontal rows
        	int winner = Helper.checkWin(theArray);
        	if (winner != 0) {
        		displayWinner(g, winner);
        	}
        } // end check4
 
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btn1)
            	onMove(1);
            else if (e.getSource() == btn2)
            	onMove(2);
            else if (e.getSource() == btn3)
            	onMove(3);
            else if (e.getSource() == btn4)
            	onMove(4);
            else if (e.getSource() == btn5)
            	onMove(5);
            else if (e.getSource() == btn6)
            	onMove(6);
            else if (e.getSource() == btn7)
            	onMove(7);
            else if (e.getSource() == newMI) {
                end=false;
                initialize();
                repaint();
            } else if (e.getSource() == exitMI) {
                System.exit(0);
            } else if (e.getSource() == redMI) {
                    // don't change colour to play in middle of game
                if (!gameStart) checkStart(RED);
            } else if (e.getSource() == yellowMI) {
        		if (!gameStart) checkStart(YELLOW);
            } else if (e.getSource() == yellowHuman) {
        		if (!gameStart) yellowPlayer=HUMAN;
            } else if (e.getSource() == yellowRandom) {
        		if (!gameStart) yellowPlayer=RANDOM;
            } else if (e.getSource() == yellowMinMax) {
            	if (!gameStart) yellowPlayer=MINMAX;
            } else if (e.getSource() == redHuman) {
        		if (!gameStart) redPlayer=HUMAN;
            } else if (e.getSource() == redRandom) {
        		if (!gameStart) redPlayer=RANDOM;
            } else if (e.getSource() == redMinMax) {
            	if (!gameStart) redPlayer=MINMAX;
            }
        } // end ActionPerformed
        
        /**
         * This method is used to make a move as a random player.
         */
        public void randomAgent() {
        	RandomPlayer randomPlayer = new RandomPlayer();
        	int nextMove = randomPlayer.nextMove();
        	putDisk(nextMove);
        	if(redPlayer != HUMAN && yellowPlayer != HUMAN)
        		agentPlay();
        }
        
        /**
         * This method is used to make a move as a MinMax Agent
         */
        public void minMaxAgent() {
        	Long startTime = System.currentTimeMillis();
        	MinMaxPlayer minMaxPlayer = new MinMaxPlayer();
        	int nextMoveEval = minMaxPlayer.nextMoveEval(theArray, activeColour);
        	putDisk(nextMoveEval);
        	Long endTime = System.currentTimeMillis();
        	System.out.println("Time needed to construct tree: " + (endTime-startTime));
        	if(redPlayer != HUMAN && yellowPlayer != HUMAN)
        		agentPlay();
        }
        /**
         * This method is called when a playing button is called.
         * It "puts" the disk in the right place and calls
         * agentPlay() for next move.
         * @param buttonNumber This represents the button pressed
         * 						by a Human player.
         */
        public void onMove(int buttonNumber) {
        	putDisk(buttonNumber);
        	agentPlay();
        }
        
        /**
         * This method sets the active colour based on user input.
         * Then calls agent play to check if the current player is 
         * an agent. 
         * @param colourToSet This represents the colour to be set
         * 						as active. 
         */
        public void checkStart(int colourToSet) {
        	activeColour = colourToSet;
        	agentPlay();
        }
        
        /**
         * This method check the current active colour, which
         * represents the active player and check if the active
         * player is an agent.
         */
        public void agentPlay() {
        	if (activeColour == YELLOW) {
        		switch(yellowPlayer) {
        			case RANDOM:
        				randomAgent();
        				break;
        			case MINMAX:
        				minMaxAgent();
        				break;
        		}
        	} else {
        		switch(redPlayer) {
    			case RANDOM:
    				randomAgent();
    				break;
    			case MINMAX:
    				minMaxAgent();
    				break;
        		}
        	}
        }
} // class