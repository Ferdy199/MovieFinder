package com.ferdsapp.moviefinder.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ferdsapp.moviefinder.R
import com.ferdsapp.moviefinder.application.MyApplication
import com.ferdsapp.moviefinder.databinding.FragmentDetailBinding


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

}