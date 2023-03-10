@file:JvmName("Lwjgl3Launcher")

package com.fatih.roguelike.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.fatih.roguelike.RogueLikeGame

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(RogueLikeGame(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("RogueLike")
        RogueLikeGame.sizeLambda = { width,height ->
            setWindowedMode(width,height)
        }
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
