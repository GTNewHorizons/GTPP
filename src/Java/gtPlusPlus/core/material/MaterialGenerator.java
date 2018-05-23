package gtPlusPlus.core.material;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.item.base.bolts.BaseItemBolt;
import gtPlusPlus.core.item.base.dusts.BaseItemDust;
import gtPlusPlus.core.item.base.gears.BaseItemGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ingots.BaseItemIngotHot;
import gtPlusPlus.core.item.base.nugget.BaseItemNugget;
import gtPlusPlus.core.item.base.ore.*;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.base.rings.BaseItemRing;
import gtPlusPlus.core.item.base.rods.BaseItemRod;
import gtPlusPlus.core.item.base.rods.BaseItemRodLong;
import gtPlusPlus.core.item.base.rotors.BaseItemRotor;
import gtPlusPlus.core.item.base.screws.BaseItemScrew;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.loaders.*;

public class MaterialGenerator {

	public static final AutoMap<Set<Runnable>> mRecipeMapsToGenerate = new AutoMap<Set<Runnable>>();
	
	static {
		mRecipeMapsToGenerate.put(RecipeGen_DustGeneration.mRecipeGenMap);
		mRecipeMapsToGenerate.put(RecipeGen_MaterialProcessing.mRecipeGenMap);		
		mRecipeMapsToGenerate.put(RecipeGen_Fluids.mRecipeGenMap);		
		mRecipeMapsToGenerate.put(RecipeGen_ShapedCrafting.mRecipeGenMap);		
		mRecipeMapsToGenerate.put(RecipeGen_Assembler.mRecipeGenMap);		
		mRecipeMapsToGenerate.put(RecipeGen_Extruder.mRecipeGenMap);		
		mRecipeMapsToGenerate.put(RecipeGen_Plates.mRecipeGenMap);		
		mRecipeMapsToGenerate.put(RecipeGen_AlloySmelter.mRecipeGenMap);		
		mRecipeMapsToGenerate.put(RecipeGen_BlastSmelter.mRecipeGenMap);
	}
	
	public static void generate(final Material matInfo){
		generate(matInfo, true);
	}


	public static void generate(final Material matInfo, final boolean generateEverything){
		generate(matInfo, generateEverything, true);
	}

