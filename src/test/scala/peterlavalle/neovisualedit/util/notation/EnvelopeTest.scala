package peterlavalle.neovisualedit.util.notation

import junit.framework.TestCase
import org.json.JSONObject
import org.junit.Assert._

class EnvelopeTest extends TestCase {
	def testTheThing(): Unit = {
		val envelope = new Envelope()

		var foo = 8

		envelope("foo") {
			foo
		}

		assertEquals(new JSONObject().put("foo", 8).toString(1), envelope.toJSON.toString(1))

		foo = 9

		assertEquals(new JSONObject().put("foo", 9).toString(1), envelope.toJSON.toString(1))
	}
}
