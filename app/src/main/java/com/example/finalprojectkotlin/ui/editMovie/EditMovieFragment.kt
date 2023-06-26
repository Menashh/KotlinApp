package com.example.finalprojectkotlin.ui.editMovie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.finalprojectkotlin.R
import com.example.finalprojectkotlin.data.model.Movie
import com.example.finalprojectkotlin.databinding.FragmentEditMovieBinding
import com.example.finalprojectkotlin.ui.moviesViewModel
import com.google.android.material.snackbar.Snackbar


class EditMovieFragment : Fragment() { // binding is the connector between our UI and our data
    private var _binding : FragmentEditMovieBinding? = null

    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    private var movieId: Int? = null


    // We use activityViewModels so that all our fragments will use the same instance of viewModel
    private val viewModel : moviesViewModel by activityViewModels()

    // This is asking for permission to access the gallery
    private var chooseImageLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.resultImage.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            imageUri = it
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentEditMovieBinding.inflate(inflater, container, false)
        //display details real time
        binding.itemTitle.addTextChangedListener(){
            binding.textTitle.text = binding.itemTitle.text.toString()
        }
        binding.itemGenre.addTextChangedListener(){
            binding.textGenre.text = binding.itemGenre.text.toString()
        }
        binding.itemYearRelease.addTextChangedListener(){
            binding.textYear.text = binding.itemYearRelease.text.toString()
        }
        //show details in layout
        viewModel.chosenMovie.observe(viewLifecycleOwner) {
            binding.itemTitle.text = Editable.Factory.getInstance().newEditable(it.title)
            binding.itemDescription.text =
                Editable.Factory.getInstance().newEditable(it.description)
            binding.itemGenre.text = Editable.Factory.getInstance().newEditable(it.genre)
            binding.itemYearRelease.text = Editable.Factory.getInstance().newEditable(it.year)
            Glide.with(requireContext()).load(it.photo)
                .into(binding.resultImage)
            imageUri=Uri.parse(it.photo)
            movieId = it.id
        }

        //Check if all the details are full
        binding.finishBtn.setOnClickListener {
            var emptyFields = ""
            if (binding.itemTitle.text.toString().isEmpty()){
                emptyFields += "${getString(R.string.title)}\n"
            }
            if (binding.itemDescription.text.toString().isEmpty()){
                emptyFields += "${getString(R.string.description)}\n"
            }
            if (binding.itemGenre.text.toString().isEmpty()){
                emptyFields += "${getString(R.string.genre)}\n"
            }
            if (binding.itemYearRelease.text.toString().isEmpty()){
                emptyFields += "${getString(R.string.year)}\n"
            }
            if (imageUri?.toString().isNullOrEmpty()){
                emptyFields += "${getString(R.string.image)}"
            }

            if(emptyFields.isNotEmpty()){
                val note = Snackbar.make(
                    requireView(),
                    "${getString(R.string.pls_fill)}\n$emptyFields", Toast.LENGTH_SHORT)
                note.setTextMaxLines(6)
                note.show()
            }
            // editing  the movie (with the data the user entered) to our local DB
            else {
                val movie = Movie(
                    binding.itemTitle.text.toString(),
                    binding.itemDescription.text.toString(),
                    binding.itemGenre.text.toString(),
                    binding.itemYearRelease.text.toString(),
                    imageUri.toString()
                )

                movie.id = movieId!!
                viewModel.updateMovie(movie)

                // Going back to home screen
                findNavController().navigate(
                    R.id.action_editMovieFragment_to_showMoviesFragment, bundleOf("movie" to movie)
                )
            }
        }
        binding.resultImage.setOnClickListener {
            chooseImageLauncher.launch(arrayOf("image/*"))
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}