package com.chronojam.ld35.environments;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Box extends Environment {
	private boolean falling = false;
	private float dy = -6;
	private float destY;
	private boolean arrived = false;
	private boolean released = false;

	public Box(AssetManager assetManager, float x, float y, float destY) {
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;
		this.destY = destY;

		texture = assetManager.get("box.png", Texture.class);
		bounds = new Rectangle(x, y, getWidth(), getHeight());
	}

	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y, getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), 1, 1, 45, 0, 0, (int) getWidth(), (int) getHeight(), false, false);
		if (falling) {
			if (y > destY)
				y += dy;
			else if (y < destY) {
				y = destY;
				falling = false;
				arrived = true;
			}
		}
	}

	public void release() {
		if (!released) {
			falling = true;
			released = true;
			assetManager.get("box.wav", Sound.class).play();
		}
	}

	public boolean isArrived() {
		return arrived;
	}

}
