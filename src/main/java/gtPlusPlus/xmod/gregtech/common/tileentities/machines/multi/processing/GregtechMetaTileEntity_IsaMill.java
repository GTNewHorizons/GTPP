package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.general.ItemGenericChemBase;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.GT_MetaTileEntity_Hatch_MillingBalls;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class GregtechMetaTileEntity_IsaMill extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IsaMill>
        implements ISurvivalConstructable {

    protected boolean boostEu = false;
    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntity_IsaMill> STRUCTURE_DEFINITION = null;

    private static final IIconContainer frontFaceActive = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE5");
    private static final IIconContainer frontFace = new CustomIcon("iconsets/Grinder/GRINDER5");

    private final ArrayList<GT_MetaTileEntity_Hatch_MillingBalls> mMillingBallBuses = new ArrayList<GT_MetaTileEntity_Hatch_MillingBalls>();
    private static final DamageSource mIsaMillDamageSource = new DamageSource("gtpp.grinder").setDamageBypassesArmor();

    public GregtechMetaTileEntity_IsaMill(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IsaMill(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Controller Block for the Large Grinding Machine")
                .addInfo("Grind ores.").addPollutionAmount(getPollutionPerSecond(null)).addSeparator()
                .beginStructureBlock(3, 3, 7, false).addController("Front Center")
                .addCasingInfo("IsaMill Exterior Casing", 40)
                .addOtherStructurePart("IsaMill Gearbox", "5x, Inner Blocks")
                .addOtherStructurePart("IsaMill Piping", "8x, ring around controller")
                .addStructureInfo("IsaMill Pipings must not be obstructed in front (only air blocks)")
                .addOtherStructurePart("Milling Ball Hatch", "Any Casing").addInputBus("Any Casing", 1)
                .addOutputBus("Any Casing", 1).addEnergyHatch("Any Casing", 1).addMaintenanceHatch("Any Casing", 1)
                .addMufflerHatch("Any Casing", 1).toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IsaMill> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IsaMill>builder().addShape(
                    mName,
                    transpose(
                            new String[][] { { "DDD", "CCC", "CCC", "CCC", "CCC", "CCC", "CCC" },
                                    { "D~D", "CGC", "CGC", "CGC", "CGC", "CGC", "CCC" },
                                    { "DDD", "CCC", "CCC", "CCC", "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            ofChain(
                                    buildHatchAdder(GregtechMetaTileEntity_IsaMill.class)
                                            .adder(GregtechMetaTileEntity_IsaMill::addMillingBallsHatch)
                                            .hatchClass(GT_MetaTileEntity_Hatch_MillingBalls.class)
                                            .shouldReject(t -> !t.mMillingBallBuses.isEmpty())
                                            .casingIndex(getCasingTextureIndex()).dot(1).build(),
                                    buildHatchAdder(GregtechMetaTileEntity_IsaMill.class).atLeast(
                                            InputBus,
                                            OutputBus,
                                            InputHatch,
                                            OutputHatch,
                                            Maintenance,
                                            Energy,
                                            Muffler).casingIndex(getCasingTextureIndex()).dot(1).build(),
                                    onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
                    .addElement('D', ofBlock(getIntakeBlock(), getIntakeMeta()))
                    .addElement('G', ofBlock(getGearboxBlock(), getGearboxMeta())).build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        mMillingBallBuses.clear();
        return checkPiece(mName, 1, 1, 0) && mCasing >= 48 - 8 && checkHatch();
    }

    @Override
    public boolean checkHatch() {
        return super.checkHatch() && mMillingBallBuses.size() == 1;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return frontFaceActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return frontFace;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(2);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    private boolean addMillingBallsHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MillingBalls) {
                return addToMachineListInternal(mMillingBallBuses, aMetaTileEntity, aBaseCasingIndex);
            }
        }
        return false;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {

        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_MillingBalls) {
            log("Found GT_MetaTileEntity_Hatch_MillingBalls");
            return addToMachineListInternal(mMillingBallBuses, aMetaTileEntity, aBaseCasingIndex);
        }
        return super.addToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GTPP_Recipe.GTPP_Recipe_Map.sOreMillRecipes;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mMillingBallBuses.clear();
            }
        }
        if (aTick % 20 == 0 && isMachineRunning()) {
            checkForEntities(aBaseMetaTileEntity, aTick);
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    private final AutoMap<BlockPos> mFrontBlockPosCache = new AutoMap<BlockPos>();

    public void checkForEntities(IGregTechTileEntity aBaseMetaTileEntity, long aTime) {

        if (aTime % 100 == 0) {
            mFrontBlockPosCache.clear();
        }
        if (mFrontBlockPosCache.isEmpty()) {
            byte tSide = aBaseMetaTileEntity.getBackFacing();
            int aTileX = aBaseMetaTileEntity.getXCoord();
            int aTileY = aBaseMetaTileEntity.getYCoord();
            int aTileZ = aBaseMetaTileEntity.getZCoord();
            boolean xFacing = (tSide == 4 || tSide == 5);
            boolean zFacing = (tSide == 2 || tSide == 3);

            // Check Casings
            int aDepthOffset = (tSide == 2 || tSide == 4) ? 1 : -1;
            for (int aHorizontalOffset = -1; aHorizontalOffset < 2; aHorizontalOffset++) {
                for (int aVerticalOffset = -1; aVerticalOffset < 2; aVerticalOffset++) {
                    int aX = !xFacing ? (aTileX + aHorizontalOffset) : (aTileX + aDepthOffset);
                    int aY = aTileY + aVerticalOffset;
                    int aZ = !zFacing ? (aTileZ + aHorizontalOffset) : (aTileZ + aDepthOffset);
                    mFrontBlockPosCache.add(new BlockPos(aX, aY, aZ, aBaseMetaTileEntity.getWorld()));
                }
            }
        }

        AutoMap<EntityLivingBase> aEntities = getEntities(mFrontBlockPosCache, aBaseMetaTileEntity.getWorld());
        if (!aEntities.isEmpty()) {
            for (EntityLivingBase aFoundEntity : aEntities) {
                if (aFoundEntity instanceof EntityPlayer) {
                    EntityPlayer aPlayer = (EntityPlayer) aFoundEntity;
                    if (PlayerUtils.isCreative(aPlayer) || !PlayerUtils.canTakeDamage(aPlayer)) {
                        continue;
                    } else {
                        if (aFoundEntity.getHealth() > 0) {
                            EntityUtils.doDamage(aFoundEntity, mIsaMillDamageSource, getPlayerDamageValue(aPlayer, 10));
                            if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())) {
                                generateParticles(aFoundEntity);
                            }
                        }
                    }
                } else if (aFoundEntity.getHealth() > 0) {
                    EntityUtils.doDamage(
                            aFoundEntity,
                            mIsaMillDamageSource,
                            Math.max(1, (int) (aFoundEntity.getMaxHealth() / 3)));
                    if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())) {
                        generateParticles(aFoundEntity);
                    }
                }
            }
        }
    }

    // 20 armor points add 80% damage reduction, more points add more damage reduction
    private int getPlayerDamageValue(EntityPlayer player, int damage) {
        int armorValue = player.getTotalArmorValue();
        int reducedDamage = (int) (damage - damage * (armorValue * 0.04));
        return Math.max(reducedDamage, 0);
    }

    private static final AutoMap<EntityLivingBase> getEntities(AutoMap<BlockPos> aPositionsToCheck, World aWorld) {
        AutoMap<EntityLivingBase> aEntities = new AutoMap<EntityLivingBase>();
        HashSet<Chunk> aChunksToCheck = new HashSet<Chunk>();
        if (!aPositionsToCheck.isEmpty()) {
            Chunk aLocalChunk;
            for (BlockPos aPos : aPositionsToCheck) {
                aLocalChunk = aWorld.getChunkFromBlockCoords(aPos.xPos, aPos.zPos);
                aChunksToCheck.add(aLocalChunk);
            }
        }
        if (!aChunksToCheck.isEmpty()) {
            AutoMap<EntityLivingBase> aEntitiesFound = new AutoMap<EntityLivingBase>();
            for (Chunk aChunk : aChunksToCheck) {
                if (aChunk.isChunkLoaded) {
                    List[] aEntityLists = aChunk.entityLists;
                    for (List aEntitySubList : aEntityLists) {
                        for (Object aEntity : aEntitySubList) {
                            if (aEntity instanceof EntityLivingBase) {
                                EntityLivingBase aPlayer = (EntityLivingBase) aEntity;
                                aEntitiesFound.add(aPlayer);
                            }
                        }
                    }
                }
            }
            if (!aEntitiesFound.isEmpty()) {
                for (EntityLivingBase aEntity : aEntitiesFound) {
                    BlockPos aPlayerPos = EntityUtils.findBlockPosOfEntity(aEntity);
                    for (BlockPos aBlockSpaceToCheck : aPositionsToCheck) {
                        if (aBlockSpaceToCheck.equals(aPlayerPos)) {
                            aEntities.add(aEntity);
                        }
                    }
                }
            }
        }
        return aEntities;
    }

    private static void generateParticles(EntityLivingBase aEntity) {
        BlockPos aPlayerPosBottom = EntityUtils.findBlockPosOfEntity(aEntity);
        BlockPos aPlayerPosTop = aPlayerPosBottom.getUp();
        AutoMap<BlockPos> aEntityPositions = new AutoMap<BlockPos>();
        aEntityPositions.add(aPlayerPosBottom);
        aEntityPositions.add(aPlayerPosTop);
        for (int i = 0; i < 64; i++) {
            BlockPos aEffectPos = aEntityPositions.get(aEntity.height > 1f ? MathUtils.randInt(0, 1) : 0);
            float aOffsetX = MathUtils.randFloat(-0.35f, 0.35f);
            float aOffsetY = MathUtils.randFloat(-0.25f, 0.35f);
            float aOffsetZ = MathUtils.randFloat(-0.35f, 0.35f);
            aEntity.worldObj.spawnParticle(
                    "reddust",
                    aEffectPos.xPos + aOffsetX,
                    aEffectPos.yPos + 0.3f + aOffsetY,
                    aEffectPos.zPos + aOffsetZ,
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        return checkRecipeGeneric();
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings5Misc;
    }

    public byte getCasingMeta() {
        return 0;
    }

    public Block getIntakeBlock() {
        return ModBlocks.blockCasings5Misc;
    }

    public byte getIntakeMeta() {
        return 1;
    }

    public Block getGearboxBlock() {
        return ModBlocks.blockCasings5Misc;
    }

    public byte getGearboxMeta() {
        return 2;
    }

    public byte getCasingTextureIndex() {
        return 66;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_IsaMill(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiIsaMill;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getExtraInfoData() {
        return new String[] { "IsaMill Grinding Machine", "Current Efficiency: " + (mEfficiency / 100) + "%",
                getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance" };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMachineType() {
        return "Grinding Machine";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 0;
    }

    /*
     * Milling Ball Handling
     */

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> tItems = super.getStoredInputs();
        for (GT_MetaTileEntity_Hatch_MillingBalls tHatch : mMillingBallBuses) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                tItems.addAll(tHatch.getContentUsageSlots());
            }
        }
        return tItems;
    }

    public int getMaxBallDurability(ItemStack aStack) {
        return ItemGenericChemBase.getMaxBallDurability(aStack);
    }

    private ItemStack findMillingBall(ItemStack[] aItemInputs) {
        if (mMillingBallBuses.size() != 1) {
            return null;
        } else {
            GT_MetaTileEntity_Hatch_MillingBalls aBus = mMillingBallBuses.get(0);
            if (aBus != null) {
                AutoMap<ItemStack> aAvailableItems = aBus.getContentUsageSlots();
                if (!aAvailableItems.isEmpty()) {
                    for (final ItemStack aInput : aItemInputs) {
                        if (ItemUtils.isMillingBall(aInput)) {
                            for (ItemStack aBall : aAvailableItems) {
                                if (GT_Utility.areStacksEqual(aBall, aInput, true)) {
                                    Logger.INFO("Found a valid milling ball to use.");
                                    return aBall;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void damageMillingBall(ItemStack aStack) {
        if (MathUtils.randFloat(0, 10000000) / 10000000f < (1.2f - (0.2 * 1))) {
            int damage = getMillingBallDamage(aStack) + 1;
            log("damage milling ball " + damage);
            if (damage >= getMaxBallDurability(aStack)) {
                log("consuming milling ball");
                aStack.stackSize -= 1;
            } else {
                setDamage(aStack, damage);
            }
        } else {
            log("not damaging milling ball");
        }
    }

    private int getMillingBallDamage(ItemStack aStack) {
        return ItemGenericChemBase.getMillingBallDamage(aStack);
    }

    private void setDamage(ItemStack aStack, int aAmount) {
        ItemGenericChemBase.setMillingBallDamage(aStack, aAmount);
    }

    @Override
    public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
            long aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {

        // Based on the Processing Array. A bit overkill, but very flexible.

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        // checks if it has a milling ball with enough durability
        ItemStack tMillingBallRecipe = findMillingBall(aItemInputs);
        if (tMillingBallRecipe == null) {
            return false;
        }

        GT_Recipe tRecipe = findRecipe(
                getBaseMetaTileEntity(),
                mLastRecipe,
                false,
                gregtech.api.enums.GT_Values.V[tTier],
                aFluidInputs,
                aItemInputs);

        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        if (tRecipe == null) {
            return false;
        }

        boolean tSuccess = super.checkRecipeGeneric(
                aItemInputs,
                aFluidInputs,
                aMaxParallelRecipes,
                aEUPercent,
                aSpeedBonusPercent,
                aOutputChanceRoll,
                tRecipe);

        // Damage Milling ball once all is said and done.
        if (tSuccess) {
            damageMillingBall(tMillingBallRecipe);
        }

        return tSuccess;
    }
}
