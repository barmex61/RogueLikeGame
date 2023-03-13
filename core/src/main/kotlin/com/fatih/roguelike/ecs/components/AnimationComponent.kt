package com.fatih.roguelike.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.fatih.roguelike.types.AnimationType

class AnimationComponent : Component, Pool.Poolable {

    var animType:AnimationType?=null
    var animTime:Float=0f
    var width:Float=0f
    var height:Float=0f

    override fun reset() {
        animType=null
        animTime=0f
        width=0f
        height=0f
    }
}
