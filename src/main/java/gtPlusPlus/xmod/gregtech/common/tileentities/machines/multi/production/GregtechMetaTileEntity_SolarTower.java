package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;

import com.gtnewhorizon.structurelib.structure.*;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.TileEntitySolarHeater;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

public class GregtechMetaTileEntity_SolarTower extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_SolarTower> {

	//862
	private static final int mCasingTextureID = TAE.getIndexFromPage(3, 9);
	private int mHeatLevel = 0;
	private int mCasing1;
	private int mCasing2;
	private int mCasing3;
	private int mCasing4;


	public ArrayList<TileEntitySolarHeater> mSolarHeaters = new ArrayList<TileEntitySolarHeater>();


	public GregtechMetaTileEntity_SolarTower(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_SolarTower(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_SolarTower(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Solar Tower";
	}

	@Override
	protected final GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
		.addInfo("Contributing Green Energy towards the future")
		.addInfo("Input: "+MISC_MATERIALS.SOLAR_SALT_COLD.getLocalizedName())
		.addInfo("Output: "+MISC_MATERIALS.SOLAR_SALT_HOT.getLocalizedName())
		.addInfo("Surround with rings of Solar Heaters")
		.addInfo("Each ring increases tier")
		.addInfo("1 = 1, 2 = 2, 3 = 4, 4 = 8, 5 = 16")
		.addInfo("Tier decreases heating time and allows more salt to be processed")
		.addSeparator()
		.beginStructureBlock(6, 27, 6, false)
		.addController("Top Middle")
		.addCasingInfo("Structural Solar Casing", 229)
		.addCasingInfo("Thermally Insulated Casing", 60)
		.addCasingInfo("Salt Containment Casing", 66)
		.addCasingInfo("Thermal Containment Casing", 60)
		.addInputHatch("Any 2 dot hint(min 1)", 2)
		.addOutputHatch("Any 2 dot hint(min 1)", 2)
		.addMaintenanceHatch("Any 2 dot hint", 2)
		.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	private static final String STRUCTURE_PIECE_BASE = "base";
	private static final String STRUCTURE_PIECE_TOWER = "tower";
	private static final String STRUCTURE_PIECE_TOP = "top";

	private static final String SOLAR_HEATER_RING_1 = "ring1";
	private static final String SOLAR_HEATER_RING_2 = "ring2";
	private static final String SOLAR_HEATER_RING_3 = "ring3";
	private static final String SOLAR_HEATER_RING_4 = "ring4";
	private static final String SOLAR_HEATER_RING_5 = "ring5";

	private static final ClassValue<IStructureDefinition<GregtechMetaTileEntity_SolarTower>> STRUCTURE_DEFINITION = new ClassValue<IStructureDefinition<GregtechMetaTileEntity_SolarTower>>() {
		@Override
		protected IStructureDefinition<GregtechMetaTileEntity_SolarTower> computeValue(Class<?> type) {
			return StructureDefinition.<GregtechMetaTileEntity_SolarTower>builder()

					// s = salt
					// c = thermal containment
					// i = thermal insulated
					// t = solar structural
					// h = hatch
					// g = solar heater

					.addShape(STRUCTURE_PIECE_TOP, (new String[][]{
						{"     ", "     ", "  ~  ", "     ", "     "},
						{"     ", "  s  ", " sss ", "  s  ", "     "},
						{"  c  ", " ccc ", "ccscc", " ccc ", "  c  "},
						{"  c  ", " ccc ", "ccscc", " ccc ", "  c  "},
						{"  c  ", " ccc ", "ccscc", " ccc ", "  c  "},
						{"  c  ", " ccc ", "ccscc", " ccc ", "  c  "},
						{"  c  ", " ccc ", "ccscc", " ccc ", "  c  "},
					}))
					.addShape(STRUCTURE_PIECE_TOWER, (new String[][]{
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
						{" i ", "isi", " i "},
					}))
					.addShape(STRUCTURE_PIECE_BASE, (new String[][]{
						{"           ", "           ", "     t     ", "    ttt    ", "   ttstt   ", "  ttssstt  ", "   ttstt   ", "    ttt    ", "     t     ", "           ", "           "},
						{"           ", "           ", "     t     ", "    ttt    ", "   tssst   ", "  ttssstt  ", "   tssst   ", "    ttt    ", "     t     ", "           ", "           "},
						{"           ", "     t     ", "    ttt    ", "   ttttt   ", "  ttssstt  ", " tttsssttt ", "  ttssstt  ", "   ttttt   ", "    ttt    ", "     t     ", "           "},
						{"           ", "     t     ", "    ttt    ", "   ttttt   ", "  ttssstt  ", " tttsssttt ", "  ttssstt  ", "   ttttt   ", "    ttt    ", "     t     ", "           "},
						{"    hhh    ", "   ttttt   ", "  ttttttt  ", " ttttttttt ", "htttsssttth", "htttsssttth", "htttsssttth", " ttttttttt ", "  ttttttt  ", "   ttttt   ", "    hhh    "},
						{"    hhh    ", "   ttttt   ", "  ttttttt  ", " ttttttttt ", "httttttttth", "httttttttth", "httttttttth", " ttttttttt ", "  ttttttt  ", "   ttttt   ", "    hhh    "},
					}))


					.addShape(SOLAR_HEATER_RING_1, (new String[][]{
						{
							"     ggggg     ",
							"    g     g    ",
							"   g       g   ",
							"  g         g  ",
							" g           g ",
							"g             g",
							"g             g",
							"g             g",
							"g             g",
							"g             g",
							" g           g ",
							"  g         g  ",
							"   g       g   ",
							"    g     g    ",
							"     ggggg     ",
						}}))
					.addShape(SOLAR_HEATER_RING_2, (new String[][]{
						{
							"     ggggggggg     ",
							"    g         g    ",
							"   g           g   ",
							"  g             g  ",
							" g               g ",
							"g                 g",
							"g                 g",
							"g                 g",
							"g                 g",
							"g                 g",
							"g                 g",
							"g                 g",
							"g                 g",
							"g                 g",
							" g               g ",
							"  g             g  ",
							"   g           g   ",
							"    g         g    ",
							"     ggggggggg     ",
						}}))
					.addShape(SOLAR_HEATER_RING_3, (new String[][]{
						{
							"     ggggggggggggg     ",
							"    g             g    ",
							"   g               g   ",
							"  g                 g  ",
							" g                   g ",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							"g                     g",
							" g                   g ",
							"  g                 g  ",
							"   g               g   ",
							"    g             g    ",
							"     ggggggggggggg     ",
						}}))
					.addShape(SOLAR_HEATER_RING_4, (new String[][]{
						{
							"     ggggggggggggggggg     ",
							"    g                 g    ",
							"   g                   g   ",
							"  g                     g  ",
							" g                       g ",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							"g                         g",
							" g                       g ",
							"  g                     g  ",
							"   g                   g   ",
							"    g                 g    ",
							"     ggggggggggggggggg     ",
						}}))
					.addShape(SOLAR_HEATER_RING_5, (new String[][]{
						{
							"     ggggggggggggggggggggg     ",
							"    g                     g    ",
							"   g                       g   ",
							"  g                         g  ",
							" g                           g ",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							"g                             g",
							" g                           g ",
							"  g                         g  ",
							"   g                       g   ",
							"    g                     g    ",
							"     ggggggggggggggggggggg     ",
						}}))

					.addElement('g', lazy(t -> ofHatchAdder(GregtechMetaTileEntity_SolarTower::addSolarHeater, 0, 1)))
					.addElement('t', lazy(t -> onElementPass(x -> ++x.mCasing1, ofBlock(t.getCasingBlock(), t.getCasingMeta()))))
					.addElement('i', lazy(t -> onElementPass(x -> ++x.mCasing2, ofBlock(t.getCasingBlock(), t.getCasingMeta2()))))
					.addElement('s', lazy(t -> onElementPass(x -> ++x.mCasing3, ofBlock(t.getCasingBlock(), t.getCasingMeta3()))))
					.addElement('c', lazy(t -> onElementPass(x -> ++x.mCasing4, ofBlock(t.getCasingBlock2(), t.getCasingMeta4()))))
					.addElement('h', lazy(t -> ofChain(
							ofHatchAdder(GregtechMetaTileEntity_SolarTower::addGenericHatch, t.getCasingTextureIndex(), 2),
							onElementPass(x -> ++x.mCasing1, ofBlock(t.getCasingBlock(), t.getCasingMeta())))))


					.build();
		}
	};

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		resetSolarHeaters();
		this.mMaintenanceHatches.clear();
		this.mInputHatches.clear();
		this.mOutputHatches.clear();
		this.mCasing1 = 0;
		this.mCasing2 = 0;
		this.mCasing3 = 0;
		this.mCasing4 = 0;

		boolean aStructureTop = checkPiece(STRUCTURE_PIECE_TOP, 2, 2, 0);
		log("Top Check: "+aStructureTop);
		boolean aStructureTower = checkPiece(STRUCTURE_PIECE_TOWER, 1, 1, -7);
		log("Tower Check: "+aStructureTower);
		boolean aStructureBase = checkPiece(STRUCTURE_PIECE_BASE, 5, 5, -22);
		log("Base Check: "+aStructureBase);
		boolean aCasingCount1 = this.mCasing1 >= 229;
		boolean aCasingCount2 = this.mCasing2 == 60;
		boolean aCasingCount3 = this.mCasing3 == 66;
		boolean aCasingCount4 = this.mCasing4 == 60;
		boolean aAllStructure = aStructureTop && aStructureTower && aStructureBase;
		boolean aAllCasings = aCasingCount1 && aCasingCount2 && aCasingCount3 && aCasingCount4;
		if (!aAllCasings || !aAllStructure ||
				this.mMaintenanceHatches.size() != 1 ||
				this.mInputHatches.size() < 1 ||
				this.mOutputHatches.size() < 1
				) {
			log("Bad Hatches - Solar Heaters: "+this.mSolarHeaters.size()+
					", Maint: "+this.mMaintenanceHatches.size()+
					", Input Hatches: "+this.mInputHatches.size()+
					", Output Hatches: "+this.mOutputHatches.size()+
					", Top: "+aStructureTop+
					", Tower: "+aStructureTower+
					", Base: "+aStructureBase+
					", Casing Count: "+aCasingCount1+" | Found: "+this.mCasing1+
					", Casing Count: "+aCasingCount2+" | Found: "+this.mCasing2+
					", Casing Count: "+aCasingCount3+" | Found: "+this.mCasing3+
					", Casing Count: "+aCasingCount4+" | Found: "+this.mCasing4);
			return false;
		}
		log("Built "+this.getLocalName()+" with "+this.mCasing1+" Structural Solar casings, "+this.mCasing2+" Thermally Insulated casings, "+this.mCasing3+" Salt Containment casings, "+this.mCasing4+" Thermal Containment casings.");
		return aAllCasings && aAllStructure;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		// Tower
		buildPiece(STRUCTURE_PIECE_TOP, stackSize, hintsOnly, 2, 2, 0);
		buildPiece(STRUCTURE_PIECE_TOWER, stackSize, hintsOnly, 1, 1, -7);
		buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 5, 5, -22);

		//Solar Heaters
		if (stackSize.stackSize >= 1) {
			buildPiece(SOLAR_HEATER_RING_1, stackSize, hintsOnly, 7, 7, -27);
			if (stackSize.stackSize >= 2) {
				buildPiece(SOLAR_HEATER_RING_2, stackSize, hintsOnly, 9, 9, -27);
				if (stackSize.stackSize >= 3) {
					buildPiece(SOLAR_HEATER_RING_3, stackSize, hintsOnly, 11, 11, -27);
					if (stackSize.stackSize >= 4) {
						buildPiece(SOLAR_HEATER_RING_4, stackSize, hintsOnly, 13, 13, -27);
						if (stackSize.stackSize >= 5) {
							buildPiece(SOLAR_HEATER_RING_5, stackSize, hintsOnly, 15, 15, -27);
						}
					}
				}
			}
		}
	}

