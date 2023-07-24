package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;



public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16;// 16x16 Tile
    final int scale = 3;//

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow;// 576 pixels

    // FPS
    int FPS = 60;

    KeyHandler keyH = new KeyHandler(); // Keyboard Commands
    Thread gameThread;// Cloak

    Player player = new Player(this,keyH);

    // Set Player's default Position
    /* int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4; */

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);// Reconhece o input do teclado
        this.setFocusable(true);

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
//GAMELOOP "SLEEP"WAY #1
/*     @Override
    public void run() {
        while (gameThread != null) {
            // Setting FPS
            double drawInterval = 1000000000 / FPS; // 0.01666seconds
            double nextDrawTime = System.nanoTime() + drawInterval;
            // 1- UPDATE: update information such as character position
            update();
            // 2- DRAW: draw the screen with the updated information
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    } */
    //GAMELOOP "DELTA" #2
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        
        while(gameThread != null){
        
            currentTime = System.nanoTime();
        
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount =0;
                timer = 0;
            }

        }
    }


    public void update() {
       player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);

        g2.dispose();
    }
}
