package za.co.synthesis.halo.printlib
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.util.Base64
import com.mobiiot.sdk.printer.CsPrinterBuffer
import java.io.Closeable



abstract class Printer: Closeable {
    
    /**
     * Initializes the printer with an optional application context.
     * 
     * Some printer implementations may require an Android application context
     * for proper initialization.
     * 
     * @param applicationContext Optional Android application context.
     *                          Pass null if the printer implementation doesn't
     *                          require a context for initialization.
     */
    abstract fun initialize(applicationContext: Context?)
    
    /**
     * Prints plain text content to the printer.
     * 
     * This method handles printing of plain text strings.
     * 
     * @param text The plain text content to be printed.
     */
    abstract fun printText(text: String)

    /**
     * Prints HTML content to the printer.
     * 
     * This method handles printing of HTML-formatted content. The printer
     * implementation should parse and render the HTML appropriately for
     * the target device capabilities.
     * 
     * Note: HTML support may vary between different printer implementations.
     * Complex HTML features may not be supported on all printer types.
     * 
     * @param html The HTML content to be printed. Must be valid HTML markup.
     */
    abstract fun printHTML(html: String)

    abstract fun addBitmap(base64Image: String, centered: Boolean = true): CsPrinterBuffer?;

    abstract fun addText(text: String, textSize: Int = 24, isBold: Boolean = false, align: Int = 1): CsPrinterBuffer?;

    abstract fun addHSpacing(): CsPrinterBuffer?;

    abstract fun print();
}