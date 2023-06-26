package com.example.finalprojectkotlin.ui.showallmovies

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectkotlin.R
import com.example.finalprojectkotlin.data.model.Movie
import com.example.finalprojectkotlin.databinding.ShowMoviesBinding
import com.example.finalprojectkotlin.ui.MainActivity
import com.example.finalprojectkotlin.ui.moviesViewModel
import com.google.android.material.snackbar.Snackbar

class ShowMoviesFragment:Fragment() {
    // binding is the connector between our UI and our data
    private var _binding : ShowMoviesBinding? = null

    private val binding get() = _binding!!

    // We use activityViewModels so that all our fragments will use the *same instance* of viewModel
    private val viewModel : moviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Creating the connection var from the data (model) to our UI
        _binding = ShowMoviesBinding.inflate(inflater,container,false)

        // Moving to 'add movie' layout when '+' button in clicked-on
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_showMoviesFragment_to_addMovieFragment2)
        }
        binding.info.setOnClickListener {
            val note = Snackbar.make(
                requireView(),
                getString(R.string.orders), Toast.LENGTH_SHORT)
            note.setTextMaxLines(4)
            note.show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Setting observer on the user's actions and the matching responses
        viewModel.movies?.observe(viewLifecycleOwner) {
            binding.recycler.adapter = MovieAdapter(it, object : MovieAdapter.MovieListener {

                override fun onItemClicked(index: Int) {
                    viewModel.setMovie(it[index])
                    findNavController().navigate(
                        R.id.action_showMoviesFragment_to_detailsMovieFragment2,
                        bundleOf("movie" to index)
                    )

                }

                override fun onItemLongClicked(index: Int) {
                    viewModel.setMovie(it[index])
                    findNavController().navigate(
                        R.id.action_showMoviesFragment_to_editMovieFragment,
                        bundleOf("movie" to index)
                    )

                }
            })
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())

            ItemTouchHelper(object : ItemTouchHelper.Callback() {

                // Allowing the user to move right and left
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) = makeFlag(
                    ItemTouchHelper.ACTION_STATE_SWIPE,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                )

                // Must implemention
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                // Delete process (with alert dialog) on swipe
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    // Saving the index of the swiped item
                    val currIndex = viewHolder.adapterPosition

                    // The line below makes the item return to the front of the screen after the swipe
                    binding.recycler.adapter?.notifyItemChanged(currIndex)

                    // The 'Are you sure you want to delete' alert dialog
                    val builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setTitle(getString(R.string.delete_movie))
                        setMessage(getString(R.string.delete_movie_msg))
                        builder.setPositiveButton(getString(R.string.delete_movie_btn)) { _, _ ->
                            // Delete item logic here
                            val item =
                                (binding.recycler.adapter as MovieAdapter).movieAt(currIndex)
                            viewModel.deleteMovie(item)
                        }

                        builder.setNegativeButton(getString(R.string.delete_movie_cancel_btn)) { dialog, _ ->
                            // Dismiss dialog
                            dialog.dismiss()
                        }
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }).attachToRecyclerView(binding.recycler)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
