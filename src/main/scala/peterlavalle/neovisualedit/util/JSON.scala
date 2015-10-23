package peterlavalle.neovisualedit.util

import org.json.JSONObject

object JSON {
	def apply(pairs: (String, Any)*): JSONObject =
		pairs.foldLeft(new JSONObject()) {
			case (json, (k, v)) =>
				json.put(k, v)
		}
}
