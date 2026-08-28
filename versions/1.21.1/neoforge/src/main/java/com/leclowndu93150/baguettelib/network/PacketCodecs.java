package com.leclowndu93150.baguettelib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class PacketCodecs {

    // Primitives
    public static <T extends Enum<T>> StreamCodec<ByteBuf, T> enumCodec(Class<T> enumClass) {
        return ByteBufCodecs.VAR_INT.map(
                ordinal -> enumClass.getEnumConstants()[ordinal],
                Enum::ordinal
        );
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, Optional<T>> optional(StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        return StreamCodec.composite(
                ByteBufCodecs.BOOL, (Optional<T> value) -> value.isPresent(),
                ByteBufCodecs.optional(codec), Function.identity(),
                (present, value) -> value
        );
    }

    public static StreamCodec<ByteBuf, ResourceLocation> resourceLocation() {
        return ResourceLocation.STREAM_CODEC;
    }

    public static StreamCodec<ByteBuf, String> string() {
        return ByteBufCodecs.STRING_UTF8;
    }

    public static StreamCodec<ByteBuf, Integer> integer() {
        return ByteBufCodecs.VAR_INT;
    }

    public static StreamCodec<ByteBuf, Long> longValue() {
        return ByteBufCodecs.VAR_LONG;
    }

    public static StreamCodec<ByteBuf, Float> floatValue() {
        return ByteBufCodecs.FLOAT;
    }

    public static StreamCodec<ByteBuf, Double> doubleValue() {
        return ByteBufCodecs.DOUBLE;
    }

    public static StreamCodec<ByteBuf, Boolean> bool() {
        return ByteBufCodecs.BOOL;
    }

    public static StreamCodec<ByteBuf, Byte> byteValue() {
        return ByteBufCodecs.BYTE;
    }

    public static StreamCodec<ByteBuf, Short> shortValue() {
        return ByteBufCodecs.SHORT;
    }

    public static StreamCodec<ByteBuf, byte[]> byteArray() {
        return ByteBufCodecs.BYTE_ARRAY;
    }

    public static StreamCodec<ByteBuf, UUID> uuid() {
        return net.minecraft.core.UUIDUtil.STREAM_CODEC;
    }

    // Minecraft Types
    public static StreamCodec<RegistryFriendlyByteBuf, ItemStack> itemStack() {
        return ItemStack.STREAM_CODEC;
    }

    public static StreamCodec<RegistryFriendlyByteBuf, Item> item() {
        return ByteBufCodecs.registry(net.minecraft.core.registries.Registries.ITEM);
    }

    public static StreamCodec<RegistryFriendlyByteBuf, Block> block() {
        return ByteBufCodecs.registry(net.minecraft.core.registries.Registries.BLOCK);
    }

    public static StreamCodec<ByteBuf, BlockState> blockState() {
        return ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY);
    }

    public static StreamCodec<ByteBuf, BlockPos> blockPos() {
        return BlockPos.STREAM_CODEC;
    }

    public static StreamCodec<ByteBuf, GlobalPos> globalPos() {
        return GlobalPos.STREAM_CODEC;
    }

    public static StreamCodec<ByteBuf, Vec3> vec3() {
        return StreamCodec.composite(
                ByteBufCodecs.DOUBLE, Vec3::x,
                ByteBufCodecs.DOUBLE, Vec3::y,
                ByteBufCodecs.DOUBLE, Vec3::z,
                Vec3::new
        );
    }

    public static StreamCodec<ByteBuf, Vector3f> vector3f() {
        return ByteBufCodecs.VECTOR3F;
    }

    public static StreamCodec<RegistryFriendlyByteBuf, MobEffectInstance> mobEffectInstance() {
        return MobEffectInstance.STREAM_CODEC;
    }

    // Collections
    public static <T> StreamCodec<RegistryFriendlyByteBuf, List<T>> list(StreamCodec<RegistryFriendlyByteBuf, T> elementCodec) {
        return elementCodec.apply(ByteBufCodecs.list());
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, List<T>> list(StreamCodec<RegistryFriendlyByteBuf, T> elementCodec, int maxSize) {
        return elementCodec.apply(ByteBufCodecs.list(maxSize));
    }

    // Utility codecs
    public static StreamCodec<RegistryFriendlyByteBuf, ItemStack> itemStackWithCount() {
        return StreamCodec.composite(
                itemStack(), Function.identity(),
                integer(), ItemStack::getCount,
                (stack, count) -> {
                    ItemStack copy = stack.copy();
                    copy.setCount(count);
                    return copy;
                }
        );
    }

    public static StreamCodec<RegistryFriendlyByteBuf, ItemStack> itemStackNoNBT() {
        return StreamCodec.composite(
                item(), ItemStack::getItem,
                integer(), ItemStack::getCount,
                ItemStack::new
        );
    }

    // Entity position codec
    public static StreamCodec<ByteBuf, EntityPosition> entityPosition() {
        return StreamCodec.composite(
                vec3(), EntityPosition::pos,
                floatValue(), EntityPosition::yaw,
                floatValue(), EntityPosition::pitch,
                EntityPosition::new
        );
    }

    // Player data codec (just UUID and name)
    public static StreamCodec<ByteBuf, PlayerData> playerData() {
        return StreamCodec.composite(
                uuid(), PlayerData::uuid,
                string(), PlayerData::name,
                PlayerData::new
        );
    }

    // Dimension codec
    public static StreamCodec<ByteBuf, ResourceKey<Level>> dimension() {
        return net.minecraft.resources.ResourceKey.streamCodec(net.minecraft.core.registries.Registries.DIMENSION);
    }

    // Helper records for complex data
    public record EntityPosition(Vec3 pos, float yaw, float pitch) {}
    public record PlayerData(UUID uuid, String name) {}
}