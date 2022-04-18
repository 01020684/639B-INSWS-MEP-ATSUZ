package models

import java.util.UUID

case class Track(
    id: UUID,
    trackNo: Int,
    title: String,
    duration: Int,
    album: Album
)

case class TrackData(
    trackNo: Int,
    title: String,
    duration: Int,
    albumId: UUID
)