	@Override
	public IStructureDefinition<GregtechMetaTileEntity_SolarTower> getStructureDefinition() {
		return STRUCTURE_DEFINITION.get(getClass());
	}

	public final boolean addGenericHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		log("Bad Hatch");
		return false;
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(212));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == 0 || aSide == 1) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)),
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12))};
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
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	@Override
	public boolean canHaveParallelUpgraded() {
		return false;
	}

	private int getHeaterTier() {
		int aSolarHeaterCounter = this.mSolarHeaters.size();
		if (aSolarHeaterCounter > 0) {
			if (aSolarHeaterCounter == 36) {
				return 1;
			}
			else if (aSolarHeaterCounter == 88) {
				return 2;
			}
			else if (aSolarHeaterCounter == 156) {
				return 4;
			}
			else if (aSolarHeaterCounter == 240) {
				return 8;
			}
			else if (aSolarHeaterCounter == 340) {
				return 16;
			}
		}
		return 0;
	}

	private int getHeaterCountForTier(int aTier) {
		switch (aTier) {
		case 1:
			return 36;
		case 2:
			return 88;
		case 4:
			return 156;
		case 8:
			return 240;
		case 16:
			return 340;
		default:
			return 0;
		}
	}

	public boolean getConnectedSolarReflectors(){

		this.mSolarHeaters.clear();
		int aRing = 1;

		if (this.mSolarHeaters.size() < 36) {
			// 15x15
			boolean aRing1 = checkPiece(SOLAR_HEATER_RING_1, 7, 7, -27);
			if (aRing1) {
				//log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
			}
		}
		if (this.mSolarHeaters.size() < 88) {
			// 17x17
			boolean aRing2 = checkPiece(SOLAR_HEATER_RING_2, 9, 9, -27);
			if (aRing2) {
				//log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
			}
		}
		if (this.mSolarHeaters.size() < 156) {
			// 19x19
			boolean aRing3 = checkPiece(SOLAR_HEATER_RING_3, 11, 11, -27);
			if (aRing3) {
				//log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
			}
		}
		if (this.mSolarHeaters.size() < 240) {
			// 21x21
			boolean aRing4 = checkPiece(SOLAR_HEATER_RING_4, 13, 13, -27);
			if (aRing4) {
				//log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
			}
		}
		if (this.mSolarHeaters.size() < 340) {
			// 23x23
			boolean aRing5 = checkPiece(SOLAR_HEATER_RING_5, 15, 15, -27);
			if (aRing5) {
				//log("Found Ring: "+(aRing++)+", Total: "+this.mSolarHeaters.size());
			}
		}
		return this.mSolarHeaters.size() > 0;
	}

	private boolean addSolarHeater(IGregTechTileEntity aTileEntity, int a) {
		if (aTileEntity == null) {
			return false;
		}
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof TileEntitySolarHeater) {
				TileEntitySolarHeater mTile = (TileEntitySolarHeater) aMetaTileEntity;
				if (mTile != null) {
					if (!mTile.hasSolarTower() && mTile.canSeeSky()) {
						//Logger.INFO("Found Solar Reflector, Injecting Data.");
						mTile.setSolarTower(this);
						return this.mSolarHeaters.add(mTile);
					}
				}
			}
		}
		return false;
	}

	private Fluid mColdSalt = null;
	private Fluid mHotSalt = null;

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		this.mEfficiencyIncrease = 10;
		this.mMaxProgresstime = 100;

		if (this.mSolarHeaters.isEmpty() || this.mSolarHeaters.size() < 340 || this.getTotalRuntimeInTicks() % 200 == 0) {
			getConnectedSolarReflectors();
		}

		int aTier = getHeaterTier();
		int aHeaters = getHeaterCountForTier(aTier);

		//Manage Heat every 5s
		//Add Heat First, if sources available
		if (aHeaters > 0) {
			for (int i = 0; i < aHeaters; i++) {
				Math.min((this.mHeatLevel += aTier), 20000);
			}
		}

		//Remove Heat, based on time of day
		if (this.mHeatLevel > 0) {
			if (this.mHeatLevel > 20000) {
				this.mHeatLevel = 20000;
			}
		}
		World w = this.getBaseMetaTileEntity().getWorld();
		if (w != null) {
			int aRemovalFactor = 0;
			if (w.isDaytime()) {
				aRemovalFactor = 1;
			}
			else {
				aRemovalFactor = 8;
			}
			for (int i = 0; i<MathUtils.randInt((aHeaters/10), aHeaters); i++){
				this.mHeatLevel -= aRemovalFactor;
			}
		}

		if (this.mEfficiency == this.getMaxEfficiency(null) && this.mHeatLevel >= 10000) {
			if (this.mColdSalt == null) {
				this.mColdSalt = MISC_MATERIALS.SOLAR_SALT_COLD.getFluid();
			}
			if (this.mHotSalt == null) {
				this.mHotSalt = MISC_MATERIALS.SOLAR_SALT_HOT.getFluid();
			}
			ArrayList<FluidStack> aFluids = this.getStoredFluids();
			for (FluidStack aFluid : aFluids) {
				if (aFluid.getFluid().equals(this.mColdSalt)) {
					if (aFluid.amount >= (aTier * 1000)) {
						this.depleteInput(FluidUtils.getFluidStack(this.mColdSalt, (aTier * 1000)));
						this.addOutput(FluidUtils.getFluidStack(this.mHotSalt, (aTier * 1000)));
						break;
					}
				}
			}
		}


		return true;
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
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	public Block getCasingBlock() {
		return ModBlocks.blockSpecialMultiCasings;
	}

	public Block getCasingBlock2() {
		return ModBlocks.blockCasings2Misc;
	}

	public byte getCasingMeta() {
		return 6;
	}


	public byte getCasingMeta2() {
		return 8;
	}


	public byte getCasingMeta3() {
		return 7;
	}

	public byte getCasingMeta4() {
		return 11;
	}

	public byte getCasingTextureIndex() {
		return (byte) mCasingTextureID;
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {



	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setInteger("mHeatLevel", this.mHeatLevel);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		this.mHeatLevel = aNBT.getInteger("mHeatLevel");
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		// Fix GT bug
		if (this.getBaseMetaTileEntity().getFrontFacing() != 1) {
			this.getBaseMetaTileEntity().setFrontFacing((byte) 1);
		}
	}

	@Override
	public void onRemoval() {
		resetSolarHeaters();
		super.onRemoval();
	}

	private void resetSolarHeaters() {
		for (TileEntitySolarHeater aTile : this.mSolarHeaters) {
			aTile.clearSolarTower();
		}
		this.mSolarHeaters.clear();
	}

	@Override
	public String[] getExtraInfoData() {
		return new String[] {
				"Internal Heat Level: "+this.mHeatLevel,
				"Connected Solar Reflectors: "+this.mSolarHeaters.size()
		};
	}

}
