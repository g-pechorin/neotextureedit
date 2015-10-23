package peterlavalle.neovisualedit

import java.awt.event.MouseEvent

import peterlavalle.neovisualedit.view.Graph


package object control {
	def channelFromMouse(e: MouseEvent): Graph#Channel =
		e.getSource match {
			case graph: view.Graph =>
				graph.nodeCache.values.flatMap {
					case node =>
						node.channels.filter {
							case channel =>
								channel.distanceSq(e.getX, e.getY) < graph.skin.dropSize
						}
				} match {
					case Nil => null
					case head :: _ =>
						head
				}
		}

	def dropFromMouse(e: MouseEvent): Graph#Drop =
		e.getSource match {
			case graph: view.Graph =>
				graph.nodeCache.values.flatMap {
					case node =>
						node.dropCache.values.filter {
							case drop =>
								drop.distanceSq(e.getX, e.getY) < graph.skin.dropSize
						}
				} match {
					case Nil => null
					case head :: _ =>
						head
				}
		}

	def nodeFromMouse(e: MouseEvent): (Graph, Graph#Node) =
		e.getSource match {
			case graph: view.Graph =>
				val x = e.getX
				val y = e.getY

				val out =
					graph.nodeCache.values.reverse.find {
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
