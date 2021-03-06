package com.gcupko.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Example extends InputAdapter implements ApplicationListener
{
	SpriteBatch sb;
	Array<MovingSprite> movingSprites;

	@Override
	public void create () {
		sb = new SpriteBatch();
		movingSprites = new Array<MovingSprite>();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void resize(int width, int height) {
		sb = new SpriteBatch();
		movingSprites = new Array<MovingSprite>();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		for (MovingSprite sprite : movingSprites) {
			sb.draw(sprite.getFigure(), sprite.getPosition().x, sprite.getPosition().y);
		}
		sb.end();
	}

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void dispose () {
		sb.dispose();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)	{
		for (MovingSprite sprite : movingSprites) {
			if (sprite.isTouched()) {
				sprite.setDragging(true);
				return true;
			}
		}

		MovingSprite newSprite = new MovingSprite(screenX, Gdx.graphics.getHeight() - screenY);

		if (!collision(newSprite)) {
			movingSprites.add(newSprite);
		}
		else {
			newSprite.dispose();
		}

		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for (MovingSprite sprite : movingSprites) {
			if (sprite.isDragging() && !collision(sprite)) {
				sprite.update(Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
			}
			if (collision(sprite) && !sprite.isDragging()) {
				movingSprites.removeValue(sprite, false);
				sprite.dispose();
			}
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (MovingSprite sprite : movingSprites) {
			if (sprite.isDragging()) {
				sprite.setDragging(false);
			}
		}
		return true;
	}

	private boolean collision(MovingSprite s)
	{
		for (int i = 0; i < movingSprites.size; i++) {
			MovingSprite sprite = movingSprites.get(i);
			if (sprite != s) {
				if (s.collides(sprite.getBounds())) {
					return true;
				}
			}
		}
		return false;
	}
}
