package peterlavalle.neovisualedit.control

import java.awt.event.MouseEvent

import peterlavalle.neovisualedit.view
import peterlavalle.neovisualedit.view.Graph

object NodeFromMouse {

	def apply(e: MouseEvent): (Graph, Graph#Node) =
		e.getSource match {
			case graph: view.Graph =>
				val x = e.getX
				val y = e.getY

				val out =
					graph.nodes.reverse.find {
						case node =>
							node.x < x && x < node.r && node.y < y && y < node.b
					} match {
						case Some(node) =>
							node
						case None =>
							null
					}

				(graph, out)
		}

}
