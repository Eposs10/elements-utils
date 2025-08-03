package dev.eposs.elementsutils.mixin.client;

import dev.eposs.elementsutils.config.ModConfig;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
	/**
	 * Pattern to match Lucky Drop messages in chat.
	 */
	@Unique
	private static final Pattern LUCKY_DROP_PATTERN = Pattern.compile("Server: Du hast einen Lucky Drop in Höhe von (\\d+) Leveln bekommen\\.");

	/**
	 * Stores the history of Lucky Drop events as timestamp and level pairs.
	 */
	@Unique
	private static final List<Map.Entry<Long, Integer>> luckyDropHistory = new ArrayList<>();

	/**
	 * Milliseconds per minute constant.
	 */
	@Unique
	private static final long MILLIS_PER_MINUTE = 60_000L;

	/**
	 * Replaces the first occurrence of the original string in the given text with the replacement,
	 * preserving text formatting and siblings.
	 *
	 * @param text        The original text object.
	 * @param original    The string to be replaced.
	 * @param replacement The replacement string.
	 * @return A new MutableText with the replacement applied.
	 */
	@Unique
	private MutableText replaceLevelInText(Text text, String original, String replacement) {
		MutableText result = Text.empty();

		if (text.getSiblings().isEmpty()) {
			String content = text.getString() != null ? text.getString() : "";
			int idx = content.indexOf(original);

			if (idx != -1) {
				if (idx > 0) {
					result.append(Text.literal(content.substring(0, idx)).setStyle(text.getStyle()));
				}
				result.append(Text.literal(replacement).setStyle(text.getStyle()));
				int endIdx = idx + original.length();
				if (endIdx < content.length()) {
					result.append(Text.literal(content.substring(endIdx)).setStyle(text.getStyle()));
				}
			} else if (!content.isEmpty()) {
				result.append(Text.literal(content).setStyle(text.getStyle()));
			}
		} else {
			MutableText contentFirst = text.copy();
			contentFirst.getSiblings().clear();
			result.append(Text.literal(contentFirst.getString()).setStyle(text.getStyle()));
		}

		for (Text sibling : text.getSiblings()) {
			result.append(replaceLevelInText(sibling, original, replacement));
		}

		return result;
	}

	/**
	 * Injects into the chat message handling to add a Lucky Drop summary if enabled in the config.
	 *
	 * @param message The chat message.
	 * @param ci      The callback info.
	 */
	@Inject(method = "addMessage*", at = @At("HEAD"), cancellable = true)
	private void onAddMessage(Text message, CallbackInfo ci) {
		if (!ModConfig.getConfig().chatEnhancements.showLuckyDropSummary) {
			return;
		}
		String msg = message.getString();
		if (msg.matches(".*Level/\\d+[Mm]in\\).*")) {
			return;
		}
		Matcher matcher = LUCKY_DROP_PATTERN.matcher(msg);
		if (matcher.find()) {
			int level = Integer.parseInt(matcher.group(1));
			long now = System.currentTimeMillis();
			luckyDropHistory.add(new AbstractMap.SimpleEntry<>(now, level));
			luckyDropHistory.removeIf(entry -> now - entry.getKey() > ModConfig.getConfig().chatEnhancements.luckyDropSummaryMinutes * MILLIS_PER_MINUTE);
			int sum = luckyDropHistory.stream().mapToInt(Map.Entry::getValue).sum();

			java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.GERMAN);
			String formattedLevel = nf.format(level);
			String formattedSum = nf.format(sum);

			String original = matcher.group(1);
			MutableText formattedText = replaceLevelInText(message, original, formattedLevel);

			MutableText prefix = Text.literal(" (").styled(style -> style.withColor(Formatting.GRAY));
			MutableText sumText = Text.literal(formattedSum + " Level").styled(style -> style.withColor(Formatting.AQUA));
			MutableText suffix = Text.literal("/" + ModConfig.getConfig().chatEnhancements.luckyDropSummaryMinutes + "Min)").styled(style -> style.withColor(Formatting.GRAY));

			MutableText newMsg = formattedText.append(prefix).append(sumText).append(suffix);

			((ChatHud)(Object)this).addMessage(newMsg);
			ci.cancel();
		}
	}
}