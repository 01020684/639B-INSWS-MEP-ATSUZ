package services

import anorm.SqlParser._
import javax.inject.Inject
import anorm._
import play.api.db.DBApi

import java.sql.Connection
import java.util.UUID
import scala.concurrent.Future

import models._
import utils.DatabaseExecutionContext

import scala.language.postfixOps
import scala.util.{Try, Success, Failure}


object AlbumsService {
    private[services] val parser: RowParser[Album] = {
        get[UUID]("album_id") ~ get[Int]("release_year") ~ get[String]("title") ~ get[Int]("tracks_no") ~ ArtistsService.parser map {
            case albumId ~ releaseYear ~ title ~ tracksNo ~ artist => Album(albumId, releaseYear, title, tracksNo, artist)
        }
    }
}

class AlbumsService @Inject()(dbAPI: DBApi)(implicit ec: DatabaseExecutionContext) {
    import AlbumsService._

    private val db = dbAPI.database("default")

    def delete(albumId: UUID): Future[Either[String, Unit]] = Future(db.withConnection { implicit connection =>
        val albumCount = SQL("SELECT COUNT(*) FROM albums WHERE album_id = {album_id}")
            .on("album_id" -> albumId)
            .as(scalar[Long].single)

        if(albumCount > 0) {
            val albumsCount = SQL("SELECT COUNT(*) FROM tracks WHERE album_id = {album_id}")
                .on("album_id" -> albumId)
                .as(scalar[Long].single)

            if(albumsCount > 0) {
                Left("Najpierw usuń wszystkie utwory albumu.")
            } else {
                SQL("DELETE FROM albums WHERE album_id = {album_id} ")
                    .on("album_id" -> albumId)
                    .execute()
                Right(())
            }
        } else {
            Left("Nie znaleziono podanego albumu.")                    
        }
                
    })    

    def get(albumId: UUID): Future[Option[Album]] = Future(db.withConnection { implicit connection => 
        SQL("SELECT * FROM albums LEFT JOIN artists ON albums.artist_id = artists.artist_id WHERE album_id = {album_id}")
            .on("album_id" -> albumId)
            .as(parser.singleOpt)
    })

    def list(artistId: Option[UUID] = None) = Future (db.withConnection { implicit connection =>
        val mainQueryString = "SELECT * FROM albums LEFT JOIN artists ON albums.artist_id = artists.artist_id"

        artistId match {
            case Some(id) =>
                SQL(s"$mainQueryString WHERE albums.artist_id = {artistId}").on("artistId" -> id).as(parser *)

            case None =>
                SQL(mainQueryString).as(parser *)
        }
    })

    def save(albumData: AlbumData): Future[Either[String, Album]] = Future(db.withConnection { implicit connection =>
        validateInput(albumData) match { 
            case Success(_) => Right {
                val newAlbumId = UUID.randomUUID()

                SQL("INSERT INTO albums (album_id, release_year, title, tracks_no, artist_id) VALUES ({album_id}, {release_year}, {title}, {tracks_no}, {artist_id})")
                    .on(
                        "album_id" -> newAlbumId, 
                        "release_year" -> albumData.releaseYear,
                        "title" -> albumData.title,
                        "tracks_no" -> albumData.tracksNo,
                        "artist_id" -> albumData.artistId
                    )
                    .executeInsert()

                SQL("SELECT * FROM albums LEFT JOIN artists ON albums.artist_id = artists.artist_id WHERE album_id = {album_id}")
                    .on("album_id" -> newAlbumId)
                    .as(parser.single)
            }

            case Failure(error) =>
                Left(error.getMessage)
        }
    })

    def update(albumId: UUID, albumData: AlbumData): Future[Either[String, Album]] = Future(db.withConnection { implicit connection =>
        validateInput(albumData) match { 
            case Success(_) => 
                val updateResult = SQL("UPDATE albums SET release_year = {release_year}, title = {title}, tracks_no = {tracks_no}, artist_id = {artist_id} WHERE album_id = {album_id}")
                    .on(
                        "album_id" -> albumId, 
                        "release_year" -> albumData.releaseYear,
                        "title" -> albumData.title,
                        "tracks_no" -> albumData.tracksNo,
                        "artist_id" -> albumData.artistId
                    )
                    .executeUpdate()

                if(updateResult == 0)
                    Left("Nie znaleziono podanego artysty.")
                else
                    Right(SQL("SELECT * FROM albums LEFT JOIN artists ON albums.artist_id = artists.artist_id WHERE album_id = {album_id}").on("album_id" -> albumId).as(parser.single))

            case Failure(error) =>
                Left(error.getMessage)
        }
    })

    private def validateInput(albumData: AlbumData)(implicit connection: Connection) = Try {
        if(albumData.releaseYear < 1900)
            throw new Exception("Rok wydania musi być późniejszy niż 1900.")

        if(albumData.title.isEmpty)
            throw new Exception("Nazwa albumu nie może być pusta.")

        if(albumData.tracksNo <= 0)
            throw new Exception("Liczba utworów musi być dodatnia.")

        val artist = SQL("SELECT COUNT(*) FROM artists WHERE artist_id = {artist_id}")
            .on("artist_id" -> albumData.artistId)
            .as(scalar[Long].single)

        if(artist == 0)
            throw new Exception("Podany artysta nie istnieje.")

        albumData
    }  
}

