package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.curios.BaguetteCuriosData;
import com.leclowndu93150.baguettelib.curios.CurioSlotData;
import com.leclowndu93150.baguettelib.player.fabric.PlayerActivityFabricHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class BaguettelibFabric implements ModInitializer {

    public static final DataComponentType<CurioSlotData> CURIO_SLOT_DATA =
            Registry.register(
                    BuiltInRegistries.DATA_COMPONENT_TYPE,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, "curio_slot_data"),
                    DataComponentType.<CurioSlotData>builder()
                            .persistent(CurioSlotData.CODEC)
                            .networkSynchronized(CurioSlotData.STREAM_CODEC)
                            .build()
            );

    @Override
    public void onInitialize() {
        BaguetteCuriosData.registerSupplier(() -> CURIO_SLOT_DATA);
        PlayerActivityFabricHandler.register();
        CommonClass.init();
    }
}
