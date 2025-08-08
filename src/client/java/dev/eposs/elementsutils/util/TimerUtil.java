package dev.eposs.elementsutils.util;

import dev.eposs.elementsutils.rendering.Position;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

// TODO: Maybe move to common?
public class TimerUtil {

    public static final DateTimeFormatter ABSOLUTE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Parses a given time string into a {@link ZonedDateTime} object.
     * The input time string must be in a format compatible with {@link ZonedDateTime#parse(CharSequence)}.
     * If the input is null, empty, or represents a time before the Unix epoch (January 1, 1970, 00:00:00 UTC), this method returns null.
     *
     * @param time the time string to be parsed
     * @return a {@link ZonedDateTime} object representing the parsed time, or null if the input is invalid or precedes the Unix epoch
     */
    public static @Nullable ZonedDateTime parseTime(String time) {
        if (time == null || time.isEmpty()) return null;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(time);
        return zonedDateTime.isBefore(ZonedDateTime.ofInstant(Instant.EPOCH, zonedDateTime.getZone())) ? null : zonedDateTime;
    }

    /**
     * Calculates the duration between the specified {@code ZonedDateTime} and the current system time.
     *
     * @param time the {@link ZonedDateTime} to calculate the duration from
     * @return a {@link Duration} representing the time elapsed between the given {@code ZonedDateTime} and now
     */
    public static Duration getDuration(ZonedDateTime time) {
        return Duration.between(time, ZonedDateTime.now());
    }

    /**
     * Converts a given {@link Duration} object into a human-readable relative time string.
     * The resulting string represents the duration in days, hours, and minutes, formatted as "Xd Yh Zm".
     * If the duration has no days, hours, or minutes, "0m" will be returned.
     *
     * @param duration the {@link Duration} to be converted into a relative time string, must not be null
     * @return a non-null string representing the relative time in the format "Xd Yh Zm"
     */
    public static @NotNull String toRelativeTime(@NotNull Duration duration) {
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");

        String timeString = sb.toString().trim();
        if (timeString.isEmpty()) timeString = "0m";

        return timeString;
    }

    /**
     * Applies the specified formatting to the given text if formatting is enabled.
     *
     * @param text    the {@link MutableText} to be conditionally formatted
     * @param enabled a boolean indicating whether the formatting should be applied
     * @param format  the {@link Formatting} to apply to the text if enabled
     * @return the {@link MutableText} with the formatting applied if enabled; otherwise, the original text
     */
    public static MutableText optionalFormattedText(MutableText text, boolean enabled, Formatting format) {
        return enabled ? text.formatted(format) : text;
    }


    /**
     * Draws the specified text onto the screen at a given position, with optional outline styling.
     *
     * @param textRenderer the {@link TextRenderer} instance used to render the text
     * @param context      the {@link DrawContext} used to handle drawing operations
     * @param position     the {@link Position} defining the x and y coordinates for the text placement
     * @param line         the vertical offset in lines from the base position
     * @param text         the {@link Text} to be drawn on the screen
     * @param outline      a boolean flag indicating whether the text should be rendered with an outline
     */
    public static void drawText(TextRenderer textRenderer, @NotNull DrawContext context, @NotNull Position position, int line, Text text, boolean outline) {
        context.drawText(
                textRenderer,
                text,
                position.getX(), position.getY() + (Util.getFontLineHeight() * line) + 3,
                Colors.WHITE, outline
        );
    }
}
