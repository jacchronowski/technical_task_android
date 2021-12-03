package com.sliide.challenge.feature.userList.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import com.sliide.challenge.R
import com.sliide.challenge.databinding.AddUserDialogLayoutBinding

fun Context.showRemoveUserConfirmationDialog(userId: Long, onRemoveUserConfirmClick: (Long)->Unit) : Dialog {
    val dialog = AlertDialog.Builder(this)
        .setMessage(R.string.user_removing_confirmation)
        .setPositiveButton(R.string.label_yes) { dialog, _ ->
            onRemoveUserConfirmClick(userId)
            dialog.dismiss()
        }
        .setNegativeButton(R.string.label_no) { dialog, _ ->
            dialog.dismiss()
        }
        .create()

    dialog.show()
    return dialog
}

fun Context.showAddUserDialog(onAddUserConfirmClick: (String?, String?)->Unit) : Dialog {
    var dialog: Dialog? = null
    val view = LayoutInflater
        .from(this)
        .inflate(R.layout.add_user_dialog_layout, null, false)
    val binding = AddUserDialogLayoutBinding.bind(view)
    binding.emailTiet.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onConfirm(binding, onAddUserConfirmClick)
        }
        false
    }
    dialog = AlertDialog.Builder(this)
        .setView(view)
        .setMessage(R.string.add_user_description)
        .setPositiveButton(R.string.label_add_user) { _, _ ->
            onConfirm(binding, onAddUserConfirmClick)
        }
        .setNegativeButton(R.string.label_cancel) { _, _ ->
            dialog?.dismiss()
        }
        .create()
    dialog.show()
    return dialog
}

private fun onConfirm(binding: AddUserDialogLayoutBinding, onAddUserConfirmClick: (String?, String?) -> Unit) {
    val name = binding.nameTiet.text?.toString()
    val email = binding.emailTiet.text?.toString()
    onAddUserConfirmClick(name, email)
}
