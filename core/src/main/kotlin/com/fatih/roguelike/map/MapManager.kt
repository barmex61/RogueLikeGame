package com.fatih.roguelike.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.utils.Array
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.types.MapType
import com.fatih.roguelike.util.Constants
import com.fatih.roguelike.util.Constants.BIT_GROUND
import java.util.EnumMap
import javax.swing.text.html.parser.Entity

class MapManager {

    private val gameObjectsToRemove= ArrayList<com.badlogic.ashley.core.Entity>()
    private val mapCache = EnumMap<MapType,Map>(MapType::class.java)
    lateinit var mapListener:MapListener
    private val bodies=Array<Body>()
    private var currentMapType : MapType?=null
    private var currentMap:Map?=null

    fun setMap(mapType: MapType){
        if (currentMapType==mapType){
            return
        }
        currentMap?.let {
            RogueLikeGame.world.getBodies(bodies)
            destroyCollisionAreas()
            destroyGameObjects()
        }
        Gdx.app.debug(Constants.TAG,"Changing to map $mapType")
        currentMap= mapCache[mapType]
        if (currentMap==null) {
            Gdx.app.debug(Constants.TAG,"Creating new map of tye $mapType")
            val tiledMap=RogueLikeGame.assetManager.get(mapType.filePath,TiledMap::class.java)
            currentMap=Map(tiledMap)
            mapCache[mapType] = currentMap
        }
        spawnCollisionAreas()
        spawnGameObjects()
        mapListener.mapChange(currentMap!!)
    }

    private fun spawnGameObjects(){
        for (obj in currentMap!!.gameObjects){
            RogueLikeGame.ecsEngine.createGameObject(obj)
        }
    }

    private fun destroyGameObjects(){
        for (entity in RogueLikeGame.ecsEngine.entities){
            if (ECSEngine.gameObjectMapper.get(entity) != null){
                gameObjectsToRemove.add(entity)
            }
        }
        for (entity in gameObjectsToRemove){
            RogueLikeGame.ecsEngine.removeEntity(entity)
        }
        gameObjectsToRemove.clear()
    }

    private fun destroyCollisionAreas(){
        for (body in bodies){
            if ("GROUND" == body.userData){
                RogueLikeGame.world.destroyBody(body)
            }
        }
    }

    private fun spawnCollisionAreas(){
        RogueLikeGame.resetBodyAndFixtureDefinition()
        for (collisionArea in currentMap!!.collisionArea){
            RogueLikeGame.bodyDef.apply {
                position.set(collisionArea.positionX,collisionArea.positionY)
                fixedRotation=true
            }
            val body =RogueLikeGame.world.createBody(RogueLikeGame.bodyDef)
            body.userData="GROUND"
            val chainShape=ChainShape().apply {
                createChain(collisionArea.vertices)
            }
            RogueLikeGame.fixtureDef.apply {
                filter.categoryBits=BIT_GROUND
                filter.maskBits=-1
                shape=chainShape
                body.createFixture(this)
                chainShape.dispose()
            }
        }
    }

    fun getCurrentMap(): Map? {return currentMap}
}
