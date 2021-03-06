package io.milkwang.util.image;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 二维码相关工具
 *
 * @author nethunder
 */
public class QRCodeUtils {
    static Logger logger = LoggerFactory.getLogger(QRCodeUtils.class);

    private static final int HEIGHT = 300;
    private static final int WIDTH = 300;
    private static final int IMAGE_WIDTH = 70;
    private static final int IMAGE_HEIGHT = 70;
    private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
    private static final int FRAME_WIDTH = 2;

    /**
     * 给二维码图片添加Logo
     *
     * @param qrPic
     * @param logoPic
     */
    public static Image addLogoToQRCode(InputStream qrPic, InputStream logoPic) {
        LogoConfig logoConfig = new LogoConfig();
        try {
            if (qrPic == null || logoPic == null) {
                logger.error("invalid logo or qr Input stream");
                return null;
            }

            /**
             * 读取二维码图片，并构建绘图对象
             */
            BufferedImage image = ImageIO.read(qrPic);
            if (image == null) {
                return null;
            }

            /**
             * 如果该图片不是彩色的，转换成彩色模式
             */
            if (!image.getColorModel().getColorSpace().isCS_sRGB()) {
                BufferedImage colorPic = new BufferedImage(image.getWidth(), image.getHeight(),
                        BufferedImage.TYPE_3BYTE_BGR);
                ColorSpace rgb = ColorSpace.getInstance(ColorSpace.CS_sRGB);
                ColorConvertOp colorConvertOp = new ColorConvertOp(rgb, null);
                colorConvertOp.filter(image, colorPic);
                image = colorPic;
            }

            try {
                if (qrPic != null) {
                    qrPic.close();
                }
            } catch (IOException ex) {
                logger.error("error occurs when close qr code inputStream", ex);
                return null;
            }

//            image = zoom(image, 100, 100);
            Graphics2D g = image.createGraphics();

            /**
             * 读取Logo图片
             */
            BufferedImage logo = ImageIO.read(logoPic);
            try {
                if (logoPic != null) {
                    logoPic.close();
                }
            } catch (IOException ex) {
                logger.error("error occurs when close logoPic inputStream", ex);
                return null;
            }
            /**
             * 设置logo的大小,设置为二维码图片的20%
             */
            int widthLogo = logo.getWidth(null) > image.getWidth() * 2 / 10 ? (image.getWidth() * 2 / 10) : logo.getWidth(null),
                    heightLogo = logo.getHeight(null) > image.getHeight() * 2 / 10 ? (image.getHeight() * 2 / 10) : logo.getWidth(null);

            // 计算图片放置位置
            /**
             * logo放在中心
             */
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            /**
             * logo放在右下角
             */
//            int x = (image.getWidth() - widthLogo);
//            int y = (image.getHeight() - heightLogo);
            //开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
//            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            logo.flush();
            image.flush();
            return image;
        } catch (Exception e) {
            logger.error("error occurs when adding log to qrCode", e);
            return null;
        }
    }

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage zoom(BufferedImage bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        if (width < 1 || height < 1) {
            return null;
        }
        float oldWidth = bitmap.getWidth(null);
        float oldHeight = bitmap.getHeight(null);
        float xRatio = oldWidth / width;
        float yRatio = oldHeight / height;

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        int x = 0, y = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                x = (int) (i * xRatio);
                if (x > oldWidth) {
                    x = (int) oldWidth;
                }
                y = (int) (j * yRatio);
                if (y > oldHeight) {
                    y = (int) oldHeight;
                }
                result.setRGB(i, j, bitmap.getRGB(x, y));
            }
        }
        return result;
    }

    /**
     * 针对二维码进行解析
     *
     * @param img
     * @return
     */
    public static String decode(InputStream img) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(img);
            if (image == null) {
                logger.error("the decode image may be not exists.");
                return "";
            }
            if (image.getWidth() == image.getHeight()) {// android image
                image = zoom(image, 256, 256);
            } else {// ios image
                image = zoom(image, 300, 300);
            }
            return decodeQRCode(image);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "";
    }

    private static String decodeQRCode(BufferedImage image) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Map<DecodeHintType, Object> hints = new HashMap<>(16);
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE));
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    public static BufferedImage genQRCodeWithLogo(String content, InputStream srcLogoImage) throws WriterException,
            IOException {
        // 读取源图像
        BufferedImage scaleImage = scale(srcLogoImage, IMAGE_WIDTH,
                IMAGE_HEIGHT, false);

        int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
        for (int i = 0; i < scaleImage.getWidth(); i++) {
            for (int j = 0; j < scaleImage.getHeight(); j++) {
                srcPixels[i][j] = scaleImage.getRGB(i, j);
            }
        }
        Hashtable hint = new Hashtable();
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 生成二维码
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,
                WIDTH, HEIGHT, hint);
        int[] pixels = new int[WIDTH * HEIGHT];

        // 二维矩阵转为一维像素数组
        int halfW = matrix.getWidth() / 2;
        int halfH = matrix.getHeight() / 2;

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // 读取图片
                if (x > halfW - IMAGE_HALF_WIDTH
                        && x < halfW + IMAGE_HALF_WIDTH
                        && y > halfH - IMAGE_HALF_WIDTH
                        && y < halfH + IMAGE_HALF_WIDTH) {
                    pixels[y * WIDTH + x] = srcPixels[x - halfW
                            + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
                }
                // 在图片四周形成边框
                else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                        && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
                        && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                        + IMAGE_HALF_WIDTH + FRAME_WIDTH)
                        || (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH
                        && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                        && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                        + IMAGE_HALF_WIDTH + FRAME_WIDTH)
                        || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                        && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                        && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                        - IMAGE_HALF_WIDTH + FRAME_WIDTH)
                        || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                        && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                        && y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                        + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
                    pixels[y * WIDTH + x] = 0xfffffff;
                } else {
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                    pixels[y * WIDTH + x] = matrix.get(x, y) ? 0xff000000
                            : 0xfffffff;
                }
            }
        }

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, WIDTH, HEIGHT, pixels);

        return image;
    }

    public static BufferedImage genQRCode(String content, int width, int height) {
        Hashtable hint = new Hashtable();
        hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 生成二维码
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,
                    width, height, hint);
        } catch (WriterException e) {
            return null;
        }

        matrix = deleteWhite(matrix);
        BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < matrix.getWidth(); x++) {
            for (int y = 0; y < matrix.getHeight(); y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xff000000 : 0xfffffff);
            }
        }
        return image;
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    private static BufferedImage scale(InputStream srcImageFile, int height,
                                       int width, boolean hasFiller) throws IOException {
        double ratio = 0.0; // 缩放比例
//        File file = new File(srcImageFile);
        BufferedImage srcImage = ImageIO.read(srcImageFile);
        Image destImage = srcImage.getScaledInstance(width, height,
                BufferedImage.SCALE_SMOOTH);
        // 计算比例
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
            if (srcImage.getHeight() > srcImage.getWidth()) {
                ratio = (new Integer(height)).doubleValue()
                        / srcImage.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue()
                        / srcImage.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform
                    .getScaleInstance(ratio, ratio), null);
            destImage = op.filter(srcImage, null);
        }
        if (hasFiller) {// 补白
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = image.createGraphics();
            graphic.setColor(Color.white);
            graphic.fillRect(0, 0, width, height);
            if (width == destImage.getWidth(null)) {
                graphic.drawImage(destImage, 0, (height - destImage
                                .getHeight(null)) / 2, destImage.getWidth(null),
                        destImage.getHeight(null), Color.white, null);
            } else {
                graphic.drawImage(destImage,
                        (width - destImage.getWidth(null)) / 2, 0, destImage
                                .getWidth(null), destImage.getHeight(null),
                        Color.white, null);
            }
            graphic.dispose();
            destImage = image;
        }
        return (BufferedImage) destImage;
    }


    public static String createBase64(String url) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            QRCodeUtils.createQrCode(outputStream, url, 1500, "jpg");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            return "data:image/jpg;base64," + encoder.encodeToString(outputStream.toByteArray());
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.error("os close failed", e);
            }
        }
    }


    /**
     * 生成包含字符串信息的二维码图片
     *
     * @param outputStream 文件输出流路径
     * @param content      二维码携带信息
     * @param qrCodeSize   二维码图片大小
     * @param imageFormat  二维码的格式
     * @throws WriterException
     * @throws IOException
     */
    public static boolean createQrCode(OutputStream outputStream, String content, int qrCodeSize, String imageFormat) throws WriterException, IOException {
        //设置二维码纠错级别ＭＡＰ
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  // 矫错级别
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //创建比特矩阵(位矩阵)的QR码编码的字符串
        BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
        // 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth - 200, matrixWidth - 200, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // 使用比特矩阵画并保存图像
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i - 100, j - 100, 1, 1);
                }
            }
        }
        return ImageIO.write(image, imageFormat, outputStream);
    }

    /**
     * 读二维码并输出携带的信息
     */
    public static String readQrCode(InputStream inputStream) throws IOException {
        //从输入流中获取字符串信息
        BufferedImage image = ImageIO.read(inputStream);
        //将图像转换为二进制位图源
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result result = reader.decode(bitmap);
            return result.getText();
        } catch (ReaderException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public static class LogoConfig {
        // logo默认边框颜色
        public final Color DEFAULT_BORDERCOLOR = Color.WHITE;
        // logo默认边框宽度
        public final int DEFAULT_BORDER = 2;
        // logo大小默认为照片的1/5
        public final int DEFAULT_LOGOPART = 5;

        private final int border = DEFAULT_BORDER;
        private Color borderColor;
        private int logoPart;


        public LogoConfig() {
        }

        public LogoConfig(Color borderColor, int logoPart) {
            this.borderColor = borderColor;
            this.logoPart = logoPart;
        }

        public Color getBorderColor() {
            return borderColor;
        }

        public int getBorder() {
            return border;
        }

        public int getLogoPart() {
            return logoPart;
        }
    }
}
