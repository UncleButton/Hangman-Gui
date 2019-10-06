
/*
 * welcome to the hangman gui, built by Britton M. Bailer!
 * Hope you like it!

As a general break down of the code, here are the main components with a brief description


menu: runs the menu page that will only send the player to the game page when they click play

game: runs the game page code, going through each individual game.  once game completes, player is sent to either youWin or gameOver

gameOver: runs the game over code when the player loses and cannot guess the word.  They can go to the menu or replay from here

youWin: runs the code for the win page when the player guesses the word correctly.  They can go to the menu or replay from here


METHODS

checkGuess is a hunk... Does a lot but its primary purpose is to check the players guess and update everything that needs updated

newGame simply resets all variables that need to be reset and begins a new game

drawHanger has the code in it that draws the hangman when it is called

drawLine works with drawHanger but is the animation part of the drawing process

EZText is a method that can take any text, its xy position, and put it on the canvas looking like pencil


*/

package hangman_gui;

import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;



public class Gui extends Application
{
	//global variables needed by all parts of the code
	static String[] charStrings = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	static Button[] buttons = new Button[26];
	static Button[] buttonsImgs = new Button[26];
	static char guess = 'a';
	
	static Text txtNumWrong = new Text();
	static Text txtGamesWon = new Text();
	static Text txtGamesLost = new Text();
	static Text txtWinPercent = new Text();
	static double winPercent = 0;
	static boolean getTempWord = true;
	static StringBuilder tempWord = new StringBuilder("");
	static int numWrong = 0;
	
	static List<String> words = new ArrayList<String>();
	
	static String chosenWord;
	
	static Pane left = new Pane();
	static Pane center = new Pane();
	static Pane right = new Pane();
	
	static HBox wordLetters = new HBox(-2);
	
