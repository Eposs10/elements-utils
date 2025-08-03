package dev.eposs.elementsutils.feature.armorhide;

import dev.eposs.elementsutils.config.ModConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * Listener for the RenderArmourCallback event.
 * Prevents rendering of the player's own armor depending on config options.
 */
@Environment(EnvType.CLIENT)
public class RenderListener implements RenderArmourCallback {
	/**
	 * Called before rendering an armor piece.
	 * Cancels rendering for the local player if the corresponding config option is enabled.
	 *
	 * @param instance      The armor feature renderer instance
	 * @param matrices      The matrix stack for rendering
	 * @param vertexConsumers The vertex consumer provider
	 * @param bipedEntityRenderState The render state of the entity
	 * @param stack         The item stack of the armor
	 * @param slot          The equipment slot
	 * @param light         The light value
	 * @param armorModel    The armor model
	 * @return ActionResult.FAIL to cancel rendering, ActionResult.PASS otherwise
	 */
	@Override
	public ActionResult preRenderArmour(
			ArmorFeatureRenderer<?, ?, ?> instance,
			MatrixStack matrices,
			VertexConsumerProvider vertexConsumers,
			BipedEntityRenderState bipedEntityRenderState,
			ItemStack stack,
			EquipmentSlot slot,
			int light,
			BipedEntityModel<?> armorModel
	) {
		ModConfig.PlayerEnhancementsConfig config = ModConfig.getConfig().playerEnhancements;
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		if (config.hideOwnArmor
				&& bipedEntityRenderState instanceof PlayerEntityRenderState pers
				&& player != null
				&& player.getId() == pers.id) {

			// Check slot-specific visibility
			boolean hide = switch (slot) {
				case HEAD -> config.hideHelmet;
				case CHEST -> config.hideChestplate;
				case LEGS -> config.hideLeggings;
				case FEET -> config.hideBoots;
				default -> false;
			};
			return hide ? ActionResult.FAIL : ActionResult.PASS;
		}
		return ActionResult.PASS;
	}
}