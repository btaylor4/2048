/************** Imports **********************/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
/*
 * This is my attempt at a 2048.

 * When there is time, go back and clean up code, deleting repetitive code.
 * Also, possibly create timer to make it easier to see where the tile is being placed.
 * To do this, I could create animations and a timer to do so
 */

public class Game implements KeyListener
{
	//.setOpaque(true) to set background
	JFrame frame = new JFrame("2048");
	
	//Buttons
	JButton[][] buttons = new JButton[4][4]; //make buttons invisible, make listeners to these
	JButton score = new JButton("Score: ");
	JButton hScore = new JButton("High Score ");
	
	int [][] counts = new int [4][4];
	Container grid = new Container();
	File file = new File("highscore.txt"); //holds the highscore
	File create = new File("cmat.txt"); // holds the matrix
	File cScore = new File("score.txt"); //saves current score
	
	final static int SIZE = 4;
	static int totalScore = 0;
	static boolean check = false;
	static boolean move = true;
	
	public Game()
	{
		frame.setSize(350,350);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());

		grid.setLayout(new GridLayout(4,4));		
		frame.add(grid, BorderLayout.CENTER);
		
		try 
		{
			Scanner input = new Scanner(file);
			
			while(input.hasNextInt())
			{
				int HS = input.nextInt();
				hScore.setText("High Score: " + HS+"");
			}
		} 
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try 
		{
			Scanner input = new Scanner(cScore);
			
			while(input.hasNextInt())
			{
				score.setText("Score: " + input.nextInt() +"");
			}
		} 
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		frame.getContentPane().setBackground(Color.GRAY);
	
		score.setEnabled(true);
		score.addKeyListener(this);
		score.setHorizontalAlignment(SwingConstants.LEFT);	
		score.setFont(new Font("Hannotate SC", Font.PLAIN, 18));
		score.setBounds(5, 250, 120, 60);
		score.setOpaque(true);
		score.setBackground(Color.GRAY);
		frame.getContentPane().add(score, BorderLayout.SOUTH);
		
		hScore.setEnabled(true);
		hScore.addKeyListener(this);
		hScore.setHorizontalAlignment(SwingConstants.LEFT);
		hScore.setFont(new Font("Hannotate SC", Font.PLAIN, 18));	
		hScore.setBounds(195, 250, 150, 60);
		hScore.setOpaque(true);
		hScore.setBackground(Color.GRAY);
		frame.getContentPane().add(hScore, BorderLayout.NORTH);
		
		for(int i=0; i < buttons.length; i++)
		{
			for(int j=0; j < buttons[i].length; j++)
			{
				buttons[i][j] = new JButton();
				buttons[i][j].setOpaque(true);
				buttons[i][j].setEnabled(true);
				buttons[i][j].addKeyListener(this);
				buttons[i][j].setFont(new Font("Hannotate SC", Font.BOLD, 20));
				buttons[i][j].setBackground(Color.GRAY);
				grid.add(buttons[i][j]);
			}
		}
				
		frame.add(grid, BorderLayout.CENTER);
		
		boolean place = true;
		createBoard();
		changeColor();
		
		//This double for loop is to check if tiles are already inside the board
		outer:
		for(int i = 0; i < counts.length; i++)
		{
			for(int j = 0; j < counts.length; j++)
			{
				if(counts[i][j] != 0)
				{
					place = false;
					break outer;
				}
			}
		}
		
