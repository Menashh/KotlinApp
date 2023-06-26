package com.example.finalprojectkotlin.ui.showallmovies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalprojectkotlin.data.model.Movie
import com.example.finalprojectkotlin.databinding.MovieLayoutBinding


class MovieAdapter(val movies:List<Movie>, val callBack: MovieListener)
    : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    interface MovieListener {
        fun onItemClicked(index:Int)
        fun onItemLongClicked(index:Int)
    }

    inner class MovieViewHolder(private val binding: MovieLayoutBinding)
        : RecyclerView.ViewHolder(binding.root),View.OnClickListener,
        View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            callBack.onItemClicked(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onItemLongClicked(adapterPosition)
            return false
        }

        fun bind(item: Movie) {

            binding.movieTitle.text = item.title
            binding.MovieGenre.text=item.genre
            binding.MovieYear.text=item.year
            Glide.with(binding.root).load(item.photo)
                .into(binding.itemImage)
        }
    }


    // Updated implementation of movieAt that check the function doesn't return invalid value
    fun movieAt(position: Int): Movie {
        return if (position >= 0 && position < movies.size) {
            movies[position]
        } else {
            movies[0]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieViewHolder(MovieLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(movies[position])

    override fun getItemCount() =
        movies.size

}