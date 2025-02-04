package com.shootforever.nuclear.module.modules.player;

import com.shootforever.nuclear.event.Event;
import com.shootforever.nuclear.event.EventTarget;
import com.shootforever.nuclear.event.events.GameTickEvent;
import com.shootforever.nuclear.module.Category;
import com.shootforever.nuclear.module.Module;
import com.shootforever.nuclear.util.Timer;
import com.shootforever.nuclear.util.InventoryUtil;
import com.shootforever.nuclear.util.MathUtil;
import com.shootforever.nuclear.value.values.BooleanValue;
import com.shootforever.nuclear.value.values.NumberValue;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

public class ChestStealer extends Module {
    private final BooleanValue noDelay = new BooleanValue(this, "NoDelay", false);
    private final BooleanValue silent = new BooleanValue(this, "Silent", false);
    private final NumberValue delay = new NumberValue(this, "StealDelay", 100, 0, 1000, 10);
    private final BooleanValue trash = new BooleanValue(this, "PickTrash", false);

    private final Timer stopwatch2 = new Timer();
    private final Timer stopwatch = new Timer();
    private long nextClick;
    private int lastClick;
    private int lastSteal;

    public ChestStealer() {
        super("ChestStealer", Category.PLAYER);
    }

    @EventTarget
    public void onTick(GameTickEvent event) {
        if (event.getSide() == Event.Side.PRE) {
            if (mc.player == null || mc.gameMode == null || !this.stopwatch.hasTimeElapsed(this.nextClick)) return;

            if (mc.player.containerMenu instanceof ChestMenu container) {
                this.lastSteal++;
                if (this.isChestEmpty(container) && this.stopwatch2.hasTimeElapsed(100L)) {
                    mc.player.closeContainer();
                    return;
                }

                for (int i = 0; i < container.getContainer().getContainerSize(); i++) {
                    if (!container.getContainer().getItem(i).isEmpty() && this.lastSteal > 1 && (this.isItemUseful(container, i) || this.trash.getValue())) {
                        this.nextClick = Math.round(
                                MathUtil.getRandomFloat((float) this.delay.getValue().intValue(), (float) (this.delay.getValue().intValue() + 5))
                        );
                        mc.gameMode.handleInventoryMouseClick(container.containerId, i, 0, ClickType.QUICK_MOVE, mc.player);
                        this.stopwatch.reset();
                        this.stopwatch2.reset();
                        this.lastClick = 0;
                        if (this.nextClick > 0L) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean isChestEmpty(@NotNull ChestMenu c) {
        for (int i = 0; i < c.getContainer().getMaxStackSize(); i++) {
            if (!c.getContainer().getItem(i).isEmpty() && (this.isItemUseful(c, i) || this.trash.getValue())) {
                return false;
            }
        }

        return true;
    }

    private boolean isItemUseful(@NotNull ChestMenu c, int i) {
        ItemStack itemStack = c.getSlot(i).getItem();
        Item item = itemStack.getItem();
        if (item instanceof AxeItem || item instanceof PickaxeItem) {
            return true;
        } else if (itemStack.getItem().getFoodProperties() != null) {
            return true;
        } else if (item instanceof BowItem || item == Items.ARROW) {
            return true;
        } else if (item instanceof PotionItem) {
            return true;
        } else if (item instanceof SwordItem && InventoryUtil.isBestSword(c, itemStack)) {
            return true;
        } else if (item instanceof ArmorItem && InventoryUtil.isBestArmor(c, itemStack)) {
            return true;
        } else if (item instanceof BlockItem) {
            return true;
        } else if (item == Items.SLIME_BALL) {
            return true;
        } else if (item instanceof CrossbowItem) {
            return true;
        } else if (item == Items.WATER_BUCKET) {
            return true;
        } else if (item == Items.TOTEM_OF_UNDYING) {
            return true;
        } else {
            return item == Items.FIRE_CHARGE || item == Items.ENDER_PEARL;
        }
    }


    public BooleanValue getNoDelay() {
        return noDelay;
    }


    public BooleanValue getSilent() {
        return silent;
    }


    public Timer getStopwatch2() {
        return stopwatch2;
    }


    public Timer getStopwatch() {
        return stopwatch;
    }


    public long getNextClick() {
        return nextClick;
    }


    public int getLastClick() {
        return lastClick;
    }


    public int getLastSteal() {
        return lastSteal;
    }


    public NumberValue getDelay() {
        return delay;
    }

    public BooleanValue getTrash() {
        return trash;
    }
}
