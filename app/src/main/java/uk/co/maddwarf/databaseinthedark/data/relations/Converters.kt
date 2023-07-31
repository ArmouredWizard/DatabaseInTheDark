package uk.co.maddwarf.databaseinthedark.data.relations

import android.util.Log
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.toString()
    }
    @TypeConverter
    fun toList(string: String): List<String> {
        val result = mutableListOf<String>()
        Log.d("FROM REPO", string + "Size: ${string.length}")
        val split = string.replace("[", "").replace("]", "").split(",")
        for (sub in split) {
            result.add(sub.trim())
        }
        result.forEach {
            if (it == ""){
                result.remove(it)
            }
        }
        Log.d("RESULT FROM REPO", result.toString()+"size: ${result.size}")
        return result
    }





}