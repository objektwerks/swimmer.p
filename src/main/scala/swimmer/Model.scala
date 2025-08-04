package swimmer

import com.typesafe.scalalogging.LazyLogging

import ox.supervised

import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.ObjectProperty

final class Model(store: Store) extends LazyLogging:
  def assertInFxThread(message: String): Unit = require(Platform.isFxApplicationThread, message)
  def assertNotInFxThread(message: String): Unit = require(!Platform.isFxApplicationThread, message)

  val selectedSwimmerId = ObjectProperty[Long](0)
  val selectedSessionId = ObjectProperty[Long](0)

  selectedSwimmerId.onChange { (_, oldSwimmerId, newSwimmerId) =>
    logger.info("*** selected swimmer id onchange event: {} -> {}", oldSwimmerId, newSwimmerId)
    assertInFxThread("*** selected swimmer id onchange should be in fx thread.")
    sessions(newSwimmerId)
  }

  val observableSwimmers = ObservableBuffer[Swimmer]()
  val observableSessions = ObservableBuffer[Session]()

  observableSwimmers.onChange { (_, changes) =>
    logger.info("*** observable swimmers onchange event: {}", changes)
  }

  observableSessions.onChange { (_, changes) =>
    logger.info("*** observable sessions onchange event: {}", changes)
  }

  def swimmers(): List[Swimmer] =
    supervised:
      store.listSwimmers()

  def add(swimmer: Swimmer): Long =
    supervised:
      store.addSwimmer(swimmer)

  def update(swimmer: Swimmer): Long =
    supervised:
      store.updateSwimmer(swimmer)

  def sessions(swimmerId: Long): List[Session] =
    supervised:
      store.listSessions(swimmerId)

  def add(session: Session): Long =
    supervised:
      store.addSession(session)

  def update(session: Session): Long =
    supervised:
      store.updateSession(session)