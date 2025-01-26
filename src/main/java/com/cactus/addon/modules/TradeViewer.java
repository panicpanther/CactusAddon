package com.cactus.addon.modules;

import com.cactus.addon.AddonCactus;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

public class TradeViewer extends Module {
    public TradeViewer() {
        super(AddonCactus.CATEGORY, "Trade Viewer", "Lets you view locked trades. (WIP)");
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof SetTradeOffersS2CPacket packet) {
            TradeOfferList tradeOffers = packet.getOffers();

            if (tradeOffers != null && !tradeOffers.isEmpty()) {
                info("Villager Trades:");
                for (TradeOffer offer : tradeOffers) {
                    info(offer.getOriginalFirstBuyItem().getName().getString() +
                        (offer.getSecondBuyItem().isEmpty() ? "" :
                            " + " + offer.getSecondBuyItem().get().toString()) +
                        " -> " + offer.getSellItem().getName().getString());
                }
            } else {
                info("No trades available for this villager.");
            }
        }
    }
}