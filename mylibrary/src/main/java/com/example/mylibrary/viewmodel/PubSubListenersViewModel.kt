package com.example.mylibrary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mylibrary.data.db.AppDatabase
import com.example.mylibrary.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PubSubListenersViewModel(app: Application) : AndroidViewModel(app) {
    private val userDao by lazy { AppDatabase.getDatabase(getApplication()).userDao()}
    private val companyDao by lazy { AppDatabase.getDatabase(getApplication()).companyDao()}

    suspend fun getUsers() : LiveData<List<User>> = withContext(Dispatchers.IO) {
        userDao.loadAll()
    }

    suspend fun getCompanyIds() : LiveData<List<String>> = withContext(Dispatchers.IO) {
        companyDao.loadAllIds()
    }
}

