package dev.thatalex.sheep;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.bukkit.BukkitAdapter;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("sheep").setExecutor(new SheepCommand());
    }

    @Override
    public void onDisable() {

    }

    // dummy command to make sure that the command 
    public class SheepCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            // Variable init
            Player p = (Player) sender;
            World world = BukkitAdapter.adapt(Bukkit.getWorlds().get(0));
				
            // Get players location
            Location pos = p.getLocation();
            
            // Grab the schematic by digging through WorldEdit's schematics
            File file = new File(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit").getDataFolder().getAbsolutePath() + "/schematics/sheep3.schem");

            // Copy the schematic
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                Clipboard clipboard = reader.read();
                
                // Paste the schematic
                try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(pos.getBlockX(), pos.getBlockY(), (pos.getBlockZ() - 1)))
                            .copyEntities(true)
                            .build();
                    Operations.complete(operation);
                } catch (WorldEditException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Send the *anonymous* message
            p.sendMessage(ChatColor.RED + "Woosh!" + ChatColor.RESET + " Something appeared around you.. check it out ;)");

            return true;
        }
    }
}
