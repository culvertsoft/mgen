package se.culvertsoft.mgen.visualdesigner.images

import javax.swing.ImageIcon

object MkImgIcon {

   val imgPath = "/se/culvertsoft/mgen/visualdesigner/images/"

   def native(path: String): ImageIcon = {
      new ImageIcon(classOf[MkImgIcon].getResource(imgPath + path))
   }

   def small(path: String): ImageIcon = {
      val img = native(path).getImage();
      val newimg = img.getScaledInstance(10, 10, java.awt.Image.SCALE_SMOOTH);
      new ImageIcon(newimg);
   }

   def large(path: String): ImageIcon = {
      val img = native(path).getImage();
      val newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
      new ImageIcon(newimg);
   }
   
   def huge(path: String): ImageIcon = {
      val img = native(path).getImage();
      val newimg = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
      new ImageIcon(newimg);
   }

}

abstract class MkImgIcon
