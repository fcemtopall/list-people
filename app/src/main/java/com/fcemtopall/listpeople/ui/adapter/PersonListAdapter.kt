package com.fcemtopall.listpeople.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fcemtopall.listpeople.MainViewModel
import com.fcemtopall.listpeople.R
import com.fcemtopall.listpeople.databinding.RowPersonBinding
import com.fcemtopall.listpeople.entity.model.Person

class PersonListAdapter(
    private var oldPersonList: MutableList<Person>,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_person,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.rowPersonBinding.apply {
            sharedViewModel = mainViewModel
            model = oldPersonList[position]
        }
    }

    override fun getItemCount() = oldPersonList.size

    fun setData(newPersonList: List<Person>) {
        oldPersonList = newPersonList as MutableList<Person>
        notifyDataSetChanged()
    }
}

class PersonViewHolder(val rowPersonBinding: RowPersonBinding) :
    RecyclerView.ViewHolder(rowPersonBinding.root)