package dev.eposs.elementsutils.util;

import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.rendering.RenderConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class DevUtil {
    public static void doSomething(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (!ModConfig.getConfig().devUtils.enable) return;

        ElementsUtils.LOGGER.info("dev");

        
        MinecraftClient.getInstance().setScreen(new RenderConfigScreen());


        // PlayerInventory inventory = client.player.getInventory();
        // ItemStack stack = inventory.getStack(0);
        // ElementsUtils.LOGGER.info(stack.getItemName().getString());
        // Item item = stack.getItem();
        // ElementsUtils.LOGGER.info(item.getTranslationKey());
        // if (item == Items.POTION) {
        //     ElementsUtils.LOGGER.info(item.getName().getString());
        //     ElementsUtils.LOGGER.info(stack.getComponents().toString());
        // }

        // if (client.crosshairTarget instanceof EntityHitResult entityHitResult) {
        //     client.player.sendMessage(Text.literal(entityHitResult.getEntity().getType().toString()), false);
        //
        //     if (entityHitResult.getEntity() instanceof VillagerEntity villager) {
        //         String text = "...";
        //         // text = villager.getCustomName().getString();
        //
        //         NbtCompound nbt = new NbtCompound();
        //         villager.writeNbt(nbt);
        //         ElementsUtils.LOGGER.info(nbt.toString());
        //
        //         ItemStack offHandStack = villager.getOffHandStack();
        //         if (offHandStack != null) {
        //             Text customName = offHandStack.getCustomName();
        //             if (customName != null) {
        //                 text = customName.getString();
        //             }
        //         } else {
        //             text = "none";
        //         }
        //
        //         client.player.sendMessage(Text.literal(text), false);
        //     }
        // }
    }
}
