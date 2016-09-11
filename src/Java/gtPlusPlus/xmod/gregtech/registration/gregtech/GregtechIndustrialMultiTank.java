package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityMultiTank;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityMultiTankDense;

public class GregtechIndustrialMultiTank
{



	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Multitank controller blocks.");
			run1();
		}

	}

	private static void run1()
	{
		GregtechItemList.Industrial_MultiTank.set(new GregtechMetaTileEntityMultiTank(827, "multitank.controller.tier.single", "Gregtech Multitank").getStackForm(1L));
		GregtechItemList.Industrial_MultiTankDense.set(new GregtechMetaTileEntityMultiTankDense(828, "multitankdense.controller.tier.single", "Gregtech Dense Multitank").getStackForm(1L));
		
	}
}
