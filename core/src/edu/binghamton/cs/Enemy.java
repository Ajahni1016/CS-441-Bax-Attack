package edu.binghamton.cs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy {
    String dir = "down";
    Animation<TextureRegion> upWalkAnimation;
    Animation<TextureRegion> leftWalkAnimation;
    Animation<TextureRegion> downWalkAnimation;
    Animation<TextureRegion> rightWalkAnimation;
    float x;
    float y;
    static int height;
    static int width;
    float stateTime;
    boolean following = false;
    float dest;
    String x_or_y;
    Rectangle hitbox= new Rectangle();
    SpriteBatch batch = new SpriteBatch();

    //SPRITES
    String curr_sprite;
    ArrayList<String> spriteList = new ArrayList();
    // Walking Sprites
    String up_walk = "data/baxter_up_walk.png";
    String down_walk = "data/baxter_down_walk.png";
    String side_walk = "data/baxter_side_walk.png";
    // Idle Sprites
    // Equip Sprites
    // Attack Sprites

    Enemy(float x,float y){
        this.animate(2, 2,4);
//        Random rand = new Random(System.currentTimeMillis());
        int edge = ThreadLocalRandom.current().nextInt(1,4);
        if(edge == 1){
            dir = "down";
            int d = ThreadLocalRandom.current().nextInt(0,99999)%Gdx.graphics.getWidth();
            this.x = d;
            this.y = Gdx.graphics.getHeight()-130;
        }
        else if(edge == 2){
            dir = "left";
            int d = ThreadLocalRandom.current().nextInt(0,99999)%Gdx.graphics.getHeight();
            this.y = d;
            this.x = Gdx.graphics.getWidth()-220;
        }
        else if(edge == 3){
            dir = "right";
            int d = ThreadLocalRandom.current().nextInt(0,99999)%Gdx.graphics.getHeight();
            this.y = d;
            this.x = -250;
        }


    }

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

    public void getHeight(){
        if(this.dir == "up"){
            height = this.upWalkAnimation.getKeyFrame(this.stateTime).getRegionHeight();
        }
        if(this.dir == "down"){
            height = this.downWalkAnimation.getKeyFrame(this.stateTime).getRegionHeight();
        }
        if(this.dir == "left"){
            height = this.leftWalkAnimation.getKeyFrame(this.stateTime).getRegionHeight();
        }
        if(this.dir == "right"){
            height = this.rightWalkAnimation.getKeyFrame(this.stateTime).getRegionHeight();
        }
    }

    public void getWidth(){
        if(this.dir == "up"){
            width = this.upWalkAnimation.getKeyFrame(this.stateTime).getRegionWidth();
        }
        if(this.dir == "down"){
            width = this.downWalkAnimation.getKeyFrame(this.stateTime).getRegionWidth();
        }
        if(this.dir == "left"){
            width = this.leftWalkAnimation.getKeyFrame(this.stateTime).getRegionWidth();
        }
        if(this.dir == "right"){
            width = this.rightWalkAnimation.getKeyFrame(this.stateTime).getRegionWidth();
        }
    }

    public void move(Player p){
        //If the destination isn't set yet/Has already been reached
        if(this.following==false){
            following = true;
            float x_dif;
            float y_dif;

            y_dif = p.y_pos - this.y;
            x_dif = p.x_pos - this.x;

            if(y_dif < 0){ y_dif*=-1; }
            if(x_dif < 0){ x_dif*=-1; }

            if(y_dif > x_dif){
                //set dest to player's y coord
                x_or_y = "y";
                dest = p.y_pos;
                if(dest > this.y){
                    dir = "up";
                }
                else{
                    dir = "down";
                }
            }
            else{
                //set dest to player's x coord
                x_or_y = "x";
                dest = p.x_pos;
                if(dest > this.x){
                    dir = "right";
                }
                else{
                    dir = "left";
                }
            }
        }

        //Moving the enemy
        if(dir=="down"){
            if(this.y<=p.y_pos){ //Dest has been reached
                following = false;
            }
            else{
                y-=1; //Moving down
            }
        }
        else if(dir=="up"){
            if(this.y>=p.y_pos){ //Dest has been reached
                following = false;
            }
            else{
                y+=1; //Moving up
            }
        }
        else if(dir=="left"){
            if(this.x<=p.x_pos){ //Dest has been reached
                following = false;
            }
            else{
                x-=1; //Moving left
            }
        }
        else if(dir=="right"){
            if(this.x>=p.x_pos){ //Dest has been reached
                following = false;
            }
            else{
                x+=1; //Moving right
            }
        }
    }

}
