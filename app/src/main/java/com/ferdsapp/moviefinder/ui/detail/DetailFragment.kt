package com.ferdsapp.moviefinder.ui.detail

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.core.data.model.entity.detail.DetailEntity
import com.ferdsapp.moviefinder.databinding.FragmentDetailBinding
import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter
import com.ferdsapp.moviefinder.ui.utils.Constant
import com.ferdsapp.moviefinder.ui.utils.OnItemClickListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class DetailFragment : Fragment(), OnItemClickListener {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding
    private val genreAdapter: MovieAdapter by lazy { MovieAdapter(this) }

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
        val item : DetailEntity? = BundleCompat.getParcelable(arguments ?: Bundle(), Constant.DATA_ITEM,  DetailEntity::class.java)
        when(item){
            is DetailEntity -> {
                binding?.apply {
                    val ratingValue = (item.vote_average?.toFloat()!! / 10f) * 5
                    ratingBar.stepSize = 0.5f
                    ratingBar.post {
                        ratingBar.rating = ratingValue
                    }

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
                        .into(posterImg)

                    genreAdapter.submitList(item.genres as List<Any>)
                }

                with(binding!!.rvGenre){
                    val flexBox = FlexboxLayoutManager(context).apply {
                        flexDirection = FlexDirection.ROW
                        flexWrap = FlexWrap.WRAP
                        justifyContent = JustifyContent.FLEX_START
                    }
                    layoutManager = flexBox
                    setHasFixedSize(true)
                    this.adapter = genreAdapter
                }
            }
        }
    }

    override fun onDestroy() {
        setFragmentResult("emptyView", bundleOf("isBack" to true))
        super.onDestroy()
    }

    override fun onItemClick(position: Int, adapter: MovieAdapter) {
        TODO("Not yet implemented")
    }

}