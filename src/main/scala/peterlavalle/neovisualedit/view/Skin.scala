package peterlavalle.neovisualedit.view

import java.awt.{Color, Graphics2D}
import javax.swing.UIManager
import javax.swing.plaf.metal.MetalLookAndFeel

class Skin {

	def paintLink(graphics2D: Graphics2D, src: (Int, Int), dst: (Int, Int)): Unit =
		(src, dst) match {
			case ((x, y), (i, j)) =>
				graphics2D.drawLine(
					x, y,
					i, j
				)
		}

	def gridSize = 14

	def fillFore =
		UIManager.getLookAndFeel match {
			case metal: MetalLookAndFeel =>
				Color.LIGHT_GRAY
		}

	def dropFore =
		UIManager.getLookAndFeel match {
			case metal: MetalLookAndFeel =>
				Color.LIGHT_GRAY
		}

	def dropSize = 10

	def dropBack =
		UIManager.getLookAndFeel match {
			case metal: MetalLookAndFeel =>
				Color.GRAY
		}

	def linkBack =
		UIManager.getLookAndFeel match {
			case metal: MetalLookAndFeel =>
				Color.BLACK
		}

	def linkFore =
		UIManager.getLookAndFeel match {
			case metal: MetalLookAndFeel =>
				Color.DARK_GRAY
		}

	def fillBack = Color.GRAY

	def gridFore = Color.DARK_GRAY

	def gridBack = Color.WHITE

	def textFore = Color.ORANGE

	def textBack = Color.YELLOW
}
