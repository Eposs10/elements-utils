package dev.eposs.elementsutils.config;

import dev.eposs.elementsutils.ElementsUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ElementsUtils.MOD_ID)
public class ModConfig implements ConfigData {
    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
    
    public static void save() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }


    @ConfigEntry.Gui.Excluded
    public Servers server = Servers.UNKNOWN;

    public enum Servers {
        UNKNOWN,
        COMMUNITY_SERVER_1,
        COMMUNITY_SERVER_2,
    }


    public boolean showMoonPhaseDisplay = true;
    public boolean showTimeDisplay = true;
    public boolean showPetDisplay = true;
    public Position displayPosition = Position.TOP_RIGHT;
    
    @ConfigEntry.Gui.CollapsibleObject
    public BossTimerConfig bossTimer = new BossTimerConfig();

    public static class BossTimerConfig {
        public boolean show = true;
        public TimeFormat timeFormat = TimeFormat.RELATIVE;
        public boolean colorBossNames = true;
        public boolean colorTime = true;

        public enum TimeFormat {
            RELATIVE,
            ABSOLUTE,
        }
    }

    public boolean playLootSound = true;
    
    public boolean showBaseDisplay = false;

    @ConfigEntry.Gui.CollapsibleObject
    public DevUtilsConfig devUtils = new DevUtilsConfig();

    public static class DevUtilsConfig {
        public boolean enable = false;
    }

    public enum Position {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
}
