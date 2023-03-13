package com.fatih.roguelike

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Colors
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.FitViewport
import com.fatih.roguelike.audio.AudioManager
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.input.InputManager
import com.fatih.roguelike.map.MapManager
import com.fatih.roguelike.util.WorldContactListener
import com.fatih.roguelike.screen.GameScreen
import com.fatih.roguelike.screen.LoadingScreen
import com.fatih.roguelike.types.ScreenType
import com.fatih.roguelike.ui.GameRenderer
import com.fatih.roguelike.util.Constants
import com.fatih.roguelike.util.Constants.FIXED_TIME_STEP
import com.fatih.roguelike.util.Constants.setScreen
import java.util.*
import kotlin.math.min

class RogueLikeGame : Game(),ContactListener by WorldContactListener(){
    private lateinit var screenCache: EnumMap<ScreenType, Screen>
    private var accumulator:Float=0f
    private var deltaTime=0f

    companion object{
        val inputManager=InputManager()
        lateinit var spriteBatch: SpriteBatch
        lateinit var screenViewPort: FitViewport
        val world= World(Vector2(0f,0f),true)
        lateinit var debugRenderer: Box2DDebugRenderer
        var sizeLambda:((Int,Int)->Unit)?=null
        val assetManager= AssetManager().apply {
            setLoader(TiledMap::class.java, TmxMapLoader(this.fileHandleResolver))  }
        lateinit var gameCamera: OrthographicCamera
        lateinit var skin:Skin
        lateinit var stage:Stage
        lateinit var i18NBundle:I18NBundle
        val audioManager=AudioManager()
        val ecsEngine=ECSEngine()
        val fixtureDef= FixtureDef()
        val bodyDef= BodyDef()
        val mapManager=MapManager()
        lateinit var gameRenderer:GameRenderer
        fun resetBodyAndFixtureDefinition(){
            bodyDef.apply {
                position.set(0f,0f)
                gravityScale=1f
                type= BodyDef.BodyType.StaticBody
                fixedRotation=false
            }
            fixtureDef.apply {
                density=0f
                isSensor=false
                restitution=0f
                friction=0.2f
                filter.categoryBits=0x0001
                filter.maskBits=-1
                fixtureDef.shape=null
            }
        }
    }

    override fun create() {
        setScreen= {
            setScreen(it)
        }
        Box2D.init()
        initializeSkin()
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
        stage= Stage(FitViewport(Gdx.graphics.width.toFloat(),Gdx.graphics.height.toFloat()), spriteBatch)
        Gdx.input.inputProcessor=InputMultiplexer(inputManager, stage)
        sizeLambda?.invoke(Gdx.graphics.width/3, (Gdx.graphics.height/1.3).toInt())
        world.setContactListener(this)
        screenCache= EnumMap(ScreenType::class.java)
        setScreen(ScreenType.LOADING)

        gameRenderer=GameRenderer()
    }

    private fun initializeSkin(){
        Colors.put("Red", Color.RED)
        Colors.put("Blue", Color.BLUE)
        val objectMap=ObjectMap<String,Any>()
        val fontGenerator=FreeTypeFontGenerator(Gdx.files.internal("hud/font.ttf"))
        FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            minFilter=Texture.TextureFilter.Linear
            magFilter=Texture.TextureFilter.Linear
            characters=FreeTypeFontGenerator.DEFAULT_CHARS + "ğüşıöçĞÜŞİÖÇ"
            val array= arrayOf(16,20,26,32)
            for (number in array){
                size=number
                objectMap.put("font_$size",fontGenerator.generateFont(this).apply { data.markupEnabled=true })
            }
        }
        fontGenerator.dispose()
        val skinParameter=SkinLoader.SkinParameter("hud/hud.atlas",objectMap)
        assetManager.load("hud/hud.json", Skin::class.java,skinParameter)
        assetManager.load("hud/strings",I18NBundle::class.java)
        assetManager.finishLoading()
        skin = assetManager.get("hud/hud.json",Skin::class.java)
        i18NBundle= assetManager.get("hud/strings",I18NBundle::class.java)

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
        deltaTime= min(0.25f,Gdx.graphics.deltaTime)
        ecsEngine.update(deltaTime)
        accumulator +=deltaTime
        while (accumulator >= FIXED_TIME_STEP){
            world.step(FIXED_TIME_STEP,6,2)
            accumulator -= FIXED_TIME_STEP
        }
        gameRenderer.render(accumulator/ FIXED_TIME_STEP)
        stage.apply {
            viewport.apply()
            act(deltaTime)
            draw()
        }

    }

    override fun dispose() {

        super.dispose()
        world.dispose()
        debugRenderer.dispose()
        assetManager.dispose()
        stage.dispose()
    }

}
