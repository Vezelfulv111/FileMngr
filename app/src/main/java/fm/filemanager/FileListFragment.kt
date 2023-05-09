package fm.filemanager

import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.io.File


enum class SortType {
    NameDown,
    NameUp,
    SizeDown,
    SizeUp,
    ExtensionDown,
    ExtensionUp,
    DateDown,
    DateUp,
}
class FileListFragment : Fragment() {
    private var showChangedFilesflag = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_file_list, container, false)
        setHasOptionsMenu(true)

        val filesAndFolders  = getFilesAndFolders()
        if (filesAndFolders != null) {
            val listViewFiles : ListView = view.findViewById(R.id.listView) // находим список
            val fileAdapter = FileAdapter(activity as MainActivity, filesAndFolders, SortType.NameDown)//передаем аргументы в адаптер
            listViewFiles.adapter = fileAdapter
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val hasArrowDown = item.title.contains("\u2193")
        when (item.itemId) {
            R.id.changedFiles -> {
                if (item.isChecked) {
                    item.isChecked = false
                    showChangedFilesflag = false
                    sortFiles(SortType.NameDown)
                }
                else {
                    item.isChecked = true
                    showChangedFilesflag = true
                    sortFiles(SortType.NameDown)
                }
            }
            R.id.name -> {
                if (hasArrowDown) {
                    item.title ="\u2191 По имени"
                    sortFiles(SortType.NameDown)
                } else {
                    item.title ="\u2193 По имени"
                    sortFiles(SortType.NameUp)
                }
                return true
            }
            R.id.size -> {
                if (hasArrowDown) {
                    item.title ="\u2191 По размеру"
                    sortFiles(SortType.SizeUp)
                } else {
                    item.title ="\u2193 По размеру"
                    sortFiles(SortType.SizeDown)
                }
                return true
            }
            R.id.date -> {
                if (hasArrowDown) {
                    item.title ="\u2191 По дате создания"
                    sortFiles(SortType.DateUp)
                } else {
                    item.title ="\u2193 По дате создания"
                    sortFiles(SortType.DateDown)
                }
                return true
            }
            R.id.extension -> {
                if (hasArrowDown) {
                    item.title ="\u2191 По расширению"
                    sortFiles(SortType.ExtensionUp)
                } else {
                    item.title ="\u2193 По расширению"
                    sortFiles(SortType.ExtensionDown)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //функция для получения списка файлов по пути либо с аргументов либо с root директории
    //в случае галочки отображения измененных файлов, возвращает только измененные файлы
    private fun getFilesAndFolders(): Array<out File>? {
        val filepath = arguments?.getString("filepath", "") ?: ""
        val path = filepath.ifEmpty {
            Environment.getExternalStorageDirectory().path
        }
        var listOfFiles: Array<out File>? = null
        val root = File(path)
        if (showChangedFilesflag) {
            listOfFiles = root.listFiles()?.let { HashCheckout().compareHashWithBD(context as MainActivity, it) }
        } else {
            listOfFiles = root.listFiles()
        }
        return listOfFiles
    }
    private fun sortFiles(sortType: SortType) {
        val filesAndFolders  = getFilesAndFolders()
        val listViewFiles : ListView? = view?.findViewById(R.id.listView) // находим список
        val fileAdapter = filesAndFolders?.let { FileAdapter(
            activity as MainActivity,
            it,
            sortType
        ) }//передаем аргументы в адаптер
        listViewFiles?.adapter = fileAdapter
    }


}