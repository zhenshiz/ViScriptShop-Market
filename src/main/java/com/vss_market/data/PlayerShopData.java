package com.vss_market.data;

import com.lowdragmc.lowdraglib2.syncdata.IPersistedSerializable;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.utils.PersistedParser;
import com.mojang.serialization.Codec;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class PlayerShopData implements IPersistedSerializable {
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerShopData> STREAM_CODEC;
    public static final Codec<PlayerShopData> CODEC;

    @Persisted
    private UUID ownerId = new UUID(0L, 0L);
    @Persisted
    private String ownerName = "";
    @Persisted
    private String name = "";
    @Persisted
    private int balance;
    @Persisted
    private long createdTime;
    @Persisted
    private long updatedTime;
    @Persisted
    private final List<MarketListing> listings = new ArrayList<>();

    static {
        CODEC = PersistedParser.createCodec(PlayerShopData::new);
        STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
    }

    public Optional<MarketListing> findListing(String listingId) {
        return listings.stream().filter(listing -> listing.getId().equals(listingId)).findFirst();
    }

    public boolean removeListing(String listingId) {
        return listings.removeIf(listing -> listing.getId().equals(listingId));
    }

    public int availableListingCount() {
        int count = 0;
        for (var listing : listings) {
            if (listing.getStock() > 0) {
                count++;
            }
        }
        return count;
    }
}