	public static boolean generate(final Material matInfo, final boolean generateEverything, final boolean generateBlastSmelterRecipes){
		try {
			final String unlocalizedName = matInfo.getUnlocalizedName();
			final String materialName = matInfo.getLocalizedName();
			final short[] C = matInfo.getRGBA();
			final int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);
			final boolean hotIngot = matInfo.requiresBlastFurnace();
			int materialTier = matInfo.vTier; //TODO

			if ((materialTier > 10) || (materialTier <= 0)){
				materialTier = 2;
			}

			int sRadiation = 0;
			if (ItemUtils.isRadioactive(materialName) || (matInfo.vRadiationLevel != 0)){
				sRadiation = matInfo.vRadiationLevel;
			}

			if (matInfo.getState() == MaterialState.SOLID){
				if (generateEverything == true){
					if (sRadiation >= 1){
						Item temp;
						Block tempBlock;
						tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
						temp = new BaseItemIngot(matInfo);

						temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
						temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
						temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);
						temp = new BaseItemNugget(matInfo);
						temp = new BaseItemPlate(matInfo);
						temp = new BaseItemRod(matInfo);
						temp = new BaseItemRodLong(matInfo);
					}

					else {
						Item temp;
						Block tempBlock;
						tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
						tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.FRAME, Colour);
						temp = new BaseItemIngot(matInfo);
						if (hotIngot){
							temp = new BaseItemIngotHot(matInfo);
						}
						temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
						temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
						temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);
						temp = new BaseItemNugget(matInfo);
						temp = new BaseItemPlate(matInfo);
						temp = new BaseItemPlateDouble(matInfo);
						temp = new BaseItemBolt(matInfo);
						temp = new BaseItemRod(matInfo);
						temp = new BaseItemRodLong(matInfo);
						temp = new BaseItemRing(matInfo);
						temp = new BaseItemScrew(matInfo);
						temp = new BaseItemRotor(matInfo);
						temp = new BaseItemGear(matInfo);
					}
				} else {
					Item temp;
					Block tempBlock;
					tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);

					temp = new BaseItemIngot(matInfo);
					temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
					temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
					temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);
					temp = new BaseItemNugget(matInfo);
					temp = new BaseItemPlate(matInfo);
					temp = new BaseItemPlateDouble(matInfo);
				}
			}
			else if (matInfo.getState() == MaterialState.LIQUID){
				Item temp;
				if (generateEverything == true){
					Block tempBlock;
					tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
				}
				temp = new BaseItemIngot(matInfo);
				temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation);
				temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation);
				temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation);
				temp = new BaseItemNugget(matInfo);
				temp = new BaseItemPlate(matInfo);
				temp = new BaseItemPlateDouble(matInfo);
			}
			else if (matInfo.getState() == MaterialState.PURE_LIQUID){
				FluidUtils.generateFluidNoPrefix(unlocalizedName,	materialName, matInfo.getMeltingPointK(), C);
				return true;
			}
			else if (matInfo.getState() == MaterialState.ORE){

			}

			//Add A jillion Recipes - old code
			new RecipeGen_AlloySmelter(matInfo);
			new RecipeGen_Assembler(matInfo);
			if (generateBlastSmelterRecipes){
				new RecipeGen_BlastSmelter(matInfo);
			}
			new RecipeGen_Extruder(matInfo);
			new RecipeGen_Fluids(matInfo);
			new RecipeGen_Plates(matInfo);
			new RecipeGen_ShapedCrafting(matInfo);
			new RecipeGen_MaterialProcessing(matInfo);
			
			new RecipeGen_DustGeneration(matInfo);
			new RecipeGen_Recycling(matInfo);
			return true;

		} catch (final Throwable t)

		{
			Logger.WARNING(""+matInfo.getLocalizedName()+" failed to generate.");
			return false;
		}
	}

	public static void generateDusts(final Material matInfo){
		final String unlocalizedName = matInfo.getUnlocalizedName();
		final String materialName = matInfo.getLocalizedName();
		final short[] C = matInfo.getRGBA();
		final int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);
		int materialTier = matInfo.vTier; //TODO

		if ((materialTier > 10) || (materialTier <= 0)){
			materialTier = 2;
		}

		int sRadiation = 0;
		if (ItemUtils.isRadioactive(materialName) || (matInfo.vRadiationLevel != 0)){
			sRadiation = matInfo.vRadiationLevel;
		}

		if (matInfo.getState() == MaterialState.SOLID){
			Item temp;
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", materialTier, sRadiation, false);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", materialTier, sRadiation, false);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", materialTier, sRadiation, false);
		}

		//Add A jillion Recipes - old code
		try {
		RecipeGen_DustGeneration.addMixerRecipe_Standalone(matInfo);
		new RecipeGen_Fluids(matInfo);
		new RecipeGen_MaterialProcessing(matInfo);
		}
		catch (Throwable t) {
			Logger.INFO("Failed to generate some recipes for "+materialName);
			Logger.ERROR("Failed to generate some recipes for "+materialName);
			t.printStackTrace();
		}
		//RecipeGen_Recycling.generateRecipes(matInfo);
	}

	public static void generateNuclearMaterial(final Material matInfo){
		generateNuclearMaterial(matInfo, true);
	}

	@SuppressWarnings("unused")
	public static void generateNuclearMaterial(final Material matInfo, final boolean generatePlates){
		try {
			final String unlocalizedName = matInfo.getUnlocalizedName();
			final String materialName = matInfo.getLocalizedName();
			final short[] C = matInfo.getRGBA();
			final int Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);

			int sRadiation = 0;
			if (matInfo.vRadiationLevel != 0){
				sRadiation = matInfo.vRadiationLevel;
			}

			Item temp;
			Block tempBlock;

			tempBlock = new BlockBaseModular(unlocalizedName, materialName,BlockTypes.STANDARD, Colour);
			temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", 3, sRadiation);
			temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", 2, sRadiation);
			temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", 1, sRadiation);

			temp = new BaseItemIngot(matInfo);
			temp = new BaseItemNugget(matInfo);
			
			if (generatePlates) {
				temp = new BaseItemPlate(matInfo);
				temp = new BaseItemPlateDouble(matInfo);
				new RecipeGen_Plates(matInfo);
				new RecipeGen_Extruder(matInfo);
				new RecipeGen_Assembler(matInfo);
			}

			new RecipeGen_ShapedCrafting(matInfo);
			new RecipeGen_Fluids(matInfo);
			new RecipeGen_MaterialProcessing(matInfo);
			new RecipeGen_DustGeneration(matInfo, true);
			new RecipeGen_Recycling(matInfo);			
			
		} catch (final Throwable t){
			Logger.WARNING(""+matInfo.getLocalizedName()+" failed to generate.");
		}
	}


	public static void generateOreMaterial(final Material matInfo){
		generateOreMaterial(matInfo, true, true, true, matInfo.getRGBA());
	}

	@SuppressWarnings("unused")
	public static void generateOreMaterial(final Material matInfo, boolean generateOre, boolean generateDust, boolean generateSmallTinyDusts, short[] customRGB){
		try {

			if (matInfo == null){
				Logger.DEBUG_MATERIALS("Invalid Material while constructing null material.");
				return;
			}

			final String unlocalizedName = matInfo.getUnlocalizedName();
			final String materialName = matInfo.getLocalizedName();
			final short[] C = customRGB;
			final Integer Colour = Utils.rgbtoHexValue(C[0], C[1], C[2]);


			if (Colour == null){
				Logger.DEBUG_MATERIALS("Invalid Material while constructing "+materialName+".");
				return;
			}

			int sRadiation = 0;
			if (matInfo.vRadiationLevel > 0){
				sRadiation = matInfo.vRadiationLevel;
			}

			Item temp;
			Block tempBlock;


			if (generateOre) {
				tempBlock = new BlockBaseOre(matInfo, BlockTypes.ORE, Colour.intValue());		
			}

			if (generateDust) {
				temp = new BaseItemDust("itemDust"+unlocalizedName, materialName, matInfo, Colour, "Dust", matInfo.vTier, sRadiation, false);
			}
			if (generateSmallTinyDusts) {
				temp = new BaseItemDust("itemDustTiny"+unlocalizedName, materialName, matInfo, Colour, "Tiny", matInfo.vTier, sRadiation, false);
				temp = new BaseItemDust("itemDustSmall"+unlocalizedName, materialName, matInfo, Colour, "Small", matInfo.vTier, sRadiation, false);
			}

			temp = new BaseItemCrushedOre(matInfo);
			temp = new BaseItemCentrifugedCrushedOre(matInfo);
			temp = new BaseItemPurifiedCrushedOre(matInfo);
			temp = new BaseItemImpureDust(matInfo);
			temp = new BaseItemPurifiedDust(matInfo);

			RecipeGen_Ore.generateRecipes(matInfo);

		} catch (final Throwable t){
			Logger.MATERIALS("[Error] "+(matInfo != null ? matInfo.getLocalizedName() : "Null Material")+" failed to generate.");
			t.printStackTrace();
		}
	}


}
