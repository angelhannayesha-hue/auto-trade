package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;

import java.util.Map;

package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;

public class ExampleMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (client.currentScreen instanceof MerchantScreen screen) {
                var handler = screen.getScreenHandler();
                var offers = handler.getRecipes();

                for (int i = 0; i < offers.size(); i++) {
                    TradeOffer offer = offers.get(i);
                    ItemStack result = offer.getSellItem();

                    // Only enchanted books
                    if (result.getItem() == Items.ENCHANTED_BOOK) {

                        ItemEnchantmentsComponent enchants =
                                result.get(DataComponentTypes.STORED_ENCHANTMENTS);

                        if (enchants == null) continue;

                        // Loop through enchantments
                        for (var entry : enchants.getEnchantmentEntries()) {
                            var enchant = entry.getKey().value();
                            int level = entry.getIntValue();

                            // Check Unbreaking III
                            if (enchant == Enchantments.UNBREAKING && level == 3) {

                                // Buy until trade locks
                                while (!offer.isDisabled()) {
                                    client.interactionManager.clickButton(
                                            handler.syncId,
                                            i
                                    );
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
