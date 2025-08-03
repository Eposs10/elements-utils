package dev.eposs.elementsutils.feature.excaliburtime;

import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.util.TimerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.ZonedDateTime;

public class ExcaliburTimeDisplay {
    private static final int DAYS = 7;
    private static final long EXTRA_SECONDS = DAYS * 20;

    public static void toggleDisplay(@NotNull MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        ModConfig.getConfig().excaliburTime.show = !ModConfig.getConfig().excaliburTime.show;
        ModConfig.save();

        if (ModConfig.getConfig().excaliburTime.show) ExcaliburTimeData.updateData();
    }

    public static void render(DrawContext context, MinecraftClient client, int baseLine) {
        var config = ModConfig.getConfig().excaliburTime;
        if (!config.show) return;

        var data = ExcaliburTimeData.getInstance();
        ZonedDateTime targetTime = calculateTargetTime(data.getTime());
        Duration timeUntilTarget = targetTime == null ? Duration.ZERO : Duration.between(ZonedDateTime.now(), targetTime);

        drawText(client, context, baseLine, Text.translatable("elements-utils.display.excaliburTime.title").formatted(Formatting.UNDERLINE));
        String nextUser = data.getNextUser();
        drawText(client, context, baseLine + 1, Text.literal("")
                .append(TimerUtil.optionalFormattedText(Text.translatable("elements-utils.display.excaliburTime.next_player"), config.colorExcaliburNames, Formatting.RED))
                .append(TimerUtil.optionalFormattedText(
                        (nextUser == null || nextUser.isEmpty())
                                ? Text.translatable("elements-utils.unknown")
                                : Text.literal(nextUser),
                        config.colorExcaliburNames,
                        Formatting.GOLD
                ))
        );
        drawText(client, context, baseLine + 2, Text.literal("")
                .append(TimerUtil.optionalFormattedText(Text.translatable("elements-utils.display.excaliburTime.time_left"), config.colorExcaliburTime, Formatting.AQUA))
                .append(config.excaliburTimeFormat == ModConfig.TimeFormat.RELATIVE
                        ? toRelativeTime(timeUntilTarget)
                        : targetTime == null
                                ? Text.translatable("elements-utils.unknown")
                                : TimerUtil.optionalFormattedText(
                                        Text.literal(TimerUtil.ABSOLUTE_TIME_FORMATTER.format(targetTime)),
                                        config.colorExcaliburTime,
                                        Formatting.GREEN
                        )
                )
        );
    }

    private static Text toRelativeTime(Duration duration) {
        String timeString = TimerUtil.toRelativeTime(duration.abs());

        // Check if in past
        String key = duration.isNegative()
                ? "elements-utils.display.excaliburTime.relative_after"
                : "elements-utils.display.excaliburTime.relative";

        return TimerUtil.optionalFormattedText(
                Text.translatable(key, timeString),
                ModConfig.getConfig().excaliburTime.colorExcaliburTime,
                Formatting.GREEN
        );
    }

    /**
     * Calculates the target time based on a given starting time by adding a predefined number
     * of days and seconds.
     *
     * @param startTime The initial {@link ZonedDateTime} to calculate the target time from.
     *                  If null, this method returns null.
     * @return A {@link ZonedDateTime} representing the calculated target time, or null if the
     * input is null.
     */
    private static ZonedDateTime calculateTargetTime(ZonedDateTime startTime) {
        if (startTime == null) return null;
        return startTime
                .plusDays(DAYS)
                .plusSeconds(EXTRA_SECONDS);
    }
    
    private static void drawText(MinecraftClient client, DrawContext context, int line, Text text) {
        boolean outline = ModConfig.getConfig().excaliburTime.textOutline;
        TimerUtil.drawText(client, context, line, text, outline);
    }
}
