package com.fatih.roguelike.input

import com.badlogic.gdx.Input

enum class GameKeys(vararg val keyCode:Int){
    UP(Input.Keys.UP,Input.Keys.W),
    DOWN(Input.Keys.DOWN,Input.Keys.S),
    RIGHT(Input.Keys.D,Input.Keys.RIGHT),
    LEFT(Input.Keys.A,Input.Keys.LEFT),
    SELECT(Input.Keys.ENTER,Input.Keys.SPACE),
    BACK(Input.Keys.BACKSPACE,Input.Keys.ESCAPE)
}
