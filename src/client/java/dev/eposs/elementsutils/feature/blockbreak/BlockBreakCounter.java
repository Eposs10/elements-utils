package dev.eposs.elementsutils.feature.blockbreak;

import dev.eposs.elementsutils.ElementsUtils;
import dev.eposs.elementsutils.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

// TODO: Display on screen
public class BlockBreakCounter {
    private static final List<Long> breakTimes = new ArrayList<>();

    public static void onBreak(ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, BlockPos blockPos, BlockState blockState) {
        breakTimes.add(System.currentTimeMillis());
    }

    public static float getBreaksPerSeconds() {
        int timeThreshold = ModConfig.getConfig().blockBreakCounterConfig.time;
        breakTimes.removeIf(time -> System.currentTimeMillis() - time > timeThreshold * 1000L);
        float count = breakTimes.size() / (float) timeThreshold;
        ElementsUtils.LOGGER.info("Breaks per second: {}", String.format("%.2f", count));
        return count;
    }
}
