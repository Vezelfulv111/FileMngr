package fm.filemanager

import java.io.File

//класс для сравнения файлов
class FileComparator(private val sortType: SortType) : Comparator<File> {
    override fun compare(file1: File, file2: File): Int {
        val isFile1Directory = file1.isDirectory
        val isFile2Directory = file2.isDirectory
        if (isFile1Directory && !isFile2Directory) {
            return -1// file1 - директория, file2 - файл
        } else if (!isFile1Directory && isFile2Directory) {
            return 1 // file1 - файл, file2 - директория
        } else {
            // file1 и file2 - оба файлы или оба директории, сортируем по имени
            return when (sortType) {//вариант сортировки зависит от sortType
                SortType.NameDown -> file1.name.compareTo(file2.name)
                SortType.NameUp -> file2.name.compareTo(file1.name)
                //Далее идут другие случаи сортировки. Проверяем что файлы не директории
                //В этих случаях сортируются только файлы
                //Директории сортируются по имени во всех этих случаях

                //сортировка по размеру
                SortType.SizeDown -> {
                    if (!isFile1Directory)
                        file1.length().compareTo(file2.length())
                    else
                        file1.name.compareTo(file2.name)
                }
                SortType.SizeUp -> {
                    if (!isFile1Directory)
                        file2.length().compareTo(file1.length())
                    else
                        file1.name.compareTo(file2.name)
                }
                //сортировка по дате
                SortType.DateDown -> {
                    if (!isFile1Directory)
                        file1.lastModified().compareTo(file2.lastModified())
                    else
                        file1.name.compareTo(file2.name)
                }
                SortType.DateUp -> {
                    if (!isFile1Directory)
                        file2.lastModified().compareTo(file1.lastModified())
                    else
                        file1.name.compareTo(file2.name)
                }
                //сортировка по расширению
                SortType.ExtensionDown -> {
                    if (!isFile1Directory) {
                        val extension1 = getFileExtension(file1)
                        val extension2 = getFileExtension(file2)
                        return extension1.compareTo(extension2)
                    }
                    else
                        file1.name.compareTo(file2.name)
                }
                SortType.ExtensionUp -> {
                    if (!isFile1Directory) {
                        val extension1 = getFileExtension(file1)
                        val extension2 = getFileExtension(file2)
                        return extension2.compareTo(extension1)
                    }
                    else
                        file1.name.compareTo(file2.name)
                }
            }
        }
    }

    //функция получения расширения файла
    private fun getFileExtension(file: File): String {
        val fileName = file.name
        val dotIndex = fileName.lastIndexOf(".")
        return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
    }
}