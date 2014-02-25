package rest.posts

import play.api.Play.current
import scala.concurrent._
import ExecutionContext.Implicits.global

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import com.github.athieriot.EmbedConnection
import play.api.Play
import scala.io.Source._
import scala.io.BufferedSource
import reactivemongo.api.MongoDriver
import play.modules.reactivemongo.json.collection.JSONCollection


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class PostsControllerTests extends Specification with EmbedConnection {

  sequential // to ensure the execution order of the tests

  "PostsController" should {

    System.setProperty("MONGODB_URL", "localhost:12345")
    System.setProperty("MONGODB_USERNAME", "")
    System.setProperty("MONGODB_PASSWORD", "")
    System.setProperty("MONGODB_DB", "testDatabase")

    val connection = new MongoDriver().connection("localhost:12345" :: Nil)
    val db = connection("testDatabase")
    val posts = db.collection[JSONCollection]("posts")

    "be able to retrieve all the posts" in {
      running(FakeApplication()) {

        val stub: BufferedSource = fromFile( Play.getFile("test/stubs/posts.json") )
        val postsStub: JsArray = Json.parse( stub.mkString ).as[JsArray]

        postsStub.validate[Seq[JsValue]].get.foreach( el =>
          posts.save(el)
        )


        // When
        val Some(result) = route(FakeRequest(GET, "/posts"))

        // Then
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")

        contentAsJson(result).as[JsArray].value.size must equalTo(5)
      }
    }
  }
}
