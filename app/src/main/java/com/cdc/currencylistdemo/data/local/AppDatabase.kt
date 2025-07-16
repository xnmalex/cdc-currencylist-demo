import androidx.room.Database
import androidx.room.RoomDatabase
import com.cdc.currencylistdemo.data.local.dao.CurrencyDao
import com.cdc.currencylistdemo.data.local.entity.CurrencyInfoEntity

@Database(
    entities = [CurrencyInfoEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}