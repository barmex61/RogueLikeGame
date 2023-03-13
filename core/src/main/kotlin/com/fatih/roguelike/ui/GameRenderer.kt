package com.fatih.roguelike.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.IntMap
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.ecs.ECSEngine
import com.fatih.roguelike.ecs.components.AnimationComponent
import com.fatih.roguelike.ecs.components.Box2dComponent
import com.fatih.roguelike.ecs.components.GameObjectComponent
import com.fatih.roguelike.map.GameObject
import com.fatih.roguelike.map.Map
import com.fatih.roguelike.map.MapListener
import com.fatih.roguelike.types.AnimationType
import com.fatih.roguelike.util.Constants
import java.util.EnumMap

class GameRenderer : Disposable, MapListener {

    init {
        RogueLikeGame.mapManager.mapListener=this
    }

    private lateinit var mapAnimations: IntMap<Animation<Sprite>?>
    private var orthogonalTiledMapRenderer= OrthogonalTiledMapRenderer(null, Constants.UNIT_SCALE,
        RogueLikeGame.spriteBatch
    )
    private val animationCache=EnumMap<AnimationType,Animation<Sprite>>(EnumMap(AnimationType::class.java))
    private var box2DDebugRenderer : Box2DDebugRenderer?=Box2DDebugRenderer()
    private var gameObjectEntities:ImmutableArray<Entity> = RogueLikeGame.ecsEngine.getEntitiesFor(Family.all(GameObjectComponent::class.java,Box2dComponent::class.java,AnimationComponent::class.java).get())
    private var animatedEntities:ImmutableArray<Entity> = RogueLikeGame.ecsEngine.getEntitiesFor(Family.all(AnimationComponent::class.java,Box2dComponent::class.java).exclude(GameObjectComponent::class.java).get())
    private var tiledMapTileLayer = com.badlogic.gdx.utils.Array<TiledMapTileLayer>()

    fun render(alpha: Float) {
        RogueLikeGame.spriteBatch.begin()
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        RogueLikeGame.screenViewPort.apply(false)
        orthogonalTiledMapRenderer.setView(RogueLikeGame.gameCamera)
        if (orthogonalTiledMapRenderer.map!=null){
            for (layer in tiledMapTileLayer){
                orthogonalTiledMapRenderer.renderTileLayer(layer)
            }
        }
        for (entity in gameObjectEntities){
            renderGameObjects(entity,alpha)
        }
        for (entity in animatedEntities){
            renderEntity(entity,alpha)
        }
        RogueLikeGame.spriteBatch.end()
        box2DDebugRenderer?.render(RogueLikeGame.world,RogueLikeGame.gameCamera.combined)
    }

    private fun renderGameObjects(entity: Entity, alpha: Float) {
        val box2dComponent=ECSEngine.box2dComponentMapper.get(entity)
        val animationComponent=ECSEngine.animationComponentMapper.get(entity)
        val gameObjectComponent=ECSEngine.gameObjectMapper.get(entity)
        if (gameObjectComponent.animationIndex != -1){
            val animation= mapAnimations[gameObjectComponent.animationIndex]
            animation?.getKeyFrame(animationComponent.animTime)?.apply {
                setBounds(box2dComponent.renderPosition.x,box2dComponent.renderPosition.y,animationComponent.width,animationComponent.height)
                setOriginCenter()
                rotation=box2dComponent.body!!.angle * MathUtils.radDeg
                draw(RogueLikeGame.spriteBatch)
            }
        }
    }

    private fun renderEntity(entity: Entity, alpha: Float) {
        val box2dComponent = ECSEngine.box2dComponentMapper.get(entity)
        ECSEngine.animationComponentMapper.get(entity).apply {
            animType?.let {
                val animation: Animation<Sprite> = getAnimation(it)
                val frame :Sprite = animation.getKeyFrame(this.animTime)
                box2dComponent.renderPosition.lerp(box2dComponent.body!!.position,alpha)
                frame.setBounds(box2dComponent.renderPosition.x-this.width*0.5f,box2dComponent.renderPosition.y-this.height*0.4f,this.width,this.height)
                frame.draw(RogueLikeGame.spriteBatch)
            }
          }

    }

    private fun getAnimation(animationType: AnimationType): Animation<Sprite> {
        var spriteAnimation= animationCache[animationType]
        return if (spriteAnimation != null){
            spriteAnimation
        }
        else{
            Gdx.app.debug(Constants.TAG,"Creating new animation type $animationType")
            val atlasRegion = RogueLikeGame.assetManager.get(animationType.filePath,TextureAtlas::class.java).findRegion(animationType.key)
            val textureRegion : Array<out Array<TextureRegion>>? = atlasRegion.split(64,64)
            spriteAnimation=Animation(animationType.frameTime,getKeyFrames(textureRegion!![animationType.rowIndex]),Animation.PlayMode.LOOP)
            animationCache[animationType] = spriteAnimation
            spriteAnimation
        }
    }

    private fun getKeyFrames(textureRegions: Array<TextureRegion>): com.badlogic.gdx.utils.Array<out Sprite>{
        val keyFrames=com.badlogic.gdx.utils.Array<Sprite>()
        for (texture in textureRegions){
            val sprite=Sprite(texture).apply { setOriginCenter() }
            keyFrames.add(sprite)
        }
        return keyFrames
    }

    override fun mapChange(map: Map) {
        orthogonalTiledMapRenderer.map=map.tiledMap
        map.tiledMap.layers.getByType(TiledMapTileLayer::class.java,tiledMapTileLayer)
        mapAnimations=map.mapAnimations

    }

    override fun dispose() {
        box2DDebugRenderer=null
        orthogonalTiledMapRenderer.dispose()
    }
}
