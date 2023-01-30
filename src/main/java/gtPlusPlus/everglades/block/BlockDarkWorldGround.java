package gtPlusPlus.everglades.block;

import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class BlockDarkWorldGround extends BlockGrass implements ITileTooltip {

    public BlockDarkWorldGround() {
        this.setCreativeTab(AddToCreativeTab.tabBOP);
        this.setBlockName("blockDarkWorldGround");
        this.setHardness(1.0F);
        this.setBlockTextureName("minecraft" + ":" + "grass");
    }

    @Override
    public int getTooltipID() {
        return 2;
    }
}
