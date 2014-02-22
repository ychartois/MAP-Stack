package rest.posts

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.test.WithApplication
import play.api.test.WithApplication
import play.api.libs.json.{JsArray, JsValue}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class PostsControllerTests extends Specification {

  "Application" should {

    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }

    "find 5 posts" in {
      running(FakeApplication()) {
        val Some(result) = route(FakeRequest(GET, "/posts"))

        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")

        contentAsJson(result).as[JsArray].value.size must equalTo(5)
      }
    }
  }
}
