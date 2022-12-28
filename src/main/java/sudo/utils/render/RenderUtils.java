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
}
