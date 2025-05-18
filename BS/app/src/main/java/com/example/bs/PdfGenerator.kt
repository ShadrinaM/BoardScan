package com.example.bs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


class PdfGenerator(private val context: Context){

    /*// Просто ПДФ
    fun generatePdf(images: List<Uri>, context: Context): Uri? {
        if (images.isEmpty()) {
            Log.e("PdfGenerator", "No images to generate PDF")
            return null
        }

        val bordScanDir = getOrCreateBordScanDir()
        val pdfFile = File(bordScanDir, "lecture_${getFormattedDateTime()}.pdf")

        return try {
            val document = Document()
            val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFile))
            document.open()

            for (imageUri in images) {
                val bitmap = try {
                    context.contentResolver.openInputStream(imageUri)?.use { stream ->
                        BitmapFactory.decodeStream(stream)?.let { originalBitmap ->
                            // Поворачиваем изображение на 90 градусов вправо
                            val matrix = Matrix()
                            matrix.postRotate(90f)
                            val rotatedBitmap = Bitmap.createBitmap(
                                originalBitmap,
                                0, 0,
                                originalBitmap.width,
                                originalBitmap.height,
                                matrix,
                                true
                            )
                            // Масштабируем после поворота
                            val maxWidth = 1000
                            val scale = maxWidth.toFloat() / rotatedBitmap.width
                            val height = (rotatedBitmap.height * scale).toInt()
                            Bitmap.createScaledBitmap(rotatedBitmap, maxWidth, height, true)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PdfGenerator", "Error loading image: $imageUri", e)
                    continue
                }

                bitmap?.let {
                    try {
                        document.setPageSize(com.itextpdf.text.Rectangle(it.width.toFloat(), it.height.toFloat()))
                        document.newPage()

                        val stream = ByteArrayOutputStream()
                        it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val image = Image.getInstance(stream.toByteArray())
                        image.setAbsolutePosition(0f, 0f)
                        document.add(image)
                    } catch (e: Exception) {
                        Log.e("PdfGenerator", "Error adding image to PDF", e)
                    }
                }
            }

            document.close()
            writer.close()

            Log.d("PdfGenerator", "PDF successfully generated at: ${pdfFile.absolutePath}")
            cleanPictures()
            Uri.fromFile(pdfFile)
        } catch (e: Exception) {
            Log.e("PdfGenerator", "Error generating PDF", e)
            null
        }
    }*/

    fun generatePdf(images: List<Uri>, context: Context): Uri? {
        if (images.isEmpty()) {
            Log.e("PdfGenerator", "No images to generate PDF")
            return null
        }

        val bordScanDir = getOrCreateBordScanDir()
        val pdfFile = File(bordScanDir, "lecture_${getFormattedDateTime()}.pdf")

        return try {
            val document = Document()
            val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFile))
            document.open()

            for (imageUri in images) {
                val bitmap = try {
                    context.contentResolver.openInputStream(imageUri)?.use { stream ->
                        BitmapFactory.decodeStream(stream)?.let { originalBitmap ->

//                            // Выравнивание доски перед обработкой
//                            val alignedBitmap = alignBoard(originalBitmap)
//
//                            // Поворачиваем изображение на 90 градусов вправо
//                            val matrix = Matrix()
//                            matrix.postRotate(90f)
//                            val rotatedBitmap = Bitmap.createBitmap(
//                                alignedBitmap,  // Используем выровненное изображение
//                                0, 0,
//                                alignedBitmap.width,
//                                alignedBitmap.height,
//                                matrix,
//                                true
//                            )
//                            // Масштабируем после поворота
//                            val maxWidth = 1000
//                            val scale = maxWidth.toFloat() / rotatedBitmap.width
//                            val height = (rotatedBitmap.height * scale).toInt()
//                            Bitmap.createScaledBitmap(rotatedBitmap, maxWidth, height, true)

                            val alignedBitmap = alignBoard(originalBitmap)

                            val maxWidth = 1000
                            val scale = maxWidth.toFloat() / alignedBitmap.width
                            val height = (alignedBitmap.height * scale).toInt()
                            Bitmap.createScaledBitmap(alignedBitmap, maxWidth, height, true)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PdfGenerator", "Error loading image: $imageUri", e)
                    continue
                }

                bitmap?.let {
                    try {
                        document.setPageSize(com.itextpdf.text.Rectangle(it.width.toFloat(), it.height.toFloat()))
                        document.newPage()

                        val stream = ByteArrayOutputStream()
                        it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val image = Image.getInstance(stream.toByteArray())
                        image.setAbsolutePosition(0f, 0f)
                        document.add(image)
                    } catch (e: Exception) {
                        Log.e("PdfGenerator", "Error adding image to PDF", e)
                    }
                }
            }

            document.close()
            writer.close()

            Log.d("PdfGenerator", "PDF successfully generated at: ${pdfFile.absolutePath}")
            cleanPictures()
            Uri.fromFile(pdfFile)
        } catch (e: Exception) {
            Log.e("PdfGenerator", "Error generating PDF", e)
            null
        }
    }

