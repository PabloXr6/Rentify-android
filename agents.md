# AGENTS.md — Rentify Project Intelligence Guide

> **Untuk AI Agent (Gemini in Android Studio):** Dokumen ini adalah sumber kebenaran tunggal
> (*single source of truth*) untuk proyek Rentify. Baca seluruh dokumen ini sebelum
> menghasilkan kode, menyarankan refactor, atau menjawab pertanyaan tentang proyek.

---

## 1. Identitas Proyek

| Key | Value |
|-----|-------|
| **Nama Aplikasi** | Rentify |
| **Deskripsi** | Sistem informasi rental kendaraan berbasis Android native |
| **Konteks** | Proyek akhir mata kuliah Pemrograman Bergerak, Informatika UNRAM |
| **Platform** | Android Native (Kotlin) |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 34 (Android 14) |
| **Bahasa** | Kotlin 1.9+ |
| **Build System** | Gradle (Kotlin DSL) + KSP |

---

## 2. Arsitektur Wajib: MVVM + Repository Pattern

Seluruh kode HARUS mengikuti pola **MVVM (Model-View-ViewModel)** dengan **Repository Pattern**.
Jangan pernah menyarankan atau menghasilkan kode yang melanggar pemisahan layer berikut:

```
┌─────────────────────────────────────────────────────────────┐
│  VIEW LAYER                                                 │
│  Activity / Fragment / XML Layout                           │
│  • Hanya mengobservasi LiveData/StateFlow dari ViewModel    │
│  • Tidak boleh mengakses Repository atau DataSource langsung│
└──────────────────────┬──────────────────────────────────────┘
                       │ observes / calls
┌──────────────────────▼──────────────────────────────────────┐
│  VIEWMODEL LAYER                                            │
│  *ViewModel : AndroidViewModel / ViewModel                  │
│  • Menyimpan UI state (LiveData / StateFlow)                │
│  • Memanggil Repository, TIDAK akses DAO/API secara langsung│
│  • Menggunakan viewModelScope untuk Coroutine               │
└──────────────────────┬──────────────────────────────────────┘
                       │ calls
┌──────────────────────▼──────────────────────────────────────┐
│  REPOSITORY LAYER                                           │
│  *Repository                                                │
│  • Single Source of Truth                                   │
│  • Koordinasi antara RemoteDataSource & LocalDataSource     │
│  • Semua fungsi bersifat suspend fun                        │
└──────┬───────────────────────────────────────┬──────────────┘
       │                                       │
┌──────▼────────────┐               ┌──────────▼─────────────┐
│  REMOTE SOURCE    │               │  LOCAL SOURCE          │
│  Retrofit / API   │               │  Room DAO              │
│  Firebase         │               │  SharedPreferences     │
└───────────────────┘               └────────────────────────┘
```

### Aturan Keras (Hard Rules) untuk Code Generation

- ✅ ViewModel BOLEH memanggil `repository.someFunction()`
- ✅ Repository BOLEH memanggil `dao.insert()` atau `apiService.getVehicles()`
- ❌ Fragment/Activity TIDAK BOLEH memanggil `dao` atau `apiService` secara langsung
- ❌ ViewModel TIDAK BOLEH mengimport `android.content.Context` (gunakan Application jika perlu)
- ❌ Repository TIDAK BOLEH mengimport class UI (`Fragment`, `Activity`, `View`)
- ❌ Jangan gunakan `AsyncTask` — gunakan **Kotlin Coroutines**
- ❌ Jangan gunakan `findViewById` — gunakan **ViewBinding**

---

## 3. Struktur Package

