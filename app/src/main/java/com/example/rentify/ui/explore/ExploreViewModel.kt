package com.example.rentify.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rentify.data.remote.Article
import com.google.firebase.firestore.FirebaseFirestore


sealed class ExploreState {
    object Loading : ExploreState()
    data class Success(val articles: List<Article>) : ExploreState()
    data class Error(val exception: Exception) : ExploreState()
}

class ExploreViewModel : ViewModel() {

    private val _exploreState = MutableLiveData<ExploreState>()
    val exploreState: LiveData<ExploreState> get() = _exploreState

    private val db = FirebaseFirestore.getInstance()

    fun fetchArticles() {
        _exploreState.value = ExploreState.Loading

        // Mengambil data dari koleksi "explore_articles" di Firebase
        db.collection("explore_articles")
            .get()
            .addOnSuccessListener { result ->
                val articleList = mutableListOf<Article>()
                for (document in result) {
                    val article = document.toObject(Article::class.java)
                    articleList.add(article)
                }
                _exploreState.value = ExploreState.Success(articleList)
            }
            .addOnFailureListener { exception ->
                _exploreState.value = ExploreState.Error(exception)
            }
    }
}