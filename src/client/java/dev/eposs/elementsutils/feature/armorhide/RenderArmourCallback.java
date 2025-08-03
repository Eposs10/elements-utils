package dev.eposs.elementsutils.feature.armorhide;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * Callback interface for intercepting armor rendering.
 * Implementations can cancel or modify the rendering of armor pieces.
 */
@Environment(EnvType.CLIENT)
public interface RenderArmourCallback {
	/**
	 * Event for registering {@link RenderArmourCallback} listeners.
	 * All listeners are called in order; the first non-PASS result is used.
	 */
	Event<RenderArmourCallback> EVENT = EventFactory.createArrayBacked(RenderArmourCallback.class,
			listeners -> (renderer, matrices, vertexConsumers, state, stack, slot, light, armorModel) -> {
				for (RenderArmourCallback listener : listeners) {
					ActionResult result = listener.preRenderArmour(renderer, matrices, vertexConsumers, state, stack, slot, light, armorModel);
					if (result != ActionResult.PASS) {
						return result;
					}
				}
				return ActionResult.PASS;
			});

	/**
	 * Called before an armor piece is rendered.
	 * Return {@link ActionResult#FAIL} to cancel rendering, or {@link ActionResult#PASS} to allow it.
	 *
	 * @param renderer         The armor feature renderer instance
	 * @param matrices         The matrix stack for rendering
	 * @param vertexConsumers  The vertex consumer provider
	 * @param state            The render state of the entity
	 * @param stack            The item stack of the armor
	 * @param slot             The equipment slot
	 * @param light            The light value
	 * @param armorModel       The armor model
	 * @return {@link ActionResult#FAIL} to cancel rendering, {@link ActionResult#PASS} otherwise
	 */
	ActionResult preRenderArmour(
			ArmorFeatureRenderer<?, ?, ?> renderer,
			MatrixStack matrices,
			VertexConsumerProvider vertexConsumers,
			BipedEntityRenderState state,
			ItemStack stack,
			EquipmentSlot slot,
			int light,
			BipedEntityModel<?> armorModel
	);
}