```
com.unram.rentify/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── FavoriteDao.kt
│   │   ├── database/
│   │   │   └── RentifyDatabase.kt
│   │   └── entity/
│   │       └── FavoriteEntity.kt
│   ├── remote/
│   │   ├── api/
│   │   │   ├── ApiService.kt
│   │   │   └── ApiClient.kt
│   │   └── dto/
│   │       ├── VehicleDto.kt
│   │       ├── ArticleDto.kt
│   │       └── BookingDto.kt
│   └── repository/
│       ├── VehicleRepository.kt
│       ├── FavoriteRepository.kt
│       ├── ArticleRepository.kt
│       └── BookingRepository.kt
├── domain/
│   └── model/
│       ├── Vehicle.kt
│       ├── Article.kt
│       └── Booking.kt
├── ui/
│   ├── auth/
│   │   ├── LoginFragment.kt
│   │   ├── RegisterFragment.kt
│   │   └── AuthViewModel.kt
│   ├── home/
│   │   ├── HomeFragment.kt
│   │   ├── HomeViewModel.kt
│   │   └── VehicleAdapter.kt
│   ├── detail/
│   │   ├── DetailVehicleFragment.kt
│   │   ├── DetailViewModel.kt
│   │   └── BookingBottomSheet.kt
│   ├── favorites/
│   │   ├── FavoritesFragment.kt
│   │   ├── FavoritesViewModel.kt
│   │   └── FavoriteAdapter.kt
│   ├── explore/
│   │   ├── ExploreFragment.kt
│   │   ├── ExploreViewModel.kt
│   │   ├── ArticleAdapter.kt
│   │   └── DetailArticleFragment.kt
│   ├── history/
│   │   ├── HistoryFragment.kt
│   │   ├── HistoryViewModel.kt
│   │   └── BookingAdapter.kt
│   └── profile/
│       ├── ProfileFragment.kt
│       └── ProfileViewModel.kt
├── utils/
│   ├── SessionManager.kt        # Wrapper SharedPreferences
│   ├── NetworkMonitor.kt        # Cek koneksi internet
│   ├── WhatsAppHelper.kt        # Generate WA deeplink
│   └── Extensions.kt            # Kotlin extension functions
└── di/
    ├── AppModule.kt
    ├── DatabaseModule.kt
    └── NetworkModule.kt
```

---

## 4. Tech Stack & Dependencies

### Selalu gunakan library berikut (sudah dikonfigurasi di `build.gradle.kts`):

```kotlin
// Arsitektur & Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
implementation("androidx.activity:activity-ktx:1.8.2")
implementation("androidx.fragment:fragment-ktx:1.6.2")

// Navigation Component
implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

// Room Database (WAJIB untuk Favorites)
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")   // Gunakan KSP, bukan kapt

// Kotlin Coroutines (WAJIB untuk semua operasi async)
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

// Retrofit + OkHttp (Network)
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Firebase
implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-storage-ktx")

// Hilt (Dependency Injection)
implementation("com.google.dagger:hilt-android:2.50")
ksp("com.google.dagger:hilt-android-compiler:2.50")

// Image Loading
implementation("com.github.bumptech.glide:glide:4.16.0")

// UI
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.security:security-crypto:1.1.0-alpha06") // EncryptedSharedPreferences
```

---

## 5. Ketentuan Teknis Wajib & Cara Implementasinya

### 5.1 RecyclerView

**Wajib digunakan pada:** Home, Favorites, Explore, History.

```kotlin
// SELALU gunakan ListAdapter + DiffUtil, bukan RecyclerView.Adapter biasa
class VehicleAdapter(
    private val onItemClick: (Vehicle) -> Unit,
    private val onFavoriteClick: (Vehicle) -> Unit
) : ListAdapter<Vehicle, VehicleAdapter.VehicleViewHolder>(DiffCallback()) {

    class VehicleViewHolder(private val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: Vehicle) { /* ... */ }
    }

    class DiffCallback : DiffUtil.ItemCallback<Vehicle>() {
        override fun areItemsTheSame(old: Vehicle, new: Vehicle) = old.id == new.id
        override fun areContentsTheSame(old: Vehicle, new: Vehicle) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VehicleViewHolder(ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) =
        holder.bind(getItem(position))
}
```

### 5.2 Room Database

**Hanya untuk:** menyimpan data Favorites secara offline.

```kotlin
// Entity
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val vehicleId: String,
    val name: String,
    val imageUrl: String,
    val pricePerDay: Long,
    val category: String,
    val insertedAt: Long = System.currentTimeMillis()
)

// DAO — semua fungsi HARUS suspend atau mengembalikan Flow/LiveData
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorites ORDER BY insertedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE vehicleId = :id)")
    fun isFavorite(id: String): Flow<Boolean>
}

// Database
@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class RentifyDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DATABASE_NAME = "rentify_db"
    }
}
```

