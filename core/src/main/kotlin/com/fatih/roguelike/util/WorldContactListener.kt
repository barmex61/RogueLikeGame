package com.fatih.roguelike.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.fatih.roguelike.util.Constants.BIT_GAME_OBJECT
import com.fatih.roguelike.util.Constants.BIT_PLAYER
import kotlin.experimental.and
import com.badlogic.ashley.core.Entity

class WorldContactListener:ContactListener {

    lateinit var playerCollisionListener: PlayerCollisionListener

    override fun beginContact(contact: Contact?) {
        contact?.let {
            val bodyA=it.fixtureA.body
            val bodyB=it.fixtureB.body
            val catFixA=it.fixtureA.filterData.categoryBits
            val catFixB=it.fixtureB.filterData.categoryBits
            val player :Entity= when (BIT_PLAYER) {
                catFixA and BIT_PLAYER -> {
                    bodyA.userData as Entity
                }
                catFixB and BIT_PLAYER -> {
                    bodyB.userData as Entity
                }
                else -> {
                    return
                }
            }
            val gameObject :Entity= when (BIT_GAME_OBJECT) {
                catFixA and BIT_GAME_OBJECT -> {
                    bodyA.userData as Entity
                }
                catFixB and BIT_GAME_OBJECT -> {
                    bodyB.userData as Entity
                }
                else -> {
                    return
                }
            }
            playerCollisionListener.playerCollision(player,gameObject)
        }
    }

    override fun endContact(contact: Contact?) {

    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }

    interface PlayerCollisionListener{
        fun playerCollision(player:Entity,gameObject:Entity)
    }

}
