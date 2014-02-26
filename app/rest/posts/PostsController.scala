package rest.posts


import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.modules.reactivemongo.json.BSONFormats._

import play.api.mvc._
import play.modules.reactivemongo.MongoController
import scala.concurrent.Future
import play.api.libs.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}

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

    // the list have only one element
    postsArray.map {  posts =>
      Ok( posts.apply(0) )
    }
  }

  /**
   * Get a specific post
   *
   * @return a json array with the post information
   */
  def getPost(id: String): Action[AnyContent] = Action.async {

    // Get the post
    val query = BSONDocument( "_id" -> BSONObjectID(id) )
    val cursor = posts.find( query ).cursor[JsObject]

    // gather all the JsObjects in a list
    val postsList: Future[List[JsObject]] = cursor.collect[List]()

    // the list have only one element
    postsList.map {  post =>
      Ok( post.head )
    }
  }

}