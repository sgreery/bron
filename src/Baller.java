import java.awt.*;

/**
 * Created by chales on 11/6/2017.
 */
public class Baller {

    //VARIABLE DECLARATION SECTION
    //Here's where you state which variables you are going to use.
    public String name;                //holds the name of the hero
    public double xpos;                //the x position
    public double ypos;                //the y position
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;
    public int height;
    public boolean isAlive;            //a boolean to denote if the hero is alive or dead.
    public boolean left; //whether the player moves left, right, up, down
    public boolean up;
    public boolean right;
    public boolean down;
    public Rectangle rec; //adds rectangles to each entity to enable reactions for collisions
    boolean stillAlive = true; //test if the player hasn't been killed
    int dist = 1000000000; //the distance to the closest opponent
    boolean powered; //test if the player has gotten the power up
    int closestguy; //decides which opponent from the array is closest
    boolean shot = false; //if the player has shot
    int randNum; //random number to determine between bouncing or wrapping

    // METHOD DEFINITION SECTION

    // Constructor Definition
    // A constructor builds the object when called and sets variable values.


    //This is a SECOND constructor that takes 3 parameters.  This allows us to specify the hero's name and position when we build it.
    // if you put in a String, an int and an int the program will use this constructor instead of the one above.
    public Baller(int pXpos, int pYpos) {
        xpos = pXpos;
        ypos = pYpos;
        dx =0;
        dy =0;
        width = 60;
        height = 60;
        isAlive = true;
        up = false;
        down= false;
        left = false;
        right = false;
        rec = new Rectangle((int) xpos, (int) ypos, width, height);
 
    } // constructor

    //The move method.  Everytime this is run (or "called") the hero's x position and y position change by dx and dy
    public void move() {

        if(up == true){
            dy = -10;
//            dx = 0;
        }
        if(down == true){
            dy = 10;
//            dx = 0;
        }
        if(left == true){
            dx = -10;
//            dy = 0;
        }
        if(right == true){
            dx = 10;
//            dy = 0;
        }
        xpos = xpos + dx;
        ypos = ypos + dy;
        rec = new Rectangle((int) xpos, (int) ypos, width, height);
 
    }
    public void bounce(){ //bounce method flips the character's dx when hitting side borders and flips the character's dy when hitting the vertical borders.
        if(xpos>1000){
            dx=-dx;
        }
        if(ypos>700){
            dy=-dy;
        }
        if(xpos<0){
            dx=-dx;
        }
        if(ypos<0){
            dy=-dy;
        }
        rec = new Rectangle((int) xpos, (int) ypos, width, height);
    }
    public void wrap(){ //sends character to other side of the screen
        if (xpos>1000){
            xpos=xpos-1000;

        }
        if (ypos>700){
            ypos=ypos-700;

        }
        if (xpos<0){
            xpos=xpos+900;


        }
        if (ypos<0){
            ypos=ypos+600;



        }
        rec = new Rectangle((int) xpos, (int) ypos, width, height);
    }
}






