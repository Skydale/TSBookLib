package io.github.mg138.tsbook.players;

import dev.reactant.reactant.core.component.Component;
import dev.reactant.reactant.core.component.lifecycle.LifeCycleHook;
import dev.reactant.reactant.core.dependency.injection.Inject;
import dev.reactant.reactant.service.spec.server.SchedulerService;
import io.github.mg138.tsbook.Book;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import tech.clayclaw.arcticglobal.javaadapter.PlayerDataJavaService;

@Component
public class Test implements LifeCycleHook {
    private final BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            for(Player player : Book.getInst().getServer().getOnlinePlayers()){
                savePlayerData(player);
            }
        }
    };

    @Inject
    private PlayerDataJavaService dataService;

    @Override
    public void onEnable() {
        dataService.register("INVENTORY", PlayerData.class, PlayerData::new);
        runnable.runTaskTimer(Book.getInst(), 0, 6000);
    }

    @Override
    public void onDisable(){
        runnable.cancel();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        savePlayerData(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("FUCK");
        PlayerData data = dataService.getData(player, PlayerData.class);
        if (data == null) return;

        player.getInventory().setContents(data.invContent);
        player.updateInventory();
    }

    //UNTESTED
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        event.getWorld().getPlayers().clear();
    }
    //UNTESTED

    private void savePlayerData(Player player){
        dataService.modify(player, PlayerData.class, data -> data.invContent = player.getInventory().getContents());
    }

    public static class PlayerData {
        public ItemStack[] invContent;
    }
}