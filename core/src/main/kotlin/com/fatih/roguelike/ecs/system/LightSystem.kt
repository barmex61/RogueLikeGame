package com.fatih.roguelike.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.ecs.components.Box2dComponent

class LightSystem() : IteratingSystem(Family.all(Box2dComponent::class.java).get()) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val box2dComponent=ECSEngine.box2dComponentMapper.get(entity)
        if (box2dComponent.light!=null && box2dComponent.lightFluctuationDistance >0f){
            box2dComponent.lightFluctuationTime+=box2dComponent.lightFluctuationSpeed*deltaTime
            if (box2dComponent.lightFluctuationTime>=MathUtils.PI2){
                box2dComponent.lightFluctuationTime=0f
            }
            box2dComponent.light!!.distance=box2dComponent.lightDistance + MathUtils.sin(box2dComponent.lightFluctuationTime * box2dComponent.lightFluctuationDistance)*20f
        }
    }
}
