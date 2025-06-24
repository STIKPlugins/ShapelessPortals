package me.stokmenn.shapelessportals;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IgnitePortalListener implements Listener {
    private final JavaPlugin plugin;

    public IgnitePortalListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block fireBlock = event.getBlock();
        if (!Config.portalFrameMaterials.contains(fireBlock.getRelative(BlockFace.DOWN).getType())) return;

        World world = fireBlock.getWorld();
        if (world.getEnvironment() == World.Environment.THE_END) return;

        if (Config.onlyPlayerCanIgnite) {
            Player player = event.getPlayer();
            if (player == null) return;
            if (Config.isPermissionRequired && !player.hasPermission(Config.permission)) return;
        }

        Axis axis = getAssumedPortalAxis(world, fireBlock.getLocation());
        if (axis == null) return;

        Set<Block> portalBlocks = getValidPortalBlocks(fireBlock, axis);
        if (portalBlocks == null) {
            axis = axis == Axis.X ? Axis.Z : Axis.X;
            portalBlocks = getValidPortalBlocks(fireBlock, axis);
        }

        if (portalBlocks != null) {
            buildPortal(portalBlocks, axis, world, event.getIgnitingEntity(), fireBlock.getLocation());
        }
    }

    private Set<Block> getValidPortalBlocks(Block fireBlock, Axis axis) {
        Set<Block> visited = new HashSet<>();
        Deque<Block> queue = new ArrayDeque<>();

        int minX = fireBlock.getX(), maxX = minX;
        int minY = fireBlock.getY(), maxY = minY;
        int minZ = fireBlock.getZ(), maxZ = minZ;

        visited.add(fireBlock);
        queue.add(fireBlock);

        while (!queue.isEmpty()) {
            Block cur = queue.poll();

            List<Block> neighbors = new ArrayList<>(4);
            neighbors.add(cur.getRelative(BlockFace.UP));
            neighbors.add(cur.getRelative(BlockFace.DOWN));
            if (axis == Axis.Z) {
                neighbors.add(cur.getRelative(BlockFace.NORTH));
                neighbors.add(cur.getRelative(BlockFace.SOUTH));
            } else {
                neighbors.add(cur.getRelative(BlockFace.EAST));
                neighbors.add(cur.getRelative(BlockFace.WEST));
            }

            for (Block neighbor : neighbors) {
                if (visited.contains(neighbor)) continue;
                Boolean valid = isBlockValid(neighbor);
                if (valid == null) return null;
                if (!valid) continue;

                visited.add(neighbor);
                queue.add(neighbor);

                int x = neighbor.getX(), y = neighbor.getY(), z = neighbor.getZ();
                if (axis == Axis.Z) {
                    minZ = Math.min(minZ, z);
                    maxZ = Math.max(maxZ, z);
                } else {
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                }
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);

                int width = (axis == Axis.Z ? (maxZ - minZ) : (maxX - minX));
                int height = maxY - minY;
                if (width >= Config.maxWidth || height >= Config.maxHeight) return null;
            }
        }

        return (visited.size() < Config.minPortalSize) ? null : visited;
    }

    private void buildPortal(Set<Block> validPortalBlocks, Axis axis, World world, Entity entity, Location location) {
        Bukkit.getRegionScheduler().run(plugin, location, task -> {
            Orientable portalData = (Orientable) Material.NETHER_PORTAL.createBlockData();
            portalData.setAxis(axis);

            List<BlockState> blockStates = new ArrayList<>(validPortalBlocks.size());
            for (Block block : validPortalBlocks) {
                BlockState state = block.getState();
                state.setBlockData(portalData);
                blockStates.add(state);
            }

            PortalCreateEvent event = new PortalCreateEvent(blockStates, world, entity, PortalCreateEvent.CreateReason.FIRE);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                blockStates.forEach(state -> state.update(true));
            }
        });
    }

    private Boolean isBlockValid(Block block) {
        Material material = block.getType();
        if (!material.equals(Material.AIR) && !material.equals(Material.FIRE)) {
            return Config.portalFrameMaterials.contains(material) ? false : null;
        } else return true;
    }

    private Axis getAssumedPortalAxis(World world, Location location) {
        final int distance = Config.maxWidth + 1;

        if (isValidFrame(world, location, new Vector(0, 0, -1), distance)) {
            if (isValidFrame(world, location, new Vector(0, 0, 1), distance)) return Axis.Z;
        }

        if (isValidFrame(world, location, new Vector(1, 0, 0), distance)) {
            if (isValidFrame(world, location, new Vector(-1, 0, 0), distance)) return Axis.X;
        }

        return null;
    }

    private boolean isValidFrame(World world, Location location, Vector direction, int distance) {
        RayTraceResult r = world.rayTraceBlocks(location, direction, distance, FluidCollisionMode.ALWAYS);
        return r != null && r.getHitBlock() != null && Config.portalFrameMaterials.contains(r.getHitBlock().getType());
    }
}