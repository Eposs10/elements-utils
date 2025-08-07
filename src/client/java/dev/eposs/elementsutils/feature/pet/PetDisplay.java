package dev.eposs.elementsutils.feature.pet;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.rendering.Position;
import dev.eposs.elementsutils.rendering.ScreenPositioning;
import dev.eposs.elementsutils.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.render.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.nbt.visitor.StringNbtWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;

public class PetDisplay {
    private static ItemStack pet = ItemStack.EMPTY;
    private static String petNbtData;
    private static int currentXP = 0;
    private static int nextLvlXP = 0;

    public static void updatePetXP(Text text, boolean fromTooltip) {
        String startText = fromTooltip ? "Pet XP: " : "Pet: ";
        String slashText = fromTooltip ? " / " : "/";

        String msg = text.getString();
        if (!msg.contains(startText)) return;

        int start = msg.indexOf(startText);
        int slash = msg.indexOf(slashText, start);
        int end = msg.indexOf(" XP", slash);

        try {
            String currentXpStr = msg.substring(start + startText.length(), slash).replaceAll("[.,]", "");
            String nextLvlXpStr = msg.substring(slash + slashText.length(), fromTooltip ? msg.length() : end).replaceAll("[.,]", "");
            currentXP = Integer.parseInt(currentXpStr);
            nextLvlXP = Integer.parseInt(nextLvlXpStr);
        } catch (Exception e) {
            ElementsUtils.LOGGER.debug("Failed to parse pet XP");
        }
    }

    public static void updatePet(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        GenericContainerScreen enderChestScreen = Util.getEnderChestScreen();
        if (enderChestScreen == null) return;

        GenericContainerScreenHandler screenHandler = enderChestScreen.getScreenHandler();
        Slot checkSlot = screenHandler.slots.get(4);
        Slot petSlot = screenHandler.slots.get(13);
        if (checkSlot.hasStack() && petSlot.hasStack()) {

            ItemStack checkStack = checkSlot.getStack();
            if (checkStack.getItem() == Items.OAK_HANGING_SIGN && checkStack.getName().getString().equals("Pets")) {

                ItemStack petStack = petSlot.getStack();
                // Only update if new stack
                if (pet == petStack) return;
                
                if (petStack.getItem() == Items.PLAYER_HEAD) {
                    pet = petStack;
                    List<Text> tooltip = petStack.getTooltip(Item.TooltipContext.DEFAULT, client.player, TooltipType.BASIC);
                    tooltip.stream()
                            .filter(text -> text.getString().contains("Pet XP:"))
                            .findFirst()
                            .ifPresent(text -> PetDisplay.updatePetXP(text, true));

                    petNbtData = new StringNbtWriter().apply(pet.toNbt(client.world.getRegistryManager()));
                } else {
                    // Set empty if no pet was found
                    pet = ItemStack.EMPTY;
                    petNbtData = null;
                    currentXP = 0;
                    nextLvlXP = 0;
                }
            }
        }
    }

    public static void render(DrawContext context, MinecraftClient client) {
        if (!ModConfig.getConfig().showPetDisplay) return;

        if (pet.isEmpty()) {
            MutableText noPet = Text.translatable("elements-utils.display.pet.no_pet");
            int textLength = client.textRenderer.getWidth(noPet);
            ScreenPositioning.PET_WIDTH = textLength + 12; // text + gap * 2
            Position position = ScreenPositioning.getPetPosition(client.getWindow());
            context.drawText(
                    client.textRenderer,
                    noPet,
                    position.getX() + 6, position.getY() + 12,
                    Colors.WHITE, false
            );
            return;
        }

        String levelText = "Level " + getPetLevel();
        int textLength = Math.max(client.textRenderer.getWidth(pet.getName()), client.textRenderer.getWidth(levelText));
        ScreenPositioning.PET_WIDTH = textLength + 32 + 6; // text + circleWidth + gap

        Position position = ScreenPositioning.getPetPosition(client.getWindow());

        renderCircle(context, new Position(position.getX() + 16, position.getY() + 16));

        context.drawItem(pet, position.getX() + 8, position.getY() + 8);
        context.drawText(client.textRenderer, pet.getName(), position.getX() + 32, position.getY() + 6, Colors.WHITE, false); // x: circleWidth
        context.drawText(client.textRenderer, levelText, position.getX() + 32, position.getY() + 6 + 8 + 4, Colors.WHITE, false); // y: marginTop + textHeight + gap
    }

