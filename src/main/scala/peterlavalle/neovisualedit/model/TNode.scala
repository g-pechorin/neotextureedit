package peterlavalle.neovisualedit.model

trait TNode {
	val drops: List[TDrop]
	val channels: List[TChannel]
	val name: String

	override def equals(obj: scala.Any): Boolean = ???
}
