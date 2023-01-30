package gtPlusPlus.core.item.effects;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RarityUncommon extends Item {

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack par1ItemStack) {
        return EnumRarity.uncommon;
    }

    @Override
    public boolean hasEffect(final ItemStack par1ItemStack) {
        return true;
    }
}
