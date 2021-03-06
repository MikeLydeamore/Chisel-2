package info.jbcs.minecraft.chisel.client.render.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import info.jbcs.minecraft.chisel.client.model.ModelAutoChisel;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class RenderAutoChisel extends TileEntitySpecialRenderer {

    private static EntityItem ghostItem;
    private static TileEntityAutoChisel autoChisel;
    private final ModelAutoChisel model;
    private final ResourceLocation texture = new ResourceLocation("chisel:textures/blocks/autoChisel.png");
    private final RenderItem renderTargetAndChisel, renderBase;

    public RenderAutoChisel(){
        model = new ModelAutoChisel();
        renderTargetAndChisel = new RenderItem(){
            @Override
            public boolean shouldBob(){
                return false;
            }
        };
        renderTargetAndChisel.setRenderManager(RenderManager.instance);

        renderBase = new RenderItem(){
            @Override
            public boolean shouldBob(){
                return true;
            }
        };

        renderBase.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float scale) {
        bindTexture(texture);
        glPushMatrix();
        glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        glRotatef(180F, 0.0F, 0.0F, 1.0F);
        glScalef(0.89F, 1.0F, 0.89F);
        this.model.renderAll();
        glPopMatrix();

        //Render Blocks
        autoChisel = (TileEntityAutoChisel) tile;
        EntityItem item = autoChisel.getItemForRendering(1);

        if(item != null){
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            GL11.glScaled(3, 3, 3);
            renderTargetAndChisel.doRender(item, 0, 0, 0, 0, 0);
            GL11.glPopMatrix();
        }

        EntityItem item2 = autoChisel.getItemForRendering(0);

        if(item2 != null){
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
            GL11.glScaled(1, 1, 1);
            renderBase.doRender(item2, 0, 0, 0, 0, 0);
            GL11.glPopMatrix();
        }

        /*EntityItem chisel = getGhostItem(autoChisel.getWorldObj(), new ItemStack(ModItems.chisel));

        if(chisel != null){
            GL11.glPushMatrix();
            GL11.glTranslated(x + 1.5, y + 1.5, z + 1.5);
            GL11.glScaled(1, 1, 1);
            renderTargetAndChisel.doRender(chisel, 0, 0, 0, 0, 0);
            GL11.glPopMatrix();
        }*/
    }

    @SideOnly(Side.CLIENT)
    public EntityItem getGhostItem(World world, ItemStack itemStack){
        if(ghostItem == null){
            ghostItem = new EntityItem(world);
            ghostItem.hoverStart = 0.0F;
        }

        if(itemStack == null){
            return null;
        } else {
            ghostItem.setEntityItemStack(itemStack);
            return ghostItem;
        }
    }


}
