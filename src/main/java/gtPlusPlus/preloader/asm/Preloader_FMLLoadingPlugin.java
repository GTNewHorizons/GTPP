package gtPlusPlus.preloader.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.Preloader_Logger;
import gtPlusPlus.preloader.asm.transformers.Preloader_Transformer_Handler;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import net.minecraft.launchwrapper.Launch;

@SortingIndex(10097)
@MCVersion(value = "1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("gtPlusPlus.preloader")
@IFMLLoadingPlugin.Name(CORE_Preloader.NAME)
public class Preloader_FMLLoadingPlugin implements IFMLLoadingPlugin {

    // -Dfml.coreMods.load=gtPlusPlus.preloader.asm.Preloader_FMLLoadingPlugin

    static {
        Preloader_Logger.INFO("Initializing IFMLLoadingPlugin");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        // This will return the name of the class
        return new String[] {Preloader_Transformer_Handler.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        // This is the name of our dummy container
        return Preloader_DummyContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        // return Preloader_SetupClass.class.getName();
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        boolean isDeObf = (boolean) data.get("runtimeDeobfuscationEnabled");
        File mcDir = (File) data.get("mcLocation");
        // LaunchClassLoader classLoader = (LaunchClassLoader) data.get("classLoader");
        File coremodLocation = (File) data.get("coremodLocation");
        String deobfuscationFileName = (String) data.get("deobfuscationFileName");
        if (mcDir != null && mcDir.exists()) {
            CORE_Preloader.setMinecraftDirectory(mcDir);
            Preloader_Logger.INFO("Set McDir via Preloader_SetupClass");
        }
        Preloader_Logger.INFO("runtimeDeobfuscationEnabled: " + isDeObf);
        Preloader_Logger.INFO("deobfuscationFileName: " + deobfuscationFileName);
        if (coremodLocation != null && coremodLocation.exists()) {
            Preloader_Logger.INFO("coremodLocation: " + coremodLocation.getPath());
        } else {
            Preloader_Logger.INFO("coremodLocation: null");
            Preloader_Logger.ERROR("Unable to determine CoreMod location");
        }
        CORE_Preloader.DEV_ENVIRONMENT = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        CORE_Preloader.DEBUG_MODE = AsmConfig.debugMode;
        Preloader_Logger.INFO("Running on " + gtPlusPlus.preloader.CORE_Preloader.JAVA_VERSION
                + " | Development Environment: " + CORE_Preloader.DEV_ENVIRONMENT);
        // Preloader_Logger.INFO("Is Client? "+Utils.isClient()+" | Is Server? "+Utils.isServer());
        Locale aDefaultLocale = Locale.getDefault();
        NumberFormat aFormat = NumberFormat.getInstance();
        Locale aDisplayLocale =
                ReflectionUtils.getFieldValue(ReflectionUtils.getField(Locale.class, "defaultDisplayLocale"));
        Locale aFormatLocale =
                ReflectionUtils.getFieldValue(ReflectionUtils.getField(Locale.class, "defaultFormatLocale"));
        Preloader_Logger.INFO("Locale: " + aDefaultLocale + " | Test: " + aFormat.format(1000000000) + " | Display: "
                + aDisplayLocale + " | Format: " + aFormatLocale);
    }
}
