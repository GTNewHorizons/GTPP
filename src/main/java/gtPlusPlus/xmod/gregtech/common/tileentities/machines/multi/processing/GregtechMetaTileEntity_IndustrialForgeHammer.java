package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksFlat;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.common.registry.GameRegistry;
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
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IndustrialForgeHammer
        extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialForgeHammer>
        implements ISurvivalConstructable {

    private int mCasing;
    private int mAnvil;
    private static IStructureDefinition<GregtechMetaTileEntity_IndustrialForgeHammer> STRUCTURE_DEFINITION = null;

    public GregtechMetaTileEntity_IndustrialForgeHammer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialForgeHammer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        setAnvilBlocks();
        return new GregtechMetaTileEntity_IndustrialForgeHammer(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Forge Hammer";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
                .addInfo("Controller Block for the Industrial Forge Hammer")
                .addInfo("Speed: +100% | EU Usage: 100% | Parallel: Tier x Anvil Tier x 8")
                .addInfo("T1 - Vanilla Anvil")
                .addInfo("Anvil goes in Middle 3x3x3 Structure");
        if (LoadedMods.Railcraft) {
            tt.addInfo("T2 - Steel Anvil");
        }
        if (LoadedMods.EnderIO) {
            tt.addInfo("T3 - Dark Steel Anvil");
        }
        if (LoadedMods.ThaumicBases) {
            tt.addInfo("T3 - Thaumic Anvil");
            tt.addInfo("T4 - Void Anvil");
        }
        tt.addPollutionAmount(getPollutionPerSecond(null))
                .addSeparator()
                .beginStructureBlock(3, 3, 3, true)
                .addController("Front Center")
                .addCasingInfo("Forge Casing", 10)
                .addInputBus("Any Casing", 1)
                .addOutputBus("Any Casing", 1)
                .addEnergyHatch("Any Casing", 1)
                .addMaintenanceHatch("Any Casing", 1)
                .addMufflerHatch("Any Casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder);
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialForgeHammer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            Map<Block, Integer> aBlockMap = new HashMap<Block, Integer>();
            aBlockMap.put(sAnvil, 0);
            if (LoadedMods.Railcraft) {
                aBlockMap.put(sSteelAnvil, 0);
            }
            if (LoadedMods.EnderIO) {
                aBlockMap.put(sDarkSteelAnvil, 0);
            }
            if (LoadedMods.ThaumicBases) {
                aBlockMap.put(sThaumiumAnvil, 0);
                aBlockMap.put(sVoidAnvil, 0);
            }
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialForgeHammer>builder()
                    .addShape(mName, transpose(new String[][] {
                        {"CCC", "CCC", "CCC"},
                        {"C~C", "CAC", "CCC"},
                        {"CCC", "CCC", "CCC"},
                    }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntity_IndustrialForgeHammer.class)
                                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                                    .casingIndex(TAE.getIndexFromPage(1, 11))
                                    .dot(1)
                                    .buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings5Misc, 6))))
                    .addElement('A', onElementPass(x -> ++x.mAnvil, ofBlocksFlat(aBlockMap, sAnvil, 0)))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        setAnvilBlocks();
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        setAnvilBlocks();
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
    }

    @Override
    public String getSound() {
        return GregTech_API.sSoundList.get(1);
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
        return TAE.getIndexFromPage(1, 11);
    }

    @Override
    public boolean hasSlotInGUI() {
        return false;
    }

    @Override
    public String getCustomGUIResourceName() {
        return "MaterialPress";
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sHammerRecipes;
    }

    @Override
    public boolean checkRecipe(final ItemStack aStack) {
        Block aAnvil = this.getBaseMetaTileEntity()
                .getBlockAtSide(this.getBaseMetaTileEntity().getBackFacing());
        if (aAnvil != null) {
            int aAnvilTier = getAnvilTier(aAnvil);
            if (aAnvilTier > 0) {
                for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
                    ArrayList<ItemStack> tBusItems = new ArrayList<ItemStack>();
                    tBus.mRecipeMap = getRecipeMap();
                    if (isValidMetaTileEntity(tBus)) {
                        for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                            if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null)
                                tBusItems.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
                        }
                    }
                    if (checkRecipeGeneric(
                            tBusItems.toArray(new ItemStack[] {}),
                            new FluidStack[] {},
                            getMaxParallelRecipes() * aAnvilTier,
                            100,
                            100,
                            10000)) {
                        return true;
                    }
                }
            }
        }
        return false;
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
        return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialForgeHammer;
    }

    @Override
    public int getAmountOfOutputs() {
        return 1;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    private static Block sAnvil;
    private static Block sSteelAnvil;
    private static Block sDarkSteelAnvil;
    private static Block sThaumiumAnvil;
    private static Block sVoidAnvil;

    private static void setAnvilBlocks() {
        if (sAnvil == null) {
            sAnvil = Blocks.anvil;
            if (LoadedMods.Railcraft) {
                sSteelAnvil = GameRegistry.findBlock("Railcraft", "anvil");
            }
            if (LoadedMods.EnderIO) {
                sDarkSteelAnvil = GameRegistry.findBlock("EnderIO", "blockDarkSteelAnvil");
            }
            if (LoadedMods.ThaumicBases) {
                sThaumiumAnvil = GameRegistry.findBlock("thaumicbases", "thaumicAnvil");
                sVoidAnvil = GameRegistry.findBlock("thaumicbases", "voidAnvil");
            }
        }
    }

    public static boolean isBlockAnvil(Block aBlock) {
        setAnvilBlocks();
        if (sAnvil == aBlock) {
            return true;
        }
        if (LoadedMods.Railcraft) {
            if (sSteelAnvil == aBlock) {
                return true;
            }
        }
        if (LoadedMods.EnderIO) {
            if (sDarkSteelAnvil == aBlock) {
                return true;
            }
        }
        if (LoadedMods.ThaumicBases) {
            if (sThaumiumAnvil == aBlock || sVoidAnvil == aBlock) {
                return true;
            }
        }
        return false;
    }

    public static int getAnvilTier(Block aBlock) {
        if (isBlockAnvil(aBlock)) {
            if (sAnvil == aBlock) {
                return 1;
            }
            if (LoadedMods.Railcraft) {
                if (sSteelAnvil == aBlock) {
                    return 2;
                }
            }
            if (LoadedMods.EnderIO) {
                if (sDarkSteelAnvil == aBlock) {
                    return 3;
                }
            }
            if (LoadedMods.ThaumicBases) {
                if (sThaumiumAnvil == aBlock) {
                    return 3;
                }
                if (sVoidAnvil == aBlock) {
                    return 4;
                }
            }
        }
        return 0;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        setAnvilBlocks();
        super.onFirstTick(aBaseMetaTileEntity);
    }
}
