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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(!checkPermission())
            requestPermission()
        else {
            setFirstFragmentView()
        }

        if (checkPermission()) {
            var changedFiles = HashCheckout().compareHashWithBD(this)
            HashCheckout().saveHashToBD(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fragment = getVisibleFragment() as FileListFragment
        val hasArrowDown = item.title.contains("\u2193")
        when (item.itemId) {
            R.id.changedFiles -> {

            }
            R.id.name -> {
                if (hasArrowDown) {
                    item.title ="\u2191 По имени"
                    fragment.sortByName(SortType.NameDown)
                } else {
                    item.title ="\u2193 По имени"
                    fragment.sortByName(SortType.NameUp)
                }
                return true
            }
            R.id.size -> {
                if (hasArrowDown) {
                    item.title ="\u2191 По размеру"
                    fragment.sortByName(SortType.SizeUp)
                } else {
                    item.title ="\u2193 По размеру"
                    fragment.sortByName(SortType.SizeDown)
                }
                return true
            }
            R.id.date -> {
                if (hasArrowDown) {
                    item.title ="\u2191 По дате создания"
                    fragment.sortByName(SortType.DateUp)
                } else {
                    item.title ="\u2193 По дате создания"
                    fragment.sortByName(SortType.DateDown)
                }
                return true
            }
            R.id.extension -> {
                if (hasArrowDown) {
                    item.title ="\u2191 По расширению"
                    fragment.sortByName(SortType.ExtensionUp)
                } else {
                    item.title ="\u2193 По расширению"
                    fragment.sortByName(SortType.ExtensionDown)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this@MainActivity.supportFragmentManager
        val fragments: List<Fragment> = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment.isVisible) return fragment
        }
        return null
    }

    //Callback формы на запрос разрешения
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