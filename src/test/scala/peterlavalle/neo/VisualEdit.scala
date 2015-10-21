package peterlavalle.neo

import com.google.inject.name.Names
import com.google.inject.{Binder, Guice, Module}
import org.eclipse.sisu.space.{SpaceModule, URLClassSpace}
import org.eclipse.sisu.wire.WireModule

object VisualEdit extends App {
	Guice.createInjector(
		new Module {
			override def configure(binder: Binder): Unit = {
				binder.bind(classOf[Array[String]]).annotatedWith(Names.named("args")).toInstance(args)

			}
		},
		new WireModule(// auto-wires unresolved dependencies
			new SpaceModule(// scans and binds @Named components
				new URLClassSpace(getClass.getClassLoader) // abstracts class/resource finding
			)
		)
	)
}
