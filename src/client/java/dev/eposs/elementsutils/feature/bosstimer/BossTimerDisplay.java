package dev.eposs.elementsutils.feature.bosstimer;

import dev.eposs.elementsutils.config.ModConfig;
import dev.eposs.elementsutils.rendering.*;
import dev.eposs.elementsutils.util.TimerUtil;
import dev.eposs.elementsutils.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.ZonedDateTime;

import static dev.eposs.elementsutils.rendering.ScreenPositioning.GAP;
import static dev.eposs.elementsutils.util.TimerUtil.getDuration;
import static dev.eposs.elementsutils.util.TimerUtil.optionalFormattedText;

public class BossTimerDisplay extends AbstractFeatureWidget {
    @Override
    public Position getDefaultPosition() {
        MinecraftClient client = MinecraftClient.getInstance();
        return new Position(GAP, (client.getWindow().getScaledHeight() / 2) - (Util.getFontLineHeight() * 3));
    }

    @Override
    public void setPosition(@NotNull Position position) {
        // Add 4 pixel offset so the text is not at the edge of the screen
        super.setPosition(position.plusX(GAP));
    }

    @Override
    public MovableRenderWidget getMovableRenderWidget(Feature feature) {
        // Remove 4 pixel offset added in setPosition()
        return new MovableRenderWidget(getPosition().getX() - GAP, getPosition().getY(), 150, Util.getFontLineHeight() * 6, "Boss Timer Display", feature);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        ModConfig.BossTimerConfig config = ModConfig.getConfig().bossTimer;
        if (!config.show) return;

        BossTimerData timerData = BossTimerData.getInstance();

        drawText(client, context, 0, Text.translatable("elements-utils.display.bossTimer.title").formatted(Formatting.UNDERLINE));
        drawText(client, context, 1, formattedText("Axolotl", Formatting.LIGHT_PURPLE, timerData.getAxolotl(), config));
        drawText(client, context, 2, formattedText("Zombie", Formatting.GREEN, timerData.getZombie(), config));
        drawText(client, context, 3, formattedText("Spider", Formatting.DARK_GRAY, timerData.getSpider(), config));
        drawText(client, context, 4, formattedText("Bogged", Formatting.DARK_GREEN, timerData.getBogged(), config));
        drawText(client, context, 5, formattedText("Piglin", Formatting.RED, timerData.getPiglin(), config));
    }

    public static void toggleDisplay(@NotNull MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        ModConfig.getConfig().bossTimer.show = !ModConfig.getConfig().bossTimer.show;
        ModConfig.save();

        if (ModConfig.getConfig().bossTimer.show) BossTimerData.updateData();
    }

    private Text formattedText(String name, Formatting nameColor, ZonedDateTime time, ModConfig.BossTimerConfig config) {
        return Text.literal("")
                .append(TimerUtil.optionalFormattedText(Text.literal(name + ": "), config.colorBossNames, nameColor))
                .append(time == null
                        ? Text.translatable("elements-utils.unknown")
                        : optionalFormattedText(
                        config.bossTimeFormat == ModConfig.TimeFormat.RELATIVE
                                ? toRelativeTime(time)
                                : Text.literal(time.format(TimerUtil.ABSOLUTE_TIME_FORMATTER)),
                        config.colorBossTime, getTimeColor(time))
                );
    }

    private Formatting getTimeColor(ZonedDateTime dateTime) {
        if (dateTime == null) return Formatting.WHITE;

        Duration duration = getDuration(dateTime);

        if (duration.isNegative()) return Formatting.WHITE;

        long hours = duration.toHours();

        if (hours >= 36 && hours < 48) return Formatting.YELLOW;

        if (hours >= 48) return Formatting.RED;

        return Formatting.GREEN;
    }

    private MutableText toRelativeTime(@NotNull ZonedDateTime dateTime) {
        Duration duration = getDuration(dateTime);
        if (duration.isNegative()) return Text.translatable("elements-utils.unknown");

        return Text.literal(TimerUtil.toRelativeTime(duration));
    }

    private void drawText(@NotNull MinecraftClient client, DrawContext context, int line, Text text) {
        boolean outline = ModConfig.getConfig().bossTimer.textOutline;
        TimerUtil.drawText(client.textRenderer, context, getPosition(), line, text, outline);
    }
}
