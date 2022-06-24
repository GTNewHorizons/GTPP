package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.List;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GasSpargingRecipe;
import gregtech.api.util.GasSpargingRecipeMap;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_SpargeTower extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_SpargeTower> {

	protected static final String STRUCTURE_PIECE_BASE = "base";
	protected static final String STRUCTURE_PIECE_LAYER = "layer";
	protected static final String STRUCTURE_PIECE_LAYER_HINT = "layerHint";
	protected static final String STRUCTURE_PIECE_TOP_HINT = "topHint";
	private static final IStructureDefinition<GregtechMetaTileEntity_SpargeTower> STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_SpargeTower>builder()
			.addShape(STRUCTURE_PIECE_BASE, transpose(new String[][]{
				{"b~b", "bbb", "bbb"},
			}))
			.addShape(STRUCTURE_PIECE_LAYER, transpose(new String[][]{
				{"lll", "lcl", "lll"}
			}))
			.addShape(STRUCTURE_PIECE_LAYER_HINT, transpose(new String[][]{
				{"lll", "l-l", "lll"}
			}))
			.addShape(STRUCTURE_PIECE_TOP_HINT, transpose(new String[][]{
				{"lll", "lll", "lll"}
			}))
			.addElement('b', ofChain(
					ofHatchAdder(GregtechMetaTileEntity_SpargeTower::addEnergyInputToMachineList, getCasingIndex(), 1),
					ofHatchAdder(GregtechMetaTileEntity_SpargeTower::addInputToMachineList, getCasingIndex(), 1),
					ofHatchAdder(GregtechMetaTileEntity_SpargeTower::addMaintenanceToMachineList, getCasingIndex(), 1),
					onElementPass(GregtechMetaTileEntity_SpargeTower::onCasingFound, ofBlock(ModBlocks.blockCasings5Misc, 4))
					))
			.addElement('l', ofChain(
					ofHatchAdder(GregtechMetaTileEntity_SpargeTower::addEnergyInputToMachineList, getCasingIndex(), 2),
					ofHatchAdder(GregtechMetaTileEntity_SpargeTower::addLayerOutputHatch, getCasingIndex(), 2),
					ofHatchAdder(GregtechMetaTileEntity_SpargeTower::addMaintenanceToMachineList, getCasingIndex(), 2),
					onElementPass(GregtechMetaTileEntity_SpargeTower::onCasingFound, ofBlock(ModBlocks.blockCasings5Misc, 4))
					))
			.addElement('c', ofChain(
					onElementPass(t -> t.onTopLayerFound(false), ofHatchAdder(GregtechMetaTileEntity_SpargeTower::addOutputToMachineList, getCasingIndex(), 3)),
					onElementPass(t -> t.onTopLayerFound(false), ofHatchAdder(GregtechMetaTileEntity_SpargeTower::addMaintenanceToMachineList, getCasingIndex(), 3)),
					onElementPass(t -> t.onTopLayerFound(true), ofBlock(ModBlocks.blockCasings5Misc, 4)),
					isAir()
					))
			.build();

	protected final List<List<GT_MetaTileEntity_Hatch_Output>> mOutputHatchesByLayer = new ArrayList<>();
	protected int mHeight;
	protected int mCasing;
	protected boolean mTopLayerFound;

	public GregtechMetaTileEntity_SpargeTower(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_SpargeTower(String aName) {
		super(aName);
	}

	public static int getCasingIndex() {
		return 68;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_SpargeTower(this.mName);
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType("Gas Sparge Tower")
		.addInfo("Controller block for the Sparging Tower")
		.addInfo("Fluids are only put out at the correct height")
		.addInfo("The correct height equals the slot number in the NEI recipe")
		.addSeparator()
		.beginStructureBlock(3, 8, 3, true)
		.addController("Front bottom")
		.addOtherStructurePart("Sparge Tower Exterior Casing", "45 (minimum)")
		.addEnergyHatch("Any casing", 1, 2)
		.addMaintenanceHatch("Any casing", 1, 2, 3)
		.addInputHatch("2x Input Hatches (Any bottom layer casing)", 1)
		.addOutputHatch("6x Output Hatches (At least one per layer except bottom layer)", 2, 3)
		.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
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
		return getCasingIndex();
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "DistillationTower.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		if (GTPP_Recipe_Map.sSpargeTowerRecipes.mRecipeList.isEmpty()) {
			generateRecipes();
		}
		return GTPP_Recipe_Map.sSpargeTowerRecipes;
	}

	private static boolean generateRecipes() {
		for (GasSpargingRecipe aRecipe : GasSpargingRecipeMap.mRecipes) {
			GTPP_Recipe newRecipe = new GTPP_Recipe(
					false, 
					new ItemStack[] {},
					new ItemStack[] {},
					null,
					null,
					aRecipe.mFluidInputs.clone(),
					new FluidStack[] {},
					aRecipe.mDuration, 
					aRecipe.mEUt, 
					0);
			GTPP_Recipe_Map.sSpargeTowerRecipes.add(newRecipe);
		}
		if (GTPP_Recipe_Map.sSpargeTowerRecipes.mRecipeList.isEmpty()) {
			return false;
		}    	
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<FluidStack> tFluidList = getStoredFluids();
		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(0, GT_Utility.getTier(tVoltage));
		FluidStack[] tFluids = tFluidList.toArray(new FluidStack[0]);
		if (tFluids.length > 0) {
			Logger.INFO("Found "+tFluids.length+" input fluids. Searching "+GTPP_Recipe_Map.sSpargeTowerRecipes.mRecipeList.size()+" recipes.");
			GT_Recipe tRecipe = getRecipeMap().findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids);
			if (tRecipe != null) {
				Logger.INFO("Found recipe!");
				if (tRecipe.isRecipeInputEqual(true, tFluids)) {
					Logger.INFO("Found recipe that matches!");
					this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
					this.mEfficiencyIncrease = 10000;

					// Reset outputs and progress stats
					this.mEUt = 0;
					this.mProgresstime = 0;
					this.mMaxProgresstime = 0;
					this.mOutputItems = new ItemStack[]{};
					this.mOutputFluids = new FluidStack[]{};
					this.mLastRecipe = tRecipe;
					
					calculateOverclockedNessMulti(tRecipe.mEUt, tRecipe.mDuration, 1, tVoltage);
					int aDevProgress = this.mMaxProgresstime / 10;
					this.mMaxProgresstime = Math.max(1, aDevProgress);
					this.mOutputItems = new ItemStack[]{};
					ArrayList<FluidStack> aFluidOutputs = getByproductsOfSparge(tRecipe.mFluidInputs[0], tRecipe.mFluidInputs[1]);					
					this.mOutputFluids = (FluidStack[]) aFluidOutputs.toArray(new FluidStack[0]);
					updateSlots();
					Logger.INFO("Done!");  
					return true;
				}
			}
			else {
				Logger.INFO("Did not find recipe!");        		
			}
		}
		this.mEUt = 0;
		this.mEfficiency = 0;
		Logger.INFO("Did not find recipe! (2)"); 
		return false;
	}

	private static ArrayList<FluidStack> getByproductsOfSparge(final FluidStack aSpargeGas, final FluidStack aSpentFuel){		
		GasSpargingRecipe aSpargeRecipe = GasSpargingRecipeMap.findRecipe(aSpargeGas, aSpentFuel);	
		ArrayList<FluidStack> aOutputGases = new ArrayList<FluidStack>();
		if (aSpargeRecipe == null) {
			Logger.INFO("Did not find sparge recipe!");
			return aOutputGases;
		}
		int aSpargeGasAmount = aSpargeRecipe.mInputGas.amount;

		aOutputGases.add(aSpargeRecipe.mOutputSpargedFuel.copy());	
		ArrayList<FluidStack> aTempMap = new ArrayList<FluidStack>();
		for (int i=2;i<aSpargeRecipe.mFluidOutputs.length;i++) {
			int aGasAmount = MathUtils.randInt(0, (aSpargeRecipe.mMaxOutputQuantity[i-2]/100));
			FluidStack aOutput = aSpargeRecipe.mFluidOutputs[i].copy();
			aSpargeGasAmount -= aGasAmount;
			FluidStack aSpargeOutput = null;
			if (aGasAmount > 0) {
				aSpargeOutput = new FluidStack(aOutput.getFluid(), aGasAmount);
			}
			aTempMap.add(aSpargeOutput);
		}
		Logger.INFO("Sparge gas left: "+aSpargeGasAmount);
		if (aSpargeGasAmount > 0) {
			aOutputGases.add(new FluidStack(aSpargeRecipe.mInputGas.getFluid(), aSpargeGasAmount));	
		}
		//Logger.INFO("Sparge Outputs: "+ItemUtils.getArrayStackNames(aTempMap));
		aOutputGases.addAll(aTempMap);
		Logger.INFO("Sparge output size: "+aOutputGases.size());
		//Logger.INFO("Output of sparging: "+ItemUtils.getArrayStackNames(aOutputGases));
		return aOutputGases;
	}

	protected void onCasingFound() {
		mCasing++;
	}

	protected void onTopLayerFound(boolean aIsCasing) {
		mTopLayerFound = true;
		if (aIsCasing) {
			onCasingFound();
		}
	}

	protected boolean addLayerOutputHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null || aTileEntity.isDead() || !(aTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Output)) {
			Logger.INFO("Bad Output Hatch");
			return false;
		}
		while (mOutputHatchesByLayer.size() < mHeight) {
			mOutputHatchesByLayer.add(new ArrayList<>());
		}
		GT_MetaTileEntity_Hatch_Output tHatch = (GT_MetaTileEntity_Hatch_Output) aTileEntity.getMetaTileEntity();
		tHatch.updateTexture(aBaseCasingIndex);
		boolean addedHatch = mOutputHatchesByLayer.get(mHeight - 1).add(tHatch);
		Logger.INFO("Added Hatch: "+addedHatch);
		return addedHatch;
	}

	@Override
	protected IAlignmentLimits getInitialAlignmentLimits() {
		// don't rotate a freaking tower, it won't work
		return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_SpargeTower> getStructureDefinition() {
		return STRUCTURE_DEFINITION;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		// reset
		mOutputHatchesByLayer.forEach(List::clear);
		mHeight = 1;
		mTopLayerFound = false;
		mCasing = 0;

		// check base
		if (!checkPiece(STRUCTURE_PIECE_BASE, 1, 0, 0)) {
			Logger.INFO("Bad Base. Height: "+mHeight);
			return false;
		}

		// check each layer
		while (mHeight < 8 && checkPiece(STRUCTURE_PIECE_LAYER, 1, mHeight, 0) && !mTopLayerFound) {
			if (mOutputHatchesByLayer.get(mHeight - 1).isEmpty()) {
				// layer without output hatch
				Logger.INFO("Height: "+mHeight + " - Missing output on "+(mHeight - 1));
				return false;
			}
			// not top
			mHeight++;
		}

		// validate final invariants...
		Logger.INFO("Height: "+mHeight);
		Logger.INFO("Casings: "+mCasing);
		Logger.INFO("Required: "+(7 * mHeight - 5));
		Logger.INFO("Found Top: "+mTopLayerFound);
		return mCasing >= 45 && mTopLayerFound && mMaintenanceHatches.size() == 1;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
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
	protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
		for (int i = 0; i < mOutputFluids2.length && i < mOutputHatchesByLayer.size(); i++) {
			FluidStack tStack = mOutputFluids2[i] != null ? mOutputFluids2[i].copy() : null;
			if (tStack == null) {
				continue;
			}
			if (!dumpFluid(mOutputHatchesByLayer.get(i), tStack, true)) {
				dumpFluid(mOutputHatchesByLayer.get(i), tStack, false);
			}
		}
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 1, 0, 0);
		int tTotalHeight = 8; // min 2 output layer, so at least 1 + 2 height
		for (int i = 1; i < tTotalHeight - 1; i++) {
			buildPiece(STRUCTURE_PIECE_LAYER_HINT, stackSize, hintsOnly, 1, i, 0);
		}
		buildPiece(STRUCTURE_PIECE_TOP_HINT, stackSize, hintsOnly, 1, tTotalHeight - 1, 0);
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getMachineType() {
		return "Gas Sparger";
	}

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}
	
	@Override
	public boolean onPlungerRightClick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {		
		int aLayerIndex = 0;
		PlayerUtils.messagePlayer(aPlayer, "Trying to clear "+mOutputHatchesByLayer.size()+" layers of output hatches.");
		for (List<GT_MetaTileEntity_Hatch_Output> layer : this.mOutputHatchesByLayer) {
			int aHatchIndex = 0;
			for (GT_MetaTileEntity_Hatch_Output hatch : layer) {
				if (hatch.mFluid != null) {
					PlayerUtils.messagePlayer(aPlayer, "Clearing "+hatch.mFluid.amount+"L of "+hatch.mFluid.getLocalizedName()+" from hatch "+aHatchIndex+" on layer "+aLayerIndex+".");
					hatch.mFluid = null;
				}
				aHatchIndex++;
			}
			aLayerIndex++;			
		}
		return aLayerIndex > 0;
	}
}
