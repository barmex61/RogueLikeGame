package com.fatih.roguelike.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.fatih.roguelike.RogueLikeGame

abstract class AbstractScreen <T:Table>:Screen {

    protected val viewPort:FitViewport=RogueLikeGame.screenViewPort
    abstract val screenUI:T
    protected val stage:Stage=RogueLikeGame.stage


    override fun resize(width: Int, height: Int) {
        viewPort.update(width,height)
        stage.viewport.update(width,height,true)
    }

    protected abstract fun getScreenUI(skin: Skin , i18NBundle: I18NBundle = RogueLikeGame.I18NBundle):T

    override fun show() {
        stage.addActor(screenUI)
    }

    override fun hide() {
        stage.root.removeActor(screenUI)
    }
}
