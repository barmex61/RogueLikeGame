package com.fatih.roguelike.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.ecs.components.AnimationComponent
import com.fatih.roguelike.ecs.components.Box2dComponent
import com.fatih.roguelike.types.AnimationType

class PlayerAnimationSystem : IteratingSystem(Family.all(AnimationComponent::class.java,Box2dComponent::class.java).get()) {

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val animComponent= ECSEngine.animationComponentMapper.get(entity)
        val box2dComponent=ECSEngine.box2dComponentMapper.get(entity)
        if (box2dComponent.body!!.linearVelocity.equals(Vector2.Zero)){
            animComponent.animTime=0f
        }else if (box2dComponent.body!!.linearVelocity.x>0){
            animComponent.animType = AnimationType.HERO_MOVE_RIGHT
        }else if (box2dComponent.body!!.linearVelocity.x<0){
            animComponent.animType = AnimationType.HERO_MOVE_LEFT
        }else if (box2dComponent.body!!.linearVelocity.y>0){
            animComponent.animType = AnimationType.HERO_MOVE_UP
        }else if (box2dComponent.body!!.linearVelocity.y<0){
            animComponent.animType = AnimationType.HERO_MOVE_DOWN
        }
    }
}
