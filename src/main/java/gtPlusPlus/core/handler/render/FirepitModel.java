// Date: 20/12/2016 5:51:14 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package gtPlusPlus.core.handler.render;

import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class FirepitModel extends ModelBase {
    // fields
    final ModelRenderer Side_A;
    final ModelRenderer Side_B;
    final ModelRenderer Side_C;
    final ModelRenderer Side_D;
    final ModelRenderer Log1;
    final ModelRenderer Log2;
    final ModelRenderer Log3;
    final ModelRenderer Log4;

    public FirepitModel() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.Side_A = new ModelRenderer(this, 0, 0);
        this.Side_A.addBox(0F, 0F, 0F, 12, 6, 1);
        this.Side_A.setRotationPoint(-6F, 18F, -6F);
        this.Side_A.setTextureSize(16, 16);
        this.Side_A.mirror = true;
        setRotation(this.Side_A, -0.3148822F, 0F, 0F);
        // Side_A.mirror = false;

        this.Side_B = new ModelRenderer(this, 0, 0);
        this.Side_B.addBox(0F, 0F, 0F, 12, 6, 1);
        this.Side_B.setRotationPoint(-6F, 18F, 6F);
        this.Side_B.setTextureSize(16, 16);
        this.Side_B.mirror = true;
        setRotation(this.Side_B, -0.3148822F, (CORE.PI / 2), 0F);

        this.Side_C = new ModelRenderer(this, 0, 0);
        this.Side_C.addBox(0F, 0F, 0F, 12, 6, 1);
        this.Side_C.setRotationPoint(6F, 18F, 6F);
        this.Side_C.setTextureSize(16, 16);
        this.Side_C.mirror = true;
        setRotation(this.Side_C, -0.3148822F, CORE.PI, 0F);
        // Side_C.mirror = false;

        this.Side_D = new ModelRenderer(this, 0, 0);
        this.Side_D.addBox(0F, 0F, 0F, 12, 6, 1);
        this.Side_D.setRotationPoint(6F, 18F, -6F);
        this.Side_D.setTextureSize(16, 16);
        this.Side_D.mirror = true;
        setRotation(this.Side_D, -0.3148822F, 4.712389F, 0F);

        this.Log1 = new ModelRenderer(this, 0, 10);
        this.Log1.addBox(0F, 0F, 0F, 14, 2, 2);
        this.Log1.setRotationPoint(4F, 10F, -4F);
        this.Log1.setTextureSize(16, 16);
        this.Log1.mirror = true;
        setRotation(this.Log1, 0F, 0F, (CORE.PI / 2));

        this.Log2 = new ModelRenderer(this, -2, 10);
        this.Log2.addBox(0F, 0F, 0F, 14, 2, 2);
        this.Log2.setRotationPoint(-4F, 10F, -4F);
        this.Log2.setTextureSize(16, 16);
        this.Log2.mirror = true;
        setRotation(this.Log2, (CORE.PI / 2), 0F, (CORE.PI / 2));
        // Log2.mirror = false;

        this.Log3 = new ModelRenderer(this, 0, 10);
        this.Log3.addBox(0F, 0F, 0F, 14, 2, 2);
        this.Log3.setRotationPoint(-4F, 10F, 4F);
        this.Log3.setTextureSize(16, 16);
        this.Log3.mirror = true;
        setRotation(this.Log3, CORE.PI, 0F, (CORE.PI / 2));

        this.Log4 = new ModelRenderer(this, -2, 10);
        this.Log4.addBox(0F, 0F, 0F, 14, 2, 2);
        this.Log4.setRotationPoint(4F, 10F, 4F);
        this.Log4.setTextureSize(16, 16);
        this.Log4.mirror = true;
        setRotation(this.Log4, 4.712389F, 0F, (CORE.PI / 2));
        // Log4.mirror = false;
    }

    @Override
    public void render(
            final Entity entity,
            final float f,
            final float f1,
            final float f2,
            final float f3,
            final float f4,
            final float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Side_A.render(f5);
        this.Side_B.render(f5);
        this.Side_C.render(f5);
        this.Side_D.render(f5);
        this.Log1.render(f5);
        this.Log2.render(f5);
        this.Log3.render(f5);
        this.Log4.render(f5);
    }

    private static void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(
            final float f,
            final float f1,
            final float f2,
            final float f3,
            final float f4,
            final float f5,
            final Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
