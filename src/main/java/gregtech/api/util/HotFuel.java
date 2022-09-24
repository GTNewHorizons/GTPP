package gregtech.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class HotFuel {

    public static void addNewHotFuel(
            FluidStack aInput1, FluidStack aOutput1, ItemStack[] outputItems, int[] chances, int aSpecialValue) {
        GTPP_Recipe.GTPP_Recipe_Map.sThermalFuels.addRecipe(
                true,
                null,
                outputItems,
                null,
                chances,
                new FluidStack[] {aInput1},
                new FluidStack[] {aOutput1},
                1, // 1 Tick
                0, // No Eu produced
                aSpecialValue // Magic Number
                );
    }

    public static void addNewHotFuel(FluidStack aInput1, FluidStack aOutput1, FluidStack aOutput2, int aSpecialValue) {
        GTPP_Recipe.GTPP_Recipe_Map.sThermalFuels.addRecipe(
                false,
                null,
                null,
                null,
                null,
                new FluidStack[] {aInput1},
                new FluidStack[] {aOutput1, aOutput2},
                20, // 1 Second
                0, // No Eu produced
                aSpecialValue // Magic Number
                );
    }
}
