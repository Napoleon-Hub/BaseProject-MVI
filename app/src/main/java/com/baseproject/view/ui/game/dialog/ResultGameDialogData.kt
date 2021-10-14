package com.baseproject.view.ui.game.dialog

import android.os.Parcel
import android.os.Parcelable

data class ResultGameDialogData(
    val title: String,
    val description: String? = null,
    val positiveBtnText: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(positiveBtnText)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ResultGameDialogData> {
        override fun createFromParcel(parcel: Parcel): ResultGameDialogData {
            return ResultGameDialogData(parcel)
        }

        override fun newArray(size: Int): Array<ResultGameDialogData?> {
            return arrayOfNulls(size)
        }
    }
}