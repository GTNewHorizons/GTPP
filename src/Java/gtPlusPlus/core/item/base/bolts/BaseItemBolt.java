package gtPlusPlus.core.item.base.bolts;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemBolt extends Item{

	final Material boltMaterial;
	final String materialName;
	final String unlocalName;

	public BaseItemBolt(Material material) {
		this.boltMaterial = material;
		this.unlocalName = "itemBolt"+material.getUnlocalizedName();
		this.materialName = material.getLocalizedName();
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(64);
		this.setTextureName(CORE.MODID + ":" + "itemBolt");
		GameRegistry.registerItem(this, unlocalName);
		GT_OreDictUnificator.registerOre(unlocalName.replace("itemB", "b"), UtilsItems.getSimpleStack(this));
		addExtruderRecipe();
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (materialName+ " Bolt");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (materialName != null && materialName != "" && !materialName.equals("")){
			list.add(EnumChatFormatting.GRAY+"A small Bolt, constructed from " + materialName + ".");		
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (boltMaterial.getRgbAsHex() == 0){
			return MathUtils.generateSingularRandomHexValue();
		}
		return boltMaterial.getRgbAsHex();

	}

	private void addExtruderRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Bolts");
		String tempIngot = unlocalName.replace("itemBolt", "ingot");
		ItemStack tempOutputStack = UtilsItems.getItemStackOfAmountFromOreDict(tempIngot, 1);
		if (null != tempOutputStack){
			GT_Values.RA.addExtruderRecipe(tempOutputStack, 
					ItemList.Shape_Extruder_Bolt.get(1), 
					UtilsItems.getSimpleStack(this, 8),
					(int) Math.max(boltMaterial.getMass() * 2L * 1, 1),
					8 * boltMaterial.vVoltageMultiplier);	
		}	

	}

}
