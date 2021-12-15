package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusOutput;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamMacerator;

public class GregtechSteamMultis {

	public static void run(){

		Logger.INFO("Gregtech5u Content | Registering Steam Multiblocks.");
		
		GregtechItemList.Controller_SteamMaceratorMulti.set(new GregtechMetaTileEntity_SteamMacerator(31041, "gtpp.multimachine.steam.macerator", "Steam Grinder").getStackForm(1L));

		GregtechItemList.Hatch_Input_Bus_Steam.set(new GT_MetaTileEntity_Hatch_Steam_BusInput(31046, "hatch.input_bus.tier.steam", "Input Bus (Steam)", 0).getStackForm(1L));
		GregtechItemList.Hatch_Output_Bus_Steam.set(new GT_MetaTileEntity_Hatch_Steam_BusOutput(31047, "hatch.output_bus.tier.steam", "Output Bus (Steam)", 0).getStackForm(1L));
		
	}
	
}
