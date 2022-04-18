package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.filters.csrf._
import play.filters.csrf.CSRF.Token

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

import models._
import services.ArtistsService


@Singleton
class ArtistsController @Inject()(val controllerComponents: ControllerComponents, addToken: CSRFAddToken, artistsService: ArtistsService)(implicit ec: ExecutionContext) extends BaseController {

  def showFormAdd() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.artists.form(None))
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    val input = for {
      formData <- request.body.asFormUrlEncoded
      name <- formData.get("name").map(_.head)
    } yield ArtistData(name)

    input.fold(Future(BadRequest(""))) { artistData => 
      artistsService.save(artistData).map { 
        case Right(artist) => Redirect(routes.ArtistsController.list()).flashing("success" -> s"Artysta ${artist.name} został zapisany.")
        case Left(error)  => Redirect(routes.ArtistsController.showFormAdd()).flashing("error" -> error)
      }
    }
  }


  def list() = Action.async { implicit request: Request[AnyContent] =>
    artistsService
      .list()
      .map(artists => Ok(views.html.artists.list(artists)))
  }


  def showFormUpdate(artistId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    artistsService.get(artistId).map {
      case Some(artist) =>
        Ok(views.html.artists.form(Some(artist)))

      case None =>
        NotFound(views.html.artists.not_found())
    }
  }

  def update(artistId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    val input = for {
      formData <- request.body.asFormUrlEncoded
      name <- formData.get("name").map(_.head)
    } yield ArtistData(name)

    input.fold(Future(BadRequest(""))) { artistData => 
      artistsService.update(artistId, artistData).map { 
        case Right(artist) => Redirect(routes.ArtistsController.list()).flashing("success" -> s"Artysta ${artist.name} został zaktualizowany.")
        case Left(error) => Redirect(routes.ArtistsController.showFormUpdate(artistId)).flashing("error" -> error)
      }
    }        
  }
  

  def showDelete(artistId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    artistsService.get(artistId).map {
      case Some(artist) =>
        Ok(views.html.artists.delete(artist))

      case None =>
        NotFound(views.html.artists.not_found())
    }
  }

  def delete(artistId: UUID) = Action.async { implicit request: Request[AnyContent] =>
    artistsService.delete(artistId).map { 
      case Right(_) => Redirect(routes.ArtistsController.list()).flashing("success" -> "Artysta został usunięty.")
      case Left(error) => Redirect(routes.ArtistsController.showDelete(artistId)).flashing("error" -> error)
    }
  }
}
