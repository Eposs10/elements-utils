package dev.eposs.elementsutils.rendering;

import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.feature.bosstimer.BossTimerDisplay;
import dev.eposs.elementsutils.feature.excaliburtime.ExcaliburTimeDisplay;
import dev.eposs.elementsutils.feature.moonphase.MoonPhaseDisplay;
import dev.eposs.elementsutils.feature.pet.PetDisplay;
import dev.eposs.elementsutils.feature.potion.PotionDisplay;
import dev.eposs.elementsutils.feature.time.TimeDisplay;
import dev.eposs.elementsutils.feature.xpmeter.XpMeter;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ScreenRendering {
    
    public static Map<Feature, AbstractFeatureWidget> widgetMap = new HashMap<>();
    
    public static void init() {
        widgetMap = Map.of(
                Feature.BOSS_TIMER, new BossTimerDisplay(),
                Feature.EXCALIBUR_TIME, new ExcaliburTimeDisplay()
        );
    }

    public static void register(@NotNull LayeredDrawerWrapper layeredDrawer) {
        layeredDrawer.attachLayerAfter(
                IdentifiedLayer.MISC_OVERLAYS,
                Identifier.of(ElementsUtils.MOD_ID, "layer_after_misc_overlays"),
                ScreenRendering::render
        );
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        widgetMap.values().forEach(widget -> widget.render(context, client));

        MoonPhaseDisplay.render(context, client);
        TimeDisplay.render(context, client);
        // new BossTimerDisplay().render(context, client);
        // ExcaliburTimeDisplay.render(context, client, 6);
        PetDisplay.render(context, client);
        PotionDisplay.render(context, client);
        XpMeter.render(context, client);
    }
}
