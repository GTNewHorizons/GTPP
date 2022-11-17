package gtPlusPlus.xmod.gregtech.common.blocks;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.objects.GTPP_CopiedBlockTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class GregtechMetaCasingBlocks4 extends GregtechMetaCasingBlocksAbstract {

    public GregtechMetaCasingBlocks4() {
        super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.4", GT_Material_Casings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            if (i == 2 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 12 || i == 13 || i == 14 || i == 15) {
                continue;
            }
            TAE.registerTexture(3, i, new GTPP_CopiedBlockTexture(this, 6, i));
        }
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Naquadah Reactor Base"); // 48
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Reactor Piping");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Naquadah Containment Chamber");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Tempered Arc Furnace Casing");
        GT_LanguageManager.addStringLocalization(
                this.getUnlocalizedName() + ".4.name", "Quantum Force Transformer Coil Casings"); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", ""); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", ""); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", ""); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", ""); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", ""); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Vacuum Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Turbodyne Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", ""); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", ""); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", ""); // Unused
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", ""); // Unused
        GregtechItemList.Casing_Naq_Reactor_A.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_Naq_Reactor_B.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_Naq_Reactor_C.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_Industrial_Arc_Furnace.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_Coil_QuantumForceTransformer.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_Vacuum_Furnace.set(new ItemStack(this, 1, 10));
        GregtechItemList.Casing_RocketEngine.set(new ItemStack(this, 1, 11));
    }

    // private static final LargeTurbineTextureHandler mTurbineTextures = new LargeTurbineTextureHandler();

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord, final int aSide) {
    	final GregtechMetaCasingBlocks4 i = this;
    	return mTurbineTextures.handleCasingsGT(aWorld, xCoord, yCoord, zCoord, aSide, i);
    }*/

    @Override
    public IIcon getIcon(final int aSide, final int aMeta) {
        return getStaticIcon((byte) aSide, (byte) aMeta);
    }

    public static IIcon getStaticIcon(final byte aSide, final byte aMeta) {
        // Texture ID's. case 0 == ID[57]
        if ((aMeta >= 0) && (aMeta < 16)) {
            switch (aMeta) {
                case 0:
                    return TexturesGtBlock.Casing_Trinium_Titanium.getIcon();
                case 1:
                    return TexturesGtBlock.TEXTURE_TECH_C.getIcon();
                case 2:
                    return TexturesGtBlock.TEXTURE_ORGANIC_PANEL_A_GLOWING.getIcon();
                case 3:
                    return TexturesGtBlock.TEXTURE_METAL_PANEL_A.getIcon();
                case 4:
                    return TexturesGtBlock.Casing_Coil_QFT.getIcon();
                case 5:
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                case 6:
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                case 7:
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                case 8:
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                case 9:
                    return TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
                case 10:
                    if (aSide < 2) {
                        return TexturesGtBlock.TEXTURE_STONE_RED_B.getIcon();
                    } else {
                        return TexturesGtBlock.TEXTURE_STONE_RED_A.getIcon();
                    }
                case 11:
                    return TexturesGtBlock.TEXTURE_CASING_ROCKETDYNE.getIcon();
                case 12:
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                case 13:
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                case 14:
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                case 15:
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                default:
                    return TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
            }
        }
        return TexturesGtBlock._PlaceHolder.getIcon();
    }
}
