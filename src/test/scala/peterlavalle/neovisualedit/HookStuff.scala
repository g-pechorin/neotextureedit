package peterlavalle.neovisualedit

import java.util
import javax.inject.{Inject, Named}
import javax.swing.JMenuBar

import org.eclipse.sisu.EagerSingleton

import scala.collection.JavaConversions._

@Named
@EagerSingleton
class HookStuff @Inject()( commands: util.Map[String, TAction]) {

	val rBound = "(\\w+)://((\\w+/)+)([^/]+)".r

	commands.foreach {
		case (binding, action) =>
			binding match {
				case rBound("menu", path, _, name) =>
//					ExploreMenu(
//						menuBar,
//						path.split("/").toList,
//						name
//					).addActionListener(action)
			}
	}
}
