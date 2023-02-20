package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.*;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntity_IndustrialRockBreaker extends
        GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialRockBreaker> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialRockBreaker> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialRockBreaker(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialRockBreaker(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialRockBreaker(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Rock Breaker";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Industrial Rock Breaker")
                .addInfo("Speed: +200% | EU Usage: 75% | Parallel: Tier x 8").addInfo("Circuit goes in the GUI slot")
                .addInfo("1 = cobble, 2 = stone, 3 = obsidian").addInfo("Supply Water/Lava")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 4, 3, true)
                .addController("Bottom Center").addCasingInfo("Thermal Processing Casing", 9)
                .addCasingInfo("Thermal Containment Casing", 16).addInputBus("Any Casing", 1)
                .addInputHatch("Any Casing", 1).addOutputBus("Any Casing", 1).addEnergyHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1).addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialRockBreaker> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialRockBreaker>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" },
                                            { "HHH", "H-H", "HHH" }, { "C~C", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialRockBreaker.class)
                                    .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.GTPP_INDEX(16)).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 0))))
                    .addElement('H', ofBlock(ModBlocks.blockCasings2Misc, 11)).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        boolean aCheckPiece = checkPiece(mName, 1, 3, 0);
        boolean aCasingCount = mCasing >= 9;
        boolean aCheckHatch = checkHatch();
        log("" + aCheckPiece + ", " + aCasingCount + ", " + aCheckHatch);
        return aCheckPiece && aCasingCount && aCheckHatch;
    }

    @Override
    public String getSound() {
        return GregTech_API.sSoundList.get(208);
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
        return TAE.GTPP_INDEX(16);
    }

    private static final GT_Recipe_Map sFakeRecipeMap = new GT_Recipe_Map(
            new HashSet<>(0),
            "gt.recipe.fakerockbreaker",
            "Rock Breaker",
            "smelting",
            RES_PATH_GUI + "basicmachines/E_Furnace",
            1,
            1,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            false);

    private static void generateRecipeMap() {
        if (sRecipe_Cobblestone == null || sRecipe_SmoothStone == null || sRecipe_Redstone == null) {
            generateRecipes();
        }
        FluidStack[] aInputFluids = new FluidStack[] { FluidUtils.getWater(1000), FluidUtils.getLava(1000) };
        GT_Recipe aTemp = sRecipe_Cobblestone.copy();
        aTemp.mFluidInputs = aInputFluids;
        sFakeRecipeMap.add(aTemp);
        aTemp = sRecipe_SmoothStone.copy();
        aTemp.mFluidInputs = aInputFluids;
        sFakeRecipeMap.add(aTemp);
        aTemp = sRecipe_Redstone.copy();
        aTemp.mFluidInputs = aInputFluids;
        sFakeRecipeMap.add(aTemp);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        if (sFakeRecipeMap.mRecipeList.isEmpty()) {
            generateRecipeMap();
        }
        return sFakeRecipeMap;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        return true;
    }

    private static GT_Recipe sRecipe_Cobblestone;
    private static GT_Recipe sRecipe_SmoothStone;
    private static GT_Recipe sRecipe_Redstone;

    private static final void generateRecipes() {
        sRecipe_Cobblestone = new GTPP_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(1) },
                new ItemStack[] { ItemUtils.getSimpleStack(Blocks.cobblestone) },
                null,
                new int[] { 10000 },
                null,
                null,
                16,
                32,
                0);
        sRecipe_SmoothStone = new GTPP_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(2) },
                new ItemStack[] { ItemUtils.getSimpleStack(Blocks.stone) },
                null,
                new int[] { 10000 },
                null,
                null,
                16,
                32,
                0);
        sRecipe_Redstone = new GTPP_Recipe(
                false,
                new ItemStack[] { CI.getNumberedCircuit(3),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L) },
                new ItemStack[] { ItemUtils.getSimpleStack(Blocks.obsidian) },
                null,
                new int[] { 10000 },
                null,
                null,
                128,
                32,
                0);
    }

    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        ArrayList<FluidStack> aFluids = this.getStoredFluids();
        if (!aFluids.isEmpty()) {
            boolean aHasWater = false;
            boolean aHasLava = false;
            for (FluidStack aFluid : aFluids) {
                if (aFluid.getFluid() == FluidRegistry.WATER) {
                    aHasWater = true;
                } else if (aFluid.getFluid() == FluidRegistry.LAVA) {
                    aHasLava = true;
                }
            }
            ArrayList<ItemStack> aItems = this.getStoredInputs();
            boolean aHasRedstone = false;
            if (!aItems.isEmpty()) {
                for (ItemStack aItem : aItems) {
                    if (GT_Utility.areStacksEqual(
                            aItem,
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))) {
                        aHasRedstone = true;
                        break;
                    }
                }
            }

            if (!aHasWater || !aHasLava) {
                return false;
            }
            ItemStack aGuiCircuit = this.getGUIItemStack();
            if (aGuiCircuit == null || !ItemUtils.isControlCircuit(aGuiCircuit)) {
                return false;
            }

            if (sRecipe_Cobblestone == null || sRecipe_SmoothStone == null || sRecipe_Redstone == null) {
                generateRecipes();
            }

            int aCircuit = aGuiCircuit.getItemDamage();

            GT_Recipe tRecipe = null;
            switch (aCircuit) {
                case 1:
                    tRecipe = sRecipe_Cobblestone;
                    break;
                case 2:
                    tRecipe = sRecipe_SmoothStone;
                    break;
                case 3:
                    if (aHasRedstone) {
                        tRecipe = sRecipe_Redstone;
                    }
                    break;
            }

            if (tRecipe == null) {
                return false;
            }

            // Based on the Processing Array. A bit overkill, but very flexible.
            ItemStack[] aItemInputs = aItems.toArray(new ItemStack[aItems.size()]);
            FluidStack[] aFluidInputs = new FluidStack[] {};

            // Reset outputs and progress stats
            this.lEUt = 0;
            this.mMaxProgresstime = 0;
            this.mOutputItems = new ItemStack[] {};
            this.mOutputFluids = new FluidStack[] {};

            long tVoltage = getMaxInputVoltage();
            long tEnergy = getMaxInputEnergy();
            // Remember last recipe - an optimization for findRecipe()
            this.mLastRecipe = tRecipe;

            int aMaxParallelRecipes = getMaxParallelRecipes();
            int aEUPercent = getEuDiscountForParallelism();
            int aSpeedBonusPercent = 200;

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
                    .calculate();
            lEUt = -calculator.getConsumption();
            mMaxProgresstime = (int) Math.ceil(calculator.getDuration() * helper.getDurationMultiplier());

            mOutputItems = helper.getItemOutputs();
            mOutputFluids = helper.getFluidOutputs();
            updateSlots();

            // Play sounds (GT++ addition - GT multiblocks play no sounds)
            startProcess();
            return true;
        }

        return false;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 75;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialRockBreaker;
    }

    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return 0;
    }

    @Override
    public int getAmountOfOutputs() {
        return 2;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> aInputs = super.getStoredInputs();
        if (this.getGUIItemStack() != null) {
            aInputs.add(this.getGUIItemStack());
        }
        return aInputs;
    }
}
