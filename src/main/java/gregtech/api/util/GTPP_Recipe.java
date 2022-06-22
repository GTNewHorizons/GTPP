package gregtech.api.util;

import static gregtech.api.enums.GT_Values.*;

import java.util.*;

import codechicken.nei.PositionedStack;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.*;
import gregtech.api.objects.GT_ItemStack;
import gtPlusPlus.api.interfaces.IComparableRecipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;

/**
 * Custom GT Recipe Class
 * @author Alkalus
 *
 */
public class GTPP_Recipe extends GT_Recipe  implements IComparableRecipe {

	private final String mRecipeHash;
	private final AutoMap<Integer> mHashMap = new AutoMap<Integer>();

	public GTPP_Recipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecialItems, final int[] aChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
		//Logger.SPECIFIC_WARNING(this.getClass().getName()+" | [GregtechRecipe]", "Created new recipe instance for "+ItemUtils.getArrayStackNames(aInputs), 167);
		this.mRecipeHash = getRecipeHash(this);
		this.mHashMap.addAll(convertStringDataToInts(getEncodedRecipeData(this)));
	}

	public GTPP_Recipe(final ItemStack aInput1, final ItemStack aOutput1, final int aFuelValue, final int aType) {
		this(aInput1, aOutput1, null, null, null, aFuelValue, aType);
	}

	private static AutoMap<Integer> convertStringDataToInts(AutoMap<String> aData){
		AutoMap<Integer> aMap = new AutoMap<Integer>();
		for (String string : aData) {
			aMap.add(string.hashCode());
		}
		return aMap;
	}

	private static AutoMap<String> getEncodedRecipeData(GTPP_Recipe aRecipe){
		AutoMap<String> aData = new AutoMap<String>();
		aData.add(aRecipe.mRecipeHash);
		aData.add(""+aRecipe.mCanBeBuffered);
		aData.add(""+aRecipe.mHidden);
		aData.add(""+aRecipe.mEnabled);
		aData.add(""+aRecipe.mDuration);
		aData.add(""+aRecipe.mEUt);
		aData.add(""+aRecipe.mFakeRecipe);
		aData.add(""+aRecipe.mSpecialItems);
		aData.add(aRecipe.mChances.toString());
		aData.add(aRecipe.mInputs.toString());
		aData.add(aRecipe.mOutputs.toString());
		aData.add(aRecipe.mFluidInputs.toString());
		aData.add(aRecipe.mFluidOutputs.toString());
		return aData;
	}

	public static String getRecipeHash(GT_Recipe aRecipe) {
		String aEncoderString = aRecipe.toString();
		return aEncoderString;
	}

	private final void checkModified() {
		if (hasBeenModified()) {
			String[] aInfo = RecipeUtils.getRecipeInfo(this);
			for (String s : aInfo) {
				Logger.INFO(s);
			}
			CORE.crash("Someone has edited an internal GT++ recipe, which is no longer allowed. Please complain to whoever has done this, not Alkalus.");
		}
	}

	private final boolean hasBeenModified() {
		String aEncoderString = this.toString();
		boolean aBasicHashCheck = this.mRecipeHash.equals(aEncoderString);
		if (!aBasicHashCheck) {
			Logger.INFO("This Recipe Hash: "+aEncoderString);
			Logger.INFO("Expected Hash Code: "+this.mRecipeHash);
			return true;
		}
		AutoMap<Integer> aData = new AutoMap<Integer>();
		aData.addAll(convertStringDataToInts(getEncodedRecipeData(this)));
		long aHashTotal = 0;
		long aExpectedHashTotal = 0;
		for (int a : aData) {
			aHashTotal += a;
		}
		for (int a : this.mHashMap) {
			aExpectedHashTotal += a;
		}
		if (aHashTotal != aExpectedHashTotal) {
			Logger.INFO("This Recipe Hash: "+aEncoderString);
			Logger.INFO("Expected Hash Code: "+this.mRecipeHash);
			Logger.INFO("This Recipe Hash: "+aHashTotal);
			Logger.INFO("Expected Hash Code: "+aExpectedHashTotal);
			return true;
		}
		return false;
	}

	// aSpecialValue = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
	public GTPP_Recipe(final ItemStack aInput1, final ItemStack aOutput1, final ItemStack aOutput2, final ItemStack aOutput3, final ItemStack aOutput4, final int aSpecialValue, final int aType) {
		this(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4}, null, null, null, null, 0, 0, Math.max(1, aSpecialValue));

		Logger.WARNING("Switch case method for adding fuels");
		if ((this.mInputs.length > 0) && (aSpecialValue > 0)) {
			switch (aType) {
			// Diesel Generator
			case 0:
				Logger.WARNING("Added fuel "+aInput1.getDisplayName()+" is ROCKET FUEL - continuing");
				GTPP_Recipe_Map.sRocketFuels.addRecipe(this);
				break;
				// Gas Turbine
			case 1:
				GTPP_Recipe_Map.sGeoThermalFuels.addRecipe(this);
				break;
				// Thermal Generator
			case 2:
				GTPP_Recipe_Map.sRTGFuels.addRecipe(this);
				break;
				// Plasma Generator
			case 4:
				//Gregtech_Recipe_Map.sPlasmaFuels.addRecipe(this);
				break;
				// Magic Generator
			case 5:
				//Gregtech_Recipe_Map.sMagicFuels.addRecipe(this);
				break;
				// Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
			default:
				//Gregtech_Recipe_Map.sDenseLiquidFuels.addRecipe(this);
				break;
			}
		}
	}

	//Custom Recipe Handlers
	public GTPP_Recipe(final ItemStack aInput, final FluidStack aFluid, final ItemStack[] aOutput, final int aDuration, final int aEUt) {
		this(true, new ItemStack[]{aInput}, aOutput.clone(), null, null, new FluidStack[]{aFluid}, null, aDuration, aEUt, 0);
		if ((this.mInputs.length > 0) && (this.mOutputs[0] != null)) {
			GTPP_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(this);
		}
	}

	/*public GregtechRecipe(ItemStack aInput, FluidStack aFluid, ItemStack[] aOutput, int aDuration, int aEUt) {
        this(true, new ItemStack[]{aInput}, aOutput.clone(), null, null, new FluidStack[]{aFluid}, null, aDuration, aEUt, 0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(this);
        }
    }*/


	public static void reInit() {
		GT_Log.out.println("GT_Mod: Re-Unificating Recipes.");
		for (final GTPP_Recipe_Map tMapEntry : GTPP_Recipe_Map.sMappings) {
			//tMapEntry.reInit();
			if (tMapEntry != null && tMapEntry.mRecipeList != null && !tMapEntry.mRecipeList.isEmpty()) {
				for (GT_Recipe aRecipe : tMapEntry.mRecipeList) {
					checkRecipeOwnership(aRecipe);
				}
			}
		}
		for (final GTPP_Recipe_Map_Internal tMapEntry : GTPP_Recipe_Map_Internal.sMappingsEx) {
			//tMapEntry.reInit();
			if (tMapEntry != null && tMapEntry.mRecipeList != null && !tMapEntry.mRecipeList.isEmpty()) {
				for (GT_Recipe aRecipe : tMapEntry.mRecipeList) {
					checkRecipeOwnership(aRecipe);
				}
			}
		}
	}

	private final static boolean checkRecipeOwnership(GT_Recipe aRecipe) {
		if (aRecipe != null && aRecipe instanceof GTPP_Recipe) {
			GTPP_Recipe nRecipe = (GTPP_Recipe) aRecipe;
			GTPP_Recipe_Map_Internal.mHashedRecipes.put(nRecipe.hashCode(), nRecipe);
			return true;
		}
		return false;
	}

	public final static void checkRecipeModifications() {
		for (GTPP_Recipe aRecipe : GTPP_Recipe_Map_Internal.mHashedRecipes.values()) {
			Logger.INFO("Checking recipe: "+aRecipe.hashCode());
			aRecipe.checkModified();
		}
	}

	@Override
	public ItemStack getRepresentativeInput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mInputs.length)) {
			return null;
		}
		return GT_Utility.copy(this.mInputs[aIndex]);
	}

	@Override
	public ItemStack getOutput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mOutputs.length)) {
			return null;
		}
		return GT_Utility.copy(this.mOutputs[aIndex]);
	}

	@Override
	public int getOutputChance(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mChances.length)) {
			return 10000;
		}
		return this.mChances[aIndex];
	}

	@Override
	public FluidStack getRepresentativeFluidInput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mFluidInputs.length) || (this.mFluidInputs[aIndex] == null)) {
			return null;
		}
		return this.mFluidInputs[aIndex].copy();
	}

	@Override
	public FluidStack getFluidOutput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mFluidOutputs.length) || (this.mFluidOutputs[aIndex] == null)) {
			return null;
		}
		return this.mFluidOutputs[aIndex].copy();
	}

	public static class GTPP_Recipe_Map_Internal extends GT_Recipe_Map {

		public static final Collection<GTPP_Recipe_Map_Internal> sMappingsEx = new ArrayList<>();
		private static final HashMap<Integer, GTPP_Recipe> mHashedRecipes = new HashMap<Integer, GTPP_Recipe>();

		public GTPP_Recipe_Map_Internal(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
			GT_Recipe_Map.sMappings.remove(this);
			GTPP_Recipe_Map_Internal.sMappingsEx.add(this);
		}

	}

	public static class GTPP_Recipe_Map {
		/**
		 * Contains all Recipe Maps
		 */
		public static final Collection<GTPP_Recipe_Map> sMappings = new ArrayList<>();
		//public static final GT_Recipe_Map sChemicalBathRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gtpp.recipe.chemicalbath", "Chemical Bath", null, RES_PATH_GUI + "basicmachines/ChemicalBath", 1, 3, 1, 1, 1, E, 1, E, true, true);
		public static final GTPP_Recipe_Map_Internal sCokeOvenRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.cokeoven", "Coke Oven", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 1, 0, 1, E, 1, E, true, true);
		public static final GTPP_Recipe_Map_Internal sMatterFab2Recipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.matterfab2", "Matter Fabricator", null, RES_PATH_GUI + "basicmachines/Default", 9, 9, 0, 0, 1, E, 1, E, true, true);
		//public static final Gregtech_Recipe_Map sMatterFabRecipes = new Gregtech_Recipe_Map(new HashSet<GregtechRecipe>(200), "gtpp.recipe.matterfab", "Matter Fabricator", null, RES_PATH_GUI + "basicmachines/Massfabricator", 1, 3, 1, 1, 1, E, 1, E, true, true);

		public static final GT_Recipe_Map_Fuel sRocketFuels = new GT_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gtpp.recipe.rocketenginefuel", "Rocket Engine Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 3000, " EU", true, true);

		public static final GTPP_Recipe_Map_Internal sGeoThermalFuels = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10), "gtpp.recipe.geothermalfuel", "GeoThermal Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, true);
		public static final GTPP_Recipe_Map_Internal sChemicalDehydratorRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.chemicaldehydrator", "Dehydrator", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 0, 0, 1, E, 1, E, true, true);
		public static final GTPP_Recipe_Map_Internal sVacuumFurnaceRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(500), "gtpp.recipe.vacfurnace", "Vacuum Furnace", null, "gregtech:textures/gui/basicmachines/Default", 6, 6, 1, 0, 1, "Heat Capacity: ", 1, " K", false, true);
		public static final GTPP_Recipe_Map_Internal sAlloyBlastSmelterRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.alloyblastsmelter", "Alloy Blast Smelter", null, RES_PATH_GUI + "basicmachines/BlastSmelter", 9, 9, 1, 0, 1, E, 1, E, true, true);
		public static final GTPP_Recipe_Map_Internal sSteamTurbineFuels = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10), "gtpp.recipe.steamturbinefuel", "GeoThermal Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, false);

		//LFTR recipes
		public static final GTPP_Recipe_Map_Internal sLiquidFluorineThoriumReactorRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(50), "gtpp.recipe.lftr", "Liquid Fluoride Thorium Reactor", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 2, 0, "Power: ", 1, " EU/t per Dynamo", true, true);

		// Ore Milling Map
		public static final GTPP_Recipe_Map_Internal sOreMillRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.oremill", "Milling", null, RES_PATH_GUI + "basicmachines/LFTR", 3, 4, 1, 0, 1, E, 1, E, true, false);

		//Fission Fuel Plant Recipes
		public static final GTPP_Recipe_Map_Internal sFissionFuelProcessing = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(50), "gtpp.recipe.fissionfuel", "Nuclear Fuel Processing", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 0, 1, E, 1, E, true, false);

		//Cold Trap
		public static final GTPP_Recipe_Map_Internal sColdTrapRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.coldtrap", "Cold Trap", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 0, 0, 1, E, 1, E, true, true);

		//Reactor Processing Unit
		public static final GTPP_Recipe_Map_Internal sReactorProcessingUnitRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.reactorprocessingunit", "Reactor Processing Unit", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 0, 0, 1, E, 1, E, true, true);

		//Basic Washer Map
		public static final GTPP_Recipe_Map_Internal sSimpleWasherRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(3), "gtpp.recipe.simplewasher", "Simple Dust Washer", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 1, 1, 0, 0, 1, E, 1, E, true, true);

		//Molecular Transformer Map
		public static final GTPP_Recipe_Map_Internal sMolecularTransformerRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(3), "gtpp.recipe.moleculartransformer", "Molecular Transformer", null, RES_PATH_GUI + "basicmachines/Scanner", 1, 1, 0, 0, 1, E, 1, E, true, true);

		//Elemental Duplicator Map
		public static final GTPP_Recipe_Map_Internal sElementalDuplicatorRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(3), "gtpp.recipe.elementaldupe", "Elemental Duplicator", null, RES_PATH_GUI + "basicmachines/Replicator", 1, 1, 0, 1, 1, E, 1, E, true, false);


		//public static final GT_Recipe_Map sSimpleWasherRecipes_FakeFuckBW = new GT_Recipe_Map(new HashSet<GT_Recipe>(3), "gtpp.recipe.simplewasher", "Fuck you Bart", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 1, 1, 0, 0, 1, E, 1, E, true, false);

		public static final GTPP_Recipe_Map_Internal sChemicalPlantRecipes = new GTPP_Recipe_Map_Internal(
				new HashSet<GT_Recipe>(100),
				"gtpp.recipe.fluidchemicaleactor",
				"Chemical Plant",
				null,
				CORE.MODID+":textures/gui/FluidReactor",
				0,
				0,
				0,
				2,
				1,
				"Tier: ",
				1,
				E,
				true,
				false);


		//RTG Fuel Map
		public static final GT_Recipe.GT_Recipe_Map_Fuel sRTGFuels = new GTPP_Recipe.GT_Recipe_Map_Fuel(
				new HashSet<GT_Recipe>(10), "gtpp.recipe.RTGgenerators", "RTG", null,
				"gregtech:textures/gui/basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 365, " Minecraft Days", true, true);

		//Thermal Boiler map
		public static final GT_Recipe.GT_Recipe_Map_Fuel sThermalFuels = new GT_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gtpp.recipe.thermalgeneratorfuel",
				"Thermal Generator Fuel", null, "gregtech:textures/gui/basicmachines/Default", 1, 1, 0, 0, 1,
				null, 1000, null, true, false);

		//Solar Tower map
		public static final GT_Recipe.GT_Recipe_Map_Fuel sSolarTowerRecipes = new GT_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gtpp.recipe.solartower",
				"Solar Tower", null, "gregtech:textures/gui/basicmachines/Default", 1, 1, 0, 0, 1,
				null, 1000, null, true, false);

		//Cyclotron recipe map
		public static final GTPP_Recipe_Map_Internal sCyclotronRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.cyclotron", "COMET - Compact Cyclotron", null, RES_PATH_GUI + "basicmachines/BlastSmelter", 2, 16, 0, 0, 1, E, 1, E, true, true);

		//Advanced Mixer
		public static final GTPP_Recipe_Map_Internal sAdvancedMixerRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(1000), "gtpp.recipe.advanced.mixer",
				"Advanced Material Combiner", null, "gregtech:textures/gui/basicmachines/MixerAdvanced", 4, 4, 1, 0, 2, "", 1, "", true, true);


		//Mini Fusion
		public static final GTPP_Recipe_Map_Internal sSlowFusionRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(50), "gtpp.recipe.slowfusionreactor",
				"Mimir - Slow Fusion", null, "gregtech:textures/gui/basicmachines/LFTR", 0, 0, 0, 2, 1, "Start: ", 1,
				" EU", true, true);


		//Component Assembler
		public static final GT_Recipe_Map sComponentAssemblerRecipes = new GT_Recipe_Map_Assembler(new HashSet<GT_Recipe>(300), "gtpp.recipe.componentassembler", "Component Assembler", null, RES_PATH_GUI + "basicmachines/Assembler", 6, 1, 1, 0, 1, E, 1, E, true, true);

		//Special Maps for Multis
		public static final GTPP_Recipe_Map_Internal sFishPondRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(3), "gtpp.recipe.fishpond", "Zhuhai - Fishing Port", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 0, 1, 0, 0, 1, "Requires Circuit: ", 1, ".", true, true);
		public static final GTPP_Recipe_Map_Internal sSpargeTowerRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.spargetower", "Sparging", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 0, 0, 1, E, 1, E, true, false);

		//public static final GTPP_Recipe_Map sMultiblockCentrifugeRecipes = new GT_Recipe_Map_LargeCentrifuge();
		//public static final GTPP_Recipe_Map sMultiblockElectrolyzerRecipes = new GT_Recipe_Map_LargeElectrolyzer();
		//public static final GTPP_Recipe_Map sAdvFreezerRecipes = new GT_Recipe_Map_AdvancedVacuumFreezer();

		public static final GTPP_Recipe_Map_Internal sAdvFreezerRecipes_GT = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(2000), "gtpp.recipe.cryogenicfreezer", "Cryogenic Freezer", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 0, 0, 1, "", 0, "", false, true);
		public static final GTPP_Recipe_Map_Internal sMultiblockCentrifugeRecipes_GT = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(2000), "gtpp.recipe.multicentrifuge", "Multiblock Centrifuge", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 0, 0, 1, "", 0, "", false, true);
		public static final GTPP_Recipe_Map_Internal sMultiblockElectrolyzerRecipes_GT = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(2000), "gtpp.recipe.multielectro", "Multiblock Electrolyzer", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 0, 0, 1, "", 0, "", false, true);
		public static final GTPP_Recipe_Map_Internal sChemicalPlant_GT = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(2000), "gtpp.recipe.temp4", "temp4", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 0, 0, 0, 0, 0, "", 0, "", false, false);
		public static final GTPP_Recipe_Map_Internal sMultiblockMixerRecipes_GT = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(2000), "gtpp.recipe.multimixer", "Multiblock Mixer", null, RES_PATH_GUI + "basicmachines/FissionFuel", 12, 9, 0, 0, 1, "", 0, "", false, true);

		//Semi-Fluid Fuel Map
		public static final GT_Recipe_Map_Fuel sSemiFluidLiquidFuels = new GT_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gtpp.recipe.semifluidgeneratorfuels", "Semifluid Generator Fuels", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, true);

		// Flotation Cell
		public static final GTPP_Recipe_Map_Internal sFlotationCellRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.flotationcell", "Flotation Cell", null, RES_PATH_GUI + "basicmachines/LFTR", 6, 4, 1, 1, 1, "Ore Key: ", 1, E, true, false);

		// Tree Growth Simulator
		public static final GTPP_Recipe_Map_Internal sTreeSimFakeRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(100), "gtpp.recipe.treefarm", "Tree Growth Simulator", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 1, 0, 1, "", 1, "", false, true);


		/**
		 * HashMap of Recipes based on their Items
		 */
		public final Map<GT_ItemStack, Collection<GTPP_Recipe>> mRecipeItemMap = new HashMap<>();
		/**
		 * HashMap of Recipes based on their Fluids
		 */
		public final Map<Fluid, Collection<GTPP_Recipe>> mRecipeFluidMap = new HashMap<>();
		/**
		 * The List of all Recipes
		 */
		public final Collection<GTPP_Recipe> mRecipeList;
		/**
		 * String used as an unlocalised Name.
		 */
		public final String mUnlocalizedName;
		/**
		 * String used in NEI for the Recipe Lists. If null it will use the unlocalised Name instead
		 */
		public final String mNEIName;
		/**
		 * GUI used for NEI Display. Usually the GUI of the Machine itself
		 */
		public final String mNEIGUIPath;
		public final String mNEISpecialValuePre, mNEISpecialValuePost;
		public final int mUsualInputCount, mUsualOutputCount, mNEISpecialValueMultiplier, mMinimalInputItems, mMinimalInputFluids, mAmperage;
		public final boolean mNEIAllowed, mShowVoltageAmperageInNEI;

		/**
		 * Initialises a new type of Recipe Handler.
		 *
		 * @param aRecipeList                a List you specify as Recipe List. Usually just an ArrayList with a pre-initialised Size.
		 * @param aUnlocalizedName           the unlocalised Name of this Recipe Handler, used mainly for NEI.
		 * @param aLocalName                 the displayed Name inside the NEI Recipe GUI.
		 * @param aNEIGUIPath                the displayed GUI Texture, usually just a Machine GUI. Auto-Attaches ".png" if forgotten.
		 * @param aUsualInputCount           the usual amount of Input Slots this Recipe Class has.
		 * @param aUsualOutputCount          the usual amount of Output Slots this Recipe Class has.
		 * @param aNEISpecialValuePre        the String in front of the Special Value in NEI.
		 * @param aNEISpecialValueMultiplier the Value the Special Value is getting Multiplied with before displaying
		 * @param aNEISpecialValuePost       the String after the Special Value. Usually for a Unit or something.
		 * @param aNEIAllowed                if NEI is allowed to display this Recipe Handler in general.
		 */
		public GTPP_Recipe_Map(final Collection<GTPP_Recipe> aRecipeList,
				final String aUnlocalizedName, final String aLocalName, final String aNEIName,
				final String aNEIGUIPath, final int aUsualInputCount,
				final int aUsualOutputCount, final int aMinimalInputItems,
				final int aMinimalInputFluids, final int aAmperage,
				final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier,
				final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI,
				final boolean aNEIAllowed) {
			sMappings.add(this);
			this.mNEIAllowed = aNEIAllowed;
			this.mShowVoltageAmperageInNEI = aShowVoltageAmperageInNEI;
			this.mRecipeList = aRecipeList;
			this.mNEIName = aNEIName == null ? aUnlocalizedName : aNEIName;
			this.mNEIGUIPath = aNEIGUIPath.endsWith(".png") ? aNEIGUIPath : aNEIGUIPath + ".png";
			this.mNEISpecialValuePre = aNEISpecialValuePre;
			this.mNEISpecialValueMultiplier = aNEISpecialValueMultiplier;
			this.mNEISpecialValuePost = aNEISpecialValuePost;
			this.mAmperage = aAmperage;
			this.mUsualInputCount = aUsualInputCount;
			this.mUsualOutputCount = aUsualOutputCount;
			this.mMinimalInputItems = aMinimalInputItems;
			this.mMinimalInputFluids = aMinimalInputFluids;
			GregTech_API.sFluidMappings.add(this.mRecipeFluidMap);
			GregTech_API.sItemStackMappings.add(this.mRecipeItemMap);
			GT_LanguageManager.addStringLocalization(this.mUnlocalizedName = aUnlocalizedName, aLocalName);
		}

		public GTPP_Recipe addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new GTPP_Recipe(aOptimize, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		public GTPP_Recipe addRecipe(final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new GTPP_Recipe(false, null, null, null, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue), false, false, false);
		}

		public GTPP_Recipe addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new GTPP_Recipe(aOptimize, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		public GTPP_Recipe addRecipe(final boolean aOptimize, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new GTPP_Recipe(aOptimize, null, null, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/*public GregtechRecipe addRecipe(boolean aOptimize, FluidStack aInput1, FluidStack aOutput1, ItemStack[] bInput1, ItemStack[] bOutput1, int aDuration, int aEUt, int aSpecialValue) {
			 return addRecipe(new GregtechRecipe(aOptimize, aInput1, aOutput1, bInput1,bOutput1, aDuration, aEUt, aSpecialValue));

			}*/

		public GTPP_Recipe addRecipe(final GTPP_Recipe aRecipe) {
			Logger.WARNING("Adding Recipe Method 1");
			return this.addRecipe(aRecipe, true, false, false);
		}

		protected GTPP_Recipe addRecipe(final GTPP_Recipe aRecipe, final boolean aCheckForCollisions, final boolean aFakeRecipe, final boolean aHidden) {
			Logger.WARNING("Adding Recipe Method 2 - This Checks if hidden, fake or if duplicate recipes exists, I think.");
			aRecipe.mHidden = aHidden;
			aRecipe.mFakeRecipe = aFakeRecipe;
			Logger.WARNING("Logging some data about this method: GregtechRecipe["+aRecipe.toString()+"] | aCheckForCollisions["+aCheckForCollisions+"] | aFakeRecipe["+aFakeRecipe+"] | aHidden["+aHidden+"]");
			Logger.WARNING("Logging some data about this method: mMinimalInputFluids["+this.mMinimalInputFluids+"] | mMinimalInputItems["+this.mMinimalInputItems+"] | aRecipe.mFluidInputs.length["+aRecipe.mFluidInputs.length+"] | aRecipe.mInputs.length["+aRecipe.mInputs.length+"]");
			if ((aRecipe.mFluidInputs.length < this.mMinimalInputFluids) && (aRecipe.mInputs.length < this.mMinimalInputItems)){
				Logger.WARNING("Step 2 failed");
				return null;}

			Logger.WARNING("Logging some data about this method: aCheckForCollisions["+aCheckForCollisions+"] | findRecipe != null ["+(this.findRecipe(null, false, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)+"]");
			if (aCheckForCollisions && (this.findRecipe(null, false, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)){
				Logger.WARNING("Step 2 failed - 2");
				return null;
			}
			return this.add(aRecipe);
		}



		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public GTPP_Recipe addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addFakeRecipe(aCheckForCollisions, new GTPP_Recipe(false, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public GTPP_Recipe addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addFakeRecipe(aCheckForCollisions, new GTPP_Recipe(false, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public GTPP_Recipe addFakeRecipe(final boolean aCheckForCollisions, final GTPP_Recipe aRecipe) {
			return this.addRecipe(aRecipe, aCheckForCollisions, true, false);
		}

		public GTPP_Recipe add(final GTPP_Recipe aRecipe) {
			Logger.WARNING("Adding Recipe Method 3");
			this.mRecipeList.add(aRecipe);
			for (final FluidStack aFluid : aRecipe.mFluidInputs) {
				if (aFluid != null) {
					Logger.WARNING("Fluid is valid - getting some kind of fluid instance to add to the recipe hashmap.");
					Collection<GTPP_Recipe> tList = this.mRecipeFluidMap.get(aFluid.getFluid());
					if (tList == null) {
						this.mRecipeFluidMap.put(aFluid.getFluid(), tList = new HashSet<>(1));
					}
					tList.add(aRecipe);
				}
			}
			return this.addToItemMap(aRecipe);
		}

		public void reInit() {
			final Map<GT_ItemStack, Collection<GTPP_Recipe>> tMap = this.mRecipeItemMap;
			if (tMap != null) {
				tMap.clear();
			}
			for (final GTPP_Recipe tRecipe : this.mRecipeList) {
				GT_OreDictUnificator.setStackArray(true, tRecipe.mInputs);
				GT_OreDictUnificator.setStackArray(true, tRecipe.mOutputs);
				if (tMap != null) {
					this.addToItemMap(tRecipe);
				}
			}
		}

		/**
		 * @return if this Item is a valid Input for any for the Recipes
		 */
		public boolean containsInput(final ItemStack aStack) {
			return (aStack != null) && (this.mRecipeItemMap.containsKey(new GT_ItemStack(aStack)) || this.mRecipeItemMap.containsKey(new GT_ItemStack(GT_Utility.copyMetaData(W, aStack))));
		}

		/**
		 * @return if this Fluid is a valid Input for any for the Recipes
		 */
		public boolean containsInput(final FluidStack aFluid) {
			return (aFluid != null) && this.containsInput(aFluid.getFluid());
		}

		/**
		 * @return if this Fluid is a valid Input for any for the Recipes
		 */
		public boolean containsInput(final Fluid aFluid) {
			return (aFluid != null) && this.mRecipeFluidMap.containsKey(aFluid);
		}

		public GTPP_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
			return this.findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, null, aInputs);
		}

		public GTPP_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GTPP_Recipe aRecipe, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
			return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, null, aInputs);
		}

		/**
		 * finds a Recipe matching the aFluid and ItemStack Inputs.
		 *
		 * @param aTileEntity    an Object representing the current coordinates of the executing Block/Entity/Whatever. This may be null, especially during Startup.
		 * @param aRecipe        in case this is != null it will try to use this Recipe first when looking things up.
		 * @param aNotUnificated if this is T the Recipe searcher will unificate the ItemStack Inputs
		 * @param aVoltage       Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
		 * @param aFluids        the Fluid Inputs
		 * @param aSpecialSlot   the content of the Special Slot, the regular Manager doesn't do anything with this, but some custom ones do.
		 * @param aInputs        the Item Inputs
		 * @return the Recipe it has found or null for no matching Recipe
		 */
		public GTPP_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GTPP_Recipe aRecipe, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack aSpecialSlot, ItemStack... aInputs) {
			// No Recipes? Well, nothing to be found then.
			if (this.mRecipeList.isEmpty()) {
				return null;
			}

			// Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
			// This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
			if (GregTech_API.sPostloadFinished) {
				if (this.mMinimalInputFluids > 0) {
					if (aFluids == null) {
						return null;
					}
					int tAmount = 0;
					for (final FluidStack aFluid : aFluids) {
						if (aFluid != null) {
							tAmount++;
						}
					}
					if (tAmount < this.mMinimalInputFluids) {
						return null;
					}
				}
				if (this.mMinimalInputItems > 0) {
					if (aInputs == null) {
						return null;
					}
					int tAmount = 0;
					for (final ItemStack aInput : aInputs) {
						if (aInput != null) {
							tAmount++;
						}
					}
					if (tAmount < this.mMinimalInputItems) {
						return null;
					}
				}
			}

			// Unification happens here in case the Input isn't already unificated.
			if (aNotUnificated) {
				aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
			}

			// Check the Recipe which has been used last time in order to not have to search for it again, if possible.
			if (aRecipe != null) {
				if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
					return aRecipe.mEnabled && ((aVoltage * this.mAmperage) >= aRecipe.mEUt) ? aRecipe : null;
				}
			}

			// Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
			if ((this.mUsualInputCount > 0) && (aInputs != null)) {
				for (final ItemStack tStack : aInputs) {
					if (tStack != null) {
						Collection<GTPP_Recipe>
						tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(tStack));
						if (tRecipes != null) {
							for (final GTPP_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
									return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
								}
							}
						}
						tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(W, tStack)));
						if (tRecipes != null) {
							for (final GTPP_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
									return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
								}
							}
						}
					}
				}
			}

			// If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map too.
			if ((this.mMinimalInputItems == 0) && (aFluids != null)) {
				for (final FluidStack aFluid : aFluids) {
					if (aFluid != null) {
						final Collection<GTPP_Recipe>
						tRecipes = this.mRecipeFluidMap.get(aFluid.getFluid());
						if (tRecipes != null) {
							for (final GTPP_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
									return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
								}
							}
						}
					}
				}
			}

			// And nothing has been found.
			return null;
		}

		protected GTPP_Recipe addToItemMap(final GTPP_Recipe aRecipe) {
			Logger.WARNING("Adding Recipe Method 4");
			for (final ItemStack aStack : aRecipe.mInputs) {
				if (aStack != null) {
					Logger.WARNING("Method 4 - Manipulating "+aStack.getDisplayName());
					final GT_ItemStack tStack = new GT_ItemStack(aStack);
					Logger.WARNING("Method 4 - Made gt stack of item "+tStack.toStack().getDisplayName());
					Collection<GTPP_Recipe> tList = this.mRecipeItemMap.get(tStack);
					if (tList != null){
						Logger.WARNING("Method 4 - Gt Recipe Hashmap: "+tList.toString());
					}
					if (tList == null){
						Logger.WARNING("Method 4 - brrr list was NUll");
						this.mRecipeItemMap.put(tStack, tList = new HashSet<>(1));
						Logger.WARNING("Method 4 - Attemping backup method for Gt Recipe Hashmap:");

						while (tList.iterator().hasNext()){
							Logger.WARNING(tList.iterator().next().toString());
						}

					}
					tList.add(aRecipe);
					Logger.WARNING("Method 4 - Added recipe to map? I think.");
				}
			}
			return aRecipe;
		}

		public GTPP_Recipe findRecipe(final IGregTechTileEntity baseMetaTileEntity, final GTPP_Recipe aRecipe, final boolean aNotUnificated,
				final long aVoltage, final FluidStack[] aFluids, final FluidStack[] fluidStacks) {

			ItemStack aInputs[] = null;
			// No Recipes? Well, nothing to be found then.
			if (this.mRecipeList.isEmpty()) {
				return null;
			}

			// Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
			// This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
			if (GregTech_API.sPostloadFinished) {
				if (this.mMinimalInputFluids > 0) {
					if (aFluids == null) {
						return null;
					}
					int tAmount = 0;
					for (final FluidStack aFluid : aFluids) {
						if (aFluid != null) {
							tAmount++;
						}
					}
					if (tAmount < this.mMinimalInputFluids) {
						return null;
					}
				}
				if (this.mMinimalInputItems > 0) {
					if (aInputs == null) {
						return null;
					}
					int tAmount = 0;
					for (final ItemStack aInput : aInputs) {
						if (aInput != null) {
							tAmount++;
						}
					}
					if (tAmount < this.mMinimalInputItems) {
						return null;
					}
				}
			}

			// Unification happens here in case the Input isn't already unificated.
			if (aNotUnificated) {
				aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
			}

			// Check the Recipe which has been used last time in order to not have to search for it again, if possible.
			if (aRecipe != null) {
				if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
					return aRecipe.mEnabled && ((aVoltage * this.mAmperage) >= aRecipe.mEUt) ? aRecipe : null;
				}
			}

			// Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
			if ((this.mUsualInputCount > 0) && (aInputs != null)) {
				for (final ItemStack tStack : aInputs) {
					if (tStack != null) {
						Collection<GTPP_Recipe>
						tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(tStack));
						if (tRecipes != null) {
							for (final GTPP_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
									return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
								}
							}
						}
						tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(W, tStack)));
						if (tRecipes != null) {
							for (final GTPP_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
									return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
								}
							}
						}
					}
				}
			}

			// If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map too.
			if ((this.mMinimalInputItems == 0) && (aFluids != null)) {
				for (final FluidStack aFluid : aFluids) {
					if (aFluid != null) {
						final Collection<GTPP_Recipe>
						tRecipes = this.mRecipeFluidMap.get(aFluid.getFluid());
						if (tRecipes != null) {
							for (final GTPP_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
									return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
								}
							}
						}
					}
				}
			}

			// And nothing has been found.
			return null;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Here are a few Classes I use for Special Cases in some Machines without having to write a separate Machine Class.
	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * Abstract Class for general Recipe Handling of non GT Recipes
	 */
	public static abstract class GT_Recipe_Map_NonGTRecipes extends GTPP_Recipe_Map {
		public GT_Recipe_Map_NonGTRecipes(final Collection<GTPP_Recipe> aRecipeList, final String aUnlocalizedName, final String aLocalName, final String aNEIName, final String aNEIGUIPath, final int aUsualInputCount, final int aUsualOutputCount, final int aMinimalInputItems, final int aMinimalInputFluids, final int aAmperage, final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier, final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI, final boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		@Override
		public boolean containsInput(final ItemStack aStack) {
			return false;
		}

		@Override
		public boolean containsInput(final FluidStack aFluid) {
			return false;
		}

		@Override
		public boolean containsInput(final Fluid aFluid) {
			return false;
		}

		@Override
		public GTPP_Recipe addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public GTPP_Recipe addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public GTPP_Recipe addRecipe(final GTPP_Recipe aRecipe) {
			return null;
		}

		@Override
		public GTPP_Recipe addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public GTPP_Recipe addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public GTPP_Recipe addFakeRecipe(final boolean aCheckForCollisions, final GTPP_Recipe aRecipe) {
			return null;
		}

		@Override
		public GTPP_Recipe add(final GTPP_Recipe aRecipe) {
			return null;
		}

		@Override
		public void reInit() {/**/}

		@Override
		protected GTPP_Recipe addToItemMap(final GTPP_Recipe aRecipe) {
			return null;
		}
	}

	/**
	 * Just a Recipe Map with Utility specifically for Fuels.
	 */
	public static class Gregtech_Recipe_Map_Fuel extends GTPP_Recipe_Map {
		public Gregtech_Recipe_Map_Fuel(final Collection<GTPP_Recipe> aRecipeList, final String aUnlocalizedName, final String aLocalName, final String aNEIName, final String aNEIGUIPath, final int aUsualInputCount, final int aUsualOutputCount, final int aMinimalInputItems, final int aMinimalInputFluids, final int aAmperage, final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier, final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI, final boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		public GTPP_Recipe addFuel(final ItemStack aInput, final ItemStack aOutput, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 1");
			return this.addFuel(aInput, aOutput, null, null, 10000, aFuelValueInEU);
		}

		public GTPP_Recipe addFuel(final ItemStack aInput, final ItemStack aOutput, final int aChance, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 2");
			return this.addFuel(aInput, aOutput, null, null, aChance, aFuelValueInEU);
		}

		public GTPP_Recipe addFuel(final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 3");
			return this.addFuel(null, null, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
		}

		public GTPP_Recipe addFuel(final ItemStack aInput, final ItemStack aOutput, final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 4");
			return this.addFuel(aInput, aOutput, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
		}

		public GTPP_Recipe addFuel(final ItemStack aInput, final ItemStack aOutput, final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aChance, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 5");
			return this.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, new int[]{aChance}, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, 0, 0, aFuelValueInEU);
		}
	}

	@Override
	public ArrayList<PositionedStack> getInputPositionedStacks() {
		return null;
	}

	@Override
	public ArrayList<PositionedStack> getOutputPositionedStacks() {
		return null;
	}

	public int compareTo(GTPP_Recipe recipe) {
		// first lowest tier recipes
		// then fastest
		// then with lowest special value
		// then dry recipes
		// then with fewer inputs
		if (this.mEUt != recipe.mEUt) {
			return this.mEUt - recipe.mEUt;
		} else if (this.mDuration != recipe.mDuration) {
			return this.mDuration - recipe.mDuration;
		} else if (this.mSpecialValue != recipe.mSpecialValue) {
			return this.mSpecialValue - recipe.mSpecialValue;
		} else if (this.mFluidInputs.length != recipe.mFluidInputs.length) {
			return this.mFluidInputs.length - recipe.mFluidInputs.length;
		} else if (this.mInputs.length != recipe.mInputs.length) {
			return this.mInputs.length - recipe.mInputs.length;
		}
		return 0;
	}

	@Override
	public int compareTo(GT_Recipe recipe) {
		// first lowest tier recipes
		// then fastest
		// then with lowest special value
		// then dry recipes
		// then with fewer inputs
		if (this.mEUt != recipe.mEUt) {
			return this.mEUt - recipe.mEUt;
		} else if (this.mDuration != recipe.mDuration) {
			return this.mDuration - recipe.mDuration;
		} else if (this.mSpecialValue != recipe.mSpecialValue) {
			return this.mSpecialValue - recipe.mSpecialValue;
		} else if (this.mFluidInputs.length != recipe.mFluidInputs.length) {
			return this.mFluidInputs.length - recipe.mFluidInputs.length;
		} else if (this.mInputs.length != recipe.mInputs.length) {
			return this.mInputs.length - recipe.mInputs.length;
		}
		return 0;
	}

}
