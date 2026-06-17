package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.curios.BaguetteCuriosData;
import com.leclowndu93150.baguettelib.curios.CurioSlotData;
import com.leclowndu93150.baguettelib.player.neoforge.PlayerActivityNeoForgeHandler;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Constants.MOD_ID)
public class BaguettelibNeoForge {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CurioSlotData>> CURIO_SLOT_DATA =
            DATA_COMPONENTS.register("curio_slot_data", () ->
                    DataComponentType.<CurioSlotData>builder()
                            .persistent(CurioSlotData.CODEC)
                            .networkSynchronized(CurioSlotData.STREAM_CODEC)
                            .build());

    public BaguettelibNeoForge(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
        BaguetteCuriosData.registerSupplier(CURIO_SLOT_DATA);
        PlayerActivityNeoForgeHandler.register(NeoForge.EVENT_BUS);
        CommonClass.init();
    }
}
