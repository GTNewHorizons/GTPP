package gtPlusPlus.xmod.bartworks;

import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;

public class BW_RadHatchMaterial {

    public static void run() {

        ItemStack err = ItemUtils.getErrorStack(1);

        for (Material material : Material.mMaterialMap) {
            if (material != null && material.vRadiationLevel > 0) {
                int level = (int) material.getProtons();
                short[] rgba = material.getRGBA();
                if (material.getRod(1) != null && !material.getRod(1).isItemEqual(err)) {
                    BWRecipes.instance.addRadHatch(material.getRod(1), level, 1, rgba);
                }
                if (material.getLongRod(1) != null && !material.getLongRod(1).isItemEqual(err)) {
                    BWRecipes.instance.addRadHatch(material.getLongRod(1), level, 2, rgba);
                }
            }
        }
    }
}
