package dev.eposs.elementsutils.feature.moonphase;

import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.rendering.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import static dev.eposs.elementsutils.rendering.ScreenPositioning.*;

public class MoonPhaseDisplay extends AbstractFeatureWidget {
    @Override
    public Position getDefaultPosition() {
        return new Position(MinecraftClient.getInstance().getWindow().getScaledWidth() - IMAGE_SIZE - IMAGE_SIZE - GAP, PET_HEIGHT + GAP);
    }

    @Override
    public MovableRenderWidget getMovableRenderWidget(Feature feature) {
        return new MovableRenderWidget(getPosition().getX(), getPosition().getY(), IMAGE_SIZE, IMAGE_SIZE, "Moon Phase Display", feature);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (!ModConfig.getConfig().showMoonPhaseDisplay) return;

        assert client.world != null;
        MoonPhase moonPhase = MoonPhase.fromId(client.world.getMoonPhase());
        if (moonPhase == null) return;

        var texture = Identifier.of(ElementsUtils.MOD_ID, moonPhase.getTexturePath());
        
        context.drawTexture(
                RenderLayer::getGuiTextured,
                texture,
                getPosition().getX(), getPosition().getY(),
                0.0f, 0.0f,
                IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE
        );
    }
}
