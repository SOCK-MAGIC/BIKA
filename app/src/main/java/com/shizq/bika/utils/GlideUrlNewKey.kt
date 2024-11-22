package com.shizq.bika.utils

//因为图片的ip地址经常改变，会占用手机存储空间和浪费手机流量，所以改变Glide缓存的key,来减少占用，Glide默认缓存的key是用图片的url，现在改成url后面的参数作为缓存key
class GlideUrlNewKey(baseUrl: String, private val key: String)