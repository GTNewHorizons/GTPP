package gtPlusPlus.xmod.bartcrops.abstracts;

import gtPlusPlus.preloader.CORE_Preloader;
import ic2.api.crops.ICropTile;

public abstract class BaseAestheticCrop extends BaseHarvestableCrop {

    public int tier() {
        return 1;
    }

    public int stat(int n) {
        switch (n) {
            case 3:
                return 4;
            case 4:
            case 2:
            case 1:
            case 0:
            default:
                return 0;
        }
    }

    public int growthDuration(ICropTile crop) {
        return CORE_Preloader.DEBUG_MODE ? 1 : 225;
    }

    public byte getSizeAfterHarvest(ICropTile crop) {
        return 1;
    }
}
