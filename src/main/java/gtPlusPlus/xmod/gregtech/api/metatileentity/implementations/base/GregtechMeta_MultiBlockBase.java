package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.Mods.TecTech;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.api.helpers.GregtechPlusPlus_API.Multiblock_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.objects.minecraft.multi.SpecialMultiBehaviour;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_AirIntake;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ControlCore;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusInput;

// Glee8e - 11/12/21 - 2:15pm
// Yeah, now I see what's wrong. Someone inherited from GregtechMeta_MultiBlockBase instead of
// GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialDehydrator> as it should have been
// so any method in GregtechMetaTileEntity_IndustrialDehydrator would see generic field declared in
// GregtechMeta_MultiBlockBase without generic parameter

public abstract class GregtechMeta_MultiBlockBase<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
        extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    public static final boolean DEBUG_DISABLE_CORES_TEMPORARILY = true;

    public GT_Recipe mLastRecipe;
    private boolean mInternalCircuit = false;
    protected long mTotalRunTime = 0;

    public ArrayList<GT_MetaTileEntity_Hatch_ControlCore> mControlCoreBus = new ArrayList<>();
    /**
     * Don't use this for recipe input check, otherwise you'll get duplicated fluids
     */
    public ArrayList<GT_MetaTileEntity_Hatch_AirIntake> mAirIntakes = new ArrayList<>();

    public ArrayList<GT_MetaTileEntity_Hatch_InputBattery> mChargeHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch_OutputBattery> mDischargeHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch> mAllEnergyHatches = new ArrayList<>();
    public ArrayList<GT_MetaTileEntity_Hatch> mAllDynamoHatches = new ArrayList<>();

    // Custom Behaviour Map
    private static final HashMap<String, SpecialMultiBehaviour> mCustomBehviours = new HashMap<>();

    public GregtechMeta_MultiBlockBase(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        voidExcess = false;
    }

    public GregtechMeta_MultiBlockBase(final String aName) {
        super(aName);
        voidExcess = false;
    }

    public static boolean isValidMetaTileEntity(final MetaTileEntity aMetaTileEntity) {
        return (aMetaTileEntity.getBaseMetaTileEntity() != null)
                && (aMetaTileEntity.getBaseMetaTileEntity().getMetaTileEntity() == aMetaTileEntity)
                && !aMetaTileEntity.getBaseMetaTileEntity().isDead();
    }

    private static int toStackCount(Entry<ItemStack, Integer> e) {
        int tMaxStackSize = e.getKey().getMaxStackSize();
        int tStackSize = e.getValue();
        return (tStackSize + tMaxStackSize - 1) / tMaxStackSize;
    }

    public long getTotalRuntimeInTicks() {
        return this.mTotalRunTime;
    }

    protected float batchMultiplier = 1.0f;
    protected static final int MAX_BATCH_SIZE = 128;

    public abstract String getMachineType();

    public String getMachineTooltip() {
        return "Machine Type: " + EnumChatFormatting.YELLOW + getMachineType() + EnumChatFormatting.RESET;
    }

    public String[] getExtraInfoData() {
        return new String[0];
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> mInfo = new ArrayList<>();
        if (!this.getMetaName().equals("")) {
            mInfo.add(this.getMetaName());
        }

        String[] extra = getExtraInfoData();

        if (extra == null) {
            extra = new String[0];
        }
        if (extra.length > 0) {
            for (String s : extra) {
                mInfo.add(s);
            }
        }

        long seconds = (this.mTotalRunTime / 20);
        int weeks = (int) (TimeUnit.SECONDS.toDays(seconds) / 7);
        int days = (int) (TimeUnit.SECONDS.toDays(seconds) - 7 * weeks);
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(days) - TimeUnit.DAYS.toHours(7 * weeks);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        mInfo.add(getMachineTooltip());

        // Lets borrow the GTNH handling

        mInfo.add(
                StatCollector.translateToLocal("GTPP.multiblock.progress") + ": "
                        + EnumChatFormatting.GREEN
                        + Integer.toString(mProgresstime / 20)
                        + EnumChatFormatting.RESET
                        + " s / "
                        + EnumChatFormatting.YELLOW
                        + Integer.toString(mMaxProgresstime / 20)
                        + EnumChatFormatting.RESET
                        + " s");

        if (!this.mAllEnergyHatches.isEmpty()) {
            long storedEnergy = getStoredEnergyInAllEnergyHatches();
            long maxEnergy = getMaxEnergyStorageOfAllEnergyHatches();
            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.energy") + ":");
            mInfo.add(
                    StatCollector.translateToLocal(
                            "" + EnumChatFormatting.GREEN
                                    + Long.toString(storedEnergy)
                                    + EnumChatFormatting.RESET
                                    + " EU / "
                                    + EnumChatFormatting.YELLOW
                                    + Long.toString(maxEnergy)
                                    + EnumChatFormatting.RESET
                                    + " EU"));

            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.mei") + ":");
            mInfo.add(
                    StatCollector.translateToLocal(
                            "" + EnumChatFormatting.YELLOW
                                    + Long.toString(getMaxInputVoltage())
                                    + EnumChatFormatting.RESET
                                    + " EU/t(*2A) "
                                    + StatCollector.translateToLocal("GTPP.machines.tier")
                                    + ": "
                                    + EnumChatFormatting.YELLOW
                                    + GT_Values.VN[GT_Utility.getTier(getMaxInputVoltage())]
                                    + EnumChatFormatting.RESET));;
        }
        if (!this.mAllDynamoHatches.isEmpty()) {
            long storedEnergy = getStoredEnergyInAllDynamoHatches();
            long maxEnergy = getMaxEnergyStorageOfAllDynamoHatches();
            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.energy") + " In Dynamos:");
            mInfo.add(
                    StatCollector.translateToLocal(
                            "" + EnumChatFormatting.GREEN
                                    + Long.toString(storedEnergy)
                                    + EnumChatFormatting.RESET
                                    + " EU / "
                                    + EnumChatFormatting.YELLOW
                                    + Long.toString(maxEnergy)
                                    + EnumChatFormatting.RESET
                                    + " EU"));
        }

        if (-lEUt > 0) {
            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.usage") + ":");
            mInfo.add(
                    StatCollector.translateToLocal(
                            "" + EnumChatFormatting.RED + (-lEUt) + EnumChatFormatting.RESET + " EU/t"));
        } else {
            mInfo.add(StatCollector.translateToLocal("GTPP.multiblock.generation") + ":");
            mInfo.add(
                    StatCollector.translateToLocal(
                            "" + EnumChatFormatting.GREEN + lEUt + EnumChatFormatting.RESET + " EU/t"));
        }

        mInfo.add(
                StatCollector.translateToLocal("GTPP.multiblock.problems") + ": "
                        + EnumChatFormatting.RED
                        + (getIdealStatus() - getRepairStatus())
                        + EnumChatFormatting.RESET
                        + " "
                        + StatCollector.translateToLocal("GTPP.multiblock.efficiency")
                        + ": "
                        + EnumChatFormatting.YELLOW
                        + Float.toString(mEfficiency / 100.0F)
                        + EnumChatFormatting.RESET
                        + " %");

        if (this.getPollutionPerSecond(null) > 0) {
            int mPollutionReduction = getPollutionReductionForAllMufflers();
            mInfo.add(
                    StatCollector.translateToLocal("GTPP.multiblock.pollution") + ": "
                            + EnumChatFormatting.RED
                            + this.getPollutionPerSecond(null)
                            + EnumChatFormatting.RESET
                            + "/sec");
            mInfo.add(
                    StatCollector.translateToLocal("GTPP.multiblock.pollutionreduced") + ": "
                            + EnumChatFormatting.GREEN
                            + mPollutionReduction
                            + EnumChatFormatting.RESET
                            + " %");
        }

        if (this.mControlCoreBus.size() > 0) {
            int tTier = this.getControlCoreTier();
            mInfo.add(
                    StatCollector.translateToLocal("GTPP.CC.machinetier") + ": "
                            + EnumChatFormatting.GREEN
                            + tTier
                            + EnumChatFormatting.RESET);
        }

        mInfo.add(
                StatCollector.translateToLocal("GTPP.CC.discount") + ": "
                        + EnumChatFormatting.GREEN
                        + (getEuDiscountForParallelism())
                        + EnumChatFormatting.RESET
                        + "%");

        mInfo.add(
                StatCollector.translateToLocal("GTPP.CC.parallel") + ": "
                        + EnumChatFormatting.GREEN
                        + (getMaxParallelRecipes())
                        + EnumChatFormatting.RESET);

        mInfo.add(
                "Total Time Since Built: " + EnumChatFormatting.DARK_GREEN
                        + Integer.toString(weeks)
                        + EnumChatFormatting.RESET
                        + " Weeks, "
                        + EnumChatFormatting.DARK_GREEN
                        + Integer.toString(days)
                        + EnumChatFormatting.RESET
                        + " Days, ");
        mInfo.add(
                EnumChatFormatting.DARK_GREEN + Long.toString(hours)
                        + EnumChatFormatting.RESET
                        + " Hours, "
                        + EnumChatFormatting.DARK_GREEN
                        + Long.toString(minutes)
                        + EnumChatFormatting.RESET
                        + " Minutes, "
                        + EnumChatFormatting.DARK_GREEN
                        + Long.toString(second)
                        + EnumChatFormatting.RESET
                        + " Seconds.");
        mInfo.add("Total Time in ticks: " + EnumChatFormatting.DARK_GREEN + Long.toString(this.mTotalRunTime));

        String[] mInfo2 = mInfo.toArray(new String[mInfo.size()]);
        return mInfo2;
    }

    public int getPollutionReductionForAllMufflers() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                mPollutionReduction = Math.max(calculatePollutionReductionForHatch(tHatch, 100), mPollutionReduction);
            }
        }
        return mPollutionReduction;
    }

    public long getStoredEnergyInAllEnergyHatches() {
        long storedEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : mAllEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
            }
        }
        return storedEnergy;
    }

    public long getMaxEnergyStorageOfAllEnergyHatches() {
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : mAllEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        return maxEnergy;
    }

    public long getStoredEnergyInAllDynamoHatches() {
        long storedEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : mAllDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
            }
        }
        return storedEnergy;
    }

    public long getMaxEnergyStorageOfAllDynamoHatches() {
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : mAllDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        return maxEnergy;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    private String[] aCachedToolTip;

    /*
     * private final String aRequiresMuffler = "1x Muffler Hatch"; private final String aRequiresCoreModule =
     * "1x Core Module"; private final String aRequiresMaint = "1x Maintanence Hatch";
     */

    public static final String TAG_HIDE_HATCHES = "TAG_HIDE_HATCHES";
    public static final String TAG_HIDE_MAINT = "TAG_HIDE_MAINT";
    public static final String TAG_HIDE_POLLUTION = "TAG_HIDE_POLLUTION";
    public static final String TAG_HIDE_MACHINE_TYPE = "TAG_HIDE_MACHINE_TYPE";

    public int getAmountOfOutputs() {
        return 1;
    }

    public abstract int getMaxParallelRecipes();

    public abstract int getEuDiscountForParallelism();

    @Override
    public boolean isCorrectMachinePart(final ItemStack paramItemStack) {
        return true;
    }

    @Override
    public int getDamageToComponent(final ItemStack paramItemStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack p0) {
        return false;
    }

    @Override
    public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GT_Utility.doSoundAtClient(getSound(), 10, 1.0F, aX, aY, aZ);
        }
    }

    public void startProcess() {
        if (GT_Utility.isStringValid(getSound())) this.sendLoopStart((byte) 1);
    }

    public String getSound() {
        return "";
    }

    /**
     * A Static {@link Method} object which holds the current status of logging.
     */
    public static Method aLogger = null;

    public void log(String s) {
        if (!AsmConfig.disableAllLogging) {
            if (CORE_Preloader.DEBUG_MODE) {
                Logger.INFO(s);
            } else {
                Logger.MACHINE_INFO(s);
            }
        }
    }

    public boolean checkRecipeGeneric() {
        return checkRecipeGeneric(1, 100, 0);
    }

    public boolean checkRecipeGeneric(int aMaxParallelRecipes, long aEUPercent, int aSpeedBonusPercent) {
        return checkRecipeGeneric(aMaxParallelRecipes, aEUPercent, aSpeedBonusPercent, 10000);
    }

    public boolean checkRecipeGeneric(int aMaxParallelRecipes, long aEUPercent, int aSpeedBonusPercent,
            int aOutputChanceRoll) {
        ArrayList<ItemStack> tItems = getStoredInputs();
        ArrayList<FluidStack> tFluids = getStoredFluids();
        ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
        FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);
        return checkRecipeGeneric(
                tItemInputs,
                tFluidInputs,
                aMaxParallelRecipes,
                aEUPercent,
                aSpeedBonusPercent,
                aOutputChanceRoll);
    }

    public boolean checkRecipeGeneric(GT_Recipe aRecipe, int aMaxParallelRecipes, long aEUPercent,
            int aSpeedBonusPercent, int aOutputChanceRoll) {
        if (aRecipe == null) {
            return false;
        }
        ArrayList<ItemStack> tItems = getStoredInputs();
        ArrayList<FluidStack> tFluids = getStoredFluids();
        ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
        FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);
        return checkRecipeGeneric(
                tItemInputs,
                tFluidInputs,
                aMaxParallelRecipes,
                aEUPercent,
                aSpeedBonusPercent,
                aOutputChanceRoll,
                aRecipe);
    }

    public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {
        return checkRecipeGeneric(
                aItemInputs,
                aFluidInputs,
                aMaxParallelRecipes,
                aEUPercent,
                aSpeedBonusPercent,
                aOutputChanceRoll,
                null);
    }

    public long getMaxInputEnergy() {
        long rEnergy = 0;
        if (mEnergyHatches.size() == 1) // so it only takes 1 amp is only 1 hatch is present so it works like most gt
                                        // multies
            return mEnergyHatches.get(0).getBaseMetaTileEntity().getInputVoltage();
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
            if (isValidMetaTileEntity(tHatch)) rEnergy += tHatch.getBaseMetaTileEntity().getInputVoltage()
                    * tHatch.getBaseMetaTileEntity().getInputAmperage();
        return rEnergy;
    }

    public boolean hasPerfectOverclock() {
        return false;
    }

    public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {
        // Based on the Processing Array. A bit overkill, but very flexible.

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        long tEnergy = getMaxInputEnergy();

        GT_Recipe tRecipe = findRecipe(
                getBaseMetaTileEntity(),
                mLastRecipe,
                false,
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                aFluidInputs,
                aItemInputs);

        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        if (tRecipe == null) {
            return false;
        }

        /*
         * Check for Special Behaviours
         */

        // First populate the map if we need to.
        if (mCustomBehviours.isEmpty()) {
            mCustomBehviours.putAll(Multiblock_API.getSpecialBehaviourItemMap());
        }

        // We have a special slot object in the recipe
        if (tRecipe.mSpecialItems != null) {
            // The special slot is an item
            if (tRecipe.mSpecialItems instanceof ItemStack) {
                // Make an Itemstack instance of this.
                ItemStack aSpecialStack = (ItemStack) tRecipe.mSpecialItems;
                // Check if this item is in an input bus.
                boolean aDidFindMatch = false;
                for (ItemStack aInputItemsToCheck : aItemInputs) {
                    // If we find a matching stack, continue.
                    if (GT_Utility.areStacksEqual(aSpecialStack, aInputItemsToCheck, false)) {
                        // Iterate all special behaviour items, to see if we need to utilise one.
                        aDidFindMatch = true;
                        break;
                    }
                }
                // Try to prevent needless iteration loops if we don't have the required inputs at all.
                if (aDidFindMatch) {
                    // Iterate all special behaviour items, to see if we need to utilise one.
                    for (SpecialMultiBehaviour aBehaviours : mCustomBehviours.values()) {
                        // Found a match, let's adjust this recipe now.
                        if (aBehaviours.isTriggerItem(aSpecialStack)) {
                            // Adjust this recipe to suit special item
                            aMaxParallelRecipes = aBehaviours.getMaxParallelRecipes();
                            aEUPercent = aBehaviours.getEUPercent();
                            aSpeedBonusPercent = aBehaviours.getSpeedBonusPercent();
                            aOutputChanceRoll = aBehaviours.getOutputChanceRoll();
                            break;
                        }
                    }
                }
            }
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                .setFluidInputs(aFluidInputs).setAvailableEUt(tEnergy).setMaxParallel(aMaxParallelRecipes)
                .enableConsumption().enableOutputCalculation().setEUtModifier(aEUPercent / 100.0f);
        if (!voidExcess) {
            helper.enableVoidProtection(this);
        }

        if (batchMode) {
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

    /*
     * Here we handle recipe boosting, which grants additional output %'s to recipes that do not have 100%.
     */

    private boolean mHasBoostedCurrentRecipe = false;
    private GT_Recipe mBoostedRecipe = null;
    private ItemStack[] mInputVerificationForBoosting = null;

    /**
     * Does this machine boost it's output?
     * 
     * @return - if true, gives additional % to output chances.
     */
    protected boolean doesMachineBoostOutput() {
        return false;
    }

    private int boostOutput(int aAmount) {
        if (aAmount <= 0) {
            return 10000;
        }
        if (aAmount <= 250) {
            aAmount += MathUtils.randInt(Math.max(aAmount / 2, 1), aAmount * 2);
        } else if (aAmount <= 500) {
            aAmount += MathUtils.randInt(Math.max(aAmount / 2, 1), aAmount * 2);
        } else if (aAmount <= 750) {
            aAmount += MathUtils.randInt(Math.max(aAmount / 2, 1), aAmount * 2);
        } else if (aAmount <= 1000) {
            aAmount = (aAmount * 2);
        } else if (aAmount <= 1500) {
            aAmount = (aAmount * 2);
        } else if (aAmount <= 2000) {
            aAmount = (int) (aAmount * 1.5);
        } else if (aAmount <= 3000) {
            aAmount = (int) (aAmount * 1.5);
        } else if (aAmount <= 4000) {
            aAmount = (int) (aAmount * 1.2);
        } else if (aAmount <= 5000) {
            aAmount = (int) (aAmount * 1.2);
        } else if (aAmount <= 7000) {
            aAmount = (int) (aAmount * 1.2);
        } else if (aAmount <= 9000) {
            aAmount = (int) (aAmount * 1.1);
        }
        return Math.min(10000, aAmount);
    }

    public GT_Recipe generateAdditionalOutputForRecipe(GT_Recipe aRecipe) {
        AutoMap<Integer> aNewChances = new AutoMap<>();
        for (int chance : aRecipe.mChances) {
            aNewChances.put(boostOutput(chance));
        }
        GT_Recipe aClone = aRecipe.copy();
        int[] aTemp = new int[aNewChances.size()];
        int slot = 0;
        for (int g : aNewChances) {
            aTemp[slot] = g;
            slot++;
        }
        aClone.mChances = aTemp;
        return aClone;
    }

    /**
     * Processes recipes but provides a bonus to the output % of items if they are < 100%.
     *
     * @param aItemInputs
     * @param aFluidInputs
     * @param aMaxParallelRecipes
     * @param aEUPercent
     * @param aSpeedBonusPercent
     * @param aOutputChanceRoll
     * @param aRecipe
     * @return
     */
    public boolean checkRecipeBoostedOutputs(ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
            int aMaxParallelRecipes, long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll,
            GT_Recipe aRecipe) {

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        long tEnergy = getMaxInputEnergy();
        log("Running checkRecipeGeneric(0)");

        GT_Recipe tRecipe = aRecipe != null ? aRecipe
                : findRecipe(
                        getBaseMetaTileEntity(),
                        mLastRecipe,
                        false,
                        gregtech.api.enums.GT_Values.V[tTier],
                        aFluidInputs,
                        aItemInputs);

        log("Running checkRecipeGeneric(1)");

        // First we check whether or not we have an input cached for boosting.
        // If not, we set it to the current recipe.
        // If we do, we compare it against the current recipe, if thy are the same, we try return a boosted recipe, if
        // not, we boost a new recipe.
        boolean isRecipeInputTheSame = false;

        // No cached recipe inputs, assume first run.
        if (mInputVerificationForBoosting == null) {
            mInputVerificationForBoosting = tRecipe.mInputs;
            isRecipeInputTheSame = true;
        }
        // If the inputs match, we are good.
        else {
            if (tRecipe.mInputs == mInputVerificationForBoosting) {
                isRecipeInputTheSame = true;
            } else {
                isRecipeInputTheSame = false;
            }
        }

        // Inputs are the same, let's see if there's a boosted version.
        if (isRecipeInputTheSame) {
            // Yes, let's just set that as the recipe
            if (mHasBoostedCurrentRecipe && mBoostedRecipe != null) {
                tRecipe = mBoostedRecipe;
            }
            // We have yet to generate a new boosted recipe
            else {
                GT_Recipe aBoostedRecipe = this.generateAdditionalOutputForRecipe(tRecipe);
                if (aBoostedRecipe != null) {
                    mBoostedRecipe = aBoostedRecipe;
                    mHasBoostedCurrentRecipe = true;
                    tRecipe = mBoostedRecipe;
                }
                // Bad boost
                else {
                    mBoostedRecipe = null;
                    mHasBoostedCurrentRecipe = false;
                }
            }
        }
        // We have changed inputs, so we should generate a new boosted recipe
        else {
            GT_Recipe aBoostedRecipe = this.generateAdditionalOutputForRecipe(tRecipe);
            if (aBoostedRecipe != null) {
                mBoostedRecipe = aBoostedRecipe;
                mHasBoostedCurrentRecipe = true;
                tRecipe = mBoostedRecipe;
            }
            // Bad boost
            else {
                mBoostedRecipe = null;
                mHasBoostedCurrentRecipe = false;
            }
        }

        // Bad modify, let's just use the original recipe.
        if (!mHasBoostedCurrentRecipe || mBoostedRecipe == null) {
            tRecipe = aRecipe != null ? aRecipe
                    : findRecipe(
                            getBaseMetaTileEntity(),
                            mLastRecipe,
                            false,
                            false,
                            gregtech.api.enums.GT_Values.V[tTier],
                            aFluidInputs,
                            aItemInputs);
        }

        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        if (tRecipe == null) {
            log("BAD RETURN - 1");
            return false;
        }

        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(tRecipe).setItemInputs(aItemInputs)
                .setFluidInputs(aFluidInputs).setAvailableEUt(tEnergy).setMaxParallel(aMaxParallelRecipes)
                .enableConsumption().enableOutputCalculation();
        if (!voidExcess) {
            helper.enableVoidProtection(this);
        }

        if (batchMode) {
            helper.enableBatchMode(128);
        }

        helper.build();

        if (helper.getCurrentParallel() == 0) {
            Logger.MACHINE_INFO("BAD RETURN - 2");
            return false;
        }

        GT_OverclockCalculator calculator = new GT_OverclockCalculator().setRecipeEUt(tRecipe.mEUt).setEUt(tEnergy)
                .setDuration(tRecipe.mDuration).setEUtDiscount(aEUPercent / 100.0f)
                .setSpeedBoost(100.0f / (100.0f + aSpeedBonusPercent))
                .setParallel((int) Math.floor(helper.getCurrentParallel() / helper.getDurationMultiplier()))
                .calculate();
        lEUt = -calculator.getConsumption();
        mMaxProgresstime = (int) Math.ceil(mMaxProgresstime * helper.getDurationMultiplier());

        mOutputItems = helper.getItemOutputs();
        mOutputFluids = helper.getFluidOutputs();
        updateSlots();

        return true;
    }

    public boolean isMachineRunning() {
        boolean aRunning = this.getBaseMetaTileEntity().isActive();
        // log("Queried Multiblock is currently running: "+aRunning);
        return aRunning;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {

        // Time Counter
        if (aBaseMetaTileEntity.isServerSide()) {
            this.mTotalRunTime++;
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mChargeHatches.clear();
                this.mDischargeHatches.clear();
                this.mControlCoreBus.clear();
                this.mAirIntakes.clear();
                this.mTecTechEnergyHatches.clear();
                this.mTecTechDynamoHatches.clear();
                this.mAllEnergyHatches.clear();
                this.mAllDynamoHatches.clear();
            }
        }

        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void explodeMultiblock() {
        MetaTileEntity tTileEntity;
        for (final Iterator<GT_MetaTileEntity_Hatch_InputBattery> localIterator = this.mChargeHatches
                .iterator(); localIterator.hasNext(); tTileEntity.getBaseMetaTileEntity()
                        .doExplosion(gregtech.api.enums.GT_Values.V[8])) {
            tTileEntity = localIterator.next();
        }
        tTileEntity = null;
        for (final Iterator<GT_MetaTileEntity_Hatch_OutputBattery> localIterator = this.mDischargeHatches
                .iterator(); localIterator.hasNext(); tTileEntity.getBaseMetaTileEntity()
                        .doExplosion(gregtech.api.enums.GT_Values.V[8])) {
            tTileEntity = localIterator.next();
        }
        tTileEntity = null;
        for (final Iterator<GT_MetaTileEntity_Hatch> localIterator = this.mTecTechDynamoHatches
                .iterator(); localIterator.hasNext(); tTileEntity.getBaseMetaTileEntity()
                        .doExplosion(gregtech.api.enums.GT_Values.V[8])) {
            tTileEntity = localIterator.next();
        }
        tTileEntity = null;
        for (final Iterator<GT_MetaTileEntity_Hatch> localIterator = this.mTecTechEnergyHatches
                .iterator(); localIterator.hasNext(); tTileEntity.getBaseMetaTileEntity()
                        .doExplosion(gregtech.api.enums.GT_Values.V[8])) {
            tTileEntity = localIterator.next();
        }

        super.explodeMultiblock();
    }

    protected int getGUICircuit(ItemStack[] t) {
        Item g = CI.getNumberedCircuit(0).getItem();
        ItemStack guiSlot = this.mInventory[1];
        int mMode = -1;
        if (guiSlot != null && guiSlot.getItem() == g) {
            this.mInternalCircuit = true;
            return guiSlot.getItemDamage();
        } else {
            this.mInternalCircuit = false;
        }

        if (!this.mInternalCircuit) {
            for (ItemStack j : t) {
                if (j.getItem() == g) {
                    mMode = j.getItemDamage();
                    break;
                }
            }
        }
        return mMode;
    }

    protected ItemStack getGUIItemStack() {
        ItemStack guiSlot = this.mInventory[1];
        return guiSlot;
    }

    protected boolean setGUIItemStack(ItemStack aNewGuiSlotContents) {
        boolean result = false;
        if (this.mInventory[1] == null) {
            this.mInventory[1] = aNewGuiSlotContents != null ? aNewGuiSlotContents.copy() : null;
            this.depleteInput(aNewGuiSlotContents);
            this.updateSlots();
            result = true;
        }
        return result;
    }

    protected boolean clearGUIItemSlot() {
        return setGUIItemStack(null);
    }

    public ItemStack findItemInInventory(Item aSearchStack) {
        return findItemInInventory(aSearchStack, 0);
    }

    public ItemStack findItemInInventory(Item aSearchStack, int aMeta) {
        return findItemInInventory(ItemUtils.simpleMetaStack(aSearchStack, aMeta, 1));
    }

    public ItemStack findItemInInventory(ItemStack aSearchStack) {
        if (aSearchStack != null && this.mInputBusses.size() > 0) {
            for (GT_MetaTileEntity_Hatch_InputBus bus : this.mInputBusses) {
                if (bus != null) {
                    for (ItemStack uStack : bus.mInventory) {
                        if (uStack != null) {
                            if (aSearchStack.getClass().isInstance(uStack.getItem())) {
                                return uStack;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Deplete fluid input from a set of restricted hatches. This assumes these hatches can store nothing else but your
     * expected fluid
     */
    protected boolean depleteInputFromRestrictedHatches(Collection<GT_MetaTileEntity_Hatch_CustomFluidBase> aHatches,
            int aAmount) {
        for (final GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : aHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                FluidStack tLiquid = tHatch.getFluid();
                if (tLiquid == null || tLiquid.amount < aAmount) {
                    continue;
                }
                tLiquid = tHatch.drain(aAmount, false);
                if (tLiquid != null && tLiquid.amount >= aAmount) {
                    tLiquid = tHatch.drain(aAmount, true);
                    return tLiquid != null && tLiquid.amount >= aAmount;
                }
            }
        }
        return false;
    }

    @Override
    public void updateSlots() {
        for (final GT_MetaTileEntity_Hatch_InputBattery tHatch : this.mChargeHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                tHatch.updateSlots();
            }
        }
        for (final GT_MetaTileEntity_Hatch_OutputBattery tHatch : this.mDischargeHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                tHatch.updateSlots();
            }
        }
        super.updateSlots();
    }

    public boolean isToolCreative(ItemStack mStack) {
        Materials t1 = GT_MetaGenerated_Tool.getPrimaryMaterial(mStack);
        Materials t2 = GT_MetaGenerated_Tool.getSecondaryMaterial(mStack);
        if (t1 == Materials._NULL && t2 == Materials._NULL) {
            return true;
        }
        return false;
    }

    /**
     * Causes a Random Maint. Issue.
     * 
     * @return {@link boolean} - Returns whether or not an issue was caused, should always be true.
     */
    public boolean causeMaintenanceIssue() {
        boolean b = false;
        switch (this.getBaseMetaTileEntity().getRandomNumber(6)) {
            case 0: {
                this.mWrench = false;
                b = true;
                break;
            }
            case 1: {
                this.mScrewdriver = false;
                b = true;
                break;
            }
            case 2: {
                this.mSoftHammer = false;
                b = true;
                break;
            }
            case 3: {
                this.mHardHammer = false;
                b = true;
                break;
            }
            case 4: {
                this.mSolderingTool = false;
                b = true;
                break;
            }
            case 5: {
                this.mCrowbar = false;
                b = true;
                break;
            }
        }
        return b;
    }

    public void fixAllMaintenanceIssue() {
        this.mCrowbar = true;
        this.mWrench = true;
        this.mHardHammer = true;
        this.mSoftHammer = true;
        this.mSolderingTool = true;
        this.mScrewdriver = true;
    }

    public boolean checkHatch() {
        return mMaintenanceHatches.size() <= 1
                && (this.getPollutionPerSecond(null) > 0 ? !mMufflerHatches.isEmpty() : true);
    }

    public <E> boolean addToMachineListInternal(ArrayList<E> aList, final IGregTechTileEntity aTileEntity,
            final int aBaseCasingIndex) {
        return addToMachineListInternal(aList, getMetaTileEntity(aTileEntity), aBaseCasingIndex);
    }

    public <E> boolean addToMachineListInternal(ArrayList<E> aList, final IMetaTileEntity aTileEntity,
            final int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }

        // Check type
        /*
         * Class <?> aHatchType = ReflectionUtils.getTypeOfGenericObject(aList); if
         * (!aHatchType.isInstance(aTileEntity)) { return false; }
         */

        // Try setRecipeMap

        try {
            if (aTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                resetRecipeMapForHatch((GT_MetaTileEntity_Hatch) aTileEntity, getRecipeMap());
            }
            if (aTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                resetRecipeMapForHatch((GT_MetaTileEntity_Hatch) aTileEntity, getRecipeMap());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (aList.isEmpty()) {
            if (aTileEntity instanceof GT_MetaTileEntity_Hatch) {
                if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
                    log(
                            "Adding " + aTileEntity.getInventoryName()
                                    + " at "
                                    + new BlockPos(aTileEntity.getBaseMetaTileEntity()).getLocationString());
                }
                updateTexture(aTileEntity, aBaseCasingIndex);
                return aList.add((E) aTileEntity);
            }
        } else {
            IGregTechTileEntity aCur = aTileEntity.getBaseMetaTileEntity();
            if (aList.contains(aTileEntity)) {
                log(
                        "Found Duplicate " + aTileEntity.getInventoryName()
                                + " @ "
                                + new BlockPos(aCur).getLocationString());
                return false;
            }
            BlockPos aCurPos = new BlockPos(aCur);
            boolean aExists = false;
            for (E m : aList) {
                IGregTechTileEntity b = ((IMetaTileEntity) m).getBaseMetaTileEntity();
                if (b != null) {
                    BlockPos aPos = new BlockPos(b);
                    if (aPos != null) {
                        if (aCurPos.equals(aPos)) {
                            if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
                                log("Found Duplicate " + b.getInventoryName() + " at " + aPos.getLocationString());
                            }
                            return false;
                        }
                    }
                }
            }
            if (aTileEntity instanceof GT_MetaTileEntity_Hatch) {
                if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
                    log("Adding " + aCur.getInventoryName() + " at " + aCurPos.getLocationString());
                }
                updateTexture(aTileEntity, aBaseCasingIndex);
                return aList.add((E) aTileEntity);
            }
        }
        return false;
    }

    public int getControlCoreTier() {

        // Always return best tier if config is off.
        /*
         * boolean aCoresConfig = gtPlusPlus.core.lib.CORE.ConfigSwitches.requireControlCores; if (!aCoresConfig) {
         * return 10; }
         */

        if (mControlCoreBus.isEmpty()) {
            log("No Control Core Modules Found.");
            return 0;
        }
        GT_MetaTileEntity_Hatch_ControlCore i = getControlCoreBus();
        if (i != null) {
            ItemStack x = i.mInventory[0];
            if (x != null) {
                return x.getItemDamage();
            }
        }
        log("Control Core Module was null.");
        return 0;
    }

    public GT_MetaTileEntity_Hatch_ControlCore getControlCoreBus() {
        if (this.mControlCoreBus == null || this.mControlCoreBus.isEmpty()) {
            return null;
        }
        GT_MetaTileEntity_Hatch_ControlCore x = this.mControlCoreBus.get(0);
        if (x != null) {
            log("getControlCore(ok)");
            return x;
        }
        log("getControlCore(bad)");
        return null;
    }

    // mControlCoreBus
    public boolean addControlCoreToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        if (!mControlCoreBus.isEmpty()) {
            log("Tried to add a secondary control core module.");
            return false;
        }
        GT_MetaTileEntity_Hatch_ControlCore Module = (GT_MetaTileEntity_Hatch_ControlCore) getMetaTileEntity(
                aTileEntity);
        if (Module != null) {
            if (Module.setOwner(aTileEntity)) {
                log("Adding control core module.");
                return addToMachineListInternal(mControlCoreBus, Module, aBaseCasingIndex);
            }
        }
        return false;
    }

    private IMetaTileEntity getMetaTileEntity(final IGregTechTileEntity aTileEntity) {
        if (aTileEntity == null) {
            return null;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        return aMetaTileEntity;
    }

    @Override
    public boolean addToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        return addToMachineList(getMetaTileEntity(aTileEntity), aBaseCasingIndex);
    }

    public boolean addToMachineList(final IMetaTileEntity aMetaTileEntity, final int aBaseCasingIndex) {
        if (aMetaTileEntity == null) {
            return false;
        }

        // Use this to determine the correct value, then update the hatch texture after.
        boolean aDidAdd = false;

        // Handle Custom Hatches
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_ControlCore) {
            log("Found GT_MetaTileEntity_Hatch_ControlCore");
            if (!mControlCoreBus.isEmpty()) {
                log("Tried to add a secondary control core module.");
                return false;
            }
            aDidAdd = addToMachineListInternal(this.mControlCoreBus, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery) {
            log("Found GT_MetaTileEntity_Hatch_InputBattery");
            aDidAdd = addToMachineListInternal(mChargeHatches, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery) {
            log("Found GT_MetaTileEntity_Hatch_OutputBattery");
            aDidAdd = addToMachineListInternal(mDischargeHatches, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_AirIntake) {
            aDidAdd = addToMachineListInternal(mAirIntakes, aMetaTileEntity, aBaseCasingIndex)
                    && addToMachineListInternal(mInputHatches, aMetaTileEntity, aBaseCasingIndex);
        }

        // Handle TT Multi-A Energy Hatches
        else if (TecTech.isModLoaded() && isThisHatchMultiEnergy(aMetaTileEntity)) {
            log("Found isThisHatchMultiEnergy");
            aDidAdd = addToMachineListInternal(mTecTechEnergyHatches, aMetaTileEntity, aBaseCasingIndex);
            updateMasterEnergyHatchList(aMetaTileEntity);
        }

        // Handle TT Multi-A Dynamos
        else if (TecTech.isModLoaded() && isThisHatchMultiDynamo(aMetaTileEntity)) {
            log("Found isThisHatchMultiDynamo");
            aDidAdd = addToMachineListInternal(mTecTechDynamoHatches, aMetaTileEntity, aBaseCasingIndex);
            updateMasterDynamoHatchList(aMetaTileEntity);
        }

        // Handle Fluid Hatches using seperate logic
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)
            aDidAdd = addToMachineListInternal(mInputHatches, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            aDidAdd = addToMachineListInternal(mOutputHatches, aMetaTileEntity, aBaseCasingIndex);

        // Process Remaining hatches using Vanilla GT Logic
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
            aDidAdd = addToMachineListInternal(mInputBusses, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            aDidAdd = addToMachineListInternal(mOutputBusses, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            aDidAdd = addToMachineListInternal(mEnergyHatches, aMetaTileEntity, aBaseCasingIndex);
            updateMasterEnergyHatchList(aMetaTileEntity);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo) {
            aDidAdd = addToMachineListInternal(mDynamoHatches, aMetaTileEntity, aBaseCasingIndex);
            updateMasterDynamoHatchList(aMetaTileEntity);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            aDidAdd = addToMachineListInternal(mMaintenanceHatches, aMetaTileEntity, aBaseCasingIndex);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler)
            aDidAdd = addToMachineListInternal(mMufflerHatches, aMetaTileEntity, aBaseCasingIndex);

        // return super.addToMachineList(aTileEntity, aBaseCasingIndex);
        return aDidAdd;
    }

    @Override
    public boolean addMaintenanceToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    @Override
    public boolean addMufflerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    @Override
    public boolean addInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input
                || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output
                || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean addAirIntakeToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_AirIntake) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean addFluidInputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        return addFluidInputToMachineList(getMetaTileEntity(aTileEntity), aBaseCasingIndex);
    }

    public boolean addFluidInputToMachineList(final IMetaTileEntity aMetaTileEntity, final int aBaseCasingIndex) {
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean clearRecipeMapForAllInputHatches() {
        return resetRecipeMapForAllInputHatches(null);
    }

    public boolean resetRecipeMapForAllInputHatches() {
        return resetRecipeMapForAllInputHatches(this.getRecipeMap());
    }

    public boolean resetRecipeMapForAllInputHatches(GT_Recipe_Map aMap) {
        int cleared = 0;
        for (GT_MetaTileEntity_Hatch_Input g : this.mInputHatches) {
            if (resetRecipeMapForHatch(g, aMap)) {
                cleared++;
            }
        }
        for (GT_MetaTileEntity_Hatch_InputBus g : this.mInputBusses) {
            if (resetRecipeMapForHatch(g, aMap)) {
                cleared++;
            }
        }
        return cleared > 0;
    }

    public boolean resetRecipeMapForHatch(IGregTechTileEntity aTileEntity, GT_Recipe_Map aMap) {
        try {
            final IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
            if (aMetaTileEntity == null) {
                return false;
            }
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input
                    || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus
                    || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Steam_BusInput) {
                return resetRecipeMapForHatch((GT_MetaTileEntity_Hatch) aMetaTileEntity, aMap);
            } else {
                return false;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public boolean resetRecipeMapForHatch(GT_MetaTileEntity_Hatch aTileEntity, GT_Recipe_Map aMap) {
        if (aTileEntity == null) {
            return false;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input
                || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus
                || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Steam_BusInput) {
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = null;
                ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = aMap;
                if (aMap != null && aMap.mNEIName != null) {
                    log("Remapped Input Hatch to " + aMap.mNEIName + ".");
                } else {
                    log("Cleared Input Hatch.");
                }
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
                ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = null;
                ((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = aMap;
                if (aMap != null && aMap.mNEIName != null) {
                    log("Remapped Input Bus to " + aMap.mNEIName + ".");
                } else {
                    log("Cleared Input Bus.");
                }
            } else {
                ((GT_MetaTileEntity_Hatch_Steam_BusInput) aMetaTileEntity).mRecipeMap = null;
                ((GT_MetaTileEntity_Hatch_Steam_BusInput) aMetaTileEntity).mRecipeMap = aMap;
                if (aMap != null && aMap.mNEIName != null) {
                    log("Remapped Input Bus to " + aMap.mNEIName + ".");
                } else {
                    log("Cleared Input Bus.");
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
        clearRecipeMapForAllInputHatches();
        onModeChangeByScrewdriver(aSide, aPlayer, aX, aY, aZ);
        resetRecipeMapForAllInputHatches();
    }

    public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {}

    /**
     * Enable Texture Casing Support if found in GT 5.09
     */
    public boolean updateTexture(final IGregTechTileEntity aTileEntity, int aCasingID) {
        return updateTexture(getMetaTileEntity(aTileEntity), aCasingID);
    }

    /**
     * Enable Texture Casing Support if found in GT 5.09
     */
    @SuppressWarnings("deprecation")
    public boolean updateTexture(final IMetaTileEntity aTileEntity, int aCasingID) {
        try { // gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch.updateTexture(int)

            final IMetaTileEntity aMetaTileEntity = aTileEntity;
            if (aMetaTileEntity == null) {
                return false;
            }
            Method mProper = ReflectionUtils.getMethod(GT_MetaTileEntity_Hatch.class, "updateTexture", int.class);
            if (mProper != null) {
                if (GT_MetaTileEntity_Hatch.class.isInstance(aMetaTileEntity)) {
                    mProper.setAccessible(true);
                    mProper.invoke(aMetaTileEntity, aCasingID);
                    // log("Good Method Call for updateTexture.");
                    return true;
                }
            } else {
                log("Bad Method Call for updateTexture.");
                if (GT_MetaTileEntity_Hatch.class.isInstance(aMetaTileEntity)) {
                    if (aCasingID <= Byte.MAX_VALUE) {
                        ((GT_MetaTileEntity_Hatch) aTileEntity).mMachineBlock = (byte) aCasingID;
                        log(
                                "Good Method Call for updateTexture. Used fallback method of setting mMachineBlock as casing id was <= 128.");
                        return true;
                    } else {
                        log("updateTexture returning false. 1.2");
                    }
                } else {
                    log("updateTexture returning false. 1.3");
                }
            }
            log("updateTexture returning false. 1");
            return false;
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log("updateTexture returning false.");
            log("updateTexture returning false. 2");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * TecTech Support
     */

    /**
     * This is the array Used to Store the Tectech Multi-Amp Dynamo hatches.
     */
    public ArrayList<GT_MetaTileEntity_Hatch> mTecTechDynamoHatches = new ArrayList<>();

    /**
     * This is the array Used to Store the Tectech Multi-Amp Energy hatches.
     */
    public ArrayList<GT_MetaTileEntity_Hatch> mTecTechEnergyHatches = new ArrayList<>();

    /**
     * TecTech Multi-Amp Dynamo Support
     * 
     * @param aTileEntity      - The Dynamo Hatch
     * @param aBaseCasingIndex - Casing Texture
     * @return
     */
    public boolean addMultiAmpDynamoToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        final IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity == null) {
            return false;
        }
        if (isThisHatchMultiDynamo(aTileEntity)) {
            return addToMachineListInternal(mTecTechDynamoHatches, aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean isThisHatchMultiDynamo(IGregTechTileEntity aTileEntity) {
        return isThisHatchMultiDynamo(getMetaTileEntity(aTileEntity));
    }

    public boolean isThisHatchMultiDynamo(IMetaTileEntity aMetaTileEntity) {
        Class<?> mDynamoClass;
        mDynamoClass = ReflectionUtils
                .getClass("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti");
        if (mDynamoClass != null) {
            if (mDynamoClass.isInstance(aMetaTileEntity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo || isThisHatchMultiDynamo(aMetaTileEntity)) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    private boolean updateMasterDynamoHatchList(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            GT_MetaTileEntity_Hatch aHatch = (GT_MetaTileEntity_Hatch) aMetaTileEntity;
            return mAllDynamoHatches.add(aHatch);
        }
        return false;
    }

    /**
     * TecTech Multi-Amp Energy Hatch Support
     * 
     * @param aTileEntity      - The Energy Hatch
     * @param aBaseCasingIndex - Casing Texture
     * @return
     */
    public boolean addMultiAmpEnergyToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        final IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity == null) {
            return false;
        }
        if (isThisHatchMultiEnergy(aMetaTileEntity)) {
            return addToMachineListInternal(mTecTechEnergyHatches, aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    public boolean isThisHatchMultiEnergy(IGregTechTileEntity aTileEntity) {
        return isThisHatchMultiEnergy(getMetaTileEntity(aTileEntity));
    }

    public boolean isThisHatchMultiEnergy(IMetaTileEntity aMetaTileEntity) {
        Class<?> mDynamoClass;
        mDynamoClass = ReflectionUtils
                .getClass("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti");
        if (mDynamoClass != null) {
            if (mDynamoClass.isInstance(aMetaTileEntity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        IMetaTileEntity aMetaTileEntity = getMetaTileEntity(aTileEntity);
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy || isThisHatchMultiEnergy(aMetaTileEntity)) {
            return addToMachineList(aMetaTileEntity, aBaseCasingIndex);
        }
        return false;
    }

    private boolean updateMasterEnergyHatchList(IMetaTileEntity aMetaTileEntity) {
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch) {
            GT_MetaTileEntity_Hatch aHatch = (GT_MetaTileEntity_Hatch) aMetaTileEntity;
            return mAllEnergyHatches.add(aHatch);
        }
        return false;
    }

    /**
     * Pollution Management
     */
    private static Method calculatePollutionReduction = null;

    public int calculatePollutionReductionForHatch(GT_MetaTileEntity_Hatch_Muffler i, int g) {
        if (calculatePollutionReduction != null) {
            try {
                return (int) calculatePollutionReduction.invoke(i, g);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

            }
        }
        return 0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setLong("mTotalRunTime", this.mTotalRunTime);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mTotalRunTime = aNBT.getLong("mTotalRunTime");
        if (!aNBT.hasKey(VOID_EXCESS_NBT_KEY)) {
            voidExcess = aNBT.getBoolean("mVoidExcess");
        }
        if (!aNBT.hasKey(BATCH_MODE_NBT_KEY)) {
            batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
    }

    /**
     * Custom Find Recipe with Debugging
     */
    public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated,
            final boolean aDontCheckStackSizes, final long aVoltage, final FluidStack[] aFluids,
            final ItemStack... aInputs) {
        return this.findRecipe(
                aTileEntity,
                null,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                (ItemStack) null,
                aInputs);
    }

    public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated,
            final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
        return this.findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, (ItemStack) null, aInputs);
    }

    public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
            final boolean aNotUnificated, final boolean aDontCheckStackSizes, final long aVoltage,
            final FluidStack[] aFluids, final ItemStack... aInputs) {
        return this.findRecipe(
                aTileEntity,
                aRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                (ItemStack) null,
                aInputs);
    }

    public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
            final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
        return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, (ItemStack) null, aInputs);
    }

    public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
            final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack aSpecialSlot,
            final ItemStack... aInputs) {
        return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, true, aVoltage, aFluids, aSpecialSlot, aInputs);
    }

    public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
            final boolean aNotUnificated, final boolean aDontCheckStackSizes, final long aVoltage,
            final FluidStack[] aFluids, final ItemStack aSpecialSlot, ItemStack... aInputs) {
        if (this.getRecipeMap().mRecipeList.isEmpty()) {
            log("No Recipes in Map to search through.");
            return null;
        }
        GT_Recipe mRecipeResult = null;
        try {
            if (GregTech_API.sPostloadFinished) {
                if (this.getRecipeMap().mMinimalInputFluids > 0) {
                    if (aFluids == null) {
                        log("aFluids == null && minFluids > 0");
                        return null;
                    }
                    int tAmount = 0;
                    for (final FluidStack aFluid : aFluids) {
                        if (aFluid != null) {
                            ++tAmount;
                        }
                    }
                    if (tAmount < this.getRecipeMap().mMinimalInputFluids) {
                        log("Not enough fluids?");
                        return null;
                    }
                }
                if (this.getRecipeMap().mMinimalInputItems > 0) {
                    if (aInputs == null) {
                        log("No inputs and minItems > 0");
                        return null;
                    }
                    int tAmount = 0;
                    for (final ItemStack aInput : aInputs) {
                        if (aInput != null) {
                            ++tAmount;
                        }
                    }
                    if (tAmount < this.getRecipeMap().mMinimalInputItems) {
                        log("Not enough items?");
                        return null;
                    }
                }
            } else {
                log("Game Not Loaded properly for recipe lookup.");
            }
            if (aNotUnificated) {
                aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
            }
            if (aRecipe != null && !aRecipe.mFakeRecipe
                    && aRecipe.mCanBeBuffered
                    && aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                mRecipeResult = (aRecipe.mEnabled /* && aVoltage * this.getRecipeMap().mAmperage >= aRecipe.mEUt */)
                        ? aRecipe
                        : null;
                log("x) Found Recipe? " + (mRecipeResult != null ? "true" : "false"));
                if (mRecipeResult != null) {
                    return mRecipeResult;
                }
            }
            if (mRecipeResult == null && this.getRecipeMap().mUsualInputCount >= 0
                    && aInputs != null
                    && aInputs.length > 0) {
                for (final ItemStack tStack : aInputs) {
                    if (tStack != null) {
                        Collection<GT_Recipe> tRecipes = this.getRecipeMap().mRecipeItemMap
                                .get(new GT_ItemStack(tStack));
                        if (tRecipes != null) {
                            for (final GT_Recipe tRecipe : tRecipes) {
                                if (!tRecipe.mFakeRecipe
                                        && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                                    mRecipeResult = (tRecipe.mEnabled /*
                                                                       * && aVoltage * this.getRecipeMap().mAmperage >=
                                                                       * tRecipe.mEUt
                                                                       */) ? tRecipe : null;
                                    log("1) Found Recipe? " + (mRecipeResult != null ? "true" : "false"));
                                    // return mRecipeResult;
                                }
                            }
                        }

                        // TODO - Investigate if this requires to be in it's own block
                        tRecipes = this.getRecipeMap().mRecipeItemMap
                                .get(new GT_ItemStack(GT_Utility.copyMetaData(32767L, new Object[] { tStack })));
                        if (tRecipes != null) {
                            for (final GT_Recipe tRecipe : tRecipes) {
                                if (!tRecipe.mFakeRecipe
                                        && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                                    mRecipeResult = (tRecipe.mEnabled /*
                                                                       * && aVoltage * this.getRecipeMap().mAmperage >=
                                                                       * tRecipe.mEUt
                                                                       */) ? tRecipe : null;
                                    log("2) Found Recipe? " + (mRecipeResult != null ? "true" : "false"));
                                    // return mRecipeResult;
                                }
                            }
                        }
                    }
                }
            }
            if (mRecipeResult == null && this.getRecipeMap().mMinimalInputItems == 0
                    && aFluids != null
                    && aFluids.length > 0) {
                for (final FluidStack aFluid2 : aFluids) {
                    if (aFluid2 != null) {
                        final Collection<GT_Recipe> tRecipes = this.getRecipeMap().mRecipeFluidMap
                                .get(aFluid2.getFluid());
                        if (tRecipes != null) {
                            for (final GT_Recipe tRecipe : tRecipes) {
                                if (!tRecipe.mFakeRecipe
                                        && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                                    mRecipeResult = (tRecipe.mEnabled /*
                                                                       * && aVoltage * this.getRecipeMap().mAmperage >=
                                                                       * tRecipe.mEUt
                                                                       */) ? tRecipe : null;
                                    log("3) Found Recipe? " + (mRecipeResult != null ? "true" : "false"));
                                    // return mRecipeResult;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            log("Invalid recipe lookup.");
        }

        if (mRecipeResult == null) {
            log(
                    "Invalid recipe, Fallback lookup. " + this.getRecipeMap().mRecipeList.size()
                            + " | "
                            + this.getRecipeMap().mNEIName);
            return getRecipeMap().findRecipe(
                    aTileEntity,
                    aRecipe,
                    aNotUnificated,
                    aDontCheckStackSizes,
                    aVoltage,
                    aFluids,
                    aSpecialSlot,
                    aInputs);
        } else {
            return mRecipeResult;
        }
    }

    /**
     * Custom Tool Handling
     */
    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX,
            float aY, float aZ) {
        // Do Things
        if (this.getBaseMetaTileEntity().isServerSide()) {
            // Logger.INFO("Right Clicked Controller.");
            ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
            if (tCurrentItem != null) {
                // Logger.INFO("Holding Item.");
                if (tCurrentItem.getItem() instanceof GT_MetaGenerated_Tool) {
                    // Logger.INFO("Is GT_MetaGenerated_Tool.");
                    int[] aOreID = OreDictionary.getOreIDs(tCurrentItem);
                    for (int id : aOreID) {
                        // Plunger
                        if (OreDictionary.getOreName(id).equals("craftingToolPlunger")) {
                            // Logger.INFO("Is Plunger.");
                            return onPlungerRightClick(aPlayer, aSide, aX, aY, aZ);
                        }
                    }
                }
            }
        }
        // Do Super
        boolean aSuper = super.onRightclick(aBaseMetaTileEntity, aPlayer, aSide, aX, aY, aZ);
        return aSuper;
    }

    public boolean onPlungerRightClick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
        int aHatchIndex = 0;
        PlayerUtils.messagePlayer(aPlayer, "Trying to clear " + mOutputHatches.size() + " output hatches.");
        for (GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
            if (hatch.mFluid != null) {
                PlayerUtils.messagePlayer(
                        aPlayer,
                        "Clearing " + hatch.mFluid.amount
                                + "L of "
                                + hatch.mFluid.getLocalizedName()
                                + " from hatch "
                                + aHatchIndex
                                + ".");
                hatch.mFluid = null;
            }
            aHatchIndex++;
        }
        return aHatchIndex > 0;
    }

    @Override
    public boolean onSolderingToolRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
            float aZ) {
        boolean tSuper = super.onSolderingToolRightClick(aSide, aWrenchingSide, aPlayer, aX, aY, aZ);
        if (aPlayer.isSneaking()) return tSuper;
        voidExcess = !voidExcess;
        aPlayer.addChatMessage(
                new ChatComponentTranslation(
                        voidExcess ? "interaction.voidexcess.enabled" : "interaction.voidexcess.disabled"));
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(byte aSide, byte aWrenchingSide, EntityPlayer aPlayer, float aX, float aY,
            float aZ) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
            }
            return true;
        }
        return false;
    }

    public boolean isValidBlockForStructure(IGregTechTileEntity aBaseMetaTileEntity, int aCasingID, boolean canBeHatch,
            Block aFoundBlock, int aFoundMeta, Block aExpectedBlock, int aExpectedMeta) {
        boolean isHatch = false;
        if (aBaseMetaTileEntity != null) {

            // Unsure why this check exists?
            /*
             * if (aCasingID < 64) { aCasingID = TAE.GTPP_INDEX(aCasingID); }
             */

            isHatch = this.addToMachineList(aBaseMetaTileEntity, aCasingID);
            if (isHatch) {
                return true;
            } else {
                int aMetaTileID = aBaseMetaTileEntity.getMetaTileID();
                // Found a controller
                if (aMetaTileID >= 750 && aMetaTileID < 1000 && aFoundBlock == GregTech_API.sBlockMachines) {
                    return true;
                }
                // Vanilla Hatches/Busses
                else if (aMetaTileID >= 10 && aMetaTileID <= 99 && aFoundBlock == GregTech_API.sBlockMachines) {
                    return true;
                }
                // Adv Mufflers
                else if (aMetaTileID >= 30001 && aMetaTileID <= 30009 && aFoundBlock == GregTech_API.sBlockMachines) {
                    return true;
                }
                // Control Core, Super IO
                else if (aMetaTileID >= 30020 && aMetaTileID <= 30040 && aFoundBlock == GregTech_API.sBlockMachines) {
                    return true;
                }
                // Auto maint
                else if (aMetaTileID == 111 && aFoundBlock == GregTech_API.sBlockMachines) {
                    return true;
                }
                // Data Ports
                else if ((aMetaTileID == 131 || aMetaTileID == 132) && aFoundBlock == GregTech_API.sBlockMachines) {
                    return true;
                } else {
                    log("Found meta Tile: " + aMetaTileID);
                }
            }
        }
        if (!isHatch) {
            if (aFoundBlock == aExpectedBlock && aFoundMeta == aExpectedMeta) {
                return true;
            } else if (aFoundBlock != aExpectedBlock) {
                if (GTplusplus.CURRENT_LOAD_PHASE == INIT_PHASE.STARTED) {
                    log(
                            "A1 - Found: " + aFoundBlock.getLocalizedName()
                                    + ":"
                                    + aFoundMeta
                                    + ", Expected: "
                                    + aExpectedBlock.getLocalizedName()
                                    + ":"
                                    + aExpectedMeta);
                    // log("Loc: "+(new BlockPos(aBaseMetaTileEntity).getLocationString()));
                }
                return false;
            } else if (aFoundMeta != aExpectedMeta) {
                log("A2");
                return false;
            }
        }
        log("A3");
        return false;
    }

    @Override
    public void onServerStart() {
        super.onServerStart();
        tryTickWaitTimerDown();
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        tryTickWaitTimerDown();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        tryTickWaitTimerDown();
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        super.onCreated(aStack, aWorld, aPlayer);
        tryTickWaitTimerDown();
    }

    private final void tryTickWaitTimerDown() {
        /*
         * if (mStartUpCheck > 10) { mStartUpCheck = 10; }
         */
    }

    // Only support to use meta to tier

    /**
     * accept meta [0, maxMeta)
     * 
     * @param maxMeta exclusive
     */
    public static <T> IStructureElement<T> addTieredBlock(Block aBlock, BiConsumer<T, Integer> aSetTheMeta,
            Function<T, Integer> aGetTheMeta, int maxMeta) {
        return addTieredBlock(aBlock, (t, i) -> {
            aSetTheMeta.accept(t, i);
            return true;
        }, aGetTheMeta, 0, maxMeta);
    }

    /**
     *
     * @param minMeta inclusive
     * @param maxMeta exclusive
     */
    public static <T> IStructureElement<T> addTieredBlock(Block aBlock, BiConsumer<T, Integer> aSetTheMeta,
            Function<T, Integer> aGetTheMeta, int minMeta, int maxMeta) {
        return addTieredBlock(aBlock, (t, i) -> {
            aSetTheMeta.accept(t, i);
            return true;
        }, aGetTheMeta, minMeta, maxMeta);
    }

    /**
     *
     * @param minMeta inclusive
     * @param maxMeta exclusive
     */
    public static <T> IStructureElement<T> addTieredBlock(Block aBlock, BiPredicate<T, Integer> aSetTheMeta,
            Function<T, Integer> aGetTheMeta, int minMeta, int maxMeta) {

        return new IStructureElement<T>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                Block tBlock = world.getBlock(x, y, z);
                if (aBlock == tBlock) {
                    Integer currentMeta = aGetTheMeta.apply(t);
                    int newMeta = tBlock.getDamageValue(world, x, y, z) + 1;
                    if (newMeta > maxMeta || newMeta < minMeta + 1) return false;
                    if (currentMeta == 0) {
                        return aSetTheMeta.test(t, newMeta);
                    } else {
                        return currentMeta == newMeta;
                    }
                }
                return false;
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, aBlock, getMeta(trigger));
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                return world.setBlock(x, y, z, aBlock, getMeta(trigger), 3);
            }

            private int getMeta(ItemStack trigger) {
                int meta = trigger.stackSize;
                if (meta <= 0) meta = minMeta;
                if (meta + minMeta >= maxMeta) meta = maxMeta - 1 - minMeta;
                return meta + minMeta;
            }

            @Nullable
            @Override
            public BlocksToPlace getBlocksToPlace(T t, World world, int x, int y, int z, ItemStack trigger,
                    AutoPlaceEnvironment env) {
                return BlocksToPlace.create(aBlock, getMeta(trigger));
            }

            @Override
            public PlaceResult survivalPlaceBlock(T t, World world, int x, int y, int z, ItemStack trigger,
                    AutoPlaceEnvironment env) {
                if (world.getBlock(x, y, z) == aBlock) {
                    if (world.getBlockMetadata(x, y, z) == getMeta(trigger)) {
                        return PlaceResult.SKIP;
                    }
                    return PlaceResult.REJECT;
                }
                return StructureUtility.survivalPlaceBlock(
                        aBlock,
                        getMeta(trigger),
                        world,
                        x,
                        y,
                        z,
                        env.getSource(),
                        env.getActor(),
                        env.getChatter());
            }
        };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive) return new ITexture[] { getCasingTexture(),
                    TextureFactory.builder().addIcon(getActiveOverlay()).extFacing().build() };
            return new ITexture[] { getCasingTexture(),
                    TextureFactory.builder().addIcon(getInactiveOverlay()).extFacing().build() };
        }
        return new ITexture[] { getCasingTexture() };
    }

    protected IIconContainer getActiveOverlay() {
        return null;
    }

    protected IIconContainer getInactiveOverlay() {
        return null;
    }

    protected ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(getCasingTextureId());
    }

    protected int getCasingTextureId() {
        return 0;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        if (doesBindPlayerInventory()) {
            super.addUIWidgets(builder, buildContext);
        } else {
            addNoPlayerInventoryUI(builder, buildContext);
        }
    }

    private static final Materials GOOD = Materials.Uranium;
    private static final Materials BAD = Materials.Plutonium;
    private static final ConcurrentHashMap<String, ItemStack> mToolStacks = new ConcurrentHashMap<>();

    @Override
    protected boolean isVoidExcessButtonEnabled() {
        return true;
    }

    @Override
    protected boolean isBatchModeButtonEnabled() {
        return true;
    }

    protected void addNoPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK).setPos(3, 4).setSize(152, 159));
        for (int i = 0; i < 9; i++) {
            builder.widget(
                    new DrawableWidget().setDrawable(GT_UITextures.BUTTON_STANDARD).setPos(155, 3 + i * 18)
                            .setSize(18, 18));
        }

        DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTextsNoPlayerInventory(screenElements);
        builder.widget(screenElements);

        setupToolDisplay();

        builder.widget(new ItemDrawable(() -> mToolStacks.get(mWrench + "WRENCH")).asWidget().setPos(156, 58))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> mWrench, val -> mWrench = val));
        builder.widget(new ItemDrawable(() -> mToolStacks.get(mCrowbar + "CROWBAR")).asWidget().setPos(156, 76))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> mCrowbar, val -> mCrowbar = val));
        builder.widget(new ItemDrawable(() -> mToolStacks.get(mHardHammer + "HARDHAMMER")).asWidget().setPos(156, 94))
                .widget(new TextWidget("H").setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(167, 103))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> mHardHammer, val -> mHardHammer = val));
        builder.widget(new ItemDrawable(() -> mToolStacks.get(mSoftHammer + "SOFTHAMMER")).asWidget().setPos(156, 112))
                .widget(new TextWidget("M").setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(167, 121))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> mSoftHammer, val -> mSoftHammer = val));
        builder.widget(
                new ItemDrawable(() -> mToolStacks.get(mScrewdriver + "SCREWDRIVER")).asWidget().setPos(156, 130))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> mScrewdriver, val -> mScrewdriver = val));
        builder.widget(
                new ItemDrawable(() -> mToolStacks.get(mSolderingTool + "SOLDERING_IRON_LV")).asWidget()
                        .setPos(156, 148))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> mSolderingTool, val -> mSolderingTool = val));
        builder.widget(
                new ItemDrawable(() -> mToolStacks.get(getBaseMetaTileEntity().isActive() + "GLASS")).asWidget()
                        .setPos(156, 22))
                .widget(
                        TextWidget.dynamicString(() -> getBaseMetaTileEntity().isActive() ? "On" : "Off")
                                .setSynced(false).setDefaultColor(COLOR_TEXT_WHITE.get()).setPos(157, 31))
                .widget(
                        new FakeSyncWidget.BooleanSyncer(
                                () -> getBaseMetaTileEntity().isActive(),
                                val -> getBaseMetaTileEntity().setActive(val)));
    }

    protected void drawTextsNoPlayerInventory(DynamicPositionedColumn screenElements) {
        screenElements.setSynced(false).setSpace(0).setPos(6, 7);

        screenElements
                .widget(
                        new TextWidget(GT_Utility.trans("138", "Incomplete Structure."))
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> !mMachine))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("GTPP.machines.input") + " "
                                                + StatCollector.translateToLocal("GTPP.machines.tier")
                                                + ": "
                                                + EnumChatFormatting.GREEN
                                                + GT_Values.VOLTAGE_NAMES[(int) getInputTier()])
                                .setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> mMachine && getInputTier() > 0))
                .widget(
                        TextWidget.dynamicString(
                                () -> StatCollector.translateToLocal("GTPP.machines.output") + " "
                                        + StatCollector.translateToLocal("GTPP.machines.tier")
                                        + ": "
                                        + EnumChatFormatting.GREEN
                                        + GT_Values.VOLTAGE_NAMES[(int) getOutputTier()])
                                .setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> mMachine && getOutputTier() > 0))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("GTPP.multiblock.progress") + ": "
                                                + EnumChatFormatting.GREEN
                                                + getBaseMetaTileEntity().getProgress() / 20
                                                + EnumChatFormatting.RESET
                                                + " s / "
                                                + EnumChatFormatting.YELLOW
                                                + getBaseMetaTileEntity().getMaxProgress() / 20
                                                + EnumChatFormatting.RESET
                                                + " s")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        new TextWidget(StatCollector.translateToLocal("GTPP.multiblock.energy") + ":")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal(
                                                "" + EnumChatFormatting.GREEN
                                                        + getStoredEnergyInAllEnergyHatches()
                                                        + EnumChatFormatting.RESET
                                                        + " EU / "
                                                        + EnumChatFormatting.YELLOW
                                                        + getMaxEnergyStorageOfAllEnergyHatches()
                                                        + EnumChatFormatting.RESET
                                                        + " EU"))
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        new TextWidget(StatCollector.translateToLocal("GTPP.multiblock.usage") + ":")
                                .setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> mMachine && getLastRecipeEU() > 0 && getLastRecipeDuration() > 0))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal(
                                                "" + EnumChatFormatting.RED
                                                        + -getLastRecipeEU()
                                                        + EnumChatFormatting.RESET
                                                        + " EU/t/parallel"))
                                .setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> mMachine && getLastRecipeEU() > 0 && getLastRecipeDuration() > 0))
                .widget(
                        TextWidget
                                .dynamicString(() -> StatCollector.translateToLocal("GTPP.multiblock.generation") + ":")
                                .setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> mMachine && getLastRecipeEU() < 0 && getLastRecipeDuration() > 0))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal(
                                                "" + EnumChatFormatting.GREEN
                                                        + getLastRecipeEU()
                                                        + EnumChatFormatting.RESET
                                                        + " EU/t/parallel"))
                                .setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> mMachine && getLastRecipeEU() < 0 && getLastRecipeDuration() > 0))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("GTPP.multiblock.duration") + ": "
                                                + EnumChatFormatting.RED
                                                + getLastRecipeDuration()
                                                + EnumChatFormatting.RESET
                                                + " ticks")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(
                                        widget -> mMachine && getLastRecipeEU() != 0 && getLastRecipeDuration() > 0))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("GTPP.multiblock.specialvalue") + ": "
                                                + EnumChatFormatting.RED
                                                + getLastRecipeEU()
                                                + EnumChatFormatting.RESET
                                                + "")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(
                                        widget -> mMachine && getLastRecipeEU() != 0
                                                && getLastRecipeDuration() > 0
                                                && (mLastRecipe != null ? mLastRecipe.mSpecialValue : 0) > 0))
                .widget(
                        new TextWidget(StatCollector.translateToLocal("GTPP.multiblock.mei") + ":")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal(
                                                "" + EnumChatFormatting.YELLOW
                                                        + getMaxInputVoltage()
                                                        + EnumChatFormatting.RESET
                                                        + " EU/t(*2A) "
                                                        + StatCollector.translateToLocal("GTPP.machines.tier")
                                                        + ": "
                                                        + EnumChatFormatting.YELLOW
                                                        + GT_Values.VN[GT_Utility.getTier(getMaxInputVoltage())]
                                                        + EnumChatFormatting.RESET))
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("GTPP.multiblock.efficiency") + ": "
                                                + EnumChatFormatting.YELLOW
                                                + (mEfficiency / 100.0F)
                                                + EnumChatFormatting.RESET
                                                + " %")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("GTPP.multiblock.pollution") + ": "
                                                + EnumChatFormatting.RED
                                                + (getPollutionPerTick(null) * 20)
                                                + EnumChatFormatting.RESET
                                                + "/sec")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> StatCollector.translateToLocal("GTPP.multiblock.pollutionreduced") + ": "
                                                + EnumChatFormatting.GREEN
                                                + getPollutionReductionForAllMufflers()
                                                + EnumChatFormatting.RESET
                                                + " %")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        new TextWidget("Total Time Since Built: ").setDefaultColor(COLOR_TEXT_WHITE.get())
                                .setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> "" + EnumChatFormatting.DARK_GREEN
                                                + getRuntimeWeeksDisplay()
                                                + EnumChatFormatting.RESET
                                                + " Weeks,")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> "" + EnumChatFormatting.DARK_GREEN
                                                + getRuntimeDaysDisplay()
                                                + EnumChatFormatting.RESET
                                                + " Days,")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> "" + EnumChatFormatting.DARK_GREEN
                                                + getRuntimeHoursDisplay()
                                                + EnumChatFormatting.RESET
                                                + " Hours,")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> "" + EnumChatFormatting.DARK_GREEN
                                                + getRuntimeMinutesDisplay()
                                                + EnumChatFormatting.RESET
                                                + " Minutes,")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> "" + EnumChatFormatting.DARK_GREEN
                                                + getRuntimeSecondsDisplay()
                                                + EnumChatFormatting.RESET
                                                + " Seconds")
                                .setDefaultColor(COLOR_TEXT_WHITE.get()).setEnabled(widget -> mMachine));
    }

    protected int getLastRecipeEU() {
        return mLastRecipe != null ? mLastRecipe.mEUt : 0;
    }

    protected int getLastRecipeDuration() {
        return mLastRecipe != null ? mLastRecipe.mDuration : 0;
    }

    protected long getRuntimeSeconds() {
        return getTotalRuntimeInTicks() / 20;
    }

    protected long getRuntimeWeeksDisplay() {
        return TimeUnit.SECONDS.toDays(getRuntimeSeconds()) / 7;
    }

    protected long getRuntimeDaysDisplay() {
        return TimeUnit.SECONDS.toDays(getRuntimeSeconds()) - 7 * getRuntimeWeeksDisplay();
    }

    protected long getRuntimeHoursDisplay() {
        return TimeUnit.SECONDS.toHours(getRuntimeSeconds()) - TimeUnit.DAYS.toHours(getRuntimeDaysDisplay())
                - TimeUnit.DAYS.toHours(7 * getRuntimeWeeksDisplay());
    }

    protected long getRuntimeMinutesDisplay() {
        return TimeUnit.SECONDS.toMinutes(getRuntimeSeconds()) - (TimeUnit.SECONDS.toHours(getRuntimeSeconds()) * 60);
    }

    protected long getRuntimeSecondsDisplay() {
        return TimeUnit.SECONDS.toSeconds(getRuntimeSeconds()) - (TimeUnit.SECONDS.toMinutes(getRuntimeSeconds()) * 60);
    }

    protected void setupToolDisplay() {
        if (!mToolStacks.isEmpty()) return;

        mToolStacks.put(
                true + "WRENCH",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
                true + "CROWBAR",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.CROWBAR, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
                true + "HARDHAMMER",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
                true + "SOFTHAMMER",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.SOFTHAMMER, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
                true + "SCREWDRIVER",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, GOOD, Materials.Tungsten, null));
        mToolStacks.put(
                true + "SOLDERING_IRON_LV",
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV,
                        1,
                        GOOD,
                        Materials.Tungsten,
                        null));

        mToolStacks.put(
                false + "WRENCH",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
                false + "CROWBAR",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.CROWBAR, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
                false + "HARDHAMMER",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.HARDHAMMER, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
                false + "SOFTHAMMER",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.SOFTHAMMER, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
                false + "SCREWDRIVER",
                GT_MetaGenerated_Tool_01.INSTANCE
                        .getToolWithStats(GT_MetaGenerated_Tool_01.SCREWDRIVER, 1, BAD, Materials.Tungsten, null));
        mToolStacks.put(
                false + "SOLDERING_IRON_LV",
                GT_MetaGenerated_Tool_01.INSTANCE.getToolWithStats(
                        GT_MetaGenerated_Tool_01.SOLDERING_IRON_LV,
                        1,
                        BAD,
                        Materials.Tungsten,
                        null));

        ItemStack aGlassPane1 = ItemUtils.getItemStackOfAmountFromOreDict("paneGlassRed", 1);
        ItemStack aGlassPane2 = ItemUtils.getItemStackOfAmountFromOreDict("paneGlassLime", 1);
        mToolStacks.put("falseGLASS", aGlassPane1);
        mToolStacks.put("trueGLASS", aGlassPane2);
    }

    public enum GTPPHatchElement implements IHatchElement<GregtechMeta_MultiBlockBase<?>> {

        AirIntake(GregtechMeta_MultiBlockBase::addAirIntakeToMachineList, GT_MetaTileEntity_Hatch_AirIntake.class) {

            @Override
            public long count(GregtechMeta_MultiBlockBase<?> t) {
                return t.mAirIntakes.size();
            }
        },
        ControlCore(GregtechMeta_MultiBlockBase::addControlCoreToMachineList,
                GT_MetaTileEntity_Hatch_ControlCore.class) {

            @Override
            public long count(GregtechMeta_MultiBlockBase<?> t) {
                return t.mControlCoreBus.size();
            }
        },
        TTDynamo(GregtechMeta_MultiBlockBase::addMultiAmpDynamoToMachineList,
                "com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti") {

            @Override
            public long count(GregtechMeta_MultiBlockBase<?> t) {
                return t.mTecTechDynamoHatches.size();
            }
        },
        TTEnergy(GregtechMeta_MultiBlockBase::addMultiAmpEnergyToMachineList,
                "com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti") {

            @Override
            public long count(GregtechMeta_MultiBlockBase<?> t) {
                return t.mTecTechEnergyHatches.size();
            }
        },;

        @SuppressWarnings("unchecked")
        private static <T> Class<T> retype(Class<?> clazz) {
            return (Class<T>) clazz;
        }

        private final List<? extends Class<? extends IMetaTileEntity>> mMteClasses;
        private final IGT_HatchAdder<? super GregtechMeta_MultiBlockBase<?>> mAdder;

        @SafeVarargs
        GTPPHatchElement(IGT_HatchAdder<? super GregtechMeta_MultiBlockBase<?>> aAdder,
                Class<? extends IMetaTileEntity>... aMteClasses) {
            this.mMteClasses = Arrays.asList(aMteClasses);
            this.mAdder = aAdder;
        }

        GTPPHatchElement(IGT_HatchAdder<? super GregtechMeta_MultiBlockBase<?>> aAdder, String... aClassNames) {
            this.mMteClasses = Arrays.stream(aClassNames).map(ReflectionUtils::getClass).filter(Objects::nonNull)
                    .<Class<? extends IMetaTileEntity>>map(GTPPHatchElement::retype).collect(Collectors.toList());
            this.mAdder = aAdder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mMteClasses;
        }

        @Override
        public IGT_HatchAdder<? super GregtechMeta_MultiBlockBase<?>> adder() {
            return mAdder;
        }
    }
}
