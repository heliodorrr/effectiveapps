package com.effectiveapps.app

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.*
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.effectiveapps.app.application.TestApplication
import com.effectiveapps.app.ui.cart.CartFragment
import com.effectiveapps.app.ui.explorer.ExplorerFragment
import com.effectiveapps.app.ui.smartphonedetails.SmartphoneDetailsFragment
import com.effectiveapps.data.api.TestApi
import com.effectiveapps.data.deserializers.GoodsQueryDeserializer
import com.effectiveapps.data.repository.RemoteRepositoryImpl

import com.effectiveapps.domain.model.GoodsQuery
import com.effectiveapps.domain.repository.GoodsQueryRepository
import com.effectiveapps.domain.repository.SmartphoneDetailsRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [
    InjectionModule::class,
    BindingModule::class,
    CommonModule::class,
    AndroidInjectionModule::class
])
interface MainComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindContext(context: Context): Builder
        fun build(): MainComponent
    }

    fun inject(application: TestApplication)
}

@Module
abstract class InjectionModule {
    @ContributesAndroidInjector
    abstract fun explorerFragmentInjector(): ExplorerFragment

    @ContributesAndroidInjector
    abstract fun smartphoneDetailsFragmentInjector(): SmartphoneDetailsFragment

    @ContributesAndroidInjector
    abstract fun cartFragmentInjector(): CartFragment

}

@Module
interface BindingModule {
    @Singleton
    @Binds
    fun bindGoodsQueryRepository(impl: RemoteRepositoryImpl): GoodsQueryRepository

    @Singleton
    @Binds
    fun bindSmartphoneDetailsRepository(impl: RemoteRepositoryImpl): SmartphoneDetailsRepository
}

@Module
class CommonModule {
    @Singleton
    @Provides
    fun provideApi(): TestApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(GoodsQuery::class.java, GoodsQueryDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TestApi::class.java)
    }

}


