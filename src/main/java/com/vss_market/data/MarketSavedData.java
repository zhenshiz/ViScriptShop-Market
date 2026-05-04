package com.vss_market.data;

import com.lowdragmc.lowdraglib2.syncdata.IPersistedSerializable;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.utils.PersistedParser;
import com.mojang.serialization.Codec;
import com.vss_market.VSSMarket;
import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class MarketSavedData extends SavedData implements IPersistedSerializable {
    private static final String DATA_NAME = VSSMarket.MOD_ID + "_market";
    private static final Factory<MarketSavedData> FACTORY = new Factory<>(MarketSavedData::new, MarketSavedData::load);

    public static final Codec<MarketSavedData> CODEC = PersistedParser.createCodec(MarketSavedData::new);
    public static final StreamCodec<ByteBuf, MarketSavedData> STREAM_CODEC = PersistedParser.createStreamCodec(MarketSavedData::new);

    @Getter
    @Persisted
    private final List<PlayerShopData> shops = new ArrayList<>();

    public static MarketSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
    }

    public static MarketSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        var data = new MarketSavedData();
        data.deserializeNBT(provider, tag);
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.merge(serializeNBT(provider));
        return tag;
    }

    public Optional<PlayerShopData> findShop(UUID ownerId) {
        return shops.stream().filter(shop -> shop.getOwnerId().equals(ownerId)).findFirst();
    }

    public Optional<PlayerShopData> findShop(String ownerId) {
        try {
            return findShop(UUID.fromString(ownerId));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    public Optional<PlayerShopData> findShopByOwnerName(String ownerName) {
        if (ownerName == null || ownerName.isBlank()) {
            return Optional.empty();
        }
        var normalized = ownerName.toLowerCase(Locale.ROOT);
        return shops.stream()
                .filter(shop -> shop.getOwnerName().toLowerCase(Locale.ROOT).equals(normalized))
                .findFirst();
    }

    public Optional<PlayerShopData> findShopByCommandKey(String key) {
        var byOwnerName = findShopByOwnerName(key);
        return byOwnerName.isPresent() ? byOwnerName : findShop(key);
    }

    public PlayerShopData getOrCreateShop(UUID ownerId, String ownerName) {
        var shop = findShop(ownerId).orElse(null);
        if (shop != null) {
            if (!ownerName.isBlank() && !ownerName.equals(shop.getOwnerName())) {
                shop.setOwnerName(ownerName);
                setDirty();
            }
            return shop;
        }
        long now = System.currentTimeMillis();
        shop = new PlayerShopData()
                .setOwnerId(ownerId)
                .setOwnerName(ownerName)
                .setName(ownerName)
                .setCreatedTime(now)
                .setUpdatedTime(now);
        shops.add(shop);
        setDirty();
        return shop;
    }

    public boolean removeShop(UUID ownerId) {
        boolean removed = shops.removeIf(shop -> shop.getOwnerId().equals(ownerId));
        if (removed) {
            setDirty();
        }
        return removed;
    }

}
