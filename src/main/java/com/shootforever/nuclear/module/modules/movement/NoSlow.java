package com.shootforever.nuclear.module.modules.movement;

import com.shootforever.nuclear.event.Event;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.KeyboardInputEvent;
import com.shootforever.nuclear.event.events.PacketEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import com.shootforever.nuclear.event.events.GameTickEvent;

public class NoSlow extends Module {
    private Input old;
    private boolean shouldSlow;
    private boolean keyboardInputCancelled = false;

    public NoSlow() {
        super("NoSlow", Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        if (mc.player != null) {
            old = mc.player.input;
            if (!(mc.player.input instanceof KeyboardInput)) {
                mc.player.input = new KeyboardInput(mc.options);
            }
        }
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) {
            mc.player.input = old;
        }
    }

    private boolean isUsable(ItemStack itemStack) {
        if (itemStack != null && !itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            boolean isFood = item.getFoodProperties() != null;
            boolean isShield = item == Items.SHIELD;
            boolean isBow = item instanceof BowItem;
            boolean isCrossBow = item instanceof CrossbowItem;
            return isFood || isShield || isBow || isCrossBow;
        } else {
            return false;
        }
    }

    @EventTarget
    public void onTick(GameTickEvent event) {
        if (mc.player != null) return;

        if (event.getSide() != Event.Side.POST) {
            if (mc.player.isUsingItem() && isUsable(mc.player.getMainHandItem()) || isUsable(mc.player.getOffhandItem())) {
                send();
            }
        }

        if (!(mc.player.input instanceof KeyboardInput)) {
            old = mc.player.input;
            mc.player.input = new KeyboardInput(mc.options);
        }

        if (mc.player.isUsingItem()) {
            keyboardInputCancelled = !shouldSlow;
        } else {
            keyboardInputCancelled = false;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (mc.player == null || mc.level == null) return;

        Packet<?> packet = event.getPacket();
        if (event.getSide() == Event.Side.PRE) {
            Block block = null;
            HitResult hitResult = mc.hitResult;
            if (hitResult != null && hitResult.getType() == Type.BLOCK) {
                block = mc.level.getBlockState(((BlockHitResult)hitResult).getBlockPos()).getBlock();
            }

            if (isUsable(mc.player.getMainHandItem())
                    || isUsable(mc.player.getOffhandItem()) && mc.player.isUsingItem() && (block == null || block != Blocks.CHEST)) {
                if (packet instanceof ClientboundContainerSetContentPacket) {
                    event.setCancelled(true);
                    noCancel();
                    shouldSlow = false;
                }

                if (packet instanceof ClientboundContainerSetSlotPacket) {
                    event.setCancelled(true);
                }
            }
        }

        if (event.getSide() == Event.Side.POST && (isUsable(mc.player.getMainHandItem()) || isUsable(mc.player.getOffhandItem()))) {
            if (packet instanceof ServerboundUseItemPacket) {
                noCancel();
                shouldSlow = true;
                send();
            }

            if (packet instanceof ServerboundPlayerActionPacket serverboundPlayerActionPacket
                    && (serverboundPlayerActionPacket).getAction() == Action.RELEASE_USE_ITEM) {
                noCancel();
                shouldSlow = true;
            }
        }
    }

    private void send() {
        if (mc.player == null) return;

        if (mc.player.getUseItemRemainingTicks()
                % (!(mc.player.getMainHandItem().getItem() instanceof BowItem)
                    && !(mc.player.getOffhandItem().getItem() instanceof BowItem)
                    && !(mc.player.getMainHandItem().getItem() instanceof CrossbowItem)
                    && !(mc.player.getOffhandItem().getItem() instanceof CrossbowItem)
                    ? 6 : 8) == 0) {
            Int2ObjectMap<ItemStack> modifiedStacks = new Int2ObjectOpenHashMap<>();
            modifiedStacks.put(36, new ItemStack(Items.BARRIER));
            mc.player.connection.send(new ServerboundContainerClickPacket(0, 0, 36, 0, ClickType.SWAP, new ItemStack(Blocks.BARRIER), modifiedStacks));
        }
    }

    @EventTarget
    public void onKeyboardInput(KeyboardInputEvent event) {
        if (keyboardInputCancelled) {
            event.setCancelled(true);
        }
    }

    private void noCancel() {
        if (mc.player == null) return;

        if (!(mc.player.input instanceof KeyboardInput)) {
            old = mc.player.input;
            mc.player.input = new KeyboardInput(mc.options);
        }

        keyboardInputCancelled = false;
    }
}
