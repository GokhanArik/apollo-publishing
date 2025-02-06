package com.example.app

import android.app.Application
import com.generated.fragment.ProdentryFrag
import com.generated.fragment.StageEntryFrag
import com.generated.type.StageEntry

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Schema types
        val a: com.generated.type.StageFieldInput // Stage
        val b: com.generated.type.FieldInput      // Prod

        // Fragment types
        val c: StageEntryFrag // Stage
        val d: ProdentryFrag
    }
}