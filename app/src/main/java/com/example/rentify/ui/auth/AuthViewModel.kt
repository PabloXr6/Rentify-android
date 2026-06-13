package com.example.rentify.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentify.data.pref.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    object Idle : AuthResult()
    object Loading : AuthResult()
    data class Success(val user: FirebaseUser? = null) : AuthResult()
    data class Error(val exception: Exception) : AuthResult()
}

class AuthViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signInState = MutableLiveData<AuthResult>(AuthResult.Idle)
    val signInState: LiveData<AuthResult> = _signInState

    private val _signUpState = MutableLiveData<AuthResult>(AuthResult.Idle)
    val signUpState: LiveData<AuthResult> = _signUpState

    fun checkLoginSession(): Boolean {
        return (auth.currentUser != null || userPreferences.isLoggedIn())
    }

    fun signIn(email: String, password: String) {
        _signInState.value = AuthResult.Loading
        viewModelScope.launch {
            // Local bypass for admin testing without Firebase configuration
            if (email == "admin@rentify.com" && password == "admin 123") {
                userPreferences.saveLoginSession("Admin", email)
                _signInState.value = AuthResult.Success(null)
                return@launch
            }

            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    val displayName = user.displayName ?: email.substringBefore("@")
                    userPreferences.saveLoginSession(displayName, email)
                    _signInState.value = AuthResult.Success(user)
                } else {
                    _signInState.value = AuthResult.Error(Exception("User is null"))
                }
            } catch (e: Exception) {
                _signInState.value = AuthResult.Error(e)
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        _signUpState.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    userPreferences.saveLoginSession(name, email)
                    _signUpState.value = AuthResult.Success(user)
                } else {
                    _signUpState.value = AuthResult.Error(Exception("User is null"))
                }
            } catch (e: Exception) {
                _signUpState.value = AuthResult.Error(e)
            }
        }
    }

    fun logout() {
        auth.signOut()
        userPreferences.logout()
    }
}
