package peterlavalle.neovisualedit.control

import java.awt.event.{MouseEvent, MouseListener}

object SelectNode extends MouseListener {
	override def mouseExited(e: MouseEvent): Unit = {}

	override def mouseClicked(e: MouseEvent): Unit = {
		nodeFromMouse(e) match {
			case (graph, null) =>
				graph.selectedNodeClear
				graph.repaint()
			case (graph, node) if node != null =>
				node.select
				e.consume()
				graph.repaint()

		}
	}

	override def mouseEntered(e: MouseEvent): Unit = {}

	override def mousePressed(e: MouseEvent): Unit = {}

	override def mouseReleased(e: MouseEvent): Unit = {}
}
