package com.effectiveapps.app.ui.smartphonedetails

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import com.effectiveapps.domain.RequestResult
import com.effectiveapps.domain.usecase.SmartphoneDetailsUseCase
import javax.inject.Inject

class SmartphoneDetailsViewModel @Inject constructor(
    private val smartphoneDetailsUseCase: SmartphoneDetailsUseCase,
): ViewModel() {

    private val smartphoneDetailsFragmentStateLiveData =
        MutableLiveData<RequestResult<SmartphoneDetailsFragmentsState>>(null)

    internal val notifyColorOptionChanged: (Int)->Unit = { newIndex->
        val currentState = smartphoneDetailsFragmentStateLiveData.value
        if (currentState is RequestResult.Success && currentState.data.colorOptionIndex!=newIndex) {
            smartphoneDetailsFragmentStateLiveData
                .value = RequestResult.Success(currentState.data.copy(colorOptionIndex = newIndex))
        }
    }

    internal val notifyFavoriteStatusChanged: ()->Unit = {
        val currentState = smartphoneDetailsFragmentStateLiveData.value

        if (currentState is RequestResult.Success) {
            val copy = currentState.data.copy(isFavorite = !currentState.data.isFavorite)
                smartphoneDetailsFragmentStateLiveData
                .value = RequestResult.Success(copy)
        }
    }

    internal val notifyStorageOptionChanged: (Int)->Unit = { newIndex->
        val currentState = smartphoneDetailsFragmentStateLiveData.value
        if (currentState is RequestResult.Success && currentState.data.storageOptionIndex!=newIndex) {
            smartphoneDetailsFragmentStateLiveData
                .value = RequestResult.Success(currentState.data.copy(storageOptionIndex = newIndex))
        }
    }

    init {

        viewModelScope.launch {
            smartphoneDetailsUseCase
                .invoke()
                .flowOn(Dispatchers.IO)
                .collect {
                    smartphoneDetailsFragmentStateLiveData
                        .value = when(it) {
                            is RequestResult.Loading -> RequestResult.Loading
                            is RequestResult.Error -> RequestResult.Error(it.error)
                            is RequestResult.Success -> {
                                RequestResult.Success(SmartphoneDetailsFragmentsState(it.data))
                            }
                        }
                }
        }
    }

    internal val currentStateResult get() = smartphoneDetailsFragmentStateLiveData.value!!

    internal fun subscribeFragment(
        lo: LifecycleOwner,
        lambda: (SmartphoneDetailsFragmentsState)->Unit
    ) {
        smartphoneDetailsFragmentStateLiveData.observe(lo) {
            if (it is RequestResult.Success) {
                lambda(it.data)
            }
        }
    }

}

