package wsdydeni.widget.library.data.network

import retrofit2.http.GET

interface WandroidService {
  @GET("/article/list/0/json")
  suspend fun getArticleList() : DataWrapper<ArticleList>
}