package fm.filemanager

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_file_list, container, false)

        val filesAndFolders  = getFilesAndFolders()
        if (filesAndFolders != null) {
            val listViewFiles : ListView = view.findViewById(R.id.listView) // находим список
            val fileAdapter = FileAdapter(activity as MainActivity, filesAndFolders, SortType.NameDown)//передаем аргументы в адаптер
            listViewFiles.adapter = fileAdapter
        }

        return view
    }

    private fun getFilesAndFolders(): Array<out File>? {
        val filepath = arguments?.getString("filepath", "") ?: ""
        val path = filepath.ifEmpty {
            Environment.getExternalStorageDirectory().path
        }
        val root = File(path)
        return root.listFiles()
    }
    fun sortByName(sortType: SortType) {
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