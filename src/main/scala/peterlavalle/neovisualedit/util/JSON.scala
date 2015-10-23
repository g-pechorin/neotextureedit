package peterlavalle.neovisualedit.util

import java.io.Writer

import org.json.JSONObject

object JSON {

	implicit def wrapObject(json: JSONObject) =
		new {
			def apply(writer: Writer): Writer =
				writer.append(
					json.toString(1)
				)
		}

	def apply(pairs: (String, Any)*): JSONObject =
		pairs.foldLeft(new JSONObject()) {
			case (json, (k, v)) =>
				json.put(k, v)
		}
}
