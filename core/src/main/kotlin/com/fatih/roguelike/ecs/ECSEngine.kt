package com.fatih.roguelike.ecs

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.Vector2

class ECSEngine:PooledEngine() {

    fun createPlayer(playerSpawnLocation:Vector2){
        val entity = this.createEntity()
    }
}
