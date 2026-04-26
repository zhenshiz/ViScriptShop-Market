package com.vss_market.network.s2c;

import com.lowdragmc.lowdraglib2.networking.rpc.RPCPacket;
import com.lowdragmc.lowdraglib2.networking.rpc.RPCPacketDistributor;
import com.lowdragmc.lowdraglib2.syncdata.rpc.RPCSender;
import com.viscriptshop.gui.components.Message;
import com.viscriptshop.util.ViScriptShopServerUtil;
import com.vss_market.VSSMarket;
import com.vss_market.network.MarketClientBridge;
import com.vss_market.util.MarketResult;
import com.vss_market.util.MarketServerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class S2CPayload {
    public static final String MOD_ID = VSSMarket.MOD_ID + ":";
    public static final String OPEN_MARKET_SCREEN = MOD_ID + "open_market_screen_s2c";

    public static void open(ServerPlayer player, String selectedShopId, MarketResult result) {
        open(player, selectedShopId, "", "MARKET", result);
    }

    public static void open(ServerPlayer player, String selectedShopId, String view, MarketResult result) {
        open(player, selectedShopId, "", view, result);
    }

    public static void open(ServerPlayer player, String selectedShopId, String selectedListingId, String view, MarketResult result) {
        RPCPacketDistributor.rpcToPlayer(player, OPEN_MARKET_SCREEN, createPayload(player, selectedShopId, selectedListingId, view, result));
    }

    public static void openUpload(ServerPlayer player, MarketResult result, ItemStack stack, int price, int bundleSize, int stock) {
        var payload = createPayload(player, player.getUUID().toString(), "", "UPLOAD", result);
        payload.put("uploadStack", (stack == null ? ItemStack.EMPTY : stack).saveOptional(player.registryAccess()));
        payload.putInt("uploadPrice", price);
        payload.putInt("uploadBundleSize", bundleSize);
        payload.putInt("uploadStock", stock);
        RPCPacketDistributor.rpcToPlayer(player, OPEN_MARKET_SCREEN, payload);
    }

    @RPCPacket(OPEN_MARKET_SCREEN)
    public static void openMarketScreen(RPCSender sender, CompoundTag payload) {
        MarketClientBridge.openMarket(payload);
    }

    private static CompoundTag createPayload(ServerPlayer player, String selectedShopId, String selectedListingId, String view, MarketResult result) {
        var payload = new CompoundTag();
        payload.put("market", MarketServerUtil.data(player).serializeNBT(player.registryAccess()));
        payload.putString("viewer", player.getUUID().toString());
        payload.putString("selectedShop", selectedShopId == null ? "" : selectedShopId);
        payload.putString("selectedListing", selectedListingId == null ? "" : selectedListingId);
        payload.putString("view", view == null || view.isBlank() ? "MARKET" : view);
        payload.putInt("money", ViScriptShopServerUtil.getMoney(player));
        if (result != null) {
            payload.putString("message", result.messageKey());
            payload.putString("messageType", result.type().name());
        } else {
            payload.putString("messageType", Message.Type.INFO.name());
        }
        return payload;
    }
}
