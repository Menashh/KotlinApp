package com.example.finalprojectkotlin.ui.register

import android.util.Patterns
import androidx.lifecycle.*
import com.example.finalprojectkotlin.data.model.User
import com.example.finalprojectkotlin.data.repository.AuthRepository
import com.example.finalprojectkotlin.util.Resource
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _userRegistrationStatus = MutableLiveData<Resource<User>>()
    val userRegistrationStatus: LiveData<Resource<User>> = _userRegistrationStatus

    fun createUser(email: String, password: String) {
        val error = if(email.isEmpty() || password.isEmpty())
            "Empty Strings"
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Not a valid email"
        }else null
        error?.let {
            _userRegistrationStatus.postValue(Resource.Error(it))
        }
        _userRegistrationStatus.value = Resource.Loading()
        viewModelScope.launch{
            val registrationResult = repository.createUser(email,password)
            _userRegistrationStatus.postValue(registrationResult)
        }

    }

    class RegisterViewModelFactory(private val repo: AuthRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(repo) as T
        }
    }
}