package gtPlusPlus.core.util.fluid;

import gregtech.api.enums.*;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.fluids.GenericFluid;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;

public class FluidUtils {

	public static FluidStack getFluidStack(String fluidName, int amount){
		Utils.LOG_WARNING("Trying to get a fluid stack of "+fluidName);
		try {
			return FluidRegistry.getFluidStack(fluidName, amount).copy();
		} 
		catch (Throwable e){
			return null;
		}

	}
	
	public static FluidStack getFluidStack(FluidStack vmoltenFluid, int fluidAmount) {
		Utils.LOG_WARNING("Trying to get a fluid stack of "+vmoltenFluid.getFluid().getName());
		try {
			return FluidRegistry.getFluidStack(vmoltenFluid.getFluid().getName(), fluidAmount).copy();
		} 
		catch (Throwable e){
			return null;
		}
	}

	public static FluidStack[] getFluidStackArray(String fluidName, int amount){
		Utils.LOG_WARNING("Trying to get a fluid stack of "+fluidName);
		try {
			FluidStack[] singleFluid = {FluidRegistry.getFluidStack(fluidName, amount)};
			return singleFluid;
		} 
		catch (Throwable e){
			return null;
		}

	}

	public static FluidStack[] getFluidStackArray(FluidStack fluidName, int amount){
		Utils.LOG_WARNING("Trying to get a fluid stack of "+fluidName);
		try {
			FluidStack[] singleFluid = {FluidRegistry.getFluidStack(fluidName.getLocalizedName(), amount)};
			return singleFluid;
		} 
		catch (Throwable e){
			return null;
		}

	}
	
	
	/**
	 * @param String displayName
	 * @param String fluidName
	 * @param int meltingPointC Temp
	 * @param short[] rgba
	 * @param byte state
	 * States: 0 (Solid), 1 (Fluid), 2(Gas), 3(Plasma) 4(Fuel I think? Don't use.)
	 * 
	 * @return short[]
	 */
	public static Fluid generateFluid(String displayName, String fluidName, int tempK, short[] rgba ,int aState){
		Fluid generatedFluid = null;
		switch (aState) {
		case 0: {
			generatedFluid = new GenericFluid(displayName, fluidName, 0, 100, tempK, 10000, false, rgba);
			break;
		}
		default:
		case 1:
		case 4: {
			generatedFluid = new GenericFluid(displayName, fluidName, 0, 100, tempK, 1000, false, rgba);
			break;
		}
		case 2: {
			generatedFluid = new GenericFluid(displayName, fluidName, 0, -100, tempK, 200, true, rgba);
			break;
		}
		case 3: {
			generatedFluid = new GenericFluid(displayName, fluidName, 15, -10000, tempK, 10, true, rgba);
			break;
		}
		}
		return generatedFluid;
	}
	/**
	 * 
	 * @param String fluidName
	 * @param int meltingPointC Temp
	 * @param short[] rgba
	 * @param byte state
	 * States: 0 (Solid), 1 (Fluid), 2(Gas), 3(Plasma) 4(Fuel I think? Don't use.)
	 * 
	 * @return short[]
	 */
	public static Fluid generateFluid(Material material ,int aState){
		int tempK = material.getMeltingPointC();
		Fluid generatedFluid = null;
		switch (aState) {
		case 0: {
			generatedFluid = new GenericFluid(material, 0, 100, tempK, 10000, false);
			break;
		}
		default:
		case 1:
		case 4: {
			generatedFluid = new GenericFluid(material, 0, 100, tempK, 1000, false);
			break;
		}
		case 2: {
			generatedFluid = new GenericFluid(material, 0, -100, tempK, 200, true);
			break;
		}
		case 3: {
			generatedFluid = new GenericFluid(material, 15, -10000, tempK, 10, true);
			break;
		}
		}
		return generatedFluid;
	}
	

	public static Fluid addAutogeneratedMoltenFluid(String materialNameFormatted, short[] rgba, int MeltingPoint) {
		return addFluid("molten." + materialNameFormatted.toLowerCase(), "molten.autogenerated", "Molten " + materialNameFormatted, null, rgba, 1, (MeltingPoint <= 0L) ? 1000L : MeltingPoint, null, null, 0);
	}	
	
