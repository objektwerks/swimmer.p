package swimmer

import com.typesafe.scalalogging.LazyLogging

import ox.supervised

import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.ObjectProperty

final class Model(store: Store) extends LazyLogging:
  def assertInFxThread(message: String, suffix: String = " should be in fx thread!"): Unit =
    require(Platform.isFxApplicationThread, message + suffix)
  def assertNotInFxThread(message: String, suffix: String = " should not be in fx thread!"): Unit =
    require(!Platform.isFxApplicationThread, message + suffix)

  val selectedSwimmerId = ObjectProperty[Long](0)
  val selectedSessionId = ObjectProperty[Long](0)

  selectedSwimmerId.onChange { (_, oldSwimmerId, newSwimmerId) =>
    logger.info("*** selected swimmer id onchange event: {} -> {}", oldSwimmerId, newSwimmerId)
    assertInFxThread("*** selected swimmer id onchange")
    sessions(newSwimmerId)
  }

  val observableSwimmers = ObservableBuffer[Swimmer]()
  val observableSessions = ObservableBuffer[Session]()

  observableSwimmers.onChange { (_, changes) =>
    logger.info("*** observable swimmers onchange event: {}", changes)
    assertInFxThread("*** observable swimmers onchange")
  }

  observableSessions.onChange { (_, changes) =>
    logger.info("*** observable sessions onchange event: {}", changes)
    assertInFxThread("*** observable sessions onchange")
  }

  logger.info("Model initialized.")

  def swimmers(): Unit =
    println("*** list swimmers in")
    supervised:
      assertNotInFxThread("*** swimmers")
      observableSwimmers ++= store.listSwimmers()
    println("*** list swimmers out")

  def add(swimmer: Swimmer): Unit =
    println("*** add swimmer in : " + swimmer.toString)
    supervised:
      assertNotInFxThread("*** add swimmer")
      val id = store.addSwimmer(swimmer)
      observableSwimmers.insert(0, swimmer.copy(id = id))
      selectedSwimmerId.value = id
    println("*** add swimmer out")

  def update(previousSwimmer: Swimmer, updatedSwimmer: Swimmer): Unit =
    println("*** update swimmer in : " + previousSwimmer.toString + " " + updatedSwimmer)
    supervised:
      assertNotInFxThread("*** update swimmer")
      store.updateSwimmer(updatedSwimmer)
      val index = observableSwimmers.indexOf(previousSwimmer)
      if index > -1 then observableSwimmers.update(index, updatedSwimmer)      
    println("*** update swimmer out")

  def sessions(swimmerId: Long): Unit =
    supervised:
      assertNotInFxThread("*** list sessions")
      observableSessions ++= store.listSessions(swimmerId)

  def add(session: Session): Unit =
    supervised:
      assertNotInFxThread("*** add session")
      val id = store.addSession(session)
      observableSessions.insert(0, session.copy(id = id))
      selectedSessionId.value = id

  def update(previousSession: Session, updatedSession: Session): Unit =
    supervised:
      assertNotInFxThread("*** update session")
      store.updateSession(updatedSession)
      val index = observableSessions.indexOf(previousSession)
      if index > -1 then observableSessions.update(index, updatedSession)