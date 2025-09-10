package com.leclowndu93150.baguettelib.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.blaze3d.platform.NativeImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ScreenTextureBuilder {
    private final String modId;
    private final String name;
    private final List<SlotData> slots = new ArrayList<>();
    private static final Map<String, ResourceLocation> textureCache = new HashMap<>();

    private static final int DEFAULT_GUI_WIDTH = 176;
    private static final int DEFAULT_GUI_HEIGHT = 166;
    private static final int DEFAULT_SLOT_SIZE = 18;
    private static final int STANDARD_SLOT_SPACING = 18;
    private static final int GUI_TOP_AREA_HEIGHT = 84;
    private static final int PLAYER_INV_START_Y = 84;
    
    private ScreenTextureBuilder(String modId, String name) {
        this.modId = modId;
        this.name = name;
    }
    
    public static ScreenTextureBuilder create(String modId, String name) {
        return new ScreenTextureBuilder(modId, name);
    }
    
    public ScreenTextureBuilder addSlot(int x, int y) {
        return addSlot(x, y, null);
    }
    
    public ScreenTextureBuilder addSlot(int x, int y, ResourceLocation customTexture) {
        slots.add(new SlotData(x, y, customTexture));
        return this;
    }
    
    public ScreenTextureBuilder addSlotRect(int x, int y, int cols, int rows, int xSpacing, int ySpacing) {
        return addSlotRect(x, y, cols, rows, xSpacing, ySpacing, null);
    }
    
    public ScreenTextureBuilder addSlotRect(int x, int y, int cols, int rows, int xSpacing, int ySpacing, ResourceLocation customTexture) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int slotX = x + (col * xSpacing);
                int slotY = y + (row * ySpacing);
                addSlot(slotX, slotY, customTexture);
            }
        }
        return this;
    }
    
    // === COMMON LAYOUT HELPERS ===
    
    public ScreenTextureBuilder addFurnaceLayout() {
        return addSlot(56, 17)    // Input slot
              .addSlot(56, 53)    // Fuel slot
              .addSlot(116, 35);  // Output slot
    }
    
    public ScreenTextureBuilder addCraftingTableLayout() {
        return addSlotRect(30, 17, 3, 3, 18, 18)  // 3x3 crafting grid
              .addSlot(124, 35);                    // Result slot
    }
    
    public ScreenTextureBuilder addChestLayout(int rows) {
        return addSlotRect(8, 18, 9, Math.min(rows, 6), 18, 18);
    }
    
    public ScreenTextureBuilder addDoubleChestLayout() {
        return addChestLayout(6);
    }
    
    public ScreenTextureBuilder addShulkerBoxLayout() {
        return addSlotRect(8, 18, 9, 3, 18, 18);
    }
    
    public ScreenTextureBuilder addHopperLayout() {
        return addSlotRect(44, 20, 5, 1, 18, 18);
    }
    
    public ScreenTextureBuilder addDispenserLayout() {
        return addSlotRect(62, 17, 3, 3, 18, 18);
    }
    
    public ScreenTextureBuilder addBrewingStandLayout() {
        return addSlot(79, 17)     // Ingredient slot
              .addSlot(56, 47)     // Bottle slot 1
              .addSlot(79, 53)     // Bottle slot 2
              .addSlot(102, 47);   // Bottle slot 3
    }
    
    public ScreenTextureBuilder addEnchantmentTableLayout() {
        return addSlot(15, 47)     // Item slot
              .addSlot(35, 47);    // Lapis slot
    }
    
    public ScreenTextureBuilder addAnvilLayout() {
        return addSlot(27, 47)     // Input slot 1
              .addSlot(76, 47)     // Input slot 2
              .addSlot(134, 47);   // Result slot
    }
    
    public ScreenTextureBuilder addBeaconLayout() {
        return addSlot(79, 34);    // Payment slot
    }
    
    // === CENTERING HELPERS ===
    
    public ScreenTextureBuilder addCenteredSlot(int y) {
        int x = (DEFAULT_GUI_WIDTH - DEFAULT_SLOT_SIZE) / 2;
        return addSlot(x, y);
    }
    
    public ScreenTextureBuilder addCenteredSlotRect(int y, int cols, int rows) {
        return addCenteredSlotRect(y, cols, rows, STANDARD_SLOT_SPACING, STANDARD_SLOT_SPACING);
    }
    
    public ScreenTextureBuilder addCenteredSlotRect(int y, int cols, int rows, int xSpacing, int ySpacing) {
        int totalWidth = (cols * xSpacing) - (xSpacing - DEFAULT_SLOT_SIZE);
        int x = (DEFAULT_GUI_WIDTH - totalWidth) / 2;
        return addSlotRect(x, y, cols, rows, xSpacing, ySpacing);
    }
    
    public ScreenTextureBuilder addCenteredRow(int y, int count) {
        return addCenteredRow(y, count, STANDARD_SLOT_SPACING);
    }
    
    public ScreenTextureBuilder addCenteredRow(int y, int count, int spacing) {
        int totalWidth = (count * spacing) - (spacing - DEFAULT_SLOT_SIZE);
        int x = (DEFAULT_GUI_WIDTH - totalWidth) / 2;
        return addSlotRect(x, y, count, 1, spacing, spacing);
    }
    
    // === SPACING CALCULATION HELPERS ===
    
    public ScreenTextureBuilder addEvenlySpacedRow(int y, int count, int leftMargin, int rightMargin) {
        int availableWidth = DEFAULT_GUI_WIDTH - leftMargin - rightMargin - (count * DEFAULT_SLOT_SIZE);
        int spacing = count > 1 ? availableWidth / (count - 1) + DEFAULT_SLOT_SIZE : 0;
        return addSlotRect(leftMargin, y, count, 1, spacing, spacing);
    }
    
    public ScreenTextureBuilder addGridWithMargins(int topMargin, int leftMargin, int rightMargin, int cols, int rows) {
        int availableWidth = DEFAULT_GUI_WIDTH - leftMargin - rightMargin;
        int spacing = cols > 1 ? (availableWidth - (cols * DEFAULT_SLOT_SIZE)) / (cols - 1) + DEFAULT_SLOT_SIZE : DEFAULT_SLOT_SIZE;
        return addSlotRect(leftMargin, topMargin, cols, rows, spacing, STANDARD_SLOT_SPACING);
    }
    
    // === PATTERN HELPERS ===
    
    public ScreenTextureBuilder addCirclePattern(int centerX, int centerY, int radius, int slotCount) {
        for (int i = 0; i < slotCount; i++) {
            double angle = 2 * Math.PI * i / slotCount;
            int x = centerX + (int) (Math.cos(angle) * radius) - DEFAULT_SLOT_SIZE / 2;
            int y = centerY + (int) (Math.sin(angle) * radius) - DEFAULT_SLOT_SIZE / 2;
            addSlot(x, y);
        }
        return this;
    }
    
    public ScreenTextureBuilder addDiamondPattern(int centerX, int centerY, int size) {
        for (int i = 0; i <= size; i++) {
            // Top half
            if (i == 0) {
                addSlot(centerX - DEFAULT_SLOT_SIZE / 2, centerY - size * STANDARD_SLOT_SPACING / 2);
            } else {
                for (int j = -i; j <= i; j += i * 2) {
                    int x = centerX + j * STANDARD_SLOT_SPACING / 2 - DEFAULT_SLOT_SIZE / 2;
                    int y = centerY - (size - i) * STANDARD_SLOT_SPACING / 2;
                    addSlot(x, y);
                }
            }
            // Bottom half (mirror)
            if (i > 0 && i < size) {
                for (int j = -i; j <= i; j += i * 2) {
                    int x = centerX + j * STANDARD_SLOT_SPACING / 2 - DEFAULT_SLOT_SIZE / 2;
                    int y = centerY + (size - i) * STANDARD_SLOT_SPACING / 2;
                    addSlot(x, y);
                }
            }
        }
        return this;
    }
    
    public ScreenTextureBuilder addLShape(int x, int y, int width, int height) {
        // Horizontal part
        addSlotRect(x, y, width, 1, STANDARD_SLOT_SPACING, STANDARD_SLOT_SPACING);
        // Vertical part
        addSlotRect(x, y + STANDARD_SLOT_SPACING, 1, height - 1, STANDARD_SLOT_SPACING, STANDARD_SLOT_SPACING);
        return this;
    }
    
    // === VALIDATION HELPERS ===
    
    public ScreenTextureBuilder validateSlotBounds() {
        for (SlotData slot : slots) {
            if (slot.x < 0 || slot.y < 0 || 
                slot.x + DEFAULT_SLOT_SIZE > DEFAULT_GUI_WIDTH || 
                slot.y + DEFAULT_SLOT_SIZE > PLAYER_INV_START_Y) {
                throw new IllegalArgumentException("Slot at (" + slot.x + ", " + slot.y + ") is out of bounds");
            }
        }
        return this;
    }
    
    public ScreenTextureBuilder removeOverlappingSlots() {
        Set<String> positions = new HashSet<>();
        slots.removeIf(slot -> {
            String pos = slot.x + "," + slot.y;
            return !positions.add(pos);
        });
        return this;
    }
    
    // === UTILITY METHODS ===
    
    public ScreenTextureBuilder addCustomPattern(SlotPattern pattern) {
        pattern.apply(this);
        return this;
    }
    
    public ScreenTextureBuilder conditionalSlot(boolean condition, int x, int y) {
        if (condition) {
            addSlot(x, y);
        }
        return this;
    }
    
    public ScreenTextureBuilder multiSlot(int count, SlotPositionFunction positionFunction) {
        for (int i = 0; i < count; i++) {
            int[] pos = positionFunction.getPosition(i);
            addSlot(pos[0], pos[1]);
        }
        return this;
    }
    
    public int getSlotCount() {
        return slots.size();
    }
    
    public boolean hasSlotAt(int x, int y) {
        return slots.stream().anyMatch(slot -> slot.x == x && slot.y == y);
    }
    
    public ScreenTextureBuilder clearSlots() {
        slots.clear();
        return this;
    }
    
    // === CALCULATION UTILITIES ===
    
    public static int calculateOptimalColumns(int slotCount, int maxCols) {
        if (slotCount <= maxCols) return slotCount;
        
        for (int cols = maxCols; cols >= 1; cols--) {
            if (slotCount % cols == 0) return cols;
        }
        
        return Math.min(maxCols, (int) Math.ceil(Math.sqrt(slotCount)));
    }
    
    public static int calculateOptimalRows(int slotCount, int cols) {
        return (int) Math.ceil((double) slotCount / cols);
    }
    
    public static int calculateCenteredX(int cols) {
        int totalWidth = (cols * STANDARD_SLOT_SPACING) - (STANDARD_SLOT_SPACING - DEFAULT_SLOT_SIZE);
        return (DEFAULT_GUI_WIDTH - totalWidth) / 2;
    }
    
    public static int calculateTotalWidth(int cols) {
        return (cols * STANDARD_SLOT_SPACING) - (STANDARD_SLOT_SPACING - DEFAULT_SLOT_SIZE);
    }
    
    public static int calculateTotalHeight(int rows) {
        return (rows * STANDARD_SLOT_SPACING) - (STANDARD_SLOT_SPACING - DEFAULT_SLOT_SIZE);
    }
    
    // === FUNCTIONAL INTERFACES ===
    
    @FunctionalInterface
    public interface SlotPattern {
        void apply(ScreenTextureBuilder builder);
    }
    
    @FunctionalInterface
    public interface SlotPositionFunction {
        int[] getPosition(int index);
    }
    
    public ResourceLocation buildAndCache() {
        String cacheKey = modId + ":" + name + "_" + generateSlotHash();
        
        if (textureCache.containsKey(cacheKey)) {
            return textureCache.get(cacheKey);
        }
        
        if (!isClientReady()) {
            throw new IllegalStateException("Cannot build GUI textures before client is initialized. Call this method after the client has started.");
        }
        
        try {
            NativeImage compositeImage = createCompositeTexture();
            ResourceLocation textureId = ResourceLocation.fromNamespaceAndPath(modId, "gui/generated/" + name);
            
            DynamicTexture dynamicTexture = new DynamicTexture(compositeImage);
            Minecraft.getInstance().getTextureManager().register(textureId, dynamicTexture);
            
            textureCache.put(cacheKey, textureId);
            return textureId;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to build GUI texture: " + cacheKey + ". Make sure the base textures exist and client is ready.", e);
        }
    }
    
    private NativeImage createCompositeTexture() throws IOException {
        NativeImage baseImage = loadBaseTexture();
        NativeImage slotTexture = loadDefaultSlotTexture();
        
        for (SlotData slot : slots) {
            NativeImage currentSlotTexture = slot.customTexture != null ? 
                loadTexture(slot.customTexture) : slotTexture;
            
            blitImage(baseImage, currentSlotTexture, slot.x, slot.y);
        }
        
        return baseImage;
    }
    
    private NativeImage loadBaseTexture() throws IOException {
        ResourceLocation baseLocation = ResourceLocation.fromNamespaceAndPath("baguettelib", "textures/gui/empty_gui.png");
        try {
            return loadTexture(baseLocation);
        } catch (Exception e) {
            throw new IOException("Failed to load base GUI texture. Make sure 'assets/baguettelib/textures/gui/empty_gui.png' exists in your resources.", e);
        }
    }
    
    private NativeImage loadDefaultSlotTexture() throws IOException {
        ResourceLocation slotLocation = ResourceLocation.fromNamespaceAndPath("baguettelib", "textures/gui/slot.png");
        try {
            return loadTexture(slotLocation);
        } catch (Exception e) {
            throw new IOException("Failed to load slot texture. Make sure 'assets/baguettelib/textures/gui/slot.png' exists in your resources.", e);
        }
    }
    
    private NativeImage loadTexture(ResourceLocation location) throws IOException {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        try (InputStream stream = resourceManager.getResource(location).orElseThrow().open()) {
            return NativeImage.read(stream);
        }
    }
    
    private void blitImage(NativeImage dest, NativeImage src, int destX, int destY) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int destWidth = dest.getWidth();
        int destHeight = dest.getHeight();
        
        for (int x = 0; x < srcWidth && destX + x < destWidth; x++) {
            for (int y = 0; y < srcHeight && destY + y < destHeight; y++) {
                if (destX + x >= 0 && destY + y >= 0) {
                    int pixel = src.getPixelRGBA(x, y);
                    int alpha = (pixel >> 24) & 0xFF;
                    
                    if (alpha > 0) {
                        dest.setPixelRGBA(destX + x, destY + y, pixel);
                    }
                }
            }
        }
    }
    
    private String generateSlotHash() {
        StringBuilder sb = new StringBuilder();
        for (SlotData slot : slots) {
            sb.append(slot.x).append(",").append(slot.y).append(",")
              .append(slot.customTexture != null ? slot.customTexture.toString() : "default")
              .append(";");
        }
        return Integer.toHexString(sb.toString().hashCode());
    }
    
    private static boolean isClientReady() {
        try {
            Minecraft minecraft = Minecraft.getInstance();
            return minecraft != null && minecraft.getResourceManager() != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public ResourceLocation buildAndCacheWhenReady() {
        if (isClientReady()) {
            return buildAndCache();
        } else {
            return null;
        }
    }
    
    public boolean canBuild() {
        return isClientReady();
    }
    
    public static void clearCache() {
        textureCache.clear();
    }
    
    private static class SlotData {
        final int x, y;
        final ResourceLocation customTexture;
        
        SlotData(int x, int y, ResourceLocation customTexture) {
            this.x = x;
            this.y = y;
            this.customTexture = customTexture;
        }
    }
}