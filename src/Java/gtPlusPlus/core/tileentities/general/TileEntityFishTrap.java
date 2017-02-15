package gtPlusPlus.core.tileentities.general;

import java.util.UUID;

import gtPlusPlus.core.inventories.*;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFishTrap extends TileEntity{

	private UUID ownerUUID;
	private int tickCount = 0;
	private boolean isInWater = false;
	private InventoryFishTrap inventoryContents;
	private int locationX;
	private int locationY;
	private int locationZ;

	public TileEntityFishTrap(){
		this.inventoryContents = new InventoryFishTrap();//number of slots - without product slot
		this.canUpdate();
		setTileLocation();
	}

	public boolean setTileLocation(){
		if (this.hasWorldObj()){
			if (!this.getWorldObj().isRemote){
				locationX = this.xCoord;
				locationY = this.yCoord;
				locationZ = this.zCoord;
				return true;
			}
		}
		return false;	
	}

	public final boolean isSurroundedByWater(){
		setTileLocation();
		Block[] surroundingBlocks = new Block[6];
		if (this.hasWorldObj()){
			//Utils.LOG_INFO("FT has world object");
			if (!this.getWorldObj().isRemote){
				//Utils.LOG_INFO("running code serverside");
				surroundingBlocks[0] = worldObj.getBlock(locationX, locationY+1, locationZ); //Above
				surroundingBlocks[1] = worldObj.getBlock(locationX, locationY-1, locationZ); //Below
				surroundingBlocks[2] = worldObj.getBlock(locationX+1, locationY, locationZ);
				surroundingBlocks[3] = worldObj.getBlock(locationX-1, locationY, locationZ);
				surroundingBlocks[4] = worldObj.getBlock(locationX, locationY, locationZ+1);
				surroundingBlocks[5] = worldObj.getBlock(locationX, locationY, locationZ-1);
				int waterCount = 0;
				for (Block checkBlock : surroundingBlocks){
					//Utils.LOG_INFO("found "+checkBlock.getLocalizedName());
					if (checkBlock == Blocks.water || checkBlock == Blocks.flowing_water || checkBlock.getUnlocalizedName().toLowerCase().contains("water")){
						waterCount++;
					}
				}
				if (waterCount >= 4){
					//Utils.LOG_INFO("found water");
					return true;
				}
			}
		}
		//Utils.LOG_INFO("Error finding water");
		return false;
	}

	public InventoryFishTrap getInventory(){
		return this.inventoryContents;
	}

	public boolean tryAddLoot(){
		Utils.LOG_INFO("1");
		if (this.getInventory().getInventory() != null){
			Utils.LOG_INFO("2");
			int checkingSlot = 0;
			ItemStack loot = generateLootForFishTrap();
			for (ItemStack contents : this.getInventory().getInventory()){
				Utils.LOG_INFO("checkingSlots");
				if (contents == loot){
					if (contents.stackSize < loot.getMaxStackSize()){
						contents.stackSize++;
					}
					else {
						this.getInventory().setInventorySlotContents(checkingSlot, loot);
					}
				}
				if (contents == null || contents != loot){
					Utils.LOG_INFO("3");
					this.getInventory().setInventorySlotContents(checkingSlot, loot);
					return true;
				}
				else {

				}
				checkingSlot++;
			}
		}

		return false;
	}

	private ItemStack generateLootForFishTrap() {
		int lootWeight = MathUtils.randInt(0,  100);
		ItemStack loot;
		if (lootWeight <= 10){
			loot = ItemUtils.getSimpleStack(Items.slime_ball);			
		}
		else if (lootWeight <= 20){
			loot = ItemUtils.getSimpleStack(Items.bone);
		}
		else if (lootWeight <= 40){
			loot = ItemUtils.getSimpleStack(Blocks.sand);
		}
		else if (lootWeight <= 80){
			loot = ItemUtils.getSimpleStack(Items.fish);
		}
		else if (lootWeight <= 100){
			loot = ItemUtils.getSimpleStack(Items.fish);
		}
		else {
			loot = ItemUtils.getSimpleStack(Blocks.diamond_ore);
		}
		return loot;
	}

	@Override
	public void updateEntity(){
		if (!this.worldObj.isRemote){
			this.tickCount++;
			//Utils.LOG_INFO("Ticking "+this.tickCount);
			//Check if the Tile is within water once per second.
			if (this.tickCount%20==0){					
				this.isInWater = isSurroundedByWater();
				Utils.LOG_INFO("In water? "+this.isInWater+" tick:"+this.tickCount);
			}
			else {

			}
			//Try add some loot once every 30 seconds.
			if (this.tickCount%300==0){	
				if (this.isInWater){
					//Add loot
					Utils.LOG_INFO("Adding Loot to the fishtrap at x["+this.locationX+"] y["+this.locationY+"] z["+this.locationZ+"]");
					tryAddLoot();
				}				
				this.tickCount = 0;
			}
			if (this.tickCount > 1000){
				this.tickCount = 0;
			}


		}
	}

	public boolean anyPlayerInRange(){
		return this.worldObj.getClosestPlayer(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, 32) != null;
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public void setOwnerUUID(UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
		markDirty();
	}

	public NBTTagCompound getTag(NBTTagCompound nbt, String tag){
		if(!nbt.hasKey(tag)){
			nbt.setTag(tag, new NBTTagCompound());
		}
		return nbt.getCompoundTag(tag);
	}


	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		inventoryContents.writeToNBT(getTag(tagCompound, "ContentsChest"));
		UUID ownerUUID = getOwnerUUID();
		if (ownerUUID != null){
			tagCompound.setLong("OwnerUUIDMost", ownerUUID.getMostSignificantBits());
			tagCompound.setLong("OwnerUUIDLeast", ownerUUID.getLeastSignificantBits());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		inventoryContents.readFromNBT(tagCompound.getCompoundTag("ContentsChest"));
		setOwnerUUID(new UUID(tagCompound.getLong("OwnerUUIDMost"), tagCompound.getLong("OwnerUUIDLeast")));
	}

}
