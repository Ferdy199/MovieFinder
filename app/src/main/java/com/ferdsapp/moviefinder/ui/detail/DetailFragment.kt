package com.ferdsapp.moviefinder.ui.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            is MovieEntity -> {
                Log.d("Home Fragment", "posisiton item movie ${item.original_title}")
                binding?.apply {
                    detailDescription.text = item.overview
                    detailMoviesName.text = item.original_title
                }
            }
            is TvShowEntity -> {
                Log.d("Home Fragment", "posisiton item tvShow ${item.original_name}")
                binding?.apply {
                    detailDescription.text = item.overview
                    detailMoviesName.text = item.original_name
                }
            }
            is DetailEntity -> {
                binding?.apply {
                    detailDescription.text = item.overview
                    detailPosterTxt.text = item.title
                    detailMoviesName.text = item.original_title
                    detailReleasedYear.text = item.release_date
                }
            }
        }
    }

}