package za.co.synthesis.halo.printlib

class PrinterData {
    lateinit var title: String
    lateinit var sections: Map<Int, Map<String, String>>

    fun fromJson(json: Map<String, Any>) {
        title = json[PrinterData::title.name] as String
        sections = json[PrinterData::sections.name] as Map<Int, Map<String, String>>
    }
}