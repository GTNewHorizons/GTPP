package gtPlusPlus.xmod.bartworks;

import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import net.minecraft.item.ItemStack;

public class BW_Utils {

    private static final Method sName;
    private static final Method sGet;
    private static final Class<?> sClassBW_NonMeta_MaterialItems;
    private static final Class<?> Werkstoff;
    private static final Class<?> WerkstoffLoader;
    private static final Map<Short, ?> werkstoffHashMap;
    private static final Method getCorrespondingItemStackUnsafe;

    static {
        sName = ReflectionUtils.getMethod(Enum.class, "name");
        sClassBW_NonMeta_MaterialItems =
                ReflectionUtils.getClass("com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems");
        sGet = ReflectionUtils.getMethod(sClassBW_NonMeta_MaterialItems, "get", long.class, Object[].class);
        Werkstoff = ReflectionUtils.getClass("com.github.bartimaeusnek.bartworks.system.material.Werkstoff");
        WerkstoffLoader =
                ReflectionUtils.getClass("com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader");
        Field fieldWerkstoffHashMap = ReflectionUtils.getField(Werkstoff, "werkstoffHashMap");
        werkstoffHashMap = ReflectionUtils.getFieldValue(fieldWerkstoffHashMap);
        getCorrespondingItemStackUnsafe = ReflectionUtils.getMethod(
                WerkstoffLoader, "getCorrespondingItemStackUnsafe", OrePrefixes.class, Werkstoff, int.class);
    }

    public enum NonMeta_MaterialItem {
        Depleted_Tiberium_1,
        Depleted_Tiberium_2,
        Depleted_Tiberium_4,
        TiberiumCell_1,
        TiberiumCell_2,
        TiberiumCell_4,
        TheCoreCell,
        Depleted_TheCoreCell;
    }

    public static ItemStack getBW_NonMeta_MaterialItems(NonMeta_MaterialItem aItem, long aAmount) {
        if (sClassBW_NonMeta_MaterialItems != null && sClassBW_NonMeta_MaterialItems.isEnum()) {
            for (Object obj : sClassBW_NonMeta_MaterialItems.getEnumConstants()) {
                try {
                    if (aItem.name().equals(ReflectionUtils.invokeNonBool(obj, sName, new Object[] {}))) {
                        return ((ItemStack) ReflectionUtils.invokeNonBool(
                                        obj, sGet, new Object[] {aAmount, new Object[] {}}))
                                .copy();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return null;
    }

    public static ArrayList<ItemStack> getAll(int aStackSize) {
        ArrayList<ItemStack> aItems = new ArrayList<ItemStack>();
        aItems.add(getBW_NonMeta_MaterialItems(NonMeta_MaterialItem.TiberiumCell_1, aStackSize));
        aItems.add(getBW_NonMeta_MaterialItems(NonMeta_MaterialItem.TiberiumCell_2, aStackSize));
        aItems.add(getBW_NonMeta_MaterialItems(NonMeta_MaterialItem.TiberiumCell_4, aStackSize));
        aItems.add(getBW_NonMeta_MaterialItems(NonMeta_MaterialItem.TheCoreCell, aStackSize));
        return aItems;
    }

    public static ItemStack getCorrespondingItemStack(OrePrefixes orePrefixes, short werkstoffID, int amount) {
        if (LoadedMods.BartWorks) {
            if (werkstoffHashMap != null) {
                Object werkstoff = werkstoffHashMap.get(werkstoffID);
                if (werkstoff != null) {
                    return (ItemStack) ReflectionUtils.invokeNonBool(
                            null, getCorrespondingItemStackUnsafe, new Object[] {orePrefixes, werkstoff, amount});
                }
            }
        }
        return null;
    }
}
