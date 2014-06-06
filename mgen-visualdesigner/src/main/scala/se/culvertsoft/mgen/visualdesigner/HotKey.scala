package se.culvertsoft.mgen.visualdesigner

import java.awt.event.InputEvent

import scala.language.implicitConversions

import javax.swing.KeyStroke

object HotKey {

  def apply() = new HotKey()
  def apply(c: Character) = new HotKey(char2vkey(c))
  def apply(vkey: Int) = new HotKey(vkey)

  def ctrl(c: Character): HotKey = new HotKey().ctrl(c)
  def shift(c: Character): HotKey = new HotKey().shift(c)
  def alt(c: Character): HotKey = new HotKey().alt(c)

  def ctrl(vkey: Int = 0): HotKey = new HotKey().ctrl(vkey)
  def shift(vkey: Int = 0): HotKey = new HotKey().shift(vkey)
  def alt(vkey: Int = 0): HotKey = new HotKey().alt(vkey)

  def char2vkey(c: Character): Int = KeyStroke.getKeyStroke(c, 0).getKeyCode()

  implicit def toStroke(s: HotKey) =
    KeyStroke.getKeyStroke(s.vkey, s.modifiers)

}

class HotKey(
  private var _vkey: Int = 0) {
  private var _modifiers: Int = 0

  private def set(vkey: Int, mods: Int): HotKey = {
    setVkey(vkey)
    addModifier(mods)
    this
  }

  def ctrl(vkey: Int = _vkey): HotKey = { set(vkey, InputEvent.CTRL_DOWN_MASK) }
  def shift(vkey: Int = _vkey): HotKey = { set(vkey, InputEvent.SHIFT_DOWN_MASK) }
  def alt(vkey: Int = _vkey): HotKey = { set(vkey, InputEvent.ALT_DOWN_MASK) }

  def ctrl(c: Character): HotKey = { ctrl(HotKey.char2vkey(c)) }
  def shift(c: Character): HotKey = { shift(HotKey.char2vkey(c)) }
  def alt(c: Character): HotKey = { alt(HotKey.char2vkey(c)) }

  def apply(c: Character): HotKey = {
    setChar(c)
    this
  }

  def apply(vkey: Int): HotKey = {
    setVkey(vkey)
    this
  }

  def vkey(): Int = _vkey
  def modifiers(): Int = _modifiers

  private def setChar(c: Character) { _vkey = HotKey.char2vkey(c) }
  private def setVkey(vkey: Int) { _vkey = vkey }
  private def addModifier(m: Int) { _modifiers |= m }

}
