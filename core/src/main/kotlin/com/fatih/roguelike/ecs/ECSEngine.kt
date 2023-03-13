package com.fatih.roguelike.ecs

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.fatih.roguelike.RogueLikeGame
import com.fatih.roguelike.RogueLikeGame.Companion.bodyDef
import com.fatih.roguelike.RogueLikeGame.Companion.fixtureDef
import com.fatih.roguelike.RogueLikeGame.Companion.resetBodyAndFixtureDefinition
import com.fatih.roguelike.RogueLikeGame.Companion.world
import com.fatih.roguelike.ecs.components.AnimationComponent
import com.fatih.roguelike.ecs.components.Box2dComponent
import com.fatih.roguelike.ecs.components.GameObjectComponent
import com.fatih.roguelike.ecs.components.PlayerComponent
import com.fatih.roguelike.ecs.system.AnimationSystem
import com.fatih.roguelike.ecs.system.PlayerAnimationSystem
import com.fatih.roguelike.ecs.system.PlayerCameraSystem
import com.fatih.roguelike.ecs.system.PlayerMovementSystem
import com.fatih.roguelike.map.GameObject
import com.fatih.roguelike.types.AnimationType
import com.fatih.roguelike.util.Constants.BIT_GAME_OBJECT
import com.fatih.roguelike.util.Constants.BIT_GROUND
import com.fatih.roguelike.util.Constants.BIT_PLAYER
import com.fatih.roguelike.util.Constants.UNIT_SCALE

class ECSEngine:PooledEngine() {

    private var bodyDef : BodyDef = BodyDef()
    private var fixtureDef : FixtureDef = FixtureDef()
    private var localPosition=Vector2()
    private var posBeforeRotation=Vector2()
    private var posAfterRotation = Vector2()

    companion object{
        val gameObjectMapper: ComponentMapper<GameObjectComponent> = ComponentMapper.getFor(GameObjectComponent::class.java)
        val playerComponentMapper: ComponentMapper<PlayerComponent> =ComponentMapper.getFor(PlayerComponent::class.java)
        val box2dComponentMapper: ComponentMapper<Box2dComponent> =ComponentMapper.getFor(Box2dComponent::class.java)
        val animationComponentMapper : ComponentMapper<AnimationComponent> = ComponentMapper.getFor(AnimationComponent::class.java)
    }

    init {
        addSystem(PlayerMovementSystem())
        addSystem(PlayerCameraSystem())
        addSystem(AnimationSystem())
        addSystem(PlayerAnimationSystem())
    }

    fun createPlayer(playerSpawnLocation:Vector2, width:Float, height:Float){
        val playerComponent=createComponent(PlayerComponent::class.java).apply {
            speed.set(60f,60f)
        }
        val player= this.createEntity().apply {
            add(playerComponent)
        }
        resetBodyAndFixtureDefinition()
        bodyDef.apply {
            position.set(playerSpawnLocation.x,playerSpawnLocation.y + height * 0.5f )
            fixedRotation=true
            type=BodyDef.BodyType.DynamicBody
        }
        val shape=PolygonShape().apply {
            setAsBox(width*0.3f,height*0.3f)
        }
        fixtureDef.apply {
            filter.categoryBits=BIT_PLAYER
            filter.maskBits= BIT_GROUND
            this.shape=shape
        }
        val box2dComponent = createComponent(Box2dComponent::class.java).apply {
            body = RogueLikeGame.world.createBody(bodyDef)
            body?.userData = "PLAYER"
            this.width=width
            this.height=height
            renderPosition.set(body!!.position)
            body?.createFixture(fixtureDef)
            shape.dispose()
        }
        player.add(box2dComponent)
        val animationComponent=createComponent(AnimationComponent::class.java).apply {
            animType=AnimationType.HERO_MOVE_DOWN
            this.width=64* UNIT_SCALE * 0.65f
            this.height=64* UNIT_SCALE * 0.65f
        }
        player.add(animationComponent)
        addEntity(player)
    }

    fun createGameObject(gameObject: GameObject) {
        val gameObjEntity=this.createEntity()
        createComponent(GameObjectComponent::class.java).apply {
            animationIndex=gameObject.animationIndex
            type=gameObject.type
            gameObjEntity.add(this)
        }
        val animationComponent = createComponent(AnimationComponent::class.java).apply {
            animType=null
            width=gameObject.width
            height=gameObject.height
            gameObjEntity.add(this)
        }
        resetBodyAndFixtureDefinition()
        val halfWidth=gameObject.width * 0.5f
        val halfHeight=gameObject.height * 0.5f
        val angleRad=-gameObject.rotDegree * MathUtils.degreesToRadians
        val box2dComponent=createComponent(Box2dComponent::class.java)
        RogueLikeGame.bodyDef.type=BodyDef.BodyType.StaticBody
        RogueLikeGame.bodyDef.position.set(gameObject.position.x + halfWidth,gameObject.position.y+halfHeight)
        box2dComponent.apply {
            body= world.createBody(RogueLikeGame.bodyDef)
            body!!.userData="GAMEOBJECT"
            width=gameObject.width
            height=gameObject.height
        }
        localPosition.set(-halfWidth,-halfHeight)
        posBeforeRotation.set(box2dComponent.body!!.getWorldPoint(localPosition))
        box2dComponent.body!!.setTransform(box2dComponent.body!!.position,angleRad)
        posAfterRotation.set(box2dComponent.body!!.getWorldPoint(localPosition))
        box2dComponent.body!!.setTransform(box2dComponent.body!!.position.add(posBeforeRotation).sub(posAfterRotation),angleRad)
        box2dComponent.renderPosition.set(box2dComponent.body!!.position.x-animationComponent.width * 0.5f,box2dComponent.body!!.position.y-box2dComponent.height*0.5f)
        val shape=PolygonShape().apply {
            setAsBox(halfWidth,halfHeight)
        }
        RogueLikeGame.fixtureDef.apply {
            filter.categoryBits=BIT_GAME_OBJECT
            filter.maskBits= BIT_PLAYER
            this.shape=shape
            box2dComponent.body!!.createFixture(this)
            shape.dispose()
        }
        gameObjEntity.add(box2dComponent)
        this.addEntity(gameObjEntity)



    }
}