	public static Fluid addAutogeneratedMoltenFluid(final GT_Materials aMaterial) {
		return addFluid("molten." + aMaterial.name().toLowerCase(), "molten.autogenerated", "Molten " + aMaterial.name(), aMaterial, aMaterial.mMoltenRGBa, 1, (aMaterial.mMeltingPoint <= 0L) ? 1000L : aMaterial.mMeltingPoint, null, null, 0);
	}

	public static Fluid addFluid(final String aName, final String aLocalized, final GT_Materials aMaterial, final int aState, final long aTemperatureK) {
		return addFluid(aName, aLocalized, aMaterial, aState, aTemperatureK, null, null, 0);
	}

	public static Fluid addFluid(final String aName, final String aLocalized, final GT_Materials aMaterial, final int aState, final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer, final int aFluidAmount) {
		return addFluid(aName, aName.toLowerCase(), aLocalized, aMaterial, null, aState, aTemperatureK, aFullContainer, aEmptyContainer, aFluidAmount);
	}

	public static Fluid addFluid(String aName, final String aTexture, final String aLocalized, final GT_Materials aMaterial, final short[] aRGBa, final int aState, final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer, final int aFluidAmount) {
		aName = Utils.sanitizeString(aName.toLowerCase());
		Fluid rFluid = new FluidGT6(aName, aTexture, (aRGBa != null) ? aRGBa : Dyes._NULL.getRGBA());
		GT_LanguageManager.addStringLocalization(rFluid.getUnlocalizedName(), (aLocalized == null) ? aName : aLocalized);
		if (FluidRegistry.registerFluid(rFluid)) {
			switch (aState) {
			case 0: {
				rFluid.setGaseous(false);
				rFluid.setViscosity(10000);
				break;
			}
			case 1:
			case 4: {
				rFluid.setGaseous(false);
				rFluid.setViscosity(1000);
				break;
			}
			case 2: {
				rFluid.setGaseous(true);
				rFluid.setDensity(-100);
				rFluid.setViscosity(200);
				break;
			}
			case 3: {
				rFluid.setGaseous(true);
				rFluid.setDensity(-10000);
				rFluid.setViscosity(10);
				rFluid.setLuminosity(15);
				break;
			}
			}
		}
		else {
			rFluid = FluidRegistry.getFluid(aName);
		}
		if (rFluid.getTemperature() == new Fluid("test").getTemperature() || rFluid.getTemperature() <= 0) {
			rFluid.setTemperature((int) (aTemperatureK));
		}
		if (aMaterial != null) {
			switch (aState) {
			case 1: {
				aMaterial.mFluid = (rFluid);
				break;
			}
			case 2: {
				aMaterial.mGas = (rFluid);
				break;
			}
			case 3: {
				aMaterial.mPlasma = (rFluid);
				break;
			}
			}
		}
		if (aFullContainer != null && aEmptyContainer != null && !FluidContainerRegistry.registerFluidContainer(new FluidStack(rFluid, aFluidAmount), aFullContainer, aEmptyContainer)) {
			GT_Values.RA.addFluidCannerRecipe(aFullContainer, container(aFullContainer, false), null, new FluidStack(rFluid, aFluidAmount));
		}
		return rFluid;
	}
	
	public static Fluid addGTFluid(final String aName, final String aLocalized, final short[] aRGBa, final int aState, final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer, final int aFluidAmount) {
		return addGTFluid("molten."+aName, "molten.autogenerated", aLocalized, aRGBa, aState, aTemperatureK, aFullContainer, aEmptyContainer, aFluidAmount);
	}
	
	public static Fluid addGTFluid(String aName, final String aTexture, final String aLocalized, final short[] aRGBa, final int aState, final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer, final int aFluidAmount) {
		aName = Utils.sanitizeString(aName.toLowerCase());
		Fluid rFluid = new FluidGT6(aName, aTexture, (aRGBa != null) ? aRGBa : Dyes._NULL.getRGBA());
		GT_LanguageManager.addStringLocalization(rFluid.getUnlocalizedName(), (aLocalized == null) ? aName : aLocalized);
		if (FluidRegistry.registerFluid(rFluid)) {
			switch (aState) {
			case 0: {
				rFluid.setGaseous(false);
				rFluid.setViscosity(10000);
				break;
			}
			case 1:
			case 4: {
				rFluid.setGaseous(false);
				rFluid.setViscosity(1000);
				break;
			}
			case 2: {
				rFluid.setGaseous(true);
				rFluid.setDensity(-100);
				rFluid.setViscosity(200);
				break;
			}
			case 3: {
				rFluid.setGaseous(true);
				rFluid.setDensity(-10000);
				rFluid.setViscosity(10);
				rFluid.setLuminosity(15);
				break;
			}
			}
		}
		else {
			rFluid = FluidRegistry.getFluid(aName);
		}
		if (rFluid.getTemperature() == new Fluid("test").getTemperature() || rFluid.getTemperature() <= 0) {
			rFluid.setTemperature((int) (aTemperatureK));
		}		
		if (aFullContainer != null && aEmptyContainer != null && !FluidContainerRegistry.registerFluidContainer(new FluidStack(rFluid, aFluidAmount), aFullContainer, aEmptyContainer)) {
			GT_Values.RA.addFluidCannerRecipe(aFullContainer, container(aFullContainer, false), null, new FluidStack(rFluid, aFluidAmount));
		}
		return rFluid;
	}

