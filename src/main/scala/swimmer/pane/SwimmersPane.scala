package swimmer.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, Tab, TabPane, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import swimmer.{Context, Model, Swimmer}
import swimmer.dialog.SwimmerDialog

final class SwimmersPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val tableView = new TableView[Swimmer]():
    columns ++= List(
      new TableColumn[Swimmer, String]:
        text = context.headerName
        cellValueFactory = _.value.nameProperty
    )
    items = model.observableSwimmers

  val addButton = new Button:
    graphic = context.addImage
    text = context.buttonAdd
    disable = false
    onAction = { _ => add() }

  val editButton = new Button:
    graphic = context.editImage
    text = context.buttonEdit
    disable = true
    onAction = { _ => update() }

  val buttonBar = new HBox:
    spacing = 6
    children = List(addButton, editButton)
  
  val tab = new Tab:
  	text = context.tabSwimmers
  	closable = false
  	content = new VBox {
      spacing = 6
      padding = Insets(6)
      children = List(tableView, buttonBar)
    }

  val tabPane = new TabPane:
    tabs = List(tab)

  children = List(tabPane)
  VBox.setVgrow(tableView, Priority.Always)
  VBox.setVgrow(tabPane, Priority.Always)

  tableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 && tableView.selectionModel().getSelectedItem != null) update()
  }

  tableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  
  tableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedItem) =>
    // model.update executes a remove and add on items. the remove passes a null selectedItem!
    if selectedItem != null then
      model.selectedSwimmerId.value = selectedItem.id
      editButton.disable = false
    else editButton.disable = true
  }

  def add(): Unit =
    SwimmerDialog(context, Swimmer(name = "")).showAndWait() match
      case Some(swimmer: Swimmer) =>
        model.add(swimmer)
        tableView.selectionModel().select(0)
      case _ =>

  def update(): Unit =
    if tableView.selectionModel().getSelectedItem != null then
      val selectedIndex = tableView.selectionModel().getSelectedIndex
      val swimmer = tableView.selectionModel().getSelectedItem.swimmer
      SwimmerDialog(context, swimmer).showAndWait() match
        case Some(updatedSwimmer: Swimmer) =>
          model.update(swimmer, updatedSwimmer)
          tableView.selectionModel().select(selectedIndex)
        case _ =>