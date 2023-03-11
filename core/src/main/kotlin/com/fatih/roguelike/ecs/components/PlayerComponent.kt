package com.fatih.roguelike.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool

class PlayerComponent : Component , Pool.Poolable {

    var hasAxe = false
    var speed = Vector2()

    override fun reset() {
        hasAxe = false
        speed.set(0f,0f)
    }
}
