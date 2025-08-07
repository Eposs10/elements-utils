package dev.eposs.elementsutils.feature.time;

import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.rendering.Position;
import dev.eposs.elementsutils.rendering.ScreenPositioning;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class TimeDisplay {

    public static void render(DrawContext context, MinecraftClient client) {
        if (!ModConfig.getConfig().showTimeDisplay) return;

        assert client.world != null;
        long timeOfDay = client.world.getTimeOfDay() % 24000L;

        String texturePath = getClockTexture(timeOfDay);

        var texture = Identifier.of(texturePath);

        Position position = ScreenPositioning.getTimePosition(client.getWindow());
        final int size = 16;

        context.drawTexture(
                RenderLayer::getGuiTextured,
                texture,
                position.getX(), position.getY(),
                0.0f, 0.0f,
                size, size, size, size
        );
    }

    private static String getClockTexture(long time) {
        // 00 : noon ( 6000 )
        // 16 : night start ( 13000 )
        // 32 : midnight ( 18000 )
        // 48 : day start ( 1000 )

        // Shift the timeline so that noon (6000) becomes position 0
        long shifted = (time - 6000 + 24000) % 24000;

        // Map the shifted value to 0-63 range
        int id = (int) (shifted * 64 / 24000);

        // Add zero padding
        return String.format("textures/item/clock_%02d.png", id);
    }
}
