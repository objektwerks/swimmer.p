package swimmer.control

import scalafx.scene.control.{TextField, TextFormatter}
import scalafx.scene.control.TextFormatter.Change

class NonEmptyTextField extends TextField:
  val filter: Change => Change = { (change: Change) =>
    if change.controlNewText.length >= 1 then change
    else null
  }

  textFormatter = TextFormatter[String](filter)