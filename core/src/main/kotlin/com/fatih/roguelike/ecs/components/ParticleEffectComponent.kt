package com.fatih.roguelike.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import com.fatih.roguelike.types.ParticleEffectType

class ParticleEffectComponent: Component,Pool.Poolable {

    var particleEffect:ParticleEffectPool.PooledEffect?=null
    var effectType: ParticleEffectType?=null
    val effectPosition=Vector2()
    var scaling:Float=0f

    override fun reset() {
        if (particleEffect!=null){
            particleEffect!!.free()
            particleEffect=null

        }
        effectPosition.set(0f,0f)
        effectType=null
        scaling=0f
    }
}
