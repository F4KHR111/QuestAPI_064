package com.example.questapi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.example.questapi.modeldata.DetailSiswa
import com.example.questapi.modeldata.UIStateSiswa
import com.example.questapi.modeldata.toUiStateSiswa
import com.example.questapi.repositori.RepositoryDataSiswa
import retrofit2.Response
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.questapi.modeldata.toDataSiswa
import com.example.questapi.uicontroller.route.DestinasiDetail
import kotlinx.coroutines.launch

class EditViewModel(savedStateHandle: SavedStateHandle, private val repositoryDataSiswa: RepositoryDataSiswa): ViewModel(){
    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    private val idSiswa: Int = checkNotNull(savedStateHandle[DestinasiDetail.itemIdArg])
    init {
        viewModelScope.launch {
            uiStateSiswa = repositoryDataSiswa.getSatuSiswa(idSiswa)
                .toUiStateSiswa(true)
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa){
        uiStateSiswa = UIStateSiswa(detailSiswa = detailSiswa, isEntryValid = validasiInput(detailSiswa))
    }

    private fun validasiInput(uiState: DetailSiswa = uiStateSiswa.detailSiswa): Boolean {
        return with(uiState){
            nama.isNotBlank() && alamat.isNotBlank() && telpon.isNotBlank()
        }
    }

    suspend fun editSatuSiswa(){
        if (validasiInput(uiStateSiswa.detailSiswa)){
            val call: Response<Void> = repositoryDataSiswa.editSatuSiswa(idSiswa,uiStateSiswa.detailSiswa.toDataSiswa())

            if (call.isSuccessful){
                println("Update Sukses : ${call.message()}")
            }else{
                println("Update Error : ${call.errorBody()}")
            }
        }
    }
}