package com.fatih.roguelike.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.ecs.components.Box2dComponent
import com.fatih.roguelike.ecs.components.PlayerComponent

class PlayerCameraSystem : IteratingSystem(Family.all(PlayerComponent::class.java,Box2dComponent::class.java).get()) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        RogueLikeGame.gameCamera.position.set(ECSEngine.box2dComponentMapper.get(entity).body?.position,0f)
        RogueLikeGame.gameCamera.update()
    }
}
