package com.ferdsapp.moviefinder.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.utils.Resource
import com.ferdsapp.moviefinder.databinding.FragmentHomeBinding
import com.ferdsapp.moviefinder.ui.adapter.MovieAdapter
import com.ferdsapp.moviefinder.ui.detail.DetailFragment
import com.ferdsapp.moviefinder.ui.utils.Constant
import com.ferdsapp.moviefinder.ui.utils.OnItemClickListener
import com.ferdsapp.moviefinder.viewModel.main.MainViewModel
import com.ferdsapp.moviefinder.viewModel.utils.ViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment(), OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var detailFragment: DetailFragment

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val movieAdapter: MovieAdapter by lazy { MovieAdapter(this) }
    private val tvShowAdapter: MovieAdapter by lazy { MovieAdapter(this) }
    private val homeViewModel: MainViewModel by viewModels{
       factory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null){
            observeApiResponse(homeViewModel.movie, movieAdapter::submitList)
            observeApiResponse(homeViewModel.tvShow, tvShowAdapter::submitList)

            recyclerViewConfig(binding.rvMovie, movieAdapter)
            recyclerViewConfig(binding.rvTvShow, tvShowAdapter)
        }
    }

    private fun recyclerViewConfig(recyclerView: RecyclerView, adapter: MovieAdapter){
        with(recyclerView){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            this.adapter = adapter
        }
    }

    private fun <T> observeApiResponse(
        liveData: LiveData<Resource<ArrayList<T>>>,
        onSuccess: (List<T>) -> Unit
    ){
        liveData.observe(viewLifecycleOwner){ apiResponse ->
            when(apiResponse){
                is Resource.Success -> {
                    Log.d("MainActivity", "response Success ${apiResponse.data}")
                    onSuccess(apiResponse.data)
                    binding.loadingShimmer1.visibility = View.GONE
                    binding.loadingShimmer2.visibility = View.GONE
                }
                is Resource.Empty -> {
                    Log.d("MainActivity", "response Empty")
                    binding.loadingShimmer1.visibility = View.GONE
                    binding.loadingShimmer2.visibility = View.GONE

                }
                is Resource.Error -> {
                    Log.d("MainActivity", "response Error: ${apiResponse.message}")
                    binding.loadingShimmer1.visibility = View.GONE
                    binding.loadingShimmer2.visibility = View.GONE
                }

                is Resource.Loading -> {
                    binding.loadingShimmer1.visibility = View.VISIBLE
                    binding.loadingShimmer2.visibility = View.VISIBLE
                    Log.d("MainActivity", "response Loading")
                }
            }
        }
    }

    override fun onItemClick(position: Int, adapter: MovieAdapter) {
        when(val item = adapter.currentList[position]){
            is MovieEntity -> {
//                Log.d("Home Fragment", "posisiton item movie ${item.original_title}")


                homeViewModel.getDetailMovie(item.media_type!!, item.id).observe(viewLifecycleOwner){ detailData ->
                    when(detailData){
                        is Resource.Empty -> {
                            Log.d("Home Fragment", "onItemClick: Empty")
                            binding.loadingAnimation.visibility = View.GONE
                        }
                        is Resource.Error -> {
                            Log.d("Home Fragment", "onItemClick: Error ${detailData.message}")
                            binding.loadingAnimation.visibility = View.GONE
                        }
                        is Resource.Loading -> {
                            binding.loadingAnimation.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            Log.d("Home Fragment", "onItemClick: Success")
                            binding.loadingAnimation.visibility = View.GONE
                            val bundle = Bundle().apply {
                                putParcelable(Constant.DATA_ITEM, detailData.data)
                            }
                            detailFragment.arguments = bundle
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.frame_container, detailFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    }
                }
            }
            is TvShowEntity -> {
                Log.d("Home Fragment", "posisiton item tvShow ${item.original_name}")
                val bundle = Bundle().apply {
                    putParcelable(Constant.DATA_ITEM, item)
                }
                detailFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

    }
}