import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.Base64
import com.google.zxing.BarcodeFormat
import com.mobiiot.api.exception.MobiIotException
import com.mobiiot.sdk.MobiiotAPI
import com.mobiiot.sdk.printer.CsPrinter
import com.mobiiot.sdk.printer.CsPrinterBuffer
import za.co.synthesis.halo.printlib.Printer
import java.io.OutputStream


enum class MobiPosPrintingError(val code: Int, val message: String) {
    OK(0, "OK"),
    NO_PAPER(1, "No paper"),
    OVERHEAT(2, "Printer overheated"),
    INVALID_DATA(3, "Invalid printing data"),
    QUEUE_FULL(4, "Printing queue full"),
    UNKNOWN(10, "Unknown error");

    companion object {
        fun fromCode(code: Int): MobiPosPrintingError {
            return values().find { it.code == code } ?: UNKNOWN
        }

        fun fromException(e: Exception): MobiPosPrintingError {
            return when {
                e.message?.contains("paper", ignoreCase = true) == true -> NO_PAPER
                e.message?.contains("overheat", ignoreCase = true) == true -> OVERHEAT
                e.message?.contains("invalid", ignoreCase = true) == true -> INVALID_DATA
                e.message?.contains("queue", ignoreCase = true) == true -> QUEUE_FULL
                else -> UNKNOWN
            }
        }
    }
}


class MobiPosPrinter : Printer() {
    private var appContext: Context? = null
    private var isInitialized = false
    private var printerBuffer: CsPrinterBuffer? = null

    override fun initialize(applicationContext: Context?) {
        try {
            if (applicationContext != null) {
                appContext = applicationContext
                MobiiotAPI.init(applicationContext)
                printerBuffer = CsPrinterBuffer()
                isInitialized = true
            };
        }catch (err: MobiIotException){
            throw IllegalStateException("Failed to initialize printer: ${err.message}.")
        }
    }

    /**
     * Unbinds the context and cleans up resources as specified in the documentation.
     */
    override fun close() {
        if(isInitialized && appContext != null){
            MobiiotAPI.unbind(appContext)
            appContext = null
            isInitialized = false
        }
    }

    override fun printText(text: String) {
        if(!isInitialized){
            throw IllegalStateException("Printer not initialized. Call initialize() first.")
        }

        val successful=  CsPrinter.printText(text);

        if(!successful){
            val errorCode =  CsPrinter.getLastError()
            val errorStatus = MobiPosPrintingError.fromCode(errorCode)
            throw Exception("Could not print text: $errorStatus")
        }
    }

    override fun addBitmap(base64Image: String, centered: Boolean): CsPrinterBuffer? {
        if (!isInitialized) {
            throw IllegalStateException("Printer not initialized. Call initialize() first.")
        }
        var base64Img = base64Image;

        if (base64Img.contains(",")) {
            base64Img = base64Image.split(",")[1];
        }
        val imageBytes = Base64.decode(base64Img, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        if (centered) {
            printerBuffer?.addBitmapToPrint(bitmapCentered(image))
        } else {
            printerBuffer?.addBitmapToPrint(image)
        }

        return printerBuffer
    }

    fun bitmapCentered(foreground: Bitmap): Bitmap {
        val resultBitmap = Bitmap.createBitmap(
            CsPrinterBuffer.PAPER_OPTIMIZED_WIDTH,
            foreground.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(resultBitmap)

        canvas.drawBitmap(resultBitmap, 0f, 0f, null)

        val centerX = (resultBitmap.width - foreground.width) / 2f
        val centerY = (resultBitmap.height - foreground.height) / 2f

        canvas.drawBitmap(foreground, centerX, centerY, null)

        return resultBitmap
    }

    override fun addText(
        text: String,
        textSize: Int,
        isBold: Boolean,
        align: Int
    ): CsPrinterBuffer? {
        if (!isInitialized) {
            throw IllegalStateException("Printer not initialized. Call initialize() first.")
        }
        printerBuffer?.addTextToPrint(text, textSize, isBold, false, align, Typeface.DEFAULT)
        return printerBuffer
    }

    override fun addHSpacing(): CsPrinterBuffer? {
        if (!isInitialized) {
            throw IllegalStateException("Printer not initialized. Call initialize() first.")
        }

        printerBuffer?.addTextToPrint(
            "-----------------------------------------------------",
            24,
            false,
            false,
            0,
            Typeface.DEFAULT
        )
        return printerBuffer
    }

    override fun print() {
        if (!isInitialized) {
            throw IllegalStateException("Printer not initialized. Call initialize() first.")
        }
        val successful = printerBuffer?.print(true)
        printerBuffer?.clear()
        CsPrinter.printEndLine()

        if (successful != true) {
            val errorCode = CsPrinter.getLastError()
            val errorStatus = MobiPosPrintingError.fromCode(errorCode)
            throw Exception("Could not print: $errorStatus")
        }
    }

    override fun printHTML(html: String) {
        if(!isInitialized){
            throw IllegalStateException("Printer not initialized. Call initialize() first.")
        }

        val successful = CsPrinter.printTextHTML(appContext, html);

        if(!successful){
            val errorCode =  CsPrinter.getLastError()
            val errorStatus = MobiPosPrintingError.fromCode(errorCode)
            throw Exception("Could not print HTML: $errorStatus")
        }
    }

}