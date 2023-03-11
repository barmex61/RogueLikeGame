package com.fatih.roguelike.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool

class Box2dComponent : Component , Pool.Poolable {

    var body : Body ?= null
    var width : Float = 0f
    var height : Float = 0f

    override fun reset() {
        body?.let {
            it.world.destroyBody(it)
            body = null
        }
        width = 0f
        height = 0f
    }
}
