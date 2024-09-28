package com.shizq.bika.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesBean(
    val categories: List<Category>
) {
    @Serializable
    data class Category(
        @SerialName("_id")
        val _id: String,
        val active: Boolean,
        val description: String,
        val isWeb: Boolean,//
        val link: String,//
        val thumb: Thumb,
        var title: String,//
        var imageRes: Int?
    ) {
        @Serializable
        data class Thumb(
            val fileServer: String,
            val originalName: String,
            val path: String
        )
    }
}
//                      {
//
//                                   "code": 200,
//                                   "message": "success",
//                                   "data": {
//                                     "categories": [
//                                       {
//                                         "title": "援助嗶咔",
//                                         "thumb": {
//                                           "originalName": "help.jpg",
//                                           "path": "help.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": true,
//                                         "active": true,
//                                         "link": "https://donate.bidobido.xyz"
//                                       },
//                                       {
//                                         "title": "嗶咔小禮物",
//                                         "thumb": {
//                                           "originalName": "picacomic-gift.jpg",
//                                           "path": "picacomic-gift.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": true,
//                                         "link": "https://gift-web.bidobido.xyz",
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "小電影",
//                                         "thumb": {
//                                           "originalName": "av.jpg",
//                                           "path": "av.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": true,
//                                         "link": "https://adult-movie.bidobido.xyz",
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "小里番",
//                                         "thumb": {
//                                           "originalName": "h.jpg",
//                                           "path": "h.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": true,
//                                         "link": "https://adult-animate.bidobido.xyz",
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "嗶咔畫廊",
//                                         "thumb": {
//                                           "originalName": "picacomic-paint.jpg",
//                                           "path": "picacomic-paint.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": true,
//                                         "link": "https://paint-web.bidobido.xyz",
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "嗶咔商店",
//                                         "thumb": {
//                                           "originalName": "picacomic-shop.jpg",
//                                           "path": "picacomic-shop.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": true,
//                                         "link": "https://online-shop-web.bidobido.xyz",
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "大家都在看",
//                                         "thumb": {
//                                           "originalName": "every-see.jpg",
//                                           "path": "every-see.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": false,
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "大濕推薦",
//                                         "thumb": {
//                                           "originalName": "recommendation.jpg",
//                                           "path": "tobs/4b9a2dfd-bb45-4b18-b10d-a8374980ab20.jpg",
//                                           "fileServer": "https://storage-b.picacomic.com"
//                                         },
//                                         "isWeb": false,
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "那年今天",
//                                         "thumb": {
//                                           "originalName": "old.jpg",
//                                           "path": "old.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": false,
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "官方都在看",
//                                         "thumb": {
//                                           "originalName": "promo.jpg",
//                                           "path": "promo.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": false,
//                                         "active": true
//                                       },
//                                       {
//                                         "title": "嗶咔運動",
//                                         "thumb": {
//                                           "originalName": "picacomic-move-cat.jpg",
//                                           "path": "picacomic-move-cat.jpg",
//                                           "fileServer": "https://diwodiwo.xyz/static/"
//                                         },
//                                         "isWeb": true,
//                                         "active": true,
//                                         "link": "https://move-web.bidobido.xyz"
//                                       },
//                                       {
//                                          "_id": "5821859b5f6b9a4f93dbf6e9",
//                                          "title": "嗶咔漢化",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "translate.png",
//                                            "path": "f541d9aa-e4fd-411d-9e76-c912ffc514d1.png",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6d1",
//                                          "title": "全彩",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "全彩.jpg",
//                                            "path": "8cd41a55-591c-424c-8261-e1d56d8b9425.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6cd",
//                                          "title": "長篇",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "長篇.jpg",
//                                            "path": "681081e7-9694-436a-97e4-898fc68a8f89.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6ca",
//                                          "title": "同人",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "同人.jpg",
//                                            "path": "1a33f1be-90fa-4ac7-86d7-802da315732e.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6ce",
//                                          "title": "短篇",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "短篇.jpg",
//                                            "path": "bd021022-8e19-49ff-8c62-6b29f31996f9.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "584ea1f45a44ac4f7dce3623",
//                                          "title": "圓神領域",
//                                          "description": "魔法少女小圓為主題的本子",
//                                          "thumb": {
//                                            "originalName": "cat_cirle.jpg",
//                                            "path": "c7e86b6e-4d27-4d81-a083-4a774ceadf72.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "58542b601b8ef1eb33b57959",
//                                          "title": "碧藍幻想",
//                                          "description": "碧藍幻想的本子",
//                                          "thumb": {
//                                            "originalName": "blue.jpg",
//                                            "path": "b8608481-6ec8-46a3-ad63-2f8dc5da4523.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6e5",
//                                          "title": "CG雜圖",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "CG雜圖.jpg",
//                                            "path": "b62b79b7-26af-4f81-95bf-d27ef33d60f3.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6e8",
//                                          "title": "英語 ENG",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "英語 ENG.jpg",
//                                            "path": "6621ae19-a792-4d0c-b480-ae3496a95de6.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6e0",
//                                          "title": "生肉",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "生肉.jpg",
//                                            "path": "c90a596c-4f63-4bdf-953d-392edcbb4889.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6de",
//                                          "title": "純愛",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "純愛.jpg",
//                                            "path": "18fde59b-bee5-4177-bf1f-88c87c7c9d70.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6d2",
//                                          "title": "百合花園",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "百合花園.jpg",
//                                            "path": "de5f1ca3-840a-4ea4-b6c0-882f1d80bd2e.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6e2",
//                                          "title": "耽美花園",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "1492872524635.jpg",
//                                            "path": "dcfa0115-80c9-4233-97e3-1ad469c2c0df.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6e4",
//                                          "title": "偽娘哲學",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "偽娘哲學.jpg",
//                                            "path": "39119d6c-4808-4859-98df-4dda30b9da3b.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6d3",
//                                          "title": "後宮閃光",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "後宮閃光.jpg",
//                                            "path": "dec122af-84bf-4736-b8f0-d6533a2839f7.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6d4",
//                                          "title": "扶他樂園",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "扶他樂園.jpg",
//                                            "path": "73d8a102-1805-4b14-b258-a95c85b02b8a.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5abb3fd683111d2ad3eecfca",
//                                          "title": "單行本",
//                                          "thumb": {
//                                            "originalName": "Loveland_001 (2).jpg",
//                                            "path": "a29c241a-2af7-47f2-aae5-b640374b12ac.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6da",
//                                          "title": "姐姐系",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "姐姐系.jpg",
//                                            "path": "91e551c5-a98f-4f41-b7a0-c125bd77c523.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6db",
//                                          "title": "妹妹系",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "妹妹系.jpg",
//                                            "path": "098f612c-9d16-4848-9732-0305b66ed799.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6cb",
//                                          "title": "SM",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "SM.jpg",
//                                            "path": "41fc9bce-68f6-4b36-98cf-14ab3d3bd19e.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6d0",
//                                          "title": "性轉換",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "性轉換.jpg",
//                                            "path": "f5c70a00-538c-44b8-b692-d6c3b049e133.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6df",
//                                          "title": "足の恋",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "足の恋.jpg",
//                                            "path": "ad3373c7-4974-45f5-b5d6-eb9490363314.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6cc",
//                                          "title": "人妻",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "人妻.jpg",
//                                            "path": "e3359724-603b-47d8-905f-c88c5d38c983.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6d8",
//                                          "title": "NTR",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "NTR.jpg",
//                                            "path": "e10cf018-e214-41fa-bf1c-376a6b7a24ea.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6d9",
//                                          "title": "強暴",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "強暴.jpg",
//                                            "path": "4c3a9fb0-3084-4abf-bbc9-8efa33554749.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6d6",
//                                          "title": "非人類",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "非人類.jpg",
//                                            "path": "b09840fe-8ca9-41ac-9e73-3dd68e426865.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6cf",
//                                          "title": "艦隊收藏",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "艦隊收藏.jpg",
//                                            "path": "1ed52b9e-8ac3-47ae-bafc-c31bfab9b3d5.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                          "_id": "5821859b5f6b9a4f93dbf6dc",
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6e1",
//                                          "title": "Fate",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "Fate.jpg",
//                                            "path": "44bf46b9-415e-490b-9b61-7916ef2cea53.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          "_id": "5821859b5f6b9a4f93dbf6dd",
//                                          "title": "東方",
//                                          "description": "未知",
//                                          "thumb": {
//                                            "originalName": "東方.jpg",
//                                            "path": "c373bf9d-1003-453d-a791-f65dde937654.jpg",
//                                            "fileServer": "https://storage1.picacomic.com"
//                                          }
//                                        },
//                                        {
//                                          }
//                                          "description": "未知",
//                                          "thumb": {
//                                          }
//                                          "description": "未知",
//                                  }