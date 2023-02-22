package com.connormarchand.itemid;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

@Plugin(
        id = "itemid",
        name = "Itemid",
        description = "Easiest way to get the full id of an item in your hand!",
        url = "https://ore.spongepowered.org/monarch8/itemid",
        authors = {
                "Connor Marchand"
        }
)
public class Itemid {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer plugin;

    //build command
    @Listener
    public void onGameInit(GameInitializationEvent event) {

        CommandSpec ItemidCommand = CommandSpec.builder()
                .description(Text.of("itemid"))
                .permission("itemid.commands.itemid")
                .executor((CommandSource src, CommandContext args) -> {
                    if (src instanceof Player) {
                        Player player = (Player) src;
                        Optional<ItemStack> optionalItemStack = player.getItemInHand(HandTypes.MAIN_HAND);
                        if(optionalItemStack.isPresent()){
                            ItemStack itemStack = optionalItemStack.get();

                            DataContainer container = itemStack.toContainer();
                            DataQuery query = DataQuery.of('/', "UnsafeDamage");
                            int metadata = Integer.parseInt(container.get(query).get().toString());

                            String itemId = itemStack.getType().getId();
                            if (metadata != 0) {
                                itemId = itemId + ":" + metadata;
                            }

                            Text text1 = Text.builder("The ID of the item is: ").color(TextColors.WHITE).append(
                                    Text.builder(itemId).color(TextColors.GREEN).build()).build();
                            player.sendMessage(text1);
                            return CommandResult.success();
                        }
                    }
                    return CommandResult.empty();
                })
                .build();

        Sponge.getCommandManager().register(plugin, ItemidCommand, "itemid");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info(plugin.getName() + " version " + plugin.getVersion().orElse("") + " is enabled!");
    }

}
