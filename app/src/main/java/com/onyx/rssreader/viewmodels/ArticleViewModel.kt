package com.onyx.rssreader.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onyx.rssreader.models.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.launch

class ArticleViewModel(val app: Application): AndroidViewModel(app) {

    val url = MutableLiveData<String>("https://www.androidauthority.com/feed")
    val articles = MutableLiveData<List<Article>>()

    init {
        getArticles()
    }

    fun getArticles() =viewModelScope.launch {
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
        println(articles.value.toString())
    }

    fun setUrl(string: String) {
        url.value = string
        getArticles()
    }

}