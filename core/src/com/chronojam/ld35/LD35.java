package com.chronojam.ld35;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.chronojam.ld35.screens.GameScreen;

public class LD35 extends Game {
	private GameScreen gameScreen;
	private AssetManager assetManager;
	private boolean loaded = false;
	
	@Override
	public void create () {
		assetManager = new AssetManager();
		assetManager.load("chara_w.png", Texture.class);
		assetManager.load("floor.png", Texture.class);
		assetManager.load("wall_left.png", Texture.class);
		assetManager.load("wall_right.png", Texture.class);
		assetManager.load("wall_middle.png", Texture.class);
		assetManager.load("ceiling.png", Texture.class);
		assetManager.load("background.png", Texture.class);
		assetManager.load("elevator.png", Texture.class);
		assetManager.load("elevator_back.png", Texture.class);
		assetManager.load("button.png", Texture.class);
		assetManager.load("button_base.png", Texture.class);
		assetManager.load("crane.png", Texture.class);
		assetManager.load("box.png", Texture.class);
		assetManager.load("cats.png", Texture.class);
		assetManager.load("cat_death.png", Texture.class);
		assetManager.load("cat_front.png", Texture.class);
		assetManager.load("cat_back.png", Texture.class);
		assetManager.load("chara_legs_w.png", Texture.class);
		assetManager.load("cat_attack.png", Texture.class);
		assetManager.load("blood1.png", Texture.class);
		assetManager.load("blood2.png", Texture.class);
		assetManager.load("blood3.png", Texture.class);
		
		assetManager.load("chara_move.wav", Sound.class);
		assetManager.load("chara_death.wav", Sound.class);
		assetManager.load("cat_death.wav", Sound.class);
		assetManager.load("box.wav", Sound.class);
		assetManager.load("elevator.wav", Sound.class);
		
		assetManager.load("Save Your Life.mp3", Music.class);
	}

	@Override
	public void render () {
		super.render();
		if(!loaded){
			if(assetManager.update()){
				gameScreen = new GameScreen(this, assetManager);
				setScreen(gameScreen);
				loaded = true;
			}
		}
	}
}
