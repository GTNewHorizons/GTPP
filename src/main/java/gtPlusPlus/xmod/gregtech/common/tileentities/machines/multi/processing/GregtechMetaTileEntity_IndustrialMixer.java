package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IndustrialMixer
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialMixer> implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    public static String mCasingName = "Multi-Use Casing";
    public static String mCasingName2 = "Titanium Turbine Casing";
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialMixer> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialMixer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 2);
    }

    public GregtechMetaTileEntity_IndustrialMixer(final String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 2);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IndustrialMixer(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Mixer";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
                .addInfo("Controller Block for the Industrial Mixer")
                .addInfo("250% faster than using single block machines of the same voltage")
                .addInfo("Processes eight recipes per voltage tier")
                .addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator()
                .beginStructureBlock(3, 4, 3, false)
                .addController("Second Layer Center")
                .addCasingInfo(mCasingName, 16)
                .addCasingInfo(mCasingName2, 2)
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
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialMixer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialMixer>builder()
                    .addShape(mName, transpose(new String[][] {
                        {"CCC", "CCC", "CCC"},
                        {"CCC", "CMC", "CCC"},
                        {"C~C", "CMC", "CCC"},
                        {"CCC", "CCC", "CCC"},
                    }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialMixer.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                                    .casingIndex(CASING_TEXTURE_ID)
                                    .dot(1)
                                    .buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 2))))
                    .addElement('M', ofBlock(GregTech_API.sBlockCasings4, 11))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 2, 0) && mCasing >= 16 && checkHatch();
    }

    @Override
    public String getSound() {
        return GregTech_API.sSoundList.get(Integer.valueOf(203));
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Default;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public boolean hasSlotInGUI() {
        return false;
    }

    @Override
    public String getCustomGUIResourceName() {
        return "IndustrialMixer";
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT;
    }

    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
            ArrayList<ItemStack> rList = new ArrayList<>();
            for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null)
                    rList.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
            }

            if (checkRecipeGeneric(
                    rList.toArray(new ItemStack[0]),
                    getStoredFluids().toArray(new FluidStack[0]),
                    getMaxParallelRecipes(),
                    getEuDiscountForParallelism(),
                    250,
                    10000)) {
                return true;
            }
        }

        return checkRecipeGeneric(
                new ItemStack[0],
                getStoredFluids().toArray(new FluidStack[0]),
                getMaxParallelRecipes(),
                getEuDiscountForParallelism(),
                250,
                10000);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 100;
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
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialMixer;
    }

    @Override
    public int getAmountOfOutputs() {
        return 1;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }
}
