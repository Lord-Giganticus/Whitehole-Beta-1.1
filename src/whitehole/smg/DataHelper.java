package whitehole.smg;

import java.io.IOException;
import whitehole.fileio.FileBase;

public class DataHelper {
  public static byte[][] decodeTextureData(FileBase file, long offset, int mipmaps, int format, int width, int height) throws IOException {
    byte[][] ret = new byte[mipmaps][];
    file.position(offset);
    for (int mip = 0; mip < mipmaps; mip++) {
      int by, i;
      byte[] image = null;
      switch (format) {
        case 0:
          image = new byte[width * height];
          for (by = 0; by < height; by += 8) {
            for (int bx = 0; bx < width; bx += 8) {
              for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x += 2) {
                  int b = file.readByte() & 0xFF;
                  int outp = (by + y) * width + bx + x;
                  image[outp++] = (byte)(b & 0xF0 | b >>> 4);
                  image[outp] = (byte)(b << 4 | b & 0xF);
                } 
              } 
            } 
          } 
          break;
        case 1:
          image = new byte[width * height];
          for (by = 0; by < height; by += 4) {
            for (int bx = 0; bx < width; bx += 8) {
              for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 8; x++) {
                  byte b = file.readByte();
                  int outp = (by + y) * width + bx + x;
                  image[outp] = b;
                } 
              } 
            } 
          } 
          break;
        case 2:
          image = new byte[width * height * 2];
          for (by = 0; by < height; by += 4) {
            for (int bx = 0; bx < width; bx += 8) {
              for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 8; x++) {
                  int b = file.readByte() & 0xFF;
                  int outp = ((by + y) * width + bx + x) * 2;
                  image[outp++] = (byte)(b << 4 | b & 0xF);
                  image[outp] = (byte)(b & 0xF0 | b >>> 4);
                } 
              } 
            } 
          } 
          break;
        case 3:
          image = new byte[width * height * 2];
          for (by = 0; by < height; by += 4) {
            for (int bx = 0; bx < width; bx += 4) {
              for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                  byte a = file.readByte();
                  byte l = file.readByte();
                  int outp = ((by + y) * width + bx + x) * 2;
                  image[outp++] = l;
                  image[outp] = a;
                } 
              } 
            } 
          } 
          break;
        case 4:
          image = new byte[width * height * 4];
          for (by = 0; by < height; by += 4) {
            for (int bx = 0; bx < width; bx += 4) {
              for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                  int col = file.readShort() & 0xFFFF;
                  int outp = ((by + y) * width + bx + x) * 4;
                  image[outp++] = (byte)((col & 0x1F) << 3 | (col & 0x1F) >>> 2);
                  image[outp++] = (byte)((col & 0x7E0) >>> 3 | (col & 0x7E0) >>> 8);
                  image[outp++] = (byte)((col & 0xF800) >>> 8 | (col & 0xF800) >>> 13);
                  image[outp] = -1;
                } 
              } 
            } 
          } 
          break;
        case 5:
          image = new byte[width * height * 4];
          for (by = 0; by < height; by += 4) {
            for (int bx = 0; bx < width; bx += 4) {
              for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                  int col = file.readShort() & 0xFFFF;
                  int outp = ((by + y) * width + bx + x) * 4;
                  if ((col & 0x8000) != 0) {
                    image[outp++] = (byte)((col & 0x1F) << 3 | (col & 0x1F) >>> 2);
                    image[outp++] = (byte)((col & 0x3E0) >>> 3 | (col & 0x3E0) >>> 8);
                    image[outp++] = (byte)((col & 0x7C00) >>> 7 | (col & 0x7C00) >>> 12);
                    image[outp] = -1;
                  } else {
                    image[outp++] = (byte)((col & 0xF) << 4 | col & 0xF);
                    image[outp++] = (byte)(col & 0xF0 | (col & 0xF0) >>> 4);
                    image[outp++] = (byte)((col & 0xF00) >>> 4 | (col & 0xF00) >>> 8);
                    image[outp] = (byte)((col & 0x7000) >>> 7 | (col & 0x7000) >>> 10 | (col & 0x7000) >>> 13);
                  } 
                } 
              } 
            } 
          } 
          break;
        case 6:
          image = new byte[width * height * 4];
          for (by = 0; by < height; by += 4) {
            for (int bx = 0; bx < width; bx += 4) {
              int y;
              for (y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                  byte a = file.readByte();
                  byte r = file.readByte();
                  int outp = ((by + y) * width + bx + x) * 4;
                  image[outp + 3] = a;
                  image[outp + 2] = r;
                } 
              } 
              for (y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                  byte g = file.readByte();
                  byte b = file.readByte();
                  int outp = ((by + y) * width + bx + x) * 4;
                  image[outp + 1] = g;
                  image[outp] = b;
                } 
              } 
            } 
          } 
          break;
        case 14:
          image = new byte[width * height * 4];
          for (by = 0; by < height; by += 8) {
            for (int bx = 0; bx < width; bx += 8) {
              for (int sby = 0; sby < 8; sby += 4) {
                for (int sbx = 0; sbx < 8; sbx += 4) {
                  int c1 = file.readShort() & 0xFFFF;
                  int c2 = file.readShort() & 0xFFFF;
                  int block = file.readInt();
                  int r1 = (c1 & 0xF800) >>> 8;
                  int g1 = (c1 & 0x7E0) >>> 3;
                  int b1 = (c1 & 0x1F) << 3;
                  int r2 = (c2 & 0xF800) >>> 8;
                  int g2 = (c2 & 0x7E0) >>> 3;
                  int b2 = (c2 & 0x1F) << 3;
                  int[][] colors = new int[4][4];
                  colors[0][0] = 255;
                  colors[0][1] = r1;
                  colors[0][2] = g1;
                  colors[0][3] = b1;
                  colors[1][0] = 255;
                  colors[1][1] = r2;
                  colors[1][2] = g2;
                  colors[1][3] = b2;
                  if (c1 > c2) {
                    int r3 = ((r1 << 1) + r2) / 3;
                    int g3 = ((g1 << 1) + g2) / 3;
                    int b3 = ((b1 << 1) + b2) / 3;
                    int r4 = (r1 + (r2 << 1)) / 3;
                    int g4 = (g1 + (g2 << 1)) / 3;
                    int b4 = (b1 + (b2 << 1)) / 3;
                    colors[2][0] = 255;
                    colors[2][1] = r3;
                    colors[2][2] = g3;
                    colors[2][3] = b3;
                    colors[3][0] = 255;
                    colors[3][1] = r4;
                    colors[3][2] = g4;
                    colors[3][3] = b4;
                  } else {
                    colors[2][0] = 255;
                    colors[2][1] = (r1 + r2) / 2;
                    colors[2][2] = (g1 + g2) / 2;
                    colors[2][3] = (b1 + b2) / 2;
                    colors[3][0] = 0;
                    colors[3][1] = r2;
                    colors[3][2] = g2;
                    colors[3][3] = b2;
                  } 
                  for (int y = 0; y < 4; y++) {
                    for (int x = 0; x < 4; x++) {
                      int c = block >>> 30;
                      int outp = ((by + sby + y) * width + bx + sbx + x) * 4;
                      image[outp++] = (byte)(colors[c][3] | colors[c][3] >>> 5);
                      image[outp++] = (byte)(colors[c][2] | colors[c][2] >>> 5);
                      image[outp++] = (byte)(colors[c][1] | colors[c][1] >>> 5);
                      image[outp] = (byte)colors[c][0];
                      block <<= 2;
                    } 
                  } 
                } 
              } 
            } 
          } 
          break;
        default:
          System.out.println(String.format("Unsupported texture type %1$d, generating solid color texture instead", new Object[] { Integer.valueOf(format) }));
          image = new byte[width * height * 4];
          for (i = 0; i < width * height; i++) {
            int outp = i * 4;
            image[outp++] = -1;
            image[outp++] = 0;
            image[outp++] = -1;
            image[outp] = -1;
          } 
          break;
      } 
      ret[mip] = image;
      width /= 2;
      height /= 2;
    } 
    return ret;
  }
}
