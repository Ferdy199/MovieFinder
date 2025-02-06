package com.ferdsapp.moviefinder.ui.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.bumptech.glide.Glide
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.core.data.model.entity.detail.DetailEntity
import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.databinding.FragmentDetailBinding
import com.ferdsapp.moviefinder.ui.utils.Constant


class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null
    val binding get() = _binding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(val item : Any? = arguments?.getParcelable(Constant.DATA_ITEM)){
            is DetailEntity -> {
                binding?.apply {
                    detailDescription.text = item.overview
                    detailPosterTxt.text = if (!item.title.isNullOrEmpty()) item.title else item.name
                    detailMoviesName.text = if (!item.original_title.isNullOrEmpty()) item.original_title else item.original_name
                    detailReleasedYear.text = if (!item.release_date.isNullOrEmpty()) item.release_date else item.first_air_date
                    Glide.with(requireActivity().application)
                        .load("https://image.tmdb.org/t/p/w500" + item.backdrop_path)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.ic_broken_image_24)
                        .into(detailPosterImg)

                    Glide.with(requireActivity().application)
                        .load("https://image.tmdb.org/t/p/w500" + item.poster_path)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.ic_broken_image_24)
                        .into(detailImg)
                }
            }
        }
    }

    override fun onDestroy() {
        setFragmentResult("emptyView", bundleOf("isBack" to true))
        super.onDestroy()
    }

}