/*
 *  Author: Henry Gu
 *  Teacher: Mr. Radulovich
 *  Date: 2019/6/18
 *  Course: ICS4U
 */

package game.client.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int texture;

    private static HashMap<File, Texture> textureCache = new HashMap<>();

    private Texture(int texture) {
        this.texture = texture;
    }

    public static Texture loadTexture(File file) throws IOException {
        Texture cached = textureCache.get(file);
        if (cached != null) {
            return cached;
        }

        System.out.println("Loading texture: " + file);
        BufferedImage bufimg = ImageIO.read(file);

        ByteBuffer buf = ByteBuffer.allocateDirect(bufimg.getWidth() * bufimg.getHeight() * 4);
        for (int i = 0; i < bufimg.getWidth(); i++) {
            for (int j = 0; j < bufimg.getHeight(); j++) {
                int rgb = bufimg.getRGB(i, j);
                buf.put((byte) ((rgb >> 16) & 0xff));
                buf.put((byte) ((rgb >> 8) & 0xff));
                buf.put((byte) ((rgb >> 0) & 0xff));
                buf.put((byte) 0xff);
            }
        }
        buf.flip();

        int[] textures = new int[1];
        glGenTextures(textures);

        glBindTexture(GL_TEXTURE_2D, textures[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bufimg.getWidth(), bufimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

        Texture texture = new Texture(textures[0]);

        textureCache.put(file, texture);

        return texture;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    @Override
    public void finalize() {
        glDeleteTextures(texture);
    }
}
