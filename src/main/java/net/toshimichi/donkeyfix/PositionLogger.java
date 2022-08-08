package net.toshimichi.donkeyfix;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class PositionLogger extends PacketAdapter {

    private final Map<Player, Integer> positionTicks = Collections.synchronizedMap(new WeakHashMap<>());
    private int tick;

    public PositionLogger(Plugin plugin) {
        super(plugin, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK);
    }

    public void updateTick(int tick) {
        this.tick = tick;
    }

    public int getLastPositionTick(Player player) {
        Integer result = positionTicks.get(player);
        if (result == null) return -1;
        else return result;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        positionTicks.put(event.getPlayer(), tick);
    }
}
