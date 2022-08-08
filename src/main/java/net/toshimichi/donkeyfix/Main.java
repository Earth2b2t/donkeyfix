package net.toshimichi.donkeyfix;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Runnable {

    private static final int TIMEOUT = 100;
    private PositionLogger positionLogger;
    private int tick;

    @Override
    public void onEnable() {
        positionLogger = new PositionLogger(this);

        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        pm.addPacketListener(positionLogger);

        Bukkit.getScheduler().runTaskTimer(this, this, 1, 1);
    }

    @Override
    public void run() {
        ++tick;
        positionLogger.updateTick(tick);
        if (tick % 20 != 0) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Entity vehicle = player.getVehicle();
            if (vehicle == null) continue;
            int positionTick = positionLogger.getLastPositionTick(player);
            if (tick - positionTick > TIMEOUT) vehicle.eject();
        }
    }
}
