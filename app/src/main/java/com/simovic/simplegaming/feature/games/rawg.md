# RAWG Integration Plan

Replace the placeholder game search API with RAWG.io.

API key: `4f38776b81314893a57a97b86af3eb90`

---

## What changes and why

The placeholder API was designed around a generic contract (`q`, `limit`, `imageUrl`).
RAWG has a different base URL, different auth mechanism, and different field names.
The existing base Retrofit instance cannot be reused — it points to `BuildConfig.apiBaseUrl`
and its interceptor appends `api_key` + `format=json` to every request. RAWG expects `key`.
A dedicated RAWG Retrofit instance is needed.

---

## RAWG endpoint

```
GET https://api.rawg.io/api/games
    ?key=4f38776b81314893a57a97b86af3eb90
    &search=hollow+knight
    &page_size=10
    &search_precise=true
```

`search_precise=true` — disables fuzzy matching. Without it RAWG returns loosely related
results that aren't useful in a search-as-you-type flow.

---

## Response shape

```json
{
  "results": [
    {
      "id": 123,
      "name": "Hollow Knight",
      "background_image": "https://..."
    }
  ]
}
```

Key differences from the placeholder contract:
- `id` is an **Int**, not a String
- Image field is `background_image`, not `imageUrl`
- `background_image` can be `null` — no image for some games

---

## Files to change

### 1. `gradle.properties` (not committed to git)

Add the API key so it flows into `BuildConfig`:

```
rawgApiKey=4f38776b81314893a57a97b86af3eb90
```

### 2. `app/build.gradle.kts`

Add a new `BuildConfig` field alongside the existing ones:

```kotlin
buildConfigField("String", "rawgApiKey", "\"${project.findProperty("rawgApiKey") ?: ""}\"")
```

### 3. `base/AppKoinModule.kt`

Add a named RAWG OkHttpClient and Retrofit instance. The RAWG interceptor appends
`key` as a query parameter. It does not reuse the existing `AuthenticationInterceptor`
(which appends `api_key` + `format=json` — wrong for RAWG).

```kotlin
single(named("rawgOkHttp")) {
    OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) addInterceptor(get<HttpLoggingInterceptor>())
            addInterceptor { chain ->
                val url = chain.request().url.newBuilder()
                    .addQueryParameter("key", BuildConfig.rawgApiKey)
                    .build()
                chain.proceed(chain.request().newBuilder().url(url).build())
            }
        }
        .build()
}

single(named("rawgRetrofit")) {
    Retrofit.Builder()
        .baseUrl("https://api.rawg.io/api/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(get(named("rawgOkHttp")))
        .addCallAdapterFactory(ApiResultAdapterFactory())
        .build()
}
```

The `json` instance needs to be extracted into a `single<Json>` so both Retrofit instances
can share it, or the RAWG Retrofit can create its own inline.

### 4. `feature/games/data/DataModule.kt`

Change the `GamesApiService` binding to use the named RAWG Retrofit:

```kotlin
// Before
single { get<Retrofit>().create(GamesApiService::class.java) }

// After
single { get<Retrofit>(named("rawgRetrofit")).create(GamesApiService::class.java) }
```

### 5. `feature/games/data/datasource/api/service/GamesApiService.kt`

Update the endpoint parameters to match RAWG:

```kotlin
@GET("games")
suspend fun searchGames(
    @Query("search") query: String,
    @Query("page_size") pageSize: Int = 10,
    @Query("search_precise") searchPrecise: Boolean = true,
): ApiResult<SearchGamesResponse>
```

### 6. `feature/games/data/datasource/api/model/GameApiModel.kt`

Match RAWG field names. `id` becomes Int. `background_image` is nullable.

```kotlin
@Serializable
data class GameApiModel(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("background_image") val backgroundImage: String? = null,
)
```

### 7. `feature/games/data/mapper/GameMapper.kt`

Convert `id` to String, map `background_image` to `imageUrl` with null fallback:

```kotlin
fun toDomain(model: GameApiModel): Game =
    Game(
        id = model.id.toString(),
        name = model.name,
        imageUrl = model.backgroundImage ?: "",
    )
```

---

## What does NOT change

- `SearchGamesResponse` — still `{ "results": [...] }`, same shape
- `Game` domain model — `id: String`, `name: String`, `imageUrl: String` — untouched
- `GameSearchRepository` interface — untouched
- `GameSearchRepositoryImpl` — untouched
- `SearchGamesUseCase` — untouched
- Everything in presentation — untouched

The boundary holds. RAWG is a data-layer detail.

---

## Order of implementation

1. `gradle.properties` — add `rawgApiKey`
2. `build.gradle.kts` — add `BuildConfig` field, sync Gradle
3. `AppKoinModule.kt` — add named RAWG OkHttpClient + Retrofit
4. `GameApiModel.kt` — update fields
5. `GameMapper.kt` — update mapping
6. `GamesApiService.kt` — update endpoint parameters
7. `DataModule.kt` — point service at named RAWG Retrofit
8. Build and verify search returns real RAWG results
