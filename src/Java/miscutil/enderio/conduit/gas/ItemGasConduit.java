package crazypants.enderio.conduit.gas;

import com.enderio.core.api.client.gui.IAdvancedTooltipProvider;
import com.enderio.core.client.handlers.SpecialTooltipHandler;
import com.enderio.core.common.Lang;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.EnderIO;
import crazypants.enderio.ModObject;
import crazypants.enderio.conduit.AbstractItemConduit;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.ItemConduitSubtype;
import crazypants.enderio.config.Config;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemGasConduit
  extends AbstractItemConduit
  implements IAdvancedTooltipProvider
{
  private static ItemConduitSubtype[] subtypes = { new ItemConduitSubtype(ModObject.itemGasConduit
    .name(), "enderio:itemGasConduit") };
  
  public static ItemGasConduit create()
  {
    ItemGasConduit result = new ItemGasConduit();
    if (GasUtil.isGasConduitEnabled()) {
      result.init();
    }
    return result;
  }
  
  protected ItemGasConduit()
  {
    super(ModObject.itemGasConduit, subtypes);
    if (!GasUtil.isGasConduitEnabled()) {
      setCreativeTab(null);
    }
  }
  
  public Class<? extends IConduit> getBaseConduitType()
  {
    return IGasConduit.class;
  }
  
  public IConduit createConduit(ItemStack stack, EntityPlayer player)
  {
    return new GasConduit();
  }
  
  @SideOnly(Side.CLIENT)
  public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {}
  
  @SideOnly(Side.CLIENT)
  public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {}
  
  @SideOnly(Side.CLIENT)
  public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag)
  {
    String gpt = " " + EnderIO.lang.localize("gas.gasTick");
    int extractRate = Config.gasConduitExtractRate;
    int maxIo = Config.gasConduitMaxIoRate;
    list.add(EnderIO.lang.localize("itemGasConduit.tooltip.maxExtract") + " " + extractRate + gpt);
    list.add(EnderIO.lang.localize("itemGasConduit.tooltip.maxIo") + " " + maxIo + gpt);
    SpecialTooltipHandler.addDetailedTooltipFromResources(list, "enderio.itemGasConduit");
  }
  
  public boolean shouldHideFacades(ItemStack stack, EntityPlayer player)
  {
    return true;
  }
}
