package com.fatih.roguelike.ecs

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.ecs.components.Box2dComponent
import com.fatih.roguelike.ecs.components.PlayerComponent
import com.fatih.roguelike.ecs.system.PlayerCameraSystem
import com.fatih.roguelike.ecs.system.PlayerMovementSystem
import com.fatih.roguelike.util.Constants.BIT_GROUND
import com.fatih.roguelike.util.Constants.BIT_PLAYER

class ECSEngine:PooledEngine() {

    private var bodyDef : BodyDef = BodyDef()
    private var fixtureDef : FixtureDef = FixtureDef()

    companion object{
        val playerComponentMapper: ComponentMapper<PlayerComponent> =ComponentMapper.getFor(PlayerComponent::class.java)
        val box2dComponentMapper: ComponentMapper<Box2dComponent> =ComponentMapper.getFor(Box2dComponent::class.java)
    }

    init {
        addSystem(PlayerMovementSystem())
        addSystem(PlayerCameraSystem())
    }

    private fun resetBodyAndFixtureDefinition(){
        bodyDef.apply {
            position.set(0f,0f)
            gravityScale=1f
            type= BodyDef.BodyType.StaticBody
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

    fun createPlayer(playerSpawnLocation:Vector2, width:Float, height:Float){
        val playerComponent=createComponent(PlayerComponent::class.java).apply {
            speed.set(60f,60f)
        }
        val player= this.createEntity().apply {
            add(playerComponent)
        }
        resetBodyAndFixtureDefinition()
        bodyDef.apply {
            position.set(playerSpawnLocation.x,playerSpawnLocation.y + height * 0.5f )
            fixedRotation=true
            type=BodyDef.BodyType.DynamicBody
        }
        val shape=PolygonShape().apply {
            setAsBox(width*0.5f,height*0.5f)
        }
        fixtureDef.apply {
            filter.categoryBits=BIT_PLAYER
            filter.maskBits= BIT_GROUND
            this.shape=shape
        }
        val box2dComponent = createComponent(Box2dComponent::class.java).apply {
            body = RogueLikeGame.world.createBody(bodyDef)
            body?.userData = "PLAYER"
            this.width=width
            this.height=height
            body?.createFixture(fixtureDef)
            shape.dispose()
        }
        player.add(box2dComponent)
        addEntity(player)
    }
}
