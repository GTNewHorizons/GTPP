package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

public class GregtechMetaTileEntity_IndustrialRockBreaker
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialRockBreaker>
        implements ISurvivalConstructable {

    private int mCasing;
    private IStructureDefinition<GregtechMetaTileEntity_IndustrialRockBreaker> STRUCTURE_DEFINITION = null;

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
        tt.addMachineType(getMachineType())
                .addInfo("Controller Block for the Industrial Rock Breaker")
                .addInfo("Speed: +200% | EU Usage: 75% | Parallel: Tier x 8")
                .addInfo("Circuit goes in the GUI slot")
                .addInfo("1 = cobble, 2 = stone, 3 = obsidian")
                .addInfo("Supply Water/Lava")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator()
                .beginStructureBlock(3, 4, 3, true)
                .addController("Bottom Center")
                .addCasingInfo("Thermal Processing Casing", 9)
                .addCasingInfo("Thermal Containment Casing", 16)
                .addInputBus("Any Casing", 1)
                .addInputHatch("Any Casing", 1)
                .addOutputBus("Any Casing", 1)
                .addEnergyHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1)
                .addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder);
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialRockBreaker> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialRockBreaker>builder()
                    .addShape(mName, transpose(new String[][] {
                        {"CCC", "CCC", "CCC"},
                        {"HHH", "H-H", "HHH"},
                        {"HHH", "H-H", "HHH"},
                        {"C~C", "CCC", "CCC"},
                    }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialRockBreaker.class)
                                    .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.GTPP_INDEX(16))
                                    .dot(1)
                                    .buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 0))))
                    .addElement('H', ofBlock(ModBlocks.blockCasings2Misc, 11))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 3, 0, elementBudget, source, actor, false, true);
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

    @Override
    public boolean hasSlotInGUI() {
        return true;
    }

    @Override
    public boolean requiresVanillaGtGUI() {
        return true;
    }

    @Override
    public String getCustomGUIResourceName() {
        return "ElectricBlastFurnace";
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
        FluidStack[] aInputFluids = new FluidStack[] {FluidUtils.getWater(1000), FluidUtils.getLava(1000)};
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
                new ItemStack[] {CI.getNumberedCircuit(1)},
                new ItemStack[] {ItemUtils.getSimpleStack(Blocks.cobblestone)},
                null,
                new int[] {10000},
                null,
                null,
                16,
                32,
                0);
        sRecipe_SmoothStone = new GTPP_Recipe(
                false,
                new ItemStack[] {CI.getNumberedCircuit(2)},
                new ItemStack[] {ItemUtils.getSimpleStack(Blocks.stone)},
                null,
                new int[] {10000},
                null,
                null,
                16,
                32,
                0);
        sRecipe_Redstone = new GTPP_Recipe(
                false,
                new ItemStack[] {
                    CI.getNumberedCircuit(3), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L)
                },
                new ItemStack[] {ItemUtils.getSimpleStack(Blocks.obsidian)},
                null,
                new int[] {10000},
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
                            aItem, GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))) {
                        aHasRedstone = true;
                        break;
                    }
                }
            }

            if (!aHasWater || !aHasLava) {
                log("BAD RETURN - 0-1");
                return false;
            }
            ItemStack aGuiCircuit = this.getGUIItemStack();
            if (aGuiCircuit == null || !ItemUtils.isControlCircuit(aGuiCircuit)) {
                log("BAD RETURN - 0-2");
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
                log("BAD RETURN - 0-3");
                return false;
            }

            // Based on the Processing Array. A bit overkill, but very flexible.
            ItemStack[] aItemInputs = aItems.toArray(new ItemStack[aItems.size()]);
            FluidStack[] aFluidInputs = new FluidStack[] {};

            // Reset outputs and progress stats
            this.mEUt = 0;
            this.mMaxProgresstime = 0;
            this.mOutputItems = new ItemStack[] {};
            this.mOutputFluids = new FluidStack[] {};

            long tVoltage = getMaxInputVoltage();
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
            long tEnergy = getMaxInputEnergy();
            log("Running checkRecipeGeneric(0)");

            log("Running checkRecipeGeneric(1)");
            // Remember last recipe - an optimization for findRecipe()
            this.mLastRecipe = tRecipe;

            int aMaxParallelRecipes = getMaxParallelRecipes();
            int aEUPercent = getEuDiscountForParallelism();
            int aSpeedBonusPercent = 200;

            aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
            if (aMaxParallelRecipes == 0) {
                log("BAD RETURN - 2");
                return false;
            }

            // EU discount
            float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
            float tTotalEUt = 0.0f;

            int parallelRecipes = 0;

            log("parallelRecipes: " + parallelRecipes);
            log("aMaxParallelRecipes: " + aMaxParallelRecipes);
            log("tTotalEUt: " + tTotalEUt);
            log("tVoltage: " + tVoltage);
            log("tRecipeEUt: " + tRecipeEUt);

            if (aItems.size() > 0 && aCircuit == 3) {
                // Count recipes to do in parallel, consuming input items and fluids and considering input voltage
                // limits
                for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
                    if (!this.depleteInput(tRecipe.mInputs[1])) {
                        break;
                    }
                    log("Bumped EU from " + tTotalEUt + " to " + (tTotalEUt + tRecipeEUt) + ".");
                    tTotalEUt += tRecipeEUt;
                }
            } else if (aCircuit >= 1 && aCircuit <= 2) {
                for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
                    log("Bumped EU from " + tTotalEUt + " to " + (tTotalEUt + tRecipeEUt) + ".");
                    tTotalEUt += tRecipeEUt;
                }
            }

            log("Broke at " + parallelRecipes + ".");
            if (parallelRecipes == 0) {
                log("BAD RETURN - 3");
                return false;
            }

            // -- Try not to fail after this point - inputs have already been consumed! --

            // Convert speed bonus to duration multiplier
            // e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
            aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
            float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
            this.mMaxProgresstime = (int) (tRecipe.mDuration * tTimeFactor);

            this.mEUt = (int) Math.ceil(tTotalEUt);

            this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            this.mEfficiencyIncrease = 10000;

            // Overclock
            if (this.mEUt <= 16) {
                this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
                this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
            } else {
                while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                    this.mEUt *= 4;
                    this.mMaxProgresstime /= 2;
                }
            }

            if (this.mEUt > 0) {
                this.mEUt = (-this.mEUt);
            }

            this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

            // Collect fluid outputs
            FluidStack[] tOutputFluids = new FluidStack[tRecipe.mFluidOutputs.length];
            for (int h = 0; h < tRecipe.mFluidOutputs.length; h++) {
                if (tRecipe.getFluidOutput(h) != null) {
                    tOutputFluids[h] = tRecipe.getFluidOutput(h).copy();
                    tOutputFluids[h].amount *= parallelRecipes;
                }
            }

            // Collect output item types
            ItemStack[] tOutputItems = new ItemStack[tRecipe.mOutputs.length];
            for (int h = 0; h < tRecipe.mOutputs.length; h++) {
                if (tRecipe.getOutput(h) != null) {
                    tOutputItems[h] = tRecipe.getOutput(h).copy();
                    tOutputItems[h].stackSize = 0;
                }
            }

            // Set output item stack sizes (taking output chance into account)
            for (int f = 0; f < tOutputItems.length; f++) {
                if (tRecipe.mOutputs[f] != null && tOutputItems[f] != null) {
                    for (int g = 0; g < parallelRecipes; g++) {
                        if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(f))
                            tOutputItems[f].stackSize += tRecipe.mOutputs[f].stackSize;
                    }
                }
            }

            tOutputItems = removeNulls(tOutputItems);

            // Sanitize item stack size, splitting any stacks greater than max stack size
            List<ItemStack> splitStacks = new ArrayList<ItemStack>();
            for (ItemStack tItem : tOutputItems) {
                while (tItem.getMaxStackSize() < tItem.stackSize) {
                    ItemStack tmp = tItem.copy();
                    tmp.stackSize = tmp.getMaxStackSize();
                    tItem.stackSize = tItem.stackSize - tItem.getMaxStackSize();
                    splitStacks.add(tmp);
                }
            }

            if (splitStacks.size() > 0) {
                ItemStack[] tmp = new ItemStack[splitStacks.size()];
                tmp = splitStacks.toArray(tmp);
                tOutputItems = ArrayUtils.addAll(tOutputItems, tmp);
            }

            // Strip empty stacks
            List<ItemStack> tSList = new ArrayList<ItemStack>();
            for (ItemStack tS : tOutputItems) {
                if (tS.stackSize > 0) tSList.add(tS);
            }
            tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);

            // Commit outputs
            this.mOutputItems = tOutputItems;
            this.mOutputFluids = tOutputFluids;
            updateSlots();

            // Play sounds (GT++ addition - GT multiblocks play no sounds)
            startProcess();

            log("GOOD RETURN - 1");
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
        if (this.hasSlotInGUI() && this.getGUIItemStack() != null) {
            aInputs.add(this.getGUIItemStack());
        }
        return aInputs;
    }
}
