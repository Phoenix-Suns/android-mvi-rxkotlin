package self.tranluunghia.mvirx.core.helper

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import self.tranluunghia.mvirx.R
import self.tranluunghia.mvirx.core.helper.extention.getAppColor

object DialogHelper {
    @JvmStatic
    fun createLoadingDlg(context: Context): AlertDialog {
        val dialog = MaterialAlertDialogBuilder(context).apply {
            setCancelable(false)
            setView(R.layout.dialog_loading)
        }.create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    @JvmStatic
    fun createAlertDlg(
        context: Context,
        title: String? = null,
        message: String?,
        cancellable: Boolean = true,
        onClose: (() -> Unit)? = null
    ): AlertDialog {

        val dialog = MaterialAlertDialogBuilder(context).apply {
            if (title == null)
                setTitle(title)
            setMessage(message)
            setCancelable(cancellable)
            setPositiveButton(context.getString(R.string.close)) { dialog, _ ->
                dialog.dismiss()
                onClose?.invoke()
            }
        }.create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(context.getAppColor(R.color.black))
        }

        return dialog
    }
}