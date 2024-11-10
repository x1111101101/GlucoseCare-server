package io.github.x1111101101.glucoseserver.prescription.util.ocr.opencv

import org.opencv.core.Mat
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.util.concurrent.Executors
import javax.swing.JFrame
import javax.swing.JPanel

fun matToBufferedImage(mat: Mat): BufferedImage {
    val type = if (mat.channels() == 1) BufferedImage.TYPE_BYTE_GRAY else BufferedImage.TYPE_3BYTE_BGR
    val bufferSize = mat.channels() * mat.cols() * mat.rows()
    val buffer = ByteArray(bufferSize)
    mat.get(0, 0, buffer) // Mat에서 데이터 가져오기
    val image = BufferedImage(mat.cols(), mat.rows(), type)
    val targetPixels = (image.raster.dataBuffer as DataBufferByte).data
    System.arraycopy(buffer, 0, targetPixels, 0, buffer.size)
    return image
}

private val pool = Executors.newFixedThreadPool(1)
fun imshow(title: String, img: Mat) {
    val clone = img.clone()
    if(clone.width() * clone.height() == 0) {
        println("Empty")
        clone.release()
        return
    }
    pool.execute {
        try {
            val image = matToBufferedImage(clone)
            val frame = JFrame(title)
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.contentPane = object : JPanel() {
                override fun paintComponent(g: Graphics) {
                    super.paintComponent(g)
                    g.drawImage(image, 0, 0, this)
                }
            }
            frame.contentPane.preferredSize = Dimension(clone.width(), clone.height())
            frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            frame.pack()
            frame.isVisible = true
        } finally {
            clone.release()
        }
    }
}