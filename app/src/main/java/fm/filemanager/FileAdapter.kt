package fm.filemanager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class FileAdapter(
    var context: Context,
    var filesAndFolders: Array<out File>,
    var sortType: SortType
) : BaseAdapter() {

    override fun getCount(): Int {
        return filesAndFolders.size
    }

    override fun getItem(position: Int): Any {
        return filesAndFolders[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, View: View?, parent: ViewGroup?): View {

        filesAndFolders.sortWith(FileNameComparator(sortType))
        //reversed() не использовался тк данный метод не учитывает что папки все равно должны быть вначале

        var convertView: View? = View
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.table_adapter_layout, parent, false)

        val selectedFile = filesAndFolders[position]
        val icon = convertView?.findViewById(R.id.icon) as ImageView
        val fileSize = convertView.findViewById(R.id.fileSize) as TextView
        val fileTime = convertView.findViewById(R.id.timeofEdit) as TextView
        val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault())

        setImage(icon, selectedFile)//выбор иконки для файла
        if (!selectedFile.isDirectory) {
            fileSize.text = fileSize(selectedFile)
            fileTime.text = sdf.format(selectedFile.lastModified())
        }
        else {
            fileSize.text = ""
            fileTime.text = ""
        }
        val fileName = convertView.findViewById(R.id.fileName) as TextView
        fileName.text = selectedFile.name

        val fileItem = convertView.findViewById(R.id.fileItem) as LinearLayout
        fileItem.setOnClickListener() {
            if (selectedFile.isDirectory) {
                val path = selectedFile.absolutePath
                (context as MainActivity).changeFragmentView(path)
            }
            else {
                //открытие файла
                val uri = Uri.fromFile(filesAndFolders[position])
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, getMimeType(filesAndFolders[position]))
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                val intentChooser = Intent.createChooser(intent, "Выберите приложение")
                StrictMode.VmPolicy.Builder().build().apply {StrictMode.setVmPolicy(this)}
                try {
                    (context as MainActivity).startActivity(intentChooser)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return convertView
    }

    private fun getMimeType(file: File): String {
        var type = "*/*"
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
        }
        return type
    }

    private fun fileSize(file : File): String {
        val fileSizeInByte = file.length()
        val fileSizeInKb = fileSizeInByte / 1024.0
        val fileSizeInMb = fileSizeInKb / 1024.0
        val fileSizeInGb = fileSizeInMb / 1024.0
        if (fileSizeInByte > 1024) {
            if (fileSizeInKb > 1024) {
                if (fileSizeInMb > 1024)
                    return " - ${(fileSizeInGb * 100).roundToInt() / 100.0} ГБ"
                else
                    return " - ${(fileSizeInMb * 100).roundToInt() / 100.0} МБ"
            }
            else
                return " - ${(fileSizeInKb * 100).roundToInt() / 100.0} КБ"
        } else
            return " - $fileSizeInByte Б"
    }

    private fun setImage(image : ImageView, file : File) {
        if (!file.isDirectory) {
            val fileName = file.name
            val index = fileName.lastIndexOf(".")
            if (index == -1) {
                image.setImageResource(R.drawable.file_icon56)
                return
            }
            when (fileName.substring(index)) {
                ".doc" -> image.setImageResource(R.drawable.icon_doc)
                ".docx"-> image.setImageResource(R.drawable.icon_docx)
                ".jpg"-> image.setImageResource(R.drawable.icon_jpg)
                ".mp3"-> image.setImageResource(R.drawable.icon_mp3)
                ".pdf"-> image.setImageResource(R.drawable.icon_pdf)
                ".png"-> image.setImageResource(R.drawable.icon_png)
                ".zip"-> image.setImageResource(R.drawable.icon_zip_folder)
                ".flac"-> image.setImageResource(R.drawable.icon_flac)
                ".mp4"-> image.setImageResource(R.drawable.icon_mp4)
                ".txt"-> image.setImageResource(R.drawable.icon_txt)
                else -> {
                    image.setImageResource(R.drawable.file_icon56)
                }
            }
        }
        else
            image.setImageResource(R.drawable.folder_icon56)
    }

}