package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public final class TexturesGtTools {

    public static final CustomIcon SKOOKUM_CHOOCHER = new CustomIcon("iconsets/SKOOKUMCHOOCHER");
    public static final CustomIcon ANGLE_GRINDER = new CustomIcon("iconsets/ANGLE_GRINDER");
    public static final CustomIcon ELECTRIC_SNIPS = new CustomIcon("iconsets/ELECTRIC_SNIPS");
    public static final CustomIcon ELECTRIC_LIGHTER = new CustomIcon("iconsets/ELECTRIC_LIGHTER");
    public static final CustomIcon ELECTRIC_BUTCHER_KNIFE = new CustomIcon("iconsets/ELECTRIC_BUTCHER_KNIFE");

    public static final class CustomIcon implements IIconContainer, Runnable {
        protected IIcon mIcon, mOverlay;
        protected final String mIconName;

        public CustomIcon(final String aIconName) {
            this.mIconName = aIconName;
            Logger.INFO("Constructing a Custom Texture. " + this.mIconName);
            GregTech_API.sGTItemIconload.add(this);
        }

        @Override
        public IIcon getIcon() {
            return this.mIcon;
        }

        @Override
        public IIcon getOverlayIcon() {
            return this.mOverlay;
        }

        @Override
        public void run() {
            this.mIcon = GregTech_API.sItemIcons.registerIcon(CORE.MODID + ":" + this.mIconName);
            // Utils.LOG_INFO("Registering a Custom Texture. "+mIcon.g);
            this.mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.MODID + ":" + this.mIconName + "_OVERLAY");
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationItemsTexture;
        }
    }
}
