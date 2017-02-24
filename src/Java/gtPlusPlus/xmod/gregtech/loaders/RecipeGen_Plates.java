package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;

public class RecipeGen_Plates implements Runnable{

	final Material toGenerate;
	
	public RecipeGen_Plates(final Material M){
		this.toGenerate = M;
	}
	
	@Override
	public void run() {
		generateRecipes(toGenerate);		
	}
	
	public static void generateRecipes(final Material material){

		final int tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 60 : 15;
		final ItemStack ingotStackOne = material.getIngot(1);
		final ItemStack ingotStackTwo = material.getIngot(2);
		final ItemStack shape_Mold = ItemList.Shape_Mold_Plate.get(0);
		final ItemStack plate_Single = material.getPlate(1);
		final ItemStack plate_SingleTwo = material.getPlate(2);
		final ItemStack plate_Double = material.getPlateDouble(1);

		Utils.LOG_WARNING("Generating Plate recipes for "+material.getLocalizedName());
		
		//Forge Hammer
		if (addForgeHammerRecipe(
				ingotStackTwo,
				plate_Single,
				(int) Math.max(material.getMass(), 1L),
				16)){
			Utils.LOG_WARNING("Forge Hammer Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("Forge Hammer Recipe: "+material.getLocalizedName()+" - Failed");			
		}
		//Bender
		if (addBenderRecipe(
				ingotStackOne,
				plate_Single,
				(int) Math.max(material.getMass() * 1L, 1L),
				24)){
			Utils.LOG_WARNING("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("Bender Recipe: "+material.getLocalizedName()+" - Failed");			
		}
		//Alloy Smelter
		if (GT_Values.RA.addAlloySmelterRecipe(
				ingotStackTwo,
				shape_Mold,
				plate_Single,
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier)){
			Utils.LOG_WARNING("Alloy Smelter Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("Alloy Smelter Recipe: "+material.getLocalizedName()+" - Failed");			
		}


		//Making Double Plates
		if (addBenderRecipe(
				ingotStackTwo,
				plate_Double,
				(int) Math.max(material.getMass() * 2L, 1L),
				96)){
			Utils.LOG_WARNING("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("Bender Recipe: "+material.getLocalizedName()+" - Failed");			
		}
		if (addBenderRecipe(
				plate_SingleTwo,
				plate_Double,
				(int) Math.max(material.getMass() * 2L, 1L),
				96)){
			Utils.LOG_WARNING("Bender Recipe: "+material.getLocalizedName()+" - Success");
		}
		else {
			Utils.LOG_WARNING("Bender Recipe: "+material.getLocalizedName()+" - Failed");			
		} 
	}

	public static boolean addBenderRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
		if ((aInput1 == null) || (aOutput1 == null)) {
			return false;
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("bender", aInput1, aDuration)) <= 0) {
			return false;
		}
		new GT_Recipe(aEUt, aDuration, aInput1, aOutput1);
		return true;
	}

	public static boolean addExtruderRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
		if ((aInput == null) || (aShape == null) || (aOutput == null)) {
			return false;
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("extruder", aOutput, aDuration)) <= 0) {
			return false;
		}
		GT_Recipe.GT_Recipe_Map.sExtruderRecipes.addRecipe(true, new ItemStack[]{aInput, aShape}, new ItemStack[]{aOutput}, null, null, null, aDuration, aEUt, 0);
		return true;
	}

	public static boolean addForgeHammerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
		if ((aInput1 == null) || (aOutput1 == null)) {
			return false;
		}
		if (!GregTech_API.sRecipeFile.get("forgehammer", aOutput1, true)) {
			return false;
		}
		GT_Recipe.GT_Recipe_Map.sHammerRecipes.addRecipe(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1}, null, null, null, aDuration, aEUt, 0);
		return true;
	}



}
