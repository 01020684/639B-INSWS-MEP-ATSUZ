package models

import java.util.UUID

case class Album(
    id: UUID,
    releaseYear: Int,
    title: String,
    tracksNo: Int,
    artist: Artist
)

case class AlbumData(
    releaseYear: Int,
    title: String,
    tracksNo: Int,
    artistId: UUID
)