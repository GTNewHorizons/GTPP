package gtPlusPlus.core.material;

import static gregtech.api.enums.GT_Values.M;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.materials.MaterialUtils;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class Material {

	final String unlocalizedName;
	final String localizedName;

	protected Object dataVar;

	private MaterialStack[] materialInput = new MaterialStack[4];
	final List<MaterialStack> mMaterialList = new ArrayList<MaterialStack>();

	final short[] RGBA;

	final boolean usesBlastFurnace;
	public final boolean isRadioactive;
	public final byte vRadioationLevel;

	final int meltingPointK;
	final int boilingPointK;
	final int meltingPointC;
	final int boilingPointC;
	final long vProtons;
	final long vNeutrons;
	final long vMass;
	final int smallestStackSizeWhenProcessing; //Add a check for <=0 || > 64
	public final int vTier;
	public final int vVoltageMultiplier;
	public final String vChemicalFormula;
	public final String vChemicalSymbol;

	public Material(String materialName, short[] rgba, int meltingPoint, int boilingPoint, long protons, long neutrons, boolean blastFurnace, MaterialStack[] inputs){		
		this(materialName, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, inputs, "", 0);
	}

	public Material(String materialName, short[] rgba, int meltingPoint, int boilingPoint, long protons, long neutrons, boolean blastFurnace, MaterialStack[] inputs, int radiationLevel){		
		this(materialName, rgba, meltingPoint, boilingPoint, protons, neutrons, blastFurnace, inputs, "", radiationLevel);
	}

	public Material(String materialName, short[] rgba, int meltingPoint, int boilingPoint, long protons, long neutrons, boolean blastFurnace, MaterialStack[] inputs, String chemicalSymbol, int radiationLevel){

		this.unlocalizedName = Utils.sanitizeString(materialName);
		this.localizedName = materialName;
		this.RGBA = rgba;
		this.meltingPointC = meltingPoint;
		if (boilingPoint == 0){
			boilingPoint = meltingPoint*4;
		}
		this.boilingPointC = boilingPoint;
		this.meltingPointK = (int) MathUtils.celsiusToKelvin(meltingPointC);
		this.boilingPointK = (int) MathUtils.celsiusToKelvin(boilingPointC);
		this.vProtons = protons;
		this.vNeutrons = neutrons;
		this.vMass = getMass();

		//List<MaterialStack> inputArray = Arrays.asList(inputs);
		int tempSmallestSize = getSmallestStackForCrafting(inputs);
		if (tempSmallestSize <= 64 && tempSmallestSize >= 1){
			this.smallestStackSizeWhenProcessing = tempSmallestSize; //Valid stacksizes
		}
		else {
			this.smallestStackSizeWhenProcessing = 50; //Can divide my math by 1/2 and round it~
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
		if (getMeltingPoint_K() >= 0 && getMeltingPoint_K() <= 750){
			this.vTier = 1;
		}
		else if(getMeltingPoint_K() >= 751 && getMeltingPoint_K() <= 1250){
			this.vTier = 2;
		}
		else if(getMeltingPoint_K() >= 1251 && getMeltingPoint_K() <= 1750){
			this.vTier = 3;
		}
		else if(getMeltingPoint_K() >= 1751 && getMeltingPoint_K() <= 2250){
			this.vTier = 4;
		}
		else if(getMeltingPoint_K() >= 2251 && getMeltingPoint_K() <= 2750){
			this.vTier = 5;
		}
		else if(getMeltingPoint_K() >= 2751 && getMeltingPoint_K() <= 3250){
			this.vTier = 6;
		}
		else if(getMeltingPoint_K() >= 3251 && getMeltingPoint_K() <= 3750){
			this.vTier = 7;
		}
		else if(getMeltingPoint_K() >= 3751 && getMeltingPoint_K() <= 4250){
			this.vTier = 8;
		}
		else if(getMeltingPoint_K() >= 4251 && getMeltingPoint_K() <= 4750){
			this.vTier = 9;
		}
		else if(getMeltingPoint_K() >= 4751 && getMeltingPoint_K() <= 9999){
			this.vTier = 10;
		}
		else {
			this.vTier = 0;
		}

		this.usesBlastFurnace = blastFurnace;
		this.vVoltageMultiplier = this.getMeltingPoint_K() >= 2800 ? 64 : 16;

		if (inputs == null){
			this.materialInput = null;			
		}
		else {
			if (inputs.length != 0){
				for (int i=0; i < inputs.length; i++){
					if (inputs[i] != null){
						this.materialInput[i] = inputs[i];
					}
				}
			}
		}

		//Makes a Fancy Chemical Tooltip
		this.vChemicalSymbol = chemicalSymbol;
		if (materialInput != null){
			this.vChemicalFormula = getToolTip(chemicalSymbol, OrePrefixes.dust.mMaterialAmount / M, true);
		}
		else if (!this.vChemicalSymbol.equals("")){
			Utils.LOG_INFO("materialInput is null, using a valid chemical symbol.");
			this.vChemicalFormula = this.vChemicalSymbol;
		}
		else{
			Utils.LOG_INFO("MaterialInput == null && chemicalSymbol probably equals nothing");
			this.vChemicalFormula = "??";
		}

		dataVar = MathUtils.generateSingularRandomHexValue();

		Utils.LOG_INFO("Creating a Material instance for "+materialName);
		Utils.LOG_INFO("Formula: "+vChemicalFormula);
		Utils.LOG_INFO("Protons: "+vProtons);
		Utils.LOG_INFO("Neutrons: "+vNeutrons);
		Utils.LOG_INFO("Mass: "+vMass+"/units");
		Utils.LOG_INFO("Melting Point: "+meltingPointC+"C.");
		Utils.LOG_INFO("Boiling Point: "+boilingPointC+"C.");
	}

	public String getLocalizedName(){
		return localizedName;
	}

	public String getUnlocalizedName(){
		return unlocalizedName;
	}

	public short[] getRGBA(){
		return RGBA;
	}

	public int getRgbAsHex(){

		int returnValue = Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
		if (returnValue == 0){
			return (int) dataVar;
		}		
		return Utils.rgbtoHexValue(RGBA[0], RGBA[1], RGBA[2]);
	}

	public long getProtons() {
		return vProtons;
	}

	public long getNeutrons() {
		return vNeutrons;
	}

	public long getMass() {
		return vProtons + vNeutrons;
	}

	public int getMeltingPoint_C() {
		return meltingPointC;
	}

	public int getBoilingPoint_C() {
		return boilingPointC;
	}

	public int getMeltingPoint_K() {
		return meltingPointK;
	}

	public int getBoilingPoint_K() {
		return boilingPointK;
	}

	public boolean requiresBlastFurnace(){
		return usesBlastFurnace;
	}

	public ItemStack getDust(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dust"+unlocalizedName, stacksize);
	}

	public ItemStack getSmallDust(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustSmall"+unlocalizedName, stacksize);
	}

	public ItemStack getTinyDust(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("dustTiny"+unlocalizedName, stacksize);
	}

	public ItemStack[] getValidInputStacks(){
		return UtilsItems.validItemsForOreDict(unlocalizedName);
	}

	public ItemStack getIngot(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("ingot"+unlocalizedName, stacksize);
	}

	public ItemStack getPlate(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("plate"+unlocalizedName, stacksize);
	}

	public ItemStack getPlateDouble(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("plateDouble"+unlocalizedName, stacksize);
	}

	public ItemStack getGear(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("gear"+unlocalizedName, stacksize);
	}

	public ItemStack getRod(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("stick"+unlocalizedName, stacksize);
	}

	public ItemStack getLongRod(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("stickLong"+unlocalizedName, stacksize);
	}

	public ItemStack getBolt(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("bolt"+unlocalizedName, stacksize);
	}

	public ItemStack getScrew(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("screw"+unlocalizedName, stacksize);
	}

	public ItemStack getRing(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("ring"+unlocalizedName, stacksize);
	}

	public ItemStack getRotor(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("rotor"+unlocalizedName, stacksize);
	}

	public ItemStack getFrameBox(int stacksize){
		return UtilsItems.getItemStackOfAmountFromOreDictNoBroken("frameGt"+unlocalizedName, stacksize);
	}

	public ItemStack[] getMaterialComposites(){
		//Utils.LOG_INFO("Something requested the materials needed for "+localizedName);
		if (materialInput != null && materialInput.length >= 1){
			ItemStack[] temp = new ItemStack[materialInput.length];
			for (int i=0;i<materialInput.length;i++){
				//Utils.LOG_INFO("i:"+i);
				ItemStack testNull = null;
				try {
					testNull = materialInput[i].getDustStack();
				} catch (Throwable r){
					Utils.LOG_INFO("Failed gathering material stack for "+localizedName+".");
					Utils.LOG_INFO("What Failed: Length:"+materialInput.length+" current:"+i);
				}
				try {
					if (testNull != null){
						//Utils.LOG_INFO("not null");
						temp[i] = materialInput[i].getDustStack();
					}
				} catch (Throwable r){
					Utils.LOG_INFO("Failed setting slot "+i+", using "+localizedName);
				}
			}		
			return temp;
		}
		return new ItemStack[]{};
	}

	public int[] getMaterialCompositeStackSizes(){
		if (materialInput != null && materialInput.length >= 1){
			int[] temp = new int[materialInput.length];
			for (int i=0;i<materialInput.length;i++){
				temp[i] = materialInput[i].getDustStack().stackSize;
			}		
			return temp;
		}
		return new int[]{};
	}




	private int getInputMaterialCount(MaterialStack[] materialInput){
		int i = 0;
		for (int r=0;r<4;r++){
			try {
				if (!materialInput[r].equals(null)){
					i++;
				}
			} catch(Throwable x){
				return i;
			}
		}
		return i;
	}


	public String getChemicalFormula(MaterialStack[] materialInput){
		if (materialInput != null && materialInput.length >= 1){
			int f = getInputMaterialCount(materialInput);
			String[] formulaComponents = new String[f];
			for (int i=0;i<f;i++){
				try {		
					if (materialInput[i] != null){					
						formulaComponents[i] = materialInput[i].stackMaterial.vChemicalFormula;	
						Utils.LOG_INFO("LOOK AT ME IN THE LOG - " + formulaComponents[i]);	
					}
					else{
						Utils.LOG_INFO("LOOK AT ME IN THE LOG - materialInput[i] was null");
					}
				} catch (Throwable e){
					Utils.LOG_INFO("LOOK AT ME IN THE LOG - got an error");
					return "??";
				}				
			}

			String properName = "";
			for (int r = 0; r < f; r++){
				properName = properName + formulaComponents[r];
				Utils.LOG_INFO("LOOK AT ME IN THE LOG - "+properName);
			}
			if (!properName.equals(""))
				return properName;

		}
		return "??";

	}

	public long[] getSmallestRatio(MaterialStack[] inputs){
		if (inputs != null){
			if (inputs.length > 0){
				Utils.LOG_INFO("length: "+inputs.length);
				Utils.LOG_INFO("(inputs != null): "+(inputs != null));
				//Utils.LOG_INFO("length: "+inputs.length);
				double tempPercentage=0;
				long[] tempRatio = new long[inputs.length];
				for (int x=0;x<inputs.length;x++){
					//tempPercentage = tempPercentage+inputs[x].percentageToUse;
					//this.mMaterialList.add(inputs[x]);
					if (inputs[x] != null){
						tempRatio[x] = inputs[x].getPartsPerOneHundred();						
					}
				}
				//Check if % of added materials equals roughly 100%
				/*if (tempPercentage <= 95 || tempPercentage >= 101){
					Utils.LOG_INFO("The compound for "+localizedName+" doesn't equal 98-100%, this isn't good.");
				}*/

				long[] smallestRatio = MathUtils.simplifyNumbersToSmallestForm(tempRatio);

				if (smallestRatio.length > 0){
					String tempRatioStringThing1 = "";
					for (int r=0;r<tempRatio.length;r++){
						tempRatioStringThing1 = tempRatioStringThing1 + tempRatio[r] +" : ";
					}
					Utils.LOG_INFO("Default Ratio: "+tempRatioStringThing1);

					String tempRatioStringThing = "";
					int tempSmallestCraftingUseSize = 0;
					for (int r=0;r<smallestRatio.length;r++){
						tempRatioStringThing = tempRatioStringThing + smallestRatio[r] +" : ";
						tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + smallestRatio[r]);
					}
					//this.smallestStackSizeWhenProcessing = tempSmallestCraftingUseSize;
					Utils.LOG_INFO("Smallest Ratio: "+tempRatioStringThing);
					return smallestRatio;
				}
			}		
		}
		return null;
	}

	public int getSmallestStackForCrafting(MaterialStack[] inputs){
		if (inputs != null){
			if (inputs.length != 0){
				long[] smallestRatio = getSmallestRatio(inputs);
				if (smallestRatio.length > 0){
					int tempSmallestCraftingUseSize = 0;
					for (int r=0;r<smallestRatio.length;r++){
						tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + smallestRatio[r]);
					}
					return tempSmallestCraftingUseSize;
				}
			}		
		}
		return 1;
	}


	public String getToolTip(String chemSymbol, long aMultiplier, boolean aShowQuestionMarks) {
		if (!aShowQuestionMarks && (vChemicalFormula.equals("?")||vChemicalFormula.equals("??"))) return "";
		Utils.LOG_INFO("===============| Calculating Atomic Formula for "+this.localizedName+" |===============");
		if (!chemSymbol.equals(""))
			return chemSymbol;
		MaterialStack[] tempInput = materialInput;
		if (tempInput != null){
			if (tempInput.length >= 1){
				String dummyFormula = "";
				long[] dummyFormulaArray = getSmallestRatio(tempInput);
				if (dummyFormulaArray != null){
					if (dummyFormulaArray.length >= 1){
						for (int e=0;e<tempInput.length;e++){
							if (tempInput[e] != null){
								if (!tempInput[e].stackMaterial.vChemicalSymbol.equals("??")){
									if (dummyFormulaArray[e] > 1){
										dummyFormula = dummyFormula + tempInput[e].stackMaterial.vChemicalSymbol + dummyFormulaArray[e];
									}
									else if (dummyFormulaArray[e] == 1){
										dummyFormula = dummyFormula + tempInput[e].stackMaterial.vChemicalSymbol;
									}
									else if (dummyFormulaArray[e] <= 0){
										dummyFormula = dummyFormula+"";
									}

								}else
									dummyFormula = dummyFormula + "??";
							}
							else {
								dummyFormula = dummyFormula+"";
							}
						}
						return MaterialUtils.subscript(dummyFormula);
						//return dummyFormula;
					}
					Utils.LOG_INFO("dummyFormulaArray <= 0");
				}
				Utils.LOG_INFO("dummyFormulaArray == null");
			}
			Utils.LOG_INFO("tempInput.length <= 0");
		}
		Utils.LOG_INFO("tempInput == null");
		return "??";

	}





























}