	static int lineNum = 0;
	static int gamesWon = 0;
	static int gamesPlayed = 0;
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
							//  Now we start with the game components //
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	//where program starts... it just throws the user to the menu page
	public void start(Stage primaryStage) throws FileNotFoundException
	{
		menu(primaryStage);		
	}//end of start
	
	
	//the menu page...
	public static void menu(Stage primaryStage) throws FileNotFoundException
	{
		//set up the panes needed for the menu
		BorderPane borderPane = new BorderPane();
		Pane menuPane = new Pane();
		borderPane.setCenter(menuPane);
		
		//get the background image and change height and width
		Image mBackgroundImg = new Image(new File("images/menu_background_img.png").toURI().toString());
		ImageView menuBackground = new ImageView(mBackgroundImg);
		menuBackground.setFitWidth(800);
		menuBackground.setFitHeight(500);
		menuBackground.setX(0);
		menuBackground.setY(0);
		
		
		//create the play button (in the noose)
		Button btnPlay = new Button("Play");
		btnPlay.setStyle("-fx-background-radius: 60em;"+"-fx-min-width: 100px;"+"-fx-min-height: 100px;"+"-fx-background-color: transparent");
		btnPlay.setLayoutX(370);
		btnPlay.setLayoutY(260);
		btnPlay.setTextFill(Color.DEEPSKYBLUE);
		btnPlay.setFont(Font.font("Actor",30));
		
		//change color of "play" when mouse is over it
		btnPlay.setOnMouseEntered(e -> btnPlay.setTextFill(Color.RED));
		btnPlay.setOnMouseExited(e -> btnPlay.setTextFill(Color.DEEPSKYBLUE));
		
		//send player to the game when the play button is clicked
		btnPlay.setOnAction((event) -> {				
			try
			{
				newGame(primaryStage);
			} catch (FileNotFoundException e1)
			{
				System.out.println("Something went wrong!");
			}
		});			
		
		//add the button and background image (not in that order) to the pane and display!
		menuPane.getChildren().addAll(menuBackground,btnPlay);			
		Scene scene = new Scene(borderPane,800,500);
		primaryStage.setTitle("hangman");
		primaryStage.setScene(scene);
		primaryStage.show();	 
	}//end of menu
	
	
	//game over page!
	public static void gameOver(Stage primaryStage) throws FileNotFoundException
	{
		//again, set up panes
		BorderPane borderPane = new BorderPane();
		Pane GOPane = new Pane();
		borderPane.setCenter(GOPane);
		
		//get background image
		Image backgroundImg = new Image(new File("images/game_over_background_img.png").toURI().toString());
		ImageView GOBackgroundImg = new ImageView(backgroundImg);
		GOBackgroundImg.setFitWidth(800);
		GOBackgroundImg.setFitHeight(500);
		GOBackgroundImg.setX(0);
		GOBackgroundImg.setY(0);		
		
		//create the button to start a new game
		Button btnPlay = new Button();
		btnPlay.setLayoutX(160);
		btnPlay.setLayoutY(420);
		btnPlay.setPrefWidth(90);
		btnPlay.setPrefHeight(50);
		btnPlay.setStyle("-fx-background-color: transparent");
		
		//change background color when mouse is over it
		btnPlay.setOnMouseEntered(e -> btnPlay.setStyle("-fx-background-color: #55555555"));
		btnPlay.setOnMouseExited(e -> btnPlay.setStyle("-fx-background-color: transparent"));
		
		//send to game when player clicks on button
		btnPlay.setOnAction((event) -> {				
			try
			{
				newGame(primaryStage);
			} catch (FileNotFoundException e1)
			{
				System.out.println("Something went wrong!");
			}
		});	
		
		//set up button to send player to the menu
		Button btnMenu = new Button();
		btnMenu.setLayoutX(490);
		btnMenu.setLayoutY(415);
		btnMenu.setPrefWidth(100);
		btnMenu.setPrefHeight(55);
		btnMenu.setStyle("-fx-background-color: transparent");
		
		//change background color of button when mouse is over it
		btnMenu.setOnMouseEntered(e -> btnMenu.setStyle("-fx-background-color: #55555555"));
		btnMenu.setOnMouseExited(e -> btnMenu.setStyle("-fx-background-color: transparent"));
		
		//send player to menu when the button is clicked!
		btnMenu.setOnAction((event) -> {				
			try
			{
				menu(primaryStage);
			} catch (FileNotFoundException e1)
			{
				System.out.println("Something went wrong!");
			}
		});	
		
		//add all those things to the pane
		GOPane.getChildren().addAll(GOBackgroundImg,btnPlay,btnMenu);
		
		//Creates variables for showing the word that the player was unable to guess
		String tcw = chosenWord;
		Image[] letterImg = new Image[tcw.length()];
		ImageView[] lettersImg = new ImageView[tcw.length()];
		
		//loops through the word so as to display all the letter images that spell it out!
		for(int i = 0; i<tcw.length(); i++)
	    {
			letterImg[i] = new Image(new File("images/"+tcw.substring(i,i+1)+".png").toURI().toString());
			lettersImg[i] = new ImageView(letterImg[i]);
			
			lettersImg[i].setX(i*23+440);
			lettersImg[i].setY(230);
			
			//add letter images to the pane
		    GOPane.getChildren().add(lettersImg[i]);
	    }
		
		
		//display the pane!
		Scene scene = new Scene(borderPane,800,500);
		primaryStage.setTitle("hangman");
		primaryStage.setScene(scene);
		primaryStage.show();
	}//end of gameOver
	
	
	//game won page!
	public static void youWin(Stage primaryStage) throws FileNotFoundException
	{
		//you got it! create panes again!
		BorderPane borderPane = new BorderPane();
		Pane YWPane = new Pane();
		borderPane.setCenter(YWPane);
		
		//as per usual, create a background image
		Image backgroundImg = new Image(new File("images/win_background_img.png").toURI().toString());
		ImageView winBackgroundImg = new ImageView(backgroundImg);
		winBackgroundImg.setFitWidth(800);
		winBackgroundImg.setFitHeight(500);
		winBackgroundImg.setX(0);
		winBackgroundImg.setY(0);	
		
		
		//create a play button (to send back to the game)
		Button btnPlay = new Button();
		btnPlay.setLayoutX(160);
		btnPlay.setLayoutY(420);
		btnPlay.setPrefWidth(90);
		btnPlay.setPrefHeight(50);
		btnPlay.setStyle("-fx-background-color: transparent");
		
		//change background color when player hovers mouse over button
		btnPlay.setOnMouseEntered(e -> btnPlay.setStyle("-fx-background-color: #55555555"));
		btnPlay.setOnMouseExited(e -> btnPlay.setStyle("-fx-background-color: transparent"));
		
		//send player to the game when button is pressed
		btnPlay.setOnAction((event) -> {				
			try
			{
				newGame(primaryStage);
			} catch (FileNotFoundException e1)
			{
				System.out.println("Something went wrong!");
			}
		});	
		
		//create a button to get back to the menu!
		Button btnMenu = new Button();
		btnMenu.setLayoutX(490);
		btnMenu.setLayoutY(415);
		btnMenu.setPrefWidth(100);
		btnMenu.setPrefHeight(55);
		btnMenu.setStyle("-fx-background-color: transparent");
		
		//change background color of button when player hovers over it
		btnMenu.setOnMouseEntered(e -> btnMenu.setStyle("-fx-background-color: #55555555"));
		btnMenu.setOnMouseExited(e -> btnMenu.setStyle("-fx-background-color: transparent"));
		
		//send player to menu if they click the button
		btnMenu.setOnAction((event) -> {				
			try
			{
				menu(primaryStage);
			} catch (FileNotFoundException e1)
			{
				System.out.println("Something went wrong!");
			}
		});	
		
		//add all that to the pane
		YWPane.getChildren().addAll(winBackgroundImg,btnPlay,btnMenu);
		
		
		//create variables to display the word the player guessed... kinda redundant, but what evs
		String tcw = chosenWord;
		Image[] letterImg = new Image[tcw.length()];
		ImageView[] lettersImg = new ImageView[tcw.length()];
		
		//loop through the word so that we can show the images for each letter and spell out the word
		for(int i = 0; i<tcw.length(); i++)
	    {
			letterImg[i] = new Image(new File("images/"+tcw.substring(i,i+1)+".png").toURI().toString());
			lettersImg[i] = new ImageView(letterImg[i]);
			
			lettersImg[i].setX(i*23+250);
			lettersImg[i].setY(290);
			
			//add the images to the pane
			YWPane.getChildren().add(lettersImg[i]);
	    }
		
		//display pane!
		Scene scene = new Scene(borderPane,800,500);
		primaryStage.setTitle("hangman");
		primaryStage.setScene(scene);
		primaryStage.show();
	}//end of youWin
	
	
	//the fun begins! welcome to the game!
	public static void game(Stage primaryStage)  throws FileNotFoundException
	{
		//create the pane
		BorderPane borderPane = new BorderPane();
		
		//variable to hold the file name
		String file = "";
		
		//try the file and see if it is a valid file... if it cannot be found, exit gracefully
		try
		{		
			file = "hangman.txt";
			Scanner wordsFileCheck = new Scanner(new File(file));
		    wordsFileCheck.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.print("I'm sorry, the file could not be found!");
			System.exit(0);
		}
	    
		Scanner wordsFile = new Scanner(new File(file));
	    
	    while (wordsFile.hasNext())
	    	words.add(wordsFile.next());
	    
	    //no need to keep it open
	    wordsFile.close();
	    
	    //chooses a random element number from the words array
	    int wordNum = (int)Math.round(Math.random()*(words.size()-1));
	    
	    //populate "tempWord" with asterisks because the word hasnt been guessed yet silly!
	    if(getTempWord)
	    {
	    	for(int i = 0; i<words.get(wordNum).length(); i++)
	    	{
	    		tempWord.append('*');
	    	}
	    	getTempWord=false;
	    }
	    
	    //two arrays of characters... the master and the empty for display
	    char[] WORD_CHARS = words.get(wordNum).toCharArray();
	    char[] displayedChars = new char[WORD_CHARS.length];
	    
	    
	    //sets chosenWord equal to the... well, chosen word
	    chosenWord = words.get(wordNum);
	    
	    //populates the displayedChars with *
	    for(int i = 0; i<displayedChars.length; i++)
	    {
	    	displayedChars[i]='*';
	    }
	    
	    
	    //get the background image
	    Image backgroundImg = new Image(new File("images/game_background_img.png").toURI().toString());
		ImageView paperImg = new ImageView(backgroundImg);
		paperImg.setX(0);
		paperImg.setY(0);
		paperImg.setFitWidth(800);
		paperImg.setFitHeight(500);
		
		//get the image for the box that the guessing letter buttons go into
		Image lettersBox = new Image(new File("images/letters_box_img2.png").toURI().toString());
		ImageView lettersBoxImg = new ImageView(lettersBox);
		lettersBoxImg.setX(-5);
		lettersBoxImg.setY(143);
		lettersBoxImg.setFitWidth(200);
		lettersBoxImg.setFitHeight(250);
		
		//add those two to the LEFT pane
		left.getChildren().addAll(paperImg,lettersBoxImg);
		
		//create an image for the asterisk (star)
	    Image starImg = new Image(new File("images/star.png").toURI().toString());
	    
	    //populate the word buttons with asterisks (to show this time)
	    for(int i = 0; i<chosenWord.length(); i++)
	    	wordLetters.getChildren().add(new ImageView(starImg));
	    
		
		StringBuilder tempWord = new StringBuilder();
		for(int i = 0; i<displayedChars.length;i++)
			tempWord.append("*");
	    
		wordLetters.setLayoutX((400-displayedChars.length*25)/2);
		wordLetters.setLayoutY(390);
		center.getChildren().add(wordLetters);
				
		/////////////////////////////// some stats per request ////////////////////////////////////////////
		
		//number of letters guessed wrong for this word
		EZText(txtNumWrong,""+numWrong,110,175);
	    
	    //games you have won :)
	    EZText(txtGamesWon,""+gamesWon,90,218);
	    
	    //games you have lost :'(
	    EZText(txtGamesLost,""+(gamesPlayed-gamesWon),95,261);
	    
	    //win percentage
	    EZText(txtWinPercent,""+(int)Math.round(winPercent)+"%",100,301);
	    
	    //and add all those stats to the RIGHT pane
	    right.getChildren().addAll(txtNumWrong,txtGamesWon,txtGamesLost,txtWinPercent);	
	    
	    
	    //set panes in their respective places...check out the creative names ;P
		borderPane.setLeft(left);
		borderPane.setCenter(center);
		borderPane.setRight(right);
		
		//change size of all the panes so it looks pwetty
		left.setPrefWidth(200);
		right.setPrefWidth(200);		
		
		
		//ahhh, the letter buttons
		for(int i = 0; i<26;i++)
		{
			//rows and collumns of letter buttons on left panel
			int btnY = (int) (180+Math.ceil(i/5)*30);
			int btnX = 20+(i%5)*30;
			
			//create the buttons! (and set background to transparent cuz who wants to see buttons anyway?)			
			buttons[i]=new Button();
			buttons[i].setStyle("-fx-background-color: transparent");
			
			//oh, and same deal here... these are for the check or scribble out of letters when pressed though
			buttonsImgs[i]=new Button();
			buttonsImgs[i].setStyle("-fx-background-color: transparent");
			
			// ugh, had to create this variable so i could reference specific buttons inside the event
			// events in java cannot have static variables in them and therefore each iteration of the loop recreates "finI"
			// which is just i as a final variable
			final int finI = i;
			
			// when the mouse enters a button, change the background color to highlighter yellow (to show the player they
			// are hovering over that button
			buttons[i].setOnMouseEntered(e ->
			{
				if(!buttons[finI].isDisabled())
				{
					buttons[finI].setStyle("-fx-background-color: #f4f44288");
				}
			});
			//when the mouse exits the button return the background color to transparent
			buttons[i].setOnMouseExited(e -> 
			{
				if(!buttons[finI].isDisabled())
				{
					buttons[finI].setStyle("-fx-background-color: transparent");
				}
			});				
			
			//add those to the pane
			left.getChildren().addAll(buttonsImgs[i],buttons[i]);
			
			//set button width, x, and y
			buttons[i].setMinWidth(25);
			buttons[i].setLayoutX(btnX);
			buttons[i].setLayoutY(btnY);
			
			//same here minus width
			buttonsImgs[i].setLayoutX(btnX-3);
			buttonsImgs[i].setLayoutY(btnY);
			
		}
		
		//here is where we do stuffs when the buttons are clicked
		//ye be warned... here be monsters
		for(int i = 0; i<buttons.length; i++)
		{
			//again, create dummy variable to pass into event
			final int index = i;
			
			buttons[i].setOnAction((event) -> {
				//set guess equal to the char on the button the player presses
				getGuess(index);
				
				//check the guess against the word
				try
				{
					checkGuess(guess, index, primaryStage);
				} 
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				
				//disable the button that was pressed
				buttons[index].setDisable(true);
			});
		}
		
		//set scenes and DISPLAY
		Scene scene = new Scene(borderPane,800,500);
		primaryStage.setTitle("hangman");
		primaryStage.setScene(scene);
		primaryStage.show();
	}//end of game
	
	
	//starts the program
	public static void main(String[] args)
	{
		launch(args);
	}
	
	
	//does what i said it does a few lines back
	static void getGuess(int i)
	{
		guess=charStrings[i].charAt(0);
	}
	
