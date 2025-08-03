package dev.eposs.elementsutils.mixin.client;

import net.minecraft.client.render.entity.state.TextDisplayEntityRenderState;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextDisplayEntityRenderState.class)
public interface TextDisplayEntityRenderStateAccessor {
	@Accessor("textLines")
	DisplayEntity.TextDisplayEntity.TextLines getTextLines();

	@Accessor("textLines")
	void setTextLines(DisplayEntity.TextDisplayEntity.TextLines lines);
}