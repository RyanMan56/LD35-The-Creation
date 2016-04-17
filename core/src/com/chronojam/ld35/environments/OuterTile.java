package com.chronojam.ld35.environments;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class OuterTile extends Environment {

	// Nothing = 0, Bottom = 1, Left = 2, Right = 3, Top = 4, Middle = 5
	public OuterTile(AssetManager assetManager, float x, float y, int tileType){
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;
		
		switch(tileType){
		case 1:
			texture = assetManager.get("floor.png", Texture.class);
			break;
		case 2:
			texture = assetManager.get("wall_left.png", Texture.class);
			break;
		case 3:
			texture = assetManager.get("wall_right.png", Texture.class);
			break;
		case 4:
			texture = assetManager.get("ceiling.png", Texture.class);
			break;
		case 5:
			texture = assetManager.get("wall_middle.png", Texture.class);
			break;
		}
		
		bounds = new Rectangle(x, y, getWidth(), getHeight());
	}
	
}
