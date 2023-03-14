package com.fatih.roguelike.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.IntMap
import com.fatih.roguelike.types.GameObjectType
import com.fatih.roguelike.util.Constants
import com.fatih.roguelike.util.Constants.TAG
import com.fatih.roguelike.util.Constants.UNIT_SCALE
import javax.print.attribute.IntegerSyntax

class Map ( val tiledMap: TiledMap) {

    val gameObjects = ArrayList<GameObject>()
    val startLocation=Vector2()
    val collisionArea:ArrayList<CollisionArea> = ArrayList()
    val mapAnimations = IntMap<Animation<Sprite>>()


    init {
        parseCollisionLayer()
        parsePlayerStartLocation()
        parseGameObjects()
    }



    private fun parsePlayerStartLocation(){
        val gameObject = tiledMap.layers.get("gameObjects")
        val gameObjects=gameObject.objects
        for (obj in gameObjects){
                when(obj){
                    is RectangleMapObject->{
                        val rectangle=obj.rectangle
                        startLocation.set(rectangle.x * UNIT_SCALE ,rectangle.y * UNIT_SCALE)
                        Gdx.app.debug(TAG, "${startLocation.x} ${startLocation.y}")
                    }
                    else->{
                        Gdx.app.debug(TAG,"There is no such an object")
                }
            }
        }
    }

    private fun parseGameObjects(){
        val gameObjectsLayer=tiledMap.layers.get("gameObjects")
        gameObjectsLayer?.let {
            val objects=gameObjectsLayer.objects
            for (obj in objects){
                if (obj !is TiledMapTileMapObject){
                    Gdx.app.debug(Constants.TAG,"Gameobject of type $obj is not supported")
                    continue
                }
                val tiledMapTileObject : TiledMapTileMapObject=obj
                val mapProperties:MapProperties = tiledMapTileObject.properties
                val tiledProperties = tiledMapTileObject.tile.properties

                val gameObjectType : GameObjectType= if (mapProperties.containsKey("type")){
                    GameObjectType.valueOf(mapProperties.get("type",String::class.java))
                }else if(tiledProperties.containsKey("type")){
                    GameObjectType.valueOf(tiledProperties.get("type",String::class.java) )
                }else{
                    Gdx.app.debug(TAG,"There is no gameobject defines for tile ${mapProperties.get("id",Integer::class.java)}")
                    continue
                }
                val animationIndex=obj.tile.id
                if (!createAnimation(animationIndex,tiledMapTileObject.tile)){
                    Gdx.app.debug(Constants.TAG,"Couldnt create animation for tile ${mapProperties.get("id",Integer::class.java)}")
                    continue
                }
                val width=mapProperties.get("width",Float::class.java) * UNIT_SCALE
                Gdx.app.debug(TAG,"width $width")
                val height=mapProperties.get("height",Float::class.java) * UNIT_SCALE
                gameObjects.add(GameObject(gameObjectType,
                    Vector2(tiledMapTileObject.x* UNIT_SCALE,tiledMapTileObject.y* UNIT_SCALE),
                    width,height,tiledMapTileObject.rotation,animationIndex
                ))
            }

        }?:Gdx.app.debug(Constants.TAG,"There is no gameObjects layer !")
    }

    private fun createAnimation(animIndex:Int,tile:TiledMapTile):Boolean{
        try {
            var animation : Animation<Sprite>? = mapAnimations.get(animIndex)
            if (animation == null) {
                Gdx.app.debug(Constants.TAG,"Creating new animations for ${tile.id}")
                when (tile) {
                    is AnimatedTiledMapTile -> {
                        val aniTile : AnimatedTiledMapTile = tile
                        val keyFrames =com.badlogic.gdx.utils.Array<Sprite>()
                        for (i in 0 until aniTile.frameTiles.size){
                            keyFrames.add(Sprite(aniTile.frameTiles[i].textureRegion))
                        }
                        animation = Animation<Sprite>(aniTile.animationIntervals.first() * 0.001f,keyFrames)
                        animation.playMode=Animation.PlayMode.LOOP
                        mapAnimations.put(animIndex,animation)
                    }
                    is StaticTiledMapTile -> {
                        animation=Animation<Sprite>(0f, Sprite(tile.textureRegion))
                        mapAnimations.put(animIndex,animation)
                    }
                    else -> {
                        Gdx.app.debug(Constants.TAG,"Tile of type $tile is not supported for map animations")
                        return false
                    }
                }
            }
        }catch (e:java.lang.Exception){
            Gdx.app.debug(Constants.TAG,e.message)
        }

        return true
    }

    private fun parseCollisionLayer(){
        val collisionLayer= tiledMap.layers.get("collision")
        val mapObject=collisionLayer.objects
        for (obj in mapObject){
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

        }
}
