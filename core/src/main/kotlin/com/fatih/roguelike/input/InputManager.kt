package com.fatih.roguelike.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.fatih.roguelike.util.Constants

class InputManager : InputProcessor {

    private val keyMap = hashMapOf<Int,GameKeys>()
    private val keyState = Array(GameKeys.values().size){false}
    var inputListener: InputListener?=null

    init {
        for (gameKey in GameKeys.values()){
            for (key in gameKey.keyCode){
                keyMap[key]=gameKey
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        val gameKey = keyMap[keycode]
        gameKey?.let {
            notifyKeyDown(it)
        }?:return false
        return false
    }

    fun notifyKeyDown(gameKeys: GameKeys){
        keyState[gameKeys.ordinal] = true
        inputListener?.keyPressed(this,gameKeys)
    }

    override fun keyUp(keycode: Int): Boolean {
        val gameKey = keyMap[keycode]
        gameKey?.let {
            notifyKeyUp(it)
        }?:return false
        return false
    }

    fun notifyKeyUp(gameKeys: GameKeys){
        keyState[gameKeys.ordinal] = false
        inputListener?.keyRelease(this,gameKeys)
    }

    fun isKeyPressed(gameKeys: GameKeys): Boolean {
        return keyState[gameKeys.ordinal]
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}