    // Получение текущей даты в нужном формате
    private fun getFormattedDateTime(): String {
        return SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault())
            .format(Date())
    }

    // Создание или получение директории BordScan
    private fun getOrCreateBordScanDir(): File {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val bordScanDir = File(downloadsDir, "BordScan")
        if (!bordScanDir.exists()) {
            bordScanDir.mkdirs()
        }
        return bordScanDir
    }

    // Очищение директории
    private fun cleanPictures() {
        val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        picturesDir?.listFiles()?.forEach {
            try {
                if (it.delete()) {
                    Log.d("PdfGenerator", "Deleted file: ${it.name}")
                }
            } catch (e: Exception) {
                Log.e("PdfGenerator", "Error deleting file: ${it.name}", e)
            }
        }
    }

    fun alignBoard(srcBitmap: Bitmap): Bitmap {
        // Конвертация Bitmap в Mat (BGR)
        val srcMat = Mat()
        Utils.bitmapToMat(srcBitmap, srcMat)
        val origMat = srcMat.clone()  // копия для применения warpPerspective

        // Преобразование в серый и размытие для подавления шума
        val gray = Mat()
        Imgproc.cvtColor(srcMat, gray, Imgproc.COLOR_BGR2GRAY)
        Imgproc.GaussianBlur(gray, gray, Size(5.0, 5.0), 0.0)

        // Решаем, светлая или тёмная доска (по средней яркости)
        val meanVal = Core.mean(gray).`val`[0]
        val thresh = Mat()
        if (meanVal > 127) {
            // Светлая доска: применяем адаптивный порог для выделения границ доски и надписей
            Imgproc.adaptiveThreshold(gray, thresh, 255.0,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 15.0)
        } else {
            // Тёмная доска: инвертированный порог (чтобы доска стала белой на чёрном фоне)
            Imgproc.adaptiveThreshold(gray, thresh, 255.0,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 15, 15.0)
        }

        // Поиск контуров
        val contours = ArrayList<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(thresh, contours, hierarchy,
            Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        Log.d("BoardAlign", "Contours found: ${contours.size}")

        // Выбираем максимальный по площади контур-четырёхугольник
        var boardContour: MatOfPoint2f? = null
        var maxArea = 0.0
        for (contour in contours) {
            val contour2f = MatOfPoint2f(*contour.toArray())
            val area = Imgproc.contourArea(contour2f)
            if (area < 1000) continue  // фильтр очень маленьких областей
            // Аппроксимация контура
            val approx = MatOfPoint2f()
            Imgproc.approxPolyDP(contour2f, approx, 0.02 * Imgproc.arcLength(contour2f, true), true)
            // Проверяем, что контур четырёхвершинный и с максимальной площадью
/*            if (approx.total() == 4L && area > maxArea) {
                maxArea = area
                boardContour = approx
            }*/
            // Вместо area > maxArea
            if (approx.total() == 4L && area > maxArea && area > srcMat.width() * srcMat.height() * 0.2) {
                maxArea = area
                boardContour = approx
            }

        }

        if (boardContour == null) {
            Log.d("BoardAlign", "Board contour not found, returning original")
            return srcBitmap  // доска не обнаружена
        }

        // Сортировка углов: разделяем по y (верхние, нижние) и по x внутри них
        val pts = boardContour.toArray().sortedBy { it.y }
        val topPts = pts.take(2).sortedBy { it.x }
        val bottomPts = pts.takeLast(2).sortedBy { it.x }
        val tl = topPts[0];  val tr = topPts[1]
        val bl = bottomPts[0];  val br = bottomPts[1]
        Log.d("BoardAlign", "Corners: TL=$tl, TR=$tr, BR=$br, BL=$bl")

        // Вычисление размеров доски по расстоянию между точками (реальные пропорции)
        fun dist(p1: Point, p2: Point) = Math.hypot(p1.x - p2.x, p1.y - p2.y)
        val widthA = dist(br, bl)
        val widthB = dist(tr, tl)
        val maxWidth = Math.max(widthA, widthB).toInt()
        val heightA = dist(tr, br)
        val heightB = dist(tl, bl)
        val maxHeight = Math.max(heightA, heightB).toInt()
        Log.d("BoardAlign", "Transformed size: width=$maxWidth, height=$maxHeight")

        // Исходные и целевые точки для преобразования
        val srcPoints = MatOfPoint2f(tl, tr, br, bl)
        val dstPoints = MatOfPoint2f(
            Point(0.0, 0.0),
            Point((maxWidth - 1).toDouble(), 0.0),
            Point((maxWidth - 1).toDouble(), (maxHeight - 1).toDouble()),
            Point(0.0, (maxHeight - 1).toDouble())
        )

        // Перспективное преобразование и обрезка
        val M = Imgproc.getPerspectiveTransform(srcPoints, dstPoints)
        val warped = Mat()
        Imgproc.warpPerspective(origMat, warped, M, Size(maxWidth.toDouble(), maxHeight.toDouble()))

        // Конвертация Mat обратно в Bitmap
        val resultBitmap = Bitmap.createBitmap(warped.cols(), warped.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(warped, resultBitmap)
        Log.d("BoardAlign", "Board aligned and cropped, returning result")
        return resultBitmap
    }
}
