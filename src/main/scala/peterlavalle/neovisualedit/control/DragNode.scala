package peterlavalle.neovisualedit.control

import java.awt.event.{MouseEvent, MouseMotionListener}

class DragNode() extends MouseMotionListener {
	var oldX = 0
	var oldY = 0
	var newX = 0
	var newY = 0

	def i = newX - oldX

	def j = newY - oldY

	def x = newX

	def y = newY

	private def updateDelta(e: MouseEvent) = {
		oldX = newX
		oldY = newY

		newX = e.getX
		newY = e.getY
	}

	override def mouseMoved(e: MouseEvent): Unit = {
		updateDelta(e)
	}

	override def mouseDragged(e: MouseEvent): Unit = {
		updateDelta(e)
		NodeFromMouse(e) match {
			case (graph, node) if node != null =>
				node.x += i
				node.y += j

				if (node.x < 0) {
					val i = -node.x
					graph.nodes.foreach {
						case other =>
							other.x += i
					}
					require(0 == node.x)
				}

				if (node.y < 0) {
					val i = -node.y
					graph.nodes.foreach {
						case other =>
							other.y += i
					}
					require(0 == node.y)
				}

				graph.repaint()
				e.consume()
			case _ =>
				;
		}
	}
}
