package gtPlusPlus.xmod.forestry.bees.handler;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public enum GTPP_PropolisType {

    DRAGONBLOOD(0, "Dragon Blood", true, Utils.rgbtoHexValue(220, 20, 20));

    public boolean mShowInList;
    public Material mMaterial;
    public int mChance;
    public int mID;

    private String mName;
    private String mNameUnlocal;
    private int mColour;

    private static void map(int aId, GTPP_PropolisType aType) {
        GTPP_Bees.sPropolisMappings.put(aId, aType);
    }

    public static GTPP_PropolisType get(int aID) {
        return GTPP_Bees.sPropolisMappings.get(aID);
    }

    private GTPP_PropolisType(int aID, String aName, boolean aShow, int aColour) {
        this.mID = aID;
        this.mName = aName;
        this.mNameUnlocal = aName.toLowerCase().replaceAll(" ", "");
        this.mShowInList = aShow;
        this.mColour = aColour;
        map(aID, this);
        this.mMaterial = GTPP_Bees.sMaterialMappings.get(aName.toLowerCase().replaceAll(" ", ""));
        GT_LanguageManager.addStringLocalization("gtplusplus.propolis." + this.mNameUnlocal, this.mName + " Propolis");
    }

    public void setHidden() {
        this.mShowInList = false;
    }

    public String getName() {
        return GT_LanguageManager.getTranslation("gtplusplus.propolis." + this.mNameUnlocal);
    }

    public int getColours() {
        return mColour;
    }

    public ItemStack getStackForType(int count) {
        return new ItemStack(GTPP_Bees.propolis, count, mID);
    }
}
