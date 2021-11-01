package edu.binghamton.cs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class Player {
    String dir = "down";
    Animation<TextureRegion> upWalkAnimation;
    Animation<TextureRegion> leftWalkAnimation;
    Animation<TextureRegion> downWalkAnimation;
    Animation<TextureRegion> rightWalkAnimation;
    static int x_pos=50;
    static int y_pos=50;
    float stateTime;

    //SPRITES
    String curr_sprite;
    ArrayList<String> spriteList = new ArrayList();
    // Walking Sprites
    String up_walk = "data/player_up_walk.png";
    String down_walk = "data/player_down_walk.png";
    String side_walk = "data/player_side_walk.png";
    // Idle Sprites
    // Equip Sprites
    // Attack Sprites


    public void animate(int num_ignored, int rows, int cols){
        spriteList.add(up_walk);
        spriteList.add(down_walk);
        spriteList.add(side_walk);
        Texture upSpriteSheet = new Texture(Gdx.files.internal(spriteList.get(0)));
        Texture rightSpriteSheet = new Texture(Gdx.files.internal(spriteList.get(2)));
        Texture leftSpriteSheet = new Texture(Gdx.files.internal(spriteList.get(2)));
        Texture downSpriteSheet = new Texture(Gdx.files.internal(spriteList.get(1)));

        //Setting up arr of sprites for left movement
        TextureRegion[][] leftSprite = TextureRegion.split(leftSpriteSheet,leftSpriteSheet.getWidth()/cols, leftSpriteSheet.getHeight()/rows); //Splitting the sprite sheet into separate sprites based on the number of rows and columns compared to the size of the image itself
        TextureRegion[] leftSpriteFrames = new TextureRegion[cols*rows-num_ignored];
        //For moving right
        TextureRegion[][] rightSprite = TextureRegion.split(rightSpriteSheet,rightSpriteSheet.getWidth()/cols, rightSpriteSheet.getHeight()/rows); //Splitting the sprite sheet into separate sprites based on the number of rows and columns compared to the size of the image itself
        TextureRegion[] rightSpriteFrames = new TextureRegion[cols*rows-num_ignored];
        //For moving down
        TextureRegion[][] downSprite = TextureRegion.split(downSpriteSheet,downSpriteSheet.getWidth()/cols, downSpriteSheet.getHeight()/rows);
        TextureRegion[] downSpriteFrames = new TextureRegion[cols*rows-num_ignored];
        //For moving up
        TextureRegion[][] upSprite = TextureRegion.split(upSpriteSheet,upSpriteSheet.getWidth()/cols, upSpriteSheet.getHeight()/rows);
        TextureRegion[] upSpriteFrames = new TextureRegion[cols*rows-num_ignored];

        int leftIndex = 0;
        int upIndex = 0;
        int downIndex = 0;
        int rightIndex = 0;

        int remaining = cols-num_ignored;
        for(int i=0; i<rows;i++){
            for(int j=0; j<cols; j++){
                if(i==rows-1){ //If we're in the last row
                    if(remaining>0){
                        remaining--;
                        leftSpriteFrames[leftIndex++] = leftSprite[i][j];
                        upSpriteFrames[upIndex++] = upSprite[i][j];
                        downSpriteFrames[downIndex++] = downSprite[i][j];
                        rightSpriteFrames[rightIndex] = rightSprite[i][j];
                        rightSpriteFrames[rightIndex].flip(true,false);
                        rightIndex++;
                    }
                }
                else {
                    leftSpriteFrames[leftIndex++] = leftSprite[i][j]; //Putting the individual sprites into an array in the order they appear on the sheet
                    upSpriteFrames[upIndex++] = upSprite[i][j]; //Putting the individual sprites into an array in the order they appear on the sheet
                    downSpriteFrames[downIndex++] = downSprite[i][j]; //Putting the individual sprites into an array in the order they appear on the sheet
                    rightSpriteFrames[rightIndex] = rightSprite[i][j]; //Putting the individual sprites into an array in the order they appear on the sheet
                    rightSpriteFrames[rightIndex].flip(true,false);
                    rightIndex++;

                }
            }
        }
        this.downWalkAnimation = new Animation<TextureRegion>((float)0.225, downSpriteFrames);
        this.leftWalkAnimation = new Animation<TextureRegion>((float)0.225, leftSpriteFrames);
        this.upWalkAnimation = new Animation<TextureRegion>((float)0.225, upSpriteFrames);
        this.rightWalkAnimation = new Animation<TextureRegion>((float)0.225, rightSpriteFrames);
        this.stateTime = (float)0;
    }




    public static class Projectile{
        String shoot_dir;
        int timer=100;
        int x = Player.x_pos+150;
        int y = Player.y_pos+150;
        SpriteBatch batch = new SpriteBatch();
        Texture book = new Texture(Gdx.files.internal("data/book.png"));

        Projectile(String d){
            shoot_dir = d;
        }


    }
}

