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


object TracksService {
    private[services] val parser: RowParser[Track] = {
        get[UUID]("tracks.track_id") ~ get[Int]("tracks.track_no") ~ get[String]("tracks.title") ~ get[Int]("tracks.duration") ~ AlbumsService.parser map {
            case id ~ trackNo ~ name ~ duration ~ album => Track(id, trackNo, name, duration, album)
        }
    }
}

class TracksService @Inject()(dbAPI: DBApi)(implicit ec: DatabaseExecutionContext) {
    import TracksService._

    private val db = dbAPI.database("default")

    def delete(trackId: UUID): Future[Either[String, Unit]] = Future(db.withConnection { implicit connection =>
        val trackCount = SQL("SELECT COUNT(*) FROM tracks WHERE track_id = {track_id}")
            .on("track_id" -> trackId)
            .as(scalar[Long].single)

        if(trackCount > 0) {
            SQL("DELETE FROM tracks WHERE track_id = {track_id} ")
                .on("track_id" -> trackId)
                .execute()
            Right(())
        } else {
            Left("Nie znaleziono podanego utworu.")                    
        }
                
    })  

    def get(trackId: UUID): Future[Option[Track]] = Future(db.withConnection { implicit connection => 
        SQL("SELECT * FROM tracks LEFT JOIN albums ON albums.album_id = tracks.album_id LEFT JOIN artists ON albums.artist_id = artists.artist_id WHERE track_id = {track_id}")
            .on("track_id" -> trackId)
            .as(parser.singleOpt)
    })

    def list(albumId: Option[UUID] = None) = Future (db.withConnection { implicit connection => 
        val mainQueryString = "SELECT * FROM tracks LEFT JOIN albums ON albums.album_id = tracks.album_id LEFT JOIN artists ON albums.artist_id = artists.artist_id"

        albumId match {
            case Some(id) => 
                SQL(s"${mainQueryString} WHERE tracks.album_id = {album_id}")
                    .on("album_id" -> albumId)
                    .as(parser *) 
            case None =>         
                SQL(mainQueryString)
                    .as(parser *)   
        }
    })

    def save(trackData: TrackData): Future[Either[String, Track]] = Future(db.withConnection { implicit connection =>
        validateInput(trackData) match { 
            case Success(_) => Right {
                val newTrackId = UUID.randomUUID()

                SQL("INSERT INTO tracks (track_id, duration, title, track_no, album_id) VALUES ({track_id}, {duration}, {title}, {track_no}, {album_id})")
                    .on(
                        "track_id" -> newTrackId,
                        "duration" -> trackData.duration,
                        "title" -> trackData.title,
                        "track_no" -> trackData.trackNo,
                        "album_id" -> trackData.albumId
                    )
                    .executeInsert()

                SQL("SELECT * FROM tracks LEFT JOIN albums ON albums.album_id = tracks.album_id LEFT JOIN artists ON albums.artist_id = artists.artist_id WHERE track_id = {track_id}")
                    .on("track_id" -> newTrackId)
                    .as(parser.single)
            }

            case Failure(error) =>
                Left(error.getMessage)
        }
    })

    def update(trackId: UUID, trackData: TrackData): Future[Either[String, Track]] = Future(db.withConnection { implicit connection =>
        validateInput(trackData) match { 
            case Success(_) => 
                val updateResult = SQL("UPDATE tracks SET duration = {duration}, title = {title}, track_no = {track_no}, album_id = {album_id} WHERE track_id = {track_id}")
                    .on(
                        "track_id" -> trackId,
                        "duration" -> trackData.duration,
                        "title" -> trackData.title,
                        "track_no" -> trackData.trackNo,
                        "album_id" -> trackData.albumId
                    )
                    .executeUpdate()

                if(updateResult == 0)
                    Left("Nie znaleziono podanego utworu.")
                else
                    Right(SQL("SELECT * FROM tracks LEFT JOIN albums ON albums.album_id = tracks.album_id LEFT JOIN artists ON albums.artist_id = artists.artist_id WHERE track_id = {track_id}")
                        .on("track_id" -> trackId)
                        .as(parser.single)
                    )

            case Failure(error) =>
                Left(error.getMessage)
        }    
    })
    


    private def validateInput(trackData: TrackData)(implicit connection: Connection) = Try {
        if(trackData.duration < 0)
            throw new Exception("Długość ścieżki nie może być ujemna.")
        if(trackData.duration > 90 * 60)
            throw new Exception("Długość ścieżki nie może przekraczać 90min.")

        if(trackData.title.isEmpty)
            throw new Exception("Nazwa ścieżki nie może być pusta.")

        if(trackData.trackNo < 0)
            throw new Exception("Numer ścieżki nie może być ujemny.")

        val album = SQL("SELECT * FROM albums LEFT JOIN artists ON albums.artist_id = artists.artist_id WHERE album_id = {album_id}")
            .on("album_id" -> trackData.albumId)
            .as(AlbumsService.parser.singleOpt)

        if(album.isEmpty)
            throw new Exception("Podany album nie istnieje.")

        trackData
    }
}

