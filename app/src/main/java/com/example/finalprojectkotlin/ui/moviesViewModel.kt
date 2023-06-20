package com.example.finalprojectkotlin.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.finalprojectkotlin.data.model.Movie
import com.example.finalprojectkotlin.data.repository.MovieRepository
import kotlinx.coroutines.launch

class moviesViewModel(application: Application)  : AndroidViewModel(application){

    private val repository = MovieRepository(application)

    val movies : LiveData<List<Movie>>? = repository.getMovies()

    private val _chosenItem = MutableLiveData<Movie>()
    val chosenMovie : LiveData<Movie> get() = _chosenItem

    fun setMovie(item:Movie) {
        _chosenItem.value = item
    }

    fun addMovie(item: Movie) {
        viewModelScope.launch {
            repository.addMovie(item)
        }
    }

    fun deleteMovie(item:Movie) {
        viewModelScope.launch {
            repository.deleteMovie(item)
        }
    }

    fun updateMovie(item: Movie){
        viewModelScope.launch {
            repository.updateMovie(item)
        }
    }

}