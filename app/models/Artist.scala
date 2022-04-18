package models

import java.util.UUID

case class Artist(
    id: UUID,
    name: String
)

case class ArtistData(
    name: String
)