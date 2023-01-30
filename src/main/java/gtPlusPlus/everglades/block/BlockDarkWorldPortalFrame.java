package gtPlusPlus.everglades.block;

import gtPlusPlus.api.interfaces.ITileTooltip;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;

public class BlockDarkWorldPortalFrame extends Block implements ITileTooltip {

    public BlockDarkWorldPortalFrame() {
        super(Material.iron);
        this.setCreativeTab(AddToCreativeTab.tabBOP);
        this.setBlockName("blockDarkWorldPortalFrame");
        this.setHardness(3.0F);
        this.setLightLevel(0.5F);
        this.setBlockTextureName(CORE.MODID + ":" + "metro/TEXTURE_TECH_PANEL_A");
    }

    @Override
    public int getTooltipID() {
        return 0;
    }
}
