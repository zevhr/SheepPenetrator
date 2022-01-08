package xyz.awexxx.sheep;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.bukkit.BukkitAdapter;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("demi").setExecutor(new CommandDemi());
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
    }

    @Override
    public void onDisable() {

    }

    public class CommandDemi implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            sender.sendMessage("The Demi Plugin has successfully loaded. Developed by Awex :3");
            return true;
        }
    }

    public class PlayerInteract implements Listener {
        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent e) throws FileNotFoundException, IOException, WorldEditException  {
            Player p = e.getPlayer();
            Block clicked = e.getClickedBlock();
            World world = BukkitAdapter.adapt(Bukkit.getWorld("world"));

            if(e.getAction() == Action.RIGHT_CLICK_BLOCK && clicked.getType() == Material.STONE_BUTTON) {
				p.sendMessage(p.getDisplayName() + " has clicked a button.");
				
				Location pos = p.getLocation();
				System.out.println(p.getName() + " is at " + pos.getBlockZ() + " Z.");
				
				int new_z = pos.getBlockZ() - 15;
				p.sendMessage(String.valueOf(new_z));
				
				File file = new File(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit").getDataFolder().getAbsolutePath() + "/schematics/sheep.schem");
				ClipboardFormat format = ClipboardFormats.findByFile(file);
				try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
				    Clipboard clipboard = reader.read();
				    
					try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
					    Operation operation = new ClipboardHolder(clipboard)
					            .createPaste(editSession)
					            .to(BlockVector3.at(pos.getBlockX(), pos.getBlockY(), new_z))
					            // configure here
					            .build();
					    Operations.complete(operation);
					}
				}
			}
        }
    }
}