		if(place)
		{
			tilePlace();
			tilePlace();
		}
		saveBoard();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
	}
	
	/*************Board Settings***************/
	/* 
	 * changeColor: changes the colors of the tiles depending on what number is shown
	 * createBoard: reads in our matrix from a file
	 * saveBoard: saves our progress matrix of our game
	 * saveScore: saves our current score in our game
	 * tilePlace: places tiles randomly in the board
	 */
	
	public void changeColor() //Color of tiles
	{
		int i, j;
		
		for (i =0; i < counts.length; i++)
		{
			for (j =0; j < counts[i].length; j++)
			{
				switch(counts[i][j])
				{
					case 0:
						buttons[i][j].setBackground(Color.gray);
						break;
					
					case 2:
						buttons[i][j].setBackground(Color.lightGray);
						break;
						
					case 4:
						buttons[i][j].setBackground(Color.DARK_GRAY);
						break;
						
					case 8:
						buttons[i][j].setBackground(new Color(255, 127, 80)); //coral
						break;
						
					case 16:
						buttons[i][j].setBackground(new Color(255, 165, 0));
						break;
						
					case 32:
						buttons[i][j].setBackground(new Color(250, 128, 144));
						break;
						
					case 64:
						buttons[i][j].setBackground(new Color(255,69, 1));
						break;
						
					case 128:
						buttons[i][j].setBackground(new Color(240, 230, 140));
						break;
						
					case 256:
						buttons[i][j].setBackground(new Color(255, 165, 0));
						break;
						
					case 512:
						buttons[i][j].setBackground(new Color(255, 165, 0));
						break;
						
					case 1024:
						buttons[i][j].setBackground(new Color(255, 165, 0));
						break;
						
					case 2048:
						buttons[i][j].setBackground(new Color(255, 165, 0));
						break;
						
					case 4096:
						buttons[i][j].setBackground(new Color(255, 165, 0));
						break;	
						
					case 8192:
						buttons[i][j].setBackground(Color.BLACK);
						break;
				}
			}
		}
	}
	
	public void createBoard() // creates board. Initialize all elements to be 0
	{
		try 
		{
			Scanner input = new Scanner(create);
			
			for(int i =0; i < counts.length; i++)
			{
				for(int j =0; j < counts.length; j++)
				{
					counts[i][j] = input.nextInt();
					
					if(counts[i][j]==0)
					{
						buttons[i][j].setText("");
					}
					
					else
						buttons[i][j].setText(counts[i][j]+"");
				}
			}
		} 
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void saveBoard()
	{
		try 
		{	
			FileWriter fw = new FileWriter(create.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i =0; i < counts.length; i++)
			{
				for(int j =0; j < counts.length; j++)
				{
					bw.write(counts[i][j] +"");
					bw.newLine();
				}
			}
			bw.close();
		} 
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void saveScore(int totalScore)
	{
		try 
		{
			FileWriter fw = new FileWriter(cScore.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(totalScore+"");
			bw.close();
		} 
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void tilePlace() //places a random 2 or 4 in a random position
	{
		//Possibly make tile placement not where a previous tile was before hand
		int row, col, choice;
	    
	    row = (int)(Math.random() * 4); // numbers 0 to 3
	    col = (int)(Math.random() * 4); //numbers 0 to 3
	    choice = (int)(Math.random() * 2); //number 0 and 1 to choice wether it is a 4 or a 2
	    
	    while(counts[row][col] != 0)
	    {
		    row = (int)(Math.random() * 4); // numbers 0 to 3
		    col = (int)(Math.random() * 4); //numbers 0 to 3
		    choice = (int)(Math.random() * 2); //number 0 and 1 to choice wether it is a 4 or a 2
	    }
	    
	    switch(choice)
	    {
		    case 0:
		    	counts[row][col] = 2;
		        buttons[row][col].setText(counts[row][col] + "");
		        changeColor();
		    	break;
		    	
		    case 1:
		    	counts[row][col] = 4;
		        buttons[row][col].setText(counts[row][col] + "");
		        changeColor();
		    	break;
	    }
	}
	
	
	/*****************MOVES*******************/
	/*
	 * The next few methods define how the moves are made, and what happens after moves are made.
	 * Short hand moves are in charge of just moving tiles (eg.lMove). These are recursive 
	 * Full moves (eg. leftMove) moves tiles if possible, add if necessary, then moves again
	 */
	/*****************************************/
	
	public boolean uMove()
	{
		int i, j;

		for(i=0; i < counts.length; i++)
		{
			for (j=0; j< 4; j++)//j=3; j>=0; j--
			{	
				if(counts[i][j] == 0)
				{
					if(i!=3 && counts[i+1][j] != 0)
					{
						counts[i][j] = counts[i+1][j];
						buttons[i][j].setText(counts[i+1][j] +"");
						counts[i+1][j] = 0;
						buttons[i+1][j].setText("");
						check = true;
						uMove();
					}
				}
				
				else if(counts[i][j] != 0)
				{
					if(i!= 0 && counts[i-1][j] == 0)
					{
						counts[i-1][j] = counts[i][j];
						buttons[i-1][j].setText(counts[i][j] +"");
						counts[i][j] = 0;
						buttons[i][j].setText("");
						check = true;
						uMove();
					}
				}
			}
		}
		
		changeColor();
		return check;	
	}
	
	public boolean upMove()
	{
		int i, j;
		
		check = uMove();
		
		//check
		for(i=0; i < counts.length; i++)
		{
			for (j= 0; j<counts[i].length; j++)//j=3; j>=0; j--
			{	
				if(counts[i][j] != 0)
				{
					if(i!= 0 && counts[i-1][j] == counts[i][j])
					{
						counts[i-1][j] = counts[i][j]*2;
						buttons[i-1][j].setText(counts[i][j]*2 +"");
						counts[i][j] = 0;
						buttons[i][j].setText("");
						check = true;
						totalScore+= (counts[i-1][j]);
						score.setText("Score: " + totalScore);
						High();
					}
				}
			}
		}
		
		check = uMove();
		changeColor();
		return check; //remember to reset check to be false after move has been made		
	}

	public boolean dMove()
	{
		int i, j;

		for(i=0; i < counts.length; i++)
		{
			for (j=0; j< 4; j++)//j=3; j>=0; j--
			{	
				if(counts[i][j] == 0)
				{
					if(i!=0 && counts[i-1][j] != 0)
					{
						counts[i][j] = counts[i-1][j];
						buttons[i][j].setText(counts[i-1][j] +"");
						counts[i-1][j] = 0;
						buttons[i-1][j].setText("");
						check = true;
						dMove();
					}
				}
				
				else if(counts[i][j] != 0)
				{
					if(i!= 3 && counts[i+1][j] == 0)
					{
						counts[i+1][j] = counts[i][j];
						buttons[i+1][j].setText(counts[i][j] +"");
						counts[i][j] = 0;
						buttons[i][j].setText("");
						check = true;
						dMove();
					}
				}
			}
		}
		
		changeColor();
		return check;
	}
	
	public boolean downMove()
	{
		int i, j;

		check = dMove();
		
		for(i=3; i>=0; i--)
		{
			for (j=0; j< counts.length; j++)
			{	
				if(counts[i][j] != 0)
				{
					if(i!= 3 && counts[i+1][j] == counts[i][j])
					{
						counts[i+1][j] = counts[i][j]*2;
						buttons[i+1][j].setText(counts[i][j]*2 +"");
						counts[i][j] = 0;
						buttons[i][j].setText("");
						check = true;
						totalScore+= (counts[i+1][j]);
						score.setText("Score: " + totalScore);
						High();
					}
				}
			}
		}
		
		check = dMove();
		changeColor();		
		return check; //remember to reset check to be false after move has been made
	}

	public boolean rMove() 
	{
		int i, j;

		for(i=0; i < counts.length; i++)
		{
			for (j=0; j< 4; j++)//j=3; j>=0; j--
			{	
				if(counts[i][j] == 0)
				{
					if(j!=0 && counts[i][j-1] != 0)
					{
						counts[i][j] = counts[i][j-1];
						buttons[i][j].setText(counts[i][j-1] +"");
						counts[i][j-1] = 0;
						buttons[i][j-1].setText("");
						check = true;
						rMove();
					}
				}
				
				else if(counts[i][j] != 0)
				{
					if(j!= 3 && counts[i][j+1] == 0)
					{
						counts[i][j+1] = counts[i][j];
						buttons[i][j+1].setText(counts[i][j] +"");
						counts[i][j] = 0;
						buttons[i][j].setText("");
						check = true;
						rMove();
					}
				}
			}
		}
		
		changeColor();
		return check;
	}
	
	public boolean rightMove()
	{		
		int i, j;

		check = rMove();
		
		//check
		for(i=0; i < counts.length; i++)
		{
			for (j=3; j>=0; j--)//j=3; j>=0; j--
			{	
				if(counts[i][j] != 0)
				{
					if(j!= 3 && counts[i][j+1] == counts[i][j])
					{
						counts[i][j+1] = counts[i][j]*2;
						buttons[i][j+1].setText(counts[i][j]*2 +"");
						counts[i][j] = 0;
						buttons[i][j].setText("");
						check = true;
						totalScore+= (counts[i][j+1]);
						score.setText("Score: " + totalScore);
						High();
					}
				}
			}
		}
		
		check = rMove();
		changeColor();
		return check;
}

	public boolean lMove()
	{
		int i, j;

		for(i=0; i < counts.length; i++)
		{
			for (j=0; j< 4; j++)//j=3; j>=0; j--
			{	
				if(counts[i][j] == 0)
				{
					if(j!=3 && counts[i][j+1] != 0)
					{
						counts[i][j] = counts[i][j+1];
						buttons[i][j].setText(counts[i][j+1] +"");
						counts[i][j+1] = 0;
						buttons[i][j+1].setText("");
						check = true;
						lMove();
					}
				}
				
				else if(counts[i][j] != 0)
				{
					if(j!= 0 && counts[i][j-1] == 0)
					{
						counts[i][j-1] = counts[i][j];
						buttons[i][j-1].setText(counts[i][j] +"");
						counts[i][j] = 0;
						buttons[i][j].setText("");
						check = true;
						lMove();
					}
				}
			}
		}
		
		changeColor();
		return check;	
	}
	
	public boolean leftMove()
	{	
		int i, j;

		check = lMove();
		
		//check
		for(i=0; i < counts.length; i++)
		{
			for (j= 0; j<counts.length; j++)//j=3; j>=0; j--
			{	
				if(counts[i][j] != 0)
				{
					if(j!= 0 && counts[i][j-1] == counts[i][j])
					{
						counts[i][j-1] = counts[i][j]*2;
						buttons[i][j-1].setText(counts[i][j]*2 +"");
						counts[i][j] = 0;
						buttons[i][j].setText("");
						check = true;
						totalScore+= (counts[i][j-1]);
						score.setText("Score: " + totalScore);
						High();
					}
				}
			}
		}
		
		check = lMove();
		changeColor();
		return check; //remember to reset check to be false after move has been made	
	}
	
	/************ Ending the Game ************/
	/* 
	 * checkLose: only checks if the loser lost and returns a boolean
	 * lose: uses checkLose in order to diplay the you lose box only once
	 * High: checks to see if we have a new highscore and rewrites our old one if we do
	 */
	/*****************************************/
	
	public boolean checkLose() //check if the user lost by check if there is a play available. (check logic)
	{
		//Each if statement may need an || to check if there is a blank space move available
		
		int i, j;
		
		for(i = 0; i < SIZE; i++)
		{
			for(j = 0; j < SIZE; j++)
			{
				if(counts[i][j] == 0)
				{
					return false;
				}
				
				else if(i!= 3 && counts[i+1][j] == counts[i][j]) // checks down
				{
					return false;	
				}
				
				else if(i!= 0 && counts[i-1][j] == counts[i][j]) // checks up
				{
					return false;
				}
				
				else if(j!= 0 && counts[i][j-1] == counts[i][j]) // checks left
				{
					return false;
				}
				
				else if(j!= 3 && counts[i][j+1] == counts[i][j]) // checks right
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean lose()
	{
		if (checkLose() && move)
		{									 
			High();
			JOptionPane.showMessageDialog(frame, "You Lost! Close and press space to restart");
			move = false;
			return true;
		}
		
		return false;
	}
	
	public void High() //creates/ rewrites highscore
	{
		try 
		{
			Scanner input = new Scanner(file);
			
			int HS = input.nextInt();
			
			if(totalScore > HS)
			{	
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(totalScore +"");
				hScore.setText("High score: "+ totalScore);
				bw.close();
			}
		} 
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		new Game();
	}
	
	/********* Keyboard inputs ***************/
	
	@Override
	public void keyPressed(KeyEvent event) //get keyboard input
	{
		// TODO Auto-generated method stub
		switch(event.getKeyCode())
		{
			case 32:
				//enter key to reset
				High();
				for(int i =0; i < counts.length; i++)
				{
					for(int j =0; j < counts.length; j++)
					{
						counts[i][j] = 0;
					}
				}
				saveBoard(); 
				
				for(int i =0; i < counts.length; i++)
				{
					for(int j =0; j < counts.length; j++)
					{
						buttons[i][j].setBackground(Color.GRAY);
						buttons[i][j].setEnabled(true);
						buttons[i][j].setText("");
					}
				}
				tilePlace();
				tilePlace();
				saveBoard();
				totalScore = 0;
				saveScore(0);
				score.setText("Score: 0");
				move = true;
				break;
				
			case 37:
				//left arrow
				if (lose() && move)
				{
					return;
				}
				
				else if(!checkLose() && leftMove())
				{
				    tilePlace();
                    changeColor();
                    saveBoard();
                    saveScore(totalScore);
                    check = false;
                    
                    if (lose())
    				{
    					return;
    				}
				}
				
				changeColor();
				saveBoard();
				break;
				
			case 38: 
				//up arrow
				if (lose() && move)
				{
					return;
				}
				
				else if(!checkLose() && upMove())
				{
				    tilePlace();
                    changeColor();
                    saveBoard();
                    saveScore(totalScore);
                    check = false;
                    
                    if (lose())
    				{
    					return;
    				}
				}
				
				changeColor();
				saveBoard();
				break;
				
			case 39:
				//right arrow
				if (lose() && move)
				{
					return;
				}
				
				else if(!checkLose() && rightMove()) //rightMove()
				{
					tilePlace();
                    changeColor();
                    saveBoard();
                    saveScore(totalScore);
                    check = false;
                    
                    if (lose())
    				{
    					return;
    				}
				}
				
				changeColor();
				saveBoard();
				break;
				
			case 40:
				//down arrow
				if (lose() && move)
				{
					return;
				}
												
				else if(!checkLose() && downMove())
				{
					tilePlace();
                    changeColor();
                    saveBoard();
                    saveScore(totalScore);
                    check = false;
                    
                    if (lose())
    				{
    					return;
    				}
				}
				
				changeColor();
				saveBoard();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent event) 
	{
		// TODO Auto-generated method stub
		
	}
}


