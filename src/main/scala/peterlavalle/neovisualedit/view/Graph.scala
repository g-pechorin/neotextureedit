package peterlavalle.neovisualedit.view

import java.awt.{Graphics, Graphics2D}
import java.beans.PropertyChangeEvent
import java.util
import javax.swing.JComponent

import peterlavalle.neovisualedit.model
import peterlavalle.neovisualedit.model.{TGraph, TModelListener, TNode}
import peterlavalle.neovisualedit.util.Cache

import scala.collection.JavaConversions._

/**
 * Long but shallow class to draw a graph.
 *
 * Override to change colors.
 *
 * Shadows model things with view things (sometimes called "husk"s)
 *
 * Should cache husks between frames
 *
 */
class Graph(val graph: model.TGraph) extends JComponent {

	private object AdaptionListener extends TModelListener {
		override def onNodeAdded(graph: TGraph, node: TNode): Unit =
			listeners.foreach {
				case listener: TViewListener =>
					listener.onNodeAdded(graph, node)
			}
	}

	private val listeners = new util.HashSet[TViewListener]()

	graph.listenerAdd(AdaptionListener)

	override def finalize(): Unit = {
		graph.listenerRemove(AdaptionListener)
		super.finalize()
	}

	def listenerAdd(listener: TViewListener) =
		listeners.add(listener)

	def listenerRemove(listener: TViewListener) =
		listeners.remove(listener)

	var skin: Skin = new Skin

	private var selectedNode: Node = null

	private def selectedNodeSet(newNode: Node): Unit = {
		val oldNode = selectedNode
		selectedNode = newNode

		listeners.foreach {
			case listener : TViewListener =>
				listener.onNodeSelected(Graph.this, newNode)
		}
	}

	def selectedNodeGet = selectedNode

	def selectedNodeClear() {
		selectedNodeSet(null)
	}


	trait TPip {
		def origin: (Int, Int)

		def distanceSq(x: Int, y: Int) = {
			origin match {
				case (i, j) =>
					val dropHalf = skin.dropSize / 2
					val u = Math.max(0, Math.abs(x - i) - dropHalf)
					val v = Math.max(0, Math.abs(y - j) - dropHalf)

					u + v
			}
		}

		def paint(graphics2D: Graphics2D): (Int, Int) = {
			val (x, y) = origin

			val dropHalf = skin.dropSize / 2

			graphics2D.setColor(skin.dropFore)
			graphics2D.setBackground(skin.dropBack)
			graphics2D.fill3DRect(
				x - dropHalf, y - dropHalf,
				skin.dropSize, skin.dropSize,
				false
			)

			(x + dropHalf, y + dropHalf)
		}
	}

	class Drop(val drop: model.TDrop, index: Int, count: Int) extends TPip {

		def origin = {
			val (x, y) = (nodeCache(drop.node).r, nodeCache(drop.node).b)

			val dropHalf = skin.dropSize / 2

			(x - ((skin.dropSize + dropHalf) * ((count - index) - 1)), y)
		}

	}

	class Channel(val channel: model.TChannel, index: Int, count: Int) extends TPip {

		def origin = {
			val (x, y) = (nodeCache(channel.node).x, nodeCache(channel.node).y)

			val dropHalf = skin.dropSize / 2

			(x + ((skin.dropSize + dropHalf) * index), y)
		}
	}

	class Node(val data: model.TNode) {
		def flush() {
			dropCache.flush()
			channelCache.flush()
		}

		def channels = data.channels.map(channelCache)

		def drops = data.drops.map(dropCache)

		def channel(data: model.TChannel): Channel = channelCache(data)

		private val channelCache = Cache.of[model.TChannel, Channel]  {
			case channel: model.TChannel =>
				require(channel.node == data)
				require (data.channels.contains(channel))
				new Channel(channel, data.channels.indexOf(channel), data.channels.size)
		}

		val dropCache = Cache.of[model.TDrop, Drop]  {
			case drop: model.TDrop =>
				require(drop.node == data, "Sanity check ; is this drop on me? " + drop + ", " + data)
				require(data.drops.contains(drop))
				new Drop(drop, data.drops.indexOf(drop), data.drops.size)
		}

		def select() {
			selectedNodeSet(this)
		}

		var _x = 0
		var _y = 0

		def x = _x

		def y = _y

		def x(value: Int): Unit = {
			xy(value, y)
		}

		def y(value: Int): Unit = {
			xy(x, value)
		}

		def x_+=(value: Int): Unit = {
			xy(x + value, y)
		}

