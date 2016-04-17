package com.chronojam.ld35.environments;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Button extends Environment{
	private Texture buttonTexture;
	private Rectangle buttonBounds;
	private boolean pressed = false;
	
	public Button(AssetManager assetManager, float x, float y){
		this.assetManager = assetManager;
		this.x = x;
		this.y = y;
		
		texture = assetManager.get("button_base.png", Texture.class);
		buttonTexture = assetManager.get("button.png", Texture.class);
		
		this.bounds = new Rectangle(x, y, getWidth(), getHeight());
		this.buttonBounds = new Rectangle(x+10, y+getHeight(), buttonTexture.getWidth(), buttonTexture.getHeight());
	}
	
	public void render(SpriteBatch batch){
		if(pressed)
			buttonBounds.y = y+getHeight()/2;
		batch.draw(buttonTexture, buttonBounds.x, buttonBounds.y);
		super.render(batch);
	}
	
	public Rectangle getButtonBounds(){
		return buttonBounds;
	}
	
	public void setPressed(boolean pressed){
		this.pressed = pressed;
	}

}
