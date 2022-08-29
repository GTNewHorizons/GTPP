package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.core.lib.CORE;

@SuppressWarnings("deprecation")
public class GT_MetaTileEntity_Dehydrator extends GT_MetaTileEntity_BasicMachine_GT_Recipe {

    private static final CustomIcon[] sDehydratorOverlays = new CustomIcon[10];

    static {
        sDehydratorOverlays[0] = new CustomIcon("basicmachines/microwave/OVERLAY_FRONT");
        sDehydratorOverlays[2] = new CustomIcon("basicmachines/plasma_arc_furnace/OVERLAY_BOTTOM");
        sDehydratorOverlays[3] = new CustomIcon("basicmachines/fluid_heater/OVERLAY_SIDE");
        sDehydratorOverlays[4] = new CustomIcon("basicmachines/chemical_bath/OVERLAY_FRONT");
        sDehydratorOverlays[5] = new CustomIcon("basicmachines/microwave/OVERLAY_FRONT_ACTIVE");
        sDehydratorOverlays[7] = new CustomIcon("basicmachines/plasma_arc_furnace/OVERLAY_BOTTOM_ACTIVE");
        sDehydratorOverlays[8] = new CustomIcon("basicmachines/fluid_heater/OVERLAY_SIDE_ACTIVE");
        sDehydratorOverlays[9] = new CustomIcon("basicmachines/chemical_bath/OVERLAY_FRONT_ACTIVE");
        // 3 8
    }

    private GT_Recipe_Map xRecipes;
    private int xTankCapacity;

    public GT_MetaTileEntity_Dehydrator(
            int aID, String aName, String aNameRegional, int aTier, String aDescription, int aTankCapacity) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                aDescription,
                GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes,
                2,
                9,
                aTankCapacity,
                2,
                5,
                "Dehydrator.png",
                "UNBOXINATOR",
                false,
                false,
                0,
                "",
                null);
        xRecipes = GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes;
        xTankCapacity = aTankCapacity;
    }

    public GT_MetaTileEntity_Dehydrator(
            String aName,
            int aTier,
            String[] aDescription,
            GT_Recipe.GT_Recipe_Map aRecipes,
            int aTankCapacity,
            int aAmperage,
            ITexture[][][] aTextures,
            String aGUIName,
            String aNEIName) {
        super(
                aName,
                aTier,
                aDescription,
                aRecipes,
                2,
                9,
                aTankCapacity,
                aAmperage,
                2,
                5,
                aTextures,
                aGUIName,
                aNEIName,
                "",
                false,
                false,
                0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Dehydrator(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.xRecipes,
                this.xTankCapacity,
                this.mAmperage,
                this.mTextures,
                this.mGUIName,
                this.mNEIName);
    }

    @Override
    public String[] getDescription() {
        String[] S = super.getDescription();
        final String[] desc = new String[S.length + 1];
        System.arraycopy(S, 0, desc, 0, S.length);
        desc[S.length] = CORE.GT_Tooltip;
        return desc;
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[15][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = getSideFacingActive(i);
            rTextures[1][i + 1] = getSideFacingInactive(i);
            rTextures[2][i + 1] = getFrontFacingActive(i);
            rTextures[3][i + 1] = getFrontFacingInactive(i);
            rTextures[4][i + 1] = getTopFacingActive(i);
            rTextures[5][i + 1] = getTopFacingInactive(i);
            rTextures[6][i + 1] = getBottomFacingActive(i);
            rTextures[7][i + 1] = getBottomFacingInactive(i);
            rTextures[8][i + 1] = getBottomFacingPipeActive(i);
            rTextures[9][i + 1] = getBottomFacingPipeInactive(i);
            rTextures[10][i + 1] = getTopFacingPipeActive(i);
            rTextures[11][i + 1] = getTopFacingPipeInactive(i);
            rTextures[12][i + 1] = getSideFacingPipeActive(i);
            rTextures[13][i + 1] = getSideFacingPipeInactive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(
            final IGregTechTileEntity aBaseMetaTileEntity,
            final byte aSide,
            final byte aFacing,
            final byte aColorIndex,
            final boolean aActive,
            final boolean aRedstone) {
        return super.getTexture(aBaseMetaTileEntity, aSide, aFacing, aColorIndex, aActive, aRedstone);
        // return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0 : aSide ==
        // GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
    }

    @Override
    public ITexture[] getFrontFacingInactive(final byte aColor) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(sDehydratorOverlays[0])
        };
    }

    @Override
    public ITexture[] getBottomFacingInactive(final byte aColor) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(sDehydratorOverlays[2])
        };
    }

    @Override
    public ITexture[] getTopFacingInactive(final byte aColor) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(sDehydratorOverlays[3])
        };
    }

    @Override
    public ITexture[] getSideFacingInactive(final byte aColor) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(sDehydratorOverlays[4])
        };
    }

    @Override
    public ITexture[] getFrontFacingActive(final byte aColor) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(sDehydratorOverlays[5])
        };
    }

    @Override
    public ITexture[] getBottomFacingActive(final byte aColor) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(sDehydratorOverlays[7])
        };
    }

    @Override
    public ITexture[] getTopFacingActive(final byte aColor) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(sDehydratorOverlays[8])
        };
    }

    @Override
    public ITexture[] getSideFacingActive(final byte aColor) {
        return new ITexture[] {
            Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(sDehydratorOverlays[9])
        };
    }

    @Override
    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] {MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT)};
    }

    @Override
    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] {MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT)};
    }

    @Override
    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] {MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT)};
    }

    @Override
    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] {MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT)};
    }

    @Override
    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] {MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT)};
    }

    @Override
    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] {MACHINE_CASINGS[mTier][aColor + 1], TextureFactory.of(OVERLAY_PIPE_OUT)};
    }
}
