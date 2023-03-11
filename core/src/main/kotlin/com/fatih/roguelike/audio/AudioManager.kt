package com.fatih.roguelike.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.fatih.roguelike.RogueLikeGame

class AudioManager {

    private var currentMusicType:AudioType?=null
    private var currentMusic:Music?=null

    fun playAudio(audioType: AudioType){
        if (audioType.isMusic){
            if (currentMusicType == audioType) return
            if (currentMusic != null) currentMusic!!.stop()
            currentMusicType=audioType
            currentMusic=RogueLikeGame.assetManager.get(audioType.path,Music::class.java)
            currentMusic?.apply {
                isLooping=true
                volume=audioType.volume
                play()
            }
        }else{
            RogueLikeGame.assetManager.get(audioType.path,Sound::class.java).play(audioType.volume)
        }
    }

    fun stopAudio(){
        if (currentMusic != null){
            currentMusic!!.stop()
            currentMusic = null
            currentMusicType = null
        }
    }
}
