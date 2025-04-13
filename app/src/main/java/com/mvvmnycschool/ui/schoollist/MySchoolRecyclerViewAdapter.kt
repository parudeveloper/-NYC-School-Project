package com.mvvmnycschool.ui.schoollist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mvvmnycschool.databinding.FragmentSchoolListBinding
import com.mvvmnycschool.domine.HighSchoolListItem

class MySchoolRecyclerViewAdapter(
    private val values: ArrayList<HighSchoolListItem>,
    private val onClickListener: (HighSchoolListItem) -> Unit
) :
    RecyclerView.Adapter<MySchoolRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: FragmentSchoolListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: HighSchoolListItem) {
            with(binding) {
                schoolName.text = item.school_name
                location.text = buildString {
                    append("Location: ")
                    append(item.primary_address_line_1)
                    append(", ")
                    append(item.city)
                    append(", ")
                    append(item.state_code)
                    append(", ")
                    append(item.zip)
                }
                phoneNumber.text = buildString {
                    append("Phone: ")
                    append(item.phone_number)
                }
                website.text = buildString {
                    append("Website: ")
                    append(item.website)
                }
                email.text = buildString {
                    append("Email: ")
                    append(item.school_email)
                }
                if (item.isSaved) {
                    bookmark.visibility = ImageView.VISIBLE
                } else {
                    bookmark.visibility = ImageView.GONE
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FragmentSchoolListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(values[position])
        holder.itemView.setOnClickListener {
            onClickListener(values[position])
        }
    }
}