package dev.eposs.elementsutils.mixin.client;

import dev.eposs.elementsutils.feature.armorhide.RenderArmourCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for ArmorFeatureRenderer to allow interception of armor rendering.
 * Enables custom logic (e.g. hiding armor) via the RenderArmourCallback event.
 */
@Mixin(ArmorFeatureRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class ArmorFeatureRendererMixin {
	/**
	 * Stores the current render state for use in the redirect.
	 */
	@Unique
	@Nullable
	private BipedEntityRenderState currentRenderState;

	/**
	 * Captures the current render state at the start of the render method.
	 *
	 * @param matrices         The matrix stack
	 * @param vertexConsumers  The vertex consumer provider
	 * @param i                Light value
	 * @param state            The entity render state
	 * @param f                Unused
	 * @param g                Unused
	 * @param ci               Callback info
	 */
	@Inject(
			method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
			at = @At("HEAD")
	)
	private void preRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i, BipedEntityRenderState state, float f, float g, CallbackInfo ci) {
		this.currentRenderState = state;
	}

	/**
	 * Clears the current render state after rendering is complete.
	 *
	 * @param matrices         The matrix stack
	 * @param vertexConsumers  The vertex consumer provider
	 * @param i                Light value
	 * @param state            The entity render state
	 * @param f                Unused
	 * @param g                Unused
	 * @param ci               Callback info
	 */
	@Inject(
			method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
			at = @At("RETURN")
	)
	private void postRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int i, BipedEntityRenderState state, float f, float g, CallbackInfo ci) {
		this.currentRenderState = null;
	}

	/**
	 * Redirects the call to renderArmor to allow event-based cancellation.
	 * If the RenderArmourCallback returns FAIL, the armor piece is not rendered.
	 *
	 * @param instance      The armor feature renderer instance
	 * @param matrices      The matrix stack
	 * @param vertexConsumers The vertex consumer provider
	 * @param stack         The item stack of the armor
	 * @param slot          The equipment slot
	 * @param light         The light value
	 * @param armorModel    The armor model
	 */
	@Redirect(
			method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/model/BipedEntityModel;)V")
	)
	private void renderArmor(
			ArmorFeatureRenderer<?, ?, ?> instance,
			MatrixStack matrices,
			VertexConsumerProvider vertexConsumers,
			ItemStack stack,
			EquipmentSlot slot,
			int light,
			BipedEntityModel<?> armorModel
	) {
		ActionResult result = RenderArmourCallback.EVENT.invoker().preRenderArmour(
				instance, matrices, vertexConsumers, currentRenderState, stack, slot, light, armorModel
		);
		if (result == ActionResult.FAIL) {
			return;
		}
		this.renderArmor(matrices, vertexConsumers, stack, slot, light, armorModel);
	}

	/**
	 * Shadowed method for rendering the armor piece.
	 */
	@Shadow
	protected abstract void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, EquipmentSlot slot, int light, BipedEntityModel<?> armorModel);
}