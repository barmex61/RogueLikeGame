package com.fatih.roguelike.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.ecs.components.RemoveComponent
import com.fatih.roguelike.types.GameObjectType
import com.fatih.roguelike.util.WorldContactListener

class PlayerCollisionSystem() :IteratingSystem(Family.all(RemoveComponent::class.java).get()),WorldContactListener.PlayerCollisionListener {

    init {
        RogueLikeGame.worldContactListener.playerCollisionListener=this
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        engine.removeEntity(entity)
    }

    override fun playerCollision(player: Entity, gameObject: Entity) {
        val gameObjComponent = ECSEngine.gameObjectMapper.get(gameObject)
        when(gameObjComponent.type){
            GameObjectType.CRYSTAL->{
                gameObject.add((engine as ECSEngine).createComponent(RemoveComponent::class.java))
            }
            GameObjectType.AXE->{
                ECSEngine.playerComponentMapper.get(player).hasAxe=true
                gameObject.add((engine as ECSEngine).createComponent(RemoveComponent::class.java))
            }
            GameObjectType.TREE->{
                if (ECSEngine.playerComponentMapper.get(player).hasAxe){
                    gameObject.add((engine as ECSEngine).createComponent(RemoveComponent::class.java))
                }
            }
            else -> Unit
        }
    }

}
