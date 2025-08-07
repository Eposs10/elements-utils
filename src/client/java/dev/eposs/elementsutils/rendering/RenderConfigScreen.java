package dev.eposs.elementsutils.rendering;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class RenderConfigScreen extends Screen {
    public RenderConfigScreen() {
        super(Text.literal("Render Config"));
    }

    @Override
    protected void init() {
        super.init();
        ScreenRendering.widgetMap.forEach((feature, customScreenWidget) -> addDrawableChild(customScreenWidget.getMovableRenderWidget(feature)));

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .dimensions(width / 2 - 85, height - 30, 80, 20)
                .build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Reset"), button -> {
                    ScreenRendering.widgetMap.forEach((feature, customScreenWidget) -> customScreenWidget.resetPosition());
                    super.close();
                })
                .dimensions(width / 2 + 5, height - 30, 80, 20)
                .build());
    }

    @Override
    public void close() {
        children().stream().filter(widget -> widget instanceof MovableRenderWidget)
                .map(widget -> (MovableRenderWidget) widget)
                .forEach(movableRenderWidget -> ScreenRendering.widgetMap.get(movableRenderWidget.modFeature)
                        .setPosition(new Position(movableRenderWidget.getX(), movableRenderWidget.getY())));
        super.close();
    }

    @Override
    public void blur() {
        // No blur
    }

    @Override
    protected void applyBlur() {
        // No blur
    }
}
