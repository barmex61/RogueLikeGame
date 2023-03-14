package com.fatih.roguelike.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.ecs.components.ParticleEffectComponent
import com.fatih.roguelike.types.ParticleEffectType
import java.util.EnumMap

class ParticleEffectSystem : IteratingSystem(Family.all(ParticleEffectComponent::class.java).get()) {

    private val effectPool : EnumMap<ParticleEffectType,ParticleEffectPool> = EnumMap(ParticleEffectType::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        ECSEngine.particleEffectComponentMapper.get(entity).apply {
            particleEffect?.let {
                it.update(deltaTime)
                if (it.isComplete){
                    entity?.remove(ParticleEffectComponent::class.java)
                }
            }
            effectType?.let {
                effectPool[it].apply {
                    if (this == null){
                        val particleEffect=RogueLikeGame.assetManager.get(it.filePath,ParticleEffect::class.java)
                        particleEffect.setEmittersCleanUpBlendFunction(false)
                        effectPool[it] = ParticleEffectPool(particleEffect,1,128)
                    }else{
                        particleEffect=this.obtain().apply {
                            setPosition(effectPosition.x,effectPosition.y)
                            scaleEffect(scaling)
                        }
                    }
                }


            }
        }
    }
}
