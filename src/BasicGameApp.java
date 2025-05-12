//Basic Game Application
//Version 2
// Basic Object, Image, Movement
// Astronaut moves to the right.
// Threaded

//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section


//Step 1: add KeyListener to the implements
public class BasicGameApp implements Runnable, KeyListener {

   //Variable Definition Section
   //Declare the variables used in the program 
   //You can set their initial values too
   
   //Sets the width and height of the program window
	final int WIDTH = 1000;
	final int HEIGHT = 700;

   //Declare the variables needed for the graphics
	public JFrame frame;
	public Canvas canvas;
   public JPanel panel;
   
	public BufferStrategy bufferStrategy;
	public Image bronnnypic;
	public Image zrichpic;
	public Image background;
	public Image endScreen;
	public Image gunPowerUp;
	public Image deadPic;

   //Declare the objects used in the program
   //These are things that are made up of more than one variable type
	private Baller bronny;
	private Baller powerUp;

	//step 1:make opponent array and say how big it is
	Baller [] opponentArray = new Baller[10];

   // Main method definition
   // This is the code that runs first and automatically
	public static void main(String[] args) {
		BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
		new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method  
	}


   // Constructor Method
   // This has the same name as the class
   // This section is the setup portion of the program
   // Initialize your variables and construct your program objects here.
	public BasicGameApp() {

      
      setUpGraphics();
       
      //variable and objects
      //create (construct) the objects needed for the game and load up
		bronnnypic = Toolkit.getDefaultToolkit().getImage("bronny.png"); //load the picture
		bronny = new Baller(10,10);
		powerUp = new Baller(500,500);
		gunPowerUp = Toolkit.getDefaultToolkit().getImage("gunpowerup.png");
		endScreen = Toolkit.getDefaultToolkit().getImage("endscreen.png");
		for(int x = 0; x< opponentArray.length; x++){
			opponentArray[x] = new Baller((int)(Math.random()*800)+200,(int)(Math.random()*500)+200);
			opponentArray[x].dx = (int)(Math.random()*10);
			opponentArray[x].dy = 5;
		}
		zrichpic = Toolkit.getDefaultToolkit().getImage("zrich.png"); //load the picture

		background = Toolkit.getDefaultToolkit().getImage("bronincar.jpg"); //load the picture
		deadPic = Toolkit.getDefaultToolkit().getImage("x.png"); //load the picture


	}// BasicGameApp()

   
//*******************************************************************************
//User Method Section
//
// put your code to do things here.

   // main thread
   // this is the code that plays the game after you set things up
	public void run() {

      //for the moment we will loop things forever.
		while (true) {

         moveThings();  //move all the game objects
         render();  // paint the graphics
         pause(20); // sleep for 10 ms
		}
	}


	public void moveThings()
	{
      //calls the move( ) code in the objects
		bronny.move();
		collisions();
		for(int x = 0; x< opponentArray.length; x++){ //setting a random number which will determine whether each opponent bounces or moves.
			opponentArray[x].randNum = (int) (Math.random()*2);
		}
		for(int x = 0; x< opponentArray.length; x++){
			if(opponentArray[x].randNum == 0){
				opponentArray[x].move();
				opponentArray[x].bounce();
			}
			else if(opponentArray[x].randNum == 1){
				opponentArray[x].move();
				opponentArray[x].wrap();
			}

		}

	}
	public void collisions(){
		if (bronny.powered){ //enabling the shooter method when the player gets the power up
			shooter();
		}

		for(int b = 0; b< opponentArray.length; b++){ //if bronny intersects with the opponent, set bronny.stillAlive to false
			if(bronny.rec.intersects(opponentArray[b].rec)){
				System.out.println("crashing ");
				bronny.stillAlive = false;
			}
		}
		if(bronny.rec.intersects(powerUp.rec)){ //gives bronny powers when he touches the power up

			bronny.powered = true;

		}

	}
	public void shooter(){
		for(int x = 0; x< opponentArray.length; x++) {
			double equaish1 = bronny.xpos - opponentArray[x].xpos;
			equaish1 = equaish1 * equaish1;
			double equaish2 = bronny.ypos - opponentArray[x].ypos;
			equaish2 = equaish2 * equaish2;
			double equaish3 = equaish1 + equaish2;
			double equaish4 = Math.sqrt(equaish3);
			//this entire previous segment is basically using the distance formula to figure out how far each opponent is away.
			if (equaish4 < bronny.dist) { //determines if the previously determined distance is the smallest and if so takes note of which astronaut it is.
				bronny.dist = (int) equaish4;
				bronny.closestguy = x;
			}

		}
		if(bronny.shot == true){ //when bronny shoots, the closest guy stops before bronny loses his powers.
			opponentArray[bronny.closestguy].dy = 0;
			opponentArray[bronny.closestguy].dx = 0;
			bronny.shot = false;
			bronny.powered = false;

			bronny.dist =  100000;
		}


	}
	
