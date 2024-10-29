import java.awt.*;
import java.awt.event.*;
import java.nio.channels.Pipe;
import java.util.ArrayList;  // this is going to store all the pipes in our game
import java.util.Random;  // this is used for placing our pipes at random positions
import javax.swing.*;

// adding two interfaces action and keylistner
public class FlappyBird extends JPanel implements ActionListener , KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    // these 4 variables are going to store our image objects 
    Image backgroudImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //bird

    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdheight = 24;

   // just to make it easier we will make a class to contain all
   // bird class

   class Bird {

       int x = birdX;
       int y = birdY;
       int width = birdWidth;
       int height = birdheight;
       Image img;
       
       // constructor
       Bird(Image img){
        this.img = img;   // we can change the bird according to uss with this

       }
   }
   // pipes
   int pipeX = boardWidth; // this is going to be on the right side of the board
   int pipeY = 0; // so the pipe is going to be on the top of outr screen

   int pipeWidth = 64;     // scaled by 1/6
   int pipeHeight = 512;
    
   class Pipe{
    int x = pipeX;
    int y = pipeY;
    int width = pipeWidth;
    int height = pipeHeight;
    Image img;
    boolean passed =false;  // this will keep the cheack wether our flappy bird has passed the pipes or not ( and keep the cheeck of the score)
    
    Pipe(Image img){
        this.img = img;

    }

   }


    






   // game logic 
   Bird bird;
   int velocityX = -4; // move pipes to the left speed (simulates bird moving right)
   int velocityY = 0;
   int gravity = 1;

   Timer gameLoop;
   Timer placePipesTimer;

   ArrayList<Pipe> pipes;  // to store all the pipes in the game we need a list
   Random random = new Random();
  
   boolean gameOver = false;
   double score =0;


    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));

        setFocusable(true); //flappybird class which is our j panel i'ts goign to make sure that this is the one that takes in our key events 
        addKeyListener(this); // it makes sure that the two lines in the kwy pressed is added in our consturtcor it also make sure for key released and key other

        

       // setBackground(Color.blue); after setting the baground image we commneted this one out

        //load images
        // get class refers to the flappybird class and get resource is the location of this flappy bird class so it is inthe src folder and we have our images in the src folder so

        backgroudImg = new  ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg  = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // bird object
        bird = new  Bird(birdImg);
        pipes  = new ArrayList<Pipe>(); 

        // place pipes timer
        // just like before for game loop we would need a action listner 
        // now were gonna add a new pipe every 1.5 seconds
        placePipesTimer = new Timer(1500, new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e){
                 placePipes();;
             }
        });
        placePipesTimer.start();
        

        // game timer
        gameLoop = new Timer(1000/60,this); // 1000/60 = 16.6 1000 milisec = 1 sec (cause we want to draw 60 fps)
        gameLoop.start();

    }

    public  void placePipes(){ 
       // math.random gives us random no bwtween(0-1)  * pipeheigth/2-->(0-256)
       // 128 --> pipeheight/4
       // 0- 128- (0 - 256) --->     range of pipeheight is gonna be  bwtween  ---->  (  1/4 pipeheight --> 3/4 pipeheight  )
       // above one is going to be how much we are gona shift upwrds for the y position

        int randomPipeY =(int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2) );
        int openingSpace= boardHeight/4;
        Pipe topPipe = new Pipe(topPipeImg);

        topPipe.y = randomPipeY;
         
        pipes.add(topPipe);// adding to the arraylist named (pipes)

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }



    public void  paintComponent(Graphics g){
       // invoking the propoerties of jpanel(its parent class)
        super.paintComponent(g);
        draw(g);


    }

    public void draw(Graphics g){
      
        //background           // here indxing stsrt from top left corner
        g.drawImage(backgroudImg, 0, 0, boardWidth, boardHeight, null);
        
        // bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
         for(int i =0;i< pipes.size(); i++){
            // here we are going to get the pipes for the current index
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x,pipe.y,pipeWidth,pipe.height, null);


         }
        // score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over:" + String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10,35);
        }



        

    }

    public void move(){ 
        // bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0); // so bird is going to be whatever position it is downwards or it's going to be zero so zero is the top of the screen
        
        // pipes
        for(int i =0;i< pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
           
            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5 ; // 0.5 because there are 2 pipes ! so 0.5 * 2 =1, 1 for each set of pipes 
            }
 

            if(collision(bird, pipe)){  // if collision fuction return true then gme over
                gameOver = true;
            }
        }

        if(bird.y > boardHeight ){ 
            gameOver=true;
        }
        
        

   
    }  

    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width &&   // a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   // a's top right corner passes  b's top left corner
               a.y < b.y + b.height &&  // a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    // a's bottom left corner passes b's top left corner

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // so this is going to be the action pefomrmed every 16 miliseconds or 60 times a sec and it's going to be 
        // the paint component function  so with the j panel we can do just repaint and this will call paint compnent
             
            move();  // before we repaint the screen i want to update the position of the bird so i gonna call move function
             // in every frame we are moving and painting and in ebery frame we are changing the position of the bird with y value

             repaint();

             if(gameOver){
                placePipesTimer.stop();
                gameLoop.stop();
             }

        
    }


    @Override
    public void keyPressed(KeyEvent e) {
         if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if( gameOver){
                // restart the game bye ressetting the conditions
                bird.y = birdY;
                velocityY =0;
                pipes.clear();
                score =0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();

            }
         }
    }


    @Override
    public void keyTyped(KeyEvent e) {
      
    }

    
    @Override
    public void keyReleased(KeyEvent e) {
       
    }



    
}
// gravity is change in velocity over time 
// here we're gonna set gravity to positive 2 and going downwards is positive