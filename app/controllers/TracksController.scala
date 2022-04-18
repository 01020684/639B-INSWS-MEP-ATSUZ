package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

import models._
import services._

@Singleton
class TracksController @Inject()(val controllerComponents: ControllerComponents, albumsService: AlbumsService, tracksService: TracksService)
  (implicit ec: ExecutionContext) extends BaseController {

  def showFormAdd(album: Option[UUID] = None) = Action.async { implicit request: Request[AnyContent] =>
    album match {
      case Some(albumId) => albumsService.get(albumId).map {

        // Album is given and it is proper ID present in the database.
        case Some(albumData) =>
           Ok(views.html.tracks.form(None, List(albumData)))

        // Album is given, but the ID is not correct.
        case None =>
          NotFound(views.html.albums.not_found())
      }

      // No album is given, will allow to create track for any album available.
      case None => 
        albumsService.list().map(albums => Ok(views.html.tracks.form(None, albums)))
    }    
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    val input = for {
      formData <- request.body.asFormUrlEncoded
      trackNo <- formData.get("track_no").map(_.head.toInt)
      title <- formData.get("title").map(_.head)
      duration <- formData.get("duration").map(_.head.toInt)
      albumId <- formData.get("album_id").map(_.head).map(UUID.fromString _)
    } yield TrackData(trackNo, title, duration, albumId)

  
    input.fold(Future(BadRequest(request.body.asFormUrlEncoded.toString))) { trackData => 
      tracksService.save(trackData).map { 
        case Right(album) => Redirect(routes.TracksController.list(None)).flashing("success" -> s"Utwór ${album.title} został zapisany.")
        case Left(error)  => Redirect(routes.TracksController.showFormAdd(None)).flashing("error" -> error)
      }
    }
  }


  def list(albumId: Option[UUID]) = Action.async { implicit request: Request[AnyContent] =>
    for {
      tracks <- tracksService.list(albumId)
      album  <- albumId.map(id => albumsService.get(id)).getOrElse(Future(None))
    } yield {
      Ok(views.html.tracks.list(tracks, album, album.map(_.artist)))
    }
  }


  def showFormUpdate(trackId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    for {
      albums <- albumsService.list()
      track <- tracksService.get(trackId)
    } yield { track match {
      case Some(_) => Ok(views.html.tracks.form(track, albums))
      case None => NotFound(views.html.tracks.not_found())
    }} 
  }

  def update(trackId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    val input = for {
      formData <- request.body.asFormUrlEncoded
      trackNo <- formData.get("track_no").map(_.head.toInt)
      title <- formData.get("title").map(_.head)
      duration <- formData.get("duration").map(_.head.toInt)
      albumId <- formData.get("album_id").map(_.head).map(UUID.fromString _)
    } yield TrackData(trackNo, title, duration, albumId)

    input.fold(Future(BadRequest(""))) { trackData => 
      tracksService.update(trackId, trackData).map { 
        case Right(artist) => Redirect(routes.TracksController.showFormUpdate(trackId)).flashing("success" -> s"Utwór ${trackData.title} został zaktualizowany.")
        case Left(error) => Redirect(routes.TracksController.showFormUpdate(trackId)).flashing("error" -> error)
      }
    }   
  }


  def showDelete(trackId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    tracksService.get(trackId).map {
      case Some(track) =>
        Ok(views.html.tracks.delete(track))

      case None =>
        NotFound(views.html.tracks.not_found())
    }
  }

  def delete(trackId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    tracksService.delete(trackId).map { 
      case Right(_) => Redirect(routes.TracksController.list(None)).flashing("success" -> "Utwór został usunięty.")
      case Left(error) => Redirect(routes.TracksController.showDelete(trackId)).flashing("error" -> error)
    }
  }  
}
