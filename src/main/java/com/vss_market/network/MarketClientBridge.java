package com.vss_market.network;

import com.vss_market.gui.MarketClientScreen;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class MarketClientBridge {
    public static void openMarket(CompoundTag payload) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MarketClientScreen.open(payload);
        }
    }
}
