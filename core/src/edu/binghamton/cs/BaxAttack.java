package edu.binghamton.cs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sun.tools.javac.util.Log;


public class BaxAttack extends ApplicationAdapter {
	Stage stage;
	SpriteBatch batch;
	Player mc;
	float w, h;
	int numKilled = 0;
	ShapeRenderer shapeRenderer;
	Rectangle hitbox = new Rectangle();
	Animation<TextureRegion> walkAnimation;
	Texture spriteSheet; //The image containing the sprite sheet
	float stateTime;
	ArrayList<SpriteBatch> enemyBatches = new ArrayList();
	ArrayList<Enemy> enemies = new ArrayList();
	ArrayList<Enemy> killedEnemies = new ArrayList();
	ArrayList<Player.Projectile> projectiles = new ArrayList();
	ArrayList<Player.Projectile> destroyedProjectiles = new ArrayList();
	SpriteBatch mc_batch;
	TextureRegion mcCurrentFrame;
	BitmapFont score;
	Boolean shearsOnScreen = false;
	String scoreText;
	TextureRegion shearsRegion;
	//Attack Button
	Texture attackImg;
	TextureRegion attackRegion;
	TextureRegionDrawable attackDrawable;
	ImageButton attackButton;
	//Scissors
	Texture scissorsImg;
	TextureRegion scissorsRegion;
	TextureRegionDrawable scissorsDrawable;
	ImageButton scissorsButton;
	//Game Background
	Texture bg;
	TextureRegion region;
	Texture shearsTexture;
	Image shears;


