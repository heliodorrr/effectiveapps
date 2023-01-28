package com.effectiveapps.app.ui.explorer

class FilterBox<T>(
    val onFiltersUpdated: ((T)->Boolean)->Unit
) {
    private val filtersMap = mutableMapOf<String,(T)->Boolean>()

    operator fun set(key: String, filter: (T)->Boolean) {
        filtersMap[key] = filter
        onFiltersUpdated { t ->
            filtersMap.forEach { entry->
                if (!entry.value(t)) return@onFiltersUpdated false
            }
            return@onFiltersUpdated true
        }
    }
}