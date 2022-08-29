package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

public class GregtechMTE_FrothFlotationCell extends GregtechMeta_MultiBlockBase<GregtechMTE_FrothFlotationCell>
        implements ISurvivalConstructable {

    private int mCasing;
    private IStructureDefinition<GregtechMTE_FrothFlotationCell> STRUCTURE_DEFINITION = null;

    public GregtechMTE_FrothFlotationCell(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMTE_FrothFlotationCell(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMTE_FrothFlotationCell(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Flotation Cell";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
                .addInfo("Process that milled ore!")
                .addInfo("You can only ever process one type of material per controller")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator()
                .beginStructureBlock(3, 9, 3, true)
                .addController("Front Center")
                .addCasingInfo("Inconel Reinforced Casing", 68)
                .addCasingInfo("Flotation Casing", 52)
                .addInputBus("Bottom Casing", 1)
                .addInputHatch("Bottom Casing", 1)
                .addOutputHatch("Bottom Casing", 1)
                .addEnergyHatch("Bottom Casing", 1)
                .addMaintenanceHatch("Bottom Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder);
        return tt;
    }

    @Override
    public String getSound() {
        return GregTech_API.sSoundList.get(Integer.valueOf(207));
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(2, 1);
    }

    @Override
    public boolean hasSlotInGUI() {
        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTPP_Recipe.GTPP_Recipe_Map.sFlotationCellRecipes;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 0;
    }

    @Override
    public IStructureDefinition<GregtechMTE_FrothFlotationCell> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_FrothFlotationCell>builder()
                    .addShape(mName, new String[][] {
                        {"       ", "       ", "   X   ", "  X~X  ", "   X   ", "       ", "       "},
                        {"       ", "   F   ", "  FFF  ", " FF FF ", "  FFF  ", "   F   ", "       "},
                        {"       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       "},
                        {"       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       "},
                        {"       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       "},
                        {"       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       "},
                        {"       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       "},
                        {"  CCC  ", " CCCCC ", "CCCCCCC", "CCCCCCC", "CCCCCCC", " CCCCC ", "  CCC  "},
                        {"  CCC  ", " CCCCC ", "CCCCCCC", "CCCCCCC", "CCCCCCC", " CCCCC ", "  CCC  "},
                    })
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMTE_FrothFlotationCell.class)
                                    .atLeast(InputBus, InputHatch, OutputHatch, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.GTPP_INDEX(1))
                                    .dot(1)
                                    .buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 1))))
                    .addElement('F', ofBlock(ModBlocks.blockSpecialMultiCasings, 9))
                    .addElement('X', ofBlock(ModBlocks.blockCasings3Misc, 1))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 3, 3, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 3, 3, 0) && mCasing >= 68 - 4 && checkHatch();
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiFrothFlotationCell;
    }

    @Override
    public int getAmountOfOutputs() {
        return 1;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public String getCustomGUIResourceName() {
        return null;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        // Fix GT bug
        if (this.getBaseMetaTileEntity().getFrontFacing() != 1) {
            log("Fixing Bad Facing. (GT Bug)");
            this.getBaseMetaTileEntity().setFrontFacing((byte) 1);
        }
    }

    @Override
    public boolean checkRecipe(ItemStack arg0) {
        return super.checkRecipeGeneric();
    }

    @Override
    public boolean checkRecipeGeneric(
            ItemStack[] aItemInputs,
            FluidStack[] aFluidInputs,
            int aMaxParallelRecipes,
            long aEUPercent,
            int aSpeedBonusPercent,
            int aOutputChanceRoll,
            GT_Recipe aRecipe) {
        // Based on the Processing Array. A bit overkill, but very flexible.

        // Reset outputs and progress stats
        this.mEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        long tEnergy = getMaxInputEnergy();
        log("Running checkRecipeGeneric(0)");

        GT_Recipe tRecipe = findRecipe(
                getBaseMetaTileEntity(),
                mLastRecipe,
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                aFluidInputs,
                aItemInputs);

        log("Running checkRecipeGeneric(1)");
        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        if (tRecipe == null) {
            log("BAD RETURN - 1");
            return false;
        }

        /*
         *
         * Material checks
         * Makes sure we can only ever use one type of material in this flotation cell.
         * We used to depend on Alk's hash, but it's unreliable and user-hostile
         * So we're using unlocalized name of material now.
         *
         */
        Material foundMaterial =
                FlotationRecipeHandler.getMaterialOfMilledProduct(FlotationRecipeHandler.findMilledStack(tRecipe));
        String foundMaterialName = null;
        if (foundMaterial != null) {
            foundMaterialName = foundMaterial.getUnlocalizedName();
        }

        if (foundMaterialName == null) {
            log("Did not find material from Milled Ore.");
            ItemStack milledStack = FlotationRecipeHandler.findMilledStack(tRecipe);
            if (milledStack != null) {
                log("Found stack: " + milledStack.getDisplayName());
            }
            return false;
        }

        // Set material locked for this controller
        if (lockedMaterialName == null) {
            lockedMaterialName = foundMaterialName;
        }

        // Check material match
        if (!Objects.equals(lockedMaterialName, foundMaterialName)) {
            log("Did not find the correct milled type.");
            log("Found: " + foundMaterialName);
            log("Expected: " + lockedMaterialName);
            return false;
        }

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
        // Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
        for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
            if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
                log("Broke at " + parallelRecipes + ".");
                break;
            }
            log("Bumped EU from " + tTotalEUt + " to " + (tTotalEUt + tRecipeEUt) + ".");
            tTotalEUt += tRecipeEUt;
        }

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
                this.mMaxProgresstime /= 4;
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
                    if (getBaseMetaTileEntity().getRandomNumber(aOutputChanceRoll) < tRecipe.getOutputChance(f))
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

    /*
     * Handle NBT
     */

    private String lockedMaterialName = null;

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (lockedMaterialName != null) {
            aNBT.setString("lockedMaterialName", lockedMaterialName);
        }
        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (lockedMaterialName != null) {
            aNBT.setString("lockedMaterialName", lockedMaterialName);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("lockedMaterialName", Constants.NBT.TAG_STRING)) {
            lockedMaterialName = aNBT.getString("lockedMaterialName");
        }
    }

    @Override
    public String[] getExtraInfoData() {
        return new String[] {"Locked material: " + lockedMaterialName};
    }
}
