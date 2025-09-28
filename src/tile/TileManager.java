package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import javax.swing.text.Utilities;

import main.GamePanel;
import main.UtilityTool;


public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp){
        this.gp=gp;
        tile = new Tile[10];//10 Tipos de tile
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/res/maps/world1.txt");
    }

    public void getTileImage(){
            
            setup(0, "tile1", false);
            setup(1, "tile2", true);
            setup(2, "tile3", true);
            setup(3, "tile4", false);
            setup(4, "tile6", false);
            setup(5, "tile5", false);

    }

    public void setup(int index, String imagePath, boolean collision){

        UtilityTool uTool = new UtilityTool();

        try{

            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/" + imagePath + ".png"));
            tile[index].image = uTool.scaledImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void loadMap(String filePath){ //Escanea linea a linea el archivo map y mete los numeros en el array mapTileNum

        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){

                String line = br.readLine(); //Tomo toda la linea

                while(col < gp.maxWorldCol){

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col ++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
                
            }
            br.close();
        } catch (Exception e) {
        }
    }


    public void draw(Graphics2D g2){
        
        //Para la cámara, se fija a qué distancia del 0,0 está el personaje
        //y dibuja al rededor del personaje respetando las distancias
        
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow]; //Toma un numero del array

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX; //worldX es una posicion del mapa y screenX es dónde lo tengo que dibujar
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            //Agregarle screenY e screenX sirve para que el personaje no quede alejado del mapa

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
                
            }

            
            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

    }
}
