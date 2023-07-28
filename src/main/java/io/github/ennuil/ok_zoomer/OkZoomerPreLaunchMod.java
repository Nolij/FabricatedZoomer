package io.github.ennuil.ok_zoomer;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.quiltmc.config.api.annotations.ConfigFieldAnnotationProcessors;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;

import io.github.ennuil.ok_zoomer.config.metadata.WidgetSize;

public class OkZoomerPreLaunchMod implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		MixinExtrasBootstrap.init();
		ConfigFieldAnnotationProcessors.register(WidgetSize.class, new WidgetSize.Processor());
	}
}
