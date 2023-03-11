package com.fatih.roguelike.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.ui.LoadingUI
import com.fatih.roguelike.util.Constants.setScreen

class LoadingScreen : AbstractScreen<LoadingUI>() {

    init {
        RogueLikeGame.assetManager.load("map/map.tmx",TiledMap::class.java)
    }

    override val screenUI: LoadingUI = getScreenUI(RogueLikeGame.skin)


    override fun getScreenUI(skin: Skin, i18NBundle: I18NBundle): LoadingUI {
        return LoadingUI(stage,skin,i18NBundle)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        if (RogueLikeGame.assetManager.update()){
          //  setScreen?.invoke(ScreenType.GAME)
        }
        screenUI.setProgress(RogueLikeGame.assetManager.progress)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }


}


