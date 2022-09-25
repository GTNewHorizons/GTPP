package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Turbine;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

@SuppressWarnings("deprecation")
public class GT_MTE_LargeTurbine_Plasma extends GregtechMetaTileEntity_LargerTurbineBase {

    public GT_MTE_LargeTurbine_Plasma(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MTE_LargeTurbine_Plasma(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MTE_LargeTurbine_Plasma(mName);
    }

    @Override
    public int getCasingMeta() {
        return 4;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 60;
    }

    @Override
    protected boolean requiresOutputHatch() {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 0;
    }

    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid == null) {
            return 0;
        }
        GT_Recipe tFuel = GT_Recipe_Map.sPlasmaFuels.findFuel(aLiquid);
        if (tFuel != null) {
            return tFuel.mSpecialValue;
        }
        return 0;
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

        try {
            ArrayList<GT_MetaTileEntity_Hatch_Turbine> aEmptyTurbineRotorHatches = getEmptyTurbineAssemblies();
            if (aEmptyTurbineRotorHatches.size() > 0) {
                log("Found " + aEmptyTurbineRotorHatches.size() + " Assemblies without Turbine.");
                hatch:
                for (GT_MetaTileEntity_Hatch_Turbine aHatch : aEmptyTurbineRotorHatches) {
                    ArrayList<ItemStack> aTurbines = getAllBufferedTurbines();
                    for (ItemStack aTurbineItem : aTurbines) {
                        if (aTurbineItem == null) {
                            continue;
                        }
                        if (aHatch.insertTurbine(aTurbineItem.copy())) {
                            boolean aDidDeplete = depleteTurbineFromStock(aTurbineItem);
                            log("Put Turbine into Assembly - " + aDidDeplete);
                            continue hatch;
                        }
                    }
                }
            }

            if (getEmptyTurbineAssemblies().size() > 0 || !areAllTurbinesTheSame()) {
                log("BAD RETURN - 1");
                stopMachine();
                return false;
            }

            ArrayList<FluidStack> tFluids = getStoredFluids();

            if (tFluids.size() > 0) {
                if (baseEff == 0
                        || optFlow == 0
                        || counter >= 512
                        || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()
                        || this.getBaseMetaTileEntity().hasInventoryBeenModified()) {
                    counter = 0;

                    float aTotalBaseEff = 0;
                    float aTotalOptimalFlow = 0;

                    ItemStack aStack = getFullTurbineAssemblies().get(0).getTurbine();
                    for (int i = 0; i < getSpeedMultiplier(); i++) {
                        if (i == 0) {
                            aTotalBaseEff += GT_Utility.safeInt((long)
                                    ((5F + ((GT_MetaGenerated_Tool) aStack.getItem()).getToolCombatDamage(aStack))
                                            * 1000F));
                        }
                        aTotalOptimalFlow += GT_Utility.safeInt((long) Math.max(
                                Float.MIN_NORMAL,
                                ((GT_MetaGenerated_Tool) aStack.getItem())
                                                .getToolStats(aStack)
                                                .getSpeedMultiplier()
                                        * GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mToolSpeed
                                        * 50));
                        if (aTotalOptimalFlow < 0) {
                            log("Int overflow, report to issue tracker");
                            aTotalOptimalFlow = 100;
                        }
                    }

                    flowMultipliers[0] = GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mSteamMultiplier;
                    flowMultipliers[1] = GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mGasMultiplier;
                    flowMultipliers[2] = GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mPlasmaMultiplier;
                    baseEff = MathUtils.roundToClosestInt(aTotalBaseEff);
                    optFlow = MathUtils.roundToClosestInt(aTotalOptimalFlow);
                    if (optFlow <= 0 || baseEff <= 0) {
                        log("Running checkRecipeGeneric(bad-1)");
                        stopMachine(); // in case the turbine got removed
                        return false;
                    }
                } else {
                    counter++;
                }
            }

            // How much the turbine should be producing with this flow
            int newPower = fluidIntoPower(tFluids, optFlow, baseEff, flowMultipliers);
            int difference = newPower - this.mEUt; // difference between current output and new output
            // Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the
            // difference in power level (per tick)
            // This is how much the turbine can actually change during this tick
            int maxChangeAllowed = Math.max(10, GT_Utility.safeInt((long) Math.abs(difference) / 100));

            if (Math.abs(difference)
                    > maxChangeAllowed) { // If this difference is too big, use the maximum allowed change
                int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
                this.mEUt += change; // Apply the change
            } else {
                this.mEUt = newPower;
            }
            if (this.mEUt <= 0) {
                this.mEUt = 0;
                this.mEfficiency = 0;
                log("Running checkRecipeGeneric(bad-2)");
                return false;
            } else {
                this.mMaxProgresstime = 20;
                this.mEfficiencyIncrease = 10;
                // Overvoltage is handled inside the MultiBlockBase when pushing out to dynamos.  no need to do it here.
                // Play sounds (GT++ addition - GT multiblocks play no sounds)
                startProcess();
                return true;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return false;
    }

    @Override
    int fluidIntoPower(ArrayList<FluidStack> aFluids, long aOptFlow, int aBaseEff, float[] flowMultipliers) {
        if (aFluids.size() >= 1) {
            aOptFlow *= 800; // CHANGED THINGS HERE, check recipe runs once per 20 ticks
            int tEU = 0;

            int actualOptimalFlow = 0;

            FluidStack firstFuelType = new FluidStack(
                    aFluids.get(0),
                    0); // Identify a SINGLE type of fluid to process.  Doesn't matter which one. Ignore the rest!
            int fuelValue = getFuelValue(firstFuelType);
            actualOptimalFlow = GT_Utility.safeInt(
                    (long) Math.ceil((double) aOptFlow * (double) flowMultipliers[2] / (double) fuelValue));
            this.realOptFlow = actualOptimalFlow; // For scanner info

            int remainingFlow = GT_Utility.safeInt((long) (actualOptimalFlow
                    * 1.25f)); // Allowed to use up to 125% of optimal flow.  Variable required outside of loop for
            // multi-hatch scenarios.
            int flow = 0;
            int totalFlow = 0;

            storedFluid = 0;
            for (FluidStack aFluid : aFluids) {
                if (aFluid.isFluidEqual(firstFuelType)) {
                    flow = Math.min(aFluid.amount, remainingFlow); // try to use up w/o exceeding remainingFlow
                    depleteInput(new FluidStack(aFluid, flow)); // deplete that amount
                    this.storedFluid += aFluid.amount;
                    remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
                    totalFlow += flow; // track total input used
                }
            }
            String fn = FluidRegistry.getFluidName(firstFuelType);
            String[] nameSegments = fn.split("\\.", 2);
            if (nameSegments.length == 2) {
                String outputName = nameSegments[1];
                FluidStack output = FluidRegistry.getFluidStack(outputName, totalFlow);
                if (output == null) {
                    output = FluidRegistry.getFluidStack("molten." + outputName, totalFlow);
                }
                if (output != null) {
                    addOutput(output);
                }
            }
            if (totalFlow <= 0) return 0;
            tEU = GT_Utility.safeInt((long) ((fuelValue / 20D) * (double) totalFlow));

            if (totalFlow == actualOptimalFlow) {
                tEU = GT_Utility.safeInt((long) (aBaseEff / 10000D * tEU));
            } else {
                double efficiency = 1.0D - Math.abs((totalFlow - actualOptimalFlow) / (float) actualOptimalFlow);

                tEU = (int) (tEU * efficiency);
                tEU = GT_Utility.safeInt((long) (aBaseEff / 10000D * tEU));
            }

            return tEU;
        }
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public String getCustomGUIResourceName() {
        return null;
    }

    @Override
    public String getMachineType() {
        return "Large Plasma Turbine";
    }

    @Override
    protected String getTurbineType() {
        return "Plasma";
    }

    @Override
    protected String getCasingName() {
        return "Reinforced Plasma Turbine Casing";
    }

    @Override
    protected ITexture getTextureFrontFace() {
        return new GT_RenderedTexture(gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_TU5);
    }

    @Override
    protected ITexture getTextureFrontFaceActive() {
        return new GT_RenderedTexture(gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_TU_ACTIVE5);
    }
}
