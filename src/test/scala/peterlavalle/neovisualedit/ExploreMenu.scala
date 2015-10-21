package peterlavalle.neovisualedit

import javax.swing.{JMenu, JMenuBar, JMenuItem}

object ExploreMenu {
	def apply(bar: JMenuBar, bind: List[String], name: String): JMenuItem =
		bind match {
			case root :: path =>
				ExploreMenu(
					(0 until bar.getMenuCount).toList.map(bar.getMenu).filter {
						case menu: JMenuItem => root == menu.getText
						case _ => false
					} match {
						case List(menu: JMenu) =>
							menu
						case Nil =>
							new JMenu() {
								setText(root)
								bar.add(this)
							}
					},
					path,
					name
				)
		}

	def apply(menu: JMenu, path: List[String], name: String): JMenuItem =
		path match {
			case List("") | Nil =>
				(0 until menu.getItemCount).toList.map(menu.getItem).filter {
					case null => false
					case item => name == item.getText
					case _ => false
				} match {
					case List(item: JMenuItem) => item
					case Nil =>
						val item = new JMenuItem(name)
						menu.add(item)
						item
				}
		}

}
