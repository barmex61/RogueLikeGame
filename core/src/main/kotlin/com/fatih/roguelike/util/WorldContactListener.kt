package com.fatih.roguelike.util

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold

class WorldContactListener:ContactListener {

    override fun beginContact(contact: Contact?) {
        val fixtureA=contact?.fixtureA
        val fixtureB=contact?.fixtureB

    }

    override fun endContact(contact: Contact?) {
        val fixtureA=contact?.fixtureA
        val fixtureB=contact?.fixtureB
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }


}
