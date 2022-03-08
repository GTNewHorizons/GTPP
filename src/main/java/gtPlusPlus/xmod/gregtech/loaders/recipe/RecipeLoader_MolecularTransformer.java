package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static advsolar.utils.MTRecipeManager.transformerRecipes;
import gregtech.api.enums.*;
import gregtech.api.interfaces.IMaterialHandler;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import advsolar.utils.MTRecipeRecord;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class RecipeLoader_MolecularTransformer {

	public static void run() {
		
	    for (int i = 0; i < transformerRecipes.size(); i++) {
	    	MTRecipeRecord aRecipe = transformerRecipes.get(i);
	    	int aEU = MaterialUtils.getVoltageForTier(5);
	    	Logger.INFO("=======================");
	    	Logger.INFO("Generating GT recipe for Molecular Transformer.");
	    	Logger.INFO("Input: "+aRecipe.inputStack.getDisplayName()+", Output: "+aRecipe.outputStack.getDisplayName()+", EU/t: "+aEU);
	    	float aTicks = (float) aRecipe.energyPerOperation / (float) aEU;	
	    	Logger.INFO("Ticks: "+aTicks);
	    	int aTicksRoundedUp = MathUtils.roundToClosestInt(Math.ceil(aTicks));
	    	Logger.INFO("Ticks: "+aTicksRoundedUp);
	    	Logger.INFO("Total EU equal or greater? "+((aTicksRoundedUp * aEU) >= aRecipe.energyPerOperation));
	    	CORE.RA.addMolecularTransformerRecipe(aRecipe.inputStack, aRecipe.outputStack, aTicksRoundedUp, aEU, 2);
	    	Logger.INFO("=======================");
	    }
		
		transformerRecipes.clear();
		ORE.RA.addMolecularTransformerRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L),GT_ModHandler.getModItem("AdvancedSolarPanel", "asp_crafting_items", 1L, 9),10240,1920,1);
		CORE.RA.addMolecularTransformerRecipe(GT_ModHandler.getModItem("GalaxySpace", "item.GlowstoneDusts", 1L, 0),GT_ModHandler.getModItem("AdvancedSolarPanel", "asp_crafting_items", 1L, 9),2560,7680,1);
		CORE.RA.addMolecularTransformerRecipe(GT_ModHandler.getModItem("GalaxySpace", "item.GlowstoneDusts", 1L, 1),GT_ModHandler.getModItem("AdvancedSolarPanel", "asp_crafting_items", 1L, 9),640,30720,1);
		CORE.RA.addMolecularTransformerRecipe(GT_ModHandler.getModItem("GalaxySpace", "item.GlowstoneDusts", 1L, 2),GT_ModHandler.getModItem("AdvancedSolarPanel", "asp_crafting_items", 1L, 9),160,122880,1);
		CORE.RA.addMolecularTransformerRecipe(GT_ModHandler.getModItem("GalaxySpace", "item.GlowstoneDusts", 1L, 3),GT_ModHandler.getModItem("AdvancedSolarPanel", "asp_crafting_items", 1L, 9),40,491520,1);
		CORE.RA.addMolecularTransformerRecipe(GT_ModHandler.getModItem("GalaxySpace", "item.GlowstoneDusts", 1L, 4),GT_ModHandler.getModItem("AdvancedSolarPanel", "asp_crafting_items", 1L, 9),10,1996080,1);
		
	}
	
}
