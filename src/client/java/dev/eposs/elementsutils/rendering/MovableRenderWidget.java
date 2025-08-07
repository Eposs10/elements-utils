package dev.eposs.elementsutils.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;

public class MovableRenderWidget extends ClickableWidget {
    public final Feature modFeature;
    private boolean renderText = true;
    private double offsetX, offsetY;

    public MovableRenderWidget(int x, int y, int width, int height, String name, Feature modFeature) {
        super(x, y, width, height, Text.literal(name).formatted(Formatting.BLACK));
        textOrTooltip(name);
        this.modFeature = modFeature;
    }

    private void textOrTooltip(String name) {
        int margin = 4;
        
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(this.getMessage());
        if (textWidth + margin > width || textRenderer.fontHeight + margin > height) {
            renderText = false;
            setTooltip(Tooltip.of(Text.literal(name).formatted(Formatting.WHITE)));
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(getX(), getY(), getX() + width, getY() + height, 0xAF00FFFF); // Aqua, semi transparent

        context.drawVerticalLine(getX(), getY(), getY() + height, Colors.BLACK);
        context.drawVerticalLine(getX() + width, getY(), getY() + height, Colors.BLACK);
        context.drawHorizontalLine(getX(), getX() + width, getY(), Colors.BLACK);
        context.drawHorizontalLine(getX(), getX() + width, getY() + height, Colors.BLACK);

        if (renderText) {
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            int textX = getX() + width / 2 - textRenderer.getWidth(this.getMessage()) / 2;
            int textY = getY() + height / 2 - textRenderer.fontHeight / 2;

            context.drawText(textRenderer, this.getMessage(), textX, textY, Colors.WHITE, false);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        // Store offset inside the widget
        offsetX = mouseX - getX();
        offsetY = mouseY - getY();
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        int newX = (int) (mouseX - offsetX);
        if (newX < 0) newX = 0;
        int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        if (newX + width > screenWidth) newX = screenWidth - width;
        this.setX(newX);

        int newY = (int) (mouseY - offsetY);
        if (newY < 0) newY = 0;
        int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
        if (newY + height > screenHeight) newY = screenHeight - height;
        this.setY(newY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        // dragging = false;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
