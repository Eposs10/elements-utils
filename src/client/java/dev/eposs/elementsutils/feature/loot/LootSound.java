package dev.eposs.elementsutils.feature.loot;

import dev.eposs.elementsutils.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class LootSound {
    public static void onGameMessage(Text text) {
        if (!ModConfig.getConfig().playLootSound) return;
        
        String msg = text.getString();
        if (msg.startsWith("Loot:")) playCoolSound();
        if (msg.startsWith("SB Loot:")) playCoolSound();
        if (msg.startsWith("PB Loot:")) playCoolSound();
        if (msg.startsWith("Pet Loot:")) playCoolSound();
    }

    private static void playCoolSound() {
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
        float pitch = 1.0F;
        float pitchIncrease = 0.5F;
        float volume = 1.0F;
        int delay = 2;

        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), pitch, volume));
        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), pitch + pitchIncrease, volume), delay);
        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), pitch + pitchIncrease * 2, volume), delay * 2);
    }
}
