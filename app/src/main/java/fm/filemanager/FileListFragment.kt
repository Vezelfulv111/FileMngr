package fm.filemanager

import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import java.io.File


class FileListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_file_list, container, false)

        val filepath = arguments?.getString("filepath", "") ?: ""
        val path = filepath.ifEmpty {
            Environment.getExternalStorageDirectory().path
        }

        val root =  File(path)
        val filesAndFolders  = root.listFiles()
        if (filesAndFolders != null) {
            val listViewFiles : ListView = view.findViewById(R.id.listView) // находим список
            val fileAdapter = FileAdapter(activity as MainActivity, filesAndFolders)//передаем аргументы в адаптер
            listViewFiles.adapter = fileAdapter
        }
        return view
    }


}