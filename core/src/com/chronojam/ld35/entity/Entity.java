package com.chronojam.ld35.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.chronojam.ld35.images.ImageProvider;

public class Entity {
	protected AssetManager assetManager;
	protected TextureRegion textureRegion;
	protected TextureRegion[] animatedTextures;
	protected float period = 1 / 5f;
	protected float elapsedTime = 0;
	protected Animation animation;
	protected Sprite sprite;
	protected float health = 100;
	protected float x, y;
	protected float dx, dy, baseDy;
	protected Rectangle bounds;
	protected ImageProvider imageProvider = new ImageProvider();
	protected boolean onFloor = false;
	protected boolean moving = false;
	protected float rotation = 0;
	protected TextureRegion region;
	protected boolean looping = true;

	public void render(SpriteBatch batch) {
		x += dx;
		y += dy;
		bounds.x = x;
		bounds.y = y;

		if (health > 0) {
			if (moving) {
				if (dy == 0) {
					elapsedTime += Gdx.graphics.getDeltaTime();
				}
			} else {
				elapsedTime = 0;
			}
			region = animation.getKeyFrame(elapsedTime, true);
			if (dx < 0) {
				if (!region.isFlipX())
					region.flip(true, false);
			} else if (dx > 0)
				if (region.isFlipX())
					region.flip(true, false);

			sprite.setRegion(animation.getKeyFrame(elapsedTime, looping));

		}

		sprite.setPosition(x, y);
		sprite.draw(batch);
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}

	public void setOnFloor(boolean onFloor) {
		this.onFloor = onFloor;
	}

	public boolean isOnFloor() {
		return onFloor;
	}
	
	public void setDx(float dx) {
		this.dx = dx;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

}
