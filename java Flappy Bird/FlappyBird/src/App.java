import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;  // these are in pixels
        int boardHeight = 640;

        JFrame frame =  new JFrame("Flappy Bird");
       // frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);// going to set the window in the centre of our screen
        frame.setResizable(false); // user cannot resize the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// it means if the user type x button it will terminae the program

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); // if we don't use it thr blue frame will also con tain the title brr plus 360 and 640 that's why we used it
        flappyBird.requestFocus();
        frame.setVisible(true);
         


    }
}
