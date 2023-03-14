package com.fatih.roguelike.ecs.components

import box2dLight.Light
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool

class Box2dComponent : Component , Pool.Poolable {

    var body : Body ?= null
    var width : Float = 0f
    var light : Light ?=null
    var lightDistance : Float=0f
    var lightFluctuationDistance:Float=0f
    var lightFluctuationTime:Float=0f
    var lightFluctuationSpeed : Float=0f
    var height : Float = 0f
    var renderPosition : Vector2 = Vector2(0f,0f)

    override fun reset() {
        if (light!=null){
            light!!.remove(true)
            light=null
        }
        lightDistance =0f
        lightFluctuationDistance=0f
        lightFluctuationTime=0f
        lightFluctuationSpeed=0f
        body?.let {
            it.world.destroyBody(it)
            body = null
        }
        width = 0f
        height = 0f
        renderPosition.set(0f,0f)
    }
}
