package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID

import models._
import services._

@Singleton
class AlbumsController @Inject()(val controllerComponents: ControllerComponents, artistsService: ArtistsService, albumsService: AlbumsService)(implicit ec: ExecutionContext) extends BaseController {

  def showFormAdd(artist: Option[UUID] = None) = Action.async { implicit request: Request[AnyContent] =>
    artist match {
      case Some(artistId) => artistsService.get(artistId).map {

        // Artist is given and it is proper ID present in the database.
        case Some(artistData) =>
           Ok(views.html.albums.form(None, List(artistData)))

        // Artist is given, but the ID is not correct.
        case None =>
          NotFound(views.html.artists.not_found())
      }

      // No artist is given, will allow to create album for any artist available.
      case None => 
        artistsService.list().map(artists => Ok(views.html.albums.form(None, artists)))
    }    
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    val input = for {
      formData <- request.body.asFormUrlEncoded
      name <- formData.get("release_year").map(_.head.toInt)
      title <- formData.get("title").map(_.head)
      tracksNo <- formData.get("tracks_no").map(_.head.toInt)
      artistId <- formData.get("artist_id").map(_.head).map(UUID.fromString _)
    } yield AlbumData(name, title, tracksNo, artistId)

  
    input.fold(Future(BadRequest(""))) { albumData => 
      albumsService.save(albumData).map { 
        case Right(album) => Redirect(routes.AlbumsController.list(None)).flashing("success" -> s"Album ${album.title} z ${album.releaseYear} został zapisany.")
        case Left(error)  => Redirect(routes.AlbumsController.showFormAdd(None)).flashing("error" -> error)
      }
    }
  }


  def list(artistId: Option[UUID] = None) = Action.async { implicit request: Request[AnyContent] =>
    for {
      albums <- albumsService.list(artistId)
      artist <- artistId.map(id => artistsService.get(id)).getOrElse(Future(None))
    } yield {
      Ok(views.html.albums.list(albums, artist))
    }
  }


  def showFormUpdate(albumId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    for {
      artists <- artistsService.list()
      album <- albumsService.get(albumId)
    } yield { album match {
      case Some(_) => Ok(views.html.albums.form(album, artists))
      case None => NotFound(views.html.albums.not_found())
    }} 
  }

  def update(albumId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    val input = for {
      formData <- request.body.asFormUrlEncoded
      name <- formData.get("release_year").map(_.head.toInt)
      title <- formData.get("title").map(_.head)
      tracksNo <- formData.get("tracks_no").map(_.head.toInt)
      artistId <- formData.get("artist_id").map(_.head).map(UUID.fromString _)
    } yield AlbumData(name, title, tracksNo, artistId)

    input.fold(Future(BadRequest(""))) { albumData => 
      albumsService.update(albumId, albumData).map { 
        case Right(artist) => Redirect(routes.AlbumsController.showFormUpdate(albumId)).flashing("success" -> s"Album ${albumData.title} z ${albumData.releaseYear} został zaktualizowany.")
        case Left(error) => Redirect(routes.AlbumsController.showFormUpdate(albumId)).flashing("error" -> error)
      }
    }    
  }


  def showDelete(albumId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    albumsService.get(albumId).map {
      case Some(album) =>
        Ok(views.html.albums.delete(album))

      case None =>
        NotFound(views.html.albums.not_found())
    }
  }

  def delete(albumId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    albumsService.delete(albumId).map {
      case Right(_) => Redirect(routes.AlbumsController.list(None)).flashing("success" -> "Album został usunięty.")
      case Left(error) => Redirect(routes.AlbumsController.showDelete(albumId)).flashing("error" -> error)
    }
  }  
}
