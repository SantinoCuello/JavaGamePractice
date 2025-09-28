package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity{


    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;

    public Player(GamePanel gp,KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize /2);
        screenY = gp.screenHeight /2 - (gp.tileSize /2);

        solidArea = new Rectangle(12,18,24,33);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;



        setDefaultValues();
        getPlayerImage();
    }

    public void getPlayerImage(){
        
        up1 = setup("per7");
        up2 = setup("per8");
        down1 = setup("per1");
        down2 = setup("per2");
        left1 = setup("per4");
        left2 = setup("per3");
        right1 = setup("per5");
        right2 = setup("per6");

    }

    public BufferedImage setup(String imageName){
         
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/res/player/" + imageName + ".png")); 
            image = uTool.scaledImage(image, gp.tileSize, gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void setDefaultValues(){ //Aplica valores estandar del personaje

        worldX = gp.tileSize * 25;
        worldY = gp.tileSize * 45;
        speed = 4; 
        direction = "down";
    }

    public void update(){

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){


        //Asigno el movimiento según la tecla
        if(keyH.upPressed == true){ 
            direction = "up";
            
        }else if(keyH.downPressed == true){
            direction = "down";
            
        }else if(keyH.leftPressed == true){
            direction = "left";
            
        }else if(keyH.rightPressed == true){
            direction = "right";
            
        }


        //Ver colisiónes con tiles
        collisionOn = false;
        gp.cChecker.checkTile(this);

        //Ver colisiones con objetos
        int objIndex = gp.cChecker.checkObject(this, true);
        pickUpObject(objIndex);

        //Si collisionOn es false, el jugador pude moverse
        if(collisionOn == false){
            
            switch(direction){
                case "up": worldY-=speed; break;
                case "down": worldY+=speed; break;
                case "left": worldX-=speed; break;
                case "right": worldX+=speed; break;
            }

        }

        spriteCounter++;
        if(spriteCounter > 10) { //Cada cuantos frames cambia de imagen
            if(spriteNum == 1){
                spriteNum = 2;
            } else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        }

    }

    public void pickUpObject(int i){

        if(i != 999){ //Si es 999 es porque no toque ningun objeto

            String objectName = gp.obj[i].name;

            switch (objectName) {
                case "Key":
                    gp.playSE(1);
                    hasKey++;           //Agrega llave
                    gp.obj[i] = null;   //Borra llave del mapa
                    gp.ui.showMessage("Recogiste una llave!");
                    break;
                case "Door":
                    gp.playSE(3);
                    if (hasKey > 0) {
                        gp.obj[i] = null;   //Borra puerta
                        hasKey--;           //Quita llave
                        gp.ui.showMessage("Abriste la puerta!");
                    }else{
                    gp.ui.showMessage("Necesitas una llave");
                    }
                    break;
                case "Boots":
                    gp.playSE(2);
                    speed += 2;         //Aumenta velocidad
                    gp.obj[i] = null;   //Borra botas del mapa
                    gp.ui.showMessage("Velocidad incrementada!");
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;
            }
        }

    }

    public void draw(Graphics2D g2){
        

        BufferedImage image = null;

        switch(direction){
            case "up": 
                if(spriteNum == 1){
                image = up1;
                }
                if(spriteNum == 2){
                image = up2;
                }
                break;
            case "down": 
               if(spriteNum == 1){
                image = down1;
                }
                if(spriteNum == 2){
                image = down2;
                }
                break;
            case "left": 
                if(spriteNum == 1){
                image = left1;
                }
                if(spriteNum == 2){
                image = left2;
                }
                break;
            case "right": 
                if(spriteNum == 1){
                image = right1;
                }
                if(spriteNum == 2){
                image = right2;
                }
                break;
        }
        
        g2.drawImage(image, screenX, screenY, null);
    
    }

    
}
