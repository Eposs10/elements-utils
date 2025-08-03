package dev.eposs.elementsutils.mixin.client;

import dev.eposs.elementsutils.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.client.render.entity.state.TextDisplayEntityRenderState;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(DisplayEntityRenderer.TextDisplayEntityRenderer.class)
public abstract class TextDisplayEntityRendererMixin {
	/**
	 * Injects at the start of the render method to optionally format numbers in text lines,
	 * depending on the playerLevelConfig.enabled setting.
	 *
	 * @param state The current TextDisplayEntityRenderState.
	 * @param matrixStack The matrix stack for rendering.
	 * @param vertexConsumerProvider The vertex consumer provider.
	 * @param i Render parameter.
	 * @param f Render parameter.
	 * @param ci Callback info.
	 */
	@Inject(
			method = "render(Lnet/minecraft/client/render/entity/state/TextDisplayEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V",
			at = @At("HEAD")
	)
	private void onRender(
			TextDisplayEntityRenderState state,
			net.minecraft.client.util.math.MatrixStack matrixStack,
			net.minecraft.client.render.VertexConsumerProvider vertexConsumerProvider,
			int i,
			float f,
			CallbackInfo ci
	) {
		if (!ModConfig.getConfig().playerLevelConfig.enabled) return;

		var lines = ((TextDisplayEntityRenderStateAccessor) state).getTextLines();
		if (lines == null) return;

		var textRenderer = MinecraftClient.getInstance().textRenderer;
		List<DisplayEntity.TextDisplayEntity.TextLine> newLines = new ArrayList<>();
		int maxWidth = 0;
		boolean changed = false;

		for (var line : lines.lines()) {
			String original = orderedTextToString(line.contents());
			String formatted = formatNumbersInString(original);
			changed |= !original.equals(formatted);

			int width = textRenderer.getWidth(formatted);
			maxWidth = Math.max(maxWidth, width);
			newLines.add(new DisplayEntity.TextDisplayEntity.TextLine(Text.literal(formatted).asOrderedText(), width));
		}

		if (changed) {
			((TextDisplayEntityRenderStateAccessor) state).setTextLines(
					new DisplayEntity.TextDisplayEntity.TextLines(newLines, maxWidth)
			);
		}
	}

	/**
	 * Converts an OrderedText object to a plain String.
	 *
	 * @param orderedText The OrderedText to convert.
	 * @return The plain String representation.
	 */
	@Unique
	private static String orderedTextToString(OrderedText orderedText) {
		StringBuilder sb = new StringBuilder();
		orderedText.accept((i, s, c) -> { sb.appendCodePoint(c); return true; });
		return sb.toString();
	}

	/**
	 * Formats numbers in the input string with thousands separators,
	 * but only if the number is preceded by a space or at the start of the line.
	 *
	 * @param input The input string.
	 * @return The formatted string.
	 */
	@Unique
	private static String formatNumbersInString(String input) {
		Matcher matcher = Pattern.compile("(?<=\\s|^)\\d+").matcher(input);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			try {
				matcher.appendReplacement(sb, String.format(Locale.GERMAN, "%,d", Long.parseLong(matcher.group())));
			} catch (NumberFormatException e) {
				matcher.appendReplacement(sb, matcher.group());
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}