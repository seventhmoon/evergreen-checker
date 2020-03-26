package app.evergreen.ui

import androidx.fragment.app.DialogFragment

typealias DialogOpener = ((dialogFragment: DialogFragment, tag: String) -> Unit)
