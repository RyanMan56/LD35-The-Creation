package com.chronojam.ld35.environments;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.chronojam.ld35.images.ImageProvider;

public class Environment {
	protected AssetManager assetManager;
	protected Texture texture;
	protected float x, y;
	protected ImageProvider imageProvider = new ImageProvider();
	protected Rectangle bounds;
	
	public void render(SpriteBatch batch){
		batch.draw(texture, x, y);
	}
	
	public void setX(float x){
		this.x = x;
		bounds.x = x;
	}
	
	public void setY(float y){
		this.y = y;
		bounds.y = y;
	}
	
	public void setPos(float x, float y){
		this.x = x;
		this.y = y;
		bounds.x = x;
		bounds.y = y;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getWidth(){
		return texture.getWidth();
	}
	
	public float getHeight(){
		return texture.getHeight();
	}
	
	public Rectangle getBounds(){
		return bounds;
	}

}
