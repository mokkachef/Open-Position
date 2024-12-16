package com.example.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Vacancy
import com.example.presentation.R
import com.example.presentation.databinding.ListVacanciesItemViewBinding

class VacancyAdapter(
    private val onItemClicked: (Vacancy) -> Unit,
    private val onFavoriteClicked: (Vacancy) -> Unit
) : ListAdapter<Vacancy, VacancyAdapter.VacancyViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(
        holder: VacancyAdapter.VacancyViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (val latestPayload = payloads.lastOrNull()) {
            is VacancyChangePayload.IsFavorite -> {
                holder.bindFavorite(latestPayload.isFavorite)
            }

            else -> onBindViewHolder(holder, position)

        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: VacancyAdapter.VacancyViewHolder, position: Int) {
        val vacancy = getItem(position)
        holder.bind(vacancy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding =
            ListVacanciesItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    // DiffUtil takes care of the check of new list for changes
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Vacancy> =
            object : DiffUtil.ItemCallback<Vacancy>() {

                override fun getChangePayload(oldItem: Vacancy, newItem: Vacancy): Any? {
                    return when {
                        oldItem.isFavorite != newItem.isFavorite -> {
                            VacancyChangePayload.IsFavorite(newItem.isFavorite)
                        }

                        else -> super.getChangePayload(oldItem, newItem)
                    }
                }

                override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
                    return oldItem == newItem
                }
            }
    }

    inner class VacancyViewHolder(
        private val binding: ListVacanciesItemViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: Vacancy) {
            if (vacancy.lookingNumber != null) binding.lookingString.text =
                vacancy.lookingString
            else binding.lookingString.visibility = View.GONE
            bindFavorite(vacancy.isFavorite)
            binding.titleVacancy.text = vacancy.title
            if (vacancy.salaryShort == null) binding.salaryVacancy.visibility = View.GONE
            else binding.salaryVacancy.text = vacancy.salaryShort
            binding.addressTownVacancy.text = vacancy.town
            binding.companyVacancy.text = vacancy.company
            binding.isFavorite.setOnClickListener {
                onFavoriteClicked(vacancy.copy(isFavorite = !vacancy.isFavorite))
            }
            binding.experiencePreviewText.text = vacancy.experience
            binding.publishedDateVacancy.text = vacancy.publishedDate
            itemView.setOnClickListener { onItemClicked(vacancy) }
        }

        fun bindFavorite(isFavorite: Boolean) {
            if (isFavorite) binding.isFavorite.setImageResource(R.drawable.heart_icon_filled)
            else binding.isFavorite.setImageResource(R.drawable.heart_icon_empty)
        }

    }

    private sealed interface VacancyChangePayload {
        @JvmInline
        value class IsFavorite(val isFavorite: Boolean) : VacancyChangePayload
    }

}