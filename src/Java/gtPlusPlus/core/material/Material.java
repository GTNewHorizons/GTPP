package gtPlusPlus.core.material;

import static gregtech.api.enums.GT_Values.M;
import gregtech.api.enums.*;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class Material {

	private final String unlocalizedName;
	private final String localizedName;

	private final Fluid vMoltenFluid;
	private final Fluid vPlasma;

	protected Object dataVar = MathUtils.generateSingularRandomHexValue();

	private ArrayList<MaterialStack> vMaterialInput = new ArrayList<MaterialStack>();
	public final long[] vSmallestRatio;
	public final short vComponentCount;
	
	private final short[] RGBA;

	private final boolean usesBlastFurnace;
	public final boolean isRadioactive;
	public final byte vRadioationLevel;

	private final int meltingPointK;
	private final int boilingPointK;
	private final int meltingPointC;
	private final int boilingPointC;
	private final long vProtons;
	private final long vNeutrons;
	private final long vMass;
	public final int smallestStackSizeWhenProcessing; //Add a check for <=0 || > 64
	public final int vTier;
	public final int vVoltageMultiplier;
	public final String vChemicalFormula;
	public final String vChemicalSymbol;

	public final long vDurability;
	public final int vToolQuality;
	public final int vHarvestLevel;

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final MaterialStack... inputs){		
		this(materialName, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", 0, inputs);
	}

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final int radiationLevel, MaterialStack... inputs){		
		this(materialName, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", radiationLevel, inputs);
	}

	public Material(final String materialName, final long durability, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final int radiationLevel, MaterialStack... inputs){		
		this(materialName, durability, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, "", radiationLevel, inputs);
	}

	public Material(final String materialName, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, final MaterialStack... inputs){
		this(materialName, 0, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, chemicalSymbol, radiationLevel, inputs);
	}

	public Material(final String materialName, final long durability, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, final MaterialStack... inputs){

		this.unlocalizedName = Utils.sanitizeString(materialName);
		this.localizedName = materialName;
		this.RGBA = rgba;
		this.meltingPointC = meltingPoint;
		if (boilingPoint != 0){
			this.boilingPointC = boilingPoint;
		}
		else {
			this.boilingPointC = meltingPoint*4;
		}
		this.meltingPointK = (int) MathUtils.celsiusToKelvin(meltingPointC);
		this.boilingPointK = (int) MathUtils.celsiusToKelvin(boilingPointC);
		this.vProtons = protons;
		this.vNeutrons = neutrons;
		this.vMass = getMass();

		//Sets tool Durability
		if (durability != 0){			
			this.vDurability = durability;
		}
		else {
			if (inputs != null){
				long durabilityTemp = 0;
				int counterTemp = 0;
				for (MaterialStack m : inputs){
					if (m.getStackMaterial() != null){
						if (m.getStackMaterial().vDurability != 0){
							durabilityTemp =  (durabilityTemp+m.getStackMaterial().vDurability);
							counterTemp++;
							
						}
					}					
				}
				if (durabilityTemp != 0 && counterTemp != 0){
					this.vDurability = (durabilityTemp/counterTemp);
				}
				else {
					this.vDurability = 8196;
				}
			}
			else {
				this.vDurability = 0;
			}
		}

		if (this.vDurability >= 0 && this.vDurability < 64000){
			this.vToolQuality = 1;
			this.vHarvestLevel = 2;
		}
		else if (this.vDurability >= 64000 && this.vDurability < 128000){
			this.vToolQuality = 2;
			this.vHarvestLevel = 2;
		}
		else if (this.vDurability >= 128000 && this.vDurability < 256000){
			this.vToolQuality = 3;
			this.vHarvestLevel = 2;
		}
		else if (this.vDurability >= 256000 && this.vDurability < 512000){
			this.vToolQuality = 3;
			this.vHarvestLevel = 3;
		}
		else if (this.vDurability >= 512000 && this.vDurability <= Integer.MAX_VALUE){
			this.vToolQuality = 4;
			this.vHarvestLevel = 4;
		}
		else {
			this.vToolQuality = 0;
			this.vHarvestLevel = 0;
		}

		//Sets the Rad level
		if (radiationLevel != 0){
			this.isRadioactive = true;
			this.vRadioationLevel = (byte) radiationLevel;
		}
		else {
			this.isRadioactive = false;
			this.vRadioationLevel = (byte) radiationLevel;
		}

		//Sets the materials 'tier'. Will probably replace this logic.
		this.vTier = MaterialUtils.getTierOfMaterial((int) MathUtils.celsiusToKelvin(meltingPoint));

		this.usesBlastFurnace = blastFurnace;
		this.vVoltageMultiplier = this.getMeltingPointK() >= 2800 ? 64 : 16;

		if (inputs == null){
			this.vMaterialInput = null;			
		}
		else {
			if (inputs.length != 0){
				for (int i=0; i < inputs.length; i++){
					if (inputs[i] != null){
						this.vMaterialInput.add(i, inputs[i]);
					}
				}
			}
		}

		this.vComponentCount = getComponentCount(inputs);
		this.vSmallestRatio = getSmallestRatio(vMaterialInput);
		int tempSmallestSize = 0;

		if (vSmallestRatio != null){
			for (int v=0;v<this.vSmallestRatio.length;v++){
				tempSmallestSize=(int) (tempSmallestSize+vSmallestRatio[v]);
			}
			this.smallestStackSizeWhenProcessing = tempSmallestSize; //Valid stacksizes
		}
		else {
			this.smallestStackSizeWhenProcessing = 1; //Valid stacksizes			
		}


		//Makes a Fancy Chemical Tooltip
		this.vChemicalSymbol = chemicalSymbol;
		if (vMaterialInput != null){
			this.vChemicalFormula = getToolTip(chemicalSymbol, OrePrefixes.dust.mMaterialAmount / M, true);
		}
		else if (!this.vChemicalSymbol.equals("")){
			Utils.LOG_WARNING("materialInput is null, using a valid chemical symbol.");
			this.vChemicalFormula = this.vChemicalSymbol;
		}
		else{
			Utils.LOG_WARNING("MaterialInput == null && chemicalSymbol probably equals nothing");
			this.vChemicalFormula = "??";
		}

		Materials isValid = Materials.get(getLocalizedName());
		if (isValid == Materials._NULL){
			this.vMoltenFluid = generateFluid();
		}
		else {			
			if (isValid.mFluid != null){
				this.vMoltenFluid = isValid.mFluid;
			}
			else if (isValid.mGas != null){
				this.vMoltenFluid = isValid.mGas;
			}
			else {
				this.vMoltenFluid = generateFluid();
			}
		}

		this.vPlasma = generatePlasma();

		//dataVar = MathUtils.generateSingularRandomHexValue();

		String ratio = "";
		if (vSmallestRatio != null) {
			for (int hu=0;hu<vSmallestRatio.length;hu++){
				if (ratio.equals("")){
					ratio = String.valueOf(vSmallestRatio[hu]);
				}
				else {
					ratio = ratio + ":" +vSmallestRatio[hu];
				}		
			}
		}

		Utils.LOG_INFO("Creating a Material instance for "+materialName);
		Utils.LOG_INFO("Formula: "+vChemicalFormula + " Smallest Stack: "+smallestStackSizeWhenProcessing+" Smallest Ratio:"+ratio);
		Utils.LOG_INFO("Protons: "+vProtons);
		Utils.LOG_INFO("Neutrons: "+vNeutrons);
		Utils.LOG_INFO("Mass: "+vMass+"/units");
		Utils.LOG_INFO("Melting Point: "+meltingPointC+"C.");
		Utils.LOG_INFO("Boiling Point: "+boilingPointC+"C.");
	}

	public final String getLocalizedName(){
		if (this.localizedName != null)
			return this.localizedName;
		return "ERROR BAD LOCALIZED NAME";
	}

	public final String getUnlocalizedName(){
		if (this.unlocalizedName != null)
			return this.unlocalizedName;
		return "ERROR.BAD.UNLOCALIZED.NAME";
	}

	final public short[] getRGBA(){
		if (this.RGBA != null)
			return this.RGBA;
		return new short[] {255,0,0};
	}

	final public int getRgbAsHex(){

		int returnValue = Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
		if (returnValue == 0){
			return (int) dataVar;
		}		
		return Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
	}

	final public long getProtons() {
		return vProtons;
	}

	public final long getNeutrons() {
		return vNeutrons;
	}

	final public long getMass() {
		return vProtons + vNeutrons;
	}

	public final int getMeltingPointC() {
		return meltingPointC;
	}

	public final int getBoilingPointC() {
		return boilingPointC;
	}

	public final int getMeltingPointK() {
		return meltingPointK;
	}

	public final int getBoilingPointK() {
		return boilingPointK;
	}

	public final boolean requiresBlastFurnace(){
		return usesBlastFurnace;
	}

	final public Block getBlock(){
		return Block.getBlockFromItem(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("block"+unlocalizedName, 1).getItem());
	}

	public final ItemStack getBlock(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("block"+unlocalizedName, stacksize);
	}

	public final ItemStack getDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dust"+unlocalizedName, stacksize);
	}

	public final ItemStack getSmallDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustSmall"+unlocalizedName, stacksize);
	}

	public final ItemStack getTinyDust(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustTiny"+unlocalizedName, stacksize);
	}

	public final ItemStack[] getValidInputStacks(){
		return ItemUtils.validItemsForOreDict(unlocalizedName);
	}

	public final ItemStack getIngot(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingot"+unlocalizedName, stacksize);
	}

	public final ItemStack getPlate(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plate"+unlocalizedName, stacksize);
	}

	public final ItemStack getPlateDouble(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDouble"+unlocalizedName, stacksize);
	}

	public final ItemStack getGear(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gear"+unlocalizedName, stacksize);
	}

	public final ItemStack getRod(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stick"+unlocalizedName, stacksize);
	}

	public final ItemStack getLongRod(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("stickLong"+unlocalizedName, stacksize);
	}

	public final ItemStack getBolt(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("bolt"+unlocalizedName, stacksize);
	}

	public final ItemStack getScrew(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("screw"+unlocalizedName, stacksize);
	}

	public final ItemStack getRing(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ring"+unlocalizedName, stacksize);
	}

	public final ItemStack getRotor(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("rotor"+unlocalizedName, stacksize);
	}

	public final ItemStack getFrameBox(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("frameGt"+unlocalizedName, stacksize);
	}

	public final ItemStack getCell(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+unlocalizedName, stacksize);
	}

	public final ItemStack getPlasmaCell(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellPlasma"+unlocalizedName, stacksize);
	}

	public final ItemStack getNugget(int stacksize){
		return ItemUtils.getItemStackOfAmountFromOreDictNoBroken("nugget"+unlocalizedName, stacksize);
	}

	final public ItemStack[] getMaterialComposites(){
		if (vMaterialInput != null){
			if (!vMaterialInput.isEmpty()){
				ItemStack[] temp = new ItemStack[vMaterialInput.size()];
				for (int i=0;i<vMaterialInput.size();i++){
					//Utils.LOG_WARNING("i:"+i);
					ItemStack testNull = null;
					try {
						testNull = vMaterialInput.get(i).getValidStack();
					} catch (Throwable r){
						Utils.LOG_WARNING("Failed gathering material stack for "+localizedName+".");
						Utils.LOG_WARNING("What Failed: Length:"+vMaterialInput.size()+" current:"+i);
					}
					try {
						if (testNull != null){
							//Utils.LOG_WARNING("not null");
							temp[i] = vMaterialInput.get(i).getValidStack();
						}
					} catch (Throwable r){
						Utils.LOG_WARNING("Failed setting slot "+i+", using "+localizedName);
					}
				}		
				return temp;
			}
		}
		return new ItemStack[]{};
	}

	public final ArrayList<MaterialStack> getComposites(){		
		return this.vMaterialInput;
	}

	final public int[] getMaterialCompositeStackSizes(){
		if (!vMaterialInput.isEmpty()){
			int[] temp = new int[vMaterialInput.size()];
			for (int i=0;i<vMaterialInput.size();i++){
				if (vMaterialInput.get(i) != null)
					temp[i] = vMaterialInput.get(i).getDustStack().stackSize;
				else
					temp[i]=0;
			}		
			return temp;
		}
		return new int[]{};
	}
	
	private final short getComponentCount(MaterialStack[] inputs){
		int counterTemp = 0;
		for (MaterialStack m : inputs){
			if (m.getStackMaterial() != null){
					counterTemp++;				
			}					
		}
		if (counterTemp != 0){
			return (short) counterTemp;
		}
		else {
			return 1;
		}
	}


	@SuppressWarnings("static-method")
	public final long[] getSmallestRatio(ArrayList<MaterialStack> tempInput){
		if (tempInput != null){
			if (!tempInput.isEmpty()){
				Utils.LOG_WARNING("length: "+tempInput.size());
				Utils.LOG_WARNING("(inputs != null): "+(tempInput != null));
				//Utils.LOG_WARNING("length: "+inputs.length);
				long[] tempRatio = new long[tempInput.size()];
				for (int x=0;x<tempInput.size();x++){
					//tempPercentage = tempPercentage+inputs[x].percentageToUse;
					//this.mMaterialList.add(inputs[x]);
					if (tempInput.get(x) != null){
						tempRatio[x] = tempInput.get(x).getPartsPerOneHundred();						
					}
				}

				long[] smallestRatio = MathUtils.simplifyNumbersToSmallestForm(tempRatio);

				if (smallestRatio.length > 0){
					String tempRatioStringThing1 = "";
					for (int r=0;r<tempRatio.length;r++){
						tempRatioStringThing1 = tempRatioStringThing1 + tempRatio[r] +" : ";
					}
					Utils.LOG_WARNING("Default Ratio: "+tempRatioStringThing1);

					String tempRatioStringThing = "";
					int tempSmallestCraftingUseSize = 0;
					for (int r=0;r<smallestRatio.length;r++){
						tempRatioStringThing = tempRatioStringThing + smallestRatio[r] +" : ";
						tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + smallestRatio[r]);
					}
					//this.smallestStackSizeWhenProcessing = tempSmallestCraftingUseSize;
					Utils.LOG_WARNING("Smallest Ratio: "+tempRatioStringThing);
					return smallestRatio;
				}
			}		
		}
		return null;
	}

	public final String getToolTip(String chemSymbol, long aMultiplier, boolean aShowQuestionMarks) {
		if (!aShowQuestionMarks && (vChemicalFormula.equals("?")||vChemicalFormula.equals("??"))) return "";
		Utils.LOG_WARNING("===============| Calculating Atomic Formula for "+this.localizedName+" |===============");
		if (!chemSymbol.equals(""))
			return chemSymbol;
		ArrayList<MaterialStack> tempInput = vMaterialInput;
		if (tempInput != null){
			if (!tempInput.isEmpty()){
				String dummyFormula = "";
				long[] dummyFormulaArray = getSmallestRatio(tempInput);
				if (dummyFormulaArray != null){
					if (dummyFormulaArray.length >= 1){
						for (int e=0;e<tempInput.size();e++){
							if (tempInput.get(e) != null){
								if (tempInput.get(e).getStackMaterial() != null){
									if (!tempInput.get(e).getStackMaterial().vChemicalSymbol.equals("??")){
										if (dummyFormulaArray[e] > 1){

											if (tempInput.get(e).getStackMaterial().vChemicalFormula.length() > 3){
												dummyFormula = dummyFormula + "(" + tempInput.get(e).getStackMaterial().vChemicalFormula + ")" + dummyFormulaArray[e];										
											}
											else {
												dummyFormula = dummyFormula + tempInput.get(e).getStackMaterial().vChemicalFormula + dummyFormulaArray[e];										
											}
										}
										else if (dummyFormulaArray[e] == 1){
											if (tempInput.get(e).getStackMaterial().vChemicalFormula.length() > 3){
												dummyFormula = dummyFormula + "(" +tempInput.get(e).getStackMaterial().vChemicalFormula + ")";											
											}
											else {
												dummyFormula = dummyFormula +tempInput.get(e).getStackMaterial().vChemicalFormula;											
											}
										}
									} else {
										dummyFormula = dummyFormula + "??";
									}
								} else {
									dummyFormula = dummyFormula + "▓▓";
								}
							}
						}
						return MaterialUtils.subscript(dummyFormula);
						//return dummyFormula;
					}
					Utils.LOG_WARNING("dummyFormulaArray <= 0");
				}
				Utils.LOG_WARNING("dummyFormulaArray == null");
			}
			Utils.LOG_WARNING("tempInput.length <= 0");
		}
		Utils.LOG_WARNING("tempInput == null");
		return "??";

	}

	public final Fluid generateFluid(){
		if (Materials.get(localizedName).mFluid == null){
			Utils.LOG_WARNING("Generating our own fluid.");

			//Generate a Cell if we need to
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+getUnlocalizedName(), 1) == null){
				@SuppressWarnings("unused")
				Item temp = new BaseItemCell(this);
			}
			return FluidUtils.addGTFluid(
					this.getUnlocalizedName(),
					"Molten "+this.getLocalizedName(),		
					this.RGBA,
					4,
					this.getMeltingPointK(),
					ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell"+getUnlocalizedName(), 1),
					ItemList.Cell_Empty.get(1L, new Object[0]),
					1000);
		}
		Utils.LOG_WARNING("Getting the fluid from a GT material instead.");
		return Materials.get(localizedName).mFluid;
	}

	public final Fluid generatePlasma(){
		Materials isValid = Materials.get(getLocalizedName());
		if (isValid != Materials._NULL && isValid != null){
			if (isValid.mPlasma != null){
				Utils.LOG_INFO("Using a pre-defined Plasma from GT.");
				return isValid.mPlasma;
			}
		}	
		Utils.LOG_INFO("Generating our own Plasma.");
		return FluidUtils.addGTPlasma(this);
		//return null;
	}



	final public FluidStack getFluid(int fluidAmount) {
		Utils.LOG_WARNING("Attempting to get "+fluidAmount+"L of "+this.vMoltenFluid.getName());
		FluidStack moltenFluid = new FluidStack(this.vMoltenFluid, fluidAmount);
		Utils.LOG_WARNING("Info: "+moltenFluid.getFluid().getName()+" Info: "+moltenFluid.amount+" Info: "+moltenFluid.getFluidID());
		return moltenFluid;
	}































}
