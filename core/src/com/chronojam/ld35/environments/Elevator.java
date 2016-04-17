package com.chronojam.ld35.environments;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Elevator extends Environment {
	private Texture backTexture;
	private Rectangle topBounds;
	private float startX, startY;
	private float destX, destY;
	private boolean moving = false;
	public final static int NONE = 0;
	public final static int UP = 1;
	public final static int DOWN = 2;
	private int direction = NONE;

	public Elevator(AssetManager assetManager, float x, float y, float destX, float destY) {
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;
		this.startX = x;
		this.startY = y;
		this.destX = destX;
		this.destY = destY;
		texture = assetManager.get("elevator.png", Texture.class);
		backTexture = assetManager.get("elevator_back.png", Texture.class);

		bounds = new Rectangle(x, y, getWidth(), getHeight());
		topBounds = new Rectangle(x, backTexture.getHeight() * 1.5f - 8, getWidth(), 8);

	}

	public void render(SpriteBatch batch) {
		batch.draw(backTexture, x, y);
		batch.draw(texture, topBounds.x, topBounds.y);
		super.render(batch);

		moving = false;	
		
		if (direction == UP){
			if (y != destY) {
				y++;
				bounds.y++;
				topBounds.y++;
				moving = true;
			}else
				direction = NONE;
		}else if(direction == DOWN){
			if(y != startY){
				y--;
				bounds.y--;
				topBounds.y--;
				moving = true;
			}else
				direction = NONE;
		}

		if(moving){
			assetManager.get("elevator.wav", Sound.class).play(0.05f);
		}
		
	}

	public Rectangle getTopBounds() {
		return topBounds;
	}

	public boolean isMoving() {
		return moving;
	}
	
	public void setDirection(int direction){
		this.direction = direction;
	}
}
