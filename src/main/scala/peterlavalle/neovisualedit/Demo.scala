package peterlavalle.neovisualedit

import java.awt.BorderLayout
import javax.swing.{JFrame, JScrollPane, WindowConstants}

import peterlavalle.neovisualedit.model._
import peterlavalle.neovisualedit.util.RestoreFrame

object Demo extends JFrame("Demoooo") with App {
	RestoreFrame(this)
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

	case class Drop(node: model.TNode, name: String) extends model.TDrop

	case class Channel(node: model.TNode, name: String) extends model.TChannel

	case class DemoNode(name: String) extends TNode {
		override val drops: List[TDrop] = List(Drop(this, "foo"), Drop(this, "bar"))
		override val channels: List[TChannel] = List(Channel(this, "bey"), Channel(this, "data-cham"), Channel(this, "duta-chum"))
	}

	private val graph =
		new view.Graph(
			new model.TGraph {
				/**
				 * try to create a link between two pips and return false iff it fails
				 */
				override def tryLink(drop: model.TDrop, channel: model.TChannel): Boolean = {
					if (inputs(drop.node).contains(channel.node))
						false
					else {
						// TODO ; remove old link
						unlinkAllInto(channel)

						hardLink(drop, channel)

						true
					}
				}
			}
		)

	graph.graph + DemoNode("Foo")
	graph.graph + DemoNode("Bar")
	graph.graph + DemoNode("FooBar")
	graph.graph + DemoNode("BarFoo")

	graph.addMouseListener(control.DragDropToChannel)
	graph.addMouseListener(control.SelectNode)
	graph.addMouseMotionListener(control.DragDropToChannel)
	graph.addMouseMotionListener(new control.DragNode())

	getContentPane.add(
		new JScrollPane(graph),
		BorderLayout.CENTER
	)

	pack()
	setVisible(true)
}
