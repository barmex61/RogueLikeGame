package com.fatih.roguelike.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.maps.tiled.TiledMap
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.util.Constants.setScreen

class LoadingScreen : AbstractScreen() {

    init {
       try {
         RogueLikeGame.assetManager.load("map/map.tmx",TiledMap::class.java)
       } catch (e:Exception){
           e.printStackTrace()
       }
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f,1f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        if (RogueLikeGame.assetManager.update()){
            setScreen?.invoke(ScreenType.GAME)
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
    }


}


