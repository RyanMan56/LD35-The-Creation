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

public class Chara extends Entity {
	private boolean alreadyPlayed = true;
	private boolean transformed = false;
	private float speed = 1;
	private float defaultJumpSpeed = 25;
	private float jumpSpeed = defaultJumpSpeed;
	private boolean jumping = false;
	private Rectangle floorBounds;
	private boolean dead = false;

	public Chara(AssetManager assetManager, float x, float y) {
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;

		textureRegion = new TextureRegion(assetManager.get("chara_w.png", Texture.class));
		animatedTextures = textureRegion.split(40, 23)[0];
		animation = new Animation(period, animatedTextures);
		animation.setPlayMode(PlayMode.LOOP);
		sprite = new Sprite(animation.getKeyFrame(elapsedTime, true));
		sprite.setX(x);
		sprite.setY(y);
		bounds = new Rectangle(x, y, 40, 27);
		floorBounds = new Rectangle(x + 2, y, 40 - 4, 27);
	}

	@Override
	public void render(SpriteBatch batch) {

		super.render(batch);

		floorBounds.x = x + 2;
		floorBounds.y = y;

		if (jumping) {
			y += jumpSpeed; // Determines jump height
			jumpSpeed += dy; // Determines jump duration
		}

		if (onFloor) {
			jumping = false;
			jumpSpeed = 0;
		}

		if (jumpSpeed < 0) {
			jumping = false;
			jumpSpeed = 0;
		}

		dx = 0;
		moving = false;
		if (!dead) {
			if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
				moving = true;
				dx += speed;
			}
			if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
				moving = true;
				dx -= speed;
			}
			if (transformed)
				if (Gdx.input.isKeyPressed(Keys.SPACE)) {
					if (onFloor) {
						jumping = true;
						jumpSpeed = defaultJumpSpeed;
					}
				}
		}
		if (region == animation.getKeyFrame(0.6f, true)) {
			if (!alreadyPlayed) {
				assetManager.get("chara_move.wav", Sound.class).play(0.5f);
				alreadyPlayed = true;
			}
		} else
			alreadyPlayed = false;
	}

	public void transform() {
		if (!transformed) {
			assetManager.get("chara_death.wav", Sound.class).play();
			textureRegion = new TextureRegion(assetManager.get("chara_legs_w.png", Texture.class));
			animatedTextures = textureRegion.split(40, 27)[0];
			period = 0.1f;
			speed = 2;
			animation = new Animation(period, animatedTextures);
			animation.setPlayMode(PlayMode.LOOP);
			transformed = true;
		}
	}

	public void die() {
		dead = true;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public boolean hasTransformed(){
		return transformed;
	}

	public float getSpeed() {
		return speed;
	}

	public float getJumpSpeed() {
		return defaultJumpSpeed;
	}

	public Rectangle getFloorBounds() {
		return floorBounds;
	}

}
