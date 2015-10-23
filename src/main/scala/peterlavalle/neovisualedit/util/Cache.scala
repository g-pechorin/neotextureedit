package peterlavalle.neovisualedit.util

import java.util

import scala.collection.JavaConversions._


object Cache {
	def of[K, V](factory: K => V) =
		new (K => V) {
			private val marked = new util.HashMap[K, V]()
		private	val saved = new util.HashMap[K, V]()

			def flush(): Unit = {
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

			def values =
				saved.values().toList
		}
}
