package com.shizq.bika

import com.android.build.api.dsl.CommonExtension

internal fun configureTestOptionsUnitTests(extension: CommonExtension<*, *, *, *, *, *>) {
    extension.apply {
        testOptions {
            // For Robolectric
            unitTests {
                isIncludeAndroidResources = true
            }
        }
    }
}
