package com.shizq.bika.feature.comment

import com.arkivanov.decompose.ComponentContext

interface CommentComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): CommentComponent
    }
}
