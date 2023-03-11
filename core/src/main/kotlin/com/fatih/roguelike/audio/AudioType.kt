package com.fatih.roguelike.audio

enum class AudioType(val path:String,val isMusic:Boolean,val volume:Float) {
    INTRO("audio/intro.mp3",true,0.3f),
    SELECT("audio/select.wav",false,0.5f)
}
