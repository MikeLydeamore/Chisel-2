package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAutoChisel extends BlockContainer
{
    public BlockAutoChisel()
    {
        super(Material.rock);
        setHardness(1F);
    }

    @Override
    public int getRenderType(){
        return -1;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int metadata, float x1, float y1, float z1){
        TileEntity tile = world.getTileEntity(x, y, z);
        if(world.isRemote)
            return true;
         if(tile !=null && tile instanceof TileEntityAutoChisel)
        	 player.openGui(Chisel.instance, 1, world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6){
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, block, par6);
    }

    private void dropItems(World world, int x, int y, int z){
        Random random = new Random();
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if(!(tileEntity instanceof IInventory)){
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for(int c = 0; c < inventory.getSizeInventory(); c++){
            ItemStack stack = inventory.getStackInSlot(c);

            if(stack != null && stack.stackSize > 0){
                float rx = random.nextFloat() * 0.8F + 0.1F;
                float ry = random.nextFloat() * 0.8F + 0.1F;
                float rz = random.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, stack);

                if(stack.hasTagCompound()){
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = random.nextGaussian() * factor;
                entityItem.motionY = random.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = random.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                stack.stackSize = 0;
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityAutoChisel();
    }
}
