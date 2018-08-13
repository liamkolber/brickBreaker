package brickbreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.Timer;
import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
    private boolean play = false; //Game doesn't play by itself
    private int score = 0; //starting score
    private int totalBricks = 42; //total bricks on start
    
    private Timer timer;
    private int delay = 8;
    
    private int playerX = 310;
    
    private int ballposX = ThreadLocalRandom.current().nextInt(100,571);
    private int ballposY = 350;
    private int ballXdir = ThreadLocalRandom.current().nextInt(-2,0);
    private int ballYdir = -1;
    
    private MapGenerator map;
    
    public Gameplay() {
        map = new MapGenerator(6,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }
    
    public void paint(Graphics g) {
        //background
        g.setColor(Color.black);
        g.fillRect(1, 1, 697, 592);
        
        //drawing bricks
        map.draw((Graphics2D)g);
        
        //borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,697,3);
        g.fillRect(697,0,3,592);
        
        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: "+score, 580, 30);
        
        //the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        
        //the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);
        
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 40));
            g.drawString("CONGRATULATIONS", 190, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press ENTER to restart.", 190, 350);
        }
        
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 40));
            g.drawString("GAME OVER", 190, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press ENTER to restart.", 190, 350);
        }
        
        g.dispose();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballposX = ThreadLocalRandom.current().nextInt(100,571);
                ballposY = 350;
                ballXdir = ThreadLocalRandom.current().nextInt(-2,3);
                ballYdir = ThreadLocalRandom.current().nextInt(-2,0);
                playerX = 310;
                score = 0;
                totalBricks = 42;
                map = new MapGenerator(6,7);
                
                repaint();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    
    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,25,8))) {
                ballYdir = -ballYdir;
                if (ballXdir > 0) {
                    ballXdir = -ballXdir;
                } else if (ballXdir == 0) {
                    ballXdir = -2;
                }
            }
            if (new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX+25,550,25,8))) {
                ballYdir = -ballYdir;
                if (ballXdir > 0) {
                    ballXdir = -ballXdir;
                } else if (ballXdir == 0) {
                    ballXdir = -1;
                }
            }
            if (new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX+50,550,25,8))) {
                ballYdir = -ballYdir;
                if (ballXdir < 0) {
                    ballXdir = -ballXdir;
                } else if (ballXdir == 0) {
                    ballXdir = 1;
                }
            }
            if (new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX+75,550,25,8))) {
                ballYdir = -ballYdir;
                if (ballXdir < 0) {
                    ballXdir = -ballXdir;
                } else if (ballXdir == 0) {
                    ballXdir = 2;
                }
            }
            
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        int brickX = j * brickWidth + 80;
                        int brickY = i * brickHeight + 50;
                        
                        Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX,ballposY,20,20);
                        Rectangle brickRect = rect;
                        
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score += 5;
                            
                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            
            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }
}