	public static boolean valid(final Object aStack) {
		return aStack != null && aStack instanceof ItemStack && ((ItemStack)aStack).getItem() != null && ((ItemStack)aStack).stackSize >= 0;
	}

	public static boolean invalid(final Object aStack) {
		return aStack == null || !(aStack instanceof ItemStack) || ((ItemStack)aStack).getItem() == null || ((ItemStack)aStack).stackSize < 0;
	}

	public static boolean equal(final ItemStack aStack1, final ItemStack aStack2) {
		return equal(aStack1, aStack2, false);
	}

	public static boolean equal(final ItemStack aStack1, final ItemStack aStack2, final boolean aIgnoreNBT) {
		return aStack1 != null && aStack2 != null && equal_(aStack1, aStack2, aIgnoreNBT);
	}

	public static boolean equal_(final ItemStack aStack1, final ItemStack aStack2, final boolean aIgnoreNBT) {
		return aStack1.getItem() == aStack2.getItem() && (aIgnoreNBT || (aStack1.getTagCompound() == null == (aStack2.getTagCompound() == null) && (aStack1.getTagCompound() == null || aStack1.getTagCompound().equals((Object)aStack2.getTagCompound())))) && (meta(aStack1) == meta(aStack2) || meta(aStack1) == 32767 || meta(aStack2) == 32767);
	}

	public static ItemStack copy(final Object... aStacks) {
		for (final Object tStack : aStacks) {
			if (valid(tStack)) {
				return ((ItemStack)tStack).copy();
			}
		}
		return null;
	}

	public static ItemStack copyMeta(final long aMetaData, final Object... aStacks) {
		final ItemStack rStack = copy(aStacks);
		if (invalid(rStack)) {
			return null;
		}
		return meta(rStack, aMetaData);
	}

	public static short meta(final ItemStack aStack) {
		return (short)Items.feather.getDamage(aStack);
	}

	public static ItemStack meta(final ItemStack aStack, final long aMeta) {
		Items.feather.setDamage(aStack, (int)(short)aMeta);
		return aStack;
	}

	public static ItemStack amount(final long aAmount, final Object... aStacks) {
		final ItemStack rStack = copy(aStacks);
		if (invalid(rStack)) {
			return null;
		}
		rStack.stackSize = (int)aAmount;
		return rStack;
	}

	public static ItemStack container(final ItemStack aStack, final boolean aCheckIFluidContainerItems) {
		if (invalid(aStack)) {
			return null;
		}
		if (aStack.getItem().hasContainerItem(aStack)) {
			return aStack.getItem().getContainerItem(aStack);
		}
		if (equal(aStack, ItemList.Cell_Empty.get(1), true)) {
			return null;
		}
		if (aCheckIFluidContainerItems && aStack.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem)aStack.getItem()).getCapacity(aStack) > 0) {
			final ItemStack tStack = amount(1L, aStack);
			((IFluidContainerItem)aStack.getItem()).drain(tStack, Integer.MAX_VALUE, true);
			if (!equal(aStack, tStack)) {
				return tStack;
			}
			return null;
		}
		if (equal(aStack, ItemList.IC2_ForgeHammer.get(1)) || equal(aStack, ItemList.IC2_WireCutter.get(1))) {
			return copyMeta(meta(aStack) + 1, aStack);
		}
		return null;
	}

	public static ItemStack container(final ItemStack aStack, final boolean aCheckIFluidContainerItems, final int aStacksize) {
		return amount(aStacksize, container(aStack, aCheckIFluidContainerItems));
	}	

}
