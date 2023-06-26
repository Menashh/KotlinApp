package com.example.finalprojectkotlin.ui.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.finalprojectkotlin.databinding.DetailsOneMovieBinding
import com.example.finalprojectkotlin.ui.moviesViewModel

class DetailsMovieFragment:Fragment() {

    // binding is the connector between our UI and our data
    private var _binding : DetailsOneMovieBinding?  = null

    // We use activityViewModels so that all our fragments will use the *same instance* of viewModel
    private val viewModel : moviesViewModel by activityViewModels()

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsOneMovieBinding.inflate(inflater,container,false)

        return binding.root
    }

    // Displaying the movie details on screen (using the connector - binding)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenMovie.observe(viewLifecycleOwner) {
            binding.movieTitle.text = it.title
            binding.movieDescription.text = it.description
            binding.movieGenre.text = it.genre
            binding.movieYear.text = it.year
            Glide.with(requireContext()).load(it.photo)
                .into(binding.itemImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}