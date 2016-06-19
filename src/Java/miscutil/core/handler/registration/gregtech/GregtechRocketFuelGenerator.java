package miscutil.core.handler.registration.gregtech;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.gregtech.api.enums.GregtechItemList;
import miscutil.gregtech.common.tileentities.generators.GregtechMetaTileEntityRocketFuelGenerator;

public class GregtechRocketFuelGenerator {
	
	public static void run()
	{
		if (LoadedMods.Gregtech){
			Utils.LOG_INFO("MiscUtils: Gregtech5u Content | Registering Industrial Rocket Engines.");
			run1();
		}

	}

	private static void run1(){
		GregtechItemList.Rocket_Engine_EV.set(new GregtechMetaTileEntityRocketFuelGenerator(793, "advancedgenerator.rocketFuel.tier.01", "Basic Rocket Engine", 4).getStackForm(1L));
		GregtechItemList.Rocket_Engine_IV.set(new GregtechMetaTileEntityRocketFuelGenerator(794, "advancedgenerator.rocketFuel.tier.02", "Advanced Rocket Engine", 5).getStackForm(1L));
		GregtechItemList.Rocket_Engine_LuV.set(new GregtechMetaTileEntityRocketFuelGenerator(795, "advancedgenerator.rocketFuel.tier.03", "Turbo Rocket Engine", 6).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_EV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_EV, Character.valueOf('P'), ItemList.Electric_Piston_LV, Character.valueOf('E'), ItemList.Electric_Motor_LV, Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Aluminium), Character.valueOf('G'), OrePrefixes.gearGt.get(Materials.Titanium)});
        GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_IV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_IV, Character.valueOf('P'), ItemList.Electric_Piston_MV, Character.valueOf('E'), ItemList.Electric_Motor_MV, Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Platinum), Character.valueOf('G'), OrePrefixes.gearGt.get(Materials.TungstenSteel)});
        GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_LuV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_LuV, Character.valueOf('P'), ItemList.Electric_Piston_HV, Character.valueOf('E'), ItemList.Electric_Motor_HV, Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Master), Character.valueOf('W'), OrePrefixes.cableGt01.get(Materials.Tungsten), Character.valueOf('G'), OrePrefixes.gearGt.get(Materials.Chrome)});

	}
	
}
