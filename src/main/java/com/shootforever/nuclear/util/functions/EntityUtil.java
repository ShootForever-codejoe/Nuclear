package com.shootforever.nuclear.util.functions;

import com.shootforever.nuclear.Nuclear;
import com.shootforever.nuclear.module.modules.misc.Team;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class EntityUtil {
    private static final Minecraft mc = Nuclear.mc;

    private EntityUtil() {
        throw new AssertionError();
    }

    public static boolean isSelected(
            @NotNull Entity entity, boolean targetPlayer, boolean targetMobs, boolean targetAnimals, boolean targetDead, boolean targetInvisible, boolean canAttackCheck
    ) {
        if (entity instanceof LivingEntity && (targetDead || entity.isAlive()) && !entity.equals(Nuclear.mc.player) && (targetInvisible || !entity.isInvisible())) {
            if (targetPlayer && entity instanceof Player entityPlayer) {
                if (!canAttackCheck) {
                    return true;
                } else if (entityPlayer.isSpectator()) {
                    return false;
                } else {
                    Team team = (Team) Nuclear.getInstance().getModuleManager().getModule("Team");
                    //IRC irc = (IRC) Nuclear.getInstance().getModuleManager().getModule("IRC");
                    if (team == null /*|| irc == null*/) return true;
                    return (!team.isEnabled() || !team.isSameTeam(entityPlayer))
                            /*&& (!irc.isEnabled() || !Objects.requireNonNull(irc.getUsers()).contains(entityPlayer.getScoreboardName()))*/;
                }
            } else {
                return targetMobs && isMob(entity) || targetAnimals && isAnimal(entity);
            }
        } else {
            return false;
        }
    }

    public static boolean isAnimal(@NotNull Entity entity) {
        return entity instanceof Animal || entity instanceof Squid || entity instanceof IronGolem || entity instanceof Bat;
    }

    public static boolean isMob(@NotNull Entity entity) {
        return entity instanceof Mob
                || entity instanceof Villager
                || entity instanceof Slime
                || entity instanceof Ghast
                || entity instanceof EnderDragon
                || entity instanceof Shulker;
    }

    public static int getPing(@NotNull Player entityPlayer) {
        ClientPacketListener connection = Nuclear.mc.getConnection();
        if (connection == null) {
            return 0;
        } else {
            PlayerInfo networkPlayerInfo = connection.getPlayerInfo(entityPlayer.getUUID());
            return networkPlayerInfo == null ? 0 : networkPlayerInfo.getLatency();
        }
    }

    public static boolean isCloseToEdge(@NotNull Entity entity, double distance) {
        if (mc.level == null || !entity.isOnGround() || distance > 0.8d) return false;

        Vec3 pos = entity.position();
        BlockPos blockPos = entity.getOnPos();
        if (mc.level.isEmptyBlock(blockPos)) {
            boolean flag = false;
            if (pos.x - blockPos.getX() <= 0.3d) {
                if (!mc.level.isEmptyBlock(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ()))) {
                    blockPos = new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ());
                } else if (pos.z - blockPos.getZ() <= 0.3d && !mc.level.isEmptyBlock(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ() - 1))) {
                    blockPos = new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ() - 1);
                } else if (pos.z - blockPos.getZ() >= 0.7d && !mc.level.isEmptyBlock(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ() + 1))) {
                    blockPos = new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ() + 1);
                } else {
                    flag = true;
                }
            } else if (pos.x - blockPos.getX() >= 0.7d) {
                if (!mc.level.isEmptyBlock(new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ()))) {
                    blockPos = new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ());
                } else if (pos.z - blockPos.getZ() <= 0.3d && !mc.level.isEmptyBlock(new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ() - 1))) {
                    blockPos = new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ() - 1);
                } else if (pos.z - blockPos.getZ() >= 0.7d && !mc.level.isEmptyBlock(new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ() + 1))) {
                    blockPos = new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ() + 1);
                } else {
                    flag = true;
                }
            } else {
                flag = true;
            }
            if (flag) {
                if (pos.z - blockPos.getZ() <= 0.3d && !mc.level.isEmptyBlock(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1))) {
                    blockPos = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1);
                } else if (pos.z - blockPos.getZ() >= 0.7d && !mc.level.isEmptyBlock(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1))) {
                    blockPos = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1);
                }
            }
        }

        double xDistanceToEdge = pos.x - blockPos.getX() + 0.3d;
        BlockPos xNextBlockPos;
        if (xDistanceToEdge <= 0.8d) {
            xNextBlockPos = new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ());
            if (xDistanceToEdge <= distance && mc.level.isEmptyBlock(xNextBlockPos)) return true;
        } else {
            xNextBlockPos = new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ());
            if (1.6d - xDistanceToEdge <= distance && mc.level.isEmptyBlock(xNextBlockPos)) return true;
        }

        double zDistanceToEdge = pos.z - blockPos.getZ() + 0.3d;
        BlockPos zNextBlockPos;
        if (zDistanceToEdge <= 0.8d) {
            zNextBlockPos = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1);
            return zDistanceToEdge <= distance && mc.level.isEmptyBlock(zNextBlockPos);
        } else {
            zNextBlockPos = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1);
            return 1.6d - zDistanceToEdge <= distance && mc.level.isEmptyBlock(zNextBlockPos);
        }
    }
}