	//this is a beast of a method
	static void checkGuess(char guess, int index, Stage primaryStage) throws FileNotFoundException
	{
		//create some variables
		char[] realWord = chosenWord.toString().toCharArray();
		StringBuilder shownWord = new StringBuilder();
		boolean isWrong = true;
		
		StringBuilder realWordString = new StringBuilder();
		for(int i = 0; i<realWord.length;i++)
			realWordString.append(realWord[i]);
		
		//check the guess against the word they be trying to guess
		for(int i = 0; i<realWord.length; i++)
		{
			//if char matches, show the player
			if(guess==realWord[i])
			{
				shownWord.append(guess);
				isWrong=false;
			}
			else//if not, show the player... a star... again.  OR show the player another letter if they already guessed it 
			{
				if(tempWord.charAt(i)=='*')
					shownWord.append('*');
				else
					shownWord.append(tempWord.charAt(i));
			}
			
		}
		
		//get the scribble out image
		Image wrongBox = new Image(new File("images/disabled_box_img.png").toURI().toString());
		ImageView wrongBoxImg = new ImageView(wrongBox);
		wrongBoxImg.setFitWidth(20);
		wrongBoxImg.setFitHeight(20);
		
		//get the check image
		Image rightBox = new Image(new File("images/correct_box_img.png").toURI().toString());
		ImageView rightBoxImg = new ImageView(rightBox);
		rightBoxImg.setFitWidth(20);
		rightBoxImg.setFitHeight(20);
		
		//if the guess was wrong, do dis
		if(isWrong)
		{
			numWrong++;
			txtNumWrong.setText(""+numWrong);
			
			//scrible over letter button
			buttons[index].setStyle("-fx-background-color: transparent;");
			buttonsImgs[index].setGraphic(wrongBoxImg);
			
			//these two things draw the hanger for hanging the hangee
			lineNum=0;
			drawHanger(200,300, numWrong-1);
			
			//if this was their last try... send em to game over, MWA HA HA HA HA!!!!
			if(numWrong>9)
			{
				gamesPlayed++;
				winPercent=((double)gamesWon/(double)gamesPlayed)*100;
				gameOver(primaryStage);
			}
		}
		else
		{
			//put a check box on the letter button
			buttons[index].setStyle("-fx-background-color: transparent;");
			buttonsImgs[index].setGraphic(rightBoxImg);
		}
		
		//actually set tempWord to what we just did with the stars and letters
		tempWord = new StringBuilder(shownWord);
		
		//and here, we loop through it and show the "hand-drawn" letters to the player
		wordLetters.getChildren().clear();
		for(int i = 0; i<tempWord.length(); i++)
		{
			if(!tempWord.substring(i,i+1).equals(new String("*")))
			{
				Image charImg = new Image(new File("images/"+tempWord.substring(i,i+1)+".png").toURI().toString());
				ImageView charImgs = new ImageView(charImg);
				wordLetters.getChildren().add(charImgs);
			}
			else
			{
				//or we show another star where letters have gone unguessed
				Image charImg = new Image(new File("images/star.png").toURI().toString());
				ImageView charImgs = new ImageView(charImg);
				wordLetters.getChildren().add(charImgs);
			}
			
		}
		
		
		//and finally, check to see if the player completed the word!
		if(tempWord.toString().equals(realWordString.toString()))
		{
			gamesPlayed++;
			gamesWon++;
			winPercent=((double)gamesWon/(double)gamesPlayed)*100;
			
			//send to the you win page! gg!
			youWin(primaryStage);
		}
	}//end of checkGuess
	
