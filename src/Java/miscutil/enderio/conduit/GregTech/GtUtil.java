package miscutil.enderio.conduit.GregTech;

import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.Loader;
import crazypants.enderio.conduit.IConduitBundle;
import crazypants.enderio.config.Config;
import crazypants.util.BlockCoord;

public final class GtUtil {

  private static boolean useCheckPerformed = false;
  private static boolean isGasConduitEnabled = false;

  public static boolean isGasConduitEnabled() {
    if (!useCheckPerformed) {
      String configOption = Config.isGasConduitEnabled;
      if (configOption.equalsIgnoreCase("auto")) {
        isGasConduitEnabled = Loader.isModLoaded("Mekanism");
        if (isGasConduitEnabled) {
          isGasConduitEnabled = Loader.instance().getIndexedModList().get("Mekanism").getVersion().startsWith("7");
        }
      } else if (configOption.equalsIgnoreCase("true")) {
        isGasConduitEnabled = true;
      } else {
        isGasConduitEnabled = false;
      }
      useCheckPerformed = true;
    }
    return isGasConduitEnabled;
  }

  public static IGasHandler getExternalGasHandler(IBlockAccess world, BlockCoord bc) {
    IGasHandler con = getGasHandler(world, bc);
    return (con != null && !(con instanceof IConduitBundle)) ? con : null;
  }

  public static IGasHandler getGasHandler(IBlockAccess world, BlockCoord bc) {
    return getGasHandler(world, bc.x, bc.y, bc.z);
  }

  public static IGasHandler getGasHandler(IBlockAccess world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(x, y, z);
    return getGasHandler(te);
  }

  public static IGasHandler getGasHandler(TileEntity te) {
    if(te instanceof IGasHandler) {
      return (IGasHandler) te;
    }
    return null;
  }

  public static boolean isGasValid(GasStack gas) {
    if(gas != null) {
      String name = gas.getGas().getLocalizedName();
      if(name != null && !name.trim().isEmpty()) {
        return true;
      }
    }
    return false;
  }

  private GtUtil() {
  }

}
