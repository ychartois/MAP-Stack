package rest.posts


import play.api.libs.concurrent.Execution.Implicits.defaultContext

import play.api._
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import scala.concurrent.Future
import play.api.libs.json._
import play.modules.reactivemongo.json.collection.JSONCollection

object PostsController extends Controller with MongoController {

  /**
   * Get the posts collection
   *
   * @return the posts collection
   */
  def posts: JSONCollection = db.collection[JSONCollection]("posts")


  /**
   * Get all the blog posts
   *
   * @return a json array with all the posts information
   */
  def getPosts: Action[AnyContent] = Action.async {

    // We want all the posts
    val cursor = posts.find( Json.obj() ).cursor[JsObject]

    // gather all the JsObjects in a list
    val postsList: Future[List[JsObject]] = cursor.collect[List]()

    // transform the list into a JsArray
    val postsArray: Future[JsArray] = postsList.map { posts =>
      Json.arr(posts)
    }

    postsArray.map {  posts =>
      Ok( posts.apply(0) )
    }
  }

}