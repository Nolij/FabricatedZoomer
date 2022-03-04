package io.github.ennuil.okzoomer.utils;

import com.mojang.blaze3d.platform.InputUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.ennuil.libzoomer.api.ZoomInstance;
import io.github.ennuil.libzoomer.api.modifiers.ZoomDivisorMouseModifier;
import io.github.ennuil.libzoomer.api.transitions.SmoothTransitionMode;
import io.github.ennuil.okzoomer.config.OkZoomerConfigManager;
import io.github.ennuil.okzoomer.key_binds.ZoomKeyBinds;
import io.github.ennuil.okzoomer.packets.ZoomPackets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

// The class that contains most of the logic behind the zoom itself
public class ZoomUtils {
    // The logger, used everywhere to print messages to the console
    public static final Logger LOGGER = LogManager.getFormatterLogger("Ok Zoomer");

    public static final ZoomInstance ZOOMER_ZOOM = new ZoomInstance(
        new Identifier("okzoomer:zoom"),
        4.0F,
        new SmoothTransitionMode(0.75f),
        new ZoomDivisorMouseModifier(),
        null
    );

    public static int zoomStep = 0;

    // The method used for changing the zoom divisor, used by zoom scrolling and the key binds
    public static final void changeZoomDivisor(boolean increase) {
        //If the zoom is disabled, don't allow for zoom scrolling
        if (ZoomPackets.getDisableZoom() || ZoomPackets.getDisableZoomScrolling()) {
            return;
        }

        double zoomDivisor = OkZoomerConfigManager.configInstance.values().getZoomDivisor();
        double minimumZoomDivisor = OkZoomerConfigManager.configInstance.values().getMinimumZoomDivisor();
        double maximumZoomDivisor = OkZoomerConfigManager.configInstance.values().getMaximumZoomDivisor();
        int upperScrollStep = OkZoomerConfigManager.configInstance.values().getUpperScrollStep();
        int lowerScrollStep = OkZoomerConfigManager.configInstance.values().getLowerScrollStep();

        if (ZoomPackets.getForceZoomDivisors()) {
            double packetMinimumZoomDivisor = ZoomPackets.getMaximumZoomDivisor();
            double packetMaximumZoomDivisor = ZoomPackets.getMaximumZoomDivisor();

            if (packetMinimumZoomDivisor < minimumZoomDivisor) {
                minimumZoomDivisor = packetMinimumZoomDivisor;
            }
            
            if (packetMaximumZoomDivisor > maximumZoomDivisor) {
                maximumZoomDivisor = packetMaximumZoomDivisor;
            }
        }

        if (increase) {
            zoomStep = Math.min(zoomStep + 1, upperScrollStep);
        } else {
            zoomStep = Math.max(zoomStep - 1, -lowerScrollStep);
        }

        if (zoomStep > 0) {
            ZOOMER_ZOOM.setZoomDivisor(zoomDivisor + ((maximumZoomDivisor - zoomDivisor) / upperScrollStep * zoomStep));
        } else if (zoomStep == 0) {
            ZOOMER_ZOOM.setZoomDivisor(zoomDivisor);
        } else {
            ZOOMER_ZOOM.setZoomDivisor(zoomDivisor + ((minimumZoomDivisor - zoomDivisor) / lowerScrollStep * -zoomStep));
        }

        // FIXME - Remove me!
        System.out.println(zoomStep);
        System.out.println(ZOOMER_ZOOM.getZoomDivisor());
    }

    // The method used by both the "Reset Zoom" keybind and the "Reset Zoom With Mouse" tweak
    public static final void resetZoomDivisor(boolean userPrompted) {
        // TODO - wait, why am i doing this check at all? Investigate
        if (userPrompted && (ZoomPackets.getDisableZoom() || ZoomPackets.getDisableZoomScrolling())) {
            return;
        }

        zoomStep = 0;

        ZOOMER_ZOOM.resetZoomDivisor();
    }

    public static final void keepZoomStepsWithinBounds() {
        int upperScrollStep = OkZoomerConfigManager.configInstance.values().getUpperScrollStep();
        int lowerScrollStep = OkZoomerConfigManager.configInstance.values().getLowerScrollStep();

        zoomStep = MathHelper.clamp(zoomStep, -lowerScrollStep, upperScrollStep);
    }

    // The method used for unbinding the "Save Toolbar Activator"
    public static final void unbindConflictingKey(MinecraftClient client, boolean userPrompted) {
        if (ZoomKeyBinds.ZOOM_KEY.isDefault()) {
            if (client.options.saveToolbarActivatorKey.isDefault()) {
                if (userPrompted) {
                    ZoomUtils.LOGGER.info("[Ok Zoomer] The \"Save Toolbar Activator\" keybind was occupying C! Unbinding...");
                    client.getToastManager().add(SystemToast.create(client, SystemToast.Type.TUTORIAL_HINT, new TranslatableText("toast.okzoomer.title"), new TranslatableText("toast.okzoomer.unbind_conflicting_key.success")));
                } else {
                    ZoomUtils.LOGGER.info("[Ok Zoomer] The \"Save Toolbar Activator\" keybind was occupying C! Unbinding... This process won't be repeated until specified in the config.");
                }
                client.options.saveToolbarActivatorKey.setBoundKey(InputUtil.UNKNOWN_KEY);
                client.options.write();
                KeyBind.updateBoundKeys();
            } else {
                ZoomUtils.LOGGER.info("[Ok Zoomer] No conflicts with the \"Save Toolbar Activator\" keybind were found!");
                if (userPrompted) {
                    client.getToastManager().add(SystemToast.create(client, SystemToast.Type.TUTORIAL_HINT, new TranslatableText("toast.okzoomer.title"), new TranslatableText("toast.okzoomer.unbind_conflicting_key.no_conflict")));
                }
            }
        }
    }
}