package com.vss_market.data;

import com.lowdragmc.lowdraglib2.syncdata.AccessorRegistries;
import com.lowdragmc.lowdraglib2.syncdata.accessor.direct.CustomDirectAccessor;
import com.lowdragmc.lowdraglib2.utils.PersistedParser;

import java.util.function.Supplier;

public final class MarketSerializers {
    private static boolean registered;

    private MarketSerializers() {
    }

    public static synchronized void register() {
        if (registered) {
            return;
        }
        register(PlayerShopData.class, PlayerShopData::new);
        register(MarketListing.class, MarketListing::new);
        registered = true;
    }

    private static <T> void register(Class<T> type, Supplier<T> factory) {
        AccessorRegistries.registerAccessor(CustomDirectAccessor.builder(type)
                .codec(PersistedParser.createCodec(factory))
                .streamCodec(PersistedParser.createStreamCodec(factory))
                .codecMark()
                .build(), 900);
    }
}
