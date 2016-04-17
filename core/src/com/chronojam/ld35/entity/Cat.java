package com.chronojam.ld35.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Cat extends Entity {
	private boolean alive = true;
	private float startX;
	private boolean attacked = false;

	public Cat(AssetManager assetManager, float x, float y) {
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;
		startX = x;

		moving = true;
		period = 0.6f;

		textureRegion = new TextureRegion(assetManager.get("cats.png", Texture.class));
		animatedTextures = textureRegion.split(69, 32)[0];
		animation = new Animation(period, animatedTextures);
		animation.setPlayMode(PlayMode.LOOP);
		sprite = new Sprite(animation.getKeyFrame(elapsedTime, true));
		sprite.scale(2);
		sprite.setX(x);
		sprite.setY(y);
		bounds = new Rectangle(x, y, 69 * 2, 32 * 2);
		//		die();
	}

	public void render(SpriteBatch batch) {
		super.render(batch);
		if (!alive && elapsedTime > 0.6f) {
			health = 0;
			sprite = new Sprite(assetManager.get("cat_front.png", Texture.class));
			sprite.scale(2);
		}
	}

	public void renderBack(SpriteBatch batch) {
		if (!alive && elapsedTime > 0.6f)
			batch.draw(assetManager.get("cat_back.png", Texture.class), x - 69, y - 32, sprite.getWidth() * 3, sprite.getHeight() * 3);
	}
	
	public void attack(){
		textureRegion = new TextureRegion(assetManager.get("cat_attack.png", Texture.class));
		animatedTextures = textureRegion.split(81, 32)[0];
		x = startX-36;
		sprite.setBounds(x, y, 81, 32);
		period = 0.1f;
		animation = new Animation(period, animatedTextures);		
		looping = false;
		elapsedTime = 0;
		attacked = true;
	}

	public void die() {
		textureRegion = new TextureRegion(assetManager.get("cat_death.png", Texture.class));
		animatedTextures = textureRegion.split(69, 32)[0];
		period = 1 / 5f;
		animation = new Animation(period, animatedTextures);
		looping = false;
		alive = false;
		elapsedTime = 0;
		assetManager.get("cat_death.wav", Sound.class).play();
	}

	public boolean isAlive() {
		return alive;
	}
	
	public boolean hasAttacked(){
		return attacked;
	}

	public float getElapsedTime() {
		return elapsedTime;
	}

}
