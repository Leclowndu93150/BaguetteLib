package com.leclowndu93150.baguettelib.curios;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class BaguetteCuriosData {

    public static final String KEY = "baguettelib:curio_slot_data";

    public static void setSlotData(ItemStack stack, String slotType, int slotIndex, boolean wasEquipped, boolean isCosmetic) {
        Tag encoded = CurioSlotData.CODEC.encodeStart(NbtOps.INSTANCE, new CurioSlotData(slotType, slotIndex, wasEquipped, isCosmetic)).getOrThrow();
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.put(KEY, encoded));
    }

    public static CurioSlotData getSlotData(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return null;
        }
        Tag tag = customData.copyTag().get(KEY);
        if (tag == null) {
            return null;
        }
        return CurioSlotData.CODEC.parse(NbtOps.INSTANCE, tag).result().orElse(null);
    }

    public static boolean hasSlotData(ItemStack stack) {
        return getSlotData(stack) != null;
    }

    public static void removeSlotData(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null || !customData.copyTag().contains(KEY)) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(KEY));
    }

    private BaguetteCuriosData() {}
}
