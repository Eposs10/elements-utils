package dev.eposs.elementsutils.feature.moonphase;

import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.rendering.Position;
import dev.eposs.elementsutils.rendering.ScreenPositioning;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class MoonPhaseDisplay {

    public static void render(DrawContext context, MinecraftClient client) {
        if (!ModConfig.getConfig().showMoonPhaseDisplay) return;

        assert client.world != null;
        MoonPhase moonPhase = MoonPhase.fromId(client.world.getMoonPhase());
        if (moonPhase == null) return;

        var texture = Identifier.of(ElementsUtils.MOD_ID, moonPhase.getTexturePath());

        Position position = ScreenPositioning.getMoonPhasePosition(client.getWindow());
        final int size = 16;

        context.drawTexture(
                RenderLayer::getGuiTextured,
                texture,
                position.getX(), position.getY(),
                0.0f, 0.0f,
                size, size, size, size
        );
    }
}
