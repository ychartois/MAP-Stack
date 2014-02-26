package rest.posts


import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import MongoDBTestUtils._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class PostsControllerTests extends Specification {

  "PostsController" should {

    "be able to retrieve all the posts" in withMongoDbAndData("posts","test/stubs/posts.json") { app =>
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
