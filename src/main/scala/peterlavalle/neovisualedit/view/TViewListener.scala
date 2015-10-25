package peterlavalle.neovisualedit.view

import peterlavalle.neovisualedit.model.TModelListener

trait TViewListener extends TModelListener {
	def onNodeMoved(graph: Graph, node: Graph#Node)
	def onNodeSelected(graph: Graph, node: Graph#Node)
}
