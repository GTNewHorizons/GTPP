package gtPlusPlus.xmod.gregtech.recipes;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.GT_Values.RA;
import static gtPlusPlus.core.lib.CORE.GTNH;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.util.*;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Recipe.GT_Recipe_AssemblyLine;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy_RTG;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntity_RTG;
import gtPlusPlus.xmod.gregtech.recipes.machines.RECIPEHANDLER_MatterFabricator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {

    @Override
    public boolean addCokeOvenRecipe(
            final ItemStack aInput1,
            final ItemStack aInput2,
            final FluidStack aFluidInput,
            final FluidStack aFluidOutput,
            final ItemStack aOutput,
            int aDuration,
            final int aEUt) {
        try {
            try {
                // RECIPEHANDLER_CokeOven.debug1();
                if (((aInput1 == null) /* && (aFluidInput == null) */)
                        || ((aOutput == null) || (aFluidOutput == null))) {
                    // Utils.LOG_WARNING("aInput1:"+aInput1.toString()+"
                    // aInput2:"+aInput2.toString()+"
                    // aFluidInput:"+aFluidInput.toString()+"
                    // aFluidOutput:"+aFluidOutput.toString()+"
                    // aOutput:"+aOutput.toString()+" aDuration:"+aDuration+"
                    // aEU/t:"+aEUt);
                    Logger.WARNING("Something was null, returning false");
                    return false;
                }

            } catch (final NullPointerException e) {
                e.getStackTrace();
            }
            try {
                // RECIPEHANDLER_CokeOven.debug2(aInput1, aInput2, aFluidInput,
                // aFluidOutput, aOutput, aDuration, aEUt);
                if ((aOutput != null)
                        && ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aOutput, aDuration)) <= 0)) {
                    // Utils.LOG_WARNING("aInput1:"+aInput1.toString()+"
                    // aInput2:"+aInput2.toString()+"
                    // aFluidInput:"+aFluidInput.toString()+"
                    // aFluidOutput:"+aFluidOutput.toString()+"
                    // aOutput:"+aOutput.toString()+" aDuration:"+aDuration+"
                    // aEU/t:"+aEUt);
                    Logger.WARNING("Something was null, returning false");
                    return false;
                }

            } catch (final NullPointerException e) {
                e.getStackTrace();
            }
            try {
                // RECIPEHANDLER_CokeOven.debug3(aInput1, aInput2, aFluidInput,
                // aFluidOutput, aOutput, aDuration, aEUt);
                if ((aFluidOutput == null)
                        || ((aDuration = GregTech_API.sRecipeFile.get(
                                        "cokeoven", aFluidOutput.getFluid().getName(), aDuration))
                                <= 0)) {
                    // Utils.LOG_WARNING("aInput1:"+aInput1.toString()+"
                    // aInput2:"+aInput2.toString()+"
                    // aFluidInput:"+aFluidInput.toString()+"
                    // aFluidOutput:"+aFluidOutput.toString()+"
                    // aOutput:"+aOutput.toString()+" aDuration:"+aDuration+"
                    // aEU/t:"+aEUt);
                    Logger.WARNING("Something was null, returning false");
                    return false;
                }

            } catch (final NullPointerException e) {
                e.getStackTrace();
            }
            try {

                GTPP_Recipe aSpecialRecipe = new GTPP_Recipe(
                        true,
                        new ItemStack[] {aInput1, aInput2},
                        new ItemStack[] {aOutput},
                        null,
                        new int[] {},
                        new FluidStack[] {aFluidInput},
                        new FluidStack[] {aFluidOutput},
                        Math.max(1, aDuration),
                        Math.max(1, aEUt),
                        0);

                int aSize = GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.mRecipeList.size();
                int aSize2 = aSize;
                GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.add(aSpecialRecipe);
                aSize = GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.mRecipeList.size();

                // RECIPEHANDLER_CokeOven.debug4(aInput1, aInput2, aFluidInput,
                // aFluidOutput, aOutput, aDuration, aEUt);
                /*if (aFluidInput == null && aInput2 != null) {
                	GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[] { aInput1, aInput2 },
                			new ItemStack[] { aOutput }, null, null, null, new FluidStack[] { aFluidOutput }, aDuration,
                			aEUt, 0);
                }
                else if (aFluidInput == null && aInput2 == null) {
                	GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[] { aInput1 },
                			new ItemStack[] { aOutput }, null, null, null, new FluidStack[] { aFluidOutput }, aDuration,
                			aEUt, 0);
                }
                else {
                	GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[] { aInput1, aInput2 },
                			new ItemStack[] { aOutput }, null, null, new FluidStack[] { aFluidInput },
                			new FluidStack[] { aFluidOutput }, aDuration, aEUt, 0);
                }*/
                // RECIPEHANDLER_CokeOven.debug5(aInput1, aInput2, aFluidInput,
                // aFluidOutput, aOutput, aDuration, aEUt);

                return aSize > aSize2;

            } catch (final NullPointerException e) {
                Logger.WARNING("Something was null, returning false");
                return false;
            }
        } catch (final Throwable e) {
            // Logger.WARNING("aInput1:"+aInput1.toString()+"
            // aInput2:"+aInput2.toString()+"
            // aFluidInput:"+aFluidInput.toString()+"
            // aFluidOutput:"+aFluidOutput.toString()+"
            // aOutput:"+aOutput.toString()+" aDuration:"+aDuration+"
            // aEU/t:"+aEUt);
            Logger.WARNING("Failed.");
            e.getStackTrace();
            return false;
        }
    }

    @Override
    public boolean addCokeOvenRecipe(
            int aCircuit,
            ItemStack aInput2,
            FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs,
            ItemStack[] aOutputs,
            int aDuration,
            int aEUt) {
        return addCokeOvenRecipe(
                CI.getNumberedCircuit(aCircuit), aInput2, aFluidInputs, aFluidOutputs, aOutputs, aDuration, aEUt);
    }

    @Override
    public boolean addCokeOvenRecipe(
            ItemStack aInput1,
            ItemStack aInput2,
            FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs,
            ItemStack[] aOutputs,
            int aDuration,
            int aEUt) {
        GTPP_Recipe aSpecialRecipe = new GTPP_Recipe(
                true,
                new ItemStack[] {aInput1, aInput2},
                aOutputs,
                null,
                new int[] {},
                aFluidInputs,
                aFluidOutputs,
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                0);
        int aSize = GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.mRecipeList.size();
        GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.add(aSpecialRecipe);
        return GTPP_Recipe.GTPP_Recipe_Map.sCokeOvenRecipes.mRecipeList.size() > aSize;
    }

    @Override
    public boolean addMatterFabricatorRecipe(
            final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aDuration, final int aEUt) {
        try {
            try {
                // RECIPEHANDLER_MatterFabricator.debug1();
                if (aFluidOutput == null) {
                    // Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+"
                    // aFluidOutput:"+aFluidOutput.toString()+"
                    // aDuration:"+aDuration+" aEU/t:"+aEUt);
                    Logger.WARNING("Something was null, returning false");
                    return false;
                }

            } catch (final NullPointerException e) {
                e.getStackTrace();
            }
            try {

                // RECIPEHANDLER_MatterFabricator.debug4(aFluidInput,
                // aFluidOutput, aDuration, aEUt);
                if (aFluidInput == null) {
                    // Recipe_GT.Gregtech_Recipe_Map.sMatterFabRecipes.addRecipe(true,
                    // null, new FluidStack[]{aFluidOutput}, aDuration, aEUt,
                    // 0);

                    GTPP_Recipe aRecipe = new GTPP_Recipe(
                            false,
                            new ItemStack[] {},
                            new ItemStack[] {},
                            null,
                            new int[] {},
                            new FluidStack[] {},
                            new FluidStack[] {aFluidOutput},
                            aDuration,
                            aEUt,
                            0);
                    GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.addRecipe(aRecipe);
                } else {
                    // Recipe_GT.Gregtech_Recipe_Map.sMatterFabRecipes.addRecipe(true,
                    // new FluidStack[]{aFluidInput}, new
                    // FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
                    GTPP_Recipe aRecipe = new GTPP_Recipe(
                            false,
                            new ItemStack[] {},
                            new ItemStack[] {},
                            null,
                            new int[] {},
                            new FluidStack[] {aFluidInput},
                            new FluidStack[] {aFluidOutput},
                            aDuration,
                            aEUt,
                            0);
                    GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.addRecipe(aRecipe);
                }
                RECIPEHANDLER_MatterFabricator.debug5(aFluidInput, aFluidOutput, aDuration, aEUt);

                return true;

            } catch (final NullPointerException e) {
                return false;
            }
        } catch (final Throwable e) {
            // Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+"
            // aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+"
            // aEU/t:"+aEUt);
            Logger.WARNING("Failed.");
            e.getStackTrace();
            return false;
        }
    }

    @Override
    public boolean addMatterFabricatorRecipe(
            final ItemStack aInputStack,
            final FluidStack aFluidInput,
            final FluidStack aFluidOutput,
            final int aDuration,
            final int aEUt) {
        try {
            try {
                if ((aFluidOutput == null) || (aInputStack == null)) {
                    return false;
                }
            } catch (final NullPointerException e) {
            }
            try {
                if (aFluidInput == null) {
                    GTPP_Recipe aRecipe = new GTPP_Recipe(
                            false,
                            new ItemStack[] {aInputStack},
                            new ItemStack[] {},
                            null,
                            new int[] {},
                            new FluidStack[] {},
                            new FluidStack[] {aFluidOutput},
                            aDuration,
                            aEUt,
                            0);
                    GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.addRecipe(aRecipe);
                } else {
                    GTPP_Recipe aRecipe = new GTPP_Recipe(
                            false,
                            new ItemStack[] {aInputStack},
                            new ItemStack[] {},
                            null,
                            new int[] {},
                            new FluidStack[] {aFluidInput},
                            new FluidStack[] {aFluidOutput},
                            aDuration,
                            aEUt,
                            0);
                    GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.addRecipe(aRecipe);
                }
                RECIPEHANDLER_MatterFabricator.debug5(aFluidInput, aFluidOutput, aDuration, aEUt);
                return true;
            } catch (final NullPointerException e) {
                return false;
            }
        } catch (final Throwable e) {
            return false;
        }
    }

    @Override
    public boolean addFuel(final ItemStack aInput1, final ItemStack aOutput1, final int aEU, final int aType) {
        if (aInput1 == null) {
            Logger.WARNING("Fuel Input is Invalid.");
            return false;
        }
        // new GregtechRecipe(aInput1, aOutput1,
        // GregTech_API.sRecipeFile.get("fuel_" + aType, aInput1, aEU), aType);
        return true;
    }

    /*
     * @Override public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack
     * aItemB, FluidStack aFluid, ItemStack[] aOutputItems, FluidStack
     * aOutputFluid, int aDuration, int aEUt) { if ((aItemA == null) || (aItemB
     * == null) || (aOutputItems == null)) { return false; } for (ItemStack
     * tStack : aOutputItems) { if (tStack != null) { if ((aDuration =
     * GregTech_API.sRecipeFile.get("dehydrator", aItemA, aDuration)) <= 0) {
     * return false; }
     * Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true,
     * new ItemStack[]{aItemA, aItemB}, aOutputItems, null, null, null,
     * aDuration, aEUt, 0); RECIPEHANDLER_Dehydrator.debug5(aItemA, aItemB,
     * aFluid, aOutputFluid, aOutputItems, aDuration, aEUt); return true; } }
     * return false; }
     * @Override public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack
     * aItemB, ItemStack[] aOutputItems, int aDuration, int aEUt) { if ((aItemA
     * == null) || (aItemB == null) || (aOutputItems == null)) { return false; }
     * if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aItemA,
     * aDuration)) <= 0) { return false; }
     * Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true,
     * new ItemStack[]{aItemA, aItemB}, aOutputItems, null, null, null,
     * aDuration, aEUt, 0); RECIPEHANDLER_Dehydrator.debug5(aItemA, aItemB,
     * null, null, aOutputItems, aDuration, aEUt); return true; }
     * @Override public boolean addDehydratorRecipe(FluidStack aFluid,
     * FluidStack aOutputFluid, ItemStack[] aOutputItems, int aDuration, int
     * aEUt){ if ((aFluid == null) || (aOutputFluid == null || aOutputItems ==
     * null)) { return false; } if ((aDuration =
     * GregTech_API.sRecipeFile.get("dehydrator", aFluid.getUnlocalizedName(),
     * aDuration)) <= 0) { return false; }
     * Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true,
     * null, aOutputItems, null, new FluidStack[]{aFluid}, new
     * FluidStack[]{aOutputFluid}, aDuration, aEUt, 0);
     * RECIPEHANDLER_Dehydrator.debug5(null, null, aFluid, aOutputFluid,
     * aOutputItems, aDuration, aEUt); return true; }
     */

    /*@Override
    public boolean addDehydratorRecipe(final ItemStack aInput, final FluidStack aFluid, final ItemStack[] aOutput,
    		int aDuration, final int aEUt) {
    	Logger.WARNING("Trying to add a Dehydrator recipe.");
    	try {
    		if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
    			return false;
    		}
    		if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aInput, aDuration)) <= 0) {
    			return false;
    		}
    		GTPP_Recipe aRecipe = new GTPP_Recipe(
    				false,
    				new ItemStack[] { aInput },
    				aOutput,
    				null,
    				new int[] {},
    				new FluidStack[] { aFluid },
    				new FluidStack[] {},
    				aDuration,
    				aEUt,
    				0);
    		GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(aRecipe);
    		// RECIPEHANDLER_Dehydrator.debug5(aInput, null, aFluid, null,
    		// aOutput, aDuration, aEUt);
    		return true;
    	}
    	catch (final NullPointerException e) {
    		Logger.WARNING("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
    		return false;
    	}
    }*/

    @Override
    public boolean addDehydratorRecipe(
            final ItemStack[] aInput,
            final FluidStack aFluidInput,
            final FluidStack aFluidOutput,
            final ItemStack[] aOutputItems,
            final int[] aChances,
            int aDuration,
            final int aEUt)
            throws IndexOutOfBoundsException {
        Logger.WARNING("Trying to add a Dehydrator recipe.");
        try {
            if (aInput != null && aInput.length > 0) {
                if (aInput[0] != null) {
                    Logger.WARNING("Recipe requires input: " + aInput[0].getDisplayName() + " x" + aInput[0].stackSize);
                }
                if (aInput.length > 1) {
                    if (aInput[1] != null) {
                        Logger.WARNING(
                                "Recipe requires input: " + aInput[1].getDisplayName() + " x" + aInput[1].stackSize);
                    }
                }
            }
            if (aFluidInput != null) {
                Logger.WARNING("Recipe requires input: "
                        + aFluidInput.getFluid().getName() + " " + aFluidInput.amount + "mbs");
            }
            if (((aInput == null || aInput.length == 0) && (aFluidInput == null))
                    || ((aOutputItems == null || aOutputItems.length == 0) && (aFluidOutput == null))) {
                return false;
            }
            if (aOutputItems != null) {
                Logger.WARNING("Recipe will output: " + ItemUtils.getArrayStackNames(aOutputItems));
            }
            if (aFluidOutput != null) {
                Logger.WARNING("Recipe will output: " + aFluidOutput.getFluid().getName());
            }

            GTPP_Recipe aSpecialRecipe = new GTPP_Recipe(
                    true,
                    aInput,
                    aOutputItems,
                    null,
                    aChances,
                    new FluidStack[] {aFluidInput},
                    new FluidStack[] {aFluidOutput},
                    Math.max(1, aDuration),
                    Math.max(1, aEUt),
                    0);

            int aSize = GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.mRecipeList.size();
            int aSize2 = aSize;
            GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.add(aSpecialRecipe);
            aSize = GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.mRecipeList.size();

            /*if (aInput.length == 1) {
            	Logger.WARNING("Dehydrator recipe only has a single input item.");
            	GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, aInput, aOutputItems, null,
            			aChances, new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput }, aDuration, aEUt,
            			0);

            }
            else {
            	Logger.WARNING("Dehydrator recipe has two input items.");
            	GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, aInput, aOutputItems, null,
            			aChances, new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput }, aDuration, aEUt,
            			0);
            }*/

            return aSize > aSize2;
        } catch (final NullPointerException e) {
            Logger.WARNING("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
            return false;
        }
    }

    @Override
    public boolean addBlastSmelterRecipe(
            final ItemStack[] aInput, FluidStack aOutput, final int aChance, int aDuration, final int aEUt) {
        return addBlastSmelterRecipe(
                aInput, null, aOutput, new ItemStack[] {}, new int[] {aChance}, aDuration, aEUt, 3700);
    }

    @Override
    public boolean addBlastSmelterRecipe(
            final ItemStack[] aInput,
            FluidStack aInputFluid,
            FluidStack aOutput,
            final int aChance,
            int aDuration,
            final int aEUt) {
        return addBlastSmelterRecipe(
                aInput, aInputFluid, aOutput, new ItemStack[] {}, new int[] {aChance}, aDuration, aEUt, 3700);
    }

    @Override
    public boolean addBlastSmelterRecipe(
            final ItemStack[] aInput,
            FluidStack aInputFluid,
            FluidStack aOutput,
            ItemStack[] aOutputStack,
            final int aChance[],
            int aDuration,
            final int aEUt) {
        return addBlastSmelterRecipe(aInput, aInputFluid, aOutput, aOutputStack, aChance, aDuration, aEUt, 3700);
    }

    @Override
    public boolean addBlastSmelterRecipe(
            ItemStack[] aInput,
            FluidStack aInputFluid,
            FluidStack aOutput,
            int aChance,
            int aDuration,
            int aEUt,
            int aSpecialValue) {
        return addBlastSmelterRecipe(
                aInput, aInputFluid, aOutput, new ItemStack[] {}, new int[] {aChance}, aDuration, aEUt, aSpecialValue);
    }

    @Override
    public boolean addBlastSmelterRecipe(
            ItemStack[] aInput,
            FluidStack aInputFluid,
            FluidStack aOutput,
            ItemStack[] aOutputStack,
            int[] aChance,
            int aDuration,
            int aEUt,
            int aSpecialValue) {
        if ((aInput == null) || (aOutput == null)) {
            Logger.WARNING("Fail - Input or Output was null.");
            return false;
        }

        if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1))) {
            aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
        }
        if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1))) {
            aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
        }
        if ((aDuration = GregTech_API.sRecipeFile.get(
                        "blastsmelter", aOutput.getFluid().getName(), aDuration))
                <= 0) {
            Logger.WARNING("Recipe did not register.");
            return false;
        }

        for (int das = 0; das < aInput.length; das++) {
            if (aInput[das] != null) {
                Logger.WARNING("tMaterial[" + das + "]: " + aInput[das].getDisplayName() + ", Amount: "
                        + aInput[das].stackSize);
            }
        }

        ArrayUtils.removeNulls(aInput);
        if (aInput.length <= 1) {
            return false;
        }

        int aSize = GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.mRecipeList.size();
        int aSize2 = aSize;
        GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.addRecipe(
                true,
                aInput,
                aOutputStack,
                null,
                aChance,
                new FluidStack[] {aInputFluid},
                new FluidStack[] {aOutput},
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                aSpecialValue);
        aSize = GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.mRecipeList.size();

        /*GTPP_Recipe.GTPP_Recipe_Map.sAlloyBlastSmelterRecipes.addRecipe(true, aInput, aOutputStack, null,
        aChance, new FluidStack[] { aInputFluid }, new FluidStack[] { aOutput }, aDuration, aEUt,
        aSpecialValue);*/

        return aSize > aSize2;
    }

    @Override
    public boolean addLFTRRecipe(
            final ItemStack aInput1,
            final FluidStack aInput2,
            final ItemStack aOutput1,
            final FluidStack aOutput2,
            final int aDuration,
            final int aEUt) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addLFTRRecipe(
            final ItemStack aInput1,
            final ItemStack aInput2,
            final ItemStack aOutput1,
            final int aDuration,
            final int aEUt) {
        return false;
    }

    @Override
    public boolean addLFTRRecipe(
            final FluidStack aInput1,
            final FluidStack aInput2,
            final FluidStack aOutput1,
            final int aDuration,
            final int aEUt) {
        if ((aInput1 == null) || (aInput2 == null) || (aOutput1 == null) || (aDuration < 1) || (aEUt < 1)) {
            return false;
        }
        GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.addRecipe(
                null, new FluidStack[] {aInput1, aInput2}, new FluidStack[] {aOutput1}, aDuration, aEUt, 16000);
        return true;
    }

    @Override
    public boolean addFissionFuel(
            final FluidStack aInput1,
            final FluidStack aInput2,
            final FluidStack aInput3,
            final FluidStack aInput4,
            final FluidStack aInput5,
            final FluidStack aInput6,
            final FluidStack aInput7,
            final FluidStack aInput8,
            final FluidStack aInput9,
            final FluidStack aOutput1,
            final FluidStack aOutput2,
            final int aDuration,
            final int aEUt) {
        return addFissionFuel(
                false, aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9, aOutput1,
                aOutput2, aDuration, aEUt);
    }

    @Override
    public boolean addFissionFuel(
            final boolean aOptimise,
            final FluidStack aInput1,
            final FluidStack aInput2,
            final FluidStack aInput3,
            final FluidStack aInput4,
            final FluidStack aInput5,
            final FluidStack aInput6,
            final FluidStack aInput7,
            final FluidStack aInput8,
            final FluidStack aInput9,
            final FluidStack aOutput1,
            final FluidStack aOutput2,
            final int aDuration,
            final int aEUt) {

        if ((aInput1 == null) || (aOutput1 == null) || (aDuration < 1) || (aEUt < 1)) {
            return false;
        }
        final FluidStack inputs[] = {aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9};
        final FluidStack outputs[] = {aOutput1, aOutput2};

        int aSize = GTPP_Recipe.GTPP_Recipe_Map.sFissionFuelProcessing.mRecipeList.size();
        int aSize2 = aSize;
        GTPP_Recipe.GTPP_Recipe_Map.sFissionFuelProcessing.addRecipe(
                aOptimise,
                new ItemStack[] {},
                new ItemStack[] {},
                null,
                new int[] {},
                inputs,
                outputs,
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                0);
        aSize = GTPP_Recipe.GTPP_Recipe_Map.sFissionFuelProcessing.mRecipeList.size();

        if (aSize > aSize2) {
            Logger.INFO("Added Nuclear Fuel Recipe.");
            return true;
        }
        return false;
    }

    @Override
    public boolean addCyclotronRecipe(
            ItemStack aInputs,
            FluidStack aFluidInput,
            ItemStack[] aOutputs,
            FluidStack aFluidOutput,
            int[] aChances,
            int aDuration,
            int aEUt,
            int aSpecialValue) {
        return addCyclotronRecipe(
                new ItemStack[] {aInputs},
                aFluidInput,
                aOutputs,
                aFluidOutput,
                aChances,
                aDuration,
                aEUt,
                aSpecialValue);
    }

    @Override
    public boolean addCyclotronRecipe(
            ItemStack[] aInputs,
            FluidStack aFluidInput,
            ItemStack[] aOutput,
            FluidStack aFluidOutput,
            int[] aChances,
            int aDuration,
            int aEUt,
            int aSpecialValue) {
        if (aOutput == null || aOutput.length < 1 || !ItemUtils.checkForInvalidItems(aOutput)) {
            Logger.INFO("Bad output for Cyclotron Recipe.");
            return false;
        }

        GTPP_Recipe aSpecialRecipe = new GTPP_Recipe(
                true,
                aInputs,
                aOutput,
                null,
                aChances,
                new FluidStack[] {aFluidInput},
                new FluidStack[] {aFluidOutput},
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                aSpecialValue);

        int aSize = GTPP_Recipe.GTPP_Recipe_Map.sCyclotronRecipes.mRecipeList.size();
        int aSize2 = aSize;
        GTPP_Recipe.GTPP_Recipe_Map.sCyclotronRecipes.add(aSpecialRecipe);
        aSize = GTPP_Recipe.GTPP_Recipe_Map.sCyclotronRecipes.mRecipeList.size();

        if (aSize > aSize2) {
            Logger.INFO("Added Cyclotron Recipe.");
            return true;
        }

        Logger.INFO("Failed to add Cyclotron Recipe. Output: " + ItemUtils.getArrayStackNames(aOutput));
        return false;
    }

    @Override
    public boolean addMixerRecipe(
            ItemStack aInput1,
            ItemStack aInput2,
            ItemStack aInput3,
            ItemStack aInput4,
            FluidStack aFluidInput,
            FluidStack aFluidOutput,
            ItemStack aOutput1,
            ItemStack aOutput2,
            ItemStack aOutput3,
            ItemStack aOutput4,
            int aDuration,
            int aEUt) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
            return false;
        }
        GTPP_Recipe aSpecialRecipe = new GTPP_Recipe(
                true,
                new ItemStack[] {aInput1, aInput2, aInput3, aInput4},
                new ItemStack[] {aOutput1, aOutput2, aOutput3, aOutput4},
                null,
                new int[] {},
                new FluidStack[] {aFluidInput},
                new FluidStack[] {aFluidOutput},
                Math.max(1, aDuration),
                Math.max(1, aEUt),
                0);

        int aSize = GT_Recipe_Map.sMixerRecipes.mRecipeList.size();
        GT_Recipe_Map.sMixerRecipes.add(aSpecialRecipe);
        return GT_Recipe_Map.sMixerRecipes.mRecipeList.size() > aSize;
    }

    // Machine Component Assembler
    @Override
    public boolean addComponentMakerRecipe(
            ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
        if (areItemsAndFluidsBothNull(aInputs, new FluidStack[] {aFluidInput})) {
            return false;
        }
        if (aOutput1 == null) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("machinecomponents", aOutput1, aDuration)) <= 0) {
            return false;
        }
        if (GTNH) {
            return false;
        }
        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false,
                aInputs,
                new ItemStack[] {aOutput1},
                null,
                new int[] {},
                new FluidStack[] {aFluidInput},
                new FluidStack[] {},
                aDuration,
                aEUt,
                0);
        GTPP_Recipe.GTPP_Recipe_Map.sComponentAssemblerRecipes.addRecipe(aRecipe);
        return true;
    }

    public boolean addMultiblockCentrifugeRecipe(
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs,
            ItemStack[] aOutputs,
            int[] aChances,
            int aDuration,
            int aEUtick,
            int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs)
                || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }

        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Large Centrifuge recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false, aInputs, aOutputs, null, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial);
        GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes_GT.addRecipe(aRecipe);

        // GTPP_Recipe.GTPP_Recipe_Map.sMultiblockCentrifugeRecipes.addRecipe(true, aInputs, aOutputs, null, aChances,
        // aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial);
        return true;
    }

    public boolean addMultiblockElectrolyzerRecipe(
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs,
            ItemStack[] aOutputs,
            int[] aChances,
            int aDuration,
            int aEUtick,
            int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs)
                || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }
        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Large Electrolyzer recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false, aInputs, aOutputs, null, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial);
        GTPP_Recipe.GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.addRecipe(aRecipe);
        // GTPP_Recipe.GTPP_Recipe_Map.sMultiblockElectrolyzerRecipes.addRecipe(true, aInputs, aOutputs, null, aChances,
        // aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial);
        return true;
    }

    public boolean addAdvancedFreezerRecipe(
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs,
            ItemStack[] aOutputs,
            int[] aChances,
            int aDuration,
            int aEUtick,
            int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs)
                || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }

        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Adv. Vac Freezer recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false, aInputs, aOutputs, null, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial);
        GTPP_Recipe.GTPP_Recipe_Map.sAdvFreezerRecipes_GT.addRecipe(aRecipe);
        /*if (GTPP_Recipe.GTPP_Recipe_Map.sAdvFreezerRecipes.addRecipe(true, aInputs, aOutputs, null, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial) != null) {
        	return true;
        }*/
        return false;
    }

    public boolean addMultiblockMixerRecipe(
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs,
            ItemStack[] aOutputs,
            int[] aChances,
            int aDuration,
            int aEUtick,
            int aSpecial) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs)
                || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)
                || aEUtick <= 0) {
            return false;
        }
        if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
            Logger.INFO("[Recipe] Error generating Large Mixer recipe.");
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aFluidInputs));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            return false;
        }

        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false, aInputs, aOutputs, null, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial);
        GTPP_Recipe.GTPP_Recipe_Map.sMultiblockMixerRecipes_GT.addRecipe(aRecipe);
        return true;
    }

    public boolean addAssemblerRecipeWithOreDict(
            Object aInput1, int aAmount1, Object aInput2, int aAmount2, ItemStack aOutput, int a1, int a2) {
        if (aInput1 instanceof String || aInput2 instanceof String) {
            int mCompleted = 0;
            if (aInput1 instanceof String && aInput2 instanceof String) {
                List<ItemStack> x = OreDictionary.getOres((String) aInput1, false);
                List<ItemStack> x1 = OreDictionary.getOres((String) aInput2, false);
                if (x != null && x.size() > 0 && x1 != null && x1.size() > 0) {
                    for (ItemStack r : x) {
                        r.stackSize = aAmount1;
                        for (ItemStack r1 : x1) {
                            r1.stackSize = aAmount2;
                            if (GT_Values.RA.addAssemblerRecipe(r, r1, aOutput, a1, a2)) {
                                mCompleted++;
                            }
                        }
                    }
                }
            } else if (aInput1 instanceof String) {
                List<ItemStack> x = OreDictionary.getOres((String) aInput1, false);
                if (x != null && x.size() > 0) {
                    for (ItemStack r : x) {
                        r.stackSize = aAmount1;
                        if (GT_Values.RA.addAssemblerRecipe(r, (ItemStack) aInput2, aOutput, a1, a2)) {
                            mCompleted++;
                        }
                    }
                }

            } else {
                List<ItemStack> x = OreDictionary.getOres((String) aInput2, false);
                if (x != null && x.size() > 0) {
                    for (ItemStack r : x) {
                        r.stackSize = aAmount1;
                        if (GT_Values.RA.addAssemblerRecipe((ItemStack) aInput1, r, aOutput, a1, a2)) {
                            mCompleted++;
                        }
                    }
                }
            }
            return mCompleted > 0;
        } else {
            return GT_Values.RA.addAssemblerRecipe((ItemStack) aInput1, (ItemStack) aInput2, aOutput, a1, a2);
        }
    }

    public boolean addAssemblerRecipeWithOreDict(
            Object aInput1,
            int aAmount1,
            Object aInput2,
            int aAmount2,
            FluidStack aInputFluid,
            ItemStack aOutput,
            int a1,
            int a2) {
        if (aInput1 instanceof String || aInput2 instanceof String) {
            int mCompleted = 0;
            if (aInput1 instanceof String && aInput2 instanceof String) {
                List<ItemStack> x = OreDictionary.getOres((String) aInput1, false);
                List<ItemStack> x1 = OreDictionary.getOres((String) aInput2, false);
                if (x != null && x.size() > 0 && x1 != null && x1.size() > 0) {
                    for (ItemStack r : x) {
                        r.stackSize = aAmount1;
                        for (ItemStack r1 : x1) {
                            r1.stackSize = aAmount2;
                            if (GT_Values.RA.addAssemblerRecipe(r, r1, aInputFluid, aOutput, a1, a2)) {
                                mCompleted++;
                            }
                        }
                    }
                }
            } else if (aInput1 instanceof String) {
                List<ItemStack> x = OreDictionary.getOres((String) aInput1, false);
                if (x != null && x.size() > 0) {
                    for (ItemStack r : x) {
                        r.stackSize = aAmount1;
                        if (GT_Values.RA.addAssemblerRecipe(r, (ItemStack) aInput2, aInputFluid, aOutput, a1, a2)) {
                            mCompleted++;
                        }
                    }
                }

            } else {
                List<ItemStack> x = OreDictionary.getOres((String) aInput2, false);
                if (x != null && x.size() > 0) {
                    for (ItemStack r : x) {
                        r.stackSize = aAmount1;
                        if (GT_Values.RA.addAssemblerRecipe((ItemStack) aInput1, r, aInputFluid, aOutput, a1, a2)) {
                            mCompleted++;
                        }
                    }
                }
            }
            return mCompleted > 0;
        } else {
            return GT_Values.RA.addAssemblerRecipe(
                    (ItemStack) aInput1, (ItemStack) aInput2, aInputFluid, aOutput, a1, a2);
        }
    }

    /*
     * Reflection Based Recipe Additions with Fallbacks
     */

    private static final Method mSixSlotAssembly;
    private static final Method mAssemblyLine;
    private static final Method mScannerTT;
    private static final Method[] mChemicalRecipe = new Method[3];
    private static final Method mLargeChemReactor;
    private static final Method mPyroOven;

    static {

        // Get GT's RA class;
        Class<? extends IGT_RecipeAdder> clazz = GT_Values.RA.getClass();

        mChemicalRecipe[0] = ReflectionUtils.getMethod(
                clazz,
                "addChemicalRecipe",
                ItemStack.class,
                ItemStack.class,
                FluidStack.class,
                FluidStack.class,
                ItemStack.class,
                int.class);

        if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {
            // 6 Slot Assembler
            mSixSlotAssembly = ReflectionUtils.getMethod(
                    clazz,
                    "addAssemblerRecipe",
                    ItemStack[].class,
                    FluidStack.class,
                    ItemStack.class,
                    int.class,
                    int.class);
            // Assembly Line
            mAssemblyLine = ReflectionUtils.getMethod(
                    clazz,
                    "addAssemblylineRecipe",
                    ItemStack.class,
                    int.class,
                    ItemStack[].class,
                    FluidStack[].class,
                    ItemStack.class,
                    int.class,
                    int.class);

            Method T = null;
            if (LoadedMods.TecTech) {
                Class TTRecipeAdder = ReflectionUtils.getClass("com.github.technus.tectech.recipe.TT_recipeAdder");
                if (TTRecipeAdder != null) {
                    Method ttTest = ReflectionUtils.getMethod(
                            TTRecipeAdder,
                            "addResearchableAssemblylineRecipe",
                            ItemStack.class,
                            int.class,
                            int.class,
                            int.class,
                            int.class,
                            Object[].class,
                            FluidStack[].class,
                            ItemStack.class,
                            int.class,
                            int.class);
                    if (ttTest != null) {
                        T = ttTest;
                    } else {
                        Method[] aDump = TTRecipeAdder.getDeclaredMethods();
                        for (Method m : aDump) {
                            if (m != null) {
                                Logger.INFO("Found " + m.getName() + " | " + m.getModifiers() + " | "
                                        + ArrayUtils.toString(m.getParameters(), "EMPTY") + "");
                                if (m.getName().toLowerCase().equals("addresearchableassemblylinerecipe")) {
                                    Logger.INFO("Types: " + ArrayUtils.toString(m.getParameterTypes()));
                                }
                            }
                        }
                    }
                }
            } else {
                T = null;
            }
            mScannerTT = T;

            mChemicalRecipe[1] = ReflectionUtils.getMethod(
                    clazz,
                    "addChemicalRecipe",
                    ItemStack.class,
                    ItemStack.class,
                    FluidStack.class,
                    FluidStack.class,
                    ItemStack.class,
                    int.class,
                    int.class);
            mChemicalRecipe[2] = ReflectionUtils.getMethod(
                    clazz,
                    "addChemicalRecipe",
                    ItemStack.class,
                    ItemStack.class,
                    FluidStack.class,
                    FluidStack.class,
                    ItemStack.class,
                    ItemStack.class,
                    int.class);

            mLargeChemReactor = ReflectionUtils.getMethod(
                    clazz,
                    "addMultiblockChemicalRecipe",
                    ItemStack[].class,
                    FluidStack[].class,
                    FluidStack[].class,
                    ItemStack[].class,
                    int.class,
                    int.class);

            mPyroOven = ReflectionUtils.getMethod(
                    clazz,
                    "addPyrolyseRecipe",
                    ItemStack.class,
                    FluidStack.class,
                    int.class,
                    ItemStack.class,
                    FluidStack.class,
                    int.class,
                    int.class);

        } else {
            mSixSlotAssembly = null;
            mAssemblyLine = null;
            mLargeChemReactor = null;
            mScannerTT = null;
            mPyroOven = null;
        }
    }

    public boolean addSixSlotAssemblingRecipe(
            ItemStack[] aInputs, FluidStack aInputFluid, ItemStack aOutput1, int aDuration, int aEUt) {
        if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {
            if (mSixSlotAssembly != null) {
                try {
                    return (boolean)
                            mSixSlotAssembly.invoke(GT_Values.RA, aInputs, aInputFluid, aOutput1, aDuration, aEUt);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    if (CORE.GTNH) {
                        return false;
                    }
                }
            }
        }
        return CORE.RA.addComponentMakerRecipe(aInputs, aInputFluid, aOutput1, aDuration, aEUt);
    }

    @Override
    public boolean addAssemblylineRecipe(
            ItemStack aResearchItem,
            int aResearchTime,
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            ItemStack aOutput,
            int aDuration,
            int aEUt) {
        if (GTNH)
            return RA.addAssemblylineRecipe(
                    aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput, aDuration, aEUt);
        if ((aResearchItem == null)
                || (aResearchTime <= 0)
                || (aInputs == null)
                || (aOutput == null)
                || aInputs.length > 15
                || aInputs.length < 4) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("assemblingline", aOutput, aDuration)) <= 0) {
            return false;
        }
        for (ItemStack tItem : aInputs) {
            if (tItem == null) {
                GT_FML_LOGGER.info("addAssemblingLineRecipe " + aResearchItem.getDisplayName() + " --> "
                        + aOutput.getUnlocalizedName() + " there is some null item in that recipe");
            }
        }
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] {aResearchItem},
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataStick.getWithName(1L, "Writes Research result", new Object[0])},
                null,
                null,
                aResearchTime,
                30,
                -201);
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(
                false,
                aInputs,
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataStick.getWithName(1L, "Reads Research result", new Object[0])},
                aFluidInputs,
                null,
                aDuration,
                aEUt,
                0,
                true);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(new GT_Recipe_AssemblyLine(
                aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput, aDuration, aEUt));
        return true;
    }

    @Override
    public boolean addAssemblylineRecipe(
            ItemStack aResearchItem,
            int aResearchTime,
            Object[] aInputs,
            FluidStack[] aFluidInputs,
            ItemStack aOutput,
            int aDuration,
            int aEUt) {
        if (GTNH)
            return RA.addAssemblylineRecipe(
                    aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput, aDuration, aEUt);
        if ((aResearchItem == null)
                || (aResearchTime <= 0)
                || (aInputs == null)
                || (aOutput == null)
                || aInputs.length > 15
                || aInputs.length < 4) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("assemblingline", aOutput, aDuration)) <= 0) {
            return false;
        }
        ItemStack[] tInputs = new ItemStack[aInputs.length];
        ItemStack[][] tAlts = new ItemStack[aInputs.length][];
        for (int i = 0; i < aInputs.length; i++) {
            Object obj = aInputs[i];
            if (obj instanceof ItemStack) {
                tInputs[i] = (ItemStack) obj;
                tAlts[i] = null;
                continue;
            } else if (obj instanceof ItemStack[]) {
                ItemStack[] aStacks = (ItemStack[]) obj;
                if (aStacks.length > 0) {
                    tInputs[i] = aStacks[0];
                    tAlts[i] = (ItemStack[]) Arrays.copyOf(aStacks, aStacks.length);
                    continue;
                }
            } else if (obj instanceof Object[]) {
                Object[] objs = (Object[]) obj;
                List<ItemStack> tList;
                if (objs.length >= 2 && !(tList = GT_OreDictUnificator.getOres(objs[0])).isEmpty()) {
                    try {
                        int tAmount = ((Number) objs[1]).intValue();
                        List<ItemStack> uList = new ArrayList<>();
                        for (ItemStack tStack : tList) {
                            ItemStack uStack = GT_Utility.copyAmount(tAmount, tStack);
                            if (GT_Utility.isStackValid(uStack)) {
                                uList.add(uStack);
                                if (tInputs[i] == null) tInputs[i] = uStack;
                            }
                        }
                        tAlts[i] = uList.toArray(new ItemStack[uList.size()]);
                        continue;
                    } catch (Exception t) {
                    }
                }
            }
            GT_FML_LOGGER.info("addAssemblingLineRecipe " + aResearchItem.getDisplayName() + " --> "
                    + aOutput.getUnlocalizedName() + " there is some null item in that recipe");
        }
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] {aResearchItem},
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataStick.getWithName(1L, "Writes Research result", new Object[0])},
                null,
                null,
                aResearchTime,
                30,
                -201);
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(
                false,
                tInputs,
                new ItemStack[] {aOutput},
                new ItemStack[] {ItemList.Tool_DataStick.getWithName(1L, "Reads Research result", new Object[0])},
                aFluidInputs,
                null,
                aDuration,
                aEUt,
                0,
                tAlts,
                true);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(new GT_Recipe_AssemblyLine(
                aResearchItem, aResearchTime, tInputs, aFluidInputs, aOutput, aDuration, aEUt, tAlts));
        return true;
    }

    private boolean tryAddTecTechScannerRecipe(
            ItemStack aResearchItem,
            Object[] aInputs,
            FluidStack[] aFluidInputs,
            ItemStack aOutput,
            int assDuration,
            int assEUt) {
        if (!LoadedMods.TecTech) {
            return true;
        } else {

            int compSec = (GT_Utility.getTier(assEUt) + 1) * 16;
            int compMax = (GT_Utility.getTier(assEUt) + 1) * 10000;

            if (mScannerTT != null) {
                try {
                    boolean aResult = (boolean) mScannerTT.invoke(
                            null,
                            aResearchItem,
                            compMax,
                            compSec,
                            (assEUt / 2),
                            16,
                            aInputs,
                            aFluidInputs,
                            aOutput,
                            assDuration,
                            assEUt);
                    Logger.INFO("Added TecTech Scanner Recipe for " + ItemUtils.getItemName(aResearchItem) + "? "
                            + aResult);
                    return aResult;

                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Logger.INFO("Failed to generate TecTech recipe for " + ItemUtils.getItemName(aResearchItem)
                            + ", please report this to Alkalus. [Severe]");
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean addChemicalRecipe(
            ItemStack input1,
            ItemStack input2,
            FluidStack inputFluid,
            FluidStack outputFluid,
            ItemStack output,
            int time,
            int eu) {
        return addChemicalRecipe(input1, input2, inputFluid, outputFluid, output, null, time, eu);
    }

    @Override
    public boolean addChemicalRecipe(
            ItemStack input1,
            ItemStack input2,
            FluidStack inputFluid,
            FluidStack outputFluid,
            ItemStack output,
            Object object,
            int time,
            int eu) {
        try {
            if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
                return (boolean)
                        mChemicalRecipe[0].invoke(GT_Values.RA, input1, input2, inputFluid, outputFluid, output, time);
            } else {
                return (boolean) mChemicalRecipe[1].invoke(
                        GT_Values.RA, input1, input2, inputFluid, outputFluid, output, time, eu);
            }
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean addChemicalRecipe(
            ItemStack input1,
            ItemStack input2,
            FluidStack inputFluid,
            FluidStack outputFluid,
            ItemStack output,
            ItemStack output2,
            int time) {
        try {
            if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
                return (boolean)
                        mChemicalRecipe[0].invoke(GT_Values.RA, input1, input2, inputFluid, outputFluid, output, time);
            } else {
                return (boolean) mChemicalRecipe[2].invoke(
                        GT_Values.RA, input1, input2, inputFluid, outputFluid, output, output2, time);
            }
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean addChemicalRecipe(
            ItemStack input1,
            ItemStack input2,
            int aCircuit,
            FluidStack inputFluid,
            FluidStack outputFluid,
            ItemStack output,
            ItemStack output2,
            int time,
            int eu) {
        if (aCircuit < 0 || aCircuit > 24) {
            aCircuit = 22;
        }
        GT_Recipe aSpecialRecipe = new GTPP_Recipe(
                false,
                new ItemStack[] {input1, input2},
                new ItemStack[] {output, output2},
                CI.getNumberedCircuit(aCircuit),
                new int[] {},
                new FluidStack[] {inputFluid},
                new FluidStack[] {outputFluid},
                time,
                eu,
                0);
        return GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList.add(aSpecialRecipe);
    }

    @Override
    public boolean addMultiblockChemicalRecipe(
            ItemStack[] itemStacks,
            FluidStack[] fluidStacks,
            FluidStack[] fluidStacks2,
            ItemStack[] outputs,
            int time,
            int eu) {
        if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || mLargeChemReactor == null) {
            return false;
        }
        try {
            return (boolean)
                    mLargeChemReactor.invoke(GT_Values.RA, itemStacks, fluidStacks, fluidStacks2, outputs, time, eu);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return false;
        }
    }

    private boolean areItemsAndFluidsBothNull(final ItemStack[] items, final FluidStack[] fluids) {
        boolean itemsNull = true;
        if (items != null) {
            for (final ItemStack itemStack : items) {
                if (itemStack != null) {
                    itemsNull = false;
                    break;
                }
            }
        }
        boolean fluidsNull = true;
        if (fluids != null) {
            for (final FluidStack fluidStack : fluids) {
                if (fluidStack != null) {
                    fluidsNull = false;
                    break;
                }
            }
        }
        return itemsNull && fluidsNull;
    }

    @Override
    public boolean addCompressorRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aInput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("compressor", aInput1, aDuration)) <= 0)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sCompressorRecipes.addRecipe(
                true, new ItemStack[] {aInput1}, new ItemStack[] {aOutput1}, null, null, null, aDuration, aEUt, 0);
        return true;
    }

    @Override
    public boolean addBrewingRecipe(
            int aCircuit, FluidStack aInput, FluidStack aOutput, int aTime, int aEu, boolean aHidden) {
        return addBrewingRecipe(CI.getNumberedCircuit(aCircuit), aInput, aOutput, aTime, aEu, aHidden);
    }

    @Override
    public boolean addBrewingRecipe(
            ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aTime, int aEu, boolean aHidden) {
        if ((aIngredient == null) || (aInput == null) || (aOutput == null)) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("brewing", aOutput.getUnlocalizedName(), true)) {
            return false;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBrewingRecipes.addRecipe(
                false,
                new ItemStack[] {aIngredient},
                null,
                null,
                new FluidStack[] {aInput},
                new FluidStack[] {aOutput},
                aTime,
                aEu,
                0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return true;
    }

    /**
     *  Lets me add recipes for GT 5.08 & 5.09, since someone broke the method headers.
     */
    @Override
    public boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aDust, ItemStack aOutput) {
        Method m = StaticFields59.mAddFurnaceRecipe;
        if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
            try {
                return (boolean) m.invoke(null, aDust, aOutput);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return false;
            }
        } else {
            try {
                return (boolean) m.invoke(null, aDust, aOutput, true);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return false;
            }
        }
    }

    @Override
    public boolean addFluidExtractionRecipe(ItemStack input, FluidStack output, int aTime, int aEu) {

        boolean aRecipe =
                RA.addFluidSmelterRecipe(GT_Utility.copyAmount(1, input), null, output, 10000, aTime, aEu, false);
        if (aRecipe) {
            Logger.INFO("Added Fluid Extractor Recipe: "
                    + input.getDisplayName() + " and obtain "
                    + output.amount + "mb of " + output.getLocalizedName()
                    + ". Time: " + aTime + ", Voltage: "
                    + aEu);
        }
        return aRecipe;

        // return MaterialGenerator.addFluidExtractionRecipe(GT_Values.NI, input, output, aTime, aEu);
    }

    @Override
    public boolean addFluidExtractionRecipe(
            ItemStack aEmpty, ItemStack aRemains, FluidStack aFluid, int aDuration, int aEU) {
        boolean aRecipe = RA.addFluidSmelterRecipe(
                GT_Utility.copyAmount(1, aEmpty), aRemains, aFluid, 10000, aDuration, aEU, false);
        return aRecipe;
        // return MaterialGenerator.addFluidExtractionRecipe(aEmpty, aRemains, aFluid, aDuration, aEU);
    }

    @Override
    public boolean addFluidCannerRecipe(ItemStack aContainer, ItemStack aFullContainer, FluidStack rFluidIn) {
        return MaterialGenerator.addFluidCannerRecipe(aContainer, aFullContainer, rFluidIn, null);
    }

    @Override
    public boolean addFluidCannerRecipe(
            ItemStack aContainer, ItemStack aFullContainer, FluidStack rFluidIn, FluidStack rFluidOut) {
        return MaterialGenerator.addFluidCannerRecipe(aContainer, aFullContainer, rFluidIn, rFluidOut);
    }

    @Override
    public boolean addFluidCannerRecipe(
            ItemStack aFullContainer,
            ItemStack container,
            FluidStack rFluidIn,
            FluidStack rFluidOut,
            int aTime,
            int aEu) {
        return MaterialGenerator.addFluidCannerRecipe(container, aFullContainer, rFluidIn, rFluidOut, aTime, aEu);
    }

    /**
     * Adds a Fusion reactor Recipe
     *
     * @param aInputStackA                        = first Input (not null, and respects StackSize)
     * @param aInputStackB                        = second Input (not null, and respects StackSize)
     * @param plasma                        = Output of the Fusion (can be null, and respects StackSize)
     * @param aOutputChance = chance to output plasma (can be 0)
     * @param aFusionDurationInTicks         = How many ticks the Fusion lasts (must be > 0)
     * @param aEu           = The EU generated per Tick (can even be negative!)
     * @param aSpecial = EU needed for heating the Reactor up (must be >= 0)
     */
    @Override
    public boolean addFusionReactorRecipe(
            FluidStack aInputStackA,
            FluidStack aInputStackB,
            FluidStack plasma,
            int aOutputChance,
            int aFusionDurationInTicks,
            int aEu,
            int aSpecial) {
        if (aInputStackA == null
                || aInputStackB == null
                || plasma == null
                || aFusionDurationInTicks < 1
                || aEu < 1
                || aSpecial < 1) {
            return false;
        }
        GTPP_Recipe aFusionCustom = new GTPP_Recipe(
                true,
                null,
                null,
                null,
                new int[] {aOutputChance},
                new FluidStack[] {aInputStackA, aInputStackB},
                new FluidStack[] {plasma},
                aFusionDurationInTicks,
                aEu,
                aSpecial);
        GT_Recipe.GT_Recipe_Map.sFusionRecipes.addRecipe(aFusionCustom);
        return true;
    }

    /**
     * Adds a Fusion reactor Recipe
     *
     * @param aInputStackA                        = first Input (not null, and respects StackSize)
     * @param aInputStackB                        = second Input (not null, and respects StackSize)
     * @param plasma                        = Output of the Fusion (can be null, and respects StackSize)
     * @param aFusionDurationInTicks         = How many ticks the Fusion lasts (must be > 0)
     * @param aEu           = The EU generated per Tick (can even be negative!)
     * @param aSpecial = EU needed for heating the Reactor up (must be >= 0)
     */
    @Override
    public boolean addFusionReactorRecipe(
            ItemStack aInputStackA,
            ItemStack aInputStackB,
            FluidStack plasma,
            int aFusionDurationInTicks,
            int aEu,
            int aSpecial) {
        return addFusionReactorRecipe(aInputStackA, aInputStackB, plasma, 10000, aFusionDurationInTicks, aEu, aSpecial);
    }

    /**
     * Adds a Fusion reactor Recipe
     *
     * @param aInputStackA                        = first Input (not null, and respects StackSize)
     * @param aInputStackB                        = second Input (not null, and respects StackSize)
     * @param plasma                        = Output of the Fusion (can be null, and respects StackSize)
     * @param aOutputChance = chance to output plasma (can be 0)
     * @param aFusionDurationInTicks         = How many ticks the Fusion lasts (must be > 0)
     * @param aEu           = The EU generated per Tick (can even be negative!)
     * @param aSpecial = EU needed for heating the Reactor up (must be >= 0)
     */
    @Override
    public boolean addFusionReactorRecipe(
            ItemStack aInputStackA,
            ItemStack aInputStackB,
            FluidStack plasma,
            int aOutputChance,
            int aFusionDurationInTicks,
            int aEu,
            int aSpecial) {
        if (aInputStackA == null
                || aInputStackB == null
                || plasma == null
                || aFusionDurationInTicks < 1
                || aEu < 1
                || aSpecial < 1) {
            return false;
        }
        GTPP_Recipe aFusionCustom = new GTPP_Recipe(
                true,
                new ItemStack[] {aInputStackA, aInputStackB},
                null,
                null,
                new int[] {aOutputChance},
                null,
                new FluidStack[] {plasma},
                aFusionDurationInTicks,
                aEu,
                aSpecial);
        GT_Recipe.GT_Recipe_Map.sFusionRecipes.addRecipe(aFusionCustom);
        return true;
    }

    @Override
    public boolean addSemifluidFuel(ItemStack aFuelItem, int aFuelValue) {
        return SemiFluidFuelHandler.addSemiFluidFuel(aFuelItem, aFuelValue);
    }

    @Override
    public boolean addSemifluidFuel(FluidStack aFuelItem, int aFuelValue) {
        return SemiFluidFuelHandler.addSemiFluidFuel(aFuelItem, aFuelValue);
    }

    @Override
    public boolean addVacuumFurnaceRecipe(
            ItemStack aInput1,
            ItemStack aInput2,
            FluidStack aFluidInput,
            FluidStack aFluidOutput,
            ItemStack aOutput1,
            ItemStack aOutput2,
            int aDuration,
            int aEUt,
            int aLevel) {
        if (aInput1 != null && aOutput1 != null) {
            return addVacuumFurnaceRecipe(
                    new ItemStack[] {aInput1, aInput2},
                    new FluidStack[] {aFluidInput},
                    new ItemStack[] {aOutput1, aOutput2},
                    new FluidStack[] {aFluidOutput},
                    aDuration,
                    aEUt,
                    aLevel);
        } else {
            return false;
        }
    }

    @Override
    public boolean addVacuumFurnaceRecipe(
            ItemStack[] aInputs,
            FluidStack[] aFluidInputs,
            ItemStack[] aOutputs,
            FluidStack[] aFluidOutputs,
            int aDuration,
            int aEUt,
            int aLevel) {
        if (aInputs != null && aOutputs != null) {
            int aSize = GTPP_Recipe.GTPP_Recipe_Map.sVacuumFurnaceRecipes.mRecipeList.size();
            int aSize2 = aSize;
            GTPP_Recipe.GTPP_Recipe_Map.sVacuumFurnaceRecipes.addRecipe(
                    false, aInputs, aOutputs, null, new int[] {}, aFluidInputs, aFluidOutputs, aDuration, aEUt, aLevel);
            aSize = GTPP_Recipe.GTPP_Recipe_Map.sVacuumFurnaceRecipes.mRecipeList.size();
            return aSize > aSize2;
        } else {
            return false;
        }
    }

    @Override
    public boolean addUvLaserRecipe(ItemStack aInput1, ItemStack aOutput, int time, long eu) {
        // Generate Special Laser Recipe
        GT_Recipe u = new GTPP_Recipe(
                false,
                new ItemStack[] {
                    aInput1, GregtechItemList.Laser_Lens_WoodsGlass.get(0),
                },
                new ItemStack[] {aOutput},
                null,
                new int[] {10000},
                new FluidStack[] {},
                new FluidStack[] {},
                time,
                (int) eu,
                0);
        return GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes.mRecipeList.add(u);
    }

    @Override
    public boolean addIrLaserRecipe(ItemStack aInput1, ItemStack aOutput, int time, long eu) {
        return addUvLaserRecipe(aInput1, aOutput, time, eu);
    }

    @Override
    public boolean addChemicalPlantRecipe(
            ItemStack[] aInputs,
            FluidStack[] aInputFluids,
            ItemStack[] aOutputs,
            FluidStack[] aFluidOutputs,
            int time,
            long eu,
            int aTier) {
        return addChemicalPlantRecipe(aInputs, aInputFluids, aOutputs, aFluidOutputs, new int[] {}, time, eu, aTier);
    }

    @Override
    public boolean addChemicalPlantRecipe(
            ItemStack[] aInputs,
            FluidStack[] aInputFluids,
            ItemStack[] aOutputs,
            FluidStack[] aFluidOutputs,
            int[] aChances,
            int time,
            long eu,
            int aTier) {

        if (aInputs.length > 4 || aInputFluids.length > 4 || aOutputs.length > 4 || aFluidOutputs.length > 2) {
            Logger.INFO("Inputs: " + ItemUtils.getArrayStackNames(aInputs));
            Logger.INFO("Fluid Inputs: " + ItemUtils.getArrayStackNames(aInputFluids));
            Logger.INFO("Outputs: " + ItemUtils.getArrayStackNames(aOutputs));
            Logger.INFO("Fluid Outputs: " + ItemUtils.getArrayStackNames(aFluidOutputs));
            CORE.crash();
        }

        int aSize = GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlantRecipes.mRecipeList.size();
        int aSize2 = aSize;
        GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlantRecipes.addRecipe(
                false, aInputs, aOutputs, null, aChances, aInputFluids, aFluidOutputs, time, (int) eu, aTier);
        aSize = GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlantRecipes.mRecipeList.size();
        return aSize > aSize2;
    }

    @Override
    public boolean addBlastRecipe(
            ItemStack[] aInputs,
            FluidStack[] aInputFluids,
            ItemStack[] aOutputs,
            FluidStack[] aFluidOutputs,
            int time,
            long eu,
            int aHeat) {
        GTPP_Recipe aSpecialRecipe = new GTPP_Recipe(
                false, aInputs, aOutputs, null, new int[] {}, aInputFluids, aFluidOutputs, time, (int) eu, aHeat);

        int aSize = GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.size();
        int aSize2 = aSize;
        GT_Recipe.GT_Recipe_Map.sBlastRecipes.add(aSpecialRecipe);
        aSize = GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.size();
        return aSize > aSize2;
    }

    @Override
    public boolean addPyrolyseRecipe(
            ItemStack aInput,
            FluidStack aFluidInput,
            int intCircuit,
            ItemStack aOutput,
            FluidStack aFluidOutput,
            int aDuration,
            int aEUt) {
        if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || mPyroOven == null) {
            return false;
        }
        try {
            return (boolean) mPyroOven.invoke(
                    GT_Values.RA, aInput, aFluidInput, intCircuit, aOutput, aFluidOutput, aDuration, aEUt);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return false;
        }
    }

    @Override
    public boolean addDistilleryRecipe(
            ItemStack aCircuit,
            FluidStack aInput,
            FluidStack aOutput,
            ItemStack aSolidOutput,
            int aDuration,
            int aEUt,
            boolean aHidden) {
        if (aInput != null && aOutput != null) {
            if ((aDuration = GregTech_API.sRecipeFile.get(
                            "distillery", aOutput.getFluid().getUnlocalizedName(), aDuration))
                    <= 0) {
                return false;
            } else {
                GT_Recipe tRecipe = GT_Recipe_Map.sDistilleryRecipes.addRecipe(
                        true,
                        new ItemStack[] {aCircuit},
                        new ItemStack[] {aSolidOutput},
                        (Object) null,
                        new FluidStack[] {aInput},
                        new FluidStack[] {aOutput},
                        aDuration,
                        aEUt,
                        0);
                if (aHidden && tRecipe != null) {
                    tRecipe.mHidden = true;
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        return addExtractorRecipe(aInput, aOutput, 10000, aDuration, aEUt);
    }

    @Override
    public boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aChance, int aDuration, int aEUt) {
        if (aInput != null && aOutput != null) {
            GT_Recipe aRecipe = new GTPP_Recipe(
                    false,
                    new ItemStack[] {aInput.copy()},
                    new ItemStack[] {aOutput.copy()},
                    null,
                    new int[] {aChance},
                    null,
                    null,
                    aDuration,
                    aEUt,
                    0);
            int aSize = GT_Recipe_Map.sExtractorRecipes.mRecipeList.size();
            GT_Recipe_Map.sExtractorRecipes.add(aRecipe);
            return GT_Recipe_Map.sExtractorRecipes.mRecipeList.size() > aSize;
        } else {
            return false;
        }
    }

    @Override
    public boolean addPulverisationRecipe(
            ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3) {
        // return GT_Values.RA.addPulveriserRecipe(arg0, arg1, arg2, arg3, arg4)

        aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
        aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
        aOutput3 = GT_OreDictUnificator.get(true, aOutput3);
        if ((GT_Utility.isStackInvalid(aInput)) || (GT_Utility.isStackInvalid(aOutput1))) {
            return false;
        }
        if (GT_Utility.getContainerItem(aInput, false) == null) {

            if (GregTech_API.sRecipeFile.get(ConfigCategories.Machines.maceration, aInput, true)) {
                GT_Utility.addSimpleIC2MachineRecipe(
                        aInput, GT_ModHandler.getMaceratorRecipeList(), null, new Object[] {aOutput1});
            }
            GT_Values.RA.addPulveriserRecipe(
                    aInput, new ItemStack[] {aOutput1, aOutput2, aOutput3}, new int[] {10000, 10000, 10000}, 400, 2);
        }
        return true;
    }

    @Override
    public boolean addMillingRecipe(Materials aMat, int aEU) {
        return addMillingRecipe(MaterialUtils.generateMaterialFromGtENUM(aMat), aEU);
    }

    @Override
    public boolean addMillingRecipe(Material aMat, int aEU) {

        ItemStack aOreStack = aMat.getOre(16);
        ItemStack aCrushedStack = aMat.getCrushed(16);

        ItemStack aMilledStackOres1 = aMat.getMilled(64);
        ItemStack aMilledStackCrushed1 = aMat.getMilled(32);
        ItemStack aMilledStackOres2 = aMat.getMilled(48);
        ItemStack aMilledStackCrushed2 = aMat.getMilled(16);

        ItemStack aMillingBall_Alumina = GregtechItemList.Milling_Ball_Alumina.get(0);
        ItemStack aMillingBall_Soapstone = GregtechItemList.Milling_Ball_Soapstone.get(0);

        // Inputs
        ItemStack[] aInputsOre1 = new ItemStack[] {CI.getNumberedCircuit(10), aOreStack, aMillingBall_Alumina};

        ItemStack[] aInputsOre2 = new ItemStack[] {CI.getNumberedCircuit(11), aOreStack, aMillingBall_Soapstone};

        ItemStack[] aInputsCrushed1 = new ItemStack[] {CI.getNumberedCircuit(10), aCrushedStack, aMillingBall_Alumina};

        ItemStack[] aInputsCrushed2 =
                new ItemStack[] {CI.getNumberedCircuit(11), aCrushedStack, aMillingBall_Soapstone};

        // Outputs
        ItemStack[] aOutputsOre1 = new ItemStack[] {aMilledStackOres1};

        ItemStack[] aOutputsOre2 = new ItemStack[] {aMilledStackOres2};

        ItemStack[] aOutputsCrushed1 = new ItemStack[] {aMilledStackCrushed1};

        ItemStack[] aOutputsCrushed2 = new ItemStack[] {aMilledStackCrushed2};

        ItemStack[][] aInputArray = new ItemStack[][] {aInputsOre1, aInputsOre2, aInputsCrushed1, aInputsCrushed2};
        ItemStack[][] aOutputArray = new ItemStack[][] {aOutputsOre1, aOutputsOre2, aOutputsCrushed1, aOutputsCrushed2};
        int[] aTime = new int[] {2400, 3000, 1200, 1500};

        int aSize = GTPP_Recipe.GTPP_Recipe_Map.sOreMillRecipes.mRecipeList.size();
        int aSize2 = aSize;

        for (int i = 0; i < 4; i++) {
            GTPP_Recipe aOreRecipe = new GTPP_Recipe(
                    false, aInputArray[i], aOutputArray[i], null, new int[] {}, null, null, aTime[i], aEU, 0);
            GTPP_Recipe.GTPP_Recipe_Map.sOreMillRecipes.add(aOreRecipe);
        }

        aSize = GTPP_Recipe.GTPP_Recipe_Map.sOreMillRecipes.mRecipeList.size();
        return aSize > aSize2;
    }

    @Override
    public boolean addFlotationRecipe(
            Materials aMat,
            ItemStack aXanthate,
            FluidStack[] aInputFluids,
            FluidStack[] aOutputFluids,
            int aTime,
            int aEU) {
        return addFlotationRecipe(
                MaterialUtils.generateMaterialFromGtENUM(aMat), aXanthate, aInputFluids, aOutputFluids, aTime, aEU);
    }

    @Override
    public boolean addFlotationRecipe(
            Material aMat,
            ItemStack aXanthate,
            FluidStack[] aInputFluids,
            FluidStack[] aOutputFluids,
            int aTime,
            int aEU) {

        FlotationRecipeHandler.registerOreType(aMat);

        int aSize = GTPP_Recipe.GTPP_Recipe_Map.sFlotationCellRecipes.mRecipeList.size();
        int aSize2 = aSize;

        GT_Recipe aRecipe = new GTPP_Recipe(
                false,
                new ItemStack[] {
                    ItemUtils.getSimpleStack(aXanthate, 32),
                    aMat.getMilled(64),
                    aMat.getMilled(64),
                    aMat.getMilled(64),
                    aMat.getMilled(64),
                },
                new ItemStack[] {},
                null,
                new int[] {},
                aInputFluids,
                aOutputFluids,
                aTime,
                aEU,
                0);

        GTPP_Recipe.GTPP_Recipe_Map.sFlotationCellRecipes.add(aRecipe);
        aSize = GTPP_Recipe.GTPP_Recipe_Map.sFlotationCellRecipes.mRecipeList.size();

        return aSize > aSize2;
    }

    @Override
    public boolean addpackagerRecipe(
            ItemStack aRecipeType, ItemStack aSmallDust, ItemStack aTinyDust, ItemStack aOutputStack1) {
        AutoMap<Boolean> aResults = new AutoMap<Boolean>();
        // Dust 1
        aResults.put(GT_Values.RA.addBoxingRecipe(
                GT_Utility.copyAmount(4L, new Object[] {aSmallDust}), aRecipeType, aOutputStack1, 100, 4));
        // Dust 2
        aResults.put(GT_Values.RA.addBoxingRecipe(
                GT_Utility.copyAmount(9L, new Object[] {aTinyDust}), aRecipeType, aOutputStack1, 100, 4));
        for (boolean b : aResults) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addFuelForRTG(ItemStack aFuelPellet, int aFuelDays, int aVoltage) {
        int aSize1 = GTPP_Recipe.GTPP_Recipe_Map.sRTGFuels.mRecipeList.size();
        GTPP_Recipe.GTPP_Recipe_Map.sRTGFuels.addRecipe(
                true, new ItemStack[] {aFuelPellet}, new ItemStack[] {}, null, null, null, 0, aVoltage, aFuelDays);
        int aSize2 = GTPP_Recipe.GTPP_Recipe_Map.sRTGFuels.mRecipeList.size();

        if (aSize2 > aSize1) {
            long eu = GregtechMetaTileEntity_RTG.getTotalEUGenerated(
                    GregtechMetaTileEntity_RTG.convertDaysToTicks(aFuelDays), aVoltage);
            GT_MetaTileEntity_Hatch_Energy_RTG.registerPelletForHatch(aFuelPellet, eu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addColdTrapRecipe(
            int aCircuit,
            ItemStack aInput,
            FluidStack aFluidInput,
            ItemStack[] aOutputs,
            int[] aChances,
            FluidStack aFluidOutput,
            int aTime,
            int aEU) {
        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false,
                new ItemStack[] {CI.getNumberedAdvancedCircuit(aCircuit), aInput},
                aOutputs,
                null,
                aChances,
                new FluidStack[] {aFluidInput},
                new FluidStack[] {aFluidOutput},
                aTime,
                aEU,
                0);

        int aSize = GTPP_Recipe_Map.sColdTrapRecipes.mRecipeList.size();
        GTPP_Recipe_Map.sColdTrapRecipes.add(aRecipe);
        return GTPP_Recipe_Map.sColdTrapRecipes.mRecipeList.size() > aSize;
    }

    @Override
    public boolean addReactorProcessingUnitRecipe(
            ItemStack aInput1,
            ItemStack aInput2,
            FluidStack aFluidInput,
            ItemStack[] aOutputs,
            int[] aChances,
            FluidStack aFluidOutput,
            int aTime,
            int aEU) {
        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false,
                new ItemStack[] {aInput1, aInput2},
                aOutputs,
                null,
                aChances,
                new FluidStack[] {aFluidInput},
                new FluidStack[] {aFluidOutput},
                aTime,
                aEU,
                0);

        int aSize = GTPP_Recipe_Map.sReactorProcessingUnitRecipes.mRecipeList.size();
        GTPP_Recipe_Map.sReactorProcessingUnitRecipes.add(aRecipe);
        return GTPP_Recipe_Map.sReactorProcessingUnitRecipes.mRecipeList.size() > aSize;
    }

    @Override
    public boolean addFluidHeaterRecipe(
            ItemStack aInput, FluidStack aFluidInput, FluidStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null && aFluidInput == null) || (aOutput == null)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes.addRecipe(
                true,
                new ItemStack[] {aInput},
                null,
                null,
                new FluidStack[] {aFluidInput},
                new FluidStack[] {aOutput},
                aDuration,
                aEUt,
                0);
        return true;
    }

    @Override
    public boolean addVacuumFreezerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false,
                new ItemStack[] {aInput},
                new ItemStack[] {aOutput},
                null,
                new int[] {10000},
                new FluidStack[] {},
                new FluidStack[] {},
                aDuration,
                aEUt,
                0);

        int aSize = GT_Recipe_Map.sVacuumRecipes.mRecipeList.size();
        GT_Recipe_Map.sVacuumRecipes.add(aRecipe);
        return GT_Recipe_Map.sVacuumRecipes.mRecipeList.size() > aSize;
    }

    @Override
    public boolean addMolecularTransformerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        GTPP_Recipe aRecipe = new GTPP_Recipe(
                false,
                new ItemStack[] {aInput},
                new ItemStack[] {aOutput},
                null,
                new int[] {10000},
                new FluidStack[] {},
                new FluidStack[] {},
                aDuration,
                aEUt,
                0);

        int aSize = GTPP_Recipe_Map.sMolecularTransformerRecipes.mRecipeList.size();
        GTPP_Recipe_Map.sMolecularTransformerRecipes.add(aRecipe);
        return GTPP_Recipe_Map.sMolecularTransformerRecipes.mRecipeList.size() > aSize;
    }

    @Override
    public boolean addMolecularTransformerRecipe(
            ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt, int aAmps) {
        return addMolecularTransformerRecipe(aInput, aOutput, aDuration, aEUt);
    }
}
