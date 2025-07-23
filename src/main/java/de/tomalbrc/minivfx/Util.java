package de.tomalbrc.dropvfx;

import com.mojang.serialization.JsonOps;
import de.tomalbrc.dropvfx.config.ColorEntry;
import de.tomalbrc.dropvfx.config.ColorRGB;
import de.tomalbrc.dropvfx.config.DefaultColors;
import de.tomalbrc.dropvfx.config.ModConfig;
import de.tomalbrc.dropvfx.impl.ItemColors;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Math;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Util {
    private static final double DISTANCE = 64;

    public static void loadColors(MinecraftServer server) {
        var ops = RegistryOps.create(JsonOps.INSTANCE, server.registryAccess());
        if (ModConfig.getInstance().itemColors != null) {
            ColorEntry.CODEC.listOf().decode(ops, ModConfig.getInstance().itemColors).ifSuccess(mapJsonElementPair -> {
                var map = mapJsonElementPair.getFirst();
                for (ColorEntry entry : map) {
                    for (Holder<Item> itemHolder : entry.items()) {
                        ItemColors.put(itemHolder.value(), entry.color().rgb());
                    }
                }
            });
        } else {
            DefaultColors.blockColors();
            DefaultColors.toolColors();
            DefaultColors.armorColors();
            DefaultColors.specialColors();

            ColorEntry.CODEC.listOf().encodeStart(ops, colorEntries()).ifSuccess(x -> {
                ModConfig.getInstance().itemColors = x;
            });

            ModConfig.save();
        }
    }

    public static List<ColorEntry> colorEntries() {
        Map<Integer, Set<Item>> reverse = new Int2ObjectOpenHashMap<>();

        for (Map.Entry<Item, Integer> entry : ItemColors.colormap.entrySet()) {
            Item item = entry.getKey();
            int color = entry.getValue();
            reverse.computeIfAbsent(color, k -> new ObjectArraySet<>()).add(item);
        }

        List<ColorEntry> list = new ObjectArrayList<>();
        for (Map.Entry<Integer, Set<Item>> entry : reverse.entrySet()) {
            var entries = entry.getValue().stream().map(Item::builtInRegistryHolder).toList();
            var set = HolderSet.direct(entries);
            list.add(new ColorEntry(set, new ColorRGB(entry.getKey())));
        }

        return list;
    }

    public static void broadcast(ServerLevel serverLevel, Vec3 pos, Packet<?> packet) {
        broadcast(serverLevel, pos, packet, DISTANCE*DISTANCE);
    }

    public static void broadcast(ServerLevel serverLevel, Vec3 pos, Packet<?> packet, double div) {
        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(pos) < div) {
                player.connection.send(packet);
            }
        }
    }

    public static Packet<?> shapedItemParticles(VoxelShape voxelShape, ItemStack stack, Vec3 pos) {
        return shapedItemParticles(voxelShape, stack, stack, pos, 0.25);
    }

    public static Packet<?> shapedItemParticles(VoxelShape voxelShape, ItemStack stack, Vec3 pos, double div) {
        return shapedItemParticles(voxelShape, stack, stack, pos, div);
    }

    public static Packet<?> shapedItemParticles(VoxelShape voxelShape, ItemStack stack, ItemStack stack2, Vec3 pos) {
        return shapedItemParticles(voxelShape, stack, stack2, pos, 0.25);
    }

    public static Packet<?> shapedItemParticles(VoxelShape voxelShape, ItemStack stack, ItemStack stack2, Vec3 pos, double div) {
        List<Packet<? super ClientGamePacketListener>> packets = new ObjectArrayList<>();
        Vec3 finalPos = pos.subtract(voxelShape.bounds().getXsize() / 2f, voxelShape.bounds().getYsize() / 2f, voxelShape.bounds().getZsize() / 2f);
        voxelShape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double dx = org.joml.Math.min(1.0, maxX - minX);
            double dy = org.joml.Math.min(1.0, maxY - minY);
            double dz = org.joml.Math.min(1.0, maxZ - minZ);
            int nx = org.joml.Math.max(2, Mth.ceil(dx / div));
            int ny = org.joml.Math.max(2, Mth.ceil(dy / div));
            int nz = org.joml.Math.max(2, Mth.ceil(dz / div));
            for (int iX = 0; iX < nx; ++iX) {
                for (int iY = 0; iY < ny; ++iY) {
                    for (int iZ = 0; iZ < nz; ++iZ) {
                        double deltaX = (iX + 0.5) / nx;
                        double deltaY = (iY + 0.5) / ny;
                        double deltaZ = (iZ + 0.5) / nz;
                        double xOffset = deltaX * dx + minX;
                        double yOffset = deltaY * dy + minY;
                        double zOffset = deltaZ * dz + minZ;
                        packets.add(new ClientboundLevelParticlesPacket(new ItemParticleOption(ParticleTypes.ITEM, (org.joml.Math.ceil(Math.random()+0.5) == 1) ? stack : stack2), true, false, finalPos.x() + xOffset, finalPos.y() + yOffset, finalPos.z() + zOffset, (float) deltaX - 0.5f, (float) deltaY - 0.5f, (float) deltaZ - 0.5f, 0.25f, 0));
                    }
                }
            }
        });

        if (!packets.isEmpty()) {
            if (packets.size() > 1) {
                return new ClientboundBundlePacket(packets);
            } else return packets.getFirst();
        }

        return null;
    }
}
