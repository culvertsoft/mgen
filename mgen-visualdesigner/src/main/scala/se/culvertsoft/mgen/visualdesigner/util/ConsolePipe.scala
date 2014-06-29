package se.culvertsoft.mgen.visualdesigner.util;

import java.awt.Color
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsolePipe(val target: ConsoleTarget) {

  def redirectOut(textColor: Color, printStream: PrintStream) {
    val cos = new ConsoleOutputStream(textColor, printStream);
    System.setOut(new PrintStream(cos, true));
  }

  def redirectErr(textColor: Color, printStream: PrintStream) {
    val cos = new ConsoleOutputStream(textColor, printStream);
    System.setErr(new PrintStream(cos, true));
  }

  class ConsoleOutputStream(
    val color: Color,
    val printStream: PrintStream) extends ByteArrayOutputStream {
    val EOL = System.getProperty("line.separator");
    val buffer = new StringBuilder(80);

    override def flush() {
      val message = toString();
      if (message.length() != 0) {
        handleAppend(message);
        reset();
      }
    }

    def handleAppend(message: String) {
      if (target.getLength() == 0)
        buffer.setLength(0);
      if (EOL.equals(message)) {
        buffer.append(message);
      } else {
        buffer.append(message);
        clearBuffer();
      }
    }

    def clearBuffer() {
      val line = buffer.toString();

      target.append(line, color);

      if (printStream != null) {
        printStream.print(line);
      }

      buffer.setLength(0);
    }
  }
}