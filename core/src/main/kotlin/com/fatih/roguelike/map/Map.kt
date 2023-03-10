package com.fatih.roguelike.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.fatih.roguelike.util.Constants.TAG
import com.fatih.roguelike.util.Constants.UNIT_SCALE

class Map (private val tiledMap: TiledMap,val collisionArea:ArrayList<CollisionArea>) {

    private lateinit var gameObjects:MapObjects

    init {
        parseCollisionLayer()
        parsePlayerStartLocation()
    }

    companion object{
        val startLocation=Vector2()
    }

    private fun parsePlayerStartLocation(){
        val gameObject = tiledMap.layers.get("gameObjects")
        gameObject?.let {
            gameObjects=it.objects
            for (obj in gameObjects){
                when(obj){
                    is RectangleMapObject->{
                        val rectangle=obj.rectangle
                        startLocation.set(rectangle.x * UNIT_SCALE ,rectangle.y * UNIT_SCALE)
                    }
                }
            }
        }?:Gdx.app.debug(TAG,"There is no such an object").also {
            return
        }
    }

    private fun parseCollisionLayer(){
        val collisionLayer= tiledMap.layers.get("collision")
        collisionLayer?.let {

        }?:Gdx.app.debug(TAG,"There is no collision layer").also {
            return
        }
        val mapObject=collisionLayer.objects
        mapObject?.let {
            for (obj in it){
                when(obj){
                    is RectangleMapObject->{
                        val rectangle=obj.rectangle
                        val rectVertices= floatArrayOf(
                            0f,0f,0f,rectangle.height,rectangle.width,rectangle.height,rectangle.width,0f,0f,0f
                        )
                        collisionArea.add(CollisionArea(rectangle.x,rectangle.y,rectVertices))
                    }
                    is PolylineMapObject->{
                        val polyLine=obj.polyline
                        collisionArea.add(CollisionArea(polyLine.x,polyLine.y,polyLine.vertices))
                    }
                    else->{Gdx.app.debug(TAG,"MapObject of type $obj is not supported for collision layer")}
                }
            }

        }?:Gdx.app.debug(TAG,"There is no collision mapObject defined").also {
           return
        }
    }
}
