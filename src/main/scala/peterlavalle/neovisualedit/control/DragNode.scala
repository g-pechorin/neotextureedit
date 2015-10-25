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

		if (!e.isConsumed)
			nodeFromMouse(e) match {
				case (graph, node) if node != null =>
					node.xy(node.x + i, node.y + j)

					(if (node.x < 0) -node.x else 0, if (node.y < 0) -node.y else 0) match {
						case (0, 0) => ;
						case movement =>
							graph.nodeCache.values.foreach {
								case other =>
									other move movement
							}
					}
					require(0 <= node.x)
					require(0 <= node.y)

					graph.repaint()
					e.consume()
				case _ =>
					;
			}
	}
}
