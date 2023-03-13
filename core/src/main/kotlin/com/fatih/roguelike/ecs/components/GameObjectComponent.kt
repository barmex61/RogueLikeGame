package com.fatih.roguelike.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import com.fatih.roguelike.types.GameObjectType

class GameObjectComponent: Component,Pool.Poolable {

    var animationIndex = -1
    var type : GameObjectType ?=null

    override fun reset() {
        animationIndex=-1
        type=null
    }
}
