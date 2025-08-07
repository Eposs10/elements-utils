package dev.eposs.elementsutils.rendering;

import dev.eposs.elementsutils.config.ModConfig;
import net.minecraft.client.util.Window;

public class ScreenPositioning {
    public static int PET_WIDTH = 64;
    public static final int PET_HEIGHT = 32;
    public static final int IMAGE_SIZE = 16;
    public static final int GAP = 4;
    public static final int POTION_GAP = 20;

    // public static Position getMoonPhasePosition(Window window) {
    //     ModConfig.Position displayPosition = ModConfig.getConfig().displayPosition;
    //
    //     int xOffset = 0;
    //     int yOffset = 0;
    //
    //     if (ModConfig.getConfig().showTimeDisplay) {
    //         xOffset += IMAGE_SIZE + GAP;
    //     }
    //     if (ModConfig.getConfig().showPetDisplay) {
    //         yOffset += PET_HEIGHT + GAP;
    //     }
    //
    //     return getPosition(window, displayPosition, xOffset, yOffset);
    // }
    //
    // public static Position getTimePosition(Window window) {
    //     ModConfig.Position displayPosition = ModConfig.getConfig().displayPosition;
    //
    //     int xOffset = 0;
    //     int yOffset = 0;
    //
    //     if (ModConfig.getConfig().showPetDisplay) {
    //         yOffset += PET_HEIGHT + GAP;
    //     }
    //    
    //     return getPosition(window, displayPosition, xOffset, yOffset);
    // }

    public static Position getPetPosition(Window window) {
        ModConfig.Position displayPosition = ModConfig.getConfig().displayPosition;

        int xOffset = window.getScaledWidth() - PET_WIDTH;
        int yOffset = window.getScaledHeight() - PET_HEIGHT;

        switch (displayPosition) {
            case TOP_LEFT -> {
                return new Position(0, 0);
            }
            case TOP_RIGHT -> {
                return new Position(xOffset, 0);
            }
            case BOTTOM_LEFT -> {
                return new Position(0, yOffset);
            }
            case BOTTOM_RIGHT -> {
                return new Position(xOffset, yOffset);
            }
        }

        return new Position(0, 0);
    }

    // private static Position getPosition(Window window, ModConfig.Position displayPosition, int xOffset, int yOffset) {
    //     switch (displayPosition) {
    //         case TOP_LEFT -> {
    //             return new Position(xOffset, yOffset);
    //         }
    //         case TOP_RIGHT -> {
    //             return new Position(window.getScaledWidth() - IMAGE_SIZE - xOffset, yOffset);
    //         }
    //         case BOTTOM_LEFT -> {
    //             return new Position(xOffset, window.getScaledHeight() - IMAGE_SIZE - yOffset);
    //         }
    //         case BOTTOM_RIGHT -> {
    //             return new Position(window.getScaledWidth() - IMAGE_SIZE - xOffset, window.getScaledHeight() - IMAGE_SIZE - yOffset);
    //         }
    //         default -> {
    //             return new Position(0, 0);
    //         }
    //     }
    // }
}
