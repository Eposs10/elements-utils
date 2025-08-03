package dev.eposs.elementsutils.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class MovableRenderWidget extends ClickableWidget {
    private double offsetX, offsetY;
    private final int color;

    public MovableRenderWidget(int x, int y, int width, int height, Text name, int color) {
        super(x, y, width, height, name);
        this.color = color;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(getX(), getY(), getX() + width, getY() + height, color);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textX = getX() + width / 2 - textRenderer.getWidth(this.getMessage()) / 2;
        int textY = getY() + height / 2 - textRenderer.fontHeight / 2;
        
        context.drawText(textRenderer, this.getMessage(),
                textX, textY, Colors.WHITE, false);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        // Store offset inside the widget
        offsetX = mouseX - getX();
        offsetY = mouseY - getY();
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setX((int) (mouseX - offsetX));
        this.setY((int) (mouseY - offsetY));
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        // dragging = false;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
