package com.onyx.rssreader.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onyx.rssreader.models.Article
import com.onyx.rssreader.services.CacheProvider
import com.prof.rssparser.Parser
import kotlinx.coroutines.launch

class ArticleViewModel(val app: Application): AndroidViewModel(app) {

    private val url = MutableLiveData("https://www.androidauthority.com/feed")
    val articles = MutableLiveData<List<Article>>()

    init {
        val string = CacheProvider.loadUrlFromCache(app.applicationContext)
        if (string != "null") {
            url.value = string
        }
        getArticles()
    }

    private fun isNetworkAvailable(): Boolean {
        val manager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting?:false
    }

    private fun getArticles() =viewModelScope.launch {
        if (isNetworkAvailable()) {
            articles.value = try {
                val parser = Parser()
                parser.getChannel(url.value!!).articles.map {
                    Article(
                        title = it.title?:"",
                        description = it.description?:"",
                        image = it.image?:"",
                        pubDate = it.pubDate?:"",
                        content = it.content?:""
                    )
                }
            } catch (e: Exception) {
                listOf()
            }
            if (articles.value!!.isNotEmpty()) {
                CacheProvider.saveArticlesToCache(app.applicationContext,articles.value!!.take(10), url.value!!)
            } else {
                Toast.makeText(app.applicationContext, "Bad RSS Feed URL!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(app.applicationContext, "You're in offline mode!", Toast.LENGTH_SHORT).show()
            articles.value = CacheProvider.loadArticlesListFromCache(app.applicationContext)
        }

    }

    fun setUrl(string: String) {
        url.value = string
        getArticles()
    }

}