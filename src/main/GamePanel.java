package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    
    // SCREEN SETTINGS

        final int originalTileSize = 16; // 16x16
        final int scale = 3;

        public final int tileSize = originalTileSize * scale; // 48x48
        public final int maxScreenCol = 16;
        public final int maxScreenRow = 12;
        public final int screenWidth = tileSize * maxScreenCol; // 768
        public final int screenHeight = tileSize * maxScreenRow; // 576

        //Configuracion del mundo
        public final int maxWorldCol = 50;
        public final int maxWorldRow = 50;
        public final int worldWidth = tileSize * maxWorldCol;
        public final int worldHeight = tileSize * maxWorldRow;

        //FPS
        int fps = 60;

        TileManager tileM = new TileManager(this);
        KeyHandler keyH = new KeyHandler();
        Thread gameThread; // Mantiene el juego con vida 
        public CollisionChecker cChecker = new CollisionChecker(this);
        public AssetSetter aSetter = new AssetSetter(this);
        public Player player = new Player(this,keyH);
        public SuperObject obj[] = new SuperObject[10];



        // Setear la posicion default del personaje
        int playerX = 100;
        int playerY = 100;
        int playerSpeed = 4;

        public GamePanel(){

            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            this.setBackground(Color.black);
            this.setDoubleBuffered(true);
            this.addKeyListener(keyH); // Agrego el key handler
            this.setFocusable(true); // El game panel puede recibir teclas
        }

        public void setupGame(){

            aSetter.setObject();

        }

        public void startGameThread(){

            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public void run() {
            
            double drawInterval = 1000000000/fps;
            double delta = 0;
            long lastTime = System.nanoTime();
            long currentTime;
            long timer = 0;
            int drawCount = 0;

            while(gameThread != null){

                currentTime = System.nanoTime(); //Tiempo actual

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
                    System.out.println("FPS:" + drawCount);
                    drawCount = 0;
                    timer = 0;
                }
            }
        }

        public void update(){

            player.update();

        }

        public void paintComponent(Graphics g){

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g;

            //Tiles
            tileM.draw(g2);

            //Objetos
            for(int i = 0; i < obj.length; i++){

                if(obj[i] != null){
                    obj[i].draw(g2, this);
                }

            }
            
            //Jugador
            player.draw(g2);

            g2.dispose();
        }
}
