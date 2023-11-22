package capstone.project.mushymatch.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.api.repository.AuthRepository
import capstone.project.mushymatch.view.login.LoginViewModel


/*
   Created by Andre Eka Putra on 22/11/23
   andremoore431@gmail.com
*/

class RegisterViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    fun register(email: String, password: String) {
        repository.register(email = email, password = password, callback = {
            _isError.value = it.isError
            _message.value = it.message
        }
        )
    }
}

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory(private val authRepository: AuthRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

