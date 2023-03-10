package com.fatih.roguelike.map

import com.fatih.roguelike.util.Constants.UNIT_SCALE


class CollisionArea(var positionX:Float, var positionY:Float, val vertices:FloatArray) {

    init {
        positionX *= UNIT_SCALE
        positionY *= UNIT_SCALE
        for (i in vertices.indices step 2){
            vertices[i] *= UNIT_SCALE
            vertices[i+1] *= UNIT_SCALE
        }
    }
}