		def y_+=(value: Int): Unit = {
			xy(x, y + value)
		}

		def xy: (Int, Int) = (x, y)

		def xy(xValue: Int, yValue: Int): Unit = {
			_x = xValue
			_y = yValue

			listeners.foreach {
				case listener: TViewListener =>
					listener.onNodeMoved(Graph.this, Node.this)
			}
		}

		def move(ijValue: (Int, Int)): Unit = {
			xy(_x + ijValue._1, _y + ijValue._2)
		}

		def w = 141

		def h = 32

		def r = x + w

		def b = y + h

		def paint(graphics2D: Graphics2D): (Int, Int) = {

			val isSelectedNode: Boolean = this == selectedNode

			// draw a highlight
			if (isSelectedNode) {
				graphics2D.setColor(skin.textFore)
				graphics2D.setBackground(skin.textBack)
				graphics2D.fill3DRect(x - 3, y - 3, w + 6, h + 6, true)
			}

			// draw the main-body
			graphics2D.setColor(skin.fillFore)
			graphics2D.setBackground(skin.fillBack)
			graphics2D.fill3DRect(x, y, w, h, !isSelectedNode)

			// draw the text twice at two slightly different places
			graphics2D.setColor(skin.textFore)
			graphics2D.setBackground(skin.textBack)
			graphics2D.drawString(data.name, x + 7, y + 23 - 1)
			graphics2D.setColor(skin.textBack)
			graphics2D.setBackground(skin.textFore)
			graphics2D.drawString(data.name, x + 7 - 1, y + 23)

			(x + w, y + h)
		}
	}

	class Link(link: model.Link) {
		def paint(graphics2D: Graphics2D) =
			nodeCache(link.channel.node) match {
				case node =>
					graphics2D.setColor(skin.linkFore)
					graphics2D.setColor(skin.linkBack)
					skin.paintLink(
						graphics2D,
						node.channel(link.channel).origin,
						nodeCache(link.drop.node).dropCache(link.drop).origin
					)
			}
	}

	val nodeCache = Cache.of[TNode, Node] {
		case node =>
			new Node(node)
	}


	override def paint(g: Graphics): Unit = {
		val graphics2D: Graphics2D = g.asInstanceOf[Graphics2D]
		super.paint(graphics2D)

		// flush anything that's been saved but not used
		nodeCache.flush()
		nodeCache.values.foreach {
			case node =>
				node.flush()
		}

		// draw the background grid
		graphics2D.setColor(skin.gridFore)
		graphics2D.setBackground(skin.gridBack)
		for (x <- 0 to (1 + (getWidth / skin.gridSize)); y <- 0 to (1 + (getHeight / skin.gridSize))) {
			graphics2D.drawRect(x * skin.gridSize, y * skin.gridSize, skin.gridSize, skin.gridSize)
		}

		// TODO ; draw the dropped-indicator

		// draw the nodes
		val (maxNodeX, maxNodeY) =
			graph.nodes.foldLeft((0, 0)) {
				case ((x, y), node) =>

					val husk: Node = nodeCache(node)
					val (i, j) =
						husk.paint(graphics2D)

					val (a, b) =
						husk.channels.zipWithIndex.foldLeft((i, j)) {
							case ((q, r), (channel, index)) =>

								val (a, b) = channel.paint(graphics2D)

								(Math.max(a, q), Math.max(b, r))
						}

					val (u, v) =
						husk.drops.zipWithIndex.foldLeft((a, b)) {
							case ((q, r), (drop, index)) =>

								val (a, b) = drop.paint(graphics2D)

								(Math.max(a, q), Math.max(b, r))
						}

					(Math.max(x, u), Math.max(y, v))
			}

		// draw the links
		graph.nodes.foreach {
			case node =>
				graph.links(node).foreach {
					case link =>
						// TODO ; cache this!
						new Link(link).paint(graphics2D)
				}
		}

		// draw the dragged-to-channel
		draggedToChannel match {
			case null => ;
			case (src, dst) =>
				graphics2D.setColor(skin.textBack)
				skin.paintLink(graphics2D, src.origin, dst)
		}

		// adjust the size
		setSize(Math.max(getWidth, maxNodeX), Math.max(getHeight, maxNodeY))
		setMinimumSize(getSize)
		setMaximumSize(getSize)
	}

	var draggedToChannel: (Graph#Drop, (Int, Int)) = null

	setSize(320, 240)
	setMinimumSize(getSize)
	setMaximumSize(getSize)
}
