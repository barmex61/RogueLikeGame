package com.fatih.roguelike.screen

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.RogueLikeGame.Companion.mapManager
import com.fatih.roguelike.RogueLikeGame.Companion.preferenceManager
import com.fatih.roguelike.RogueLikeGame.Companion.skin
import com.fatih.roguelike.input.GameKeys
import com.fatih.roguelike.input.InputManager
import com.fatih.roguelike.types.MapType
import com.fatih.roguelike.ui.GameUI

class GameScreen: AbstractScreen<GameUI>()  {

    private val player:Entity
    init {
        mapManager.apply {
            setMap(MapType.MAP_1)
        }
       player =  RogueLikeGame.ecsEngine.createPlayer(mapManager.getCurrentMap()!!.startLocation,30f,30f)
    }


    override val screenUI: GameUI = getScreenUI(skin)

    override fun getScreenUI(skin: Skin, i18NBundle: I18NBundle): GameUI {
        return GameUI(skin,i18NBundle)
    }

    override fun render(delta: Float) {

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            mapManager.setMap(MapType.MAP_1)
        }else if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
            mapManager.setMap(MapType.MAP_2)
        }else if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
            preferenceManager.saveGameState(player)
        }else if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
            preferenceManager.loadGameState(player)
        }
    }


    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
    }

    override fun keyPressed(inputManager: InputManager, key: GameKeys) {
    }

    override fun keyRelease(inputManager: InputManager, key: GameKeys) {

    }
}
