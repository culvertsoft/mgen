package se.culvertsoft.mgen.visualdesigner

import se.culvertsoft.mgen.visualdesigner.images.MkImgIcon

object Icons {

  object TreeView {

    object Dash {
      val PROJECT_ICON = MkImgIcon.small("placeholder.png")
      val MODULE_ICON = MkImgIcon.small("v3/list_icon_module.png")
      val CLASS_ICON = MkImgIcon.small("v3/list_icon_class.png")
      val FIELD_ICON = MkImgIcon.small("v3/list_icon_field.png")
    }

    object Top {
      val MOVE_UP = MkImgIcon.large("v3/list_up.png")
      val MOVE_DOWN = MkImgIcon.large("v3/list_down.png")
      val RENAME = MkImgIcon.large("rename.png")
      val FIND = MkImgIcon.large("v3/list_find.png")
    }

  }

  object DashBoard {

    object Top {

      val ALIGN_X_RIGHT_ICON = MkImgIcon.large("v3/arrange_align_right.png")
      val ALIGN_X_CENTER_ICON = MkImgIcon.large("v3/arrange_align_horizontal_middle.png")
      val ALIGN_X_LEFT_ICON = MkImgIcon.large("v3/arrange_align_left.png")

      val ALIGN_Y_TOP_ICON = MkImgIcon.large("v3/arrange_align_top.png")
      val ALIGN_Y_CENTER_ICON = MkImgIcon.large("v3/arrange_align_vertical_middle.png")
      val ALIGN_Y_BOTTOM_ICON = MkImgIcon.large("v3/arrange_align_bottom.png")

      val SPREAD_EQUAL_X = MkImgIcon.large("v3/arrange_spacing_equal_horizontal.png")
      val SPREAD_EQUAL_Y = MkImgIcon.large("v3/arrange_spacing_equal_vertical.png")

      val RESIZE_EQUAL_X = MkImgIcon.large("v3/arrange_same_width.png")
      val RESIZE_EQUAL_Y = MkImgIcon.large("v3/arrange_same_height.png")

      val SPACE_OUT_X = MkImgIcon.large("v3/arrange_spacing_horizontal.png")
      val SPACE_OUT_Y = MkImgIcon.large("v3/arrange_spacing_vertical.png")

      val LAY_OUT = MkImgIcon.large("v3/arrange_auto.png")

    }

    object Left {

      val GO_UP = MkImgIcon.large("v3/view_up.png")
      val GO_BACK = MkImgIcon.large("v3/view_back.png")
      val GO_FORWARD = MkImgIcon.large("v3/view_forward.png")
      val RESET_VIEW = MkImgIcon.large("v3/view_top.png")
      val VIEW_SELECTED = MkImgIcon.large("v3/view_selected.png")
      val TOGGLE_FULL_SCREEN = MkImgIcon.large("fullscreen.png")

      val NEW_MODULE = MkImgIcon.large("v3/add_module.png")
      val NEW_CLASS = MkImgIcon.large("v3/add_class.png")
      val NEW_ENUM = MkImgIcon.large("v3/add_enum.png")
      val NEW_FIELD = MkImgIcon.large("v3/add_field.png")

    }

  }

  object MainMenu {

    object File {
      val NEW = MkImgIcon.small("new-16.png")
      val LOAD = MkImgIcon.small("open-16.png")
      val SAVE = MkImgIcon.small("save-16.png")
      val EXIT = MkImgIcon.small("exit-16.png")
    }

    object Edit {

      val UNDO = MkImgIcon.small("undo.png")
      val REDO = MkImgIcon.small("redo.png")

      val CUT = MkImgIcon.small("cut.png")
      val COPY = MkImgIcon.small("copy.png")
      val PASTE = MkImgIcon.small("paste.png")

      val FIND = MkImgIcon.small("v3/list_find.png")

      val SELECT_ALL = MkImgIcon.small("selectAll.png")
      val DESELECT_ALL = MkImgIcon.small("script_delete.png")

      val RENAME = MkImgIcon.small("rename.png")
      val DELETE = MkImgIcon.small("delete-16.png")
    }

    object View {
      val GO_UP = MkImgIcon.small("up.png")
      val GO_BACK = MkImgIcon.small("left.png")
      val GO_FORWARD = MkImgIcon.small("right.png")
      val SET_VIEW_ROOT = MkImgIcon.small("v3/view_selected.png")
      val FULL_SREEN = MkImgIcon.small("fullscreen.png")
      val RESET_VIEW = MkImgIcon.small("resetView.png")

      val ICONS_ONLY_VIEW = MkImgIcon.small("find.png")

      val OPEN_CONSOLE = MkImgIcon.small("console.png")
      val REBUILD_VIEW = MkImgIcon.small("rebuildView.png")
    }

    object Tools {

      val NEW_PROJECT = MkImgIcon.small("placeholder.png")
      val NEW_MODULE = MkImgIcon.small("folder-16.png")
      val NEW_TYPE = MkImgIcon.small("folder_table.png")
      val NEW_FIELD = MkImgIcon.small("add.png")

      val ALIGN_X_RIGHT_ICON = MkImgIcon.small("align-right.png")
      val ALIGN_X_CENTER_ICON = MkImgIcon.small("align-center-vertical.png")
      val ALIGN_X_LEFT_ICON = MkImgIcon.small("align-left.png")

      val ALIGN_Y_TOP_ICON = MkImgIcon.small("align-top.png")
      val ALIGN_Y_CENTER_ICON = MkImgIcon.small("align-center-horizontal.png")
      val ALIGN_Y_BOTTOM_ICON = MkImgIcon.small("align-bottom.png")

      val SPREAD_EQUAL_X = MkImgIcon.small("distribute-horizontal.png")
      val SPREAD_EQUAL_Y = MkImgIcon.small("distribute-vertical.png")

      val RESIZE_EQUAL_X = MkImgIcon.small("samew.png")
      val RESIZE_EQUAL_Y = MkImgIcon.small("sameh.png")

      val SPACE_OUT_X = MkImgIcon.small("spacew.png")
      val SPACE_OUT_Y = MkImgIcon.small("spaceh.png")

      val LAY_OUT = MkImgIcon.small("distribute.png")

      val GENERATE = MkImgIcon.small("generate.png")

    }

    object Settings {
      val GENERATION = MkImgIcon.small("application_form_edit.png")
    }

    object About {
      val HELP = MkImgIcon.small("help.png")
      val ABOUT = MkImgIcon.small("information.png")
    }

  }

}
