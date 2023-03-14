package com.fatih.roguelike

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.fatih.roguelike.ecs.ECSEngine

class PreferenceManager : Json.Serializable{

    private var preferences:Preferences = Gdx.app.getPreferences("RogueLikeGame")
    private val json=Json()
    private var playerPos=Vector2()
    private val jsonReader=JsonReader()

    fun containsKey(key:String):Boolean{
        return preferences.contains(key)
    }

    fun setFloatValue(key:String,value:Float){
        preferences.putFloat(key,value)
        preferences.flush()
    }

    fun getFloatValue(key: String):Float{
        return preferences.getFloat(key,0f)
    }

    fun saveGameState(player:Entity){
        playerPos = ECSEngine.box2dComponentMapper.get(player).body!!.position
        preferences.putString("game_state",Json().toJson(this))
        preferences.flush()
    }

    fun loadGameState(player: Entity){
        val savedJsonStr = jsonReader.parse(preferences.getString("game_state"))
        val box2dComponent=ECSEngine.box2dComponentMapper.get(player)
        box2dComponent.body!!.setTransform(
            savedJsonStr.getFloat("player_x",0f),
            savedJsonStr.getFloat("player_y",0f),
            box2dComponent.body!!.angle
            )
    }

    override fun write(json: Json?) {
        json?.let {
            json.writeValue("player_x",playerPos.x)
            json.writeValue("player_y",playerPos.y)
        }
    }

    override fun read(json: Json?, jsonData: JsonValue?) {

    }
}
