package com.fatih.roguelike.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.RogueLikeGame.Companion.assetManager
import com.fatih.roguelike.RogueLikeGame.Companion.spriteBatch
import com.fatih.roguelike.RogueLikeGame.Companion.world
import com.fatih.roguelike.util.Constants.BIT_GROUND
import com.fatih.roguelike.util.Constants.BIT_PLAYER
import com.fatih.roguelike.util.Constants.UNIT_SCALE
import com.fatih.roguelike.map.Map

class GameScreen: AbstractScreen() {

    private var fixtureDef=FixtureDef()
    private var bodyDef=BodyDef()
    private lateinit var player:Body
    private var map:Map
    private var orthogonalTiledMapRenderer= OrthogonalTiledMapRenderer(null, UNIT_SCALE,spriteBatch)
    private var speedX=0f
    private var speedY=0f

    companion object{
        lateinit var tileMap: TiledMap
    }

    init {
        tileMap = assetManager.get("map/map.tmx",TiledMap::class.java)
        map=Map(tileMap, arrayListOf())
        spawnCollisionAreas()
        spawnPlayer()
    }




    private fun resetBodyAndFixtureDefinition(){
        bodyDef.apply {
            position.set(0f,0f)
            gravityScale=1f
            type=BodyDef.BodyType.StaticBody
            fixedRotation=false
        }
        fixtureDef.apply {
            density=0f
            isSensor=false
            restitution=0f
            friction=0.2f
            filter.categoryBits=0x0001
            filter.maskBits=-1
            fixtureDef.shape=null
        }
    }

    private fun spawnPlayer(){

        resetBodyAndFixtureDefinition()

        val myShape= PolygonShape().apply { setAsBox(10f,10f) }

        fixtureDef=FixtureDef().apply {
            isSensor=false
            restitution=0.5f
            friction=0.2f
            filter.categoryBits = BIT_PLAYER
            filter.maskBits= BIT_GROUND
            shape=myShape
        }
        bodyDef=BodyDef().apply {
            position.set(Map.startLocation)
            type=BodyDef.BodyType.DynamicBody
            fixedRotation=true
        }.also {
            player=world.createBody(it).apply {
                userData="Player"
                createFixture(fixtureDef)
            }
            myShape.dispose()
        }
    }

    private fun spawnCollisionAreas(){
        for (collisionArea in map.collisionArea){
            resetBodyAndFixtureDefinition()
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

    override fun show() {
        orthogonalTiledMapRenderer.map= tileMap
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        speedX = if (Gdx.input.isKeyPressed(Input.Keys.A)){ -100f }else if (Gdx.input.isKeyPressed(Input.Keys.D)){ 100f }else{ 0f }
        speedY = if (Gdx.input.isKeyPressed(Input.Keys.S)){ -100f }else if (Gdx.input.isKeyPressed(Input.Keys.W)){ 100f }else{ 0f }
        player.applyLinearImpulse(
            (speedX-player.linearVelocity.x) * player.mass,
            (speedY - player.linearVelocity.y) * player.mass,
            player.worldCenter.x,
            player.worldCenter.y,
            true
        )
        viewPort.apply(true)
        orthogonalTiledMapRenderer.setView(RogueLikeGame.gameCamera)
        orthogonalTiledMapRenderer.render()
        RogueLikeGame.debugRenderer.render(RogueLikeGame.world,viewPort.camera.combined)

    }


    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        orthogonalTiledMapRenderer.dispose()
    }


}
