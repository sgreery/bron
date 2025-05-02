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
	public Image astroPic;
	public Image zrichpic;
	public Image background;
	public Image endScreen;
	public Image gunPowerUp;

   //Declare the objects used in the program
   //These are things that are made up of more than one variable type
	private Astronaut astro;
	private Astronaut powerUp;

	//step 1:make astro array and say how big it is
Astronaut [] astronautsArray = new Astronaut[10];

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
		astroPic = Toolkit.getDefaultToolkit().getImage("bronny.png"); //load the picture
		astro = new Astronaut(10,10);
		powerUp = new Astronaut(500,500);
		gunPowerUp = Toolkit.getDefaultToolkit().getImage("gunpowerup.png");
		endScreen = Toolkit.getDefaultToolkit().getImage("endscreen.png");
		for(int x = 0; x< astronautsArray.length; x++){
			astronautsArray[x] = new Astronaut((int)(Math.random()*900)+100,(int)(Math.random()*600)+100);
			astronautsArray[x].dx = (int)(Math.random()*10);
			astronautsArray[x].dy = 5;
		}
		zrichpic = Toolkit.getDefaultToolkit().getImage("zrich.png"); //load the picture

		background = Toolkit.getDefaultToolkit().getImage("bronincar.jpg"); //load the picture


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
		astro.move();
		//step 3: move astro array
		for(int y=0; y< astronautsArray.length; y++){
			astronautsArray[y].move();
			astronautsArray[y].bounce();
		}
		collisions();

	}
	public void collisions(){
		if (astro.powered){
			shooter();
		}

		for(int b = 0; b< astronautsArray.length; b++){
			if(astro.rec.intersects(astronautsArray[b].rec)){
				System.out.println("crashing ");
				astro.stillAlive = false;
			}
		}
		if(astro.rec.intersects(powerUp.rec)){
			astro.pewpew = true;
			astro.powered = true;

		}
	}
	public void shooter(){
		for(int x = 0; x<astronautsArray.length; x++) {
			double equaish1 = astro.xpos - astronautsArray[x].xpos;
			equaish1 = equaish1 * equaish1;
			double equaish2 = astro.ypos - astronautsArray[x].ypos;
			equaish2 = equaish2 * equaish2;
			double equaish3 = equaish1 + equaish2;
			double equaish4 = Math.sqrt(equaish3);
			if (equaish4 < astro.dist) {
				astro.dist = (int) equaish4;
				astro.closestguy = x;
			}

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

      //draw the image of the astronaut
		if(astro.stillAlive){
			g.drawImage(background, 0, 0, WIDTH, HEIGHT,null);
		}
		if(astro.powered == false){
			g.drawImage(gunPowerUp, (int) powerUp.xpos, (int) powerUp.ypos, powerUp.width, powerUp.height, null);
		}
		g.drawImage(astroPic, (int) astro.xpos, (int) astro.ypos, astro.width, astro.height, null);
		//step 4: render astro array
		for(int z = 0; z < astronautsArray.length; z++){
			g.drawImage(zrichpic, (int) astronautsArray[z].xpos, (int) astronautsArray[z].ypos, astro.width, astro.height, null);
		}
		if(!astro.stillAlive){
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
		if(e.getKeyCode() == 16 && astro.powered){
			astro.shot == true;
		}
		if(e.getKeyCode() == 38){
			System.out.println("Going up");
			astro.left = false;
			astro.right = false;
			astro.down = false;
			astro.up = true;
		}
		if(e.getKeyCode() == 37){
			System.out.println("Going left");
			astro.left = true;
		}
		if(e.getKeyCode() == 39){
			System.out.println("Going right");
			astro.right = true;
			astro.left = false;
			astro.up = false;
			astro.down = false;


		}
		if(e.getKeyCode() == 40){
			System.out.println("Going down");
			astro.down = true;
			astro.left = false;
			astro.right = false;
			astro.up = false;

		}
		if(astro.up == true && astro.left == true){
			astro.dx = -5;
			astro.dy = -5;

		}
		if(astro.down == true && astro.left == true){
			astro.dx = -5;
			astro.dy = 5;

		}

		if(e.getKeyCode() == 83){
			astro.up = false;
			astro.down = false;
			astro.left = false;
			astro.right = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("released");
		System.out.println(e.getKeyCode());
		if(e.getKeyCode() == 38){
			astro.up = false;
			astro.dy = 0;
		}
		if(e.getKeyCode() == 40){
			astro.down = false;
			astro.dy = 0;
		}
		if(e.getKeyCode() == 37){
			astro.left = false;
			astro.dx = 0;
		}
		if(e.getKeyCode() == 39){
			astro.right = false;
			astro.dx = 0;
		}

	}
	//Step 3: add methods keyReleased, keyPressed, and keyTyped
}