### 5.3 SharedPreferences (SessionManager)

**Gunakan `EncryptedSharedPreferences` untuk token.**

```kotlin
class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    private val prefs = EncryptedSharedPreferences.create(
        context, "rentify_secure_prefs", masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var userId: String?
        get() = prefs.getString(KEY_USER_ID, null)
        set(value) = prefs.edit().putString(KEY_USER_ID, value).apply()

    var userToken: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    fun clearSession() = prefs.edit().clear().apply()

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_TOKEN = "user_token"
    }
}
```

### 5.4 Kotlin Coroutines

**Aturan dispatcher:**

```kotlin
// Di Repository — semua fungsi HARUS suspend
class VehicleRepository @Inject constructor(
    private val apiService: ApiService,         // Remote
    private val favoriteDao: FavoriteDao        // Local
) {
    // Operasi network di IO dispatcher
    suspend fun getVehicles(): Result<List<Vehicle>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getVehicles()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Room Flow sudah di IO, tidak perlu withContext
    fun getFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()
}

// Di ViewModel — gunakan viewModelScope
class HomeViewModel @HiltViewModel constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _vehicles = MutableStateFlow<UiState<List<Vehicle>>>(UiState.Loading)
    val vehicles: StateFlow<UiState<List<Vehicle>>> = _vehicles.asStateFlow()

    fun fetchVehicles() {
        viewModelScope.launch {
            _vehicles.value = UiState.Loading
            vehicleRepository.getVehicles()
                .onSuccess { _vehicles.value = UiState.Success(it) }
                .onFailure { _vehicles.value = UiState.Error(it.message ?: "Terjadi kesalahan") }
        }
    }
}

// UiState sealed class — gunakan ini di seluruh proyek
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

### 5.5 WhatsApp Integration

```kotlin
object WhatsAppHelper {
    private const val ADMIN_PHONE = "628xxxxxxxxxx" // Ganti dengan nomor admin

