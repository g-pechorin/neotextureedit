package peterlavalle.neovisualedit.model

import java.util

import scala.collection.JavaConversions._

trait TGraph {

	private val listeners = new util.HashSet[TModelListener]()

	def nodeAdd(node: TNode): Unit = {
		if (nodeList.add(node))
			listeners.foreach {
				case listener =>
					listener.onNodeAdded(this, node)
			}
	}


	def listenerAdd(listener: TModelListener) =
		listeners.add(listener)

	def listenerRemove(listener: TModelListener) =
		listeners.remove(listener)

	def unlinkAllInto(channel: TChannel): Unit = {
		if (linkList.nonEmpty) (linkList.size() - 1 to 0).foreach {
			case index =>
				if (linkList(index).channel == channel)
					linkList.remove(index)
		}
	}

	/**
	 * Use this method to REALLY REALLY create a link between two pips. If the link is impossible the graph should blow-up
	 */
	def hardLink(drop: TDrop, channel: TChannel): Unit = {
		linkList.add(Link(this, drop, channel))
	}

	/**
	 * try to create a link between two pips and return false iff it fails
	 */
	def tryLink(drop: TDrop, channel: TChannel): Boolean

	private val nodeList = new util.LinkedList[TNode]()
	private val linkList = new util.LinkedList[Link]()

	def nodes: List[TNode] = nodeList.toList

	def links(node: TNode): List[Link] =
		linkList.toList.filter {
			case link =>
				link.channel.node == node
		}

	def inputs(node: TNode): Set[TNode] = {
		def recur(todo: List[TChannel], done: Set[TNode]): Set[TNode] =
			todo match {
				case Nil => done
				case null :: tail =>
					recur(tail, done)
				case head :: tail =>
					recur(
						tail ++ linkList.filter(_.channel == head).map(_.channel).filterNot(c => done.contains(c)),
						done + head.node
					)
			}

		recur(node.channels, Set())
	}
}
