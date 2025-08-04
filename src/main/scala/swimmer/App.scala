package swimmer

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scalafx.application.JFXApp3

object App extends JFXApp3 with LazyLogging:
  private val conf = ConfigFactory.load("app.conf")
  private val context = Context(conf)
  private val store = Store(context)
  private val model = Model(store)

  override def start(): Unit =
    val view = View(context, model)
    stage = new JFXApp3.PrimaryStage:
      scene = view.scene
      title = context.windowTitle
      minWidth = context.windowWidth
      minHeight = context.windowHeight
      icons += context.logo
    
    stage.show()
    logger.info("*** App started, server url: {}", context.url)

  override def stopApp(): Unit = logger.info("*** App stopped.")