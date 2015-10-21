package peterlavalle.neo.actions

import java.awt.event.ActionEvent
import javax.inject.{Inject, Named}
import javax.swing.JFrame
import javax.inject.{Inject, Named}
import javax.inject.{Inject, Named}


@Named("MenuBar://File/Quit")
class FileQuit extends TVisualAction {

	@Inject
	val visualFrame: JFrame = null

	???

	override def actionPerformed(actionEvent: ActionEvent): Unit = {
		???
	}
}
