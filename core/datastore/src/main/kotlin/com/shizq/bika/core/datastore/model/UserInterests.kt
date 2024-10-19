package com.shizq.bika.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInterests(
    val interestsVisibility: Map<String, Boolean> = mapOf(
        "推荐" to true,
        "排行榜" to true,
        "游戏推荐" to true,
        "哔咔小程序" to true,
        "留言板" to true,
        "最近更新" to true,
        "随机本子" to true,
        "援助嗶咔" to true,
        "嗶咔小禮物" to true,
        "小電影" to true,
        "小里番" to true,
        "嗶咔畫廊" to true,
        "嗶咔商店" to true,
        "大家都在看" to true,
        "大濕推薦" to true,
        "那年今天" to true,
        "官方都在看" to true,
        "嗶咔運動" to true,
        "嗶咔漢化" to true,
        "全彩" to true,
        "長篇" to true,
        "同人" to true,
        "短篇" to true,
        "圓神領域" to true,
        "碧藍幻想" to true,
        "CG雜圖" to true,
        "英語 ENG" to true,
        "生肉" to true,
        "純愛" to true,
        "百合花園" to true,
        "耽美花園" to true,
        "偽娘哲學" to true,
        "扶他樂園" to true,
        "單行本" to true,
        "姐姐系" to true,
        "妹妹系" to true,
        "SM" to true,
        "性轉換" to true,
        "足の恋" to true,
        "人妻" to true,
        "NTR" to true,
        "強暴" to true,
        "非人類" to true,
        "艦隊收藏" to true,
        "Love Live" to true,
        "SAO 刀劍神域" to true,
        "Fate" to true,
        "東方" to true,
        "WEBTOON" to true,
        "禁書目錄" to true,
        "歐美" to true,
        "Cosplay" to true,
        "重口地帶" to true,
    ),
)