    fun openWhatsApp(context: Context, booking: BookingRequest): Boolean {
        val message = buildMessage(booking)
        val encodedMessage = Uri.encode(message)
        val url = "https://wa.me/$ADMIN_PHONE?text=$encodedMessage"
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    private fun buildMessage(b: BookingRequest): String = """
        Halo Admin Rentify,
        Saya ingin melakukan pemesanan kendaraan dengan detail berikut:
        
        Nama Pemesan   : ${b.customerName}
        No. HP         : ${b.customerPhone}
        Kendaraan      : ${b.vehicleName} - ${b.category}
        Tanggal Mulai  : ${b.startDate}
        Tanggal Selesai: ${b.endDate}
        Durasi Sewa    : ${b.durationDays} hari
        Total Estimasi : Rp ${b.totalPrice}
        
        Mohon konfirmasinya. Terima kasih.
    """.trimIndent()
}

data class BookingRequest(
    val customerName: String,
    val customerPhone: String,
    val vehicleName: String,
    val category: String,
    val startDate: String,
    val endDate: String,
    val durationDays: Int,
    val totalPrice: String
)
```

---

## 6. Navigation & Screen Map

### Bottom Navigation (4 Tab)

| Tab | Fragment | Destination ID |
|-----|----------|----------------|
| Home | `HomeFragment` | `nav_home` |
| Favorites | `FavoritesFragment` | `nav_favorites` |
| Explore | `ExploreFragment` | `nav_explore` |
| Profile | `ProfileFragment` | `nav_profile` |

### Seluruh Screen & Nav Graph

```
[SplashActivity]
    └─> cek SessionManager.isLoggedIn
        ├─> TRUE  ──> [MainActivity] (BottomNav)
        └─> FALSE ──> [AuthActivity]
                          ├── LoginFragment
                          └── RegisterFragment

[MainActivity] — nav_graph_main.xml
    ├── HomeFragment
    │     └── DetailVehicleFragment (args: vehicleId: String)
    │               └── BookingBottomSheet (args: vehicleId, vehicleName, pricePerDay)
    ├── FavoritesFragment
    │     └── DetailVehicleFragment (shared nav action)
    ├── ExploreFragment
    │     └── DetailArticleFragment (args: articleId: String)
    └── ProfileFragment
          ├── EditProfileFragment
          └── HistoryFragment
```

### Safe Args yang Wajib Didefinisikan

```xml
<!-- nav_graph_main.xml -->
<fragment android:id="@+id/detailVehicleFragment" ...>
    <argument android:name="vehicleId" app:argType="string" />
</fragment>

<fragment android:id="@+id/detailArticleFragment" ...>
    <argument android:name="articleId" app:argType="string" />
</fragment>
```

---

## 7. Firebase Firestore — Struktur Data

### Koleksi & Dokumen

```
firestore/
├── vehicles/                    # Koleksi kendaraan
│   └── {vehicleId}/
│       ├── id: String
│       ├── name: String
│       ├── category: String     # "motor" | "mobil" | "minibus"
│       ├── description: String
│       ├── pricePerDay: Long    # dalam Rupiah
│       ├── isAvailable: Boolean
│       ├── transmission: String # "manual" | "matic"
│       ├── capacity: Int
│       ├── year: Int
│       └── images: List<String> # URL Firebase Storage
│
├── users/                       # Profil pengguna
│   └── {userId}/
│       ├── uid: String
│       ├── name: String
│       ├── email: String
│       ├── phone: String
│       └── photoUrl: String
│
├── bookings/                    # Riwayat pemesanan
│   └── {bookingId}/
│       ├── id: String
│       ├── userId: String
│       ├── vehicleId: String
│       ├── vehicleName: String
│       ├── startDate: Timestamp
│       ├── endDate: Timestamp
│       ├── durationDays: Int
│       ├── totalPrice: Long
│       ├── status: String       # "pending" | "confirmed" | "done" | "cancelled"
│       └── createdAt: Timestamp
│
└── articles/                    # Konten Explore
    └── {articleId}/
        ├── id: String
        ├── title: String
        ├── content: String
        ├── thumbnailUrl: String
        └── publishedAt: Timestamp
```

---

## 8. Pola Error Handling Standar

Gunakan pola ini di **seluruh Repository dan ViewModel** secara konsisten:

```kotlin
// Di Repository — wrapping dengan Result
suspend fun getVehicles(): Result<List<Vehicle>> = withContext(Dispatchers.IO) {
    runCatching {
        apiService.getVehicles().map { it.toDomain() }
    }
}

// Di ViewModel — mapping Result ke UiState
fun loadVehicles() = viewModelScope.launch {
    _uiState.value = UiState.Loading
    vehicleRepository.getVehicles()
        .onSuccess { _uiState.value = UiState.Success(it) }
        .onFailure { _uiState.value = UiState.Error(it.message ?: "Error tidak diketahui") }
}

// Di Fragment — observing UiState
viewLifecycleOwner.lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { state ->
            when (state) {
                is UiState.Loading -> showLoading()
                is UiState.Success -> showData(state.data)
                is UiState.Error   -> showError(state.message)
            }
        }
    }
}
```

---

## 9. Konvensi Penamaan

| Elemen | Konvensi | Contoh |
|--------|----------|--------|
| Fragment | `NamaFragment` | `HomeFragment` |
| ViewModel | `NamaViewModel` | `HomeViewModel` |
| Repository | `NamaRepository` | `VehicleRepository` |
| DAO | `NamaDao` | `FavoriteDao` |
| Entity (Room) | `NamaEntity` | `FavoriteEntity` |
| DTO (API) | `NamaDto` | `VehicleDto` |
| Domain Model | Nama saja | `Vehicle`, `Booking` |
| Adapter | `NamaAdapter` | `VehicleAdapter` |
| Layout Fragment | `fragment_nama` | `fragment_home.xml` |
| Layout Item RV | `item_nama` | `item_vehicle.xml` |
| Layout Activity | `activity_nama` | `activity_main.xml` |
| LiveData (private) | `_namaCamelCase` | `_vehicles` |
| LiveData (public) | `namaCamelCase` | `vehicles` |
| Hilt Module | `NamaModule` | `DatabaseModule` |
| String resource | `snake_case` | `label_search_hint` |
| Color resource | `nama_deskriptif` | `color_primary_brand` |

---

## 10. Hal yang Dilarang (Never Do)

```kotlin
// ❌ JANGAN — akses database di MainThread
val favorites = database.favoriteDao().getAllFavorites() // akan crash

