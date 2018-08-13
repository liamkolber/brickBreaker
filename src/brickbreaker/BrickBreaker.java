package brickbreaker;

import javax.swing.JFrame;

public class BrickBreaker {
    public static void main(String[] args) {
        JFrame obj = new JFrame(); //Creates frame
        Gameplay gamePlay = new Gameplay(); //Creates new object of type Gameplay
        obj.setBounds(10, 10, 700, 600); //Sets frame size
        obj.setTitle("Breakout Ball"); //Sets title
        obj.setResizable(false); //Prevents resizing
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exits when closed
        obj.add(gamePlay); //add gamePlay to obj
        obj.setVisible(true); // Makes visible
    }
}
