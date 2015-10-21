package peterlavalle.neovisualedit.model

import java.util

import scala.collection.JavaConversions._

class Graph {
	def +(node: TNode): Unit =
		nodeList.add(node)


	def link(drop: TDrop, channel: TChannel): Unit = {
		linkList.add(Link(this, drop, channel))
	}

	private val nodeList = new util.LinkedList[TNode]()
	private val linkList = new util.LinkedList[Link]()

	def nodes: List[TNode] = nodeList.toList

	def links(node: TNode): List[Link] =
		linkList.toList.filter {
			case link =>
				link.channel.node == node
		}
}
