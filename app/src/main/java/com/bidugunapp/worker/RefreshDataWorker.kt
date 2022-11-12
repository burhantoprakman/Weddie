package com.bidugunapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.bidugunapp.model.Photo
import com.bidugunapp.repository.GuestBookRepository
import com.bidugunapp.repository.HomePageRepository
import com.bidugunapp.repository.PhotosRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.bidugunapp.worker.RefreshDataWorker"
    }
    //TODO : Ilgilenmek lazim
    override suspend fun doWork(): Result {
        val photosRepository = PhotosRepository()
        val homePageRepository = HomePageRepository()
        val guestBookRepository = GuestBookRepository()

           // val photoData = workDataOf("photos" to photosRepository.getPhotos() )
            val homePageData = workDataOf("home" to homePageRepository.getEventInfo())
            val guestBookData = workDataOf("guestBook" to guestBookRepository.getGuestBookList())


        return Result.success()
    }
}