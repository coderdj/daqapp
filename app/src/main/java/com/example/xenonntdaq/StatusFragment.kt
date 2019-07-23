package com.example.xenonntdaq
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.R
import android.widget.TextView




class StatusFragment : Fragment() {
    var mtxt_rate_reader0: TextView? = null
    var mtxt_rate_reader1:TextView? = null
    var mtxt_rate_reader2:TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View?{
        var rootview : View = inflater.inflate(R.layout.fragment_status, container, false)
        mtxt_rate_reader0 = rootview.findViewById(R.id.reader0_rate) as TextView
        mtxt_rate_reader1 = rootview.findViewById(R.id.reader1_rate) as TextView
        mtxt_rate_reader2 = rootview.findViewById(R.id.reader2_rate) as TextView
        return rootview
        }

    companion object {
        fun newInstance(): StatusFragment = StatusFragment()
    }

    // Receive data from MainActivity
    @SuppressLint("SetTextI18n")
    fun passData(value: Float) {
        mtxt_rate_reader0?.setText(value.toString() + " MB/s")
    }
}