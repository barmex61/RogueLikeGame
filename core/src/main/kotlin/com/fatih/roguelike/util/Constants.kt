package com.fatih.roguelike.util

import com.badlogic.gdx.Gdx
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.screen.ScreenType

object Constants {

    val TAG= RogueLikeGame::class.simpleName
    const val BIT_CIRCLE:Short=(1 shl 0).toShort()
    const val BIT_BOX=(1 shl 1).toShort()
    const val BIT_GROUND=(1 shl 2).toShort()
    const val BIT_PLAYER=(1 shl 3).toShort()
    const val FIXED_TIME_STEP=1/60f
    var UNIT_SCALE=Gdx.graphics.width.toFloat()/1000f
    var setScreen:((ScreenType)->Unit)?=null
}
