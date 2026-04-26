package com.vss_market.data;

import com.lowdragmc.lowdraglib2.syncdata.IPersistedSerializable;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class MarketListing implements IPersistedSerializable {
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

    public String getId() {
        return id;
    }

    public MarketListing setId(String id) {
        this.id = id;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public MarketListing setItem(ItemStack item) {
        this.item = item == null ? ItemStack.EMPTY : item;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public MarketListing setPrice(int price) {
        this.price = price;
        return this;
    }

    public int getBundleSize() {
        return Math.max(1, bundleSize);
    }

    public MarketListing setBundleSize(int bundleSize) {
        this.bundleSize = Math.max(1, bundleSize);
        return this;
    }

    public int getStock() {
        return stock;
    }

    public MarketListing setStock(int stock) {
        this.stock = stock;
        return this;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public MarketListing setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public MarketListing setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }
}
