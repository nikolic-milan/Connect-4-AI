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
        private JLabel 			yellowLabel, redLabel, bothPlayerLabel, moveTimeLabel;
        private Panel 			movesPanel;
        MenuItem                newMI, exitMI, redMI, yellowMI, yellowHuman,
        						yellowRandom, yellowMinMax, yellowMinMaxAB,
        						redHuman, redRandom, redMinMax, redMinMaxAB,
        						randomVMinMax, randomVMinMaxAB, minMaxVMinMaxAB;
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
        public static final int MINMAXAB = 3;
        
        int yellowPlayer = HUMAN;
        int redPlayer = HUMAN;
        
        int activeColour = RED;
        int activePlayer = HUMAN;
       
        public static String yellowPlaceHolder = "Yellow Moves Counter: ";
        public static String redPlaceHolder = "Red Moves Counter: ";
        public static String bothPlayerHolder = "Total Number of Moves: ";
        public static String moveTimePlaceHolder = "Time needed to play move: ";
        int yellowMoveCounter = 0;
        int redMoveCounter = 0;
        int moveTime = 0;
        int winner = 0;
        
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
            yellowMinMaxAB = new MenuItem("Yellow as MinMax AB Agent");
            yellowMinMaxAB.addActionListener(this);
            yellowPlayerMenu.add(yellowMinMaxAB);
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
            redMinMaxAB = new MenuItem("Red as MinMax AB Agent");
            redMinMaxAB.addActionListener(this);
            redPlayerMenu.add(redMinMaxAB);
            mbar.add(redPlayerMenu);
            // Simulations menu
            Menu simulationsMenu = new Menu ("Simulations");
            randomVMinMax = new MenuItem("Random vs MinMax");
            randomVMinMax.addActionListener(this);
            simulationsMenu.add(randomVMinMax);
            randomVMinMaxAB = new MenuItem("Random vs MinMax AB");
            randomVMinMaxAB.addActionListener(this);
            simulationsMenu.add(randomVMinMaxAB);
            minMaxVMinMaxAB = new MenuItem("MinMax v MinMax AB");
            minMaxVMinMaxAB.addActionListener(this);
            simulationsMenu.add(minMaxVMinMaxAB);
            mbar.add(simulationsMenu);
            setMenuBar(mbar);
            
            // Build Moves counter
            movesPanel = new Panel();
            
            yellowLabel = new JLabel(yellowPlaceHolder + yellowMoveCounter);
            redLabel = new JLabel(redPlaceHolder + redMoveCounter);;
            bothPlayerLabel = new JLabel(bothPlayerHolder + (yellowMoveCounter + redMoveCounter));
            moveTimeLabel = new JLabel(moveTimePlaceHolder);
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
        	moveTimeLabel.setText(moveTimePlaceHolder + moveTime + " seconds");
        	movesPanel.add(moveTimeLabel);
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
        	moveTimeLabel.setText(moveTimePlaceHolder + moveTime + " seconds");
        	movesPanel.add(moveTimeLabel);
        	check4(g);
        } // paint
 
        public void putDisk(int n, boolean simulation) {
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
                if (simulation)       
                	winner = Helper.checkWin(theArray);
                else
                	repaint();
            }
        }
 
        public void displayWinner(Graphics g, int n) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Courier", Font.BOLD, 100));
            if (n==RED) {
            	g.drawString("Red wins!", 100, 400);
            	System.out.println("Redi wins!");
            }          
            else {
                g.drawString("Yellow wins!", 100, 400);
                System.out.println("Redi wins!");
            }
            end=true;
        }
 
        public void check4(Graphics g) {
        // see if there are 4 disks in a row: horizontal, vertical or diagonal
            // horizontal rows
        	winner = Helper.checkWin(theArray);
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
            } else if (e.getSource() == yellowMinMaxAB) {
            	if (!gameStart) yellowPlayer=MINMAXAB;
            } else if (e.getSource() == redHuman) {
        		if (!gameStart) redPlayer=HUMAN;
            } else if (e.getSource() == redRandom) {
        		if (!gameStart) redPlayer=RANDOM;
            } else if (e.getSource() == redMinMax) {
            	if (!gameStart) redPlayer=MINMAX;
            } else if (e.getSource() == redMinMaxAB) {
            	if (!gameStart) redPlayer=MINMAXAB;
            } else if (e.getSource() == randomVMinMax) 
            	randomVSMinMax();
            else if (e.getSource() == randomVMinMaxAB) 
            	randomVSMinMaxAB();
            else if (e.getSource() == minMaxVMinMaxAB) 
            	minMaxVSMinMaxAB();
        } // end ActionPerformed
        
        /**
         * This method is used to make a move as a random player.
         */
        public void randomAgent(boolean simulation) {
        	RandomPlayer randomPlayer = new RandomPlayer();
        	int nextMove = randomPlayer.nextMove();
        	putDisk(nextMove, simulation);
        	
        	if(redPlayer != HUMAN && yellowPlayer != HUMAN && Helper.checkWin(theArray) == 0)
        		agentPlay(simulation);
        }
        
        /**
         * This method is used to make a move as a MinMax Agent
         */
        public void minMaxAgent(boolean simulation) {
        	Long startTime = System.currentTimeMillis();
        	MinMaxPlayer minMaxPlayer = new MinMaxPlayer();
        	int nextMoveEval = minMaxPlayer.nextMoveEval(theArray, activeColour);
        	putDisk(nextMoveEval, simulation);
        	Long endTime = System.currentTimeMillis();
        	moveTime = (int) ((endTime-startTime) / 1000);
        	//System.out.println("Time needed to construct tree: " + moveTime);
        	if(redPlayer != HUMAN && yellowPlayer != HUMAN && Helper.checkWin(theArray) == 0)
        		agentPlay(simulation);
        }
        
        /**
         * This method is used to make a move as a MinMax Alpha-Beta Agent
         */
        public void minMaxABAgent(boolean simulation) {
        	Long startTime = System.currentTimeMillis();
        	MinMaxPlayer minMaxPlayer = new MinMaxPlayer();
        	int nextMoveAB = minMaxPlayer.nextMoveAB(theArray, activeColour);
        	putDisk(nextMoveAB, simulation);
        	Long endTime = System.currentTimeMillis();
        	moveTime = (int) ((endTime-startTime) / 1000);
        	//System.out.println("Time needed to construct tree: " + moveTime);
        	if(redPlayer != HUMAN && yellowPlayer != HUMAN && Helper.checkWin(theArray) == 0)
        		agentPlay(simulation);
        }
        /**
         * This method is called when a playing button is called.
         * It "puts" the disk in the right place and calls
         * agentPlay() for next move.
         * @param buttonNumber This represents the button pressed
         * 						by a Human player.
         */
        public void onMove(int buttonNumber) {
        	putDisk(buttonNumber, false);
        	agentPlay(false);
        	
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
        	agentPlay(false);
        }
        
        /**
         * This method check the current active colour, which
         * represents the active player and check if the active
         * player is an agent.
         */
        public void agentPlay(boolean simulation) {
        	if (activeColour == YELLOW) {
        		switch(yellowPlayer) {
        			case RANDOM:
        				randomAgent(simulation);
        				break;
        			case MINMAX:
        				minMaxAgent(simulation);
        				break;
        			case MINMAXAB:
        				minMaxABAgent(simulation);
        		}
        	} else {
        		switch(redPlayer) {
    			case RANDOM:
    				randomAgent(simulation);
    				break;
    			case MINMAX:
    				minMaxAgent(simulation);
    				break;
    			case MINMAXAB:
    				minMaxABAgent(simulation);
        		}
        	}
        }
        
        /**
         * This method simulates 10 games in a row between
         * Random Agent and MinMax Agent.
         */
        public void randomVSMinMax() {
        	yellowPlayer = RANDOM;
        	redPlayer = MINMAX;
        	activeColour = YELLOW;
        	System.out.println("Yellow Player is Random Agent.");
        	System.out.println("Red Player is MinMax Agent.");
        	System.out.println("-------------------------------------------------------");
        	for(int i = 0; i < 10; i++) {
        		Long startTime = System.currentTimeMillis();
        		if(activeColour == RED)
        			System.out.println("First player this game is red. Begin!");
        		else
        			System.out.println("First player this game is yellow. Begin!");
        		agentPlay(true);
        		Long endTime = System.currentTimeMillis();

                moveTime = (int) ((endTime-startTime) / 1000);
                if(winner == RED)
                	System.out.println("The winner is: Red player");
                else
                	System.out.println("The winner is: Yellow player");
                System.out.println("Game lasted: " + moveTime + " seconds.");
                System.out.println("Total moves made: " + (redMoveCounter + yellowMoveCounter));
                activeColour = Math.abs(activeColour-3);
                System.out.println("-------------------------------------------------------");
                end=false;
                initialize();
        	}
        	System.out.println("Simulation over.");
        }
        
        /**
         * This method simulates 10 games in a row between
         * Random Agent and MinMaxAb Agent.
         */
        public void randomVSMinMaxAB() {
        	yellowPlayer = RANDOM;
        	redPlayer = MINMAXAB;
        	activeColour = YELLOW;
        	System.out.println("Yellow Player is Random Agent.");
        	System.out.println("Red Player is MinMaxAB Agent.");
        	System.out.println("-------------------------------------------------------");
        	for(int i = 0; i < 10; i++) {
        		Long startTime = System.currentTimeMillis();
        		if(activeColour == RED)
        			System.out.println("First player this game is red. Begin!");
        		else
        			System.out.println("First player this game is yellow. Begin!");
        		agentPlay(true);
        		Long endTime = System.currentTimeMillis();

                moveTime = (int) ((endTime-startTime) / 1000);
                if(winner == RED)
                	System.out.println("The winner is: Red player");
                else
                	System.out.println("The winner is: Yellow player");
                System.out.println("Game lasted: " + moveTime + " seconds.");
                System.out.println("Total moves made: " + (redMoveCounter + yellowMoveCounter));
                activeColour = Math.abs(activeColour-3);
                System.out.println("-------------------------------------------------------");
                end=false;
                initialize();
        	}
        	System.out.println("Simulation over.");
        }
        
        /**
         * This method simulates 5 games in a row between
         * MinMax Agent and MinMaxAb Agent.
         */
        public void minMaxVSMinMaxAB() {
        	yellowPlayer = MINMAX;
        	redPlayer = MINMAXAB;
        	activeColour = YELLOW;
        	System.out.println("Yellow Player is MinMax Agent.");
        	System.out.println("Red Player is MinMaxAB Agent.");
        	System.out.println("-------------------------------------------------------");
        	for(int i = 0; i < 5; i++) {
        		Long startTime = System.currentTimeMillis();
        		RandomPlayer randomPlayer = new RandomPlayer();
        		for (int j = 0; j < 4; j++) {
        			putDisk(randomPlayer.nextMove(), true);
        		}
        		if(activeColour == RED)
        			System.out.println("First player this game is red. Begin!");
        		else
        			System.out.println("First player this game is yellow. Begin!");
        		agentPlay(true);
        		Long endTime = System.currentTimeMillis();

                moveTime = (int) ((endTime-startTime) / 1000);
                if(winner == RED)
                	System.out.println("The winner is: Red player");
                else
                	System.out.println("The winner is: Yellow player");
                System.out.println("Game lasted: " + moveTime + " seconds.");
                System.out.println("Total moves made: " + (redMoveCounter + yellowMoveCounter));
                activeColour = Math.abs(activeColour-3);
                System.out.println("-------------------------------------------------------");
                end=false;
                initialize();
        	}
        	System.out.println("Simulation over.");
        }
} // class