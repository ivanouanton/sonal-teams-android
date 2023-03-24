package com.waveneuro.ui.session.precautions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.waveneuro.R

class PrecautionsBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.dialog_precautions, container,
            false
        )
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    companion object {
        fun newInstance(): PrecautionsBottomSheet {
            return PrecautionsBottomSheet()
        }
    }
}