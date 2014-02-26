package rest.posts

import play.api.Play.current
import play.api._
import play.api.test.Helpers._
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.ReactiveMongoPlugin
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.BufferedSource
import scala.io.Source._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.test.FakeApplication
import play.api.libs.json.{JsArray, JsValue, Json}

/**
 * Test utils for running tests with MongoDB
 */
object MongoDBTestUtils {

  /**
   * We define a new db for the tests
   */
  val app = FakeApplication(
    additionalConfiguration = Map("mongodb.uri" -> "mongodb://localhost/unittests")
  )

  /**
   * Run the given block with MongoDB
   */
  def withMongoDb[T](collectionName: String)(block: Application => T): T = {
    running(app) {
      try {
        block(app)
      }
      finally {
        dropAll(collectionName)
      }
    }
  }

  /**
   * Load in the specified collection the fixture and Run the given block with MongoDB
   */
  def withMongoDbAndData[T](collectionName: String, fixtureName: String)(block: Application => T): T = {

    running(app) {
      try {
        val posts = ReactiveMongoPlugin.db.collection[JSONCollection](collectionName)
        val stub: BufferedSource = fromFile(Play.getFile(fixtureName))
        val postsStub: JsArray = Json.parse(stub.mkString).as[JsArray]

        postsStub.validate[Seq[JsValue]].get.foreach( el =>
          posts.save(el).value //because we don't want to do it asynchronously
        )

        block(app)
      }
      finally {
        dropAll(collectionName)
      }
    }
  }

  /**
   * clean the specified collection
   */
  def dropAll(collectionName: String) = {
    Await.ready(Future.sequence(Seq(
      ReactiveMongoPlugin.db.collection[JSONCollection]( collectionName ).drop()
    )), 2 seconds)
  }
}