import android.content.Context
import com.mobiiot.api.exception.MobiIotException
import com.mobiiot.sdk.MobiiotAPI
import com.mobiiot.sdk.printer.CsPrinterBuffer

import android.graphics.Typeface
import com.mobiiot.sdk.printer.CsPrinter
import za.co.synthesis.halo.printlib.Printer


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

    fun toMap(): Map<String, Any> {
        return mapOf(
            "code" to code,
            "message" to message
        )
    }
}


class MobiPosPrinter : Printer() {
    private var appContext: Context? = null
    private var isInitialized = false

    override fun initialize(applicationContext: Context?) {
        try {
            if (applicationContext != null) {
                appContext = applicationContext
                MobiiotAPI.init(applicationContext)
                isInitialized = true
            };
        }catch (err: MobiIotException){
            // throw the exception
            isInitialized = false;
        }
    }

    /**
     * Unbinds the context and cleans up resources as specified in the documentation.
     */
    override fun close() {
        if(isInitialized && appContext != null){
            try {
                MobiiotAPI.unbind(appContext)
            }catch (e: Exception){

            }finally {
                appContext = null
                isInitialized = false
            }
        }
    }

    override fun printText(
        text: String,
        textSize: Int?,
        isBold: Boolean?,
        isUnderlined: Boolean?,
    ) {
        if(!isInitialized){
            throw IllegalStateException("Printer not initialized. Call initialize() first.")
        }
        val buffer = CsPrinterBuffer()
        buffer.clear()
        buffer.addTextToPrint(text, textSize ?: 24, isBold == true,
            isUnderlined == true, 1, Typeface.DEFAULT)
        buffer.clear()
    }

    override fun printHTML(html: String) {
        if(!isInitialized){
            throw IllegalStateException("Printer not initialized. Call initialize() first.")
        }

        val successful=  CsPrinter.printTextHTML(appContext, html);

        if(!successful){
            val errorCode =  CsPrinter.getLastError()
            val errorStatus = MobiPosPrintingError.fromCode(errorCode)
            throw Exception("Could not print HTML: $errorStatus")
        }
    }

}