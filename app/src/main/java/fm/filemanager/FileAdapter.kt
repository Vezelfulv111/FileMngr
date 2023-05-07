package fm.filemanager

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.io.File

class FileAdapter(var context: Context, var filesAndFolders:  Array<out File>) : BaseAdapter() {


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
        var convertView: View? = View
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.table_adapter_layout, parent, false)
        }

        val selectedFile = filesAndFolders[position]

        val icon = convertView?.findViewById(R.id.icon) as ImageView
        if (!selectedFile.isDirectory) {
            setImage(icon, selectedFile.name)//выбор иконки для файла
        }
        val textView = convertView.findViewById(R.id.fileName) as TextView
        textView.text = selectedFile.name

        val fileItem = convertView.findViewById(R.id.fileItem) as LinearLayout
        fileItem.setOnClickListener() {
            if (selectedFile.isDirectory) {
                val path = selectedFile.absolutePath
                (context as MainActivity).changeFragmentView(path)
            }
            else {
                //TODO() open the fle
            }
        }
        return convertView
    }

    private fun setImage(image : ImageView, fileName : String) {
        when (fileName.substring(fileName.lastIndexOf("."))) {
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

}