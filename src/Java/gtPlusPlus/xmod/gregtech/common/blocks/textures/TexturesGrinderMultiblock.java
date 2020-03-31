package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks5;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IsaMill;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class TexturesGrinderMultiblock {

	private static CustomIcon GT8_1_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE1");
	private static CustomIcon GT8_1 = new CustomIcon("iconsets/Grinder/GRINDER1");
	private static CustomIcon GT8_2_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE2");
	private static CustomIcon GT8_2 = new CustomIcon("iconsets/Grinder/GRINDER2");
	private static CustomIcon GT8_3_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE3");
	private static CustomIcon GT8_3 = new CustomIcon("iconsets/Grinder/GRINDER3");
	private static CustomIcon GT8_4_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE4");
	private static CustomIcon GT8_4 = new CustomIcon("iconsets/Grinder/GRINDER4");
	private static CustomIcon GT8_5_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE5");
	private static CustomIcon GT8_5 = new CustomIcon("iconsets/Grinder/GRINDER5");
	private static CustomIcon GT8_6_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE6");
	private static CustomIcon GT8_6 = new CustomIcon("iconsets/Grinder/GRINDER6");
	private static CustomIcon GT8_7_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE7");
	private static CustomIcon GT8_7 = new CustomIcon("iconsets/Grinder/GRINDER7");
	private static CustomIcon GT8_8_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE8");
	private static CustomIcon GT8_8 = new CustomIcon("iconsets/Grinder/GRINDER8");
	private static CustomIcon GT8_9_Active = new CustomIcon("iconsets/Grinder/GRINDER_ACTIVE9");
	private static CustomIcon GT8_9 = new CustomIcon("iconsets/Grinder/GRINDER9");

	private static CustomIcon frontFace_0 = (GT8_1);
	private static CustomIcon frontFaceActive_0 = (GT8_1_Active);
	private static CustomIcon frontFace_1 = (GT8_2);
	private static CustomIcon frontFaceActive_1 = (GT8_2_Active);
	private static CustomIcon frontFace_2 = (GT8_3);
	private static CustomIcon frontFaceActive_2 = (GT8_3_Active);
	private static CustomIcon frontFace_3 = (GT8_4);
	private static CustomIcon frontFaceActive_3 = (GT8_4_Active);
	private static CustomIcon frontFace_4 = (GT8_5);
	private static CustomIcon frontFaceActive_4 = (GT8_5_Active);
	private static CustomIcon frontFace_5 = (GT8_6);
	private static CustomIcon frontFaceActive_5 = (GT8_6_Active);
	private static CustomIcon frontFace_6 = (GT8_7);
	private static CustomIcon frontFaceActive_6 = (GT8_7_Active);
	private static CustomIcon frontFace_7 = (GT8_8);
	private static CustomIcon frontFaceActive_7 = (GT8_8_Active);
	private static CustomIcon frontFace_8 = (GT8_9);
	private static CustomIcon frontFaceActive_8 = (GT8_9_Active);

	CustomIcon[] GRINDER = new CustomIcon[]{
			frontFace_0,
			frontFace_1,
			frontFace_2,
			frontFace_3,
			frontFace_4,
			frontFace_5,
			frontFace_6,
			frontFace_7,
			frontFace_8
	};

	CustomIcon[] GRINDER_ACTIVE = new CustomIcon[]{
			frontFaceActive_0,
			frontFaceActive_1,
			frontFaceActive_2,
			frontFaceActive_3,
			frontFaceActive_4,
			frontFaceActive_5,
			frontFaceActive_6,
			frontFaceActive_7,
			frontFaceActive_8
	};

	public IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord, final int aSide, final GregtechMetaCasingBlocks5 i) {
		final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
		if (tMeta != 0) {
			return GregtechMetaCasingBlocks5.getStaticIcon(aSide, tMeta);
		}
		final int tStartIndex = tMeta == 6 ? 1 : 13;
		if (tMeta == 0) {
			if ((aSide == 2) || (aSide == 3)) {
				TileEntity tTileEntity;
				IMetaTileEntity tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex(tMetaTileEntity, 0);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex(tMetaTileEntity, 3);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 3 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex(tMetaTileEntity, 6);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex(tMetaTileEntity, 1);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex(tMetaTileEntity, 7);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex(tMetaTileEntity, 8);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex(tMetaTileEntity, 5);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord + (aSide == 2 ? 1 : -1), yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex(tMetaTileEntity, 2);
				}
			} else if ((aSide == 4) || (aSide == 5)) {
				TileEntity tTileEntity;
				Object tMetaTileEntity;
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex((IMetaTileEntity) tMetaTileEntity, 0);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex((IMetaTileEntity) tMetaTileEntity, 3);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 4 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex((IMetaTileEntity) tMetaTileEntity, 6);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex((IMetaTileEntity) tMetaTileEntity, 1);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex((IMetaTileEntity) tMetaTileEntity, 7);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord + 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex((IMetaTileEntity) tMetaTileEntity, 8);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex((IMetaTileEntity) tMetaTileEntity, 5);
				}
				if ((null != (tTileEntity = aWorld.getTileEntity(xCoord, yCoord - 1, zCoord + (aSide == 5 ? 1 : -1)))) && ((tTileEntity instanceof IGregTechTileEntity)) && (((IGregTechTileEntity) tTileEntity).getFrontFacing() == aSide) && (null != (tMetaTileEntity = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())) && ((tMetaTileEntity instanceof GregtechMetaTileEntity_IsaMill))) {
					return getIconByIndex((IMetaTileEntity) tMetaTileEntity, 2);
				}
			}
			return TexturesGtBlock.TEXTURE_CASING_GRINDING_MILL.getIcon();
		}
		final boolean[] tConnectedSides = {(aWorld.getBlock(xCoord, yCoord - 1, zCoord) == i) && (aWorld.getBlockMetadata(xCoord, yCoord - 1, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord + 1, zCoord) == i) && (aWorld.getBlockMetadata(xCoord, yCoord + 1, zCoord) == tMeta), (aWorld.getBlock(xCoord + 1, yCoord, zCoord) == i) && (aWorld.getBlockMetadata(xCoord + 1, yCoord, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord, zCoord + 1) == i) && (aWorld.getBlockMetadata(xCoord, yCoord, zCoord + 1) == tMeta), (aWorld.getBlock(xCoord - 1, yCoord, zCoord) == i) && (aWorld.getBlockMetadata(xCoord - 1, yCoord, zCoord) == tMeta), (aWorld.getBlock(xCoord, yCoord, zCoord - 1) == i) && (aWorld.getBlockMetadata(xCoord, yCoord, zCoord - 1) == tMeta)};
		switch (aSide) {
		case 0:
			if (tConnectedSides[0]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[4]) && (!tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (!tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((tConnectedSides[4]) && (!tConnectedSides[5]) && (!tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (!tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((!tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[5]) && (!tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[2])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			if ((!tConnectedSides[5]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
		case 1:
			if (tConnectedSides[1]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[4]) && (!tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (!tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[5]) && (tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((tConnectedSides[4]) && (!tConnectedSides[5]) && (!tConnectedSides[2]) && (tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((tConnectedSides[4]) && (tConnectedSides[5]) && (!tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((!tConnectedSides[4]) && (tConnectedSides[5]) && (tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((!tConnectedSides[4]) && (!tConnectedSides[5]) && (!tConnectedSides[2]) && (!tConnectedSides[3])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[4])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			if ((!tConnectedSides[3]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
		case 2:
			if (tConnectedSides[5]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[2]) && (!tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (!tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((tConnectedSides[2]) && (!tConnectedSides[0]) && (!tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (!tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((!tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[0]) && (!tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[4])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
		case 3:
			if (tConnectedSides[3]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[2]) && (!tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (!tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[0]) && (tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((tConnectedSides[2]) && (!tConnectedSides[0]) && (!tConnectedSides[4]) && (tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((tConnectedSides[2]) && (tConnectedSides[0]) && (!tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((!tConnectedSides[2]) && (tConnectedSides[0]) && (tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[0]) && (!tConnectedSides[4]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[2]) && (!tConnectedSides[4])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
		case 4:
			if (tConnectedSides[4]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((tConnectedSides[0]) && (!tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (!tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((tConnectedSides[0]) && (!tConnectedSides[3]) && (!tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (!tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((!tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[3]) && (!tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
			if ((!tConnectedSides[3]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
		case 5:
			if (tConnectedSides[2]) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 6)].getIcon();
			}
			if ((!tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 5)].getIcon();
			}
			if ((tConnectedSides[0]) && (!tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 2)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (!tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 3)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 4)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[3]) && (tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 11)].getIcon();
			}
			if ((tConnectedSides[0]) && (!tConnectedSides[3]) && (!tConnectedSides[1]) && (tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 8)].getIcon();
			}
			if ((tConnectedSides[0]) && (tConnectedSides[3]) && (!tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 9)].getIcon();
			}
			if ((!tConnectedSides[0]) && (tConnectedSides[3]) && (tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 10)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[3]) && (!tConnectedSides[1]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
			}
			if ((!tConnectedSides[0]) && (!tConnectedSides[1])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 0)].getIcon();
			}
			if ((!tConnectedSides[3]) && (!tConnectedSides[5])) {
				return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 1)].getIcon();
			}
			break;
		}
		return Textures.BlockIcons.CONNECTED_HULLS[(tStartIndex + 7)].getIcon();
	}
	
	public boolean isCentrifugeRunning(IMetaTileEntity aTile) {
		if (aTile == null) {
			return false;
		}
		else {
			return aTile.getBaseMetaTileEntity().isActive();
		}
		
		
	}
	
	public boolean isUsingAnimatedTexture(IMetaTileEntity aMetaTileEntity) {
		return true;
	}

	public IIcon getIconByIndex(IMetaTileEntity aMetaTileEntity, int aIndex) {
			if (isCentrifugeRunning(aMetaTileEntity)) {
				return this.GRINDER_ACTIVE[aIndex].getIcon();				
			}
		
		return this.GRINDER[aIndex].getIcon();
	}
	
}
