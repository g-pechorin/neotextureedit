package peterlavalle.neovisualedit.model

trait TModelListener {
	def onNodeAdded(graph: TGraph, node: TNode)
}
