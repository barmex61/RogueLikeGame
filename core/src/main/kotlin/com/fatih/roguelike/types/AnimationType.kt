package com.fatih.roguelike.types

enum class AnimationType(val filePath:String,val key:String,val frameTime:Float,val rowIndex:Int) {
    HERO_MOVE_UP("characters_and_effects/character_and_effect.atlas","hero",0.05f,0),
    HERO_MOVE_DOWN("characters_and_effects/character_and_effect.atlas","hero",0.05f,2),
    HERO_MOVE_RIGHT("characters_and_effects/character_and_effect.atlas","hero",0.05f,3),
    HERO_MOVE_LEFT("characters_and_effects/character_and_effect.atlas","hero",0.05f,1)
}
