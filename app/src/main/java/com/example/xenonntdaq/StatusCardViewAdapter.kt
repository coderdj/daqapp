package com.example.xenonntdaq

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList

class StatusCardViewAdapter(private val mDataset: ArrayList<DAQRate>) :
    RecyclerView.Adapter<StatusCardViewAdapter.DataObjectHolder>() {
    private val LOG_TAG = "StatusCardViewAdapter"
    private var myClickListener: MyClickListener? = null

    inner class DataObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var tv_host: TextView
        internal var tv_rate: TextView
        internal var tv_mode: TextView
        internal var tv_status: TextView
        internal var tv_run: TextView
        internal var tv_buffer: TextView


        init  {
            tv_host = itemView.findViewById<View>(R.id.card_host) as TextView
            tv_rate = itemView.findViewById<View>(R.id.card_rate) as TextView
            tv_mode = itemView.findViewById<View>(R.id.card_mode) as TextView
            tv_status = itemView.findViewById<View>(R.id.card_status) as TextView
            tv_run = itemView.findViewById<View>(R.id.card_run) as TextView
            tv_buffer = itemView.findViewById<View>(R.id.card_buffer) as TextView

            //Log.i(LOG_TAG, "Adding Listener")
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            myClickListener!!.onItemClick(adapterPosition, v)
        }
    }

    fun setOnItemClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataObjectHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.run_card, parent, false)

        return DataObjectHolder(view)
    }

    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        holder.tv_host.setText(mDataset[position].host)
        holder.tv_rate.setText(mDataset[position].rate.toString()+" MB/s")
        holder.tv_mode.setText(mDataset[position].run_mode)
        holder.tv_status.setText(mDataset[position].status.toString())
        //holder.tv_run.setText(mDataset[position].current_run_id)
        holder.tv_buffer.setText(mDataset[position].buffer_length.toString())

    }

    fun addItem(dataObj: DAQRate, index: Int) {
        mDataset.add(index, dataObj)
        notifyItemInserted(index)
    }
    fun updateItem(dataObj: DAQRate, index: Int) {
        mDataset[index] = dataObj
        notifyItemChanged(index)
    }
    fun deleteItem(index: Int) {
        mDataset.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    interface MyClickListener {
        fun onItemClick(position: Int, v: View)
    }


}