package com.chronojam.ld35.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.chronojam.ld35.LD35;
import com.chronojam.ld35.entity.Cat;
import com.chronojam.ld35.entity.Chara;
import com.chronojam.ld35.environments.Box;
import com.chronojam.ld35.environments.Button;
import com.chronojam.ld35.environments.Crane;
import com.chronojam.ld35.environments.Elevator;
import com.chronojam.ld35.environments.Level1;
import com.chronojam.ld35.environments.OuterTile;
import com.chronojam.ld35.images.ImageProvider;

public class GameScreen implements Screen {
	private LD35 game;
	private AssetManager assetManager;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private ImageProvider imageProvider = new ImageProvider();
	private Viewport viewport;
	private Chara chara;
	private OuterTile[][] outerTiles;
	private Elevator elevators[] = new Elevator[2];
	private Button button;
	private Crane crane;
	private Box box;
	private Cat cat;
	private int rot1, rot2, rot3;
	private float fadeToBlackAlpha = 0;
	private boolean resetPressed = false;
	private BitmapFont[] fonts;
	private boolean gameOver = false;
	private String[] overMessages = { "The creation escaped,", "there was nothing we could do,", "how can something so small hold such power and intelligence?", "If anybody finds this...", "Just know...", "We", "are", "sorry.", "Thanks for playing!" };
	private float creditsTimer = 0;
	private float[] messageAlpha;

	public GameScreen(LD35 game, AssetManager assetManager) {
		this.game = game;
		this.assetManager = assetManager;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, imageProvider.getScreenWidth(), imageProvider.getScreenHeight());
		viewport = new FitViewport(imageProvider.getScreenWidth(), imageProvider.getScreenHeight(), camera);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		elevators[0] = new Elevator(assetManager, 80, 80, 480, 480 - 8);
		elevators[1] = new Elevator(assetManager, 720, 80, 720, 320 - 8);
		button = new Button(assetManager, 320, 480);
		crane = new Crane(assetManager, 880, 560);
		box = new Box(assetManager, 905, 550 - 3, 390 - 3);
		cat = new Cat(assetManager, 945, 351);
		outerTiles = new OuterTile[Level1.level.length][Level1.level[0].length];
		for (int y = 0; y < outerTiles.length; y++)
			for (int x = 0; x < outerTiles[0].length; x++) {
				if (Level1.level[y][x] != 0)
					outerTiles[y][x] = new OuterTile(assetManager, x * 80, imageProvider.getScreenHeight() - 80 - y * 80, Level1.level[y][x]);
			}

		chara = new Chara(assetManager, 240, 80);

		Random rand = new Random();
		rot1 = rand.nextInt(361);
		rot2 = rand.nextInt(361);
		rot3 = rand.nextInt(361);

		fonts = new BitmapFont[9];

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zooeys Diary.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 36;
		for (int i = 0; i < fonts.length; i++) {
			fonts[i] = generator.generateFont(parameter);
			fonts[i].setColor(Color.WHITE);
		}
		generator.dispose();
		messageAlpha = new float[9];
		for (int i = 0; i < messageAlpha.length; i++)
			messageAlpha[i] = 0;
		
		Music music = assetManager.get("Save Your Life.mp3", Music.class);
		music.setVolume(0.2f);
		music.setLooping(true);
		music.play();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		if (Gdx.input.isKeyPressed(Keys.R)) {
			resetPressed = true;
		}

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect(0, 0, imageProvider.getScreenWidth(), imageProvider.getScreenHeight());
		shapeRenderer.end();

		if (chara.isDead())
			resetPressed = true;

		checkCollisions();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(assetManager.get("background.png", Texture.class), 0, 0);
		for (int y = 0; y < outerTiles.length; y++)
			for (int x = 0; x < outerTiles[0].length; x++) {
				if (outerTiles[y][x] != null)
					outerTiles[y][x].render(batch);
			}
		for (int i = 0; i < elevators.length; i++)
			elevators[i].render(batch);
		button.render(batch);
		crane.render(batch);

