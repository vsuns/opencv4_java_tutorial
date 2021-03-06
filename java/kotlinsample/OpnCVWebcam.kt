package kotlinsample

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.videoio.VideoCapture
import org.scijava.nativelib.NativeLoader
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel

class OpenCVWebcam : JPanel {

    internal lateinit var image: BufferedImage

    override fun paint(g: Graphics?) {
        g!!.drawImage(image, 0, 0, this)
    }

    constructor() {}

    constructor(img: BufferedImage) {
        image = img
    }

    // Show image on window
    fun window(img: BufferedImage, text: String, x: Int, y: Int) {
        val frame0 = JFrame()
        frame0.contentPane.add(OpenCVWebcam(img))
        frame0.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame0.title = text
        frame0.setSize(img.width, img.height + 30)
        frame0.setLocation(x, y)
        frame0.isVisible = true
    }

    // Load an image
    fun loadImage(file: String): BufferedImage? {
        val img: BufferedImage

        try {
            val input = File(file)
            img = ImageIO.read(input)

            return img
        } catch (e: Exception) {
            println("erro")
        }

        return null
    }

    // Save an image
    fun saveImage(img: BufferedImage) {
        try {
            val outputfile = File("Images/new.png")
            ImageIO.write(img, "png", outputfile)
        } catch (e: Exception) {
            println("error")
        }

    }

    // Grayscale filter
    fun grayscale(img: BufferedImage): BufferedImage {
        for (i in 0 until img.height) {
            for (j in 0 until img.width) {
                val c = Color(img.getRGB(j, i))

                val red = (c.red * 0.299).toInt()
                val green = (c.green * 0.587).toInt()
                val blue = (c.blue * 0.114).toInt()

                val newColor = Color(red + green + blue, red + green + blue, red + green + blue)

                img.setRGB(j, i, newColor.rgb)
            }
        }

        return img
    }

    fun MatToBufferedImage(frame: Mat): BufferedImage {
        // Mat() to BufferedImage
        var type = 0
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR
        }
        val image = BufferedImage(frame.width(), frame.height(), type)
        val raster = image.raster
        val dataBuffer = raster.dataBuffer as DataBufferByte
        val data = dataBuffer.data
        frame.get(0, 0, data)

        return image
    }

    companion object {
        init {
            try {
                // Load the native OpenCV library
                NativeLoader.loadLibrary(Core.NATIVE_LIBRARY_NAME)
            } catch (e: Exception) {

            }

        }

        @Throws(InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {

            val t = OpenCVWebcam()
            val camera = VideoCapture(0)

            val frame = Mat()
            camera.read(frame)

            if (!camera.isOpened) {
                println("Error")
            } else {
                while (true) {
                    if (camera.read(frame)) {
                        val image = t.MatToBufferedImage(frame)
                        t.window(image, "Original Image", 0, 0)
                        t.window(t.grayscale(image), "Processed Image", 40, 60)
                        break
                    }
                }
            }
            camera.release()
        }
    }

}