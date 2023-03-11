package com.fatih.roguelike.ui

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.I18NBundle

class GameUI(private val skin: Skin,private val i18NBundle: I18NBundle):Table(skin) {

    init {
        setFillParent(true)
        add(TextButton("Blub",skin,"huge"))
    }
}