    private static void renderCircle(@NotNull DrawContext context, @NotNull Position centerPosition) {
        int segments = 64;
        float innerRadius = 10;
        float outerRadius = 12;
        float centerX = centerPosition.getX();
        float centerY = centerPosition.getY();

        Matrix4f transformationMatrix = context.getMatrices().peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();

        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        for (int i = 0; i < segments; i++) {
            // Start at the top
            float angle = (float) (Math.PI * (2.0F * i / segments + 1.5F));
            float angleNext = (float) (Math.PI * (2.0F * (i + 1) / segments + 1.5F));

            float outerX1 = centerX + MathHelper.cos(angle) * outerRadius;
            float outerY1 = centerY + MathHelper.sin(angle) * outerRadius;

            float outerX2 = centerX + MathHelper.cos(angleNext) * outerRadius;
            float outerY2 = centerY + MathHelper.sin(angleNext) * outerRadius;

            float innerX1 = centerX + MathHelper.cos(angle) * innerRadius;
            float innerY1 = centerY + MathHelper.sin(angle) * innerRadius;

            float innerX2 = centerX + MathHelper.cos(angleNext) * innerRadius;
            float innerY2 = centerY + MathHelper.sin(angleNext) * innerRadius;

            int color = Colors.GREEN;
            if (nextLvlXP > 0 && (float) i / segments > (float) currentXP / nextLvlXP) color = Colors.LIGHT_GRAY;

            buffer.vertex(transformationMatrix, innerX1, innerY1, 0).color(color);
            buffer.vertex(transformationMatrix, innerX2, innerY2, 0).color(color);
            buffer.vertex(transformationMatrix, outerX2, outerY2, 0).color(color);
            buffer.vertex(transformationMatrix, outerX1, outerY1, 0).color(color);
        }
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    private static int getPetLevel() {
        if (nextLvlXP == 5000) return 1;
        if (nextLvlXP == 20000) return 2;
        if (nextLvlXP == 50000) return 3;
        if (nextLvlXP == 100000) return 4;
        if (nextLvlXP == 200000) return 5;
        if (nextLvlXP == 350000) return 6;
        if (nextLvlXP == 550000) return 7;
        if (nextLvlXP == 750000) return 8;
        if (nextLvlXP == 1000000) return 9;
        if (nextLvlXP == -1) return 10;
        return 0;
    }

    public static void savePet() {
        ElementsUtils.LOGGER.info("Saving Pet Data...");

        ModConfig.InternalConfig.PetData petData = new ModConfig.InternalConfig.PetData();

        petData.data = petNbtData;
        petData.currentXP = currentXP;
        petData.nextLvlXP = nextLvlXP;

        ModConfig.getConfig().internal.petData = petData;
        ModConfig.save();
    }

    public static void loadPet(RegistryWrapper.WrapperLookup registry) {
        ElementsUtils.LOGGER.info("Loading pet data...");

        ModConfig.InternalConfig.PetData petData = ModConfig.getConfig().internal.petData;

        try {
            pet = ItemStack.fromNbtOrEmpty(registry, StringNbtReader.parse(petData.data));
        } catch (Exception e) {
            ElementsUtils.LOGGER.error("Failed to parse pet data. Error: {}", e.getMessage());
            return;
        }

        petNbtData = petData.data;
        currentXP = petData.currentXP;
        nextLvlXP = petData.nextLvlXP;
    }

    public static void setPetMaxLevel() {
        nextLvlXP = -1;
    }
}
