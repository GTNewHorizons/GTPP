package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.GregTech_API.sBlockCasings4;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import com.gtnewhorizon.structurelib.structure.*;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntity_Adv_Implosion extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_Adv_Implosion> {

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_Adv_Implosion> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_Adv_Implosion(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_Adv_Implosion(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Adv_Implosion(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Implosion Compressor";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
		.addInfo("Factory Grade Advanced Implosion Compressor")
		.addInfo("Speed: 100% | Eu Usage: 100% | Parallel: ((Tier/2)+1)")
		.addInfo("Constructed exactly the same as a normal Implosion Compressor")
		.addPollutionAmount(getPollutionPerSecond(null))
		.addSeparator()
		.beginStructureBlock(3, 3, 3, true)
		.addController("Front center")
		.addCasingInfo("Robust TungstenSteel Casing", 10)
		.addInputBus("Any casing", 1)
		.addOutputBus("Any casing", 1)
		.addEnergyHatch("Any casing", 1)
		.addMaintenanceHatch("Any casing", 1)
		.addMufflerHatch("Any casing", 1)
		.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_Adv_Implosion> getStructureDefinition() {
		if (this.STRUCTURE_DEFINITION == null) {
			this.STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_Adv_Implosion>builder()
					.addShape(this.mName, transpose(new String[][]{
						{"CCC", "CCC", "CCC"},
						{"C~C", "C-C", "CCC"},
						{"CCC", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_Adv_Implosion::addAdvImplosionList, 48, 1
											),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													sBlockCasings4, 0
													)
											)
									)
							)
					.build();
		}
		return this.STRUCTURE_DEFINITION;
	}

	public final boolean addAdvImplosionList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
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
			}
		}
		return false;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(this.mName, stackSize, hintsOnly, 1, 1, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		this.mCasing = 0;
		return checkPiece(this.mName, 1, 1, 0) && this.mCasing >= 10 && checkHatch();
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(48), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(48)};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "ImplosionCompressor";
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sImplosionRecipes;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric((GT_Utility.getTier(this.getMaxInputVoltage())/2+1), 100, 100);
	}

	@Override
	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 20) {
			GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(Integer.valueOf(5)), 10, 1.0F, aX, aY, aZ);
		}
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(5));
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiAdvImplosion;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public int getMaxParallelRecipes() {
		return (GT_Utility.getTier(this.getMaxInputVoltage())/2+1);
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

}