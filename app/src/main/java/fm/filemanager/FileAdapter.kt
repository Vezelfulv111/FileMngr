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

        val selectedFile = filesAndFolders[position];

        val icon = convertView?.findViewById(R.id.icon) as ImageView
        val textView = convertView.findViewById(R.id.fileName) as TextView
        textView.text = selectedFile.name

        val fileItem = convertView.findViewById(R.id.fileItem) as LinearLayout
        fileItem.setOnClickListener() {
            if (selectedFile.isDirectory) {
            }
            else {
                //TODO() open the fle
            }
        }


        return convertView
    }

}