package peterlavalle.neovisualedit

import java.awt.event.{ActionEvent, ActionListener}
import java.util
import javax.inject.{Inject, Named}
import javax.swing.{JOptionPane, SwingUtilities}

import com.google.inject.Guice
import com.mystictri.neotextureedit.TextureEditor
import org.eclipse.sisu.space.{SpaceModule, URLClassSpace}
import org.eclipse.sisu.wire.WireModule

trait TAction extends ActionListener

@Named("menu://File/Dumbo Gumbo")
class DumboAction extends TAction {
	override def actionPerformed(e: ActionEvent): Unit = {
		JOptionPane.showMessageDialog(null, "Hello!")
	}
}

@Named("menu://Another/Dumbo Gumbo")
class DumboActions extends TAction {
	override def actionPerformed(e: ActionEvent): Unit = {
		JOptionPane.showMessageDialog(null, "Hello!")
	}
}

import scala.collection.JavaConversions._

import java.util

@Named
@org.eclipse.sisu.EagerSingleton
class AddMenus @Inject()(commands: java.util.Map[String, TAction]) {

	val rBound = "(\\w+)://((\\w+/)+)([^/]+)".r


	commands.foreach {
		case (binding, action) =>
			binding match {
				case rBound("menu", path, _, name) =>
					ExploreMenu(
						TextureEditor.INSTANCE.m_MainMenuBar,
						path.split("/").toList,
						name
					).addActionListener(action)
			}
	}
}


object ReduxTextureEdit extends App {
	// https://github.com/Netflix/governator/wiki/Getting-Started

	TextureEditor.main(args)
	SwingUtilities.invokeAndWait(new Runnable {
		override def run(): Unit = {
			Guice.createInjector(
				new WireModule(// auto-wires unresolved dependencies
					new SpaceModule(// scans and binds @Named components
						new URLClassSpace(getClass.getClassLoader) // abstracts class/resource finding
					)
				)
			)
		}
	})
}
