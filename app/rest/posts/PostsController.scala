package rest.posts

import play.api._
import play.api.mvc._

object PostsController extends Controller {

  def getPosts: Action[AnyContent] = Action {
    Ok("works")
  }

}