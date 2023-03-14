package com.fatih.roguelike.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.input.InputListener

abstract class AbstractScreen <T:Table>:Screen ,InputListener{

    protected val viewPort:FitViewport=RogueLikeGame.screenViewPort
    abstract val screenUI:T
    protected val stage:Stage=RogueLikeGame.stage
    private val inputManager=RogueLikeGame.inputManager
    protected val audioManager=RogueLikeGame.audioManager

    companion object{
        var sendMessage:((Boolean)->Unit)?=null
    }

    override fun resize(width: Int, height: Int) {
        viewPort.update(width,height)
        stage.viewport.update(width,height,true)
        RogueLikeGame.rayHandler.useCustomViewport(viewPort.screenX,viewPort.screenY,viewPort.screenWidth,viewPort.screenHeight)
    }

    protected abstract fun getScreenUI(skin: Skin , i18NBundle: I18NBundle = RogueLikeGame.i18NBundle):T

    override fun show() {
        stage.addActor(screenUI)
        inputManager.inputListener=this
        if (this is GameScreen){
            sendMessage?.invoke(true)
        }
    }

    override fun hide() {
        stage.root.removeActor(screenUI)
        inputManager.inputListener=null
    }
}
