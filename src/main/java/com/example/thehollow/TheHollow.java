package com.example.thehollow;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod(TheHollow.MOD_ID)
public class TheHollow {
    public static final String MOD_ID = "thehollow";
    private final Map<UUID, Integer> playerTicks = new HashMap<>();

    public TheHollow() { MinecraftForge.EVENT_BUS.register(this); }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.literal("§8[THE HOLLOW] §7Тишина здесь кажется неправильной..."));
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;
        if (!(event.player instanceof ServerPlayer player) || player.level().isDay()) return;
        UUID id = player.getUUID();
        int ticks = playerTicks.getOrDefault(id, 0) + 1;
        playerTicks.put(id, ticks);
        if (ticks % 24000 == 0 && player.getRandom().nextInt(3) == 0) {
            String[] lines = {"Кажется, кто-то наблюдает за тобой.", "Ты слышал шаги... но вокруг никого.", "Не оборачивайся слишком быстро.", "В темноте что-то шевельнулось."};
            player.sendSystemMessage(Component.literal("§8[THE HOLLOW] §7" + lines[player.getRandom().nextInt(lines.length)]));
        }
    }
}
