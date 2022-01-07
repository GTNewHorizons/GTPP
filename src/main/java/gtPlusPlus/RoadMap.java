package gtPlusPlus;

import java.util.LinkedHashMap;

import com.google.common.base.Objects;

import gtPlusPlus.api.objects.data.ObjMap;
import net.minecraft.item.ItemStack;

/**
 * This Class purely exists to note down ideas and or plans to (re)implement things.
 * 
 * @author Alkalus
 *
 */
public class RoadMap {

	//Reorganization of Item, Block and Common Class loading.
	/*
	 * So, due to the complex/silly way I've done things, I've ran into some circular loading problems around the mod.
	 * Issues occur where Classes like CI.java try access the GregtechItemList.java objects before they're actually set.
	 * A plan should be created to organize the best scheme to load things in the best order.
	 */

	//Recreation of GUIs for all Multiblocks
	/*
	 * Most Multi's use generic or straight out wrong GUI's on the controller.
	 * I'd like to go back and recreate all of these.
	 * 
	 * Some could even benefit from a totally new type of UI (Instead of Text issues, just change a 2x2px area between red and green for status lights)
	 * These advanced GUIs are probably out of my capability, but if anyone thinks they're a good idea, I'll give them a go.
	 */

	//Better Integration with GTNH
	/*
	 * Refactor things to be more common, refactor things to automatically switch between GTNH and standard variants 
	 * without having to over-abuse CORE.GTNH switches everywhere.
	 * Most of this can be done via expanding CI.java, so that we have automated handlers for everything 
	 * (IE. getX(5) will get 5x of the correct version of X)
	 */


	/*
	 Thallium - Everglades only. (Mostly useless)
	Technetium - Has some recipes but seem to be dead ends so unobtainable.
	Polonium - Unobtainable. (Radioactive, Mostly Useless)
	Francium - Unobtainable. (Radioactive, Mostly Useless)
	Promethium - Unobtainable.
	Radium - Everglades only. (Do use, Moderately Rare)
	Actinium - Unobtainable. (Radioactive, Do use)
	Proactinium - Unobtainable. (Do use)
	Neptunium - Has some recipes but seem to be dead ends so unobtainable.
	Curium - Fusion only.
	Berkelium - Unobtainable.
	Californium - Fusion only.
	Einsteinium - Unobtainable.
	Fermium - Unobtainable.
	Strontium - Everglades only. (Do use 1)
	Iodine - Everglades only.
	Dysprosium - Everglades only. (Do use 1)
	Rhenium - Weird ABS recipe only.
	Flerovium - Unobtainable.
	Dubnium - Unobtainable.
	Seaborgium - Unobtainable.
	Bohrium - Unobtainable.
	Hassium - Unobtainable.
	Meitnerium - Unobtainable.
	Darmstadtium - Unobtainable.
	Roentgenium - Unobtainable.
	Copernicium - Unobtainable.
	Nihonium - Unobtainable.
	Moscovium - Unobtainable.
	Livermorium - Unobtainable.
	Tennessine - Unobtainable.
	Nobelium - Unobtainable.
	Lawrencium - Unobtainable.
	Astatine - Unobtainable. (Radioactive, Mostly Useless)
	 */

}
