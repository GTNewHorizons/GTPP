package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialDehydrator extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialDehydrator> implements ISurvivalConstructable {

    private static int CASING_TEXTURE_ID;
    private static String mCasingName = "Vacuum Casing";
    private HeatingCoilLevel mHeatingCapacity;
    private boolean mDehydratorMode = false;
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialDehydrator> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialDehydrator(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(3, 10);
    }

    public GregtechMetaTileEntity_IndustrialDehydrator(String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(3, 10);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialDehydrator(mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Factory Grade Vacuum Furnace")
                .addInfo("Can toggle the operation temperature with a Screwdriver")
                .addInfo("All Dehydrator recipes are Low Temp recipes")
                .addInfo("Speed: +120% | EU Usage: 50% | Parallel: 4")
                .addInfo("Each 900K over the min. Heat Capacity grants 5% speedup (multiplicatively)")
                .addInfo("Each 1800K over the min. Heat Capacity allows for one upgraded overclock")
                .addInfo("Upgraded overclocks reduce recipe time to 25% and increase EU/t to 400%")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 5, 3, true)
                .addController("Bottom Center").addCasingInfo(mCasingName, 5).addInputBus("Any Casing", 1)
                .addOutputBus("Any Casing", 1).addInputHatch("Any Casing", 1).addOutputHatch("Any Casing", 1)
                .addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialDehydrator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialDehydrator>builder().addShape(
                    mName,
                    transpose(
                            new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "HHH", "H-H", "HHH" },
                                    { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialDehydrator.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                                    .casingIndex(CASING_TEXTURE_ID).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings4Misc, 10))))
                    .addElement(
                            'H',
                            ofCoil(
                                    GregtechMetaTileEntity_IndustrialDehydrator::setCoilLevel,
                                    GregtechMetaTileEntity_IndustrialDehydrator::getCoilLevel))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        setCoilLevel(HeatingCoilLevel.None);
        return checkPiece(mName, 1, 4, 0) && mCasing >= 5 && getCoilLevel() != HeatingCoilLevel.None && checkHatch();
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return mDehydratorMode ? GTPP_Recipe.GTPP_Recipe_Map.sMultiblockChemicalDehydratorRecipes
                : GTPP_Recipe.GTPP_Recipe_Map.sVacuumFurnaceRecipes;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialDehydrator;
    }

    @Override
    public String getMachineType() {
        return "Vacuum Furnace / Dehydrator";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 4;
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 50;
    }

    public boolean checkRecipe(ItemStack aStack) {
        return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 120);
    }

    @Override
    public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {
        // Based on the Processing Array. A bit overkill, but very flexible.

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        long tEnergy = getMaxInputEnergy();

        GT_Recipe tRecipe = this.getRecipeMap().findRecipe(
                getBaseMetaTileEntity(),
                mLastRecipe,
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                aFluidInputs,
                aItemInputs);

        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        if (tRecipe == null || this.mHeatingCapacity.getHeat() < tRecipe.mSpecialValue) {
            return false;
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                .setFluidInputs(aFluidInputs).setAvailableEUt(tEnergy).setMaxParallel(aMaxParallelRecipes)
                .enableConsumption().enableOutputCalculation().setEUtModifier(aEUPercent / 100.0f);
        if (!mVoidExcess) {
            helper.enableVoidProtection(this);
        }

        if (mUseMultiparallelMode) {
            helper.enableBatchMode(128);
        }

        helper.build();

        if (helper.getCurrentParallel() == 0) {
            return false;
        }

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(tRecipe.mEUt).setEUt(tEnergy)
                .setDuration(tRecipe.mDuration).setEUtDiscount(aEUPercent / 100.0f)
                .setSpeedBoost(100.0f / (100.0f + aSpeedBonusPercent))
                .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplier()))
                .enableHeatOC().enableHeatDiscount().setRecipeHeat(tRecipe.mSpecialValue)
                .setMultiHeat((int) getCoilLevel().getHeat()).calculate();
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplier());

        mOutputItems = helper.getItemOutputs();
        mOutputFluids = helper.getFluidOutputs();
        updateSlots();

        // Play sounds (GT++ addition - GT multiblocks play no sounds)
        startProcess();
        return true;
    }

    @Override
    public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mDehydratorMode = Utils.invertBoolean(mDehydratorMode);
        String aMode = mDehydratorMode ? "Dehydrator" : "Vacuum Furnace";
        PlayerUtils.messagePlayer(aPlayer, "Mode: " + aMode);
        mLastRecipe = null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mDehydratorMode", mDehydratorMode);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mDehydratorMode = aNBT.getBoolean("mDehydratorMode");
    }

    public HeatingCoilLevel getCoilLevel() {
        return mHeatingCapacity;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mHeatingCapacity = aCoilLevel;
    }
}
