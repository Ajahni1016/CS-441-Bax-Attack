package edu.binghamton.cs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import 	java.lang.Math;

public class CustomDrawing extends ApplicationAdapter {
	SpriteBatch batch;
	//	Texture img;
	TextureRegion img;
	boolean touched;
	Player mc;
	private final Vector2 knobPercent = new Vector2();
	Stage stage;
	double x, y=50;
	int bax_y=720;
	float dx, dy;
	Random rand = new Random();
	int alreadyRight = 0;
	float w, h;
	private final Vector2 knobPosition = new Vector2();
	boolean resetOnTouchUp = true;
	Touchpad joystick;
	String shoot_projectile = "no";
	ShapeRenderer circleRenderer;
	/////////////////////////////////////////////
	String enemySpriteSheetPath = "data/baxter_down_walk.png";
	private static final int FRAME_COLS = 4, FRAME_ROWS = 2; //The number of rows and columns in the sprite sheet
	Animation<TextureRegion> walkAnimation;
	Texture spriteSheet; //The image containing the sprite sheet
	float stateTime;
	int num_ignored = 2; // How many sprite locations to ignore in the last row of the sheet (for if a sheet isn't completely filled)
	ArrayList<SpriteBatch> enemies = new ArrayList();
	ArrayList<Player.Projectile> projectiles = new ArrayList();
	SpriteBatch mc_batch;
	TextureRegion mcCurrentFrame;

	@Override
	public void create () {
		mc = new Player();
		mc.animate(2, 2,4);
		mc_batch = new SpriteBatch();
		addEnemy();
		addEnemy();
		addEnemy();
		batch = new SpriteBatch();
		batch.begin();
		}

	@Override
	public void render () {
		Gdx.gl.glClearColor( 122/255f, 2/255f, 92/255f, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();
		mc.stateTime += Gdx.graphics.getDeltaTime();

		//Input processing
		if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)){
			mc.y_pos += 2;
			mc.dir = "up";
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)){
			mc.dir = "down";
			mc.y_pos -= 2;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)){
			mc.x_pos += 2;
			mc.dir = "right";
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)){
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

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			addProjectile();
		}

		//createTouchpad();

		//enemy
		//int x = (int)(Math.random() * ((stage.getWidth()) + 1));
		//int y = (int)(Math.random() * ((stage.getHeight()) + 1));
		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true); //enemy
		int x = 700;
		int going_down = 1;
		if(bax_y>=h){
			going_down = 1;
		}
		if (bax_y <= 0){
			going_down = 0;
		}

		if(going_down==1){
			bax_y-=2;
		}
		else{
			bax_y+=2;
		}
		for(SpriteBatch s:enemies){
			s.begin();
			s.draw(currentFrame, x, bax_y,500,500); // Draw Baxter
			s.end();
			x+=200;
		}

		for(Player.Projectile p:projectiles){
			p.batch.begin();
			p.batch.draw(p.book,p.x,p.y,150,150 ); // Draw Book
			p.batch.end();
			p.timer--;
			if(p.shoot_dir=="up"){
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
		}

		//Touchpad
//		stage.act(Gdx.graphics.getDeltaTime());
//		stage.draw();

	}

	@Override
	public void dispose () {
		batch.dispose();
		spriteSheet.dispose();
		for(SpriteBatch s:enemies){
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


	public void createTouchpad(){
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
//
//		joystickSkin = new Skin();
//		joystickSkin.add("touchKnob", new Texture(Gdx.files.internal("data/Pan_Blue_Circle.png")));

		Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
//		touchpadStyle.knob = joystickSkin.getDrawable("touchKnob");

		Pixmap knob = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
		knob.setColor((float)231, (float)11, (float)11, 1);
		knob.fillCircle(100, 100, 50);
		touchpadStyle.knob = new TextureRegionDrawable(new TextureRegion(new Texture(knob)));

		Pixmap background = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
		background.setColor(1, 1, 1, 1);
		background.fillCircle(100, 100, 100);
		touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(background)));
		joystick = new Touchpad(10, touchpadStyle);
		joystick.setBounds(25, 25, 300, 300); //Size of outter circle and position on screen


		stage.addActor(joystick);

//		joystick.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				float deltaX = ((Touchpad) actor).getKnobPercentX();
//				float deltaY = ((Touchpad) actor).getKnobPercentY();
//			}
//		});


		joystick.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (touched) return false;
				touched = true;
				System.out.println("DOWN "+joystick.getKnobPercentX());
				return true;
			}

			@Override
			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				System.out.println("DRAGGED");

			}

			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				touched = false;
				System.out.println("UP");
			}
		});
	}

	public void createSpriteAnimation(int num_ignored, String path){
		spriteSheet = new Texture(Gdx.files.internal(path));
		TextureRegion[][] sprite = TextureRegion.split(spriteSheet,spriteSheet.getWidth()/FRAME_COLS, spriteSheet.getHeight()/FRAME_ROWS); //Splitting the sprite sheet into separate sprites based on the number of rows and columns compared to the size of the image itself
		TextureRegion[] spriteFrames = new TextureRegion[FRAME_COLS*FRAME_ROWS-num_ignored];
		int index = 0;
		int remaining = FRAME_COLS-num_ignored;
		for(int i=0; i<FRAME_ROWS;i++){
			for(int j=0; j<FRAME_COLS; j++){
				if(i==FRAME_ROWS-1){ //If we're in the last row
					if(remaining>0){
						remaining--;
						spriteFrames[index++] = sprite[i][j];
					}
				}
				else {
					spriteFrames[index++] = sprite[i][j]; //Putting the individual sprites into an array in the order they appear on the sheet
				}
			}
		}
		walkAnimation = new Animation<TextureRegion>((float)0.225, spriteFrames);
		stateTime = (float)0;
	}

	public void addEnemy(){
		createSpriteAnimation(num_ignored,enemySpriteSheetPath);
		enemies.add(new SpriteBatch());
	}

	public void addProjectile(){
		projectiles.add(new Player.Projectile(mc.dir));
		System.out.println("Pressed spacebar");
	}
}