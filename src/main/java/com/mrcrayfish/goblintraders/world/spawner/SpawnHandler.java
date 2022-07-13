package com.mrcrayfish.goblintraders.world.spawner;

import com.mrcrayfish.goblintraders.Config;
import com.mrcrayfish.goblintraders.Reference;
import com.mrcrayfish.goblintraders.init.ModEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class SpawnHandler
{
    private static Map<ResourceLocation, GoblinTraderSpawner> spawners = new HashMap<>();

    @SubscribeEvent
    public static void onWorldLoad(ServerStartingEvent event)
    {
        MinecraftServer server = event.getServer();
        spawners.put(BuiltinDimensionTypes.OVERWORLD.location(), new GoblinTraderSpawner(server, "GoblinTrader", ModEntities.GOBLIN_TRADER.get(), Config.COMMON.goblinTrader));
        spawners.put(BuiltinDimensionTypes.NETHER.location(), new GoblinTraderSpawner(server, "VeinGoblinTrader", ModEntities.VEIN_GOBLIN_TRADER.get(), Config.COMMON.veinGoblinTrader));
    }

    @SubscribeEvent
    public static void onServerStart(ServerStoppedEvent event)
    {
        spawners.clear();
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        if(event.side != LogicalSide.SERVER)
            return;

        GoblinTraderSpawner spawner = spawners.get(event.level.dimension().location());
        if(spawner != null)
        {
            spawner.tick(event.level);
        }
    }
}
