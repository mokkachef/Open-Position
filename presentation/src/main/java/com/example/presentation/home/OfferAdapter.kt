package com.example.presentation.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.IconType
import com.example.domain.entities.Offer
import com.example.presentation.R
import com.example.presentation.databinding.ListOffersItemViewBinding

class OfferAdapter(
    private val onItemClicked: (Offer) -> Unit,
) : ListAdapter<Offer, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding =
            ListOffersItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val offer = getItem(position)
        (holder as VacancyViewHolder).bind(offer)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Offer> =
            object : DiffUtil.ItemCallback<Offer>() {
                override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Offer, newItem: Offer
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    inner class VacancyViewHolder(
        private val binding: ListOffersItemViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(offer: Offer) {
            when (offer.iconType) {
                IconType.NearVacancies -> {
                    binding.offerIcon.visibility = View.VISIBLE
                    binding.offerIcon.setImageResource(R.drawable.near_vacancies)
                }

                IconType.LevelUpResume -> {
                    binding.offerIcon.visibility = View.VISIBLE
                    binding.offerIcon.setImageResource(R.drawable.level_up_resume)
                }

                IconType.TemporaryJob -> {
                    binding.offerIcon.visibility = View.VISIBLE
                    binding.offerIcon.setImageResource(R.drawable.temporary_job)
                }

                null -> {}
            }
            binding.titleText.text = offer.title
            if (offer.buttonOffer != null) {
                binding.offerActionText.text = offer.buttonOffer
                binding.offerActionText.visibility = View.VISIBLE
            }
            itemView.setOnClickListener { onItemClicked(offer) }
        }
    }

}