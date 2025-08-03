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
    public InternalConfig internal = new InternalConfig();

    public static class InternalConfig {
        public Servers server = Servers.UNKNOWN;

        public enum Servers {
            UNKNOWN,
            COMMUNITY_SERVER_1,
            COMMUNITY_SERVER_2,
        }

        public PetData petData = new PetData();

        public static class PetData {
            public String data;
            public int currentXP;
            public int nextLvlXP;
        }
    }

    public boolean showMoonPhaseDisplay = true;
    public boolean showTimeDisplay = true;
    public boolean showPetDisplay = true;
    public Position displayPosition = Position.TOP_RIGHT;

    public enum Position {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    public enum TimeFormat {
        RELATIVE,
        ABSOLUTE,
    }

    @ConfigEntry.Gui.CollapsibleObject
    public BossTimerConfig bossTimer = new BossTimerConfig();
    public static class BossTimerConfig {
        public boolean show = true;
        public boolean textOutline = true;

        public boolean colorBossNames = true;
        public boolean colorBossTime = true;
        public TimeFormat bossTimeFormat = TimeFormat.RELATIVE;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public ExcaliburTimeConfig excaliburTime = new ExcaliburTimeConfig();
    public static class ExcaliburTimeConfig {
        public boolean show = true;
        public boolean textOutline = true;

        public boolean colorExcaliburNames = true;
        public boolean colorExcaliburTime = true;
        public TimeFormat excaliburTimeFormat = TimeFormat.ABSOLUTE;
    }

    public boolean playLootSound = true;

    public boolean showBaseDisplay = false;

    @ConfigEntry.Gui.CollapsibleObject
    public PotionDisplayConfig potionDisplay = new PotionDisplayConfig();
    public static class PotionDisplayConfig {
        public boolean show = true;

        public Position position = Position.RIGHT;
        public enum Position {
            LEFT,
            RIGHT,
        }

    }

    @ConfigEntry.Gui.CollapsibleObject
    public ChatEnhancementsConfig chatEnhancements = new ChatEnhancementsConfig();

    public static class ChatEnhancementsConfig {
        public boolean showLuckyDropSummary = false;
        public int luckyDropSummaryMinutes = 60;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public PlayerEnhancementsConfig playerEnhancements = new PlayerEnhancementsConfig();

    public static class PlayerEnhancementsConfig {
        public boolean hideOwnArmor = false;
        public boolean hideHelmet = true;
        public boolean hideChestplate = true;
        public boolean hideLeggings = true;
        public boolean hideBoots = false;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public PlayerLevelConfig playerLevelConfig = new PlayerLevelConfig();
    public static class PlayerLevelConfig {
        public boolean enabled = true;
        public KnownColor formattedPlayerLevelColor = KnownColor.EXPERIENCE_GREEN;
        public KnownColor formattedPlayerListLevelColor = KnownColor.YELLOW;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public ElementsXPConfig elementsXPConfig = new ElementsXPConfig();
    public static class ElementsXPConfig {
        public boolean enabled = true;
        public boolean showXpPerSecond = false;
        public KnownColor xpPerSecondColor = KnownColor.GRAY;
        public int maxAgeSeconds = 20;
        public int resetTimeoutSeconds = 5;
        public KnownColor overlayMessageColor = KnownColor.DARK_AQUA;
        public boolean hideMaxPetXP = false;
        public int overlayMessageYOffset = 0;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public OverlaySettingsConfig overlaySettings = new OverlaySettingsConfig();
    public static class OverlaySettingsConfig {
        public boolean overrideAfkTitleTime = false;

        @ConfigEntry.Gui.Tooltip
        public AfkTitleTimeType afkTitleTimeType = AfkTitleTimeType.INFINITY;

        public int afkTitleTimeSeconds = 30;

        public enum AfkTitleTimeType {
            INFINITY,
            SECONDS
        }
    }

    public enum KnownColor {
        BLACK(0x000000),
        DARK_BLUE(0x0000AA),
        DARK_GREEN(0x00AA00),
        DARK_AQUA(0x00AAAA),
        DARK_RED(0xAA0000),
        DARK_PURPLE(0xAA00AA),
        GOLD(0xFFAA00),
        GRAY(0xAAAAAA),
        DARK_GRAY(0x555555),
        BLUE(0x5555FF),
        GREEN(0x55FF55),
        EXPERIENCE_GREEN(0x80FF20),
        AQUA(0x55FFFF),
        RED(0xFF5555),
        LIGHT_PURPLE(0xFF55FF),
        YELLOW(0xFFFF55),
        WHITE(0xFFFFFF);

        public final int color;
        KnownColor(int color) { this.color = color; }
    }

    @ConfigEntry.Gui.CollapsibleObject
    public XPMeterConfig xpMeterConfig = new XPMeterConfig();
    public static class XPMeterConfig {
        public Integer measuringXpTarget = 500;
        public Integer measuringTimeTarget = 300;
    }

    @ConfigEntry.Gui.CollapsibleObject
    public DevUtilsConfig devUtils = new DevUtilsConfig();
    public static class DevUtilsConfig {
        public boolean enable = false;

    }
}
