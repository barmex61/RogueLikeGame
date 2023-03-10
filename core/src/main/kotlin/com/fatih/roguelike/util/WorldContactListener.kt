package com.fatih.box2dgame.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.fatih.roguelike.util.Constants.TAG

class WorldContactListener:ContactListener {

    override fun beginContact(contact: Contact?) {
        val fixtureA=contact?.fixtureA
        val fixtureB=contact?.fixtureB
        Gdx.app.debug(TAG,"fixture a" + fixtureA?.body?.userData.toString()+fixtureA?.isSensor)
        Gdx.app.debug(TAG,"fixture b" + fixtureB?.body?.userData.toString()+fixtureB?.isSensor)
    }

    override fun endContact(contact: Contact?) {
        val fixtureA=contact?.fixtureA
        val fixtureB=contact?.fixtureB
        Gdx.app.debug(TAG,"fixture a" + fixtureA?.body?.userData.toString()+fixtureA?.isSensor)
        Gdx.app.debug(TAG,"fixture b" + fixtureB?.body?.userData.toString()+fixtureB?.isSensor)
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }


}
