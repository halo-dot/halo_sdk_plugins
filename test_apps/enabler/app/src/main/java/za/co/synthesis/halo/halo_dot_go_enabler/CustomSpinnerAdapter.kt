package za.co.synthesis.halo.halo_dot_go_enabler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomSpinnerAdapter(context: Context?, dropdownList: List<Dropdown>) : BaseAdapter() {
    private var context: Context? = context
    private var dropdownList: List<Dropdown>? = dropdownList

    fun CustomSpinnerAdapter(context: Context?, dropdownList: List<Dropdown>?) {
        this.context = context
        this.dropdownList = dropdownList
    }

    override fun getCount(): Int {
//        return dropdownList?.size?.minus(1) ?: 0
        return dropdownList?.size?.minus(1) ?: 0
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rootView: View = LayoutInflater.from(context)
            .inflate(R.layout.simple_spinner_button, p2, false)

        val txtName = rootView.findViewById<TextView>(R.id.name)
        val image: ImageView = rootView.findViewById(R.id.image)

        txtName.text = dropdownList?.get(p0)?.name ?: ""
        dropdownList?.get(p0)?.let { image.setImageResource(it.image) }

        return rootView
    }
}