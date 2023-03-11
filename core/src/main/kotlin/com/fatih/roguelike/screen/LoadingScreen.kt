package com.fatih.roguelike.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.audio.AudioType
import com.fatih.roguelike.input.GameKeys
import com.fatih.roguelike.input.InputListener
import com.fatih.roguelike.input.InputManager
import com.fatih.roguelike.ui.LoadingUI
import com.fatih.roguelike.util.Constants.setScreen

class LoadingScreen : AbstractScreen<LoadingUI>() ,InputListener{

    private var isMusicLoaded=false

    init {
        RogueLikeGame.assetManager.load("map/map.tmx",TiledMap::class.java)
        for (audioType in AudioType.values()){
            RogueLikeGame.assetManager.load(audioType.path,if (audioType.isMusic) Music::class.java else Sound::class.java)
        }

    }

    override fun hide() {
        super.hide()
        audioManager.stopAudio()
    }

    override val screenUI: LoadingUI = getScreenUI(RogueLikeGame.skin)


    override fun getScreenUI(skin: Skin, i18NBundle: I18NBundle): LoadingUI {
        return LoadingUI(stage,skin,i18NBundle)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        RogueLikeGame.assetManager.apply {
            update()
            if (this.isLoaded(AudioType.INTRO.path)&&!isMusicLoaded){
                isMusicLoaded=true
                audioManager.playAudio(AudioType.INTRO)
            }
        }
        screenUI.setProgress(RogueLikeGame.assetManager.progress)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

    override fun keyPressed(inputManager: InputManager, key: GameKeys) {
        audioManager.playAudio(AudioType.SELECT)
        if (RogueLikeGame.assetManager.progress >= 1f){
            setScreen?.invoke(ScreenType.GAME)
        }
    }

    override fun keyRelease(inputManager: InputManager, key: GameKeys) {

    }


}


