package ice.graphic.utils;


import ice.graphic.texture.DdsTexture;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * User: jason
 * Date: 12-4-1
 * Time: 下午12:18
 */
public class DDSLoader implements DDSurface {
    private static final String DDS_IDENTIFIER = "DDS ";
    private static final int DDS_HEADER_SIZE = 128;        // size of the dds header
    private static final int DDS_DESC2_RESERVED_1 = 44;    // bytesize of DWORD[11]
    private static final int DDS_DESC2_RESERVED_2 = 4;     // bytesize of DWORD
    private static final int DDS_CAPS2_RESERVED = 8;       // bytesize of DWORD[2]
    private static final int DEFAULT_DXT_BLOCKSIZE = 16;
    private static final int DXT1_BLOCKSIZE = 8;

    public static DDSurfaceDesc2 readFileHeader(InputStream input) {
        DDSurfaceDesc2 ddsDesc2 = new DDSurfaceDesc2();

        ByteBuffer ddsHeader = ByteBuffer.allocateDirect(DDS_HEADER_SIZE).order(ByteOrder.nativeOrder());

        try {
            // read the header
            byte[] header = new byte[DDS_HEADER_SIZE];
            input.read(header, 0, DDS_HEADER_SIZE);
            ddsHeader.put(header);
            ddsHeader.rewind();

            // read and feed the DDSurfaceDesc2
            ddsDesc2.setIdentifier(ddsHeader.getInt());
            ddsDesc2.setSize(ddsHeader.getInt());
            ddsDesc2.setFlags(ddsHeader.getInt());
            ddsDesc2.setHeight(ddsHeader.getInt());
            ddsDesc2.setWidth(ddsHeader.getInt());
            ddsDesc2.setPitchOrLinearSize(ddsHeader.getInt());
            ddsDesc2.setDepth(ddsHeader.getInt());
            ddsDesc2.setMipMapCount(ddsHeader.getInt());
            // skip, cause next is unused
            ddsHeader.position(ddsHeader.position() + DDS_DESC2_RESERVED_1);

            // DDPixelFormat of DDSurfaceDesc2
            DDPixelFormat pixelFormat = ddsDesc2.getDDPixelformat();
            pixelFormat.setSize(ddsHeader.getInt());
            pixelFormat.setFlags(ddsHeader.getInt());
            pixelFormat.setFourCC(ddsHeader.getInt());
            pixelFormat.setRGBBitCount(ddsHeader.getInt());
            pixelFormat.setRBitMask(ddsHeader.getInt());
            pixelFormat.setGBitMask(ddsHeader.getInt());
            pixelFormat.setBBitMask(ddsHeader.getInt());
            pixelFormat.setRGBAlphaBitMask(ddsHeader.getInt());

            // DDSCaps2 of DDSurfaceDesc2
            DDSCaps2 caps2 = ddsDesc2.getDDSCaps2();
            caps2.setCaps1(ddsHeader.getInt());
            caps2.setCaps2(ddsHeader.getInt());

            // skip, cause next is unused
            ddsHeader.position(ddsHeader.position() + DDS_CAPS2_RESERVED);

            // we don't wanna read the last 4 bytes, they are not used anyway,
            // but we skip them. Funny, ain't?
            ddsHeader.position(ddsHeader.position() + DDS_DESC2_RESERVED_2);
            // the last two instuctions might be banned, but thats your decission

            return ddsDesc2;
        }
        catch (BufferUnderflowException bue) {
            bue.printStackTrace();
        }
        catch (TextureFormatException tfe) {
            tfe.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            ddsHeader = null;   // free the memory
        }
        return ddsDesc2;
    }

    public static ByteBuffer readFileData(InputStream input, DDSurfaceDesc2 ddsDesc2) {
        final DDPixelFormat ddpf = ddsDesc2.getDDPixelformat();
        int imageSize = 0;
        int dxtFormat = 0;

        /* calculate the image size depending on the used blocksize
  and set the used DXT format */
        if (ddpf.isCompressed && ddpf.getFourCCString().equalsIgnoreCase("DXT1")) {
            imageSize = calculateSize(ddsDesc2, DXT1_BLOCKSIZE);
            // at the moment we treat any DXT1 image as RGBA,
            // maybe this can be switched dynamically in future...
            // dxtFormat = GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;

            dxtFormat = DdsTexture.ATC_RGBA_INTERPOLATED_ALPHA_AMD;
        }
        else {
//            imageSize = calculateSize(DEFAULT_DXT_BLOCKSIZE);
//            if (ddpf.getFourCCString().equalsIgnoreCase("DXT3"))
//                dxtFormat = GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
//            else if (ddpf.getFourCCString().equals("DXT5"))
//                dxtFormat = GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
        }

        // read the dds file data itself
        ByteBuffer imageData = ByteBuffer.allocateDirect(ddsDesc2.pitchOrLinearSize).order(ByteOrder.nativeOrder());

        try {
            byte[] data = new byte[ddsDesc2.pitchOrLinearSize];
            input.read(data, 0, ddsDesc2.pitchOrLinearSize);
            imageData.put(data);
            imageData.rewind();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return imageData;
    }

    private static int calculateSize(DDSurfaceDesc2 ddsDesc2, int blockSize) {
        double size = Math.ceil(ddsDesc2.width / 4) * Math.ceil(ddsDesc2.height / 4) * blockSize;
        return (int) size;
    }

}

