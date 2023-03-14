package com.fatih.roguelike.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.ecs.components.AnimationComponent

class AnimationSystem : IteratingSystem(Family.all(AnimationComponent::class.java).get()) {

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val animComponent=ECSEngine.animationComponentMapper.get(entity)
        animComponent.animTime +=deltaTime
    }
}
