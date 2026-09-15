package com.leclowndu93150.baguettelib.mixin;

import com.leclowndu93150.baguettelib.curios.LegacyCurioSlotDataMigration;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.component.DataComponentPatch;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataComponentPatch.class)
public class DataComponentPatchMixin {

    @Shadow
    @Final
    @Mutable
    public static Codec<DataComponentPatch> CODEC;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void baguettelib$migrateLegacyCurioSlotData(CallbackInfo ci) {
        Codec<DataComponentPatch> original = CODEC;
        CODEC = Codec.of(original, new Decoder<DataComponentPatch>() {
            @Override
            public <T> DataResult<Pair<DataComponentPatch, T>> decode(DynamicOps<T> ops, T input) {
                return original.decode(ops, LegacyCurioSlotDataMigration.migrate(ops, input));
            }
        });
    }
}
