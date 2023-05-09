package fm.filemanager

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.io.File


class MainActivity : AppCompatActivity() {
    //var changedFiles: Array<out File>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(!checkPermission())//запрос разрешения на запись в хранилище
            requestPermission()//если разрешение не получено, запрашиваем его
        else {
            setFirstFragmentView()//разрешение получено, фрагмент с списком файлов отображается
        }

        if (checkPermission()) {
            //changedFiles = HashCheckout().compareHashWithBD(this)
            HashCheckout().saveHashToBD(this)
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
    private fun setFirstFragmentView() {
        val fragment = FileListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(android.R.id.content, fragment)
        //addToBackStack - не нужно добавлять к первому фрагменту - иначе неккоректно работает кнопка назад
        transaction.commit()
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
      ActivityCompat.requestPermissions(this@MainActivity,
          arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),123)
    }
}