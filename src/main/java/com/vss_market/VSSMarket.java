package com.vss_market;

import com.lowdragmc.lowdraglib2.syncdata.AccessorRegistries;
import com.lowdragmc.lowdraglib2.syncdata.accessor.direct.CustomDirectAccessor;
import com.mojang.logging.LogUtils;
import com.viscriptshop.gui.data.AggregatedResources;
import com.viscriptshop.gui.data.CategoryInfo;
import com.viscriptshop.gui.data.MerchantInfo;
import com.viscriptshop.gui.data.ShopInfo;
import com.vss_market.data.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(VSSMarket.MOD_ID)
public class VSSMarket {
    public static final String MOD_ID = "vss_market";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VSSMarket(IEventBus modEventBus, ModContainer modContainer, Dist dist) {
        AccessorRegistries.setPriority(0);
        AccessorRegistries.registerAccessor(CustomDirectAccessor.builder(MarketListing.class)
                .codec(MarketListing.CODEC)
                .streamCodec(MarketListing.STREAM_CODEC)
                .codecMark()
                .build()
        );
        AccessorRegistries.registerAccessor(CustomDirectAccessor.builder(PlayerShopData.class)
                .codec(PlayerShopData.CODEC)
                .streamCodec(PlayerShopData.STREAM_CODEC)
                .codecMark()
                .build()
        );
        AccessorRegistries.registerAccessor(CustomDirectAccessor.builder(MarketSavedData.class)
                .codec(MarketSavedData.CODEC)
                .streamCodec(MarketSavedData.STREAM_CODEC)
                .codecMark()
                .build()
        );
        AccessorRegistries.registerAccessor(CustomDirectAccessor.builder(MarketScreenPayload.class)
                .codec(MarketScreenPayload.CODEC)
                .streamCodec(MarketScreenPayload.STREAM_CODEC)
                .codecMark()
                .build()
        );
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG_SPEC, "%s_config.toml".formatted(MOD_ID));
        if (dist == Dist.CLIENT) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static String formattedMod(String path) {
        return ("%s:" + path).formatted(MOD_ID);
    }

    public static boolean isPresentResource(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().getResourceManager().getResource(resourceLocation).isPresent();
    }

    public static boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    public static boolean isDevEnv() {
        return !FMLLoader.isProduction();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
