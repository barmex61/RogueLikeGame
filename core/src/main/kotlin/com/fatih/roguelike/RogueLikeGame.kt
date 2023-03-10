package com.fatih.roguelike

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.FitViewport
import com.fatih.box2dgame.util.WorldContactListener
import com.fatih.roguelike.screen.GameScreen
import com.fatih.roguelike.screen.LoadingScreen
import com.fatih.roguelike.screen.ScreenType
import com.fatih.roguelike.util.Constants
import com.fatih.roguelike.util.Constants.FIXED_TIME_STEP
import com.fatih.roguelike.util.Constants.setScreen
import java.util.*

class RogueLikeGame : Game(),ContactListener by WorldContactListener() {
    private lateinit var screenCache: EnumMap<ScreenType, Screen>
    private var accumulator:Float=0f

    companion object{
        lateinit var spriteBatch: SpriteBatch
        lateinit var screenViewPort: FitViewport
        val world= World(Vector2(0f,0f),true)
        lateinit var debugRenderer: Box2DDebugRenderer
        var sizeLambda:((Int,Int)->Unit)?=null
        val assetManager= AssetManager().apply {
            setLoader(TiledMap::class.java, TmxMapLoader(this.fileHandleResolver))  }
        lateinit var gameCamera: OrthographicCamera

    }

    override fun create() {
        Box2D.init()
        spriteBatch= SpriteBatch()
        gameCamera = OrthographicCamera().apply {
            setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            position.set(this.viewportWidth / 2f, this.viewportHeight / 2f, 0f)
        }
        Gdx.app.logLevel= Application.LOG_DEBUG
        debugRenderer= Box2DDebugRenderer()
        screenViewPort= FitViewport(
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat(), gameCamera)
        sizeLambda?.invoke(Gdx.graphics.width, Gdx.graphics.height)
        world.setContactListener(this)
        screenCache= EnumMap(ScreenType::class.java)
        setScreen(ScreenType.LOADING)
        setScreen= {
            setScreen(it)
        }
    }

    private fun setScreen(screenType: ScreenType) {
        val screen= screenCache[screenType]
        if (screen==null){
            try {
                Gdx.app.debug(Constants.TAG,"Creating new screen $screenType")
                val newScreen= when(screenType){
                    ScreenType.LOADING-> LoadingScreen()
                    else-> GameScreen()
                }
                screenCache[screenType]=newScreen as Screen
                setScreen(newScreen)
            }catch (e:Exception){
                throw GdxRuntimeException("Screen $screenType could not be created $e")
            }
        }else{
            Gdx.app.debug(Constants.TAG,"Switching to screen $screenType")
            setScreen(screen)
        }
    }

    override fun render() {
        super.render()
        accumulator += 0.25f.coerceAtMost(Gdx.graphics.deltaTime)
        while (accumulator >= FIXED_TIME_STEP){
            world.step(FIXED_TIME_STEP,6,2)
            accumulator -= FIXED_TIME_STEP
        }
    }

    override fun dispose() {

        super.dispose()
        world.dispose()
        debugRenderer.dispose()
        assetManager.dispose()
    }
}