		if (chara.hasTransformed()) {
			batch.draw(assetManager.get("blood1.png", Texture.class), 11 * 80 - 20, 360, 40, 40, 80, 80, 1, 1, rot1, 0, 0, 80, 80, false, false);
			batch.draw(assetManager.get("blood2.png", Texture.class), 12 * 80, 330, 40, 40, 80, 80, 1, 1, rot2, 0, 0, 80, 80, false, false);
			batch.draw(assetManager.get("blood3.png", Texture.class), 13 * 80 + 20, 360, 40, 40, 80, 80, 1, 1, rot3, 0, 0, 80, 80, false, false);
		}

		cat.renderBack(batch);
		chara.render(batch);
		cat.render(batch);
		box.render(batch);
		batch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		if (resetPressed)
			fadeToBlack(shapeRenderer);
		if (gameOver)
			gameOver();
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void checkCollisions() {
		float dy = 0;
		chara.setOnFloor(false);
		Rectangle charaBounds = chara.getBounds();

		if (charaBounds.x > imageProvider.getScreenWidth())
			gameOver = true;

		if (box.isArrived() && cat.isAlive()) {
			cat.die();
		}

		if (cat.isAlive() && !cat.hasAttacked())
			if (charaBounds.y >= 300 && charaBounds.x > 820) {
				cat.attack();
				assetManager.get("chara_death.wav", Sound.class).play(1f);
				chara.die();
			}

		if (charaBounds.y >= 300 && charaBounds.x > 960) {
			chara.transform();
		}

		if (!cat.isAlive()) {
			if (cat.getElapsedTime() >= 0.2f)
				box.setY(390 - 3 - 3);
			if (cat.getElapsedTime() >= 0.4f)
				box.setY(390 - 3 - 6);
			if (cat.getElapsedTime() >= 0.6f)
				box.setY(390 - 3 - 9);
		}

		for (int y = 0; y < outerTiles.length; y++)
			for (int x = 0; x < outerTiles[0].length; x++) {
				if (outerTiles[y][x] != null) {

					if (new Rectangle(charaBounds.x + 2, charaBounds.y, charaBounds.width - 4, charaBounds.height).overlaps(outerTiles[y][x].getBounds())) {
						chara.setOnFloor(true);
					}
					if (new Rectangle(charaBounds.x, charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(outerTiles[y][x].getBounds())) {
						chara.setX(chara.getBounds().x + chara.getSpeed());
					}
					if (new Rectangle(charaBounds.x + charaBounds.width - chara.getSpeed(), charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(outerTiles[y][x].getBounds())) {
						chara.setX(chara.getBounds().x - chara.getSpeed());
					}
					if (new Rectangle(charaBounds.x + 10, charaBounds.y + charaBounds.height - 2, charaBounds.getWidth() - 20, 2).overlaps(outerTiles[y][x].getBounds())) {
						chara.setY(chara.getBounds().y - chara.getJumpSpeed());
					}
					if (!chara.isOnFloor()) {
						if (new Rectangle(charaBounds.x, charaBounds.y - 5, charaBounds.width, charaBounds.height).overlaps(outerTiles[y][x].getBounds()))
							dy = -1;
					}
				}
			}
		if (chara.isOnFloor())
			dy = 0;
		for (int i = 0; i < elevators.length; i++) {
			if (charaBounds.overlaps(elevators[i].getBounds()) || charaBounds.overlaps(elevators[i].getTopBounds())) {
				if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))
					elevators[i].setDirection(Elevator.DOWN);
				if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP))
					elevators[i].setDirection(Elevator.UP);
				chara.setOnFloor(true);
			}
			if (new Rectangle(charaBounds.x, charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(elevators[i].getBounds())) {
				chara.setY(elevators[i].getY() + elevators[i].getHeight());
			}
			if (new Rectangle(charaBounds.x, charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(elevators[i].getTopBounds())) {
				chara.setY(elevators[i].getTopBounds().y + elevators[i].getHeight());
			}
			if (new Rectangle(charaBounds.x + charaBounds.width - chara.getSpeed(), charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(elevators[i].getBounds())) {
				chara.setY(elevators[i].getY() + elevators[i].getHeight());
			}
			if (new Rectangle(charaBounds.x + charaBounds.width - chara.getSpeed(), charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(elevators[i].getTopBounds())) {
				chara.setY(elevators[i].getTopBounds().y + elevators[i].getHeight());
			}
			if (!chara.isOnFloor())
				if (new Rectangle(charaBounds.x, charaBounds.y - 5, charaBounds.width, charaBounds.height).overlaps(elevators[i].getBounds()) || new Rectangle(charaBounds.x, charaBounds.y - 5, charaBounds.width, charaBounds.height).overlaps(elevators[i].getTopBounds()))
					dy = -1;
		}
		if (charaBounds.overlaps(button.getBounds()) || charaBounds.overlaps(button.getButtonBounds())) {
			chara.setOnFloor(true);
		}
		if (charaBounds.overlaps(button.getButtonBounds())) {
			button.setPressed(true);
			box.release();
		}
		if (new Rectangle(charaBounds.x, charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(button.getBounds())) {
			chara.setY(button.getY() + button.getHeight());
		}
		if (new Rectangle(charaBounds.x, charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(button.getButtonBounds())) {
			chara.setY(button.getButtonBounds().y + button.getButtonBounds().height);
		}
		if (new Rectangle(charaBounds.x + charaBounds.width - chara.getSpeed(), charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(button.getBounds())) {
			chara.setY(button.getY() + button.getHeight());
		}
		if (new Rectangle(charaBounds.x + charaBounds.width - chara.getSpeed(), charaBounds.y + 1, 1, charaBounds.height - 1).overlaps(button.getButtonBounds())) {
			chara.setY(button.getButtonBounds().y + button.getButtonBounds().height);
		}
		if (!chara.isOnFloor())
			if (new Rectangle(charaBounds.x, charaBounds.y - 5, charaBounds.width, charaBounds.height).overlaps(button.getBounds()) || new Rectangle(charaBounds.x, charaBounds.y - 5, charaBounds.width, charaBounds.height).overlaps(button.getButtonBounds()))
				dy = -1;

		if (dy == 0)
			if (!chara.isOnFloor()) {
				dy = -5;
			} else
				dy = 0;
		chara.setDy(dy);

	}

	public void fadeToBlack(ShapeRenderer shapeRenderer) {
		if (fadeToBlackAlpha > 1)
			fadeToBlackAlpha = 1;
		if (fadeToBlackAlpha == 1)
			reset();
		shapeRenderer.setColor(0, 0, 0, fadeToBlackAlpha += 0.01f);
		shapeRenderer.rect(0, 0, imageProvider.getScreenWidth(), imageProvider.getScreenHeight());
	}

	public void gameOver() {
		shapeRenderer.setColor(0, 0, 0, fadeToBlackAlpha += 0.01f);
		shapeRenderer.rect(0, 0, imageProvider.getScreenWidth(), imageProvider.getScreenHeight());

		if (fadeToBlackAlpha > 1)
			fadeToBlackAlpha = 1;

		if (fadeToBlackAlpha == 1) {
			shapeRenderer.end();

			creditsTimer += Gdx.graphics.getDeltaTime();

			if (creditsTimer < 15) {
				if (creditsTimer >= 1) {
					if (messageAlpha[0] > 1)
						messageAlpha[0] = 1;
					else if (messageAlpha[0] < 1)
						messageAlpha[0] += 0.1f;
				}
				if (creditsTimer >= 3) {
					if (messageAlpha[1] > 1)
						messageAlpha[1] = 1;
					else if (messageAlpha[1] < 1)
						messageAlpha[1] += 0.1f;
				}
				if (creditsTimer >= 5) {
					if (messageAlpha[2] > 1)
						messageAlpha[2] = 1;
					else if (messageAlpha[2] < 1)
						messageAlpha[2] += 0.1f;
				}
				if (creditsTimer >= 7) {
					if (messageAlpha[3] > 1)
						messageAlpha[3] = 1;
					else if (messageAlpha[3] < 1)
						messageAlpha[3] += 0.1f;
				}
				if (creditsTimer >= 9) {
					if (messageAlpha[4] > 1)
						messageAlpha[4] = 1;
					else if (messageAlpha[4] < 1)
						messageAlpha[4] += 0.1f;
				}
				if (creditsTimer >= 11) {
					if (messageAlpha[5] > 1)
						messageAlpha[5] = 1;
					else if (messageAlpha[5] < 1)
						messageAlpha[5] += 0.1f;
				}
				if (creditsTimer >= 12) {
					if (messageAlpha[6] > 1)
						messageAlpha[6] = 1;
					else if (messageAlpha[6] < 1)
						messageAlpha[6] += 0.1f;
				}
				if (creditsTimer >= 13) {
					if (messageAlpha[7] > 1)
						messageAlpha[7] = 1;
					else if (messageAlpha[7] < 1)
						messageAlpha[7] += 0.1f;
				}
			}

			if (creditsTimer >= 15) {
				for (int i = 0; i < fonts.length - 1; i++) {
					if (messageAlpha[i] < 0.0f)
						messageAlpha[i] = 0;
					else if (messageAlpha[i] > 0.0f)
						messageAlpha[i] -= 0.1f;
				}
			}
			
			if(creditsTimer >= 16){
				if (messageAlpha[8] > 1)
					messageAlpha[8] = 1;
				else if (messageAlpha[8] < 1)
					messageAlpha[8] += 0.1f;
			}

			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			batch.enableBlending();
			for (int i = 0; i < fonts.length; i++)
				fonts[i].setColor(1, 1, 1, messageAlpha[i]);

			fonts[0].draw(batch, overMessages[0], 400, imageProvider.getScreenHeight() - 100);
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, messageAlpha[1]);
			fonts[1].draw(batch, overMessages[1], 350, imageProvider.getScreenHeight() - 200);
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, messageAlpha[2]);
			fonts[2].draw(batch, overMessages[2], 100, imageProvider.getScreenHeight() - 300);
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, messageAlpha[3]);
			fonts[3].draw(batch, overMessages[3], 275, imageProvider.getScreenHeight() - 400);
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, messageAlpha[4]);
			fonts[4].draw(batch, overMessages[4], 725, imageProvider.getScreenHeight() - 400);
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, messageAlpha[5]);
			fonts[5].draw(batch, overMessages[5], 450, imageProvider.getScreenHeight() - 500);
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, messageAlpha[6]);
			fonts[6].draw(batch, overMessages[6], 550, imageProvider.getScreenHeight() - 500);
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, messageAlpha[7]);
			fonts[7].draw(batch, overMessages[7], 650, imageProvider.getScreenHeight() - 500);
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, messageAlpha[8]);
			fonts[8].draw(batch, overMessages[8], 450, imageProvider.getScreenHeight() - 300);
			batch.end();

			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Filled);
		}
	}

	public void reset() {
		elevators[0] = new Elevator(assetManager, 80, 80, 480, 480 - 8);
		elevators[1] = new Elevator(assetManager, 720, 80, 720, 320 - 8);
		button = new Button(assetManager, 320, 480);
		crane = new Crane(assetManager, 880, 560);
		box = new Box(assetManager, 905, 550 - 3, 390 - 3);
		cat = new Cat(assetManager, 945, 351);
		outerTiles = new OuterTile[Level1.level.length][Level1.level[0].length];
		for (int y = 0; y < outerTiles.length; y++)
			for (int x = 0; x < outerTiles[0].length; x++) {
				if (Level1.level[y][x] != 0)
					outerTiles[y][x] = new OuterTile(assetManager, x * 80, imageProvider.getScreenHeight() - 80 - y * 80, Level1.level[y][x]);
			}

		chara = new Chara(assetManager, 240, 80);

		Random rand = new Random();
		rot1 = rand.nextInt(361);
		rot2 = rand.nextInt(361);
		rot3 = rand.nextInt(361);

		fadeToBlackAlpha = 0;
		resetPressed = false;
		gameOver = false;
		creditsTimer = 0;
		
		for(int i = 0; i < fonts.length; i++)
			messageAlpha[i] = 0;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
