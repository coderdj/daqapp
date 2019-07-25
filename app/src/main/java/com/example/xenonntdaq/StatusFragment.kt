package com.example.xenonntdaq
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.R
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xenonntdaq.StatusCardViewAdapter.MyClickListener







class StatusFragment : Fragment() {

    // Fancy recycler view, to dynamically create a bunch of cardviews inside
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: StatusCardViewAdapter? = null // RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootview: View = inflater.inflate(R.layout.fragment_status, container, false)

        mRecyclerView = rootview.findViewById(R.id.status_recycler_view);
        mRecyclerView!!.setHasFixedSize(true);
        mLayoutManager = LinearLayoutManager(this.context);
        mRecyclerView!!.setLayoutManager(mLayoutManager);
        mAdapter = StatusCardViewAdapter(getDataSet());
        mRecyclerView!!.setAdapter(mAdapter);

        return rootview
    }

    override fun onResume() {
        super.onResume()
        (mAdapter as StatusCardViewAdapter).setOnItemClickListener(object : StatusCardViewAdapter.MyClickListener {
            override fun onItemClick(position: Int, v: View) {
                Log.i("StatusFragment", " Clicked on Item $position")
            }
        })
    }

    private fun getDataSet(): ArrayList<DAQRate> {
        val results = ArrayList<DAQRate>()
        for (index in 0..2) {

            val obj = DAQRate(
                "reader $index _reader_0",
                (0.0).toFloat(),
                (0).toInt(),
                "mode",
                (0.0).toFloat()
            )
            results.add(index, obj)
        }
        return results
    }

    companion object {
        fun newInstance(): StatusFragment = StatusFragment()
    }

    // Receive data from MainActivity

    //@SuppressLint("SetTextI18n")
    fun passData(data : DAQRate, index : Int) {
        this.mAdapter?.updateItem(data, index)
        /*
        if (host == "reader0_reader_0") {
            mtxt_rate_reader0?.setText(rate + " MB/s")
            mtxt_host_reader0?.setText(host)
        }
        if (host == "reader1_reader_0") {
            mtxt_rate_reader1?.setText(rate + " MB/s")
            mtxt_host_reader1?.setText(host)
        }
        if (host == "reader2_reader_0") {
            mtxt_rate_reader2?.setText(rate + " MB/s")
            mtxt_host_reader2?.setText(host)
        }
        */
    }

}