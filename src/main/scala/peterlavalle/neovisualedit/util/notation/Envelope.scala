package peterlavalle.neovisualedit.util.notation

import java.util

import org.json.JSONObject

import scala.collection.JavaConversions._

class Envelope() {

	private val links =
		new util.HashMap[String, () => Any]()

	def update(name: String, data: () => Any) =
		links.put(name, data)

	def apply(name: String)(data: () => Any) =
		this (name) = data

	def toJSON =
		links.foldLeft(new JSONObject()) {
			case (json, (name, data)) =>
				data() match {
					case int: Int =>
						json.put(name, int)
				}
		}
}
