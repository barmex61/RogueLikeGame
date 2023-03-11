package com.fatih.roguelike.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.I18NBundle
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.RogueLikeGame.Companion.assetManager
import com.fatih.roguelike.RogueLikeGame.Companion.skin
import com.fatih.roguelike.RogueLikeGame.Companion.spriteBatch
import com.fatih.roguelike.RogueLikeGame.Companion.world
import com.fatih.roguelike.input.GameKeys
import com.fatih.roguelike.input.InputManager
import com.fatih.roguelike.util.Constants.BIT_GROUND
import com.fatih.roguelike.util.Constants.UNIT_SCALE
import com.fatih.roguelike.map.Map
import com.fatih.roguelike.ui.GameUI

class GameScreen: AbstractScreen<GameUI>()  {

    private var map:Map
    private var orthogonalTiledMapRenderer= OrthogonalTiledMapRenderer(null, UNIT_SCALE,spriteBatch)
    private var tileMap:TiledMap = assetManager.get("map/map.tmx",TiledMap::class.java)


    init {
        orthogonalTiledMapRenderer.map= tileMap
        map=Map(tileMap, arrayListOf())
        spawnCollisionAreas()
        RogueLikeGame.ecsEngine.createPlayer(map.startLocation,20f,20f)
    }

    private fun spawnCollisionAreas(){
        val fixtureDef=FixtureDef()
        val bodyDef=BodyDef()
        for (collisionArea in map.collisionArea){
            val myShape=ChainShape().apply { createChain(collisionArea.vertices) }
            fixtureDef.apply {
                filter.categoryBits = BIT_GROUND
                filter.maskBits = -1
                shape=myShape

            }
            bodyDef.apply {
                position.set(collisionArea.positionX,collisionArea.positionY)
                fixedRotation=true
                world.createBody(this).also {
                    it.userData="GROUND"
                    it.createFixture(fixtureDef)
                }
            }
           myShape.dispose()
        }
    }

    override val screenUI: GameUI = getScreenUI(skin)

    override fun getScreenUI(skin: Skin, i18NBundle: I18NBundle): GameUI {
        return GameUI(skin,i18NBundle)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewPort.apply(false)
        orthogonalTiledMapRenderer.apply {
            setView(RogueLikeGame.gameCamera)
            render()
        }
        RogueLikeGame.debugRenderer.render(world,viewPort.camera.combined)

    }


    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        orthogonalTiledMapRenderer.dispose()
    }

    override fun keyPressed(inputManager: InputManager, key: GameKeys) {
        //
    }

    override fun keyRelease(inputManager: InputManager, key: GameKeys) {
        //
    }

}
