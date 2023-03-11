package com.fatih.roguelike.input

interface InputListener {

    fun keyPressed(inputManager: InputManager,key:GameKeys)
    fun keyRelease(inputManager: InputManager,key:GameKeys)
}
