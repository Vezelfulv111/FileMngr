package fm.filemanager

import java.io.File


class FileNameComparator : Comparator<File> {
    override fun compare(file1: File, file2: File): Int {
        val isFile1Directory = file1.isDirectory
        val isFile2Directory = file2.isDirectory
        if (isFile1Directory && !isFile2Directory) {
            return -1// file1 - директория, file2 - файл
        } else if (!isFile1Directory && isFile2Directory) {
            return 1 // file1 - файл, file2 - директория
        } else {
            return file1.name.compareTo(file2.name)
            // file1 и file2 - оба файлы или оба директории, сортируем по имени
        }
    }
}