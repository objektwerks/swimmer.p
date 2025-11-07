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

  selectedSwimmerId.onChange { (_, _, newSwimmerId) =>
    sessions(newSwimmerId)
  }

  val observableSwimmers = ObservableBuffer[Swimmer]()
  val observableSessions = ObservableBuffer[Session]()

  logger.info("Model initialized.")

  def swimmers(): Unit =
    supervised:
      assertNotInFxThread("list swimmers")
      observableSwimmers.clear
      observableSwimmers ++= store.listSwimmers()

  def add(swimmer: Swimmer): Unit =
    supervised:
      assertNotInFxThread(s"add swimmer: $swimmer")
      val id = store.addSwimmer(swimmer)
      observableSwimmers.insert(0, swimmer.copy(id = id))
      selectedSwimmerId.value = id
      logger.info(s"Added swimmer: $swimmer")

  def update(previousSwimmer: Swimmer, updatedSwimmer: Swimmer): Unit =
    supervised:
      assertNotInFxThread(s"update swimmer from: $previousSwimmer to: $updatedSwimmer")
      store.updateSwimmer(updatedSwimmer)
      val index = observableSwimmers.indexOf(previousSwimmer)
      if index > -1 then
        observableSwimmers.update(index, updatedSwimmer)      
        logger.info(s"Updated swimmer from: $previousSwimmer to: $updatedSwimmer")
      else
        logger.error(s"Update of swimmer: $updatedSwimmer \nfailed due to invalid index: $index")

  def sessions(swimmerId: Long): Unit =
    supervised:
      assertNotInFxThread(s"list sessions, swimmer id: $swimmerId")
      observableSessions.clear
      observableSessions ++= store.listSessions(swimmerId)

  def add(session: Session): Unit =
    supervised:
      assertNotInFxThread(s"add session: $session")
      val id = store.addSession(session)
      observableSessions.insert(0, session.copy(id = id))
      selectedSessionId.value = id
      logger.info(s"Added session: $session")

  def update(previousSession: Session, updatedSession: Session): Unit =
    supervised:
      assertNotInFxThread(s"update session from: $previousSession to: $updatedSession")
      store.updateSession(updatedSession)
      val index = observableSessions.indexOf(previousSession)
      if index > -1 then
        observableSessions.update(index, updatedSession)
        logger.info(s"Updated session: $updatedSession")
      else
        logger.error(s"Update of session: $updatedSession \nfailed due to invalid index: $index")