package dev.eposs.elementsutils.feature.potion;

import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.rendering.AbstractFeatureWidget;
import dev.eposs.elementsutils.rendering.Feature;
import dev.eposs.elementsutils.rendering.MovableRenderWidget;
import dev.eposs.elementsutils.rendering.Position;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.Window;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import static dev.eposs.elementsutils.rendering.ScreenPositioning.*;

public class PotionDisplay extends AbstractFeatureWidget {
    private static int smallHeal = 0;
    private static int bigHeal = 0;
    private static int smallMana = 0;
    private static int bigMana = 0;

    public static void updatePotions(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        smallHeal = 0;
        bigHeal = 0;
        smallMana = 0;
        bigMana = 0;

        DefaultedList<ItemStack> stacks = client.player.getInventory().main;
        stacks.stream()
                .filter(stack -> !stack.isEmpty() && stack.getItem() == Items.POTION)
                .forEach(stack -> {
                    NbtComponent customData = stack.getComponents().get(DataComponentTypes.CUSTOM_DATA);
                    if (customData != null) {
                        String tag = customData.copyNbt().getString("tag");
                        int count = stack.getCount();

                        switch (tag) {
                            case "heal_potion_small" -> smallHeal += count;
                            case "heal_potion_big" -> bigHeal += count;
                            case "mana_potion_small" -> smallMana += count;
                            case "mana_potion_big" -> bigMana += count;
                        }
                    }
                });
    }

    @Override
    public Position getDefaultPosition() {
        Window window = MinecraftClient.getInstance().getWindow();
        Position position = new Position(window.getScaledWidth() / 2 + 200 - (POTION_GAP * 4), window.getScaledHeight() - 25);
        // Position position = new Position(0, 0);
        ElementsUtils.LOGGER.info(position.toString());
        return position;
    }

    @Override
    public MovableRenderWidget getMovableRenderWidget(Feature feature) {
        return new MovableRenderWidget(getPosition().getX(), getPosition().getY(), IMAGE_SIZE * 4 + GAP * 4, 24, "Potion Display", feature);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (!ModConfig.getConfig().potionDisplay.show) return;
        
        Position position = getPosition();

        int start = position.getX();
        int gap = POTION_GAP;
        int y = position.getY();

        draw(context, client.textRenderer, "small_heal.png", smallHeal, new Position(start, y));
        draw(context, client.textRenderer, "big_heal.png", bigHeal, new Position(start + gap, y));
        draw(context, client.textRenderer, "small_mana.png", smallMana, new Position(start + gap * 2, y));
        draw(context, client.textRenderer, "big_mana.png", bigMana, new Position(start + gap * 3, y));
    }

    private void draw(DrawContext context, TextRenderer textRenderer, String texture, int count, Position position) {
        context.drawTexture(
                RenderLayer::getGuiTextured,
                Identifier.of(ElementsUtils.MOD_ID, "gui/containers/" + texture),
                position.getX(), position.getY(),
                0.0f, 0.0f,
                IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE
        );

        String countString = String.valueOf(count);
        context.drawText(
                textRenderer,
                countString,
                position.getX() + IMAGE_SIZE - textRenderer.getWidth(countString),
                position.getY() + IMAGE_SIZE,
                Colors.WHITE,
                false
        );
    }
}
