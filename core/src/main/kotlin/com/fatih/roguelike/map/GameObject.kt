package com.fatih.roguelike.map

import com.badlogic.gdx.math.Vector2
import com.fatih.roguelike.types.GameObjectType

class GameObject(val type: GameObjectType, val position:Vector2, val width:Float, val height:Float, val rotDegree:Float, val animationIndex:Int) {

}
