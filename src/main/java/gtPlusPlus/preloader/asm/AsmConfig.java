package gtPlusPlus.preloader.asm;

import cpw.mods.fml.common.FMLLog;
import gtPlusPlus.preloader.Preloader_Logger;
import java.io.File;
import java.util.ArrayList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

public class AsmConfig {

    public static boolean loaded;
    public static Configuration config;

    public static boolean enableOreDictPatch;
    public static boolean enableTiConFluidLighting;
    public static boolean enableGtTooltipFix;
    public static boolean enableGtNbtFix;
    public static boolean enableGtCharcoalPitFix;
    public static boolean enableChunkDebugging;
    public static boolean enableCofhPatch;
    public static boolean enableGcFuelChanges;
    public static boolean enableRcFlowFix;
    public static int maxRailcraftTankProcessVolume;
    public static int maxRailcraftFluidLoaderFlow;
    public static int maxRailcraftFluidUnloaderFlow;
    public static boolean enableRcItemDupeFix;
    public static boolean enableTcAspectSafety;
    public static boolean enabledLwjglKeybindingFix;
    public static boolean enabledFixEntitySetHealth;
    public static boolean enableThaumicTinkererRepairFix;

    public static boolean disableAllLogging;
    public static boolean debugMode;

    public AsmConfig(File file) {
        if (!loaded) {
            config = new Configuration(file);
            syncConfig(true);
        }
    }

