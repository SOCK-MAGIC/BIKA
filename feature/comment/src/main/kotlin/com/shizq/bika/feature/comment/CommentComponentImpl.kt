package com.shizq.bika.feature.comment

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.repository.CommentListRepository
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class CommentComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted comicId: String,
    private val network: BikaNetworkDataSource,
    private val commentListRepository: CommentListRepository,
) : CommentComponent,
    ComponentContext by componentContext {


    // "_id": "62b5d7b39612083487422cb6",
    // content": "550w贺电",
    // user": {
    // "_id": "5b695cd52c04e24caad1a74e",
    // "gender": "m",
    // "name": "冰茶_Official",
    // "title": "冰茶",
    // "verified": false,
    // "exp": 22008,
    // "level": 15,
    // "characters": [
    //   "knight"
    // ],
    // "role": "knight",
    // "avatar": {
    //   "originalName": "avatar.jpg",
    //   "path": "e9aaf0dc-d53e-4ba2-aca9-395940b74d6e.jpg",
    //   "fileServer": "https://storage1.picacomic.com"
    // },
    // "slogan": "時不時上傳本子給LSP看 事務繁忙 不定期更新",
    // "character": "https://pica-web.wakamoment.tk/images/halloween_m.png"
    // comic": "602bf6ee1001c26ee70e6822",
    // totalComments": 6,
    // isTop": true,
    // hide": false,
    // created_at": "2022-06-24T15:26:43.101Z",
    // likesCount": 2,
    // commentsCount": 6,
    // isLiked": false
    init {
        componentScope.launch {
            network.getComments(comicId, 1)
        }
    }
 override  val pagingDataFlow = commentListRepository(comicId)
    @AssistedFactory
    interface Factory : CommentComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            comicId: String,
        ): CommentComponentImpl
    }
}