// ✅ BENAR — gunakan Flow atau suspend di Coroutine
val favorites: Flow<List<FavoriteEntity>> = dao.getAllFavorites()

// ❌ JANGAN — akses network di MainThread
val response = apiService.getVehicles().execute()

// ✅ BENAR — gunakan suspend fun di Dispatchers.IO
val response = withContext(Dispatchers.IO) { apiService.getVehicles() }

// ❌ JANGAN — leak context di ViewModel
class HomeViewModel(private val context: Context) : ViewModel() { }

// ✅ BENAR — gunakan Application context via Hilt jika memang perlu
class HomeViewModel @HiltViewModel constructor(
    @ApplicationContext private val context: Context
) : ViewModel()

// ❌ JANGAN — gunakan AsyncTask (deprecated)
object : AsyncTask<Void, Void, List<Vehicle>>() { }.execute()

// ❌ JANGAN — hardcode string di kode Kotlin
Toast.makeText(context, "Berhasil disimpan", Toast.LENGTH_SHORT).show()

// ✅ BENAR — gunakan string resource
Toast.makeText(context, getString(R.string.msg_save_success), Toast.LENGTH_SHORT).show()
```

---

## 11. Fitur Utama & Lokasi Implementasinya

| Fitur | Fragment | ViewModel | Repository | Catatan Khusus |
|-------|----------|-----------|------------|----------------|
| Katalog Kendaraan | `HomeFragment` | `HomeViewModel` | `VehicleRepository` | RecyclerView + GridLayout 2 kolom |
| Pencarian Real-time | `HomeFragment` | `HomeViewModel` | — | Filter di `currentList` Adapter |
| Filter Kendaraan | `FilterBottomSheet` | `HomeViewModel` | — | BottomSheetDialogFragment |
| Detail Kendaraan | `DetailVehicleFragment` | `DetailViewModel` | `VehicleRepository` | ViewPager2 untuk galeri foto |
| Favorit (Offline) | `FavoritesFragment` | `FavoritesViewModel` | `FavoriteRepository` | **Room Database** — bukan Firebase |
| Pemesanan WA | `BookingBottomSheet` | `DetailViewModel` | `BookingRepository` | `WhatsAppHelper.openWhatsApp()` |
| Riwayat Pesanan | `HistoryFragment` | `HistoryViewModel` | `BookingRepository` | Data dari Firebase |
| Artikel Wisata | `ExploreFragment` | `ExploreViewModel` | `ArticleRepository` | Data dari Firebase |
| Manajemen Profil | `ProfileFragment` | `ProfileViewModel` | — | Firebase Auth + Firestore |
| Login/Register | `LoginFragment` | `AuthViewModel` | — | Firebase Authentication |

---

## 12. Contoh Skeleton Lengkap (Copy-Paste Ready)

### Fragment dengan ViewBinding + StateFlow

```kotlin
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val adapter = VehicleAdapter(
        onItemClick = { vehicle ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToDetail(vehicle.id)
            )
        },
        onFavoriteClick = { vehicle -> viewModel.toggleFavorite(vehicle) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        observeViewModel()
        viewModel.fetchVehicles()
    }

    private fun setupRecyclerView() {
        binding.rvVehicles.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vehicles.collect { state ->
                    when (state) {
                        is UiState.Loading -> binding.progressBar.isVisible = true
                        is UiState.Success -> {
                            binding.progressBar.isVisible = false
                            adapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.progressBar.isVisible = false
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Wajib untuk mencegah memory leak
    }
}
```

### Hilt Module untuk Database

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RentifyDatabase =
        Room.databaseBuilder(context, RentifyDatabase::class.java, RentifyDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFavoriteDao(database: RentifyDatabase): FavoriteDao = database.favoriteDao()
}
```

---

*Dokumen ini di-generate dari PRD Rentify v1.0.0 | Informatika UNRAM | Pemrograman Bergerak 2024/2025*