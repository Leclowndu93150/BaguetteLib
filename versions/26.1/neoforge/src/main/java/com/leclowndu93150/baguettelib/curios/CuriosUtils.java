package com.leclowndu93150.baguettelib.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class CuriosUtils {

    public static Optional<ICuriosItemHandler> getCuriosInventory(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity);
    }

    public static boolean isEquipped(LivingEntity entity, Item item) {
        return getCuriosInventory(entity)
                .map(handler -> handler.isEquipped(item))
                .orElse(false);
    }

    public static boolean isEquipped(LivingEntity entity, Predicate<ItemStack> filter) {
        return getCuriosInventory(entity)
                .map(handler -> handler.isEquipped(filter))
                .orElse(false);
    }

    public static Optional<SlotResult> findFirstCurio(LivingEntity entity, Item item) {
        return getCuriosInventory(entity)
                .flatMap(handler -> handler.findFirstCurio(item));
    }

    public static Optional<SlotResult> findFirstCurio(LivingEntity entity, Predicate<ItemStack> filter) {
        return getCuriosInventory(entity)
                .flatMap(handler -> handler.findFirstCurio(filter));
    }

    public static List<SlotResult> findCurios(LivingEntity entity, Item item) {
        return getCuriosInventory(entity)
                .map(handler -> handler.findCurios(item))
                .orElse(List.of());
    }

    public static List<SlotResult> findCurios(LivingEntity entity, Predicate<ItemStack> filter) {
        return getCuriosInventory(entity)
                .map(handler -> handler.findCurios(filter))
                .orElse(List.of());
    }

    public static List<SlotResult> findCurios(LivingEntity entity, String... slotIdentifiers) {
        return getCuriosInventory(entity)
                .map(handler -> handler.findCurios(slotIdentifiers))
                .orElse(List.of());
    }

    public static Optional<SlotResult> getCurio(LivingEntity entity, String slotType, int index) {
        return getCuriosInventory(entity)
                .flatMap(handler -> handler.findCurio(slotType, index));
    }

    public static void setCurio(LivingEntity entity, String slotType, int index, ItemStack stack) {
        getCuriosInventory(entity)
                .ifPresent(handler -> handler.setEquippedCurio(slotType, index, stack));
    }

    public static void forEachEquipped(LivingEntity entity, BiConsumer<SlotContext, ItemStack> action) {
        getCuriosInventory(entity).ifPresent(handler -> {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                String slotType = entry.getKey();
                ICurioStacksHandler stacksHandler = entry.getValue();
                IDynamicStackHandler stacks = stacksHandler.getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        SlotContext ctx = new SlotContext(slotType, entity, i, false, true);
                        action.accept(ctx, stack);
                    }
                }
            }
        });
    }

    public static void forEachCosmetic(LivingEntity entity, BiConsumer<SlotContext, ItemStack> action) {
        getCuriosInventory(entity).ifPresent(handler -> {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                String slotType = entry.getKey();
                ICurioStacksHandler stacksHandler = entry.getValue();
                if (!stacksHandler.hasCosmetic()) continue;
                IDynamicStackHandler cosmetics = stacksHandler.getCosmeticStacks();

                for (int i = 0; i < cosmetics.getSlots(); i++) {
                    ItemStack stack = cosmetics.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        SlotContext ctx = new SlotContext(slotType, entity, i, true, true);
                        action.accept(ctx, stack);
                    }
                }
            }
        });
    }

    public static List<ItemStack> getAllEquippedStacks(LivingEntity entity) {
        List<ItemStack> result = new ArrayList<>();
        forEachEquipped(entity, (ctx, stack) -> result.add(stack));
        return result;
    }

    public static int getSlotCount(LivingEntity entity, String slotType) {
        return getCuriosInventory(entity)
                .flatMap(handler -> handler.getStacksHandler(slotType))
                .map(ICurioStacksHandler::getSlots)
                .orElse(0);
    }

    public static int getTotalSlotCount(LivingEntity entity) {
        return getCuriosInventory(entity)
                .map(ICuriosItemHandler::getSlots)
                .orElse(0);
    }

    public static boolean tryEquipItem(LivingEntity entity, String slotType, ItemStack stack) {
        return getCuriosInventory(entity).map(handler -> {
            Optional<ICurioStacksHandler> opt = handler.getStacksHandler(slotType);
            if (opt.isEmpty()) return false;
            IDynamicStackHandler stacks = opt.get().getStacks();

            for (int i = 0; i < stacks.getSlots(); i++) {
                if (stacks.getStackInSlot(i).isEmpty()) {
                    if (CuriosApi.isStackValid(new SlotContext(slotType, entity, i, false, true), stack)) {
                        stacks.setStackInSlot(i, stack);
                        return true;
                    }
                }
            }
            return false;
        }).orElse(false);
    }

    public static boolean tryEquipItemAnywhere(LivingEntity entity, ItemStack stack) {
        return getCuriosInventory(entity).map(handler -> {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                String slotType = entry.getKey();
                IDynamicStackHandler stacks = entry.getValue().getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    if (stacks.getStackInSlot(i).isEmpty()) {
                        if (CuriosApi.isStackValid(new SlotContext(slotType, entity, i, false, true), stack)) {
                            stacks.setStackInSlot(i, stack);
                            return true;
                        }
                    }
                }
            }
            return false;
        }).orElse(false);
    }

    public static ItemStack extractItem(LivingEntity entity, String slotType, int index) {
        return getCuriosInventory(entity).map(handler -> {
            Optional<ICurioStacksHandler> opt = handler.getStacksHandler(slotType);
            if (opt.isEmpty()) return ItemStack.EMPTY;
            IDynamicStackHandler stacks = opt.get().getStacks();
            if (index < 0 || index >= stacks.getSlots()) return ItemStack.EMPTY;

            ItemStack existing = stacks.getStackInSlot(index);
            if (existing.isEmpty()) return ItemStack.EMPTY;

            stacks.setStackInSlot(index, ItemStack.EMPTY);
            return existing;
        }).orElse(ItemStack.EMPTY);
    }

    public static void clearAllCurios(LivingEntity entity) {
        getCuriosInventory(entity).ifPresent(handler -> {
            for (ICurioStacksHandler stacksHandler : handler.getCurios().values()) {
                IDynamicStackHandler stacks = stacksHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    stacks.setStackInSlot(i, ItemStack.EMPTY);
                }
                if (stacksHandler.hasCosmetic()) {
                    IDynamicStackHandler cosmetics = stacksHandler.getCosmeticStacks();
                    for (int i = 0; i < cosmetics.getSlots(); i++) {
                        cosmetics.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
        });
    }

    public static void dropAllCurios(Player player) {
        getCuriosInventory(player).ifPresent(handler -> {
            for (ICurioStacksHandler stacksHandler : handler.getCurios().values()) {
                IDynamicStackHandler stacks = stacksHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        player.drop(stack, true, false);
                        stacks.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
        });
    }

    private CuriosUtils() {}
}
