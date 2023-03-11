package com.fatih.roguelike.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.I18NBundle

class LoadingUI(private val stage:Stage,private val skin: Skin,private val i18NBundle: I18NBundle) : Table(skin) {


    private val progressBar: ProgressBar
    private val textButton: TextButton
    private val pressAnyKey: TextButton

    init {
        setFillParent(true)
        progressBar = ProgressBar(0f,1f,0.01f,false,skin,"default").apply {
            setAnimateDuration(1f)
        }
        textButton = TextButton("[RED]Loading...",skin,"huge").apply {
            label.wrap=true
        }
        pressAnyKey=TextButton("Press any key",skin,"normal").apply {
            label.wrap=true
            isVisible=false
        }
        add(pressAnyKey).expand().fill().center().row()
        add(textButton).expandX().fillX().bottom().apply {
            padBottom(5f)
            row()
        }
        add(progressBar).expandX().fillX().bottom().padBottom(15f)

    }

    fun setProgress(progress:Float){
        progressBar.value = progress
        if (progress >=1f && !pressAnyKey.isVisible){
            pressAnyKey.apply {
                isVisible=true
                setColor(1f,1f,1f,0f)
                addAction(Actions.forever(Actions.sequence(Actions.alpha(1f,1f),Actions.alpha(0f,1f))))
            }
        }
    }

}
