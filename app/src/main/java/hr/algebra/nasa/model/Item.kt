package hr.algebra.nasa.model

data class Item(
    var _id: Long?,
    var binomialName: String,
    var commonName: String,
    var location: String,
    var wikiLink: String,
    var lastRecord: String,
    var imageSrc: String,
    var shortDesc: String,
    var read: Boolean
)
