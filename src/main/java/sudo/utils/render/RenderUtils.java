package sudo.utils.render;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

public class RenderUtils {
    public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples) { 
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer(); 
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR); 
 
        double toX1 = toX - rad; 
        double toY1 = toY - rad; 
        double fromX1 = fromX + rad; 
        double fromY1 = fromY + rad; 
        double[][] map = new double[][]{new double[]{toX1, toY1}, new double[]{toX1, fromY1}, new double[]{fromX1, fromY1}, new double[]{fromX1, toY1}}; 
        for (int i = 0; i < 4; i++) { 
            double[] current = map[i]; 
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) { 
                float rad1 = (float) Math.toRadians(r); 
                float sin = (float) (Math.sin(rad1) * rad); 
                float cos = (float) (Math.cos(rad1) * rad); 
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next(); 
            } 
        } 
        BufferRenderer.drawWithShader(bufferBuilder.end()); 
    }
	
    public static void renderRoundedQuad(MatrixStack matrices, Color c, double fromX, double fromY, double toX, double toY, double rad, double samples) { 
        int color = c.getRGB(); 
        Matrix4f matrix = matrices.peek().getPositionMatrix(); 
        float f = (float) (color >> 24 & 255) / 255.0F; 
        float g = (float) (color >> 16 & 255) / 255.0F; 
        float h = (float) (color >> 8 & 255) / 255.0F; 
        float k = (float) (color & 255) / 255.0F; 
        RenderSystem.enableBlend(); 
        RenderSystem.disableTexture(); 
        RenderSystem.setShader(GameRenderer::getPositionColorShader); 
 
        renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples); 
 
        RenderSystem.enableTexture(); 
        RenderSystem.disableBlend(); 
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f); 
    }

    public static void setup2DRender(boolean disableDepth) {
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        if (disableDepth)
            RenderSystem.disableDepthTest();
    }

    public static void end2DRender() {
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }

    public static void fill(MatrixStack matrixStack, double x1, double y1, double x2, double y2, int color) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        double j;
        if (x1 < x2) {
            j = x1;
            x1 = x2;
            x2 = j;
        }

        if (y1 < y2) {
            j = y1;
            y1 = y2;
            y2 = j;
        }

        float f = (float)(color >> 24 & 255) / 255.0F;
        float g = (float)(color >> 16 & 255) / 255.0F;
        float h = (float)(color >> 8 & 255) / 255.0F;
        float k = (float)(color & 255) / 255.0F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(g, h, k, f).next();
        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g, h, k, f).next();
        bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(g, h, k, f).next();
        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g, h, k, f).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawOutlineCircle(MatrixStack matrices, double xx, double yy, double radius, Color color) {
        RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        RenderUtils.bindTexture(new Identifier("hypnotic", "textures/outlinecircle.png"));
        RenderSystem.enableBlend();
        RenderUtils.drawTexture(matrices, (float) xx,(float) yy, (float)radius, (float)radius, 0, 0, (float)radius,(float) radius,(float) radius,(float) radius);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void bindTexture(Identifier identifier) {
        RenderSystem.setShaderTexture(0, identifier);
    }

    public static void drawTexture(MatrixStack matrices, float x, float y, float u, float v, float width, float height, int textureWidth, int textureHeight) {
        RenderSystem.enableBlend();
        drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
        RenderSystem.disableBlend();
    }

    public static void drawTexture(MatrixStack matrices, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight) {
        drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(MatrixStack matrices, float x0, float y0, float x1, float y1, int z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight) {
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, y0, x1, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
    }

    public static void drawTexturedQuad(Matrix4f matrices, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrices, (float)x0, (float)y1, (float)z).texture(u0, v1).next();
        bufferBuilder.vertex(matrices, (float)x1, (float)y1, (float)z).texture(u1, v1).next();
        bufferBuilder.vertex(matrices, (float)x1, (float)y0, (float)z).texture(u1, v0).next();
        bufferBuilder.vertex(matrices, (float)x0, (float)y0, (float)z).texture(u0, v0).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
    }
}
