package gtPlusPlus.core.block;

import gtPlusPlus.core.block.general.LightGlass;
import gtPlusPlus.core.block.machine.Machine_Workbench;
import gtPlusPlus.core.fluids.FluidRegistryHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModBlocks {

	public static Block blockWorkbench;
	//Blocks
	//public static Block blockBloodSteel;
	//public static Block blockStaballoy;
	// WIP TODO public static Block blockToolBuilder;
	public static Block blockGriefSaver;
	public static Block blockCasingsMisc;
	public static Block blockHeliumGenerator;
	public static Block blockNHG;
	public static Block blockCharger;

	public static Block MatterFabricatorEffectBlock;

	public static Fluid fluidJackDaniels = new Fluid("fluidJackDaniels");
	public static Block blockFluidJackDaniels;



	public static void init() {
		Utils.LOG_INFO("Initializing Blocks.");
		//blockGriefSaver = new TowerDevice().setBlockName("blockGriefSaver").setCreativeTab(AddToCreativeTab.tabBlock).setBlockTextureName("blockDefault");

		registerBlocks(); 
	}

	public static void registerBlocks(){

		Utils.LOG_INFO("Registering Blocks.");
		GameRegistry.registerBlock(MatterFabricatorEffectBlock = new LightGlass(Material.glass, false).setHardness(0.1F).setBlockTextureName(CORE.MODID + ":" + "blockMFEffect").setStepSound(Block.soundTypeGlass), "blockMFEffect");


		//Casing Blocks
		blockCasingsMisc = new GregtechMetaCasingBlocks();

		//Fluids
		FluidRegistryHandler.registerFluids();

		//Workbench
		blockWorkbench = new Machine_Workbench().setHardness(1.5F);

		
	}

}