import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.practica1.data.City

class FavoriteCityDbHelper(context: Context): SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
){
    companion object {
        private const val DATABASE_NAME = "favoriteCity.db"
        private const val DATABASE_VERSION = 1

        //Tabla
        const val TABLE_CITY = "city"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"

        //SQL CREAR TABLA
        private const val CREATE_TABLE_CITY = """
            CREATE TABLE $TABLE_CITY (
               $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
               $COLUMN_NAME TEXT NOT NULL UNIQUE
           )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_CITY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar la tabla anterior si existe y crear una nueva
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CITY")
        onCreate(db)
    }

    fun insertCity(cityName: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, cityName)
        }
        return db.insert(TABLE_CITY, null, values)
    }

    fun getAllCities(): List<City> {
        val cities = mutableListOf<City>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_CITY,
            arrayOf(COLUMN_ID, COLUMN_NAME),
            null,
            null,
            null,
            null,
            "$COLUMN_NAME ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))

                cities.add(City(id, name))
            }
        }
        return cities
    }

    fun deleteCityById(id: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_CITY,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteCityByName(cityName: String): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_CITY,
            "$COLUMN_NAME = ?",
            arrayOf(cityName)
        )
    }

    fun isFavorite(cityName: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CITY,
            arrayOf(COLUMN_NAME),
            "$COLUMN_NAME = ?",
            arrayOf(cityName),
            null,
            null,
            null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }
}