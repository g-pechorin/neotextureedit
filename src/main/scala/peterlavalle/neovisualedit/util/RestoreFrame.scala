package peterlavalle.neovisualedit.util


import java.awt.Frame
import java.awt.event._
import java.io.{File, FileWriter}
import javax.swing.JFrame

import org.json.JSONObject

import scala.io.Source

object RestoreFrame {

	private object DoLaterAdapter extends WindowAdapter {
		override def windowOpened(e: WindowEvent): Unit = {
			e.getWindow match {
				case frame: JFrame =>
					frame.removeWindowListener(this)
					RestoreFrame(frame)
			}
		}
	}

	private object SaveResize extends WindowAdapter {
		override def windowClosing(e: WindowEvent): Unit = {
			e.getSource match {
				case frame: JFrame =>
					JSON(
						"w" -> frame.getWidth,
						"h" -> frame.getHeight,
						"x" -> frame.getLocation.x,
						"y" -> frame.getLocation.y,
						"m" -> (0 != (frame.getExtendedState & Frame.MAXIMIZED_BOTH))
					)
						.write(new FileWriter(frame.getClass.getName + "-" + frame.getName + ".json"))
						.close()
			}
		}
	}

	def apply(frame: JFrame): Unit = {
		if (!frame.isVisible) {
			frame.addWindowListener(DoLaterAdapter)
		} else {
			frame.pack()

			val file = new File(frame.getClass.getName + "-" + frame.getName + ".json")

			val jsonObject: JSONObject =
				if (file.exists())
					new JSONObject(Source.fromFile(file).mkString)
				else
					new JSONObject()

			frame.setSize(
				jsonObject.optInt("w", 320),
				jsonObject.optInt("h", 240)
			)
			frame.setLocation(
				jsonObject.optInt("x", 3),
				jsonObject.optInt("y", 14)
			)

			if (jsonObject.optBoolean("m", false))
				frame.setExtendedState(frame.getExtendedState | Frame.MAXIMIZED_BOTH)

			frame.addWindowListener(SaveResize)
		}
	}

}
