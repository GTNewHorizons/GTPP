package gtPlusPlus.xmod.gregtech.api.metatileentity.custom.power;

import java.util.Collection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;

public abstract class GTPP_MTE_BasicLosslessGenerator extends GTPP_MTE_BasicTank {

    public GTPP_MTE_BasicLosslessGenerator(int aID, String aName, String aNameRegional, int aTier, String aDescription,
            ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 3, aDescription, aTextures);
    }

    public GTPP_MTE_BasicLosslessGenerator(int aID, String aName, String aNameRegional, int aTier,
            String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, 3, aDescription, aTextures);
    }

    public GTPP_MTE_BasicLosslessGenerator(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GTPP_MTE_BasicLosslessGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[10][17][];

        for (byte i = -1; i < 16; ++i) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFrontActive(i);
            rTextures[6][i + 1] = this.getBackActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }

        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int aColorIndex, boolean aActive, boolean aRedstone) {
        return this.mTextures[(aActive ? 5 : 0)
                + (side == facing ? 0
                        : (side == facing.getOpposite() ? 1
                                : (side == ForgeDirection.DOWN ? 2 : (side == ForgeDirection.UP ? 3 : 4))))][aColorIndex
                                        + 1];
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[this.mDescriptionArray.length + 1];
        System.arraycopy(this.mDescriptionArray, 0, desc, 0, this.mDescriptionArray.length);
        desc[this.mDescriptionArray.length] = "Fuel Efficiency: " + this.getEfficiency() + "%";
        return desc;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        Logger.WARNING("Right Clicked");
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1] };
    }

    public ITexture[] getFrontActive(byte aColor) {
        return this.getFront(aColor);
    }

    public ITexture[] getBackActive(byte aColor) {
        return this.getBack(aColor);
    }

    public ITexture[] getBottomActive(byte aColor) {
        return this.getBottom(aColor);
    }

    public ITexture[] getTopActive(byte aColor) {
        return this.getTop(aColor);
    }

    public ITexture[] getSidesActive(byte aColor) {
        return this.getSides(aColor);
    }

    public boolean isFacingValid(ForgeDirection side) {
        return side.offsetY == 0;
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 2;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    public boolean isOutputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public long maxEUOutput() {
        return this.getBaseMetaTileEntity().isAllowedToWork() ? GT_Values.V[this.mTier] : 0L;
    }

    @Override
    public long maxEUStore() {
        return Math.max(this.getEUVar(), GT_Values.V[this.mTier] * 40L + this.getMinimumStoredEU());
    }

    @Override
    public boolean doesFillContainers() {
        return this.getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean doesEmptyContainers() {
        return this.getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean canTankBeFilled() {
        return this.getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean canTankBeEmptied() {
        return this.getBaseMetaTileEntity().isAllowedToWork();
    }

    @Override
    public boolean displaysItemStack() {
        return true;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        int aVal = this.getFuelValue(aFluid);
        Logger.WARNING("Fuel Value: " + aVal);
        return aVal > 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && aTick % 10L == 0L) {
            int tFuelValue;
            if (this.mFluid == null) {
                if (aBaseMetaTileEntity.getUniversalEnergyStored() < this.maxEUOutput() + this.getMinimumStoredEU()) {
                    this.mInventory[this.getStackDisplaySlot()] = null;
                } else {
                    if (this.mInventory[this.getStackDisplaySlot()] == null) {
                        this.mInventory[this.getStackDisplaySlot()] = new ItemStack(Blocks.fire, 1);
                    }

                    this.mInventory[this.getStackDisplaySlot()].setStackDisplayName(
                            "Generating: "
                                    + (aBaseMetaTileEntity.getUniversalEnergyStored() - this.getMinimumStoredEU())
                                    + " EU");
                }
            } else {
                tFuelValue = this.getFuelValue(this.mFluid);
                int tConsumed = this.consumedFluidPerOperation(this.mFluid);
                if (tFuelValue > 0 && tConsumed > 0 && this.mFluid.amount > tConsumed) {
                    long tFluidAmountToUse = Math.min(
                            (long) (this.mFluid.amount / tConsumed),
                            (this.maxEUStore() - aBaseMetaTileEntity.getUniversalEnergyStored()) / (long) tFuelValue);
                    if (tFluidAmountToUse > 0L && aBaseMetaTileEntity
                            .increaseStoredEnergyUnits(tFluidAmountToUse * (long) tFuelValue, true)) {
                        PollutionUtils.addPollution(this.getBaseMetaTileEntity(), 10 * this.getPollution());
                        this.mFluid.amount = (int) ((long) this.mFluid.amount - tFluidAmountToUse * (long) tConsumed);
                    }
                }
            }

            if (this.mInventory[this.getInputSlot()] != null
                    && aBaseMetaTileEntity.getUniversalEnergyStored()
                            < this.maxEUOutput() * 20L + this.getMinimumStoredEU()
                    && GT_Utility.getFluidForFilledItem(this.mInventory[this.getInputSlot()], true) == null) {
                tFuelValue = this.getFuelValue(this.mInventory[this.getInputSlot()]);
                if (tFuelValue > 0) {
                    ItemStack tEmptyContainer = this.getEmptyContainer(this.mInventory[this.getInputSlot()]);
                    if (aBaseMetaTileEntity.addStackToSlot(this.getOutputSlot(), tEmptyContainer)) {
                        aBaseMetaTileEntity.increaseStoredEnergyUnits((long) tFuelValue, true);
                        aBaseMetaTileEntity.decrStackSize(this.getInputSlot(), 1);
                        PollutionUtils.addPollution(this.getBaseMetaTileEntity(), 10 * this.getPollution());
                    }
                }
            }
        }

        if (aBaseMetaTileEntity.isServerSide()) {
            Logger.WARNING("Ticking Servside");
            aBaseMetaTileEntity.setActive(
                    aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.getUniversalEnergyStored()
                            >= this.maxEUOutput() + this.getMinimumStoredEU());
        }
    }

    public abstract int getPollution();

    public abstract GT_Recipe_Map getRecipes();

    public abstract int getEfficiency();

    public int consumedFluidPerOperation(FluidStack aLiquid) {
        return 1;
    }

    public int getFuelValue(FluidStack aLiquid) {
        if (aLiquid != null && this.getRecipes() != null) {
            Collection<GT_Recipe> tRecipeList = this.getRecipes().mRecipeList;
            if (tRecipeList != null) {
                Logger.WARNING("Fuels: " + tRecipeList.size());

                for (GT_Recipe tFuel : tRecipeList) {
                    FluidStack tLiquid;
                    if ((tLiquid = GT_Utility.getFluidForFilledItem(tFuel.getRepresentativeInput(0), true)) != null
                            && aLiquid.isFluidEqual(tLiquid)) {
                        Logger.WARNING("Fuel Ok");
                        return (int) ((long) tFuel.mSpecialValue * (long) this.getEfficiency()
                                * (long) this.consumedFluidPerOperation(tLiquid)
                                / 100L);
                    }
                    if ((tLiquid = tFuel.getRepresentativeFluidInput(0)) != null && aLiquid.isFluidEqual(tLiquid)) {
                        Logger.WARNING("Fuel Ok");
                        return (int) ((long) tFuel.mSpecialValue * (long) this.getEfficiency()
                                * (long) this.consumedFluidPerOperation(tLiquid)
                                / 100L);
                    }
                }
            }

            return 0;
        } else {
            return 0;
        }
    }

    public int getFuelValue(ItemStack aStack) {
        if (!GT_Utility.isStackInvalid(aStack) && this.getRecipes() != null) {
            Logger.WARNING("Fuel Item OK");
            GT_Recipe tFuel = this.getRecipes().findRecipe(
                    this.getBaseMetaTileEntity(),
                    false,
                    Long.MAX_VALUE,
                    (FluidStack[]) null,
                    new ItemStack[] { aStack });
            return tFuel != null ? (int) ((long) tFuel.mSpecialValue * 1000L * (long) this.getEfficiency() / 100L) : 0;
        } else {
            return 0;
        }
    }

    public ItemStack getEmptyContainer(ItemStack aStack) {
        if (!GT_Utility.isStackInvalid(aStack) && this.getRecipes() != null) {
            GT_Recipe tFuel = this.getRecipes().findRecipe(
                    this.getBaseMetaTileEntity(),
                    false,
                    Long.MAX_VALUE,
                    (FluidStack[]) null,
                    new ItemStack[] { aStack });
            return tFuel != null ? GT_Utility.copy(new Object[] { tFuel.getOutput(0) })
                    : GT_Utility.getContainerItem(aStack, true);
        } else {
            return null;
        }
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack) && (this.getFuelValue(aStack) > 0
                || this.getFuelValue(GT_Utility.getFluidForFilledItem(aStack, true)) > 0);
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public int getTankPressure() {
        return -100;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }
}
