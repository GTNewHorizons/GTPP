package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GTPP_Recipe;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMetaTileEntity_IndustrialCokeOven extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialCokeOven> {
	
	private int mLevel = 0;
	private int mCasing;
	private int mCasing1;
	private int mCasing2;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialCokeOven> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_IndustrialCokeOven(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialCokeOven(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialCokeOven(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Coke Oven";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Processes Logs and Coal into Charcoal and Coal Coke.")
				.addInfo("Controller Block for the Industrial Coke Oven")
				.addInfo("Gain 4% energy discount per voltage tier")
				.addInfo("Process 12x materials with Heat Resistant Casings")
				.addInfo("Or 24x materials with Heat Proof Casings")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 3, 3, true)
				.addController("Front middle at bottom")
				.addCasingInfo("Structural Coke Oven Casings", 8)
				.addCasingInfo("Heat Resistant/Proof Coke Oven Casings", 8)
				.addInputBus("Any Casing", 1)
				.addOutputBus("Any Casing", 1)
				.addInputHatch("Any Casing", 1)
				.addOutputHatch("Any Casing", 1)
				.addEnergyHatch("Any Casing", 1)
				.addMaintenanceHatch("Any Casing", 1)
				.addMufflerHatch("Any Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialCokeOven> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialCokeOven>builder()
					.addShape(mName, transpose(new String[][]{
							{"CCC", "CCC", "CCC"},
							{"HHH", "H-H", "HHH"},
							{"C~C", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_IndustrialCokeOven::addIndustrialCokeOvenList, TAE.GTPP_INDEX(1), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasingsMisc, 1
											)
									)
							)
					)
					.addElement(
							'H',
							ofChain(
									onElementPass(
											x -> ++x.mCasing1,
											ofBlock(
													ModBlocks.blockCasingsMisc, 2
											)
									),
									onElementPass(
											x -> ++x.mCasing2,
											ofBlock(
													ModBlocks.blockCasingsMisc, 3
											)
									)
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 1, 2, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		mCasing1 = 0;
		mCasing2 = 0;
		mLevel = 0;
		if (checkPiece(mName, 1, 2, 0)) {
			if (mCasing1 == 8) mLevel = 1;
			if (mCasing2 == 8) mLevel = 2;
			return mLevel > 0 && mCasing >= 8 && checkHatch();
		}
		return false;
	}

	public final boolean addIndustrialCokeOvenList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(207));
	}

	@Override
	protected IIconContainer getActiveOverlay() {
		return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
	}

	@Override
	protected IIconContainer getInactiveOverlay() {
		return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
	}

	@Override
	protected int getCasingTextureId() {
		return TAE.GTPP_INDEX(1);
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "CokeOven";
	}	

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes;

	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 0);
	}

	@Override
	public int getMaxParallelRecipes() {
		return this.mLevel * 12;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return (100-(GT_Utility.getTier(this.getMaxInputVoltage())*4));
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialCokeOven;
	}

	@Override
	public int getAmountOfOutputs() {
		return 24;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}
}
