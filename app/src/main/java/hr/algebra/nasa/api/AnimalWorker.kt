package hr.algebra.nasa.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class AnimalWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams){
    override fun doWork(): Result {
        //bg
        AnimalFetcher(context).fetchItems(10)
        return Result.success()
    }

}