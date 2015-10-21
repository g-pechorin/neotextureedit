package peterlavalle.neovisualedit.view

import java.awt.{Color, Graphics, Graphics2D}
import java.beans.PropertyChangeEvent
import javax.swing.plaf.metal.MetalLookAndFeel
import javax.swing.{JComponent, UIManager}

import peterlavalle.neovisualedit.model
import peterlavalle.neovisualedit.model.TNode
import peterlavalle.neovisualedit.util.Cache

class Graph(var graph: model.Graph) extends JComponent {

	def nodes: List[Node] =
		nodeCache.savedValues

	def fillFore =
		UIManager.getLookAndFeel match {
			case metal: MetalLookAndFeel =>
				Color.LIGHT_GRAY
		}


	def fillBack = Color.GRAY

	def gridFore = Color.DARK_GRAY

	def gridBack = Color.WHITE

	def textFore = Color.ORANGE

	def textBack = Color.YELLOW

	private var selectedNode: Node = null

	def changeProperty[T <: AnyRef](name: String, oldValue: T, newValue: T): Unit = {
		if (oldValue == newValue) return
		val event: PropertyChangeEvent = new PropertyChangeEvent(this, name, oldValue, newValue)
		getPropertyChangeListeners.foreach {
			case listener =>
				listener.propertyChange(event)
		}
	}

	private def selectedNodeSet(newNode: Node): Unit = {
		val oldNode = selectedNode
		selectedNode = newNode
		changeProperty("selectedNode", oldNode, newNode)
	}

	def selectedNodeGet = selectedNode

	def selectedNodeClear() {
		selectedNodeSet(null)
	}

	class Node(node: model.TNode) {

		def select() {
			selectedNodeSet(this)
		}

		var x = 0
		var y = 0

		def w = 141

		def h = 32

		def r = x + w

		def b = y + h

		def paint(graphics2D: Graphics2D): (Int, Int) = {

			val isSelectedNode: Boolean = this == selectedNode

			// draw a highlight
			if (isSelectedNode) {
				graphics2D.setColor(textFore)
				graphics2D.setBackground(textBack)
				graphics2D.fill3DRect(x - 2, y - 2, w + 4, h + 4, true)
			}

			// draw the main-body
			graphics2D.setColor(fillFore)
			graphics2D.setBackground(fillBack)
			graphics2D.fill3DRect(x, y, w, h, !isSelectedNode)

			// draw the text twice at two slightly different places
			graphics2D.setColor(textFore)
			graphics2D.setBackground(textBack)
			graphics2D.drawString(node.name, x + 7, y + 23 - 1)
			graphics2D.setColor(textBack)
			graphics2D.setBackground(textFore)
			graphics2D.drawString(node.name, x + 7 - 1, y + 23)

			(x + w, y + h)
		}
	}

	class Link(link: model.Link) {
		def paint(graphics2D: Graphics2D): (Int, Int) = {
			???
		}
	}

	val nodeCache = Cache.of[TNode, Node] {
		case node =>
			new Node(node)
	}


	var grid = 14

	override def paint(g: Graphics): Unit = {
		val graphics2D: Graphics2D = g.asInstanceOf[Graphics2D]
		super.paint(graphics2D)

		graphics2D.setColor(gridFore)
		graphics2D.setBackground(gridBack)
		for (x <- 0 to (1 + (getWidth / grid)); y <- 0 to (1 + (getHeight / grid))) {
			graphics2D.drawRect(x * grid, y * grid, grid, grid)
		}

		nodeCache.markAndSweep()

		val (maxNodeX, maxNodeY) =
			graph.nodes.foldLeft((0, 0)) {
				case ((x, y), node) =>

					val husk: Node = nodeCache(node)
					val (i, j) =
						husk.paint(graphics2D)

					val (a, b) =
						node.channels.foldLeft((i, j)) {
							case ((q, r), channel) =>
								???
						}

					val (u, v) =
						node.drops.foldLeft((a, b)) {
							case ((q, r), channel) =>
								???
						}

					(Math.max(x, u), Math.max(y, v))
			}

		val (maxX, maxY) =
			graph.nodes.foldLeft((maxNodeX, maxNodeY)) {
				case ((x, y), node) =>
					graph.links(node).foldLeft((x, y)) {
						case ((i, j), link) =>
							// TODO ; cache this!
							val (a, b) =
								new Link(link).paint(graphics2D)

							(Math.max(a, i), Math.max(b, j))
					}
			}

		setSize(Math.max(getWidth, maxX), Math.max(getHeight, maxY))
		setMinimumSize(getSize)
		setMaximumSize(getSize)
	}

	setSize(320, 240)
	setMinimumSize(getSize)
	setMaximumSize(getSize)
}
