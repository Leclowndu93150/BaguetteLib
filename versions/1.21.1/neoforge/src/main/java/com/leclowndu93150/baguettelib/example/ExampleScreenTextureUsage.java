package com.leclowndu93150.baguettelib.example;

import com.leclowndu93150.baguettelib.gui.ScreenTextureBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ExampleScreenTextureUsage {
    
    private static final Map<String, ResourceLocation> TEST_TEXTURES = new HashMap<>();
    private static boolean initialized = false;
    
    private static final String[] TEXTURE_NAMES = {
        "furnace", "crafting", "chest_4", "brewing", "anvil", "hopper", "dispenser", "beacon",
        "centered_slot", "centered_grid", "centered_row", "even_spaced", "margin_grid",
        "circle", "diamond", "l_shape", "custom_zigzag", "dynamic_slots", "auto_layout"
    };
    
    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        SuggestionProvider<CommandSourceStack> textureNameSuggestions = (context, builder) -> 
            SharedSuggestionProvider.suggest(TEXTURE_NAMES, builder);
        
        dispatcher.register(Commands.literal("test_gui")
            .then(Commands.argument("name", StringArgumentType.word())
                .suggests(textureNameSuggestions)
                .executes(context -> {
                    if (!initialized) {
                        createAllTestTextures();
                        initialized = true;
                    }
                    String textureName = StringArgumentType.getString(context, "name");
                    ResourceLocation texture = TEST_TEXTURES.get(textureName);
                    if (texture != null) {
                        Minecraft.getInstance().setScreen(new TestGuiScreen(texture, textureName));
                        return 1;
                    }
                    return 0;
                })));
    }
    
    private static void createAllTestTextures() {
        try {
            TEST_TEXTURES.put("furnace", ScreenTextureBuilder.create("baguettelib", "test_furnace")
                    .addFurnaceLayout().buildAndCache());
            
            TEST_TEXTURES.put("crafting", ScreenTextureBuilder.create("baguettelib", "test_crafting")
                    .addCraftingTableLayout().buildAndCache());
            
            TEST_TEXTURES.put("chest_4", ScreenTextureBuilder.create("baguettelib", "test_chest_4")
                    .addChestLayout(4).buildAndCache());
            
            TEST_TEXTURES.put("brewing", ScreenTextureBuilder.create("baguettelib", "test_brewing")
                    .addBrewingStandLayout().buildAndCache());
            
            TEST_TEXTURES.put("anvil", ScreenTextureBuilder.create("baguettelib", "test_anvil")
                    .addAnvilLayout().buildAndCache());
            
            TEST_TEXTURES.put("hopper", ScreenTextureBuilder.create("baguettelib", "test_hopper")
                    .addHopperLayout().buildAndCache());
            
            TEST_TEXTURES.put("dispenser", ScreenTextureBuilder.create("baguettelib", "test_dispenser")
                    .addDispenserLayout().buildAndCache());
            
            TEST_TEXTURES.put("beacon", ScreenTextureBuilder.create("baguettelib", "test_beacon")
                    .addBeaconLayout().buildAndCache());
            
            TEST_TEXTURES.put("centered_slot", ScreenTextureBuilder.create("baguettelib", "test_centered_slot")
                    .addCenteredSlot(30).buildAndCache());
            
            TEST_TEXTURES.put("centered_grid", ScreenTextureBuilder.create("baguettelib", "test_centered_grid")
                    .addCenteredSlotRect(20, 5, 3).buildAndCache());
            
            TEST_TEXTURES.put("centered_row", ScreenTextureBuilder.create("baguettelib", "test_centered_row")
                    .addCenteredRow(30, 7).buildAndCache());
            
            TEST_TEXTURES.put("even_spaced", ScreenTextureBuilder.create("baguettelib", "test_even_spaced")
                    .addEvenlySpacedRow(30, 5, 10, 10).buildAndCache());
            
            TEST_TEXTURES.put("margin_grid", ScreenTextureBuilder.create("baguettelib", "test_margin_grid")
                    .addGridWithMargins(15, 8, 8, 9, 4).buildAndCache());
            
            TEST_TEXTURES.put("circle", ScreenTextureBuilder.create("baguettelib", "test_circle")
                    .addCirclePattern(88, 40, 30, 8).buildAndCache());
            
            TEST_TEXTURES.put("diamond", ScreenTextureBuilder.create("baguettelib", "test_diamond")
                    .addDiamondPattern(88, 40, 3).buildAndCache());
            
            TEST_TEXTURES.put("l_shape", ScreenTextureBuilder.create("baguettelib", "test_l_shape")
                    .addLShape(20, 20, 4, 3).buildAndCache());
            
            TEST_TEXTURES.put("custom_zigzag", ScreenTextureBuilder.create("baguettelib", "test_custom_zigzag")
                    .addCustomPattern(builder -> {
                        for (int i = 0; i < 6; i++) {
                            int x = 20 + (i * 20);
                            int y = 30 + ((i % 2) * 20);
                            builder.addSlot(x, y);
                        }
                    }).buildAndCache());
            
            TEST_TEXTURES.put("dynamic_slots", ScreenTextureBuilder.create("baguettelib", "test_dynamic_slots")
                    .multiSlot(8, index -> {
                        int x = 30 + (index % 4) * 25;
                        int y = 20 + (index / 4) * 25;
                        return new int[]{x, y};
                    }).buildAndCache());
            
            int totalSlots = 27;
            int maxCols = 9;
            int cols = ScreenTextureBuilder.calculateOptimalColumns(totalSlots, maxCols);
            int rows = ScreenTextureBuilder.calculateOptimalRows(totalSlots, cols);
            int centeredX = ScreenTextureBuilder.calculateCenteredX(cols);
            
            TEST_TEXTURES.put("auto_layout", ScreenTextureBuilder.create("baguettelib", "test_auto_layout")
                    .addSlotRect(centeredX, 18, cols, rows, 18, 18).buildAndCache());
                    
        } catch (Exception e) {
            System.err.println("Failed to create test textures: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static class TestGuiScreen extends Screen {
        private final ResourceLocation texture;
        
        protected TestGuiScreen(ResourceLocation texture, String name) {
            super(Component.literal("Test GUI: " + name));
            this.texture = texture;
        }
        
        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            super.render(graphics, mouseX, mouseY, partialTick);
            
            int x = (width - 176) / 2;
            int y = (height - 166) / 2;
            
            graphics.blit(texture, x, y, 0, 0, 176, 166);
        }
        
        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }
}