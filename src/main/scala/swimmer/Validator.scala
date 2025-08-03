package swimmer

object Validator:
  extension (swimmer: Swimmer)
    def isValid =
      swimmer.id >= 0 &&
      swimmer.name.length >= 2

  extension (session: Session)
    def isValid: Boolean =
      session.id >= 0 &&
      session.swimmerId > 0 &&
      session.weight > 0 &&
      session.weightUnit.length == 2 &&
      session.laps > 0 &&
      session.lapDistance > 0 &&
      session.lapUnit.length >= 4 &&
      session.style.nonEmpty &&
      session.minutes > 0 &&
      session.seconds >= 0 &&
      session.calories >= 0 &&
      session.datetime > 0