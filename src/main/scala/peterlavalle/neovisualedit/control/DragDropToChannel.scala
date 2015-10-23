package peterlavalle.neovisualedit.control

import java.awt.event.{MouseEvent, MouseListener, MouseMotionListener}

import peterlavalle.neovisualedit.view


object DragDropToChannel extends MouseMotionListener with MouseListener {
	override def mouseMoved(e: MouseEvent): Unit = {
		e.getSource match {
			case graph: view.Graph =>
				graph.draggedToChannel = null
				graph.repaint()
		}
	}

	override def mouseExited(e: MouseEvent): Unit = {
		e.getSource match {
			case graph: view.Graph =>
				graph.draggedToChannel = null
				graph.repaint()
		}
	}

	override def mouseClicked(e: MouseEvent): Unit = {
		e.getSource match {
			case graph: view.Graph =>
				graph.draggedToChannel = null
				graph.repaint()
		}
	}

	override def mouseEntered(e: MouseEvent): Unit = {
		e.getSource match {
			case graph: view.Graph =>
				graph.draggedToChannel = null
				graph.repaint()
		}
	}

	override def mousePressed(e: MouseEvent): Unit = {
		e.getSource match {
			case graph: view.Graph =>
				dropFromMouse(e) match {
					case null =>
						channelFromMouse(e) match {
							case null =>
								graph.draggedToChannel = null
								graph.repaint()

							case channel =>
								;
								graph.draggedToChannel = null
								graph.repaint()
							// TODO ; find link
							// TODO ; > none
							// TODO ; > - do nothing
							// TODO ; > found
							// TODO ; > - unlink found
							// TODO ; > - start dragging from drop to mouse
							// TODO ; > - e.consume()
						}

					case drop: view.Graph#Drop =>
						// start dragging from drop to mouse
						graph.draggedToChannel = (drop, (e.getX, e.getY))
						e.consume()
						graph.repaint()
				}
		}
	}

	override def mouseReleased(e: MouseEvent): Unit = {
		e.getSource match {
			case graph: view.Graph =>
				graph.draggedToChannel match {
					case null => ;
					case (drop, _) =>
						channelFromMouse(e) match {
							case null => ;

							case channel =>
								graph.graph.tryLink(drop.drop, channel.channel)
								e.consume()
						}
				}
				graph.draggedToChannel = null
				graph.repaint()
		}
	}

	override def mouseDragged(e: MouseEvent): Unit =
		e.getSource match {
			case graph: view.Graph =>
				graph.draggedToChannel match {
					case null =>
						;

					case (drop, _) =>
						graph.draggedToChannel = (drop, (e.getX, e.getY))
						graph.repaint()
						e.consume()
				}
		}
}
