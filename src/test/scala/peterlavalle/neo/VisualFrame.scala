package peterlavalle.neo

import java.awt.BorderLayout
import javax.inject.{Inject, Named}
import javax.swing.{JFrame, JMenuBar, WindowConstants}

import org.eclipse.sisu.EagerSingleton

@Named
@EagerSingleton
class VisualFrame @Inject()(visualMenuBar: JMenuBar) extends JFrame {
	setLayout(new BorderLayout())
	setVisible(true)
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
	setJMenuBar(visualMenuBar)
	pack()
}
