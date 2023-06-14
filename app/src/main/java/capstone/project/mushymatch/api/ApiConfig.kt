package capstone.project.mushymatch.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL = "https://mushymatch4-wbdnjog4kq-as.a.run.app/"
    private const val ML = "https://modeljamur3-wbdnjog4kq-as.a.run.app/predict/"

    fun createApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    fun createMLService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ML)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
