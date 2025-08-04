package dev.eposs.elementsutils.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public interface ICustomScreenWidget {

    /**
     * Retrieves the associated {@link MovableRenderWidget} of the implementing class.
     * This widget is a used in {@link RenderConfigScreen}.
     *
     * @return the {@link MovableRenderWidget} instance associated with this element
     */
    MovableRenderWidget getMovableRenderWidget();

    /**
     * Renders the custom screen widget in {@link ScreenRendering} using the provided drawing context and client instance.
     *
     * @param context the drawing context used for rendering
     * @param client  the Minecraft client instance
     */
    void render(DrawContext context, MinecraftClient client);
    
}
