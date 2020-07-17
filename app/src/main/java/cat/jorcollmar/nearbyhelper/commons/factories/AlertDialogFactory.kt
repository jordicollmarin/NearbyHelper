package cat.jorcollmar.nearbyhelper.commons.factories

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import cat.jorcollmar.nearbyhelper.R

object AlertDialogFactory {
    fun createAlertDialog(
        context: Context, cancelable: Boolean,
        message: String, buttonText: String, buttonAction: (dialog: DialogInterface) -> Unit
    ): AlertDialog {
        return AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog)
            .setCancelable(cancelable)
            .setMessage(message)
            .setPositiveButton(buttonText) { dialog, _ -> buttonAction(dialog) }
            .create()
    }

    fun createSingleChoiceAlertDialog(
        context: Context,
        title: String,
        values: Array<String>,
        defaultValue: Int,
        buttonAction: (dialog: DialogInterface, which: Int) -> Unit
    ): AlertDialog {
        return AlertDialog.Builder(context, R.style.Theme_MaterialComponents_Light_Dialog)
            .setTitle(title)
            .setCancelable(true)
            .setSingleChoiceItems(values, defaultValue) { dialog, which ->
                buttonAction(
                    dialog,
                    which
                )
            }
            .create()
    }
}