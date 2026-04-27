package com.vss_market.data;

import com.lowdragmc.lowdraglib2.syncdata.IPersistedSerializable;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.utils.PersistedParser;
import com.mojang.serialization.Codec;
import com.viscriptshop.gui.data.ShopInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MarketListing implements IPersistedSerializable {
    public static final StreamCodec<RegistryFriendlyByteBuf, MarketListing> STREAM_CODEC;
    public static final Codec<MarketListing> CODEC;

    @Persisted
    private String id = UUID.randomUUID().toString();
    @Persisted
    private ItemStack item = ItemStack.EMPTY;
    @Persisted
    private int price = 1;
    @Persisted
    private int bundleSize = 1;
    @Persisted
    private int stock;
    @Persisted
    private long createdTime;
    @Persisted
    private long updatedTime;

    static {
        CODEC = PersistedParser.createCodec(MarketListing::new);
        STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
    }

    public ItemStack displayStack() {
        if (item.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return item.copyWithCount(Math.max(1, bundleSize));
    }

    public ItemStack unitStack() {
        if (item.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return item.copyWithCount(1);
    }

    public boolean isSoldOut() {
        return stock <= 0;
    }

    public int getBundleSize() {
        return Math.max(1, bundleSize);
    }

    public MarketListing setBundleSize(int bundleSize) {
        this.bundleSize = Math.max(1, bundleSize);
        return this;
    }
}