   //Pauses or sleeps the computer for the amount specified in milliseconds
   public void pause(int time ){
   		//sleep
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {

			}
   }

   //Graphics setup method
   private void setUpGraphics() {
      frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.
   
      panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
      panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
      panel.setLayout(null);   //set the layout
   
      // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
      // and trap input events (Mouse and Keyboard events)
      canvas = new Canvas();  
      canvas.setBounds(0, 0, WIDTH, HEIGHT);
      canvas.setIgnoreRepaint(true);

	  //Step 2: add key listener to canvas as this
	   canvas.addKeyListener(this);
   
      panel.add(canvas);  // adds the canvas to the panel.
   
      // frame operations
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
      frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
      frame.setResizable(false);   //makes it so the frame cannot be resized
      frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!
      
      // sets up things so the screen displays images nicely.
      canvas.createBufferStrategy(2);
      bufferStrategy = canvas.getBufferStrategy();
      canvas.requestFocus();
      System.out.println("DONE graphic setup");
   
   }


	//paints things on the screen using bufferStrategy
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);

      //draw the image of the background, the power up, bronny, the opponents, and the end screen.
		if(bronny.stillAlive){
			g.drawImage(background, 0, 0, WIDTH, HEIGHT,null);
		}
		if(bronny.powered == false){
			g.drawImage(gunPowerUp, (int) powerUp.xpos, (int) powerUp.ypos, powerUp.width, powerUp.height, null);
		}
		g.drawImage(bronnnypic, (int) bronny.xpos, (int) bronny.ypos, bronny.width, bronny.height, null);
		//step 4: render bronny array
		for(int z = 0; z < opponentArray.length; z++){
				g.drawImage(zrichpic, (int) opponentArray[z].xpos, (int) opponentArray[z].ypos, bronny.width, bronny.height, null);
		}
		if(!bronny.stillAlive){
			g.drawImage(endScreen, 0, 0, WIDTH, HEIGHT,null);
		}





		g.dispose();

		bufferStrategy.show();
	}

	@Override
	public void keyTyped(KeyEvent e) { //Don't use this one I don't know what it does

	}

	@Override
	public void keyPressed(KeyEvent e) {

		System.out.println("Pressed");
		System.out.println(e.getKeyChar());
		System.out.println(e.getKeyCode());
		//hw: Identify the key codes for up, down, left, and right arrow keys.
		if(e.getKeyCode() == 47 && bronny.powered){
			bronny.shot = true;
		}
		if(e.getKeyCode() == 38){ //when the up key is pressed, bronny goes up
			System.out.println("Going up");
			bronny.left = false;
			bronny.right = false;
			bronny.down = false;
			bronny.up = true;
		}
		if(e.getKeyCode() == 37){ //when the left key is pressed, bronny goes left
			System.out.println("Going left");
			bronny.left = true;
		}
		if(e.getKeyCode() == 39){
			System.out.println("Going right"); //when the right key is pressed, bronny goes right
			bronny.right = true;
			bronny.left = false;
			bronny.up = false;
			bronny.down = false;


		}
		if(e.getKeyCode() == 40){ //when the down key is pressed, bronny goes down
			System.out.println("Going down");
			bronny.down = true;
			bronny.left = false;
			bronny.right = false;
			bronny.up = false;

		}
		if(bronny.up == true && bronny.left == true){ //making diagonals
			bronny.dx = -5;
			bronny.dy = -5;

		}
		if(bronny.down == true && bronny.left == true){
			bronny.dx = -5;
			bronny.dy = 5;

		}


	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("released");
		System.out.println(e.getKeyCode());
		if(e.getKeyCode() == 38){ //stopping bronny when the key is no longer pressed
			bronny.up = false;
			bronny.dy = 0;
		}
		if(e.getKeyCode() == 40){
			bronny.down = false;
			bronny.dy = 0;
		}
		if(e.getKeyCode() == 37){
			bronny.left = false;
			bronny.dx = 0;
		}
		if(e.getKeyCode() == 39){
			bronny.right = false;
			bronny.dx = 0;
		}

	}
	//Step 3: add methods keyReleased, keyPressed, and keyTyped
}