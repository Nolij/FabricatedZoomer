package io.github.ennuil.ok_zoomer.events;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import io.github.ennuil.ok_zoomer.utils.ZoomUtils;
import net.minecraft.command.CommandBuildContext;

public class RegisterCommands implements ClientCommandRegistrationCallback {
	@Override
	public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext buildContext) {
		dispatcher.register(
			ClientCommandManager.literal("ok_zoomer").executes(ctx -> {
				ZoomUtils.setOpenCommandScreen(true);
				return 0;
			}
		));
	}
}
