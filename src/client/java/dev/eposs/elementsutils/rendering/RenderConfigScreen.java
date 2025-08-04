package dev.eposs.elementsutils.rendering;

import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.ElementsUtilsClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class RenderConfigScreen extends Screen {
    // private static List<MovableRenderWidget> widgets = List.of(
    //         new MovableRenderWidget(100, 100, 50, 50, "Movable"),
    //         new MovableRenderWidget(200, 100, 10, 50, "Movable"),
    //         new MovableRenderWidget(300, 100, 50, 10, "Movable")
    // );

    public RenderConfigScreen() {
        super(Text.literal("Render Config"));
    }

    @Override
    protected void init() {
        super.init();
        ElementsUtilsClient.widgets.forEach(customScreenWidget -> addDrawableChild(customScreenWidget.getMovableRenderWidget()));

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .dimensions(width / 2 - 50, height - 30, 100, 20)
                .build());
    }

    @Override
    public void close() {
        // TODO: Save config
        List<MovableRenderWidget> list = new ArrayList<>();
        for (Element widget : children()) {
            if (widget instanceof MovableRenderWidget) {
                list.add((MovableRenderWidget) widget);
            }
        }
        // TODO: figure out how to save xD
        // if (ElementsUtilsClient.widgets.size() == list.size()) ElementsUtilsClient.widgets = list;
        // else ElementsUtils.LOGGER.warn("Invalid render config size, discarding");

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
