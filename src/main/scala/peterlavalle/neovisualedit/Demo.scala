package peterlavalle.neovisualedit

import java.awt.BorderLayout
import javax.swing.{JFrame, JScrollPane, WindowConstants}

import peterlavalle.neovisualedit.model._

object Demo extends JFrame("Demoooo") with App {
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

	case class DemoNode(name: String) extends TNode {
		override val drops: List[TDrop] = List()
		override val channels: List[TChannel] = List()
	}

	private val graph = new view.Graph(new model.Graph)

	graph.graph + DemoNode("Foo")
	graph.graph + DemoNode("Bar")
	graph.graph + DemoNode("FooBar")
	graph.graph + DemoNode("BarFoo")

	graph.addMouseListener(control.SelectNode)
	graph.addMouseMotionListener(new control.DragNode())

	getContentPane.add(
		new JScrollPane(
			graph
		),
		BorderLayout.CENTER
	)

	pack()
	setVisible(true)
}
