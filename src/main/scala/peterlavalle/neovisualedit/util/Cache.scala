package peterlavalle.neovisualedit.util

import java.util

import scala.collection.JavaConversions._

class Cache[K, V](factory: K => V) {
	val marked = new util.HashMap[K, V]()
	val saved = new util.HashMap[K, V]()

	def markAndSweep(): Unit = {
		// sweep old and mark new
		marked.clear()
		marked.putAll(saved)
		saved.clear()
	}

	def apply(node: K): V = {
		if (!saved.containsKey(node)) {
			if (marked.containsKey(node)) {
				saved.put(node, marked.get(node))
			} else {
				saved.put(node, factory(node))
			}
		}
		saved.get(node)
	}

	def savedValues =
		saved.values().toList

}

object Cache {
	def of[K, V](factory: K => V) = new Cache[K, V](factory)
}
