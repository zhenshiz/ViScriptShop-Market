package com.vss_market.data;

import com.lowdragmc.lowdraglib2.syncdata.IPersistedSerializable;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerShopData implements IPersistedSerializable {
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public PlayerShopData setOwnerId(UUID ownerId) {
        this.ownerId = ownerId == null ? new UUID(0L, 0L) : ownerId;
        return this;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public PlayerShopData setOwnerName(String ownerName) {
        this.ownerName = ownerName == null ? "" : ownerName;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlayerShopData setName(String name) {
        this.name = name == null ? "" : name;
        return this;
    }

    public int getBalance() {
        return balance;
    }

    public PlayerShopData setBalance(int balance) {
        this.balance = balance;
        return this;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public PlayerShopData setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public PlayerShopData setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public List<MarketListing> getListings() {
        return listings;
    }
}
