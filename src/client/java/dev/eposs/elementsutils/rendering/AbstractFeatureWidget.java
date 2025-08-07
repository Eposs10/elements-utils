package dev.eposs.elementsutils.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFeatureWidget {
    private Position position = null;

    public AbstractFeatureWidget() {
        this.position = getDefaultPosition();
    }

    /**
     * Retrieves the default {@link Position} for the implementing instance.
     * This method is intended to provide the initial or default coordinates for an object.
     *
     * @return the default {@link Position}, representing the initial coordinates
     */
    public abstract Position getDefaultPosition();

    /**
     * Retrieves the current {@link Position} of the object.
     * If the position is not set, it returns the default {@link Position}.
     *
     * @return the current {@link Position} of the object, or the default {@link Position} if the current position is null
     */
    public Position getPosition() {
        if (this.position == null) return getDefaultPosition();
        return this.position;
    }

    /**
     * Sets the position for this widget using the provided {@link Position} object. Given Position is usually from {@link RenderConfigScreen}.
     *
     * @param position the {@link Position} object representing the x and y coordinates
     *                 to set the location of this widget
     */
    public void setPosition(@NotNull Position position) {
        this.position = position;
    }

    /**
     * Resets the position of the current object to its default position.
     * This method uses the {@link #getDefaultPosition()} implementation to determine
     * and assign the default {@link Position}.
     */
    public void resetPosition() {
        this.position = getDefaultPosition();
    }

    /**
     * Retrieves the associated {@link MovableRenderWidget} of the implementing class.
     * This widget is a used in {@link RenderConfigScreen}.
     *
     * @return the {@link MovableRenderWidget} instance associated with this element
     */
    public abstract MovableRenderWidget getMovableRenderWidget(Feature feature);

    /**
     * Renders the custom screen widget in {@link ScreenRendering} using the provided drawing context and client instance.
     *
     * @param context the drawing context used for rendering
     * @param client  the Minecraft client instance
     */
    public abstract void render(DrawContext context, MinecraftClient client);
}