	//reset variables and start a new game method
	public static void newGame(Stage primaryStage) throws FileNotFoundException
	{
		txtNumWrong = new Text();
		
		getTempWord = true;
		tempWord = new StringBuilder("");
		numWrong = 0;
		
		words = new ArrayList<String>();
		
		chosenWord = "";
		
		left = new Pane();
		center = new Pane();
		right = new Pane();
		
		lineNum = 0;
		
		wordLetters.getChildren().clear();
		
		game(primaryStage);
	}
	
	//here we draw the hanger that hangs the hangee
	public static void drawHanger(int x, int y, int i)
	{
		//the line segments that make up the hanger
		int[][][] testing = {
				{{-100,0,100,0}},
				{{-40,-200,-40,0}},
				{{-40,-200,40,-200}},
				{{40,-200,40,-170}},
				{{40,-170,50,-160},{50,-160,50,-150},{50,-150,40,-140},{40,-140,30,-150},{30,-150,30,-160},{30,-160,40,-170}},
				{{40,-140,40,-80}},
				{{40,-80,60,-50}},
				{{40,-80,20,-50}},
				{{40,-120,60,-130}},
				{{40,-120,20,-130}},
		};	
		
		//just draws it... nothing fancy
		while(lineNum<testing[i].length)
		{
			Node tests = drawLine(x+testing[i][lineNum][0],y+testing[i][lineNum][1],x+testing[i][lineNum][2],y+testing[i][lineNum][3]);		
			center.getChildren().add(tests);
			lineNum++;
		}
		
	}
	
