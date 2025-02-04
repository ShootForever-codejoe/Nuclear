package com.shootforever.nuclear.module.modules.player;

import com.shootforever.nuclear.event.Event;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.GameTickEvent;
import com.shootforever.nuclear.event.events.PacketEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.Timer;
import com.shootforever.nuclear.util.BlockUtil;
import com.shootforever.nuclear.util.InventoryUtil;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.NumberValue;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvCleaner extends Module {
    private final BooleanValue swing = new BooleanValue(this, "Swing", true);
    private final BooleanValue offHand = new BooleanValue(this, "OffhandGapple", false);
    private final NumberValue delay = new NumberValue(this, "Delay", 5, 0, 300, 10);
    private final NumberValue armorDelay = new NumberValue(this, "ArmorDelay", 20, 0, 300, 10);
    public final NumberValue slotWeapon = new NumberValue(this, "WeaponSlot", 1, 1, 9, 1);
    public final NumberValue slotPick = new NumberValue(this, "PickaxeSlot", 2, 1, 9, 1);
    public final NumberValue slotAxe = new NumberValue(this, "AxeSlot", 3, 1, 9, 1);
    public final NumberValue slotGapple = new NumberValue(this, "GappleSlot", 4, 1, 9, 1);
    public final NumberValue slotWater = new NumberValue(this, "WaterSlot", 5, 1, 9, 1);
    public final NumberValue slotBow = new NumberValue(this, "BowSlot", 6, 1, 9, 1);
    public final NumberValue slotBlock = new NumberValue(this, "BlockSlot", 7, 1, 9, 1);
    public final NumberValue slotPearl = new NumberValue(this, "PearlSlot", 8, 1, 9, 1);

    private final int[] bestArmorPieces = new int[6];
    private final List<Integer> trash = new ArrayList<>();
    private final int[] bestToolSlots = new int[2];
    private final List<Integer> gappleStackSlots = new ArrayList<>();
    private int bestSwordSlot;
    private int bestPearlSlot;
    private int bestBowSlot;
    private int bestWaterSlot;
    private int ticksSinceLastClick;
    private boolean nextTickCloseInventory;
    private boolean serverOpen;
    private boolean clientOpen;
    private final Timer timer = new Timer();

    public InvCleaner() {
        super("InvCleaner", Category.PLAYER);
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
        if (mc.player == null) return;

        Packet<?> packet = event.getPacket();
        if (packet instanceof ClientboundOpenScreenPacket) {
            this.clientOpen = false;
            this.serverOpen = false;
        }

        if (packet instanceof ServerboundPlayerCommandPacket wrapper) {
            if (wrapper.getData() == mc.player.getId() && wrapper.getAction() == Action.OPEN_INVENTORY) {
                this.clientOpen = true;
                this.serverOpen = true;
            }
        } else if (packet instanceof ServerboundContainerClosePacket wrapperX) {
            if (wrapperX.getContainerId() == mc.player.inventoryMenu.containerId) {
                this.clientOpen = false;
                this.serverOpen = false;
            }
        } else if (packet instanceof ServerboundContainerClickPacket && !mc.player.isUsingItem()) {
            this.ticksSinceLastClick = 0;
        }
    }

    private boolean dropItem(@NotNull List<@NotNull Integer> listOfSlots) {
        if (mc.player != null && mc.gameMode != null && mc.getConnection() != null && !listOfSlots.isEmpty()) {
            int slot = listOfSlots.remove(0);
            mc.gameMode.handleInventoryMouseClick(mc.player.inventoryMenu.containerId, slot, 1, ClickType.THROW, mc.player);
            if (this.swing.getValue()) {
                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
            }

            return true;
        }
        return false;
    }

    @EventTarget
    private void onTick(GameTickEvent event) {
        if (mc.player == null) return;

        if (!mc.player.isSpectator()) {
            if (event.getSide() == Event.Side.PRE && !mc.player.isUsingItem()) {
                this.ticksSinceLastClick++;
                if ((double) this.ticksSinceLastClick < Math.floor(this.delay.getValue().doubleValue() / 50.0)) {
                    return;
                }

                if (mc.screen instanceof InventoryScreen) {
                    this.clear();

                    for (int slot = 5; slot < 45; slot++) {
                        ItemStack stack = mc.player.containerMenu.getSlot(slot).getItem();
                        AbstractContainerMenu handler = mc.player.containerMenu;
                        if (!stack.isEmpty()) {
                            if (stack.getItem() instanceof SwordItem && InventoryUtil.isBestSword(handler, stack)) {
                                this.bestSwordSlot = slot;
                            } else if (stack.getItem() instanceof DiggerItem && InventoryUtil.isBestTool(handler, stack)) {
                                int toolType = InventoryUtil.getToolType(stack);
                                if (toolType != -1 && slot != this.bestToolSlots[toolType]) {
                                    this.bestToolSlots[toolType] = slot;
                                }
                            } else {
                                Item armorSlot = stack.getItem();
                                if (armorSlot instanceof ArmorItem armor) {
                                    if (InventoryUtil.isBestArmor(handler, stack)) {
                                        EquipmentSlot armorSlotX = armor.getSlot();
                                        int index = armorSlotX.ordinal();
                                        if (index >= 1 && index < this.bestArmorPieces.length + 2) {
                                            int pieceSlot = this.bestArmorPieces[index];
                                            if (pieceSlot == -1 || slot != pieceSlot) {
                                                this.bestArmorPieces[index] = slot;
                                            }
                                        }
                                        continue;
                                    }
                                }

                                if (!(stack.getItem() instanceof BowItem) || !InventoryUtil.isBestBow(handler, stack)) {
                                    if (stack.getItem() == Items.GOLDEN_APPLE) {
                                        this.gappleStackSlots.add(slot);
                                    } else if (stack.getItem() == Items.ENDER_PEARL) {
                                        this.bestPearlSlot = slot;
                                    } else if (stack.getItem() == Items.WATER_BUCKET) {
                                        if (slot != this.bestWaterSlot) {
                                            this.bestWaterSlot = slot;
                                        }
                                    } else if (!this.trash.contains(slot) && !isValidStack(stack)) {
                                        this.trash.add(slot);
                                    }
                                } else if (slot != this.bestBowSlot) {
                                    this.bestBowSlot = slot;
                                }
                            }
                        }
                    }

                    boolean busy = !this.trash.isEmpty() || this.equipArmor(false) || this.sortItems(false);
                    if (!busy) {
                        if (this.nextTickCloseInventory) {
                            this.close();
                            this.nextTickCloseInventory = false;
                        } else {
                            this.nextTickCloseInventory = true;
                        }

                        return;
                    }

                    boolean waitUntilNextTick = !this.serverOpen;
                    this.open();
                    if (this.nextTickCloseInventory) {
                        this.nextTickCloseInventory = false;
                    }

                    if (waitUntilNextTick) {
                        return;
                    }

                    if (this.timer.hasTimeElapsed(this.armorDelay.getValue().longValue()) && this.equipArmor(true)) {
                        return;
                    }

                    if (this.dropItem(this.trash)) {
                        return;
                    }

                    this.sortItems(true);
                }
            }
        }
    }

    private boolean sortItems(boolean moveItems) {
        if (mc.player == null) return false;

        int goodSwordSlot = this.slotWeapon.getValue().intValue() + 35;
        if (this.bestSwordSlot != -1 && this.bestSwordSlot != goodSwordSlot) {
            if (moveItems) {
                this.putItemInSlot(goodSwordSlot, this.bestSwordSlot);
                this.bestSwordSlot = goodSwordSlot;
            }

            return true;
        } else {
            int goodBowSlot = this.slotBow.getValue().intValue() + 35;
            if (this.bestBowSlot != -1 && this.bestBowSlot != goodBowSlot) {
                if (moveItems) {
                    this.putItemInSlot(goodBowSlot, this.bestBowSlot);
                    this.bestBowSlot = goodBowSlot;
                }

                return true;
            } else {
                int goodWaterSlot = this.slotWater.getValue().intValue() + 35;
                if (this.bestWaterSlot != -1 && this.bestWaterSlot != goodWaterSlot) {
                    if (moveItems) {
                        this.putItemInSlot(goodWaterSlot, this.bestWaterSlot);
                        this.bestWaterSlot = goodWaterSlot;
                    }

                    return true;
                } else {
                    int goodGappleSlot = this.slotGapple.getValue().intValue() + 35;
                    if (this.offHand.getValue()) {
                        if (!this.gappleStackSlots.isEmpty()) {
                            this.gappleStackSlots.sort((slot1, slot2) -> {
                                int count1 = mc.player.containerMenu.getSlot(slot1).getItem().getCount();
                                int count2 = mc.player.containerMenu.getSlot(slot2).getItem().getCount();
                                return Integer.compare(count1, count2);
                            });
                            int bestGappleSlot = this.gappleStackSlots.get(0);
                            if (bestGappleSlot != 45) {
                                if (moveItems) {
                                    this.putItemInSlot(45, bestGappleSlot);
                                    this.gappleStackSlots.set(0, 45);
                                }

                                return true;
                            }
                        }
                    } else if (!this.gappleStackSlots.isEmpty()) {
                        this.gappleStackSlots.sort((slot1, slot2) -> {
                            int count1 = mc.player.containerMenu.getSlot(slot1).getItem().getCount();
                            int count2 = mc.player.containerMenu.getSlot(slot2).getItem().getCount();
                            return Integer.compare(count1, count2);
                        });
                        int bestGappleSlot = this.gappleStackSlots.get(0);
                        if (bestGappleSlot != goodGappleSlot) {
                            if (moveItems) {
                                this.putItemInSlot(goodGappleSlot, bestGappleSlot);
                                this.gappleStackSlots.set(0, goodGappleSlot);
                            }

                            return true;
                        }
                    }

                    int[] toolSlots = new int[]{this.slotPick.getValue().intValue() + 35, this.slotAxe.getValue().intValue() + 35};

                    for (int toolSlot : this.bestToolSlots) {
                        if (toolSlot != -1) {
                            int type = InventoryUtil.getToolType(mc.player.containerMenu.getSlot(toolSlot).getItem());
                            if (type != -1 && toolSlot != toolSlots[type]) {
                                if (moveItems) {
                                    this.putToolsInSlot(type, toolSlots);
                                }

                                return true;
                            }
                        }
                    }

                    int goodBlockSlot = this.slotBlock.getValue().intValue() + 35;
                    int mostBlocksSlot = this.getMostBlocks();
                    if (mostBlocksSlot != -1 && mostBlocksSlot != goodBlockSlot) {
                        Slot dss = mc.player.containerMenu.getSlot(goodBlockSlot);
                        ItemStack dssItem = dss.getItem();
                        if (dssItem.isEmpty()
                                || !(dssItem.getItem() instanceof BlockItem)
                                || dssItem.getCount() < mc.player.containerMenu.getSlot(mostBlocksSlot).getItem().getCount()) {
                            this.putItemInSlot(goodBlockSlot, mostBlocksSlot);
                        }
                    }

                    int goodPearlSlot = this.slotPearl.getValue().intValue() + 35;
                    if (this.bestPearlSlot != -1 && this.bestPearlSlot != goodPearlSlot) {
                        if (moveItems) {
                            this.putItemInSlot(goodPearlSlot, this.bestPearlSlot);
                            this.bestPearlSlot = goodPearlSlot;
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public int getMostBlocks() {
        if (mc.player == null) return 0;
        int stack = 0;
        int biggestSlot = -1;

        for (int i = 9; i < 45; i++) {
            Slot slot = mc.player.containerMenu.getSlot(i);
            ItemStack is = slot.getItem();
            if (!is.isEmpty() && is.getItem() instanceof BlockItem && is.getCount() > stack) {
                stack = is.getCount();
                biggestSlot = i;
            }
        }

        return biggestSlot;
    }

    private boolean equipArmor(boolean moveItems) {
        if (mc.player == null || mc.gameMode == null) return false;

        for (int i = 0; i < this.bestArmorPieces.length; i++) {
            int piece = this.bestArmorPieces[i];
            if (piece != -1) {
                int armorPieceSlot = this.getArmorSlot(EquipmentSlot.values()[i]);
                if (armorPieceSlot >= 0 && armorPieceSlot < mc.player.containerMenu.slots.size()) {
                    ItemStack stack = mc.player.containerMenu.getSlot(armorPieceSlot).getItem();
                    if (stack.isEmpty()) {
                        if (moveItems) {
                            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, piece, 0, ClickType.QUICK_MOVE, mc.player);
                        }

                        this.timer.reset();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Contract(pure = true)
    private int getArmorSlot(@NotNull EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> 5;
            case CHEST -> 6;
            case LEGS -> 7;
            case FEET -> 8;
            default -> -1;
        };
    }

    private void putItemInSlot(int slot, int slotIn) {
        if (mc.gameMode != null && mc.player != null) {
            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, slotIn, slot == 45 ? 40 : slot - 36, ClickType.SWAP, mc.player);
        }
    }

    private void putToolsInSlot(int tool, int @NotNull [] toolSlots) {
        int toolSlot = toolSlots[tool];
        if (mc.gameMode != null && mc.player != null) {
            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, this.bestToolSlots[tool], toolSlot - 36, ClickType.SWAP, mc.player);
        }
        this.bestToolSlots[tool] = toolSlot;
    }

    private static boolean isValidStack(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof BlockItem && BlockUtil.isValidBlock(((BlockItem) stack.getItem()).getBlock())) {
            return true;
        } else if (stack.getItem() instanceof PotionItem && InventoryUtil.isBuffPotion(stack)) {
            return true;
        } else if (stack.getItem().getFoodProperties() != null && InventoryUtil.isGoodFood(stack)) {
            return true;
        } else {
            return stack.getItem() == Items.TOTEM_OF_UNDYING || InventoryUtil.isGoodItem(stack);
        }
    }

    @Override
    protected void onEnable() {
        this.ticksSinceLastClick = 0;
        this.clientOpen = mc.screen instanceof InventoryScreen;
        this.serverOpen = this.clientOpen;
    }

    @Override
    protected void onDisable() {
        this.clear();
    }

    private void open() {
        if (mc.player != null && mc.getConnection() != null && !this.clientOpen && !this.serverOpen) {
            mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, Action.OPEN_INVENTORY));
            this.serverOpen = true;
        }
    }

    private void close() {
        if (mc.player != null && mc.getConnection() != null && !this.clientOpen && this.serverOpen) {
            mc.getConnection().send(new ServerboundContainerClosePacket(mc.player.inventoryMenu.containerId));
            this.serverOpen = false;
        }
    }

    private void clear() {
        this.trash.clear();
        this.bestBowSlot = -1;
        this.bestSwordSlot = -1;
        this.bestWaterSlot = -1;
        this.gappleStackSlots.clear();
        Arrays.fill(this.bestArmorPieces, -1);
        Arrays.fill(this.bestToolSlots, -1);
    }
}
