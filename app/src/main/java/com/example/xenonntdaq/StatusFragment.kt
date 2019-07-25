package com.example.xenonntdaq
import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import com.beust.klaxon.Klaxon
import org.jetbrains.anko.doAsync
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import androidx.annotation.NonNull
import com.androidplot.xy.XYGraphWidget
import java.text.*


class StatusFragment : Fragment() {

    // Fancy recycler view, to dynamically create a bunch of cardviews inside
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: StatusCardViewAdapter? = null // RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var plot: XYPlot? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootview: View = inflater.inflate(R.layout.fragment_status, container, false)

        mRecyclerView = rootview.findViewById(R.id.status_recycler_view);
        mRecyclerView!!.setHasFixedSize(true);
        mLayoutManager = LinearLayoutManager(this.context);
        mRecyclerView!!.setLayoutManager(mLayoutManager);
        mAdapter = StatusCardViewAdapter(getDataSet());
        mRecyclerView!!.setAdapter(mAdapter);

        // initialize our XYPlot reference:
        var plot : XYPlot = rootview.findViewById(R.id.status_plot_view) as XYPlot
        refreshPlot(plot)

        return rootview
    }

    fun refreshPlot(plotview: XYPlot) {

        var hosts = arrayOf("reader0_reader_0", "reader1_reader_0", "reader2_reader_0")
        var rates_array : MutableList<MutableList<Float>> = mutableListOf<MutableList<Float>>()
        var hosts_array : MutableList<String> = mutableListOf<String>()
        var times_array : MutableList<MutableList<Long>> = mutableListOf<MutableList<Long>>()

        doAsync {
            for (host in hosts) {

                // API call
                Log.d("CALLING", BuildConfig.API_URL + "getstatus/" + host +
                        "?api_key=" + BuildConfig.API_KEY + "&api_user=" + BuildConfig.API_USER +
                        "&time_seconds=60")
                var raw_result = URL(
                    BuildConfig.API_URL + "getstatus/" + host +
                            "?api_key=" + BuildConfig.API_KEY + "&api_user=" + BuildConfig.API_USER +
                            "&time_seconds=60"
                ).readText()
                var result = Klaxon().parseArray<DAQRate>(raw_result.toString())

                var ra : MutableList<Float> = mutableListOf<Float>()
                var ta : MutableList<Long> = mutableListOf<Long>()

                Log.d("resultSize", result?.size.toString())
                result?.forEach{
                    ra.add(it.rate)
                    ta.add(java.lang.Long.parseLong(it._id.substring(0, 8), 16) * 1000)
                }
                hosts_array.add(host)
                times_array.add(ta)
                rates_array.add(ra)
            }
            // JOIN to main thread to plot
            var serieslow = times_array[0][0]
            var serieshigh = times_array[0][times_array[0].size-1]
            activity?.runOnUiThread {
                for(i in 0..hosts_array.size-1) {
                    var series = SimpleXYSeries(times_array[i], rates_array[i], hosts_array[i])
                    var seriesFormat = LineAndPointFormatter(context, R.xml.line_point_formatter_with_labels)
                    plotview.addSeries(series, seriesFormat)
                }
                plotview.setDomainBoundaries(serieslow, serieshigh, BoundaryMode.FIXED );

                plotview.setDomainStep(StepMode.SUBDIVIDE, 5.0);
                plotview.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
                    private val dateFormat = DateFormat.getTimeInstance()
                    override fun format(
                        obj: Any,
                        toAppendTo: StringBuffer,
                        pos: FieldPosition
                    ): StringBuffer {
                        val timestamp = obj as Number
                        return dateFormat.format(timestamp, toAppendTo, pos)
                    }

                    override fun parseObject(source: String, pos: ParsePosition): Any? {
                        return null
                    }
                }
                plotview.redraw()
            } // end runOnUiThread
        } // end doAsync
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
                (0.0).toFloat(),
                "0"
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