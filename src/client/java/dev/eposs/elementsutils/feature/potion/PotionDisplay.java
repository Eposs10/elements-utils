package dev.eposs.elementsutils.feature.potion;

import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.rendering.Position;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class PotionDisplay {
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

    public static void render(DrawContext context, MinecraftClient client) {
        if (!ModConfig.getConfig().potionDisplay.show) return;

        int start = 100;
        int gap = 20;
        int y = context.getScaledWindowHeight() - 25;

        switch (ModConfig.getConfig().potionDisplay.position) {
            case LEFT -> start = context.getScaledWindowWidth() / 2 - 200;
            case RIGHT -> start = context.getScaledWindowWidth() / 2 + 200 - (gap * 4);
        }

        draw(context, client.textRenderer, "small_heal.png", smallHeal, new Position(start, y));
        draw(context, client.textRenderer, "big_heal.png", bigHeal, new Position(start + gap, y));
        draw(context, client.textRenderer, "small_mana.png", smallMana, new Position(start + gap * 2, y));
        draw(context, client.textRenderer, "big_mana.png", bigMana, new Position(start + gap * 3, y));
    }

    private static void draw(DrawContext context, TextRenderer textRenderer, String texture, int count, Position position) {
        var size = 16;
        context.drawTexture(
                RenderLayer::getGuiTextured,
                Identifier.of(ElementsUtils.MOD_ID, "gui/containers/" + texture),
                position.getX(), position.getY(),
                0.0f, 0.0f,
                size, size, size, size
        );

        String countString = String.valueOf(count);
        context.drawText(
                textRenderer,
                countString,
                position.getX() + size - textRenderer.getWidth(countString),
                position.getY() + size,
                Colors.WHITE,
                false
        );
    }
}
