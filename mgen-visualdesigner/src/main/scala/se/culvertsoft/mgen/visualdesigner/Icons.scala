package se.culvertsoft.mgen.visualdesigner

import se.culvertsoft.mgen.visualdesigner.images.MkImgIcon

object Icons {

  object TreeView {

    object Dash {
      val PROJECT_ICON = MkImgIcon.small("placeholder.png")
      val MODULE_ICON = MkImgIcon.small("list_icon_module.png")
      val CLASS_ICON = MkImgIcon.small("list_icon_class.png")
      val FIELD_ICON = MkImgIcon.small("list_icon_field.png")
    }

    object Top {
      val MOVE_UP = MkImgIcon.large("list_up.png")
      val MOVE_DOWN = MkImgIcon.large("list_down.png")
      val RENAME = MkImgIcon.large("list_rename.png")
      val FIND = MkImgIcon.large("list_find.png")
    }

  }

  object DashBoard {

    object Top {

      val ALIGN_X_RIGHT_ICON = MkImgIcon.large("arrange_align_right.png")
      val ALIGN_X_CENTER_ICON = MkImgIcon.large("arrange_align_horizontal_middle.png")
      val ALIGN_X_LEFT_ICON = MkImgIcon.large("arrange_align_left.png")

      val ALIGN_Y_TOP_ICON = MkImgIcon.large("arrange_align_top.png")
      val ALIGN_Y_CENTER_ICON = MkImgIcon.large("arrange_align_vertical_middle.png")
      val ALIGN_Y_BOTTOM_ICON = MkImgIcon.large("arrange_align_bottom.png")

      val SPREAD_EQUAL_X = MkImgIcon.large("arrange_spacing_equal_horizontal.png")
      val SPREAD_EQUAL_Y = MkImgIcon.large("arrange_spacing_equal_vertical.png")

      val RESIZE_EQUAL_X = MkImgIcon.large("arrange_same_width.png")
      val RESIZE_EQUAL_Y = MkImgIcon.large("arrange_same_height.png")

      val SPACE_OUT_X = MkImgIcon.large("arrange_spacing_horizontal.png")
      val SPACE_OUT_Y = MkImgIcon.large("arrange_spacing_vertical.png")

      val LAY_OUT = MkImgIcon.large("arrange_auto.png")

    }

    object Left {

      val GO_UP = MkImgIcon.large("view_up.png")
      val GO_BACK = MkImgIcon.large("view_back.png")
      val GO_FORWARD = MkImgIcon.large("view_forward.png")
      val RESET_VIEW = MkImgIcon.large("view_top.png")
      val VIEW_SELECTED = MkImgIcon.large("view_selected.png")
      val TOGGLE_FULL_SCREEN = MkImgIcon.large("toggle_fullscreen.png")

      val NEW_MODULE = MkImgIcon.large("add_module.png")
      val NEW_CLASS = MkImgIcon.large("add_class.png")
      val NEW_ENUM = MkImgIcon.large("add_enum.png")
      val NEW_FIELD = MkImgIcon.large("add_field.png")

    }

  }

}
