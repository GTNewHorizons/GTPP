package gtPlusPlus.xmod.gregtech.common.tileentities.generators.ULV;

import static gregtech.api.enums.GT_Values.V;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.tileentities.generators.GT_MetaTileEntity_GasTurbine;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GT_MetaTileEntity_ULV_GasTurbine extends GT_MetaTileEntity_GasTurbine {

    public GT_MetaTileEntity_ULV_GasTurbine(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_ULV_GasTurbine(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { this.mDescription, "Produces " + (this.getPollution() * 20) + " pollution/sec",
                "Fuel Efficiency: " + this.getEfficiency() + "%", CORE.GT_Tooltip.get() };
    }

    @Override
    public long maxEUStore() {
        return Math.max(getEUVar(), V[1] * 80L + getMinimumStoredEU());
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_ULV_GasTurbine(this.mName, this.mTier, this.mDescription, this.mTextures);
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public void onConfigLoad() {
        this.mEfficiency = GregTech_API.sMachineFile
                .get(ConfigCategories.machineconfig, "GasTurbine.efficiency.tier." + this.mTier, 95);
    }

    @Override
    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0],
                new GT_RenderedTexture((IIconContainer) TexturesGtBlock.Overlay_Machine_Turbine_Active) };
    }
}
