package fm.filemanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(!checkPermission())//запрос разрешения на запись в хранилище
            requestPermission()//если разрешение не получено, запрашиваем его
        else {
            setFirstFragmentView()//разрешение получено, фрагмент с списком файлов отображается
        }

        if (checkPermission()) {
            val hashService = Intent(this, HashService::class.java)
            startService(hashService)//запуск сервиса в фоне
        }
    }

    //Callback формы на запрос разрешения на запись во внутреннее хранилище
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            for (i in permissions.indices) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        setFirstFragmentView()//пользователь дал разрешение, продолжаем работу
                    }
                    else {
                        //пользователь отклонил разрешение, выводится сообщение, зачем данное разрешение нужно
                        Toast.makeText(this@MainActivity,
                            "Storage permission is requires,please allow from settings", Toast.LENGTH_SHORT ).show()
                    }
                }
            }
        }
    }

    //фукнция для cмены фрагмента
    fun changeFragmentView(filepath: String) {
        val fragment = FileListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        val args = Bundle()
        args.putString("filepath", filepath)
        fragment.arguments = args
        transaction.replace(android.R.id.content, fragment).addToBackStack("Fragment")
        transaction.commit()
    }

    //функция отображения первого фрагмента
    private fun setFirstFragmentView() {
        val fragment = FileListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(android.R.id.content, fragment)
        //addToBackStack - не нужно добавлять к первому фрагменту - иначе неккоректно работает кнопка назад
        //отображается активи без фрагментов, а не происходит выход из приложения
        transaction.commit()
    }

    //функция проверки разрешения на запись
    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    //функция запроса разрешения на запись
    private fun requestPermission() {
      ActivityCompat.requestPermissions(this@MainActivity,
          arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),123)
    }
}