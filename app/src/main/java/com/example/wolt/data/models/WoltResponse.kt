package com.example.wolt.data.models

data class WoltResponse(
    val name: String,
    val sections: List<Section>
)

data class Section(
    val items: List<Item>
)

data class Item(
    val contentId: String,
    val image: Image,
    val quantity: Int,
    val template: String,
    val title: String
)

data class Image(
    val url: String
)