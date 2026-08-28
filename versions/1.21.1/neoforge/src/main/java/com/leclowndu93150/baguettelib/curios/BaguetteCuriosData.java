package com.leclowndu93150.baguettelib.curios;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class BaguetteCuriosData {

    private static Supplier<DataComponentType<CurioSlotData>> curioSlotDataSupplier;

    public static void registerSupplier(Supplier<DataComponentType<CurioSlotData>> supplier) {
        curioSlotDataSupplier = supplier;
    }

    public static DataComponentType<CurioSlotData> getCurioSlotDataType() {
        if (curioSlotDataSupplier == null) {
            throw new IllegalStateException("CurioSlotData component not registered yet");
        }
        return curioSlotDataSupplier.get();
    }

    public static void setSlotData(ItemStack stack, String slotType, int slotIndex, boolean wasEquipped, boolean isCosmetic) {
        stack.set(getCurioSlotDataType(), new CurioSlotData(slotType, slotIndex, wasEquipped, isCosmetic));
    }

    public static CurioSlotData getSlotData(ItemStack stack) {
        return stack.get(getCurioSlotDataType());
    }

    public static boolean hasSlotData(ItemStack stack) {
        return stack.has(getCurioSlotDataType());
    }

    public static void removeSlotData(ItemStack stack) {
        stack.remove(getCurioSlotDataType());
    }

    private BaguetteCuriosData() {}
}