	@Override
	public void create () {
		//Text
		score = new BitmapFont();
		score.getData().setScale(5f);

		//Player
		mc = new Player();
		mc.animate(2, 2,4);
		mc_batch = new SpriteBatch();

		//Enemy
		addEnemy();
		int d = ThreadLocalRandom.current().nextInt(0,99999)%Gdx.graphics.getWidth();
		enemies.get(0).x = d;
		enemies.get(0).y = Gdx.graphics.getHeight()-150;
		enemies.get(0).following = true;
		enemies.get(0).dir = "down";
		addEnemy();
		d = ThreadLocalRandom.current().nextInt(0,99999)%Gdx.graphics.getWidth();
		enemies.get(1).x = d;
		enemies.get(1).y = Gdx.graphics.getHeight()-150;
		enemies.get(1).following = true;
		enemies.get(1).dir = "down";
		addEnemy();
		d = ThreadLocalRandom.current().nextInt(0,99999)%Gdx.graphics.getWidth();
		enemies.get(2).x = d;
		enemies.get(2).y = Gdx.graphics.getHeight()-150;
		enemies.get(2).following = true;
		enemies.get(2).dir = "down";

		//Scene
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		//Buttons
		attackImg = new Texture(Gdx.files.internal("data/bookButton.png"));
		attackRegion = new TextureRegion(attackImg);
		attackDrawable = new TextureRegionDrawable(attackRegion);
		attackButton = new ImageButton(attackDrawable);
		attackButton.setSize(200,200);
		attackButton.setPosition(Gdx.graphics.getWidth()-240,40);
		attackButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y){
				projectiles.add(new Player.Projectile(mc.dir));
			}
		});

		//Background
		bg = new Texture(Gdx.files.internal("data/bg.png"));
		region = new TextureRegion(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );

		//Scissors
		scissorsImg = new Texture(Gdx.files.internal("data/dead_bg.png"));
		scissorsRegion = new TextureRegion(scissorsImg);
		scissorsDrawable = new TextureRegionDrawable(scissorsRegion);
		scissorsButton = new ImageButton(scissorsDrawable);
		scissorsButton.setSize(1000,1000);
		scissorsButton.setPosition(Gdx.graphics.getWidth()/2-500,40);
		scissorsButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y){
				numKilled = 0;
				mc.x_pos = 0;
				mc.y_pos = 0;
				enemies.clear();
				killedEnemies.clear();
				projectiles.clear();
				create();
				mc.isDead = false;
			}
		});

		//Staging
		stage = new Stage(new ScreenViewport());
		stage.addActor(attackButton);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render () {
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();
		mc.stateTime += Gdx.graphics.getDeltaTime();

		scoreText = "Points: "+numKilled;


		batch.begin();
		batch.draw(region,0,0);
		score.draw(batch, scoreText, Gdx.graphics.getWidth()-400, Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),10,true);
		batch.end();

		if(mc.isDead==false){
			//Player hitbox
			hitbox.set(mc.x_pos+215, mc.y_pos+140, mc.getWidth(), mc.getHeight());

			//Input processing
			if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)){
				mc.y_pos += 2;
				mc.dir = "up";
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)){
				mc.dir = "down";
				mc.y_pos -= 2;
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)){
				mc.x_pos += 2;
				mc.dir = "right";
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)){
				mc.dir = "left";
				mc.x_pos -= 2;
			}
			//Idle animation would go here when I add it
			if(mc.dir=="up"){
				mcCurrentFrame = mc.upWalkAnimation.getKeyFrame(mc.stateTime, true);
				mc_batch.begin();
				mc_batch.draw(mcCurrentFrame, mc.x_pos, mc.y_pos,500,500);
				mc_batch.end();
			}
			else if(mc.dir=="down"){
				mcCurrentFrame = mc.downWalkAnimation.getKeyFrame(mc.stateTime, true);
				mc_batch.begin();
				mc_batch.draw(mcCurrentFrame, mc.x_pos, mc.y_pos,500,500);
				mc_batch.end();
			}
			else if(mc.dir=="left"){
				mcCurrentFrame = mc.leftWalkAnimation.getKeyFrame(mc.stateTime, true);
				mc_batch.begin();
				mc_batch.draw(mcCurrentFrame, mc.x_pos, mc.y_pos,500,500);
				mc_batch.end();
			}
			if(mc.dir=="right"){
				mcCurrentFrame = mc.rightWalkAnimation.getKeyFrame(mc.stateTime, true);
				mc_batch.begin();
				mc_batch.draw(mcCurrentFrame, mc.x_pos, mc.y_pos,500,500);
				mc_batch.end();
			}

			for(Enemy e:enemies){
				TextureRegion currentFrame;
				if(e.dir=="down"){currentFrame = e.downWalkAnimation.getKeyFrame(stateTime, true);}
				else if(e.dir=="up"){currentFrame = e.upWalkAnimation.getKeyFrame(stateTime, true);}
				else if(e.dir=="left"){currentFrame = e.leftWalkAnimation.getKeyFrame(stateTime, true);}
				else{currentFrame = e.rightWalkAnimation.getKeyFrame(stateTime, true);}

				e.hitbox.set(e.x+215, e.y+140, mc.getWidth()+15, mc.getHeight()+15);
				if(hitbox.overlaps(e.hitbox)){
					mc.isDead = true;
				}

				if(e.batch!=null){
					e.batch.begin(); // Drawing Baxter
					e.batch.draw(currentFrame, e.x, e.y,515,515);
					e.batch.end();
				}
				e.move(mc);
			}


			//Projectiles
			for(Player.Projectile p:projectiles){
				p.hitbox.set(p.x+20,p.y+40,90, 80);
	//			if(p.shapeRenderer.overlaps())
				if(p.batch!=null){
					p.batch.begin(); //Drawing Book
					p.batch.draw(p.book,p.x,p.y,150,150 );
					p.batch.end();
					p.timer--;
				}

				if(p.shoot_dir=="up"){ //Updating Coordinates
					p.y+=5;
				}
				else if(p.shoot_dir=="down"){
					p.y-=5;
				}
				else if(p.shoot_dir=="left"){
					p.x-=5;
				}
				else if(p.shoot_dir=="right"){
					p.x+=5;
				}

				for(Enemy e: enemies){

					if(p.hits==0){
						if(p.hitbox.overlaps(e.hitbox)){
							destroyedProjectiles.add(p);
							killedEnemies.add(e);
							p.hits++;
							numKilled++;
						}
					}
				}
			}
			for(Enemy e:killedEnemies){
				e.batch = null;
				e.hitbox.set(9000, 9000, 1,1);
				enemies.remove(e);
				int numNewEnemies = ThreadLocalRandom.current().nextInt(1,3);
				for(int i=0 ; i < numNewEnemies ; i++){
					if(enemies.size()<16){
						addEnemy();
					}
				}
			}

			killedEnemies.clear();

			for(Player.Projectile p:destroyedProjectiles){
				p.batch = null;
				projectiles.remove(p);
			}
			destroyedProjectiles.clear();

		}
		else{
			stage.addActor(scissorsButton);
		}

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		spriteSheet.dispose();
		for(SpriteBatch s:enemyBatches){
			s.dispose();
		}
		for(Player.Projectile p:projectiles){
			p.batch.dispose();
		}
	}

	@Override
	public void resize(int width, int height){
		w = width;
		h = height;
	}

	public void addEnemy(){
		Enemy bax = new Enemy(500,500);
		enemies.add(bax);
	}
}