package com.bukkit.gemo.FalseBook.IC.Listeners;

import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class FalseBookICPlayerListener
        implements Listener {

    private ICFactory factory = null;

    public void init(ICFactory factory) {
        this.factory = factory;
    }

    public void ExecuteHiddenSwitch(org.bukkit.block.Block block, Player player, BlockFace face) {
        Sign signBlock = (Sign) block.getState();

        int dir = SignUtils.getDirection(signBlock);
        org.bukkit.block.Block[] neighbours = new org.bukkit.block.Block[8];
        neighbours[0] = block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ());
        neighbours[1] = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());
        if ((dir == 2) || (dir == 4)) {
            neighbours[2] = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() + 1);
            neighbours[3] = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() - 1);
            neighbours[4] = block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ() + 1);
            neighbours[5] = block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ() - 1);
            neighbours[6] = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ() + 1);
            neighbours[7] = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ() - 1);
        } else if ((dir == 1) || (dir == 3)) {
            neighbours[2] = block.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ());
            neighbours[3] = block.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ());
            neighbours[4] = block.getWorld().getBlockAt(block.getX() + 1, block.getY() + 1, block.getZ());
            neighbours[5] = block.getWorld().getBlockAt(block.getX() - 1, block.getY() + 1, block.getZ());
            neighbours[6] = block.getWorld().getBlockAt(block.getX() + 1, block.getY() - 1, block.getZ());
            neighbours[7] = block.getWorld().getBlockAt(block.getX() - 1, block.getY() - 1, block.getZ());
        }

        if ((dir >= 1) && (dir <= 4)) {
            for (int i = 0; i < 8; i++) {
                if (neighbours[i].getType().equals(Material.LEVER)) {
                    int data = neighbours[i].getData();
                    if ((data & 0x8) != 8) {
                        data |= 8;
                    } else if ((data & 0x8) == 8) {
                        data ^= 8;
                    }
                    neighbours[i].setTypeIdAndData(Material.LEVER.getId(), (byte) data, true);
                } else {
                    if (!neighbours[i].getType().equals(Material.STONE_BUTTON)) {
                        continue;
                    }
                    CraftPlayer thisPlayer = (CraftPlayer) player;
                    CraftWorld cWorld = (CraftWorld) block.getWorld();
                    net.minecraft.server.Block.byId[Material.STONE_BUTTON.getId()].interact(cWorld.getHandle(), neighbours[i].getX(), neighbours[i].getY(), neighbours[i].getZ(), thisPlayer.getHandle());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            this.factory.handleLeftClick(event);
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getPlayer() == null) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }

        if ((event.getPlayer().getItemInHand().getType().equals(Material.COAL))
                && (event.getClickedBlock().getType().equals(Material.REDSTONE_WIRE))) {
            byte rData = event.getClickedBlock().getData();

            String txt = ChatColor.YELLOW + "Ammeter: [";
            for (int i = 0; i < 15; i++) {
                if (i >= rData) {
                    txt = txt + ChatColor.DARK_GRAY;
                }
                txt = txt + ":";
            }
            txt = txt + ChatColor.YELLOW + "]";
            txt = txt + ChatColor.WHITE + " " + rData + " A";

            ChatUtils.printLine(event.getPlayer(), ChatColor.YELLOW, txt);
        }


        BlockFace[] arrayOfBlockFace;
        int i = (arrayOfBlockFace = BlockFace.values()).length;
        for (int txt = 0; txt < i; txt++) {
            BlockFace face = arrayOfBlockFace[txt];
            if ((face.equals(BlockFace.SELF)) || (face.equals(BlockFace.DOWN)) || (face.equals(BlockFace.UP)) || (face.toString().contains("_"))) {
                continue;
            }
            if (event.getClickedBlock().getRelative(face).getType().equals(Material.WALL_SIGN)) {
                Player player = event.getPlayer();
                if ((((Sign) event.getClickedBlock().getRelative(face).getState()).getLine(1).equalsIgnoreCase("[x]")) && ((UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.hiddenswitch")) || (UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.*")) || (UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.hiddenswitch.create")) || (UtilPermissions.playerCanUseCommand(player, "*")))) {
                    ExecuteHiddenSwitch(event.getClickedBlock().getRelative(face), event.getPlayer(), event.getBlockFace());
                }
            }

        }

        this.factory.handleRightClick(event);
    }
}