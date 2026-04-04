package com.leclowndu93150.baguettelib.curios;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CurioSlotData(String slotType, int slotIndex, boolean wasEquipped, boolean isCosmetic) {

    public static final Codec<CurioSlotData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("slotType").forGetter(CurioSlotData::slotType),
                    Codec.INT.fieldOf("slotIndex").forGetter(CurioSlotData::slotIndex),
                    Codec.BOOL.fieldOf("wasEquipped").forGetter(CurioSlotData::wasEquipped),
                    Codec.BOOL.optionalFieldOf("isCosmetic", false).forGetter(CurioSlotData::isCosmetic)
            ).apply(instance, CurioSlotData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CurioSlotData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CurioSlotData::slotType,
            ByteBufCodecs.VAR_INT, CurioSlotData::slotIndex,
            ByteBufCodecs.BOOL, CurioSlotData::wasEquipped,
            ByteBufCodecs.BOOL, CurioSlotData::isCosmetic,
            CurioSlotData::new);
}
