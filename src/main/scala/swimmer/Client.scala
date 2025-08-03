package swimmer

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scalafx.application.JFXApp3

import swimmer.dialog.Alerts

object Client extends JFXApp3 with LazyLogging:
  private val conf = ConfigFactory.load("client.conf")
  private val context = Context(conf)
  private val model = Model(fetcher)

  override def start(): Unit =
    val view = View(context, model)
    stage = new JFXApp3.PrimaryStage:
      scene = view.scene
      title = context.windowTitle
      minWidth = context.windowWidth
      minHeight = context.windowHeight
      icons.add(context.logo)
    
    stage.show()
    logger.info("*** client started, server url: {} endpoint: {}", context.url, context.endpoint)

  override def stopApp(): Unit = logger.info("*** client stopped.")