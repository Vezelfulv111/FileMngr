package fm.filemanager

import android.app.Service
import android.app.Service.START_STICKY
import android.content.Intent
import android.os.IBinder

//сервис для записи файлов в БД
class HashService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        HashCheckout().saveHashToBD(this)//сохранение хэша файлов

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}