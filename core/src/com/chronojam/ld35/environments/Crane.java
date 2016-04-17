package com.chronojam.ld35.environments;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Crane extends Environment {
	
	public Crane(AssetManager assetManager, float x, float y){
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;
		
		texture = assetManager.get("crane.png", Texture.class);
		bounds = new Rectangle(x, y, getWidth(), getHeight());
	}
	
	public void render(SpriteBatch batch){
		super.render(batch);
		
	}

}
