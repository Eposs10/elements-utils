package dev.eposs.elementsutils.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

public class Util {

    public static void sendChatMessage(Text message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        client.player.sendMessage(Text.literal("")
                        .append(Text.literal("[ElementsUtils] ").formatted(Formatting.GOLD))
                        .append(message)
                , false);
    }

    public static GenericContainerScreen getEnderChestScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        Screen currentScreen = client.currentScreen;

        if (currentScreen instanceof GenericContainerScreen screen) {
            if (screen.getTitle().getContent() instanceof TranslatableTextContent translatable) {
                if (translatable.getKey().equals("container.enderchest")) return screen;
            }
        }

        return null;
    }

    public static String formatLevel(int level) {
        return String.format("%,d", level).replace(',', '.');
    }

    public static int parseXp(String xp) {
        return Integer.parseInt(xp.replace(".", "").replace(",", ""));
    }
    
    public static int getFontLineHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight + 3;
    }
}
