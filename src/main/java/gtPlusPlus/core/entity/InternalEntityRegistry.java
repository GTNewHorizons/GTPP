package gtPlusPlus.core.entity;

import cpw.mods.fml.common.registry.EntityRegistry;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.entity.type.EntityAustralianSpiderBase;
import gtPlusPlus.australia.entity.type.EntityBoar;
import gtPlusPlus.australia.entity.type.EntityDingo;
import gtPlusPlus.australia.entity.type.EntityOctopus;
import gtPlusPlus.core.entity.monster.EntityBatKing;
import gtPlusPlus.core.entity.monster.EntityGiantChickenBase;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityHydrofluoricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntityLightningAttack;
import gtPlusPlus.core.entity.projectile.EntitySulfuricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntityThrowableBomb;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import gtPlusPlus.core.item.general.spawn.ItemCustomSpawnEgg;
import gtPlusPlus.core.util.Utils;

public class InternalEntityRegistry {

	static int mEntityID = 0;
	
	public static void registerEntities(){	
		Logger.INFO("Registering GT++ Entities.");
		
        //EntityRegistry.registerGlobalEntityID(EntityPrimedMiningExplosive.class, "MiningCharge", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 0, 0), Utils.rgbtoHexValue(125, 125, 125));
        EntityRegistry.registerModEntity(EntityPrimedMiningExplosive.class, "MiningCharge", mEntityID++, GTplusplus.instance, 64, 20, true);

        //EntityRegistry.registerGlobalEntityID(EntitySulfuricAcidPotion.class, "throwablePotionSulfuric", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(200, 0, 200), Utils.rgbtoHexValue(125, 125, 125));
        EntityRegistry.registerModEntity(EntitySulfuricAcidPotion.class, "throwablePotionSulfuric", mEntityID++, GTplusplus.instance, 64, 20, true);
       

        //EntityRegistry.registerGlobalEntityID(EntityHydrofluoricAcidPotion.class, "throwablePotionHydrofluoric", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 0, 0), Utils.rgbtoHexValue(255, 255, 255));
        EntityRegistry.registerModEntity(EntityHydrofluoricAcidPotion.class, "throwablePotionHydrofluoric", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        //EntityRegistry.registerGlobalEntityID(EntityToxinballSmall.class, "toxinBall", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 25, 0), Utils.rgbtoHexValue(0, 125, 0));
        EntityRegistry.registerModEntity(EntityToxinballSmall.class, "toxinBall", mEntityID++, GTplusplus.instance, 64, 20, true);
        

        //EntityRegistry.registerGlobalEntityID(EntityStaballoyConstruct.class, "constructStaballoy", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 75, 0), Utils.rgbtoHexValue(50, 220, 50));
        EntityRegistry.registerModEntity(EntityStaballoyConstruct.class, "constructStaballoy", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(0, "constructStaballoy", Utils.rgbtoHexValue(20, 200, 20), Utils.rgbtoHexValue(20, 20, 20));

        //EntityRegistry.registerGlobalEntityID(EntitySickBlaze.class, "sickBlaze", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(0, 75, 0), Utils.rgbtoHexValue(75, 175, 75));
        EntityRegistry.registerModEntity(EntitySickBlaze.class, "sickBlaze", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(1, "sickBlaze", Utils.rgbtoHexValue(40, 180, 40), Utils.rgbtoHexValue(75, 75, 75));

        //EntityRegistry.registerGlobalEntityID(EntityTeslaTowerLightning.class, "plasmaBolt", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(125, 125, 125));
        EntityRegistry.registerModEntity(EntityTeslaTowerLightning.class, "plasmaBolt", mEntityID++, GTplusplus.instance, 64, 5, true);
        
        EntityRegistry.registerModEntity(EntityThrowableBomb.class, "EntityThrowableBomb", mEntityID++, GTplusplus.instance, 64, 10, true);
        
        EntityRegistry.registerModEntity(EntityLightningAttack.class, "EntityLightningAttack", mEntityID++, GTplusplus.instance, 64, 20, true);
        
        /**
         * Globals, which generate spawn eggs. (Currently required for Giant chicken spawning)
         */
        
        EntityRegistry.registerModEntity(EntityGiantChickenBase.class, "bigChickenFriendly", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(2, "bigChickenFriendly", Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        EntityRegistry.registerModEntity(EntityBatKing.class, "batKing", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(3, "batKing", Utils.rgbtoHexValue(175, 175, 0), Utils.rgbtoHexValue(0, 175, 175));
        //EntityRegistry.registerModEntity(EntityGiantChickenBase.class, "bigChickenFriendly", mEntityID++, GTplusplus.instance, 64, 20, true);
        
        
      
        
        
        //Australia
        EntityRegistry.registerModEntity(EntityAustralianSpiderBase.class, "AusSpider", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(30, "AusSpider", Utils.rgbtoHexValue(125, 0, 125), Utils.rgbtoHexValue(175, 175, 175));
        EntityRegistry.registerModEntity(EntityBoar.class, "AusBoar", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(31, "AusBoar", Utils.rgbtoHexValue(75, 75, 0), Utils.rgbtoHexValue(175, 175, 75));
        EntityRegistry.registerModEntity(EntityDingo.class, "AusDingo", mEntityID++, GTplusplus.instance, 64, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(32, "AusDingo", Utils.rgbtoHexValue(175, 125, 0), Utils.rgbtoHexValue(175, 75, 175));
        EntityRegistry.registerModEntity(EntityOctopus.class, "AusOctopus", mEntityID++, GTplusplus.instance, 32, 20, true);
        ItemCustomSpawnEgg.registerEntityForSpawnEgg(33, "AusOctopus", Utils.rgbtoHexValue(150, 50, 150), Utils.rgbtoHexValue(75, 175, 175));
        //EntityRegistry.registerGlobalEntityID(EntityAustralianSpiderBase.class, "AusSpider", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        //EntityRegistry.registerGlobalEntityID(EntityBoar.class, "AusBoar", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        //EntityRegistry.registerGlobalEntityID(EntityDingo.class, "AusDingo", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        //EntityRegistry.registerGlobalEntityID(EntityOctopus.class, "AusOctopus", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(255, 0, 0), Utils.rgbtoHexValue(175, 175, 175));
        
        
        
        
        
        
       
	}
	
}
