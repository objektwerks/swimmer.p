package swimmer

import com.typesafe.config.ConfigFactory

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

final class StoreTest extends AnyFunSuite with Matchers:
  val context = Context( ConfigFactory.load("test.conf") )
  val store = Store(context)

  var swimmer = Swimmer(name = "fred")
  var session = Session(swimmerId = 0)

  test("store"):
    addSwimmer()
    updateSwimmer()
    listSwimmers()


  def addSwimmer(): Unit =
    val swimmerId = store.addSwimmer(swimmer)
    swimmerId shouldBe 1
    swimmer = swimmer.copy(id = swimmerId)
    session = session.copy(swimmerId = swimmerId)

  def updateSwimmer(): Unit =
    swimmer = swimmer.copy(name = "fred flintstone")
    store.updateSwimmer(swimmer)

  def listSwimmers(): Unit =
    store.listSwimmers().length shouldBe 1