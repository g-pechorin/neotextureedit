package peterlavalle.neo

import java.util
import javax.inject.{Inject, Named}
import javax.swing.JMenuBar

import org.eclipse.sisu.EagerSingleton
import peterlavalle.neo.actions.TVisualAction
import peterlavalle.neovisualedit.ExploreMenu

import scala.collection.JavaConversions._
import javax.inject.{Inject, Named}

@Named
@EagerSingleton
class VisualMenuBar @Inject()(commands: util.Map[String, TVisualAction]) extends JMenuBar {

	val rBound = "(\\w+)://((\\w+/)+)([^/]+)".r
	commands.foreach {
		case (binding, actionListener) =>
			binding match {
				case rBound("MenuBar", path, _, name) =>
					ExploreMenu(this, path.split("/").toList, name)
						.addActionListener(actionListener)
			}
	}
}
