package com.sliide.challenge.feature.userList.plugins

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.sliide.challenge.architecture.Plugin
import com.sliide.challenge.feature.userList.utils.showAddUserDialog
import com.sliide.challenge.feature.userList.utils.showRemoveUserConfirmationDialog
import com.sliide.challenge.feature.userList.viewModel.UserListRouting
import com.sliide.challenge.ext.isShowing

class UserListRoutingPlugin(
    private val context: Context,
    private val userListRouting: UserListRouting,
    private val lifecycleOwner: LifecycleOwner
) : Plugin {

    private var addUserDialog: Dialog? = null
    private var removeUserDialog: Dialog? = null

    override fun applyPlugin(view: View) {
        userListRouting.showAddUserDialogEvent.observe(lifecycleOwner) {
            addUserDialog = context.showAddUserDialog(userListRouting::onAddUserConfirmClick)
        }
        userListRouting.showRemoveUserDialogEvent.observe(lifecycleOwner) { userId ->
            userId?.let {
                removeUserDialog = context.showRemoveUserConfirmationDialog(
                    it,
                    userListRouting::onRemoveUserConfirmClick
                )
            }
        }
        userListRouting.dismissAddUserDialogEvent.observe(lifecycleOwner) {
            addUserDialog?.dismiss()
        }
    }

    override fun clearPlugin() {
        if (addUserDialog.isShowing()) {
            addUserDialog?.dismiss()
        }
        if (removeUserDialog.isShowing()) {
            removeUserDialog?.dismiss()
        }
    }
}
