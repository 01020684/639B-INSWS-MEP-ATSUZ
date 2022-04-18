package services

import anorm._
import anorm.SqlParser._
import play.api.db.DBApi

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.Future

import models._
import utils.DatabaseExecutionContext

import scala.language.postfixOps

object ArtistsService {
    private[services] val parser: RowParser[Artist] = {
        get[UUID]("artists.artist_id") ~ get[String]("name") map {
            case id ~ name => Artist(id, name)
        }
    }
}

class ArtistsService @Inject()(dbAPI: DBApi)(implicit ec: DatabaseExecutionContext) {
    import ArtistsService._

    private val db = dbAPI.database("default")

    def delete(artistId: UUID): Future[Either[String, Unit]] = Future(db.withConnection { implicit connection =>
        val artist = SQL("SELECT * FROM artists WHERE artist_id = {artist_id}")
                    .on("artist_id" -> artistId)
                    .as(parser.singleOpt)

        artist match {
            case Some(_) =>
                val albumsCount = SQL("SELECT COUNT(*) FROM albums WHERE artist_id = {artist_id}")
                    .on("artist_id" -> artistId)
                    .as(scalar[Long].single)

                if(albumsCount > 0) {
                    Left("Najpierw usuń wszystkie albumy wykonawcy.")
                } else {
                    SQL("DELETE FROM artists WHERE artist_id = {artist_id} ")
                        .on("artist_id" -> artistId)
                        .execute()
                    Right(())
                }

            case None =>
                Left("Nie znaleziono podanego artysty.")                    
        }
    })

    def get(artistId: UUID): Future[Option[Artist]] = Future(db.withConnection { implicit connection => 
        SQL("SELECT * FROM artists WHERE artist_id = {artist_id}")
            .on("artist_id" -> artistId)
            .as(parser.singleOpt)
    })

    def list(): Future[List[Artist]] = Future (db.withConnection { implicit connection =>
        SQL("SELECT * FROM artists").as(parser *)
    })

    def save(artistData: ArtistData): Future[Either[String, Artist]] = Future(db.withConnection { implicit connection =>
        val newArtistId = UUID.randomUUID()

        if(artistData.name.length < 1)
            Left("Nazwa artysty nie może być pusta.")
        else {
            SQL("INSERT INTO artists (artist_id, name) VALUES ({artist_id}, {name})")
                .on("artist_id" -> newArtistId, "name" -> artistData.name)
                .executeInsert()
            Right(SQL("SELECT * FROM artists WHERE artist_id = {artist_id}")
                .on("artist_id" -> newArtistId)
                .as(parser.single)
            )
        }        
    })

    def update(artistId: UUID, artistData: ArtistData): Future[Either[String, Artist]] = Future(db.withConnection { implicit connection =>
        if(artistData.name.length < 1)
            Left("Nazwa artysty nie może być pusta.")
        else {
            val updateResult = SQL("UPDATE artists SET name = {name} WHERE artist_id = {artist_id}")
                .on("artist_id" -> artistId, "name" -> artistData.name)
                .executeUpdate()
            if(updateResult == 0)
                Left("Nie znaleziono podanego artysty.")
            else
                Right(SQL("SELECT * FROM artists WHERE artist_id = {artist_id}")
                    .on("artist_id" -> artistId)
                    .as(parser.single)
                )
        }      
    })
}
