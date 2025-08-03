package swimmer

import com.typesafe.scalalogging.LazyLogging

import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.ObjectProperty

final class Model() extends LazyLogging:
  val shouldBeInFxThread = (message: String) => require(Platform.isFxApplicationThread, message)
  val shouldNotBeInFxThread = (message: String) => require(!Platform.isFxApplicationThread, message)

  val selectedSwimmerId = ObjectProperty[Long](0)
  val selectedSessionId = ObjectProperty[Long](0)

  selectedSwimmerId.onChange { (_, oldSwimmerId, newSwimmerId) =>
    logger.info("*** selected swimmer id onchange event: {} -> {}", oldSwimmerId, newSwimmerId)
    shouldBeInFxThread("*** selected swimmer id onchange should be in fx thread.")
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

  def swimmers(): Unit =
    fetcher.fetch(
      ListSwimmers(objectAccount.get.license, objectAccount.get.id),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.swimmers", fault)
        case SwimmersListed(swimmers) =>
          observableSwimmers.clear()
          observableSwimmers ++= swimmers
        case _ => ()
    )

  def add(selectedIndex: Int, swimmer: Swimmer)(runLast: => Unit): Unit =
    fetcher.fetch(
      SaveSwimmer(objectAccount.get.license, swimmer),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.save swimmer", swimmer, fault)
        case SwimmerSaved(id) =>
          observableSwimmers += swimmer.copy(id = id)
          observableSwimmers.sort()
          selectedSwimmerId.set(id)
          runLast
        case _ => ()
    )

  def update(selectedIndex: Int, swimmer: Swimmer)(runLast: => Unit): Unit =
    fetcher.fetch(
      SaveSwimmer(objectAccount.get.license, swimmer),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.save swimmer", swimmer, fault)
        case SwimmerSaved(id) =>
          observableSwimmers.update(selectedIndex, swimmer)
          runLast
        case _ => ()
    )

  def sessions(swimmerId: Long): Unit =
    fetcher.fetch(
      ListSessions(objectAccount.get.license, swimmerId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.sessions", fault)
        case SessionsListed(sessions) =>
          observableSessions.clear()
          observableSessions ++= sessions
        case _ => ()
    )

  def add(selectedIndex: Int, session: Session)(runLast: => Unit): Unit =
    fetcher.fetch(
      SaveSession(objectAccount.get.license, session),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.save session", session, fault)
        case SessionSaved(id) =>
          observableSessions += session.copy(id = id)
          observableSessions.sort()
          selectedSessionId.set(id)
          runLast
        case _ => ()
    )

  def update(selectedIndex: Int, session: Session)(runLast: => Unit): Unit =
    fetcher.fetch(
      SaveSession(objectAccount.get.license, session),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.save session", session, fault)
        case SessionSaved(id) =>
          observableSessions.update(selectedIndex, session)
          runLast
        case _ => ()
    )