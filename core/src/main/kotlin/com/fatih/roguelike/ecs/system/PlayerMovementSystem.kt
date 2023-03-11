package com.fatih.roguelike.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.ecs.components.Box2dComponent
import com.fatih.roguelike.ecs.components.PlayerComponent
import com.fatih.roguelike.input.GameKeys
import com.fatih.roguelike.input.InputListener
import com.fatih.roguelike.input.InputManager
import com.fatih.roguelike.screen.AbstractScreen
import com.fatih.roguelike.util.Constants

class PlayerMovementSystem : IteratingSystem(Family.all(PlayerComponent::class.java, Box2dComponent::class.java).get()), InputListener {

    private var directionChange=false
    private var xFactor=0f
    private var yFactor=0f

    init {
        AbstractScreen.sendMessage = {
            if (it){
                RogueLikeGame.inputManager.inputListener=this
            }
        }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {

        if (directionChange){
            val playerComponent =  ECSEngine.playerComponentMapper.get(entity)
            val box2dComponent = ECSEngine.box2dComponentMapper.get(entity)
            directionChange=false
            val body=box2dComponent!!.body
            body!!.applyLinearImpulse(
                (xFactor*playerComponent.speed.x - body.linearVelocity.x) * body.mass,
                (yFactor*playerComponent.speed.y - body.linearVelocity.y) * body.mass,
                body.worldCenter.x,
                body.worldCenter.y,
                true
            )
        }
    }

    override fun keyPressed(inputManager: InputManager, key: GameKeys) {
        when(key){
            GameKeys.LEFT->{
                directionChange=true
                xFactor=-1f
            }
            GameKeys.RIGHT->{
                directionChange=true
                xFactor=1f
            }
            GameKeys.DOWN->{
                directionChange=true
                yFactor=-1f
            }
            GameKeys.UP->{
                directionChange=true
                yFactor=1f
            }
            else->{}
        }
    }

    override fun keyRelease(inputManager: InputManager, key: GameKeys) {
        when(key){
            GameKeys.LEFT->{
                directionChange=true
                xFactor=if (inputManager.isKeyPressed(GameKeys.RIGHT)) 1f else 0f
                Gdx.app.debug(Constants.TAG,xFactor.toString())
            }
            GameKeys.RIGHT->{
                directionChange=true
                xFactor=if (inputManager.isKeyPressed(GameKeys.LEFT)) -1f else 0f
            }
            GameKeys.DOWN->{
                directionChange=true
                yFactor=if (inputManager.isKeyPressed(GameKeys.UP)) 1f else 0f
            }
            GameKeys.UP->{
                directionChange=true
                yFactor=if (inputManager.isKeyPressed(GameKeys.DOWN)) -1f else 0f
            }
            else->{}
        }
    }

}