    public static void syncConfig(boolean load) {
        ArrayList<String> propOrder = new ArrayList<String>();
        ArrayList<String> propOrderDebug = new ArrayList<String>();

        try {
            if (!config.isChild && load) {
                config.load();
            }

            Property prop;

            // Debug
            prop = config.get("debug", "disableAllLogging", true);
            prop.comment = "Disables ALL logging from GT++.";
            prop.setLanguageKey("gtpp.disableAllLogging").setRequiresMcRestart(false);
            disableAllLogging = prop.getBoolean(true);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "debugMode", false);
            prop.comment = "Enables all sorts of debug logging. (Don't use unless told to, breaks other things.)";
            prop.setLanguageKey("gtpp.debugMode").setRequiresMcRestart(false);
            debugMode = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "enabledFixEntitySetHealth", false);
            prop.comment = "Enable/Disable entity setHealth() fix.";
            prop.setLanguageKey("gtpp.enabledFixEntitySetHealth").setRequiresMcRestart(true);
            enabledFixEntitySetHealth = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "enableChunkDebugging", false);
            prop.comment = "Enable/Disable Chunk Debugging Features, Must Be enabled on Client and Server.";
            prop.setLanguageKey("gtpp.enableChunkDebugging").setRequiresMcRestart(true);
            enableChunkDebugging = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "enableGtNbtFix", true);
            prop.comment = "Enable/Disable GT NBT Persistency Fix";
            prop.setLanguageKey("gtpp.enableGtNbtFix").setRequiresMcRestart(true);
            enableGtNbtFix = prop.getBoolean(true);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "enableCofhPatch", false);
            prop.comment = "Enable/Disable COFH OreDictionaryArbiter Patch (Useful for Development)";
            prop.setLanguageKey("gtpp.enableCofhPatch").setRequiresMcRestart(true);
            enableCofhPatch = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "enableOreDictPatch", false);
            prop.comment = "Enable/Disable Forge OreDictionary Patch (Useful for Development)";
            prop.setLanguageKey("gtpp.enableOreDictPatch").setRequiresMcRestart(true);
            enableOreDictPatch = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            prop = config.get("debug", "enableThaumicTinkererRepairFix", false);
            prop.comment = "Enable/Disable Patch for Thaumic Repairer";
            prop.setLanguageKey("gtpp.enableThaumicTinkererRepairFix").setRequiresMcRestart(true);
            enableThaumicTinkererRepairFix = prop.getBoolean(false);
            propOrderDebug.add(prop.getName());

            // General Features
            prop = config.get("general", "enableTiConFluidLighting", true);
            prop.comment = "Enable/Disable Brightness Visuals for Tinkers Fluids, only required on the Client.";
            prop.setLanguageKey("gtpp.enableTiConFluidLighting").setRequiresMcRestart(true);
            enableTiConFluidLighting = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get("general", "enabledLwjglKeybindingFix", true);
            prop.comment =
                    "Prevents the game crashing from having invalid keybinds. https://github.com/alkcorp/GTplusplus/issues/544";
            prop.setLanguageKey("gtpp.enabledLwjglKeybindingFix").setRequiresMcRestart(true);
            enabledLwjglKeybindingFix = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get("general", "enableGtTooltipFix", true);
            prop.comment = "Enable/Disable Custom GT Tooltips";
            prop.setLanguageKey("gtpp.enableGtTooltipFix").setRequiresMcRestart(true);
            enableGtTooltipFix = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get("general", "enableGtCharcoalPitFix", true);
            prop.comment = "Makes the Charcoal Pile Igniter work better.";
            prop.setLanguageKey("gtpp.enableGtCharcoalPitFix").setRequiresMcRestart(true);
            enableGtCharcoalPitFix = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get("general", "enableGcFuelChanges", true);
            prop.comment = "Enable/Disable changes to Galacticraft Rocket Fuels.";
            prop.setLanguageKey("gtpp.enableGcFuelChanges").setRequiresMcRestart(true);
            // Disabled because Broken
            // enableGcFuelChanges = prop.getBoolean(true);
            enableGcFuelChanges = false;
            propOrder.add(prop.getName());

            // Railcraft Tank fix
            prop = config.get("general", "enableRcFlowFix", true);
            prop.comment = "Allows Custom max IO rates on RC tanks";
            prop.setLanguageKey("gtpp.enableRcFlowFix").setRequiresMcRestart(true);
            enableRcFlowFix = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get("general", "maxRailcraftTankProcessVolume", 4000);
            prop.comment = "Max IO for RC fluid tanks (Not Carts). 'enableRcFlowFix' Must be enabled.";
            prop.setLanguageKey("gtpp.maxRailcraftTankProcessVolume").setRequiresMcRestart(true);
            maxRailcraftTankProcessVolume = prop.getInt(4000);
            propOrder.add(prop.getName());

            // Railcraft Loader Max flowrate
            prop = config.get("general", "maxRailcraftFluidLoaderFlow", 20);
            prop.comment = "Max Output rate for RC Fluid Loaders";
            prop.setLanguageKey("gtpp.maxRailcraftFluidLoaderFlow").setRequiresMcRestart(true);
            maxRailcraftFluidLoaderFlow = prop.getInt(20);
            propOrder.add(prop.getName());

            // Railcraft Unloader Max flowrate
            prop = config.get("general", "maxRailcraftFluidUnloaderFlow", 80);
            prop.comment = "Max Output rate for RC Fluid Unloaders";
            prop.setLanguageKey("gtpp.maxRailcraftFluidUnloaderFlow").setRequiresMcRestart(true);
            maxRailcraftFluidUnloaderFlow = prop.getInt(80);
            propOrder.add(prop.getName());

            // Railcraft Dupe Fix
            prop = config.get("general", "enableRcItemDupeFix", true);
            prop.comment = "Fixes possible negative itemstacks";
            prop.setLanguageKey("gtpp.enableRcItemDupeFix").setRequiresMcRestart(true);
            enableRcItemDupeFix = prop.getBoolean(true);
            propOrder.add(prop.getName());

            // TC Aspect Safety
            prop = config.get("general", "enableTcAspectSafety", true);
            prop.comment = "Fixes small oversights in Thaumcraft 4.";
            prop.setLanguageKey("gtpp.enableTcAspectSafety").setRequiresMcRestart(true);
            enableTcAspectSafety = prop.getBoolean(true);
            propOrder.add(prop.getName());

            config.setCategoryPropertyOrder("general", propOrder);
            config.setCategoryPropertyOrder("debug", propOrderDebug);
            if (config.hasChanged()) {
                config.save();
            }

            Preloader_Logger.INFO("Chunk Debugging - Enabled: " + enableChunkDebugging);
            Preloader_Logger.INFO("Gt Nbt Fix - Enabled: " + enableGtNbtFix);
            Preloader_Logger.INFO("TiCon Fluid Lighting - Enabled: " + enableTiConFluidLighting);
            Preloader_Logger.INFO("Gt Tooltip Fix - Enabled: " + enableGtTooltipFix);
            Preloader_Logger.INFO("COFH Patch - Enabled: " + enableCofhPatch);
            Preloader_Logger.INFO("Gc Fuel Changes Patch - Enabled: " + enableGcFuelChanges);
            Preloader_Logger.INFO("Railcraft Fluid Flow Patch - Enabled: " + enableRcFlowFix);
            Preloader_Logger.INFO("Thaumcraft Aspect Safety Patch - Enabled: " + enableTcAspectSafety);
            Preloader_Logger.INFO(
                    "Fix bad usage of EntityLivingBase.setHealth Patch - Enabled: " + enabledFixEntitySetHealth);

        } catch (Exception var3) {
            FMLLog.log(Level.ERROR, var3, "GT++ ASM had a problem loading it's config", new Object[0]);
        }
    }
}