	//here we actually do the animation that makes the hanger that hangs the hangee (almost) look drawn
	public static Node drawLine(int x1, int y1, int x2, int y2)
	{
		//create the pane for animations
        Pane animationPane = new Pane();
        animationPane.setPrefSize(500,200);

        //create the line with a bit of randomness
        Line line = new Line(x1+Math.round(Math.random()*2)-1, y1+Math.round(Math.random()*2)-1, x1, y1);
        line.setStroke(Color.rgb(127, 127, 127));
        line.setStrokeWidth(2);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        
        //add line to the animation pane
        animationPane.getChildren().add(line);

        //create a timeline to allow movement
        Timeline timeline = new Timeline();
        
        //only run thru once
        timeline.setCycleCount(1);

        //just stuff to say what is moving and where to
        KeyValue kVMoveX = new KeyValue(line.endXProperty(), x2+Math.round(Math.random()*2)-1);
        KeyValue kVMoveY = new KeyValue(line.endYProperty(), y2+Math.round(Math.random()*2)-1);
        
        //run for 200 milliseconds
        KeyFrame keyFrame = new KeyFrame(Duration.millis(200), kVMoveX, kVMoveY);

        //add keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);        
        
        //play the animation
      	timeline.play();
      	
        return animationPane;
	}
	
	public static void EZText(Text txtName, String text, int x, int y)
	{
		txtName.setText(text);
		txtName.setX(x);
		txtName.setY(y);
		txtName.setFill(Color.rgb(150,150,150));
		txtName.setStyle("-fx-font-size: 2em");
	}
	
}// FIN

