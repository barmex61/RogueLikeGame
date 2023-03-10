package com.fatih.roguelike.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.viewport.FitViewport
import com.fatih.roguelike.RogueLikeGame

abstract class AbstractScreen():Screen {

    protected val viewPort:FitViewport=RogueLikeGame.screenViewPort

    override fun resize(width: Int, height: Int) {
        viewPort.update(width,height)
    }
}
