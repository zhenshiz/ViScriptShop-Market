package com.vss_market.network;

import com.vss_market.gui.MarketClientScreen;
import com.vss_market.data.MarketScreenPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

public class MarketClientBridge {
    public static void openMarket(MarketScreenPayload payload) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MarketClientScreen.open(payload);
        }
    }
}
