import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gyroapp.GyroscopeRepository
import com.example.gyroapp.data.AppDatabase
import com.example.gyroapp.data.GyroscopeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GyroscopeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GyroscopeRepository
    val allGyroscopeData: LiveData<List<GyroscopeData>>

    // LiveData for the selected gyroscope data
    private val _selectedGyroscopeData = MutableLiveData<GyroscopeData?>()
    val selectedGyroscopeData: LiveData<GyroscopeData?> = _selectedGyroscopeData

    init {
        val database = AppDatabase.getInstance(application)
        repository = GyroscopeRepository(database.gyroscopeDataDao())
        allGyroscopeData = repository.allGyroscopeData
    }

    fun insertGyroscopeData(data: GyroscopeData) {
        viewModelScope.launch {
            repository.insertGyroscopeData(data)

            // After insertion, fetch the latest gyroscope data and update the LiveData
            fetchLatestGyroscopeData()
        }
    }

    fun updateGyroscopeData(data: GyroscopeData) {
        viewModelScope.launch {
            repository.updateGyroscopeData(data)

            // After update, fetch the latest gyroscope data and update the LiveData
            fetchLatestGyroscopeData()
        }
    }

    private suspend fun fetchLatestGyroscopeData() {
        // Fetch the latest gyroscope data from the repository
        val latestData = repository.getLatestGyroscopeData()

        // Update the LiveData with the latest data
        _selectedGyroscopeData.postValue(latestData)
    }
}
